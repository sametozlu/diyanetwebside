import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { CoverImage } from "@/components/ui/cover-image";
import { Chip } from "@/components/ui/section";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { Link } from "@/i18n/navigation";
import { apiTry } from "@/lib/api";
import { pagesMeta } from "@/lib/page-meta";
import { emptyPage, type MediaItem, type Paged } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 120;
export const generateMetadata = () => pagesMeta("mediaTitle");

const TYPES = ["VIDEO", "RADIO", "PODCAST", "LIVE"] as const;

export default async function MediaPage({ searchParams }: { searchParams: Promise<{ type?: string }> }) {
  const t = await getTranslations();
  const sp = await searchParams;
  const qs = new URLSearchParams({ size: "20" });
  if (sp.type) qs.set("type", sp.type);
  const result = await apiTry<Paged<MediaItem>>(`/api/media?${qs}`);
  const data = result.ok ? result.data : emptyPage<MediaItem>();
  return (
    <PageShell width="wide">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.mediaTitle") }]} />
      <PageIntro title={t("pages.mediaTitle")} lead={t("pages.mediaLead")} />
      <div className="mb-8 flex flex-wrap gap-2">
        <Chip href="/medya" active={!sp.type}>{t("common.all")}</Chip>
        {TYPES.map((type) => (
          <Chip key={type} href={`/medya?type=${type}`} active={sp.type === type}>
            {t(`pages.media${type}`)}
          </Chip>
        ))}
      </div>
      {!result.ok ? (
        <ErrorState retryHref="/medya" />
      ) : data.content.length === 0 ? (
        <EmptyState title={t("pages.emptyFilter")} />
      ) : (
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {data.content.map((m) => (
            <Link key={m.id} href={`/medya/${m.slug}`} className="group block">
              <CoverImage src={m.thumbnailUrl} alt={m.title} ratio="video" sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 360px" />
              <div className="pt-3">
                <p className="kicker">{t(`pages.media${m.type}` as "pages.mediaVIDEO")}</p>
                <h2 className="mt-1 font-serif text-lg group-hover:text-forest">{m.title}</h2>
              </div>
            </Link>
          ))}
        </div>
      )}
    </PageShell>
  );
}
