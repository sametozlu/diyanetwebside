import { PageBanner } from "@/components/ui/page-banner";
import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { EventCard } from "@/components/ui/event-card";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { apiTry } from "@/lib/api";
import { SITE_IMAGES } from "@/lib/images";
import { pagesMeta } from "@/lib/page-meta";
import { emptyPage, type EventItem, type Paged } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 60;
export const generateMetadata = () => pagesMeta("eventsTitle");

export default async function EventsPage({
  searchParams,
}: {
  searchParams: Promise<{ province?: string; category?: string }>;
}) {
  const t = await getTranslations();
  const sp = await searchParams;
  const qs = new URLSearchParams({ size: "20" });
  if (sp.province) qs.set("province", sp.province);
  if (sp.category) qs.set("category", sp.category);
  const result = await apiTry<Paged<EventItem>>(`/api/events?${qs}`);
  const data = result.ok ? result.data : emptyPage<EventItem>();
  return (
    <PageShell>
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.eventsTitle") }]} />
      <PageIntro title={t("pages.eventsTitle")} lead={t("pages.eventsLead")} />
      <PageBanner src={SITE_IMAGES.mosqueInterior.src} alt={SITE_IMAGES.mosqueInterior.alt} />
      {!result.ok ? (
        <ErrorState retryHref="/etkinlikler" />
      ) : data.content.length === 0 ? (
        <EmptyState title={t("pages.noEvents")} />
      ) : (
        <div className="border-t border-line">
          {data.content.map((e) => (
            <EventCard key={e.id} item={e} />
          ))}
        </div>
      )}
    </PageShell>
  );
}
