import { getTranslations } from "next-intl/server";
import { Mark } from "@/components/brand/mark";
import { Link } from "@/i18n/navigation";

export async function Footer() {
  const t = await getTranslations("footer");
  const nav = await getTranslations("nav");
  const brand = await getTranslations("brand");
  return (
    <footer className="mt-16 border-t border-line bg-forest-deep text-white">
      <div className="mx-auto grid max-w-7xl gap-10 px-4 py-12 sm:grid-cols-2 lg:grid-cols-5">
        <div className="lg:col-span-1">
          <div className="flex items-center gap-3">
            <Mark />
            <div>
              <p className="font-serif text-lg">{brand("name")}</p>
              <p className="text-xs text-white/70">{brand("tagline")}</p>
            </div>
          </div>
          <p className="mt-4 max-w-sm text-sm leading-6 text-white/75">{t("copy")}</p>
        </div>
        <div>
          <p className="mb-3 text-xs font-semibold uppercase tracking-wider text-white/60">{t("about")}</p>
          <ul className="space-y-2 text-sm text-white/85">
            <li><Link href="/baskanligimiz" className="hover:underline">{t("about")}</Link></li>
            <li><Link href="/hizmetler">{t("services")}</Link></li>
            <li><Link href="/il-muftulukleri">{nav("provinces")}</Link></li>
            <li><Link href="/iletisim">{t("contact")}</Link></li>
          </ul>
        </div>
        <div>
          <p className="mb-3 text-xs font-semibold uppercase tracking-wider text-white/60">{t("religious")}</p>
          <ul className="space-y-2 text-sm text-white/85">
            <li><Link href="/kuran">{nav("quran")}</Link></li>
            <li><Link href="/hadis">{nav("hadith")}</Link></li>
            <li><Link href="/fetva">{nav("fatwa")}</Link></li>
            <li><Link href="/hutbeler">{nav("sermons")}</Link></li>
            <li><Link href="/namaz-vakitleri">{nav("prayerTimes")}</Link></li>
            <li><Link href="/dini-bilgiler">{nav("religious")}</Link></li>
          </ul>
        </div>
        <div>
          <p className="mb-3 text-xs font-semibold uppercase tracking-wider text-white/60">{t("publications")}</p>
          <ul className="space-y-2 text-sm text-white/85">
            <li><Link href="/yayinlar">{t("publications")}</Link></li>
            <li><Link href="/medya">{t("media")}</Link></li>
            <li><Link href="/haberler">{t("news")}</Link></li>
            <li><Link href="/etkinlikler">{t("events")}</Link></li>
          </ul>
        </div>
        <div>
          <p className="mb-3 text-xs font-semibold uppercase tracking-wider text-white/60">{t("digital")}</p>
          <ul className="space-y-2 text-sm text-white/85">
            <li><Link href="/arama">{t("search")}</Link></li>
            <li><Link href="/cami-bul">{t("findMosque")}</Link></li>
            <li><Link href="/hac-umre">{t("hajj")}</Link></li>
            <li><Link href="/dini-gunler">{t("religiousDays")}</Link></li>
            <li><Link href="/erisilebilirlik">{t("accessibility")}</Link></li>
          </ul>
        </div>
      </div>
      <div className="border-t border-white/10">
        <div className="mx-auto flex max-w-7xl flex-wrap items-center gap-x-5 gap-y-2 px-4 py-4 text-xs text-white/60">
          <span>© {new Date().getFullYear()} {brand("name")}</span>
          <Link href="/gizlilik">{t("privacy")}</Link>
          <Link href="/kvkk">{t("kvkk")}</Link>
          <Link href="/cerez-politikasi">{t("cookies")}</Link>
          <Link href="/kullanim-kosullari">{t("terms")}</Link>
          <Link href="/erisilebilirlik">{t("accessibility")}</Link>
          <Link href="/gorsel-kaynaklari">{t("imageSources")}</Link>
          <Link href="/arama">{t("sitemap")}</Link>
        </div>
      </div>
    </footer>
  );
}
