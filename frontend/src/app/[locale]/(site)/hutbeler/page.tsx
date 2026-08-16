import { CoverImage } from "@/components/ui/cover-image";
import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { Link } from "@/i18n/navigation";
import { apiTry } from "@/lib/api";
import { SITE_IMAGES } from "@/lib/images";
import { pagesMeta } from "@/lib/page-meta";
import { emptyPage, type Paged, type Sermon } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 120;
export const generateMetadata = () => pagesMeta("sermonsTitle");

export default async function SermonsPage() {
  const t = await getTranslations();
  const result = await apiTry<Paged<Sermon>>("/api/sermons?size=20");
  const data = result.ok ? result.data : emptyPage<Sermon>();
  const latest = data.content[0];
  return (
    <PageShell>
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.sermonsTitle") }]} />
      <PageIntro title={t("pages.sermonsTitle")} lead={t("pages.sermonsLead")} />
      {!result.ok ? (
        <ErrorState retryHref="/hutbeler" />
      ) : !latest ? (
        <EmptyState title={t("pages.noSermons")} />
      ) : (
        <>
          <article className="mb-10 grid gap-6 border-b border-line pb-8 lg:grid-cols-[1.1fr_0.9fr] lg:items-center">
            <CoverImage
              src={SITE_IMAGES.mosqueInterior.src}
              alt={SITE_IMAGES.mosqueInterior.alt}
              ratio="hero"
              sizes="(max-width: 1024px) 100vw, 560px"
            />
            <div>
              <p className="kicker">{t("pages.weekSermon")}</p>
              <h2 className="mt-2 font-serif text-2xl md:text-3xl">{latest.title}</h2>
              <p className="mt-2 max-w-2xl text-muted">{latest.summary}</p>
              <Link href={`/hutbeler/${latest.slug}`} className="mt-4 inline-block text-sm font-medium text-forest hover:underline">{t("common.read")}</Link>
            </div>
          </article>
          <ul className="divide-y divide-line border-y border-line">
            {data.content.map((s) => (
              <li key={s.id}>
                <Link href={`/hutbeler/${s.slug}`} className="flex items-baseline justify-between gap-4 py-4 hover:text-forest">
                  <div>
                    <p className="text-xs text-muted">{s.sermonDate}</p>
                    <h3 className="font-serif text-lg">{s.title}</h3>
                  </div>
                  <span className="shrink-0 text-sm text-muted">{s.pdfUrl ? t("common.download") : t("common.text")}</span>
                </Link>
              </li>
            ))}
          </ul>
        </>
      )}
    </PageShell>
  );
}
