import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { ErrorState } from "@/components/ui/states";
import { PrayerTimesPanel } from "@/features/prayer-times/prayer-times-panel";
import { apiTry } from "@/lib/api";
import { pagesMeta } from "@/lib/page-meta";
import type { PrayerTimes } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const generateMetadata = () => pagesMeta("prayerTitle");

export default async function PrayerPage() {
  const t = await getTranslations();
  const prayer = await apiTry<PrayerTimes>("/api/prayer-times?province=ankara", { cache: "no-store" });
  return (
    <PageShell width="wide">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.prayerTitle") }]} />
      <PageIntro title={t("pages.prayerTitle")} lead={t("pages.prayerLead")} />
      {prayer.ok ? (
        <PrayerTimesPanel initial={prayer.data} showCalendar />
      ) : (
        <ErrorState retryHref="/namaz-vakitleri" description={t("pages.prayerUnavailable")} />
      )}
    </PageShell>
  );
}
