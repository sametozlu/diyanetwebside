import type { Metadata } from "next";
import { notFound } from "next/navigation";
import { HtmlContent } from "@/components/ui/html-content";
import { CoverImage } from "@/components/ui/cover-image";
import { Breadcrumb, PageShell } from "@/components/ui/page-intro";
import { Badge } from "@/components/ui/section";
import { Link } from "@/i18n/navigation";
import { apiGet } from "@/lib/api";
import { siteUrl } from "@/lib/site";
import { formatDate } from "@/lib/utils";
import type { NewsDetail } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 120;

export async function generateMetadata({ params }: { params: Promise<{ slug: string }> }): Promise<Metadata> {
  const { slug } = await params;
  try {
    const news = await apiGet<NewsDetail>(`/api/news/${slug}`);
    return {
      title: news.title,
      description: news.summary ?? undefined,
      openGraph: { title: news.title, description: news.summary ?? undefined, images: news.imageUrl ? [news.imageUrl] : [] },
    };
  } catch {
    const t = await getTranslations("pages");
    return { title: t("newsFallback") };
  }
}

export default async function NewsDetailPage({ params }: { params: Promise<{ slug: string }> }) {
  const t = await getTranslations();
  const { slug } = await params;
  let news: NewsDetail;
  try {
    news = await apiGet<NewsDetail>(`/api/news/${slug}`);
  } catch {
    notFound();
  }
  const jsonLd = {
    "@context": "https://schema.org",
    "@type": "NewsArticle",
    headline: news.title,
    datePublished: news.publishedAt,
    description: news.summary,
    url: `${siteUrl()}/haberler/${news.slug}`,
  };
  return (
    <PageShell width="narrow">
      <article>
        <script type="application/ld+json" dangerouslySetInnerHTML={{ __html: JSON.stringify(jsonLd) }} />
        <Breadcrumb
          items={[
            { href: "/", label: t("common.home") },
            { href: "/haberler", label: t("pages.newsTitle") },
            { label: news.title },
          ]}
        />
        {news.category ? <Badge>{news.category.name}</Badge> : null}
        <h1 className="mt-3 font-serif text-3xl font-semibold leading-tight md:text-[2.1rem]">{news.title}</h1>
        <p className="mt-3 text-sm text-muted">{formatDate(news.publishedAt)}</p>
        {news.imageUrl ? (
          <div className="mt-6 overflow-hidden">
            <CoverImage src={news.imageUrl} alt={news.title} ratio="hero" priority sizes="(max-width: 768px) 100vw, 768px" />
          </div>
        ) : null}
        {news.summary ? <p className="mt-6 text-lg leading-8 text-muted">{news.summary}</p> : null}
        <HtmlContent className="prose-portal mt-6" html={news.body} />
        <h2 className="mt-12 font-serif text-xl">İlgili haberler</h2>
        {news.related?.length ? (
          <ul className="mt-4 divide-y divide-line border-y border-line">
            {news.related.map((item) => (
              <li key={item.id} className="py-3">
                <Link href={`/haberler/${item.slug}`} className="text-forest hover:underline">{item.title}</Link>
              </li>
            ))}
          </ul>
        ) : (
          <p className="mt-3 text-sm text-muted">Bu kategoride başka yayın bulunmuyor.</p>
        )}
      </article>
    </PageShell>
  );
}
