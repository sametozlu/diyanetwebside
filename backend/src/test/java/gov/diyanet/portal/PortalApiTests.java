package gov.diyanet.portal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.diyanet.portal.modules.authentication.Role;
import gov.diyanet.portal.modules.authentication.RoleRepository;
import gov.diyanet.portal.modules.authentication.User;
import gov.diyanet.portal.modules.authentication.UserRepository;
import gov.diyanet.portal.modules.content.News;
import gov.diyanet.portal.modules.content.NewsCategory;
import gov.diyanet.portal.modules.content.NewsCategoryRepository;
import gov.diyanet.portal.modules.content.NewsRepository;
import gov.diyanet.portal.modules.hadith.Hadith;
import gov.diyanet.portal.modules.hadith.HadithRepository;
import gov.diyanet.portal.modules.province.Province;
import gov.diyanet.portal.modules.province.ProvinceRepository;
import gov.diyanet.portal.modules.quran.QuranAyah;
import gov.diyanet.portal.modules.quran.QuranAyahRepository;
import gov.diyanet.portal.modules.quran.QuranSurah;
import gov.diyanet.portal.modules.quran.QuranSurahRepository;
import gov.diyanet.portal.modules.search.SearchService;
import java.time.Instant;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PortalApiTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NewsCategoryRepository newsCategoryRepository;
	@Autowired
	private NewsRepository newsRepository;
	@Autowired
	private ProvinceRepository provinceRepository;
	@Autowired
	private QuranSurahRepository quranSurahRepository;
	@Autowired
	private QuranAyahRepository quranAyahRepository;
	@Autowired
	private HadithRepository hadithRepository;
	@Autowired
	private SearchService searchService;

	@BeforeEach
	void seed() {
		Role admin = roleRepository.save(Role.builder().name("ADMIN").build());
		userRepository.save(User.builder()
				.email("admin@portal.demo")
				.passwordHash(passwordEncoder.encode("DemoAdmin123!"))
				.fullName("Demo")
				.enabled(true)
				.roles(Set.of(admin))
				.build());
		NewsCategory cat = newsCategoryRepository.save(NewsCategory.builder()
				.name("Gündem").slug("gundem").build());
		News news = newsRepository.save(News.builder()
				.title("Dijital Kapı")
				.slug("dijital-kapi")
				.summary("Demo haber")
				.body("Gövde")
				.category(cat)
				.featured(true)
				.publishedAt(Instant.now())
				.readCount(9)
				.locale("tr")
				.status("PUBLISHED")
				.build());
		searchService.index("news", news.getId(), news.getSlug(), news.getTitle(), news.getSummary(), news.getBody());
		provinceRepository.save(Province.builder()
				.name("Ankara").slug("ankara").plateCode(6).lat(39.93).lng(32.85)
				.about("Demo il").build());
		QuranSurah fatiha = quranSurahRepository.save(QuranSurah.builder()
				.number(1).nameAr("الفاتحة").nameTr("Fâtiha").nameEn("Al-Fatiha")
				.ayahCount(7).revelationType("MEKKI").juzStart(1).build());
		quranAyahRepository.save(QuranAyah.builder()
				.surah(fatiha).number(1)
				.textAr("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")
				.textTr("Rahman ve Rahim Allah’ın adıyla")
				.juz(1).build());
		hadithRepository.save(Hadith.builder()
				.slug("niyetler").title("Ameller niyetlere göredir")
				.textTr("Ameller niyetlere göredir").source("Buhârî")
				.publishedAt(Instant.now()).build());
	}

	@Test
	void newsListAndDetail() throws Exception {
		mockMvc.perform(get("/api/news"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].slug").value("dijital-kapi"));
		mockMvc.perform(get("/api/news/dijital-kapi"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Dijital Kapı"));
		mockMvc.perform(get("/api/news/categories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].slug").value("gundem"));
	}

	@Test
	void searchGroupsNews() throws Exception {
		mockMvc.perform(get("/api/search").param("q", "Dijital"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.groups[0].type").value("news"))
				.andExpect(jsonPath("$.groups[0].items[0].slug").value("dijital-kapi"));
	}

	@Test
	void prayerTimesForAnkara() throws Exception {
		mockMvc.perform(get("/api/prayer-times").param("province", "ankara"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.province").value("ankara"))
				.andExpect(jsonPath("$.times.imsak").exists())
				.andExpect(jsonPath("$.nextPrayer.name").exists());
	}

	@Test
	void quranSurahAndSearch() throws Exception {
		mockMvc.perform(get("/api/quran/surahs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nameTr").value("Fâtiha"));
		mockMvc.perform(get("/api/quran/surahs/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ayahs[0].number").value(1));
		mockMvc.perform(get("/api/quran/search").param("q", "Rahman"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].surahNumber").value(1));
	}

	@Test
	void provinceTemplate() throws Exception {
		mockMvc.perform(get("/api/provinces"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].slug").value("ankara"));
		mockMvc.perform(get("/api/provinces/ankara"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Ankara"))
				.andExpect(jsonPath("$.prayerTimes.times.ogle").exists());
	}

	@Test
	void authenticationAndAdminCrud() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"wrong@x.com\",\"password\":\"nope\"}"))
				.andExpect(status().isUnauthorized());

		String token = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"admin@portal.demo\",\"password\":\"DemoAdmin123!\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").isString())
				.andReturn().getResponse().getContentAsString();
		String jwt = token.replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");

		mockMvc.perform(get("/api/admin/news"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(post("/api/admin/news")
						.header("Authorization", "Bearer " + jwt)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"title":"Yeni","slug":"yeni-haber","summary":"s","body":"b","featured":false,"locale":"tr","status":"PUBLISHED"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.slug").value("yeni-haber"));

		Long id = newsRepository.findBySlug("yeni-haber").orElseThrow().getId();
		mockMvc.perform(put("/api/admin/news/" + id)
						.header("Authorization", "Bearer " + jwt)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"title":"Güncellendi","slug":"yeni-haber","summary":"s2","body":"b2","featured":true,"locale":"tr","status":"PUBLISHED"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Güncellendi"));

		mockMvc.perform(delete("/api/admin/news/" + id)
						.header("Authorization", "Bearer " + jwt))
				.andExpect(status().isNoContent());
	}

	@Test
	void contactMessageAccepted() throws Exception {
		mockMvc.perform(post("/api/contact")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Ayşe","email":"ayse@example.com","subject":"Soru","message":"Namaz vakitleri sayfası hakkında bilgi almak istiyorum."}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.message").exists());
	}

	@Test
	void hadithDaily() throws Exception {
		mockMvc.perform(get("/api/hadith/daily"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.slug").value("niyetler"));
	}
}
