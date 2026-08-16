import type { Metadata } from "next";
import { notFound } from "next/navigation";
import { QuranReader } from "@/features/quran/quran-reader";
import { Breadcrumb, PageShell } from "@/components/ui/page-intro";
import { apiGet, apiGetSafe } from "@/lib/api";
import type { SurahDetail, SurahSummary } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 3600;

export async function generateMetadata({ params }: { params: Promise<{ number: string }> }): Promise<Metadata> {
  const { number } = await params;
  try {
    const surah = await apiGet<SurahDetail>(`/api/quran/surahs/${number}`);
    const t = await getTranslations("pages");
    return { title: t("surahTitle", { name: surah.nameTr }) };
  } catch {
    const t = await getTranslations("pages");
    return { title: t("surahFallback") };
  }
}

export default async function SurahPage({ params }: { params: Promise<{ number: string }> }) {
  const t = await getTranslations();
  const { number } = await params;
  const n = Number(number);
  if (!n) notFound();
  let surah: SurahDetail;
  try {
    surah = await apiGet<SurahDetail>(`/api/quran/surahs/${n}`);
  } catch {
    notFound();
  }
  const surahs = await apiGetSafe<SurahSummary[]>("/api/quran/surahs", []);
  return (
    <PageShell width="wide">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { href: "/kuran", label: t("pages.quranTitle") }, { label: surah.nameTr }]} />
      <h1 className="sr-only">{t("pages.surahTitle", { name: surah.nameTr })}</h1>
      <QuranReader initial={surah} surahs={surahs} />
    </PageShell>
  );
}
