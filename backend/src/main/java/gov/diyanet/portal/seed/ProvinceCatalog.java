package gov.diyanet.portal.seed;

import java.util.List;

public final class ProvinceCatalog {

	public record ProvinceDef(
			int plate, String name, String slug, double lat, double lng, String about) {
	}

	public record DistrictDef(String provinceSlug, String name, String slug) {
	}

	private ProvinceCatalog() {
	}

	public static List<ProvinceDef> provinces() {
		return List.of(
				p(1, "Adana", "adana", 37.00, 35.32),
				p(2, "Adıyaman", "adiyaman", 37.76, 38.28),
				p(3, "Afyonkarahisar", "afyonkarahisar", 38.76, 30.54),
				p(4, "Ağrı", "agri", 39.72, 43.05),
				p(5, "Amasya", "amasya", 40.65, 35.83),
				p(6, "Ankara", "ankara", 39.93, 32.85),
				p(7, "Antalya", "antalya", 36.90, 30.70),
				p(8, "Artvin", "artvin", 41.18, 41.82),
				p(9, "Aydın", "aydin", 37.84, 27.85),
				p(10, "Balıkesir", "balikesir", 39.65, 27.89),
				p(11, "Bilecik", "bilecik", 40.14, 29.98),
				p(12, "Bingöl", "bingol", 38.89, 40.50),
				p(13, "Bitlis", "bitlis", 38.40, 42.11),
				p(14, "Bolu", "bolu", 40.73, 31.61),
				p(15, "Burdur", "burdur", 37.72, 30.29),
				p(16, "Bursa", "bursa", 40.18, 29.06),
				p(17, "Çanakkale", "canakkale", 40.15, 26.41),
				p(18, "Çankırı", "cankiri", 40.60, 33.62),
				p(19, "Çorum", "corum", 40.55, 34.95),
				p(20, "Denizli", "denizli", 37.78, 29.09),
				p(21, "Diyarbakır", "diyarbakir", 37.91, 40.23),
				p(22, "Edirne", "edirne", 41.68, 26.56),
				p(23, "Elazığ", "elazig", 38.67, 39.22),
				p(24, "Erzincan", "erzincan", 39.75, 39.49),
				p(25, "Erzurum", "erzurum", 39.90, 41.27),
				p(26, "Eskişehir", "eskisehir", 39.78, 30.52),
				p(27, "Gaziantep", "gaziantep", 37.07, 37.38),
				p(28, "Giresun", "giresun", 40.91, 38.39),
				p(29, "Gümüşhane", "gumushane", 40.46, 39.48),
				p(30, "Hakkari", "hakkari", 37.57, 43.74),
				p(31, "Hatay", "hatay", 36.20, 36.16),
				p(32, "Isparta", "isparta", 37.76, 30.55),
				p(33, "Mersin", "mersin", 36.81, 34.64),
				p(34, "İstanbul", "istanbul", 41.01, 28.98),
				p(35, "İzmir", "izmir", 38.42, 27.14),
				p(36, "Kars", "kars", 40.60, 43.10),
				p(37, "Kastamonu", "kastamonu", 41.38, 33.78),
				p(38, "Kayseri", "kayseri", 38.73, 35.48),
				p(39, "Kırklareli", "kirklareli", 41.73, 27.23),
				p(40, "Kırşehir", "kirsehir", 39.15, 34.16),
				p(41, "Kocaeli", "kocaeli", 40.77, 29.94),
				p(42, "Konya", "konya", 37.87, 32.48),
				p(43, "Kütahya", "kutahya", 39.42, 29.98),
				p(44, "Malatya", "malatya", 38.36, 38.32),
				p(45, "Manisa", "manisa", 38.62, 27.43),
				p(46, "Kahramanmaraş", "kahramanmaras", 37.59, 36.94),
				p(47, "Mardin", "mardin", 37.31, 40.74),
				p(48, "Muğla", "mugla", 37.22, 28.37),
				p(49, "Muş", "mus", 38.74, 41.49),
				p(50, "Nevşehir", "nevsehir", 38.62, 34.71),
				p(51, "Niğde", "nigde", 37.97, 34.68),
				p(52, "Ordu", "ordu", 40.98, 37.88),
				p(53, "Rize", "rize", 41.02, 40.52),
				p(54, "Sakarya", "sakarya", 40.76, 30.40),
				p(55, "Samsun", "samsun", 41.29, 36.33),
				p(56, "Siirt", "siirt", 37.93, 41.94),
				p(57, "Sinop", "sinop", 42.03, 35.15),
				p(58, "Sivas", "sivas", 39.75, 37.02),
				p(59, "Tekirdağ", "tekirdag", 40.98, 27.51),
				p(60, "Tokat", "tokat", 40.32, 36.55),
				p(61, "Trabzon", "trabzon", 41.00, 39.72),
				p(62, "Tunceli", "tunceli", 39.11, 39.54),
				p(63, "Şanlıurfa", "sanliurfa", 37.17, 38.79),
				p(64, "Uşak", "usak", 38.67, 29.41),
				p(65, "Van", "van", 38.50, 43.40),
				p(66, "Yozgat", "yozgat", 39.82, 34.81),
				p(67, "Zonguldak", "zonguldak", 41.46, 31.80),
				p(68, "Aksaray", "aksaray", 38.37, 34.03),
				p(69, "Bayburt", "bayburt", 40.26, 40.23),
				p(70, "Karaman", "karaman", 37.18, 33.22),
				p(71, "Kırıkkale", "kirikkale", 39.85, 33.52),
				p(72, "Batman", "batman", 37.88, 41.13),
				p(73, "Şırnak", "sirnak", 37.52, 42.45),
				p(74, "Bartın", "bartin", 41.64, 32.34),
				p(75, "Ardahan", "ardahan", 41.11, 42.70),
				p(76, "Iğdır", "igdir", 39.92, 44.05),
				p(77, "Yalova", "yalova", 40.66, 29.28),
				p(78, "Karabük", "karabuk", 41.21, 32.63),
				p(79, "Kilis", "kilis", 36.72, 37.12),
				p(80, "Osmaniye", "osmaniye", 37.07, 36.25),
				p(81, "Düzce", "duzce", 40.84, 31.16));
	}

	public static List<DistrictDef> districts() {
		return List.of(
				d("istanbul", "Kadıköy", "kadikoy"),
				d("istanbul", "Beşiktaş", "besiktas"),
				d("istanbul", "Fatih", "fatih"),
				d("istanbul", "Üsküdar", "uskudar"),
				d("istanbul", "Şişli", "sisli"),
				d("istanbul", "Bakırköy", "bakirkoy"),
				d("istanbul", "Ümraniye", "umraniye"),
				d("istanbul", "Pendik", "pendik"),
				d("ankara", "Çankaya", "cankaya"),
				d("ankara", "Keçiören", "kecioren"),
				d("ankara", "Yenimahalle", "yenimahalle"),
				d("ankara", "Mamak", "mamak"),
				d("ankara", "Etimesgut", "etimesgut"),
				d("ankara", "Sincan", "sincan"),
				d("ankara", "Altındağ", "altindag"),
				d("ankara", "Gölbaşı", "golbasi"),
				d("izmir", "Konak", "konak"),
				d("izmir", "Karşıyaka", "karsiyaka"),
				d("izmir", "Bornova", "bornova"),
				d("izmir", "Buca", "buca"),
				d("izmir", "Bayraklı", "bayrakli"),
				d("izmir", "Çiğli", "cigli"),
				d("bursa", "Osmangazi", "osmangazi"),
				d("bursa", "Nilüfer", "nilufer"),
				d("bursa", "Yıldırım", "yildirim"),
				d("antalya", "Muratpaşa", "muratpasa"),
				d("antalya", "Kepez", "kepez"),
				d("antalya", "Konyaaltı", "konyaalti"),
				d("adana", "Seyhan", "seyhan"),
				d("adana", "Çukurova", "cukurova"),
				d("adana", "Yüreğir", "yuregir"),
				d("konya", "Selçuklu", "selcuklu"),
				d("konya", "Meram", "meram"),
				d("konya", "Karatay", "karatay"),
				d("gaziantep", "Şahinbey", "sahinbey"),
				d("gaziantep", "Şehitkamil", "sehitkamil"),
				d("gaziantep", "Oğuzeli", "oguzeli"),
				d("sanliurfa", "Haliliye", "haliliye"),
				d("sanliurfa", "Eyyübiye", "eyyubiye"),
				d("sanliurfa", "Karaköprü", "karakopru"),
				d("mersin", "Yenişehir", "yenisehir"),
				d("mersin", "Mezitli", "mezitli"),
				d("mersin", "Toroslar", "toroslar"),
				d("kocaeli", "İzmit", "izmit"),
				d("kocaeli", "Gebze", "gebze"),
				d("kocaeli", "Körfez", "korfez"),
				d("kayseri", "Melikgazi", "melikgazi"),
				d("kayseri", "Kocasinan", "kocasinan"),
				d("kayseri", "Talas", "talas"),
				d("samsun", "İlkadım", "ilkadim"),
				d("samsun", "Atakum", "atakum"),
				d("samsun", "Canik", "canik"));
	}

	private static ProvinceDef p(int plate, String name, String slug, double lat, double lng) {
		return new ProvinceDef(plate, name, slug, lat, lng,
				"DEMO: " + name + " İl Müftülüğü örnek kaydı. Resmî kurum bilgisi değildir. "
						+ "Bu içerik kavramsal portal denemesi için üretilmiştir.");
	}

	private static DistrictDef d(String provinceSlug, String name, String slug) {
		return new DistrictDef(provinceSlug, name, slug);
	}
}
