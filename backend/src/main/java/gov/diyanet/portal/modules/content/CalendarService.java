package gov.diyanet.portal.modules.content;

import gov.diyanet.portal.config.CacheConfig;
import gov.diyanet.portal.integrations.AladhanClient;
import gov.diyanet.portal.integrations.AladhanClient.HijriDate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {

	private static final ZoneId TR = ZoneId.of("Europe/Istanbul");
	private static final String NOTE =
			"Hicrî tarihler Aladhan dönüşümü iledir. Resmî Diyanet takvimi ile bir gün kayması olabilir.";

	private static final List<Spec> SPECS = List.of(
			new Spec("Hicri Yılbaşı", 1, 1, "HICRI"),
			new Spec("Aşure Günü", 1, 10, "OZEL"),
			new Spec("Mevlid Kandili", 3, 12, "KANDIL"),
			new Spec("Miraç Kandili", 7, 27, "KANDIL"),
			new Spec("Berat Kandili", 8, 15, "KANDIL"),
			new Spec("Ramazan Başlangıcı", 9, 1, "RAMAZAN"),
			new Spec("Kadir Gecesi", 9, 27, "KANDIL"),
			new Spec("Ramazan Bayramı", 10, 1, "BAYRAM"),
			new Spec("Arefe", 12, 9, "HAC"),
			new Spec("Kurban Bayramı", 12, 10, "BAYRAM"));

	private final AladhanClient aladhanClient;

	@Cacheable(CacheConfig.RELIGIOUS_DAYS)
	public List<ReligiousDay> religiousDays() {
		if (!aladhanClient.enabled()) {
			return List.of();
		}
		LocalDate today = LocalDate.now(TR);
		Optional<HijriDate> hijri = aladhanClient.hijriFor(today);
		if (hijri.isEmpty()) {
			return List.of();
		}
		int year = hijri.get().year();
		List<ReligiousDay> days = new ArrayList<>();
		for (int y : List.of(year, year + 1)) {
			for (Spec spec : SPECS) {
				aladhanClient.gregorianFor(spec.day(), spec.month(), y)
						.filter(date -> !date.isBefore(today.minusDays(2)))
						.filter(date -> !date.isAfter(today.plusMonths(14)))
						.ifPresent(date -> days.add(new ReligiousDay(
								spec.title(),
								date,
								"%d-%02d-%02d".formatted(y, spec.month(), spec.day()),
								spec.type(),
								NOTE)));
			}
		}
		days.sort(Comparator.comparing(ReligiousDay::gregorianDate));
		return days.stream().distinct().toList();
	}

	public record ReligiousDay(String title, LocalDate gregorianDate, String hijriDate, String type, String note) {
	}

	private record Spec(String title, int month, int day, String type) {
	}
}
