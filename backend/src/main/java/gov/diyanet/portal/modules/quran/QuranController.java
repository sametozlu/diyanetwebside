package gov.diyanet.portal.modules.quran;

import gov.diyanet.portal.modules.quran.QuranService.AyahSearchHit;
import gov.diyanet.portal.modules.quran.QuranService.SurahDetail;
import gov.diyanet.portal.modules.quran.QuranService.SurahSummary;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quran")
@RequiredArgsConstructor
public class QuranController {

	private final QuranService quranService;

	@GetMapping("/surahs")
	public List<SurahSummary> surahs() {
		return quranService.listSurahs();
	}

	@GetMapping("/search")
	public List<AyahSearchHit> search(@RequestParam(name = "q", defaultValue = "") String q) {
		return quranService.search(q);
	}

	@GetMapping("/surahs/{number}")
	public SurahDetail surah(@PathVariable int number) {
		return quranService.getSurah(number);
	}
}
