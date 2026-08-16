# Görsel kaynakları

Yerel dosyalar `frontend/public/images/` altındadır. Wikimedia Commons görselleri indirilip WebP’ye dönüştürülmüştür (en fazla 1280px, metadata temizlenmiştir).

Bu portal kavramsal bir çalışmadır; Diyanet İşleri Başkanlığı logosu kopyalanmamıştır. Marka işareti projeye ait SVG’dir (`frontend/src/components/brand/mark.tsx`).

CC BY-SA görseller için atıf bu belgede ve sitedeki [Görsel kaynakları](/gorsel-kaynaklari) sayfasında tutulur.

| Görsel | Kaynak | Lisans | Kullanıldığı yer |
|---|---|---|---|
| `hero/mosque-courtyard.webp` | [Innenhof Blaue Moschee Istanbul.jpg](https://commons.wikimedia.org/wiki/File:Innenhof_Blaue_Moschee_Istanbul.jpg) — Herbert wie | CC BY-SA 4.0 | Ana sayfa hero; Başkanlıktan haber kapakları |
| `news/mosque-exterior.webp` | [Exterior of Sultan Ahmed I Mosque.jpg](https://commons.wikimedia.org/wiki/File:Exterior_of_Sultan_Ahmed_I_Mosque.jpg) | Public domain | Gündem/dünya haberleri; il müftülükleri bandı |
| `news/mosque-interior.webp` | [Suleymaniye Mosque.jpg](https://commons.wikimedia.org/wiki/File:Suleymaniye_Mosque.jpg) — Emna Mizouni | CC BY-SA 4.0 | Din hizmetleri haberleri; hutbe; etkinlik; medya |
| `news/mosque-dome.webp` | [Sultan Ahmed Mosque interior – panoramio](https://commons.wikimedia.org/wiki/File:Sultan_Ahmed_Mosque_interior_-_Istanbul,_Turkey_-_panoramio.jpg) — Sergey Ashmarin | CC BY-SA 3.0 | Kültür haberleri |
| `news/masjid-al-haram.webp` | [Great Mosque of Mecca.jpg](https://commons.wikimedia.org/wiki/File:Great_Mosque_of_Mecca.jpg) — saudipics | CC BY-SA 4.0 | Hac-umre haberleri; Hac ve Umre sayfası |
| `quran/mamluk-manuscript.webp` | [Mamluk era Quran, circa 1380](https://commons.wikimedia.org/wiki/File:Mamluk_era_Quran,_circa_1380,_open_to_sura_16.jpg) — Mustafa-trit20 | CC BY-SA 4.0 | Kur’an ve hadis sayfaları; eğitim haberleri |
| `publications/ottoman-quran-leaf.webp` | [Quran.jpg](https://commons.wikimedia.org/wiki/File:Quran.jpg) — 17. yy. Osmanlı yaprağı | Public domain | Yayın kapakları ve yayın detayı |

## Kullanılmayan yerler

Fetva ve namaz vakitleri sayfalarına görsel eklenmedi: içerik metin ve tablo ağırlıklıdır.

## Yeniden indirme

```bash
python frontend/scripts/fetch-images.py
```

Wikimedia küçük resim istekleri yalnızca standart genişliklerde kabul edilir (`1280px` bu projede kullanılmıştır). İndirme betiği uygun bir User-Agent gönderir.
