import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { PublicationCard } from "@/components/ui/publication-card";
import { Chip, SearchBar } from "@/components/ui/section";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { apiTry } from "@/lib/api";
import { pagesMeta } from "@/lib/page-meta";
import { emptyPage, type Paged, type Publication } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 120;
export const generateMetadata = () => pagesMeta("publicationsTitle");

const TYPES = ["BOOK", "MAGAZINE", "ARTICLE", "RESEARCH", "ARCHIVE"] as const;

export default async function PublicationsPage({
  searchParams,
}: {
  searchParams: Promise<{ type?: string; q?: string }>;
}) {
  const t = await getTranslations();
  const tTypes = await getTranslations("types");
  const sp = await searchParams;
  const qs = new URLSearchParams({ size: "20" });
  if (sp.type) qs.set("type", sp.type);
  if (sp.q) qs.set("q", sp.q);
  const result = await apiTry<Paged<Publication>>(`/api/publications?${qs}`);
  const data = result.ok ? result.data : emptyPage<Publication>();
  return (
    <PageShell width="wide">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.publicationsTitle") }]} />
      <PageIntro title={t("pages.publicationsTitle")} lead={t("pages.publicationsLead")} />
      <SearchBar defaultValue={sp.q} placeholder={t("pages.publicationSearch")}>
        {sp.type ? <input type="hidden" name="type" value={sp.type} /> : null}
      </SearchBar>
      <div className="mb-8 flex flex-wrap gap-2">
        <Chip href="/yayinlar" active={!sp.type}>{t("common.all")}</Chip>
        {TYPES.map((type) => (
          <Chip key={type} href={`/yayinlar?type=${type}`} active={sp.type === type}>
            {tTypes(type)}
          </Chip>
        ))}
      </div>
      {!result.ok ? (
        <ErrorState retryHref="/yayinlar" />
      ) : data.content.length === 0 ? (
        <EmptyState title={t("pages.emptyFilter")} />
      ) : (
        <div className="grid gap-8 sm:grid-cols-2 lg:grid-cols-4">
          {data.content.map((p) => (
            <PublicationCard key={p.id} item={p} />
          ))}
        </div>
      )}
    </PageShell>
  );
}
