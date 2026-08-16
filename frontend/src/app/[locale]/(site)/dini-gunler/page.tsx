import { Breadcrumb, PageIntro } from "@/components/ui/page-intro";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { apiTry } from "@/lib/api";
import { pagesMeta } from "@/lib/page-meta";
import type { ReligiousDay } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 3600;
export const generateMetadata = () => pagesMeta("religiousDaysTitle");

export default async function ReligiousDaysPage() {
  const t = await getTranslations();
  const result = await apiTry<ReligiousDay[]>("/api/calendar/religious-days");
  const days = result.ok ? result.data : [];
  return (
    <main className="mx-auto max-w-4xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("nav.religiousDays") }]} />
      <PageIntro title={t("pages.religiousDaysTitle")} lead={t("pages.religiousDaysLead")} />
      {!result.ok ? (
        <ErrorState retryHref="/dini-gunler" />
      ) : days.length === 0 ? (
        <EmptyState title={t("pages.emptyCalendar")} description={t("pages.emptyCalendarHint")} />
      ) : (
        <ol className="divide-y divide-line border-y border-line">
          {days.map((d) => (
            <li key={`${d.title}-${d.gregorianDate}`} className="py-4">
              <p className="kicker">{d.type}</p>
              <h2 className="mt-1 font-serif text-xl">{d.title}</h2>
              <p className="text-sm text-muted">{d.gregorianDate} · {d.hijriDate}</p>
              <p className="mt-1 text-sm">{d.note}</p>
            </li>
          ))}
        </ol>
      )}
    </main>
  );
}
