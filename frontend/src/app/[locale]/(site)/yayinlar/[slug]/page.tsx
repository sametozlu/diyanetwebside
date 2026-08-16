import { notFound } from "next/navigation";
import { HtmlContent } from "@/components/ui/html-content";
import { CoverImage } from "@/components/ui/cover-image";
import { Breadcrumb } from "@/components/ui/page-intro";
import { apiGet } from "@/lib/api";
import { formatDate } from "@/lib/utils";
import { getTranslations } from "next-intl/server";
import type { Publication } from "@/types/api";

export default async function PublicationDetail({ params }: { params: Promise<{ slug: string }> }) {
  const t = await getTranslations();
  const tTypes = await getTranslations("types");
  const { slug } = await params;
  let item: Publication;
  try {
    item = await apiGet<Publication>(`/api/publications/${slug}`);
  } catch {
    notFound();
  }
  return (
    <article className="mx-auto max-w-3xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { href: "/yayinlar", label: t("pages.publicationsTitle") }, { label: item.title }]} />
      <p className="kicker">{item.type ? tTypes(item.type) : ""} · {item.author} · {formatDate(item.publishedAt)}</p>
      <h1 className="mt-2 font-serif text-3xl">{item.title}</h1>
      {item.coverUrl ? (
        <div className="mt-6 max-w-[14rem]">
          <CoverImage
            src={item.coverUrl}
            alt={item.title}
            ratio="portrait"
            sizes="224px"
          />
        </div>
      ) : null}
      <p className="mt-4 text-muted">{item.summary}</p>
      <HtmlContent className="prose-portal mt-6" html={item.body} />
      {item.fileUrl ? <a className="mt-6 inline-block text-forest underline" href={item.fileUrl}>{t("pages.downloadFile")}</a> : null}
    </article>
  );
}
