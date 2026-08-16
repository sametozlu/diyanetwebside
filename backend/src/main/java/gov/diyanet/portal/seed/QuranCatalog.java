package gov.diyanet.portal.seed;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo Quran metadata and a representative subset of ayahs.
 * Arabic text is public-domain Quranic text. Turkish renderings are DEMO
 * educational paraphrases, not an official translation.
 */
public final class QuranCatalog {

	public record SurahDef(
			int number, String nameAr, String nameTr, String nameEn,
			int ayahCount, String revelation, int juzStart) {
	}

	public record AyahDef(int surah, int number, String ar, String tr, int juz) {
	}

	private QuranCatalog() {
	}

	public static List<SurahDef> surahs() {
		return List.of(
				s(1, "الفاتحة", "Fâtiha", "Al-Fatiha", 7, "MEKKI", 1),
				s(2, "البقرة", "Bakara", "Al-Baqarah", 286, "MEDENI", 1),
				s(3, "آل عمران", "Âl-i İmrân", "Ali 'Imran", 200, "MEDENI", 3),
				s(4, "النساء", "Nisâ", "An-Nisa", 176, "MEDENI", 4),
				s(5, "المائدة", "Mâide", "Al-Ma'idah", 120, "MEDENI", 6),
				s(6, "الأنعام", "En'âm", "Al-An'am", 165, "MEKKI", 7),
				s(7, "الأعراف", "A'râf", "Al-A'raf", 206, "MEKKI", 8),
				s(8, "الأنفال", "Enfâl", "Al-Anfal", 75, "MEDENI", 9),
				s(9, "التوبة", "Tevbe", "At-Tawbah", 129, "MEDENI", 10),
				s(10, "يونس", "Yûnus", "Yunus", 109, "MEKKI", 11),
				s(11, "هود", "Hûd", "Hud", 123, "MEKKI", 11),
				s(12, "يوسف", "Yûsuf", "Yusuf", 111, "MEKKI", 12),
				s(13, "الرعد", "Ra'd", "Ar-Ra'd", 43, "MEDENI", 13),
				s(14, "إبراهيم", "İbrâhîm", "Ibrahim", 52, "MEKKI", 13),
				s(15, "الحجر", "Hicr", "Al-Hijr", 99, "MEKKI", 14),
				s(16, "النحل", "Nahl", "An-Nahl", 128, "MEKKI", 14),
				s(17, "الإسراء", "İsrâ", "Al-Isra", 111, "MEKKI", 15),
				s(18, "الكهف", "Kehf", "Al-Kahf", 110, "MEKKI", 15),
				s(19, "مريم", "Meryem", "Maryam", 98, "MEKKI", 16),
				s(20, "طه", "Tâhâ", "Ta-Ha", 135, "MEKKI", 16),
				s(21, "الأنبياء", "Enbiyâ", "Al-Anbya", 112, "MEKKI", 17),
				s(22, "الحج", "Hac", "Al-Hajj", 78, "MEDENI", 17),
				s(23, "المؤمنون", "Mü'minûn", "Al-Mu'minun", 118, "MEKKI", 18),
				s(24, "النور", "Nûr", "An-Nur", 64, "MEDENI", 18),
				s(25, "الفرقان", "Furkân", "Al-Furqan", 77, "MEKKI", 18),
				s(26, "الشعراء", "Şuarâ", "Ash-Shu'ara", 227, "MEKKI", 19),
				s(27, "النمل", "Neml", "An-Naml", 93, "MEKKI", 19),
				s(28, "القصص", "Kasas", "Al-Qasas", 88, "MEKKI", 20),
				s(29, "العنكبوت", "Ankebût", "Al-'Ankabut", 69, "MEKKI", 20),
				s(30, "الروم", "Rûm", "Ar-Rum", 60, "MEKKI", 21),
				s(31, "لقمان", "Lokmân", "Luqman", 34, "MEKKI", 21),
				s(32, "السجدة", "Secde", "As-Sajdah", 30, "MEKKI", 21),
				s(33, "الأحزاب", "Ahzâb", "Al-Ahzab", 73, "MEDENI", 21),
				s(34, "سبأ", "Sebe'", "Saba", 54, "MEKKI", 22),
				s(35, "فاطر", "Fâtır", "Fatir", 45, "MEKKI", 22),
				s(36, "يس", "Yâsîn", "Ya-Sin", 83, "MEKKI", 22),
				s(37, "الصافات", "Sâffât", "As-Saffat", 182, "MEKKI", 23),
				s(38, "ص", "Sâd", "Sad", 88, "MEKKI", 23),
				s(39, "الزمر", "Zümer", "Az-Zumar", 75, "MEKKI", 23),
				s(40, "غافر", "Mü'min", "Ghafir", 85, "MEKKI", 24),
				s(41, "فصلت", "Fussilet", "Fussilat", 54, "MEKKI", 24),
				s(42, "الشورى", "Şûrâ", "Ash-Shuraa", 53, "MEKKI", 25),
				s(43, "الزخرف", "Zuhruf", "Az-Zukhruf", 89, "MEKKI", 25),
				s(44, "الدخان", "Duhân", "Ad-Dukhan", 59, "MEKKI", 25),
				s(45, "الجاثية", "Câsiye", "Al-Jathiyah", 37, "MEKKI", 25),
				s(46, "الأحقاف", "Ahkâf", "Al-Ahqaf", 35, "MEKKI", 26),
				s(47, "محمد", "Muhammed", "Muhammad", 38, "MEDENI", 26),
				s(48, "الفتح", "Fetih", "Al-Fath", 29, "MEDENI", 26),
				s(49, "الحجرات", "Hucurât", "Al-Hujurat", 18, "MEDENI", 26),
				s(50, "ق", "Kâf", "Qaf", 45, "MEKKI", 26),
				s(51, "الذاريات", "Zâriyât", "Adh-Dhariyat", 60, "MEKKI", 26),
				s(52, "الطور", "Tûr", "At-Tur", 49, "MEKKI", 27),
				s(53, "النجم", "Necm", "An-Najm", 62, "MEKKI", 27),
				s(54, "القمر", "Kamer", "Al-Qamar", 55, "MEKKI", 27),
				s(55, "الرحمن", "Rahmân", "Ar-Rahman", 78, "MEDENI", 27),
				s(56, "الواقعة", "Vâkıa", "Al-Waqi'ah", 96, "MEKKI", 27),
				s(57, "الحديد", "Hadîd", "Al-Hadid", 29, "MEDENI", 27),
				s(58, "المجادلة", "Mücâdele", "Al-Mujadila", 22, "MEDENI", 28),
				s(59, "الحشر", "Haşr", "Al-Hashr", 24, "MEDENI", 28),
				s(60, "الممتحنة", "Mümtehine", "Al-Mumtahanah", 13, "MEDENI", 28),
				s(61, "الصف", "Saff", "As-Saf", 14, "MEDENI", 28),
				s(62, "الجمعة", "Cuma", "Al-Jumu'ah", 11, "MEDENI", 28),
				s(63, "المنافقون", "Münâfikûn", "Al-Munafiqun", 11, "MEDENI", 28),
				s(64, "التغابن", "Tegâbün", "At-Taghabun", 18, "MEDENI", 28),
				s(65, "الطلاق", "Talâk", "At-Talaq", 12, "MEDENI", 28),
				s(66, "التحريم", "Tahrîm", "At-Tahrim", 12, "MEDENI", 28),
				s(67, "الملك", "Mülk", "Al-Mulk", 30, "MEKKI", 29),
				s(68, "القلم", "Kalem", "Al-Qalam", 52, "MEKKI", 29),
				s(69, "الحاقة", "Hâkka", "Al-Haqqah", 52, "MEKKI", 29),
				s(70, "المعارج", "Meâric", "Al-Ma'arij", 44, "MEKKI", 29),
				s(71, "نوح", "Nûh", "Nuh", 28, "MEKKI", 29),
				s(72, "الجن", "Cin", "Al-Jinn", 28, "MEKKI", 29),
				s(73, "المزمل", "Müzzemmil", "Al-Muzzammil", 20, "MEKKI", 29),
				s(74, "المدثر", "Müddessir", "Al-Muddaththir", 56, "MEKKI", 29),
				s(75, "القيامة", "Kıyâme", "Al-Qiyamah", 40, "MEKKI", 29),
				s(76, "الإنسان", "İnsan", "Al-Insan", 31, "MEDENI", 29),
				s(77, "المرسلات", "Mürselât", "Al-Mursalat", 50, "MEKKI", 29),
				s(78, "النبأ", "Nebe", "An-Naba", 40, "MEKKI", 30),
				s(79, "النازعات", "Nâziât", "An-Nazi'at", 46, "MEKKI", 30),
				s(80, "عبس", "Abese", "Abasa", 42, "MEKKI", 30),
				s(81, "التكوير", "Tekvîr", "At-Takwir", 29, "MEKKI", 30),
				s(82, "الإنفطار", "İnfitâr", "Al-Infitar", 19, "MEKKI", 30),
				s(83, "المطففين", "Mutaffifîn", "Al-Mutaffifin", 36, "MEKKI", 30),
				s(84, "الإنشقاق", "İnşikâk", "Al-Inshiqaq", 25, "MEKKI", 30),
				s(85, "البروج", "Bürûc", "Al-Buruj", 22, "MEKKI", 30),
				s(86, "الطارق", "Târık", "At-Tariq", 17, "MEKKI", 30),
				s(87, "الأعلى", "A'lâ", "Al-A'la", 19, "MEKKI", 30),
				s(88, "الغاشية", "Gâşiye", "Al-Ghashiyah", 26, "MEKKI", 30),
				s(89, "الفجر", "Fecr", "Al-Fajr", 30, "MEKKI", 30),
				s(90, "البلد", "Beled", "Al-Balad", 20, "MEKKI", 30),
				s(91, "الشمس", "Şems", "Ash-Shams", 15, "MEKKI", 30),
				s(92, "الليل", "Leyl", "Al-Layl", 21, "MEKKI", 30),
				s(93, "الضحى", "Duhâ", "Ad-Duhaa", 11, "MEKKI", 30),
				s(94, "الشرح", "İnşirâh", "Ash-Sharh", 8, "MEKKI", 30),
				s(95, "التين", "Tîn", "At-Tin", 8, "MEKKI", 30),
				s(96, "العلق", "Alak", "Al-'Alaq", 19, "MEKKI", 30),
				s(97, "القدر", "Kadir", "Al-Qadr", 5, "MEKKI", 30),
				s(98, "البينة", "Beyyine", "Al-Bayyinah", 8, "MEDENI", 30),
				s(99, "الزلزلة", "Zilzâl", "Az-Zalzalah", 8, "MEDENI", 30),
				s(100, "العاديات", "Âdiyât", "Al-'Adiyat", 11, "MEKKI", 30),
				s(101, "القارعة", "Kâria", "Al-Qari'ah", 11, "MEKKI", 30),
				s(102, "التكاثر", "Tekâsür", "At-Takathur", 8, "MEKKI", 30),
				s(103, "العصر", "Asr", "Al-'Asr", 3, "MEKKI", 30),
				s(104, "الهمزة", "Hümeze", "Al-Humazah", 9, "MEKKI", 30),
				s(105, "الفيل", "Fîl", "Al-Fil", 5, "MEKKI", 30),
				s(106, "قريش", "Kureyş", "Quraysh", 4, "MEKKI", 30),
				s(107, "الماعون", "Mâûn", "Al-Ma'un", 7, "MEKKI", 30),
				s(108, "الكوثر", "Kevser", "Al-Kawthar", 3, "MEKKI", 30),
				s(109, "الكافرون", "Kâfirûn", "Al-Kafirun", 6, "MEKKI", 30),
				s(110, "النصر", "Nasr", "An-Nasr", 3, "MEDENI", 30),
				s(111, "المسد", "Tebbet", "Al-Masad", 5, "MEKKI", 30),
				s(112, "الإخلاص", "İhlâs", "Al-Ikhlas", 4, "MEKKI", 30),
				s(113, "الفلق", "Felak", "Al-Falaq", 5, "MEKKI", 30),
				s(114, "الناس", "Nâs", "An-Nas", 6, "MEKKI", 30));
	}

	public static List<AyahDef> ayahs() {
		List<AyahDef> list = new ArrayList<>();
		list.addAll(fatiha());
		list.addAll(baqarahOpening());
		list.addAll(yasinOpening());
		list.addAll(juzAmma());
		return list;
	}

	private static List<AyahDef> fatiha() {
		return List.of(
				a(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
						"Rahman ve Rahim Allah’ın adıyla. (DEMO meal)", 1),
				a(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
						"Hamd, âlemlerin Rabbi Allah’a mahsustur. (DEMO meal)", 1),
				a(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ",
						"O, Rahman’dır, Rahim’dir. (DEMO meal)", 1),
				a(1, 4, "مَالِكِ يَوْمِ الدِّينِ",
						"Din gününün sahibidir. (DEMO meal)", 1),
				a(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
						"Yalnız Sana kulluk eder, yalnız Senden yardım dileriz. (DEMO meal)", 1),
				a(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
						"Bizi doğru yola ilet. (DEMO meal)", 1),
				a(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
						"Nimet verdiklerinin yoluna; gazaba uğrayanların ve sapkınların yoluna değil. (DEMO meal)", 1));
	}

	private static List<AyahDef> baqarahOpening() {
		return List.of(
				a(2, 1, "الم", "Elif Lâm Mîm. (DEMO meal)", 1),
				a(2, 2, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ",
						"Bu, kendisinde şüphe olmayan kitaptır; takva sahipleri için bir hidayettir. (DEMO meal)", 1),
				a(2, 3, "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ",
						"Onlar gayba inanır, namazı kılar ve kendilerine verdiğimiz rızıktan infak ederler. (DEMO meal)", 1),
				a(2, 4, "وَالَّذِينَ يُؤْمِنُونَ بِمَا أُنزِلَ إِلَيْكَ وَمَا أُنزِلَ مِن قَبْلِكَ وَبِالْآخِرَةِ هُمْ يُوقِنُونَ",
						"Sana ve senden önce indirilene inanır, ahirete de kesin kanaat getirirler. (DEMO meal)", 1),
				a(2, 5, "أُولَٰئِكَ عَلَىٰ هُدًى مِّن رَّبِّهِمْ ۖ وَأُولَٰئِكَ هُمُ الْمُفْلِحُونَ",
						"İşte onlar Rablerinden bir hidayet üzeredir ve kurtuluşa erenler onlardır. (DEMO meal)", 1),
				a(2, 6, "إِنَّ الَّذِينَ كَفَرُوا سَوَاءٌ عَلَيْهِمْ أَأَنذَرْتَهُمْ أَمْ لَمْ تُنذِرْهُمْ لَا يُؤْمِنُونَ",
						"İnkâr edenlere gelince, onları uyarsan da uyarmasan da inanmazlar. (DEMO meal)", 1),
				a(2, 7, "خَتَمَ اللَّهُ عَلَىٰ قُلُوبِهِمْ وَعَلَىٰ سَمْعِهِمْ ۖ وَعَلَىٰ أَبْصَارِهِمْ غِشَاوَةٌ ۖ وَلَهُمْ عَذَابٌ عَظِيمٌ",
						"Allah onların kalplerini ve kulaklarını mühürlemiştir; gözlerinde de bir perde vardır. (DEMO meal)", 1));
	}

	private static List<AyahDef> yasinOpening() {
		return List.of(
				a(36, 1, "يس", "Yâ-Sîn. (DEMO meal)", 22),
				a(36, 2, "وَالْقُرْآنِ الْحَكِيمِ", "Hikmetli Kur’an’a andolsun. (DEMO meal)", 22),
				a(36, 3, "إِنَّكَ لَمِنَ الْمُرْسَلِينَ", "Şüphesiz sen gönderilen elçilerdensin. (DEMO meal)", 22),
				a(36, 4, "عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ", "Dosdoğru bir yol üzeresin. (DEMO meal)", 22),
				a(36, 5, "تَنزِيلَ الْعَزِيزِ الرَّحِيمِ", "Bu, mutlak galip ve çok merhametli olanın indirmesidir. (DEMO meal)", 22));
	}

	private static List<AyahDef> juzAmma() {
		List<AyahDef> list = new ArrayList<>();
		list.addAll(List.of(
				a(103, 1, "وَالْعَصْرِ", "Asra andolsun. (DEMO meal)", 30),
				a(103, 2, "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ", "İnsan gerçekten ziyan içindedir. (DEMO meal)", 30),
				a(103, 3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ",
						"Ancak iman edip salih amel işleyenler, birbirine hakkı ve sabrı tavsiye edenler başka. (DEMO meal)", 30),
				a(108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "Şüphesiz biz sana Kevser’i verdik. (DEMO meal)", 30),
				a(108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "Öyleyse Rabbin için namaz kıl ve kurban kes. (DEMO meal)", 30),
				a(108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "Asıl soyu kesik olan, sana kin besleyendir. (DEMO meal)", 30),
				a(109, 1, "قُلْ يَا أَيُّهَا الْكَافِرُونَ", "De ki: Ey inkârcılar! (DEMO meal)", 30),
				a(109, 2, "لَا أَعْبُدُ مَا تَعْبُدُونَ", "Ben sizin taptıklarınıza tapmam. (DEMO meal)", 30),
				a(109, 3, "وَلَا أَنتُمْ عَابِدُونَ مَا أَعْبُدُ", "Siz de benim taptığıma tapmazsınız. (DEMO meal)", 30),
				a(109, 4, "وَلَا أَنَا عَابِدٌ مَّا عَبَدتُّمْ", "Ben sizin taptığınıza tapacak değilim. (DEMO meal)", 30),
				a(109, 5, "وَلَا أَنتُمْ عَابِدُونَ مَا أَعْبُدُ", "Siz de benim taptığıma tapacak değilsiniz. (DEMO meal)", 30),
				a(109, 6, "لَكُمْ دِينُكُمْ وَلِيَ دِينِ", "Sizin dininiz size, benim dinim bana. (DEMO meal)", 30),
				a(110, 1, "إِذَا جَاءَ نَصْرُ اللَّهِ وَالْفَتْحُ", "Allah’ın yardımı ve fetih geldiğinde. (DEMO meal)", 30),
				a(110, 2, "وَرَأَيْتَ النَّاسَ يَدْخُلُونَ فِي دِينِ اللَّهِ أَفْوَاجًا",
						"İnsanların bölük bölük Allah’ın dinine girdiğini gördüğünde. (DEMO meal)", 30),
				a(110, 3, "فَسَبِّحْ بِحَمْدِ رَبِّكَ وَاسْتَغْفِرْهُ ۚ إِنَّهُ كَانَ تَوَّابًا",
						"Rabbini hamd ile tesbih et ve O’ndan bağışlanma dile. (DEMO meal)", 30),
				a(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "De ki: O Allah birdir. (DEMO meal)", 30),
				a(112, 2, "اللَّهُ الصَّمَدُ", "Allah Samed’dir. (DEMO meal)", 30),
				a(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "O doğurmamış ve doğmamıştır. (DEMO meal)", 30),
				a(112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Hiçbir şey O’na denk değildir. (DEMO meal)", 30),
				a(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "De ki: Sabahın Rabbine sığınırım. (DEMO meal)", 30),
				a(113, 2, "مِن شَرِّ مَا خَلَقَ", "Yarattığı şeylerin şerrinden. (DEMO meal)", 30),
				a(113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "Karanlığı çöktüğü zaman gecenin şerrinden. (DEMO meal)", 30),
				a(113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "Düğümlere üfleyenlerin şerrinden. (DEMO meal)", 30),
				a(113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "Haset ettiği zaman hasetçinin şerrinden. (DEMO meal)", 30),
				a(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "De ki: İnsanların Rabbine sığınırım. (DEMO meal)", 30),
				a(114, 2, "مَلِكِ النَّاسِ", "İnsanların Melik’ine. (DEMO meal)", 30),
				a(114, 3, "إِلَٰهِ النَّاسِ", "İnsanların İlâh’ına. (DEMO meal)", 30),
				a(114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "Sinsi vesvesecinin şerrinden. (DEMO meal)", 30),
				a(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "O ki insanların göğüslerine vesvese verir. (DEMO meal)", 30),
				a(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "Cinlerden ve insanlardan. (DEMO meal)", 30),
				a(97, 1, "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ", "Biz onu Kadir gecesinde indirdik. (DEMO meal)", 30),
				a(97, 2, "وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ", "Kadir gecesinin ne olduğunu sen nereden bileceksin? (DEMO meal)", 30),
				a(97, 3, "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ", "Kadir gecesi bin aydan hayırlıdır. (DEMO meal)", 30),
				a(97, 4, "تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ",
						"Melekler ve Ruh o gece Rablerinin izniyle her iş için iner. (DEMO meal)", 30),
				a(97, 5, "سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ", "O gece tan yeri ağarıncaya kadar esenliktir. (DEMO meal)", 30),
				a(94, 1, "أَلَمْ نَشْرَحْ لَكَ صَدْرَكَ", "Biz senin göğsünü açmadık mı? (DEMO meal)", 30),
				a(94, 2, "وَوَضَعْنَا عَنكَ وِزْرَكَ", "Belini büken yükünü senden kaldırmadık mı? (DEMO meal)", 30),
				a(94, 3, "الَّذِي أَنقَضَ ظَهْرَكَ", "O yük ki sırtını çökertmişti. (DEMO meal)", 30),
				a(94, 4, "وَرَفَعْنَا لَكَ ذِكْرَكَ", "Senin şanını yüceltmedik mi? (DEMO meal)", 30),
				a(94, 5, "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا", "Elbette güçlükle beraber bir kolaylık vardır. (DEMO meal)", 30),
				a(94, 6, "إِنَّ مَعَ الْعُسْرِ يُسْرًا", "Gerçekten, güçlükle beraber bir kolaylık vardır. (DEMO meal)", 30),
				a(94, 7, "فَإِذَا فَرَغْتَ فَانصَبْ", "Öyleyse boş kaldığında yine çalış. (DEMO meal)", 30),
				a(94, 8, "وَإِلَىٰ رَبِّكَ فَارْغَبْ", "Ve yalnız Rabbine yönel. (DEMO meal)", 30),
				a(93, 1, "وَالضُّحَىٰ", "Kuşluk vaktine andolsun. (DEMO meal)", 30),
				a(93, 2, "وَاللَّيْلِ إِذَا سَجَىٰ", "Sakinleştiği zaman geceye andolsun. (DEMO meal)", 30),
				a(93, 3, "مَا وَدَّعَكَ رَبُّكَ وَمَا قَلَىٰ", "Rabbin seni bırakmadı ve darılmadı. (DEMO meal)", 30),
				a(93, 4, "وَلَلْآخِرَةُ خَيْرٌ لَّكَ مِنَ الْأُولَىٰ", "Elbette sonraki, senin için öncekinden daha hayırlıdır. (DEMO meal)", 30),
				a(93, 5, "وَلَسَوْفَ يُعْطِيكَ رَبُّكَ فَتَرْضَىٰ", "Rabbin sana verecek ve sen hoşnut olacaksın. (DEMO meal)", 30)));
		return list;
	}

	private static SurahDef s(int n, String ar, String tr, String en, int count, String rev, int juz) {
		return new SurahDef(n, ar, tr, en, count, rev, juz);
	}

	private static AyahDef a(int surah, int n, String ar, String tr, int juz) {
		return new AyahDef(surah, n, ar, tr, juz);
	}
}
