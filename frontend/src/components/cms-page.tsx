import { notFound } from "next/navigation";
import { HtmlContent } from "@/components/ui/html-content";
import { Breadcrumb } from "@/components/ui/page-intro";
import { apiGet } from "@/lib/api";
import type { PageDto } from "@/types/api";
import { getTranslations } from "next-intl/server";

export async function CmsPage({ slug }: { slug: string }) {
  const t = await getTranslations("common");
  let page: PageDto;
  try {
    page = await apiGet<PageDto>(`/api/pages/${slug}`);
  } catch {
    notFound();
  }
  return (
    <article className="mx-auto max-w-3xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("home") }, { label: page.title }]} />
      <h1 className="font-serif text-3xl">{page.title}</h1>
      <HtmlContent className="prose-portal mt-6" html={page.body} />
    </article>
  );
}
