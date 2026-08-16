package gov.diyanet.portal.modules.quran;

import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.config.CacheConfig;
import gov.diyanet.portal.integrations.AlQuranCloudClient;
import gov.diyanet.portal.integrations.AlQuranCloudClient.RemoteAyah;
import gov.diyanet.portal.integrations.AlQuranCloudClient.RemoteSearchHit;
import gov.diyanet.portal.integrations.AlQuranCloudClient.RemoteSurahDetail;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuranService {

	public static final String REMOTE_NOTE =
			"Arapça metin: Uthmânî mushaf (alquran.cloud). Türkçe meal: Diyanet İşleri Başkanlığı "
					+ "(tr.diyanet, alquran.cloud aracılığıyla). İbadet için matbu mushaf esas alınmalıdır.";
	public static final String LOCAL_NOTE =
			"Yerel katalogdaki âyetler gösteriliyor. Tam sure metni için alquran.cloud kaynağına erişilemedi.";

	private final QuranSurahRepository surahRepository;
	private final QuranAyahRepository ayahRepository;
	private final AlQuranCloudClient alQuranCloudClient;

	@Cacheable(CacheConfig.QURAN_SURAHS)
	@Transactional(readOnly = true)
	public List<SurahSummary> listSurahs() {
		return surahRepository.findAllByOrderByNumberAsc().stream()
				.map(this::toSummary)
				.toList();
	}

	@Cacheable(value = CacheConfig.QURAN_SURAH, key = "#number")
	@Transactional(readOnly = true)
	public SurahDetail getSurah(int number) {
		QuranSurah surah = surahRepository.findByNumber(number)
				.orElseThrow(() -> new NotFoundException("Sure bulunamadı: " + number));
		var remote = alQuranCloudClient.getSurah(number);
		if (remote.isPresent() && !remote.get().ayahs().isEmpty()) {
			RemoteSurahDetail detail = remote.get();
			List<AyahDto> ayahs = detail.ayahs().stream().map(this::toAyah).toList();
			return new SurahDetail(
					surah.getNumber(),
					surah.getNameAr(),
					surah.getNameTr(),
					surah.getNameEn(),
					surah.getAyahCount(),
					surah.getRevelationType(),
					surah.getJuzStart(),
					ayahs,
					REMOTE_NOTE,
					"alquran");
		}
		List<AyahDto> ayahs = ayahRepository.findBySurah_IdOrderByNumberAsc(surah.getId()).stream()
				.map(a -> new AyahDto(a.getNumber(), a.getTextAr(), a.getTextTr(), a.getJuz(), a.getPage()))
				.toList();
		return new SurahDetail(
				surah.getNumber(),
				surah.getNameAr(),
				surah.getNameTr(),
				surah.getNameEn(),
				surah.getAyahCount(),
				surah.getRevelationType(),
				surah.getJuzStart(),
				ayahs,
				ayahs.isEmpty()
						? "Bu sure için metin şu anda yüklenemedi."
						: LOCAL_NOTE,
				"local");
	}

	@Transactional(readOnly = true)
	public List<AyahSearchHit> search(String q) {
		if (q == null || q.trim().length() < 2) {
			return List.of();
		}
		List<RemoteSearchHit> remote = alQuranCloudClient.search(q.trim());
		if (!remote.isEmpty()) {
			return remote.stream()
					.map(h -> {
						String nameTr = surahRepository.findByNumber(h.surahNumber())
								.map(QuranSurah::getNameTr)
								.orElse(h.surahNameTr());
						return new AyahSearchHit(
								h.surahNumber(),
								nameTr,
								h.surahNameAr(),
								h.ayahNumber(),
								h.textAr(),
								h.textTr());
					})
					.toList();
		}
		return ayahRepository.search(q.trim(), org.springframework.data.domain.PageRequest.of(0, 40)).stream()
				.map(a -> new AyahSearchHit(
						a.getSurah().getNumber(),
						a.getSurah().getNameTr(),
						a.getSurah().getNameAr(),
						a.getNumber(),
						a.getTextAr(),
						a.getTextTr()))
				.toList();
	}

	private SurahSummary toSummary(QuranSurah s) {
		return new SurahSummary(
				s.getNumber(), s.getNameAr(), s.getNameTr(), s.getNameEn(),
				s.getAyahCount(), s.getRevelationType(), s.getJuzStart());
	}

	private AyahDto toAyah(RemoteAyah a) {
		return new AyahDto(a.number(), a.textAr(), a.textTr(), a.juz(), a.page());
	}

	public record SurahSummary(
			int number, String nameAr, String nameTr, String nameEn,
			int ayahCount, String revelationType, Integer juzStart) {
	}

	public record AyahDto(int number, String textAr, String textTr, Integer juz, Integer page) {
	}

	public record SurahDetail(
			int number, String nameAr, String nameTr, String nameEn,
			int ayahCount, String revelationType, Integer juzStart,
			List<AyahDto> ayahs, String translationNote, String source) {
	}

	public record AyahSearchHit(
			int surahNumber, String surahNameTr, String surahNameAr,
			int ayahNumber, String textAr, String textTr) {
	}
}
