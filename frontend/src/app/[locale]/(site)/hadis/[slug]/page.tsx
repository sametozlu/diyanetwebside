import { notFound } from "next/navigation";
import { Breadcrumb } from "@/components/ui/page-intro";
import { apiGet } from "@/lib/api";
import type { Hadith } from "@/types/api";
import { getTranslations } from "next-intl/server";

export default async function HadithDetail({ params }: { params: Promise<{ slug: string }> }) {
  const t = await getTranslations();
  const { slug } = await params;
  let item: Hadith;
  try {
    item = await apiGet<Hadith>(`/api/hadith/${slug}`);
  } catch {
    notFound();
  }
  return (
    <article className="mx-auto max-w-3xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { href: "/hadis", label: t("pages.hadithTitle") }, { label: item.title }]} />
      <p className="arabic text-right text-3xl leading-loose" dir="rtl">{item.textAr}</p>
      <h1 className="mt-6 font-serif text-3xl">{item.title}</h1>
      <p className="mt-4 text-lg leading-8">{item.textTr}</p>
      <p className="mt-6 text-sm text-muted">{t("common.source")}: {item.source} · {t("pages.narrator")}: {item.narrator}</p>
    </article>
  );
}
