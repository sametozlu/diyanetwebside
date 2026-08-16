import { notFound } from "next/navigation";
import { HtmlContent } from "@/components/ui/html-content";
import { Breadcrumb } from "@/components/ui/page-intro";
import { apiGet } from "@/lib/api";
import type { Sermon } from "@/types/api";
import { getTranslations } from "next-intl/server";

export default async function SermonDetail({ params }: { params: Promise<{ slug: string }> }) {
  const t = await getTranslations();
  const { slug } = await params;
  let item: Sermon;
  try {
    item = await apiGet<Sermon>(`/api/sermons/${slug}`);
  } catch {
    notFound();
  }
  return (
    <article className="mx-auto max-w-3xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { href: "/hutbeler", label: t("pages.sermonsTitle") }, { label: item.title }]} />
      <p className="text-xs text-muted">{item.sermonDate} · {item.preacher}</p>
      <h1 className="mt-2 font-serif text-4xl">{item.title}</h1>
      <p className="mt-4 text-muted">{item.summary}</p>
      <HtmlContent className="prose-portal mt-6" html={item.body} />
      <div className="mt-8 flex gap-3">
        {item.pdfUrl ? <a className="text-forest underline" href={item.pdfUrl}>{t("pages.downloadPdf")}</a> : null}
        {item.audioUrl ? <audio controls src={item.audioUrl} className="w-full" /> : null}
      </div>
    </article>
  );
}
