import { Breadcrumb, PageIntro } from "@/components/ui/page-intro";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { Link } from "@/i18n/navigation";
import { apiTry } from "@/lib/api";
import { pagesMeta } from "@/lib/page-meta";
import type { ProvinceSummary } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const generateMetadata = () => pagesMeta("findMosqueTitle");

export default async function MosqueFinderPage() {
  const t = await getTranslations();
  const result = await apiTry<ProvinceSummary[]>("/api/provinces");
  const provinces = result.ok ? result.data : [];
  return (
    <main className="mx-auto max-w-4xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.findMosqueTitle") }]} />
      <PageIntro title={t("pages.findMosqueTitle")} lead={t("pages.findMosqueLead")} />
      <EmptyState title={t("pages.noMosqueCoords")} description={t("pages.noMosqueCoordsHint")} />
      {!result.ok ? (
        <div className="mt-6"><ErrorState retryHref="/cami-bul" /></div>
      ) : (
        <div className="mt-6 grid grid-cols-2 gap-2 sm:grid-cols-3">
          {provinces.map((p) => (
            <Link key={p.slug} href={`/il-muftulukleri/${p.slug}`} className="rounded-lg border border-line px-3 py-2 text-sm">
              {p.name}
            </Link>
          ))}
        </div>
      )}
    </main>
  );
}
