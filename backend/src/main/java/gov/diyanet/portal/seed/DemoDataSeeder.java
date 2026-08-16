package gov.diyanet.portal.seed;

import gov.diyanet.portal.modules.authentication.Role;
import gov.diyanet.portal.modules.authentication.RoleRepository;
import gov.diyanet.portal.modules.authentication.User;
import gov.diyanet.portal.modules.authentication.UserRepository;
import gov.diyanet.portal.modules.catalog.DigitalService;
import gov.diyanet.portal.modules.catalog.DigitalServiceRepository;
import gov.diyanet.portal.modules.content.News;
import gov.diyanet.portal.modules.content.NewsCategory;
import gov.diyanet.portal.modules.content.NewsCategoryRepository;
import gov.diyanet.portal.modules.content.NewsRepository;
import gov.diyanet.portal.modules.content.Organization;
import gov.diyanet.portal.modules.content.OrganizationRepository;
import gov.diyanet.portal.modules.content.PageEntity;
import gov.diyanet.portal.modules.content.PageRepository;
import gov.diyanet.portal.modules.event.Event;
import gov.diyanet.portal.modules.event.EventCategory;
import gov.diyanet.portal.modules.event.EventCategoryRepository;
import gov.diyanet.portal.modules.event.EventRepository;
import gov.diyanet.portal.modules.fatwa.Fatwa;
import gov.diyanet.portal.modules.fatwa.FatwaCategory;
import gov.diyanet.portal.modules.fatwa.FatwaCategoryRepository;
import gov.diyanet.portal.modules.fatwa.FatwaRepository;
import gov.diyanet.portal.modules.hadith.Hadith;
import gov.diyanet.portal.modules.hadith.HadithCategory;
import gov.diyanet.portal.modules.hadith.HadithCategoryRepository;
import gov.diyanet.portal.modules.hadith.HadithRepository;
import gov.diyanet.portal.modules.media.MediaAsset;
import gov.diyanet.portal.modules.media.MediaAssetRepository;
import gov.diyanet.portal.modules.media.MediaCategory;
import gov.diyanet.portal.modules.media.MediaCategoryRepository;
import gov.diyanet.portal.modules.prayer.PrayerTimeRepository;
import gov.diyanet.portal.modules.province.District;
import gov.diyanet.portal.modules.province.DistrictRepository;
import gov.diyanet.portal.modules.province.Province;
import gov.diyanet.portal.modules.province.ProvinceRepository;
import gov.diyanet.portal.modules.publication.Publication;
import gov.diyanet.portal.modules.publication.PublicationCategory;
import gov.diyanet.portal.modules.publication.PublicationCategoryRepository;
import gov.diyanet.portal.modules.publication.PublicationRepository;
import gov.diyanet.portal.modules.quran.QuranAyah;
import gov.diyanet.portal.modules.quran.QuranAyahRepository;
import gov.diyanet.portal.modules.quran.QuranSurah;
import gov.diyanet.portal.modules.quran.QuranSurahRepository;
import gov.diyanet.portal.modules.search.SearchService;
import gov.diyanet.portal.modules.sermon.Sermon;
import gov.diyanet.portal.modules.sermon.SermonCategory;
import gov.diyanet.portal.modules.sermon.SermonCategoryRepository;
import gov.diyanet.portal.modules.sermon.SermonRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.demo", havingValue = "true")
public class DemoDataSeeder implements ApplicationRunner {

	private static final String EDITORIAL_NOTE =
			" Bu içerik kavramsal kamu portalı editöryel metnidir; resmî kurum açıklaması değildir.";
	private static final String ILMIHAL_NOTE =
			" Eğitim amaçlı ilmihal özetidir; bağlayıcı fetva veya resmî hüküm değildir.";

	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final NewsCategoryRepository newsCategoryRepository;
	private final NewsRepository newsRepository;
	private final ProvinceRepository provinceRepository;
	private final DistrictRepository districtRepository;
	private final PrayerTimeRepository prayerTimeRepository;
	private final QuranSurahRepository quranSurahRepository;
	private final QuranAyahRepository quranAyahRepository;
	private final HadithCategoryRepository hadithCategoryRepository;
	private final HadithRepository hadithRepository;
	private final FatwaCategoryRepository fatwaCategoryRepository;
	private final FatwaRepository fatwaRepository;
	private final SermonCategoryRepository sermonCategoryRepository;
	private final SermonRepository sermonRepository;
	private final PublicationCategoryRepository publicationCategoryRepository;
	private final PublicationRepository publicationRepository;
	private final EventCategoryRepository eventCategoryRepository;
	private final EventRepository eventRepository;
	private final MediaCategoryRepository mediaCategoryRepository;
	private final MediaAssetRepository mediaAssetRepository;
	private final DigitalServiceRepository digitalServiceRepository;
	private final OrganizationRepository organizationRepository;
	private final PageRepository pageRepository;
	private final SearchService searchService;

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		if (userRepository.count() > 0) {
			sanitizeExistingRecords();
			log.info("Demo data already present, sanitized placeholders");
			return;
		}
		log.info("Seeding conceptual portal dataset");
		seedUsers();
		Map<String, Province> provinces = seedProvinces();
		seedQuran();
		seedNews();
		seedHadith();
		seedFatwa();
		seedSermons();
		seedPublications();
		seedEvents(provinces);
		seedMedia();
		seedServices();
		seedPages();
		log.info("Demo seed completed");
	}

	private void sanitizeExistingRecords() {
		for (MediaAsset asset : mediaAssetRepository.findAll()) {
			String url = asset.getVideoUrl();
			if (url != null && (url.contains("dQw4w9WgXcQ") || url.contains("youtube.com") || url.contains("youtu.be"))) {
				asset.setVideoUrl(null);
			}
			if (staleCover(asset.getThumbnailUrl())) {
				asset.setThumbnailUrl(cover("media"));
			}
			mediaAssetRepository.save(asset);
		}
		for (News news : newsRepository.findAll()) {
			if (staleCover(news.getImageUrl())) {
				String cat = news.getCategory() == null ? "gundem" : news.getCategory().getSlug();
				news.setImageUrl(cover(cat));
				newsRepository.save(news);
			}
		}
		for (Publication publication : publicationRepository.findAll()) {
			if (staleCover(publication.getCoverUrl())) {
				String cat = publication.getCategory() == null ? "kitap" : publication.getCategory().getSlug();
				publication.setCoverUrl(cover(cat));
			}
			if (publication.getFileUrl() != null && publication.getFileUrl().startsWith("/demo/")) {
				publication.setFileUrl(null);
			}
			publicationRepository.save(publication);
		}
		for (Sermon sermon : sermonRepository.findAll()) {
			if (sermon.getPdfUrl() != null && sermon.getPdfUrl().startsWith("/demo/")) {
				sermon.setPdfUrl(null);
			}
			if (sermon.getAudioUrl() != null && sermon.getAudioUrl().contains("soundhelix")) {
				sermon.setAudioUrl(null);
			}
			if ("Demo Hatip".equals(sermon.getPreacher())) {
				sermon.setPreacher("Portal Editörlüğü");
			}
			sermonRepository.save(sermon);
		}
		for (Province province : provinceRepository.findAll()) {
			if (province.getAddress() != null && province.getAddress().contains("DEMO")) {
				province.setAddress(null);
			}
			if (province.getPhone() != null && province.getPhone().contains("000 00 00")) {
				province.setPhone(null);
			}
			if (province.getEmail() != null && province.getEmail().endsWith("@portal.demo")) {
				province.setEmail(null);
			}
			if (province.getWebsite() != null && province.getWebsite().contains("portal.demo")) {
				province.setWebsite(null);
			}
			provinceRepository.save(province);
		}
		prayerTimeRepository.deleteAll();
	}

	private static boolean staleCover(String url) {
		if (url == null || url.isBlank()) {
			return true;
		}
		return url.contains("unsplash")
				|| url.startsWith("http")
				|| url.startsWith("/covers/");
	}

	private static String cover(String category) {
		return switch (category) {
			case "egitim", "cocuk" -> "/images/quran/mamluk-manuscript.webp";
			case "hac-umre", "hac" -> "/images/news/masjid-al-haram.webp";
			case "kultur" -> "/images/news/mosque-dome.webp";
			case "din-hizmetleri", "ibadet", "media" -> "/images/news/mosque-interior.webp";
			case "dunya" -> "/images/news/mosque-exterior.webp";
			case "baskanliktan" -> "/images/hero/mosque-courtyard.webp";
			case "kitap", "arastirma", "dergi" -> "/images/publications/ottoman-quran-leaf.webp";
			default -> "/images/news/mosque-exterior.webp";
		};
	}

	private void seedUsers() {
		Role admin = roleRepository.save(Role.builder().name("ADMIN").build());
		roleRepository.save(Role.builder().name("EDITOR").build());
		userRepository.save(User.builder()
				.email("admin@portal.demo")
				.passwordHash(passwordEncoder.encode("DemoAdmin123!"))
				.fullName("Demo Editör")
				.enabled(true)
				.roles(Set.of(admin))
				.build());
	}

	private Map<String, Province> seedProvinces() {
		Map<String, Province> map = new HashMap<>();
		for (var def : ProvinceCatalog.provinces()) {
			Province p = provinceRepository.save(Province.builder()
					.name(def.name())
					.slug(def.slug())
					.plateCode(def.plate())
					.lat(def.lat())
					.lng(def.lng())
					.about(def.about())
					.build());
			map.put(def.slug(), p);
			searchService.index("province", p.getId(), p.getSlug(), p.getName() + " İl Müftülüğü",
					p.getAbout(), p.getName());
		}
		for (var d : ProvinceCatalog.districts()) {
			Province p = map.get(d.provinceSlug());
			if (p != null) {
				districtRepository.save(District.builder().province(p).name(d.name()).slug(d.slug()).build());
			}
		}
		return map;
	}

	private void seedQuran() {
		Map<Integer, QuranSurah> surahs = new HashMap<>();
		for (var def : QuranCatalog.surahs()) {
			QuranSurah surah = quranSurahRepository.save(QuranSurah.builder()
					.number(def.number())
					.nameAr(def.nameAr())
					.nameTr(def.nameTr())
					.nameEn(def.nameEn())
					.ayahCount(def.ayahCount())
					.revelationType(def.revelation())
					.juzStart(def.juzStart())
					.build());
			surahs.put(def.number(), surah);
			searchService.index("quran", surah.getId(), String.valueOf(surah.getNumber()),
					surah.getNameTr() + " Suresi", surah.getNameAr(), surah.getNameEn());
		}
		for (var ayah : QuranCatalog.ayahs()) {
			QuranSurah surah = surahs.get(ayah.surah());
			if (surah == null) {
				continue;
			}
			quranAyahRepository.save(QuranAyah.builder()
					.surah(surah)
					.number(ayah.number())
					.textAr(ayah.ar())
					.textTr(ayah.tr())
					.juz(ayah.juz())
					.build());
		}
	}

	private void seedNews() {
		NewsCategory baskanlik = cat(newsCategoryRepository, "Başkanlıktan", "baskanliktan");
		NewsCategory gundem = cat(newsCategoryRepository, "Gündem", "gundem");
		NewsCategory din = cat(newsCategoryRepository, "Din Hizmetleri", "din-hizmetleri");
		NewsCategory egitim = cat(newsCategoryRepository, "Eğitim", "egitim");
		NewsCategory hac = cat(newsCategoryRepository, "Hac ve Umre", "hac-umre");
		NewsCategory kultur = cat(newsCategoryRepository, "Kültür", "kultur");
		NewsCategory dunya = cat(newsCategoryRepository, "Dünya", "dunya");
		String[][] rows = {
				{"dijital-donusum", "Yeni Dönemde Din Hizmetlerinde Dijital Dönüşüm",
						"Kurumsal dijital kapı, vatandaşın namaz vakitlerinden fetva aramasına kadar tüm kamu din hizmetlerini tek çatı altında topluyor.",
						"baskanliktan", "1"},
				{"yaz-kuran-kursu", "Yaz Kur’an Kursları İçin Demo Kayıt Duyurusu",
						"Demo duyuru: yaz dönemi kurs programları örnek takvimle yayımlandı.", "egitim", "1"},
				{"cuma-hutbesi-tema", "Haftanın Hutbesi: Merhamet ve Komşuluk",
						"Cuma hutbesi arşivine yeni bir demo metin eklendi.", "din-hizmetleri", "1"},
				{"umre-bilgilendirme", "Umre Sezonu İçin Örnek Bilgilendirme Notu",
						"Başvuru takvimi, belgeler ve sağlık uyarısı örnek metin olarak derlendi.", "hac-umre", "0"},
				{"kutuphane-acilis", "Dijital Kütüphane Koleksiyonu Genişletildi",
						"Yayın arşivine 20 yeni demo eser eklendi.", "kultur", "0"},
				{"ramazan-hazirlik", "Ramazan Ayına Hazırlık: Demo Rehber",
						"İmsakiyeler, teravih ve fitre konularında örnek içerik.", "gundem", "0"},
				{"genclik-bulusmasi", "Gençlik ve Değerler Buluşması Programı",
						"İl müftülükleriyle eşzamanlı demo etkinlik takvimi.", "egitim", "0"},
				{"cami-erisilebilirlik", "Camilerde Erişilebilirlik Standartları Çalışması",
						"Engelli vatandaşlar için örnek erişim ilkeleri yayımlandı.", "din-hizmetleri", "0"},
				{"yayin-dergisi", "Aylık Kültür Dergisi Yeni Sayısı",
						"Demo dergi kapağı ve makale özetleri yayında.", "kultur", "0"},
				{"uluslararasi-toplantı", "Uluslararası Din Hizmetleri Forumu",
						"Kavramsal bir forum özeti; gerçek bir diplomatik etkinlik değildir.", "dunya", "0"},
				{"hadis-serisi", "Hadis Okumaları Serisi Başlıyor",
						"Günlük hadis vitrini ve kaynak bilgisi örnekleri.", "egitim", "0"},
				{"fetva-masasi", "Fetva Masası Sık Sorulanlar Güncellendi",
						"İbadet ve aile kategorilerinde 20 demo soru-cevap.", "din-hizmetleri", "0"},
				{"il-muftulugu-ankara", "Ankara İl Müftülüğü Örnek Duyurusu",
						"Tek şablonla 81 il ofisinin nasıl yönetileceğini gösteren örnek.", "baskanliktan", "0"},
				{"sesli-kuran", "Kur’an Okuma Deneyimi Yenilendi",
						"Arapça metin, meal, ses ve yer imi ile sade bir okuyucu.", "kultur", "0"},
				{"hac-rehberi", "Hac Rehberi: Adım Adım Demo Süreç",
						"Başvurudan dönüşe kadar zaman çizelgesi.", "hac-umre", "0"},
				{"medya-canli", "Canlı Yayın Takvimi (Demo)",
						"TV, radyo ve podcast kartları tek medya merkezinde.", "gundem", "0"},
				{"ogretmen-semineri", "Din Görevlileri İçin Dijital Okuryazarlık Semineri",
						"Örnek eğitim duyurusu.", "egitim", "0"},
				{"vakif-eserleri", "Yazma Eserlerden Seçmeler",
						"Kütüphane vitrini için kurgusal tanıtım.", "kultur", "0"},
				{"dini-gunler", "Yaklaşan Dini Günler Takvimi",
						"Kandil ve bayram tarihleri örnek veri olarak listelendi.", "gundem", "0"},
				{"camii-bul", "Cami Bul Hizmeti İl Bazında Çalışıyor",
						"Harita ve il seçimi ile yerel ofislere yönlendirme.", "din-hizmetleri", "0"},
				{"kitap-fuari", "Kitap Fuarı Demo Stand Programı",
						"Yayınlar biriminin örnek fuar notu.", "kultur", "0"},
				{"goc-ve-uyum", "Göç ve Uyum Çalışmaları Özet Raporu",
						"Kavramsal politika notu.", "dunya", "0"},
				{"cocuk-kitaplari", "Çocuklar İçin Değerler Dizisi",
						"Yeni demo çocuk kitabı tanıtımı.", "egitim", "0"},
				{"sesli-hutbe", "Hutbeler Artık Sesli Arşivde",
						"PDF ve ses bağlantılı örnek hutbe kaydı.", "din-hizmetleri", "0"},
				{"sehir-gezisi", "Tarihi Camiler Yürüyüşü",
						"Kültür etkinliği duyurusu.", "kultur", "0"},
				{"bilim-ve-din", "Bilim ve Din Söyleşileri",
						"Demo konferans özeti.", "egitim", "0"},
				{"yardimlasma", "Yardımlaşma Kampanyası Bilgilendirmesi",
						"Bağış çağrısı içermeyen örnek kamuoyu notu.", "gundem", "0"},
				{"kuran-meal", "Meal Okuma Notları",
						"Okuyucu arayüzündeki tipografi ve RTL düzeni anlatılıyor.", "kultur", "0"},
				{"il-koordinasyon", "81 İl Koordinasyon Toplantısı",
						"Merkezi içerik + yerel şablon modelinin tanıtımı.", "baskanliktan", "0"},
				{"acik-veri", "Açık Veri ve API Dokümantasyonu",
						"Geliştiriciler için örnek OpenAPI uçları.", "baskanliktan", "0"},
				{"podcast-serisi", "Haftalık Podcast: Günün Ayeti",
						"Medya vitrinine yeni bölüm eklendi.", "gundem", "0"},
				{"kutuphane-saatleri", "Kütüphane Çalışma Saatleri Güncellendi",
						"Demo duyuru.", "kultur", "0"}
		};
		Map<String, NewsCategory> cats = Map.of(
				"baskanliktan", baskanlik, "gundem", gundem, "din-hizmetleri", din,
				"egitim", egitim, "hac-umre", hac, "kultur", kultur, "dunya", dunya);
		Instant now = Instant.now();
		for (int i = 0; i < rows.length; i++) {
			String[] r = rows[i];
			News n = newsRepository.save(News.builder()
					.slug(r[0])
					.title(r[1])
					.summary(r[2] + EDITORIAL_NOTE)
					.body("<p>" + r[2] + "</p><p>" + EDITORIAL_NOTE.trim()
							+ "</p><p>Metin, portalın ilgili hizmet sayfalarındaki güncel veri akışını (namaz vakitleri, Kur’an okuyucu, hadis, hutbe ve yayın arşivi) açıklamak için yazılmıştır.</p>")
					.imageUrl(cover(r[3]))
					.category(cats.get(r[3]))
					.featured("1".equals(r[4]))
					.publishedAt(now.minusSeconds((i + 1) * 86_400L))
					.readCount(Math.max(12, 420 - i * 11))
					.locale("tr")
					.status("PUBLISHED")
					.build());
			searchService.index("news", n.getId(), n.getSlug(), n.getTitle(), n.getSummary(), n.getBody());
		}
	}

	private void seedHadith() {
		HadithCategory ibadet = hcat("İbadet", "ibadet");
		HadithCategory ahlak = hcat("Ahlak", "ahlak");
		HadithCategory ilim = hcat("İlim", "ilim");
		HadithCategory merhamet = hcat("Merhamet", "merhamet");
		String[][] rows = {
				{"niyetler", "Ameller niyetlere göredir", "إِنَّمَا الْأَعْمَالُ بِالنِّيَّاتِ",
						"Ameller ancak niyetlere göredir.", "Buhârî", "Ömer b. Hattâb", "ibadet"},
				{"kolaylastirma", "Kolaylaştırınız, zorlaştırmayınız", "يَسِّرُوا وَلَا تُعَسِّرُوا",
						"Kolaylaştırın, zorlaştırmayın; müjdeleyin, nefret ettirmeyin.", "Buhârî", "Enes b. Mâlik", "ahlak"},
				{"komsu", "Komşu hakkı", "مَا زَالَ جِبْرِيلُ يُوصِينِي بِالْجَارِ",
						"Cebrail bana komşu hakkında o kadar tavsiyede bulundu ki, neredeyse komşuyu mirasçı kılacak sandım.", "Buhârî", "Âişe", "merhamet"},
				{"ilim-talebi", "İlim öğrenmek", "طَلَبُ الْعِلْمِ فَرِيضَةٌ",
						"İlim öğrenmek her Müslüman’a farzdır.", "İbn Mâce", "Enes b. Mâlik", "ilim"},
				{"temizlik", "Temizlik imanın yarısıdır", "الطُّهُورُ شَطْرُ الْإِيمَانِ",
						"Temizlik imanın yarısıdır.", "Müslim", "Ebû Mâlik el-Eş’arî", "ibadet"},
				{"guler-yuz", "Güler yüz sadakadır", "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ صَدَقَةٌ",
						"Kardeşine gülümsemen sadakadır.", "Tirmizî", "Ebû Zer", "ahlak"},
				{"anne-baba", "Anne-babaya iyilik", "رِضَا الرَّبِّ فِي رِضَا الْوَالِدِ",
						"Rabbin rızası, anne-babanın rızasındadır.", "Tirmizî", "Abdullah b. Amr", "ahlak"},
				{"yemek", "Yemeğe basmala", "سَمِّ اللَّهَ وَكُلْ بِيَمِينِكَ",
						"Allah’ın adını an, sağ elinle ye ve önünden ye.", "Buhârî", "Ömer b. Ebû Seleme", "ahlak"},
				{"selam", "Selamı yayınız", "أَفْشُوا السَّلَامَ",
						"Aranızda selamı yayın.", "Müslim", "Ebû Hüreyre", "ahlak"},
				{"sabir", "Sabır bir ışıktır", "وَالصَّبْرُ ضِيَاءٌ",
						"Sabır bir ışıktır.", "Müslim", "Ebû Mâlik", "ibadet"},
				{"merhamet-et", "Merhamet edin ki merhamet olunasınız", "ارْحَمُوا تُرْحَمُوا",
						"Merhamet edin ki size de merhamet edilsin.", "Ahmed", "Abdullah b. Amr", "merhamet"},
				{"guzel-soz", "Güzel söz sadakadır", "وَالْكَلِمَةُ الطَّيِّبَةُ صَدَقَةٌ",
						"Güzel söz sadakadır.", "Buhârî", "Ebû Hüreyre", "ahlak"},
				{"namaz-nuru", "Namaz bir nurdur", "وَالصَّلَاةُ نُورٌ",
						"Namaz bir nurdur.", "Müslim", "Ebû Mâlik", "ibadet"},
				{"kitap", "İlim müminin yitiğidir", "الْحِكْمَةُ ضَالَّةُ الْمُؤْمِنِ",
						"Hikmet müminin yitiğidir.", "Tirmizî", "Ebû Hüreyre", "ilim"},
				{"yetim", "Yetimi koruyan", "أَنَا وَكَافِلُ الْيَتِيمِ",
						"Yetimi koruyanla ben cennette şöylece yan yanayız.", "Buhârî", "Sehl b. Sa’d", "merhamet"},
				{"israf", "İsraf etmeyiniz", "كُلُوا وَاشْرَبُوا وَلَا تُسْرِفُوا",
						"Yiyin, için fakat israf etmeyin.", "Ahmed", "Abdullah b. Amr", "ahlak"},
				{"dogru-soz", "Doğruluk güvene götürür", "عَلَيْكُمْ بِالصِّدْقِ",
						"Doğruluğa sarılın.", "Müslim", "Abdullah b. Mes’ûd", "ahlak"},
				{"tesbih", "Dilini korumak", "مَنْ كَانَ يُؤْمِنُ بِاللَّهِ",
						"Allah’a ve ahiret gününe iman eden, ya hayır söylesin ya da sussun.", "Buhârî", "Ebû Hüreyre", "ahlak"},
				{"cemaat", "Cemaatle namaz", "صَلَاةُ الْجَمَاعَةِ",
						"Cemaatle kılınan namaz, tek başına kılınandan daha faziletlidir.", "Buhârî", "Abdullah b. Ömer", "ibadet"},
				{"misafir", "Misafire ikram", "مَنْ كَانَ يُؤْمِنُ بِاللَّهِ وَالْيَوْمِ الْآخِرِ فَلْيُكْرِمْ ضَيْفَهُ",
						"Allah’a ve ahiret gününe iman eden misafirine ikram etsin.", "Buhârî", "Ebû Hüreyre", "merhamet"},
				{"tefekkur", "Tefekkür", "تَفَكُّرُ سَاعَةٍ",
						"Bir saat tefekkür, bir gece ibadetten hayırlıdır. zayıf rivayet uyarısıyla örnek)", "Beyhakî", "—", "ilim"},
				{"emanet", "Emanete riayet", "أَدِّ الْأَمَانَةَ",
						"Emaneti ehline veriniz.", "Tirmizî", "Ebû Hüreyre", "ahlak"}
		};
		Map<String, HadithCategory> cats = Map.of("ibadet", ibadet, "ahlak", ahlak, "ilim", ilim, "merhamet", merhamet);
		Instant now = Instant.now();
		for (int i = 0; i < rows.length; i++) {
			String[] r = rows[i];
			Hadith h = hadithRepository.save(Hadith.builder()
					.slug(r[0]).title(r[1]).textAr(r[2]).textTr(r[3] + EDITORIAL_NOTE)
					.source(r[4]).narrator(r[5]).category(cats.get(r[6]))
					.publishedAt(now.minusSeconds((i + 1) * 43_200L))
					.build());
			searchService.index("hadith", h.getId(), h.getSlug(), h.getTitle(), h.getTextTr(), h.getTextAr());
		}
	}

	private void seedFatwa() {
		FatwaCategory ibadet = fcat("İbadet", "ibadet");
		FatwaCategory aile = fcat("Aile", "aile");
		FatwaCategory ticaret = fcat("Ticaret", "ticaret");
		FatwaCategory helal = fcat("Helal-Haram", "helal-haram");
		FatwaCategory gunluk = fcat("Günlük Hayat", "gunluk-hayat");
		FatwaCategory sosyal = fcat("Sosyal Hayat", "sosyal-hayat");
		String[][] rows = {
				{"namaz-kaza", "Kaza namazı nasıl kılınır?", "Kaza namazı, vaktinde kılınamayan farzın sonradan kılınmasıdır. DEMO cevap: niyet edilerek aynı rekât sayısıyla kılınır. Resmi fetva makamı değildir.", "ibadet"},
				{"abdest-bozan", "Abdesti bozan durumlar nelerdir?", "Klasik fıkıh literatüründe sayılan hususlar özetlenmiştir. DEMO metin; kişisel durumlar için yetkili mercilere başvurulmalıdır.", "ibadet"},
				{"oruc-seyahat", "Yolculukta oruç tutulur mu?", "Rukhsat hükümleri örnektir. DEMO: meşru seferde orucu erteleme imkânı anlatılır.", "ibadet"},
				{"zekat-nisab", "Zekât nisabı nedir?", "Nisap ve oranlar örnek anlatımdır. DEMO: güncel hesap için resmi tablolar kullanılmalıdır.", "ibadet"},
				{"cuma-yolcu", "Yolcu Cuma namazı kılar mı?", "Hanefi mezhebinde yolcuya Cuma farz olmayabilir. DEMO özet.", "ibadet"},
				{"nikah-sahit", "Nikâh için şahit gerekir mi?", "İslam hukukunda şahitlik şartı örneklenmiştir. DEMO; resmi nikâh ayrı bir konudur.", "aile"},
				{"anne-sut", "Süt emzirme süresi", "Kur’an’da zikredilen iki yıl örneği DEMO açıklama ile verilmiştir.", "aile"},
				{"miras-pay", "Miras payları nasıl hesaplanır?", "Feraiz özeti DEMO’dur, dava konusu yapılmamalıdır.", "aile"},
				{"boşanma", "Boşanma sürecinde dini hükümler", "Özet bilgi; resmi hukuk süreci saklıdır. DEMO.", "aile"},
				{"cocuk-isim", "Çocuğa isim koyma", "Anlamı güzel, inanca uygun isimler tavsiye edilir. DEMO.", "aile"},
				{"faiz", "Faiz caiz midir?", "Kur’an’da ribanın haramlığı vurgulanır. DEMO eğitim metni.", "ticaret"},
				{"veresiye", "Veresiye satış caiz midir?", "Şartları belli olmak kaydıyla örneklenmiştir. DEMO.", "ticaret"},
				{"sigorta", "Sigorta sözleşmeleri", "Klasik ve çağdaş yaklaşımlar özetlenir. DEMO, bağlayıcı fetva değildir.", "ticaret"},
				{"ortaklik", "Ticari ortaklık esasları", "Mudarebe/müşareke kavramları tanıtılır. DEMO.", "ticaret"},
				{"alkol", "Alkollü içecekler", "Sarhoş edici maddelerin hükmü özetlenir. DEMO.", "helal-haram"},
				{"et-kesim", "Etin helal olması", "Kesim şartları örnektir. DEMO.", "helal-haram"},
				{"muzik", "Müzik dinlemek", "Farklı görüşler özetlenir. DEMO, polemik amacı yoktur.", "helal-haram"},
				{"dovme", "Dövme yaptırmak", "Bedeni kalıcı değiştirme tartışması DEMO özetidir.", "helal-haram"},
				{"selam-verme", "Selam vermek sünnet midir?", "Selamlaşmanın fazileti anlatılır. DEMO.", "gunluk-hayat"},
				{"hayvan-hakki", "Hayvanlara eziyet", "Merhamet vurgusuyla DEMO cevap.", "gunluk-hayat"},
				{"cevre", "Çevreyi kirletmek", "İsraf ve emanet bilinci. DEMO.", "sosyal-hayat"},
				{"komsuluk", "Komşu hakkı", "Hadis kaynaklı özet. DEMO.", "sosyal-hayat"},
				{"sadaka", "Sadaka kimlere verilir?", "Öncelik sıralaması örneği. DEMO.", "sosyal-hayat"},
				{"fitre", "Fitre ne zaman verilir?", "Bayram namazından önce örneği. DEMO.", "ibadet"}
		};
		Map<String, FatwaCategory> cats = Map.of(
				"ibadet", ibadet, "aile", aile, "ticaret", ticaret,
				"helal-haram", helal, "gunluk-hayat", gunluk, "sosyal-hayat", sosyal);
		Instant now = Instant.now();
		for (int i = 0; i < rows.length; i++) {
			String[] r = rows[i];
			Fatwa f = fatwaRepository.save(Fatwa.builder()
					.slug(r[0]).question(r[1]).answer(r[2] + EDITORIAL_NOTE)
					.category(cats.get(r[3]))
					.publishedAt(now.minusSeconds((i + 1) * 75_000L))
					.build());
			searchService.index("fatwa", f.getId(), f.getSlug(), f.getQuestion(), f.getAnswer(), f.getAnswer());
		}
	}

	private void seedSermons() {
		SermonCategory merhamet = scat("Merhamet", "merhamet");
		SermonCategory aile = scat("Aile", "aile");
		SermonCategory ibadet = scat("İbadet", "ibadet");
		SermonCategory toplum = scat("Toplum", "toplum");
		String[] titles = {
				"Merhamet ve Komşuluk", "Emanet Bilinci", "Aile İçi Huzur", "İlim ve Tevazu",
				"Cuma’nın Bereketi", "İsraftan Sakınmak", "Doğruluk ve Güven", "Yardımlaşma Ahlakı",
				"Sabır ve Şükür", "Gençlik ve Sorumluluk", "Kul Hakkı", "Temizlik ve İbadet",
				"Vatan ve Kardeşlik", "Anne-Baba Hakkı", "Sözünde Durmak", "Adalet Duygusu",
				"Ramazan’a Hazırlık", "Kurban Bilinci", "Kitap Okuma Ahlakı", "Dijital Ortamda Edep",
				"Yetim ve Yoksul", "Selam ve Ülfet"
		};
		SermonCategory[] cycle = {merhamet, aile, ibadet, toplum};
		for (int i = 0; i < titles.length; i++) {
			String slug = "hutbe-" + (i + 1);
			Sermon s = sermonRepository.save(Sermon.builder()
					.slug(slug)
					.title(titles[i])
					.summary("Haftanın hutbesi: " + titles[i] + ".")
					.body("<p>Muhterem cemaat,</p><p>" + titles[i]
							+ " üzerine editöryel bir hutbe metnidir." + EDITORIAL_NOTE + "</p>")
					.preacher("Portal Editörlüğü")
					.sermonDate(LocalDate.now().minusWeeks(i))
					.pdfUrl(null)
					.audioUrl(null)
					.category(cycle[i % cycle.length])
					.build());
			searchService.index("sermon", s.getId(), s.getSlug(), s.getTitle(), s.getSummary(), s.getBody());
		}
	}

	private void seedPublications() {
		PublicationCategory kitap = pcat("Kitap", "kitap");
		PublicationCategory dergi = pcat("Dergi", "dergi");
		PublicationCategory arastirma = pcat("Araştırma", "arastirma");
		PublicationCategory cocuk = pcat("Çocuk", "cocuk");
		String[][] rows = {
				{"ilmihal-ozeti", "Kısa İlmihal (Demo)", "Temel ibadet özeti.", "BOOK", "kitap"},
				{"kuran-rehberi", "Kur’an Okuma Rehberi", "Tecvid notları DEMO.", "BOOK", "kitap"},
				{"hadis-seckisi", "Kırk Hadis Seçkisi", "Eğitim amaçlı derleme.", "BOOK", "kitap"},
				{"aile-rehberi", "Aile Rehberi", "İletişim ve merhamet.", "BOOK", "kitap"},
				{"hac-el-kitabi", "Hac El Kitabı", "Menâsik özeti DEMO.", "BOOK", "kitap"},
				{"aylik-dergi-01", "Kültür Dergisi Sayı 1", "Makale ve söyleşi.", "MAGAZINE", "dergi"},
				{"aylik-dergi-02", "Kültür Dergisi Sayı 2", "Ramazan özel.", "MAGAZINE", "dergi"},
				{"aylik-dergi-03", "Kültür Dergisi Sayı 3", "Gençlik dosyası.", "MAGAZINE", "dergi"},
				{"dijital-portal", "Dijital Portal Tasarım Notları", "Bu projenin mimari özeti.", "ARTICLE", "arastirma"},
				{"erisilebilirlik-rapor", "Erişilebilirlik Raporu", "WCAG 2.2 notları.", "RESEARCH", "arastirma"},
				{"sehir-ve-cami", "Şehir ve Cami", "Mimari deneme.", "ARTICLE", "arastirma"},
				{"cocuk-degerler", "Değerler Masalları", "Çocuk kitabı DEMO.", "BOOK", "cocuk"},
				{"elifba", "Elif-Bâ Alıştırmaları", "Başlangıç seti.", "BOOK", "cocuk"},
				{"yillik-arsiv", "Yıllık Hutbe Arşivi", "PDF derleme.", "ARCHIVE", "arastirma"},
				{"meal-notlari", "Meal Okuma Notları", "Tipografi ve RTL.", "ARTICLE", "arastirma"},
				{"sosyal-uyum", "Sosyal Uyum Kitapçığı", "Komşuluk ve dil.", "BOOK", "kitap"},
				{"vakif-tarihi", "Vakıf Kültürü Denemesi", "Tarihsel bakış DEMO.", "RESEARCH", "arastirma"},
				{"radyo-metinleri", "Radyo Konuşma Metinleri", "Medya arşivi.", "ARCHIVE", "dergi"},
				{"kadin-ve-aile", "Kadın ve Aile Bülteni", "Demo bülten.", "MAGAZINE", "dergi"},
				{"genclik-kitabi", "Gençlerle Sohbet", "Soru-cevap.", "BOOK", "kitap"},
				{"cevre-ahlaki", "Çevre Ahlakı", "Emanet bilinci.", "ARTICLE", "arastirma"},
				{"dil-kilavuzu", "Kurumsal Dil Kılavuzu", "Yazım ilkeleri.", "BOOK", "kitap"}
		};
		Map<String, PublicationCategory> cats = Map.of("kitap", kitap, "dergi", dergi, "arastirma", arastirma, "cocuk", cocuk);
		Instant now = Instant.now();
		for (int i = 0; i < rows.length; i++) {
			String[] r = rows[i];
			Publication p = publicationRepository.save(Publication.builder()
					.slug(r[0]).title(r[1]).summary(r[2] + EDITORIAL_NOTE)
					.body("<p>" + r[2] + EDITORIAL_NOTE + "</p>")
					.author("Demo Yayın Kurulu")
					.publishedAt(now.minusSeconds((i + 1) * 120_000L))
					.coverUrl(cover(r[4]))
					.fileUrl(null)
					.type(r[3])
					.category(cats.get(r[4]))
					.build());
			searchService.index("publication", p.getId(), p.getSlug(), p.getTitle(), p.getSummary(), p.getBody());
		}
	}

	private void seedEvents(Map<String, Province> provinces) {
		EventCategory konferans = ecat("Konferans", "konferans");
		EventCategory kurs = ecat("Kurs", "kurs");
		EventCategory kultur = ecat("Kültür", "kultur");
		EventCategory genc = ecat("Gençlik", "genclik");
		String[][] rows = {
				{"kuran-hatim", "Toplu Hatim Programı", "Ankara", "ankara", "kultur"},
				{"yaz-kursu-acilis", "Yaz Kursu Açılışı", "İstanbul", "istanbul", "kurs"},
				{"genclik-kampi", "Değerler Kampı", "İzmir", "izmir", "genclik"},
				{"hutbe-semineri", "Hatip Semineri", "Konya", "konya", "konferans"},
				{"kitap-soylesi", "Kitap Söyleşisi", "Bursa", "bursa", "kultur"},
				{"aile-okulu", "Aile Okulu", "Ankara", "ankara", "kurs"},
				{"cami-gezisi", "Tarihi Cami Gezisi", "Edirne", "edirne", "kultur"},
				{"hafizlik-bulusma", "Hafızlık Buluşması", "Kayseri", "kayseri", "genclik"},
				{"dil-kursu", "Osmanlıca Okuma", "İstanbul", "istanbul", "kurs"},
				{"konferans-merhamet", "Merhamet Konferansı", "Gaziantep", "gaziantep", "konferans"},
				{"cocuk-atolyesi", "Çocuk Atölyesi", "Antalya", "antalya", "genclik"},
				{"ramazan-iftar", "Demo İftar Programı", "Samsun", "samsun", "kultur"},
				{"kadin-semineri", "Kadınlara Yönelik Seminer", "Trabzon", "trabzon", "konferans"},
				{"muzik-ilahi", "İlahi Dinletisi", "Konya", "konya", "kultur"},
				{"genc-soru", "Gençlerle Soru-Cevap", "İzmir", "izmir", "genclik"},
				{"ogretmen-calistay", "Öğretici Çalıştayı", "Ankara", "ankara", "konferans"},
				{"vakif-sempozyum", "Vakıf Sempozyumu", "Bursa", "bursa", "konferans"},
				{"elifba-mezuniyet", "Elif-Bâ Mezuniyeti", "Adana", "adana", "kurs"},
				{"sehir-yuruyusu", "Şehir Yürüyüşü", "Çanakkale", "canakkale", "kultur"},
				{"yazma-eser", "Yazma Eser Tanıtımı", "İstanbul", "istanbul", "kultur"},
				{"gida-yardim", "Gıda Paylaşımı Duyurusu", "Şanlıurfa", "sanliurfa", "kultur"},
				{"dil-atolye", "Hitabet Atölyesi", "Kayseri", "kayseri", "kurs"}
		};
		Map<String, EventCategory> cats = Map.of("konferans", konferans, "kurs", kurs, "kultur", kultur, "genclik", genc);
		Instant now = Instant.now();
		for (int i = 0; i < rows.length; i++) {
			String[] r = rows[i];
			Province p = provinces.get(r[3]);
			Event e = eventRepository.save(Event.builder()
					.slug(r[0]).title(r[1]).summary(r[1] + " — örnek etkinlik." + EDITORIAL_NOTE)
					.body("<p>Yer: " + r[2] + ". Kurgusal etkinliktir.</p>")
					.startsAt(now.plusSeconds((i - 4) * 86_400L))
					.endsAt(now.plusSeconds((i - 4) * 86_400L + 7_200))
					.location(r[2])
					.province(p)
					.category(cats.get(r[4]))
					.build());
			searchService.index("event", e.getId(), e.getSlug(), e.getTitle(), e.getSummary(), e.getBody());
		}
	}

	private void seedMedia() {
		MediaCategory tv = mcat("Diyanet TV", "tv");
		MediaCategory radyo = mcat("Radyo", "radyo");
		MediaCategory podcast = mcat("Podcast", "podcast");
		MediaCategory video = mcat("Video", "video");
		MediaCategory canli = mcat("Canlı", "canli");
		String[][] rows = {
				{"gunun-ayeti", "Günün Ayeti", "VIDEO", "video"},
				{"hutbe-ozeti", "Hutbe Özeti", "VIDEO", "tv"},
				{"kuran-tilavet", "Kur’an Tilaveti", "VIDEO", "tv"},
				{"radyo-sabah", "Sabah Sohbeti", "RADIO", "radyo"},
				{"radyo-aksam", "Akşam Bandı", "RADIO", "radyo"},
				{"podcast-01", "Hadis ve Hayat #1", "PODCAST", "podcast"},
				{"podcast-02", "Hadis ve Hayat #2", "PODCAST", "podcast"},
				{"podcast-03", "Aile Sohbeti", "PODCAST", "podcast"},
				{"belgesel-cami", "Cami Mimarisi", "VIDEO", "video"},
				{"cocuk-program", "Çocuk Saati", "VIDEO", "tv"},
				{"canli-cuma", "Cuma Canlı Yayını", "LIVE", "canli"},
				{"canli-teravih", "Teravih Demo Yayını", "LIVE", "canli"},
				{"roportaj", "Yayın Kurulu Röportajı", "VIDEO", "video"},
				{"dil-dersi", "Arapça Alfabe", "VIDEO", "video"},
				{"sehir-belgesel", "Şehir ve İnanç", "VIDEO", "tv"},
				{"kisa-fetva", "Kısa Fetva", "VIDEO", "video"},
				{"ilahi", "İlahi Dinletisi", "RADIO", "radyo"},
				{"kitap-tanitim", "Kitap Tanıtımı", "VIDEO", "tv"},
				{"genc-mikrofon", "Genç Mikrofon", "PODCAST", "podcast"},
				{"arsiv-hutbe", "Arşiv Hutbesi", "VIDEO", "tv"},
				{"erisilebilirlik-spot", "Erişilebilirlik Spotu", "VIDEO", "video"}
		};
		Map<String, MediaCategory> cats = Map.of("tv", tv, "radyo", radyo, "podcast", podcast, "video", video, "canli", canli);
		Instant now = Instant.now();
		for (int i = 0; i < rows.length; i++) {
			String[] r = rows[i];
			MediaAsset m = mediaAssetRepository.save(MediaAsset.builder()
					.slug(r[0]).title(r[1])					.summary(r[1] + " — yayın kartı. Video dosyası bu kayıtta bulunmuyorsa oynatıcı boş durum gösterir.")
					.type(r[2])
					.videoUrl(null)
					.thumbnailUrl(cover("media"))
					.durationSeconds(180 + i * 17)
					.publishedAt(now.minusSeconds((i + 1) * 50_000L))
					.category(cats.get(r[3]))
					.build());
			searchService.index("media", m.getId(), m.getSlug(), m.getTitle(), m.getSummary(), m.getType());
		}
	}

	private void seedServices() {
		String[][] rows = {
				{"namaz-vakitleri", "Namaz Vakitleri", "İl bazında vakitler", "/namaz-vakitleri", "Clock", "Ibadet", "1"},
				{"kuran", "Kur'an-ı Kerim", "Okuma ve meal", "/kuran", "BookOpen", "Ibadet", "2"},
				{"hadis", "Hadis", "Günlük hadis ve arşiv", "/hadis", "Scroll", "Ibadet", "3"},
				{"fetva", "Fetva", "Soru-cevap araması", "/fetva", "MessageCircleQuestion", "Ibadet", "4"},
				{"hutbeler", "Hutbeler", "Cuma hutbesi arşivi", "/hutbeler", "FileText", "Ibadet", "5"},
				{"hac-umre", "Hac ve Umre", "Rehber ve takvim", "/hac-umre", "Landmark", "Hac", "6"},
				{"dini-gunler", "Dini Günler", "Kandil ve bayramlar", "/dini-gunler", "CalendarDays", "Takvim", "7"},
				{"cami-bul", "Cami Bul", "İl ve ilçe yönlendirme", "/cami-bul", "MapPin", "Yerel", "8"},
				{"il-muftulukleri", "İl Müftülükleri", "81 il ofisi", "/il-muftulukleri", "Building2", "Yerel", "9"},
				{"yayinlar", "Yayınlar", "Kitap ve dergi", "/yayinlar", "Library", "Yayin", "10"},
				{"basvurular", "Başvurular", "Örnek başvuru kapısı", "/hizmetler", "ClipboardList", "Basvuru", "11"}
		};
		for (String[] r : rows) {
			DigitalService s = digitalServiceRepository.save(DigitalService.builder()
					.slug(r[0]).title(r[1]).summary(r[2]).href(r[3]).icon(r[4]).category(r[5])
					.sortOrder(Integer.parseInt(r[6]))
					.build());
			searchService.index("service", s.getId(), s.getSlug(), s.getTitle(), s.getSummary(), s.getHref());
		}
	}

	private void seedPages() {
		organizationRepository.save(Organization.builder()
				.name("Başkanlık (Demo)").slug("baskanlik")
				.summary("Kavramsal kurumsal yapı.")
				.body("Bu sayfa resmi bir teşkilat şeması değildir.")
				.sortOrder(1).build());
		page("baskanligimiz", "Başkanlığımız",
				"<p>Bu portal, dağınık kurumsal web sitelerini tek bir dijital kapıda birleştirmeyi amaçlayan kavramsal bir çalışmadır.</p>");
		page("iletisim", "İletişim",
				"<p>Aşağıdaki form bu kavramsal portala iletilir; resmî Diyanet birimine ulaşmaz. Adres ve telefon uydurulmamıştır.</p>");
		page("gizlilik", "Gizlilik", "<p>Kişisel veri işlenmez. Demo ortamıdır.</p>");
		page("kvkk", "KVKK", "<p>Kavramsal aydınlatma metni. Resmi KVKK metni değildir.</p>");
		page("cerez-politikasi", "Çerez Politikası", "<p>Zorunlu çerezler dışında izleme yoktur (demo).</p>");
		page("kullanim-kosullari", "Kullanım Koşulları", "<p>İçerikler kurgusaldır; ibadet için resmi kaynak kullanın.</p>");
		page("erisilebilirlik", "Erişilebilirlik", "<p>WCAG 2.2 ilkeleri hedeflenir. Erişilebilirlik menüsünü kullanabilirsiniz.</p>");
	}

	private void page(String slug, String title, String body) {
		PageEntity p = pageRepository.save(PageEntity.builder()
				.slug(slug).title(title).body(body + "<p>" + EDITORIAL_NOTE + "</p>").locale("tr").build());
		searchService.index("page", p.getId(), p.getSlug(), p.getTitle(), p.getTitle(), p.getBody());
	}

	private NewsCategory cat(NewsCategoryRepository repo, String name, String slug) {
		return repo.save(NewsCategory.builder().name(name).slug(slug).build());
	}

	private HadithCategory hcat(String name, String slug) {
		return hadithCategoryRepository.save(HadithCategory.builder().name(name).slug(slug).build());
	}

	private FatwaCategory fcat(String name, String slug) {
		return fatwaCategoryRepository.save(FatwaCategory.builder().name(name).slug(slug).build());
	}

	private SermonCategory scat(String name, String slug) {
		return sermonCategoryRepository.save(SermonCategory.builder().name(name).slug(slug).build());
	}

	private PublicationCategory pcat(String name, String slug) {
		return publicationCategoryRepository.save(PublicationCategory.builder().name(name).slug(slug).build());
	}

	private EventCategory ecat(String name, String slug) {
		return eventCategoryRepository.save(EventCategory.builder().name(name).slug(slug).build());
	}

	private MediaCategory mcat(String name, String slug) {
		return mediaCategoryRepository.save(MediaCategory.builder().name(name).slug(slug).build());
	}
}
