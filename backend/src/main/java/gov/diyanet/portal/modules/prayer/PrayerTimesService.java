package gov.diyanet.portal.modules.prayer;

import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.config.CacheConfig;
import gov.diyanet.portal.integrations.AladhanClient;
import gov.diyanet.portal.integrations.AladhanClient.DaySnapshot;
import gov.diyanet.portal.integrations.AladhanClient.Snapshot;
import gov.diyanet.portal.modules.prayer.PrayerTimesResponse.CalendarResponse;
import gov.diyanet.portal.modules.prayer.PrayerTimesResponse.Day;
import gov.diyanet.portal.modules.prayer.PrayerTimesResponse.NextPrayer;
import gov.diyanet.portal.modules.prayer.PrayerTimesResponse.Times;
import gov.diyanet.portal.modules.province.District;
import gov.diyanet.portal.modules.province.DistrictRepository;
import gov.diyanet.portal.modules.province.Province;
import gov.diyanet.portal.modules.province.ProvinceRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrayerTimesService {

	public static final String ALADHAN_DISCLAIMER =
			"Vakitler Aladhan servisi üzerinden Diyanet hesaplama yöntemi (method 13) ile alınmıştır. "
					+ "Resmî Diyanet uygulaması değildir; ibadet için teyit ediniz.";
	public static final String FALLBACK_DISCLAIMER =
			"Harici vakit servisine ulaşılamadı. Yaklaşık astronomik formül kullanıldı. "
					+ "İbadet için resmî kaynak kullanınız.";
	public static final String LOCAL_DISCLAIMER =
			"Vakitler yerel kayıtlı tablodan okundu. İbadet için resmî kaynak kullanınız.";
	private static final ZoneId TR = ZoneId.of("Europe/Istanbul");

	private final PrayerTimeRepository prayerTimeRepository;
	private final ProvinceRepository provinceRepository;
	private final DistrictRepository districtRepository;
	private final AladhanClient aladhanClient;

	@Cacheable(value = CacheConfig.PRAYER_TIMES, key = "#provinceSlug + '-' + #districtSlug + '-' + #date")
	@Transactional(readOnly = true)
	public PrayerTimesResponse getTimes(String provinceSlug, String districtSlug, LocalDate date) {
		String slug = (provinceSlug == null || provinceSlug.isBlank()) ? "ankara" : provinceSlug;
		LocalDate target = date == null ? LocalDate.now(TR) : date;
		Province province = provinceRepository.findBySlug(slug)
				.orElseThrow(() -> new NotFoundException("İl bulunamadı: " + slug));
		District district = resolveDistrict(province, districtSlug);
		String cityLabel = district != null ? district.getName() : province.getName();
		String districtKey = district != null ? district.getSlug() : null;

		var remote = aladhanClient.timings(province.getLat(), province.getLng(), cityLabel, target);
		if (remote.isPresent()) {
			Snapshot snap = remote.get();
			Times times = toTimes(snap);
			return build(province.getSlug(), districtKey, cityLabel, target, snap.hijriDay(), snap.hijriMonth(),
					snap.hijriYear(), false, times, "aladhan", ALADHAN_DISCLAIMER);
		}

		PrayerTime row = prayerTimeRepository.findByProvince_IdAndDate(province.getId(), target).orElse(null);
		if (row != null) {
			Times times = new Times(row.getImsak(), row.getGunes(), row.getOgle(), row.getIkindi(), row.getAksam(),
					row.getYatsi());
			HijriParts hijri = approxHijriParts(target);
			return build(province.getSlug(), districtKey, cityLabel, target, hijri.day(), hijri.month(), hijri.year(),
					true, times, "local", LOCAL_DISCLAIMER);
		}

		Times generated = generate(province.getLat(), province.getLng(), target);
		HijriParts hijri = approxHijriParts(target);
		return build(province.getSlug(), districtKey, cityLabel, target, hijri.day(), hijri.month(), hijri.year(), true,
				generated, "fallback", FALLBACK_DISCLAIMER);
	}

	public PrayerTimesResponse getTimes(String provinceSlug, LocalDate date) {
		return getTimes(provinceSlug, null, date);
	}

	@Cacheable(value = CacheConfig.PRAYER_CALENDAR, key = "#provinceSlug + '-' + #districtSlug + '-' + #year + '-' + #month")
	@Transactional(readOnly = true)
	public CalendarResponse getCalendar(String provinceSlug, String districtSlug, Integer year, Integer month) {
		String slug = (provinceSlug == null || provinceSlug.isBlank()) ? "ankara" : provinceSlug;
		YearMonth ym = YearMonth.of(
				year == null ? LocalDate.now(TR).getYear() : year,
				month == null ? LocalDate.now(TR).getMonthValue() : month);
		Province province = provinceRepository.findBySlug(slug)
				.orElseThrow(() -> new NotFoundException("İl bulunamadı: " + slug));
		District district = resolveDistrict(province, districtSlug);
		String cityLabel = district != null ? district.getName() : province.getName();
		String districtKey = district != null ? district.getSlug() : null;

		List<DaySnapshot> remote = aladhanClient.calendar(province.getLat(), province.getLng(), cityLabel, ym.getYear(),
				ym.getMonthValue());
		if (!remote.isEmpty()) {
			List<Day> days = remote.stream()
					.map(d -> new Day(d.date(), hijriLabel(d.hijriDay(), d.hijriMonth(), d.hijriYear(), false),
							d.hijriDay(), d.hijriMonth(), d.hijriYear(),
							new Times(d.imsak(), d.gunes(), d.ogle(), d.ikindi(), d.aksam(), d.yatsi())))
					.toList();
			return new CalendarResponse(province.getSlug(), districtKey, cityLabel, ym.getYear(), ym.getMonthValue(),
					days, "aladhan", ALADHAN_DISCLAIMER);
		}

		List<Day> days = new ArrayList<>();
		for (int i = 1; i <= ym.lengthOfMonth(); i++) {
			LocalDate date = ym.atDay(i);
			Times times = generate(province.getLat(), province.getLng(), date);
			HijriParts hijri = approxHijriParts(date);
			days.add(new Day(date, hijriLabel(hijri.day(), hijri.month(), hijri.year(), true), hijri.day(),
					hijri.month(), hijri.year(), times));
		}
		return new CalendarResponse(province.getSlug(), districtKey, cityLabel, ym.getYear(), ym.getMonthValue(), days,
				"fallback", FALLBACK_DISCLAIMER);
	}

	/**
	 * Approximate solar schedule used only when Aladhan is unavailable.
	 */
	public Times generate(Double lat, Double lng, LocalDate date) {
		double latitude = lat == null ? 39.93 : lat;
		double longitude = lng == null ? 32.85 : lng;
		int day = date.getDayOfYear();
		double decl = 23.45 * Math.sin(Math.toRadians(360.0 / 365.0 * (day - 81)));
		double latRad = Math.toRadians(latitude);
		double declRad = Math.toRadians(decl);
		double cosHa = -Math.tan(latRad) * Math.tan(declRad);
		cosHa = Math.max(-0.98, Math.min(0.98, cosHa));
		double hourAngle = Math.toDegrees(Math.acos(cosHa));
		double lngOffset = (45.0 - longitude) / 15.0;
		double sunrise = 12.0 - hourAngle / 15.0 + lngOffset;
		double sunset = 12.0 + hourAngle / 15.0 + lngOffset;
		double noon = 12.0 + lngOffset;
		LocalTime gunes = hourToTime(sunrise);
		LocalTime aksam = hourToTime(sunset);
		LocalTime ogle = hourToTime(noon);
		LocalTime imsak = hourToTime(sunrise - 1.35);
		LocalTime ikindi = hourToTime(noon + (sunset - noon) * 0.52);
		LocalTime yatsi = hourToTime(sunset + 1.35);
		return new Times(imsak, gunes, ogle, ikindi, aksam, yatsi);
	}

	NextPrayer nextPrayer(Times times, LocalDate date) {
		Map<String, LocalTime> map = ordered(times);
		ZonedDateTime now = ZonedDateTime.now(TR);
		for (var entry : map.entrySet()) {
			ZonedDateTime zdt = LocalDateTime.of(date, entry.getValue()).atZone(TR);
			if (zdt.isAfter(now)) {
				long remaining = Math.max(0, Duration.between(now, zdt).getSeconds());
				return new NextPrayer(entry.getKey(), entry.getValue(), remaining);
			}
		}
		LocalDateTime tomorrowImsak = LocalDateTime.of(date.plusDays(1), times.imsak());
		long remaining = Math.max(0, Duration.between(now, tomorrowImsak.atZone(TR)).getSeconds());
		return new NextPrayer("imsak", times.imsak(), remaining);
	}

	String currentPrayer(Times times, LocalDate date) {
		Map<String, LocalTime> map = ordered(times);
		ZonedDateTime now = ZonedDateTime.now(TR);
		String last = "yatsi";
		for (var entry : map.entrySet()) {
			ZonedDateTime zdt = LocalDateTime.of(date, entry.getValue()).atZone(TR);
			if (!zdt.isAfter(now)) {
				last = entry.getKey();
			}
		}
		if (now.toLocalDate().isAfter(date)) {
			return "yatsi";
		}
		if (now.toLocalDate().isBefore(date)) {
			return null;
		}
		return last;
	}

	public static String approxHijri(LocalDate gregorian) {
		HijriParts parts = approxHijriParts(gregorian);
		return hijriLabel(parts.day(), parts.month(), parts.year(), true);
	}

	static HijriParts approxHijriParts(LocalDate gregorian) {
		int jd = (int) gregorian.toEpochDay() + 2440588;
		int l = jd - 1948440 + 10632;
		int n = (l - 1) / 10631;
		l = l - 10631 * n + 354;
		int j = ((10985 - l) / 5316) * ((50 * l) / 17719) + (l / 5670) * ((43 * l) / 15238);
		l = l - ((30 - j) / 15) * ((17719 * j) / 50) - (j / 16) * ((15238 * j) / 43) + 29;
		int month = (24 * l) / 709;
		int day = l - (709 * month) / 24;
		int year = 30 * n + j - 30;
		int mIndex = Math.max(1, Math.min(month, 12));
		return new HijriParts(Math.max(1, day), mIndex, year);
	}

	private static String hijriLabel(int day, int month, int year, boolean approx) {
		return day + "-" + month + "-" + year + (approx ? " ~" : "");
	}

	record HijriParts(int day, int month, int year) {
	}

	private PrayerTimesResponse build(
			String province,
			String district,
			String cityLabel,
			LocalDate date,
			int hijriDay,
			int hijriMonth,
			int hijriYear,
			boolean approx,
			Times times,
			String source,
			String disclaimer) {
		return new PrayerTimesResponse(
				province,
				district,
				cityLabel,
				date,
				hijriLabel(hijriDay, hijriMonth, hijriYear, approx),
				hijriDay,
				hijriMonth,
				hijriYear,
				times,
				nextPrayer(times, date),
				currentPrayer(times, date),
				source,
				disclaimer);
	}

	private District resolveDistrict(Province province, String districtSlug) {
		if (districtSlug == null || districtSlug.isBlank()) {
			return null;
		}
		return districtRepository.findByProvince_IdAndSlug(province.getId(), districtSlug).orElse(null);
	}

	private static Times toTimes(Snapshot snap) {
		return new Times(snap.imsak(), snap.gunes(), snap.ogle(), snap.ikindi(), snap.aksam(), snap.yatsi());
	}

	private static Map<String, LocalTime> ordered(Times times) {
		Map<String, LocalTime> map = new LinkedHashMap<>();
		map.put("imsak", times.imsak());
		map.put("gunes", times.gunes());
		map.put("ogle", times.ogle());
		map.put("ikindi", times.ikindi());
		map.put("aksam", times.aksam());
		map.put("yatsi", times.yatsi());
		return map;
	}

	private static LocalTime hourToTime(double hour) {
		double wrapped = (hour % 24 + 24) % 24;
		int h = (int) Math.floor(wrapped);
		int m = (int) Math.round((wrapped - h) * 60);
		if (m == 60) {
			h = (h + 1) % 24;
			m = 0;
		}
		return LocalTime.of(h, m);
	}
}
