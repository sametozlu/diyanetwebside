import { QuranReader } from "@/features/quran/quran-reader";
import { PageBanner } from "@/components/ui/page-banner";
import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { ErrorState } from "@/components/ui/states";
import { apiGetSafe } from "@/lib/api";
import { SITE_IMAGES } from "@/lib/images";
import { pagesMeta } from "@/lib/page-meta";
import type { SurahDetail, SurahSummary } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 3600;
export const generateMetadata = () => pagesMeta("quranTitle");

export default async function QuranPage() {
  const t = await getTranslations();
  const [surahs, fatiha] = await Promise.all([
    apiGetSafe<SurahSummary[]>("/api/quran/surahs", []),
    apiGetSafe<SurahDetail | null>("/api/quran/surahs/1", null),
  ]);
  return (
    <PageShell width="wide">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.quranTitle") }]} />
      <PageIntro title={t("pages.quranTitle")} lead={t("pages.quranLead")} />
      <PageBanner src={SITE_IMAGES.quranManuscript.src} alt={SITE_IMAGES.quranManuscript.alt} />
      {fatiha ? <QuranReader initial={fatiha} surahs={surahs} /> : (
        <ErrorState retryHref="/kuran" description={t("pages.quranUnavailable")} />
      )}
    </PageShell>
  );
}
