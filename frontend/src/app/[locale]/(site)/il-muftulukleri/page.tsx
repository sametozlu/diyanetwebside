import { PageBanner } from "@/components/ui/page-banner";
import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { TurkeyMap } from "@/features/provinces/turkey-map";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { Link } from "@/i18n/navigation";
import { apiTry } from "@/lib/api";
import { SITE_IMAGES } from "@/lib/images";
import { pagesMeta } from "@/lib/page-meta";
import type { ProvinceSummary } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 3600;
export const generateMetadata = () => pagesMeta("provincesTitle");

export default async function ProvincesPage() {
  const t = await getTranslations();
  const result = await apiTry<ProvinceSummary[]>("/api/provinces");
  const provinces = result.ok ? result.data : [];
  return (
    <PageShell width="wide">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.provincesTitle") }]} />
      <PageIntro title={t("pages.provincesTitle")} lead={t("pages.provincesLead")} />
      <PageBanner src={SITE_IMAGES.mosqueExterior.src} alt={SITE_IMAGES.mosqueExterior.alt} />
      {!result.ok ? (
        <ErrorState retryHref="/il-muftulukleri" />
      ) : provinces.length === 0 ? (
        <EmptyState title={t("pages.noProvinces")} />
      ) : (
        <>
          <TurkeyMap provinces={provinces} />
          <div className="mt-8 grid grid-cols-2 gap-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6">
            {provinces.map((p) => (
              <Link key={p.slug} href={`/il-muftulukleri/${p.slug}`} className="border border-line bg-white px-3 py-2 text-sm hover:border-forest">
                {p.plateCode} {p.name}
              </Link>
            ))}
          </div>
        </>
      )}
    </PageShell>
  );
}
