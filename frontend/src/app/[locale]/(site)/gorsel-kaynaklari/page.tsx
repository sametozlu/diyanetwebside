import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { Link } from "@/i18n/navigation";
import { pagesMeta } from "@/lib/page-meta";
import { getTranslations } from "next-intl/server";

export const generateMetadata = () => pagesMeta("imageSourcesTitle");

const ROWS = [
  {
    file: "mosque-courtyard.webp",
    source: "https://commons.wikimedia.org/wiki/File:Innenhof_Blaue_Moschee_Istanbul.jpg",
    license: "CC BY-SA 4.0 — Herbert wie",
    usedKey: "imageUsedHero",
  },
  {
    file: "mosque-exterior.webp",
    source: "https://commons.wikimedia.org/wiki/File:Exterior_of_Sultan_Ahmed_I_Mosque.jpg",
    license: "Public domain",
    usedKey: "imageUsedNews",
  },
  {
    file: "mosque-interior.webp",
    source: "https://commons.wikimedia.org/wiki/File:Suleymaniye_Mosque.jpg",
    license: "CC BY-SA 4.0 — Emna Mizouni",
    usedKey: "imageUsedInterior",
  },
  {
    file: "mosque-dome.webp",
    source: "https://commons.wikimedia.org/wiki/File:Sultan_Ahmed_Mosque_interior_-_Istanbul,_Turkey_-_panoramio.jpg",
    license: "CC BY-SA 3.0 — Sergey Ashmarin",
    usedKey: "imageUsedCulture",
  },
  {
    file: "masjid-al-haram.webp",
    source: "https://commons.wikimedia.org/wiki/File:Great_Mosque_of_Mecca.jpg",
    license: "CC BY-SA 4.0 — saudipics",
    usedKey: "imageUsedHajj",
  },
  {
    file: "mamluk-manuscript.webp",
    source: "https://commons.wikimedia.org/wiki/File:Mamluk_era_Quran,_circa_1380,_open_to_sura_16.jpg",
    license: "CC BY-SA 4.0 — Mustafa-trit20",
    usedKey: "imageUsedQuran",
  },
  {
    file: "ottoman-quran-leaf.webp",
    source: "https://commons.wikimedia.org/wiki/File:Quran.jpg",
    license: "Public domain",
    usedKey: "imageUsedCovers",
  },
] as const;

export default async function ImageSourcesPage() {
  const t = await getTranslations();
  return (
    <PageShell>
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.imageSourcesTitle") }]} />
      <PageIntro title={t("pages.imageSourcesTitle")} lead={t("pages.imageSourcesLead")} />
      <div className="overflow-x-auto border border-line bg-white">
        <table className="w-full min-w-[40rem] text-left text-sm">
          <thead className="border-b border-line bg-paper-2 text-xs uppercase tracking-wider text-muted">
            <tr>
              <th className="px-3 py-3 font-medium">{t("pages.imageCol")}</th>
              <th className="px-3 py-3 font-medium">{t("pages.sourceCol")}</th>
              <th className="px-3 py-3 font-medium">{t("pages.licenseCol")}</th>
              <th className="px-3 py-3 font-medium">{t("pages.usedCol")}</th>
            </tr>
          </thead>
          <tbody>
            {ROWS.map((row) => (
              <tr key={row.file} className="border-b border-line last:border-0">
                <td className="px-3 py-3 font-medium">{row.file}</td>
                <td className="px-3 py-3">
                  <a href={row.source} className="text-forest hover:underline" rel="noreferrer" target="_blank">
                    Wikimedia Commons
                  </a>
                </td>
                <td className="px-3 py-3">{row.license}</td>
                <td className="px-3 py-3 text-muted">{t(`pages.${row.usedKey}`)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <p className="mt-6 text-sm text-muted">
        {t("pages.imageSourcesNote")}{" "}
        <Link href="/kullanim-kosullari" className="text-forest hover:underline">
          {t("footer.terms")}
        </Link>
      </p>
    </PageShell>
  );
}
