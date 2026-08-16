import { NewsCard } from "@/components/ui/news-card";
import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { Chip, Pagination } from "@/components/ui/section";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { apiTry } from "@/lib/api";
import { pagesMeta } from "@/lib/page-meta";
import { emptyPage, type Category, type NewsSummary, type Paged } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 60;
export const generateMetadata = () => pagesMeta("newsTitle");

function newsHref(category: string, page: number) {
  const qs = new URLSearchParams();
  if (category) qs.set("category", category);
  if (page > 0) qs.set("page", String(page));
  const query = qs.toString();
  return query ? `/haberler?${query}` : "/haberler";
}

export default async function NewsPage({
  searchParams,
}: {
  searchParams: Promise<{ category?: string; page?: string }>;
}) {
  const t = await getTranslations();
  const sp = await searchParams;
  const category = sp.category ?? "";
  const page = Number(sp.page ?? 0);
  const qs = new URLSearchParams({ size: "12", page: String(Number.isFinite(page) && page > 0 ? page : 0) });
  if (category) qs.set("category", category);
  const [news, categories] = await Promise.all([
    apiTry<Paged<NewsSummary>>(`/api/news?${qs}`),
    apiTry<Category[]>("/api/news/categories"),
  ]);
  const data = news.ok ? news.data : emptyPage<NewsSummary>();
  const [lead, ...rest] = page === 0 ? data.content : [];
  const gridItems = page === 0 ? rest : data.content;

  return (
    <PageShell width="wide">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.newsTitle") }]} />
      <PageIntro title={t("pages.newsTitle")} lead={t("pages.newsLead")} />
      <div className="mb-8 flex flex-wrap gap-2">
        <Chip href="/haberler" active={!category}>{t("common.all")}</Chip>
        {categories.ok
          ? categories.data.map((c) => (
              <Chip key={c.slug} href={`/haberler?category=${c.slug}`} active={category === c.slug}>
                {c.name}
              </Chip>
            ))
          : null}
      </div>
      {!news.ok ? (
        <ErrorState retryHref="/haberler" />
      ) : data.content.length === 0 ? (
        <EmptyState title={t("pages.emptyFilter")} description={t("pages.emptyFilterHint")} />
      ) : (
        <>
          {lead ? (
            <div className="mb-10 grid gap-8 border-b border-line pb-10 lg:grid-cols-[1.4fr_0.8fr]">
              <NewsCard item={lead} variant="featured" />
              <div className="flex flex-col gap-6">
                {rest.slice(0, 3).map((item) => (
                  <NewsCard key={item.id} item={item} variant="row" />
                ))}
              </div>
            </div>
          ) : null}
          <div className="grid gap-8 sm:grid-cols-2 lg:grid-cols-3">
            {(lead ? rest.slice(3) : gridItems).map((item) => (
              <NewsCard key={item.id} item={item} />
            ))}
          </div>
        </>
      )}
      <Pagination page={page} totalPages={data.totalPages} hrefFor={(p) => newsHref(category, p)} />
    </PageShell>
  );
}
