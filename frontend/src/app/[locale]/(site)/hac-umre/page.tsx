import { PageBanner } from "@/components/ui/page-banner";
import { Breadcrumb, PageIntro } from "@/components/ui/page-intro";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { Link } from "@/i18n/navigation";
import { apiTry } from "@/lib/api";
import { SITE_IMAGES } from "@/lib/images";
import { pagesMeta } from "@/lib/page-meta";
import { emptyPage, type Fatwa, type NewsSummary, type Paged, type Publication } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const generateMetadata = () => pagesMeta("hajjTitle");

export default async function HajjPage() {
  const t = await getTranslations();
  const [news, fatwas, publications] = await Promise.all([
    apiTry<Paged<NewsSummary>>("/api/news?category=hac-umre&size=6"),
    apiTry<Paged<Fatwa>>("/api/fatwas?category=ibadet&size=6"),
    apiTry<Paged<Publication>>("/api/publications?size=6"),
  ]);
  const newsItems = news.ok ? news.data : emptyPage<NewsSummary>();
  return (
    <main className="mx-auto max-w-5xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.hajjTitle") }]} />
      <PageIntro title={t("pages.hajjTitle")} lead={t("pages.hajjLead")} />
      <PageBanner src={SITE_IMAGES.masjidAlHaram.src} alt={SITE_IMAGES.masjidAlHaram.alt} />
      <div className="grid gap-6 md:grid-cols-2">
        <div className="border border-line bg-forest p-6 text-white">
          <h2 className="font-serif text-2xl">{t("pages.hajjCardTitle")}</h2>
          <p className="mt-2 text-sm leading-6 text-white/80">{t("pages.hajjCardLead")}</p>
        </div>
        <div className="border border-line bg-white p-6">
          <h2 className="font-serif text-2xl">{t("pages.umrahCardTitle")}</h2>
          <p className="mt-2 text-sm leading-6 text-muted">{t("pages.umrahCardLead")}</p>
        </div>
      </div>
      <h2 className="mt-10 font-serif text-2xl">{t("pages.announcements")}</h2>
      {!news.ok ? <ErrorState retryHref="/hac-umre" /> : newsItems.content.length ? (
        <ul className="mt-3 grid gap-2">
          {newsItems.content.map((n) => (
            <li key={n.id}><Link href={`/haberler/${n.slug}`} className="text-forest hover:underline">{n.title}</Link></li>
          ))}
        </ul>
      ) : <EmptyState title={t("pages.emptyFilter")} />}
      <h2 className="mt-10 font-serif text-2xl">{t("pages.relatedQuestions")}</h2>
      {fatwas.ok && fatwas.data.content.length ? (
        <ul className="mt-3 grid gap-2">
          {fatwas.data.content.map((f) => (
            <li key={f.id}><Link href={`/fetva/${f.slug}`} className="text-forest hover:underline">{f.question}</Link></li>
          ))}
        </ul>
      ) : <EmptyState title={t("pages.noRelatedQuestions")} />}
      <h2 className="mt-10 font-serif text-2xl">{t("pages.guidePublications")}</h2>
      {publications.ok && publications.data.content.length ? (
        <ul className="mt-3 grid gap-2">
          {publications.data.content.slice(0, 4).map((p) => (
            <li key={p.id}><Link href={`/yayinlar/${p.slug}`} className="text-forest hover:underline">{p.title}</Link></li>
          ))}
        </ul>
      ) : <EmptyState title={t("pages.noPublications")} />}
    </main>
  );
}
