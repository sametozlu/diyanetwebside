"use client";

import { useQuery } from "@tanstack/react-query";
import { useTranslations } from "next-intl";
import * as React from "react";
import { apiTry } from "@/lib/api";
import { formatTime } from "@/lib/utils";
import type { PrayerCalendar, PrayerTimes, ProvinceDetail, ProvinceSummary } from "@/types/api";
import { Link } from "@/i18n/navigation";
import { ErrorState, Skeleton } from "@/components/ui/states";
import { Select } from "@/components/ui/section";

const NAMES = ["imsak", "gunes", "ogle", "ikindi", "aksam", "yatsi"] as const;

function hijriLabel(data: PrayerTimes | undefined, t: (key: string) => string) {
  if (data?.hijriDay && data?.hijriMonth && data?.hijriYear) {
    const month = t(`prayer.hijriMonths.${data.hijriMonth}`);
    const approx = data.source === "fallback" || data.source === "local" ? ` ${t("prayer.approx")}` : "";
    return `${data.hijriDay} ${month} ${data.hijriYear}${approx}`;
  }
  return data?.hijriDate ?? "";
}

function disclaimerFor(source: string | undefined, t: (key: string) => string) {
  if (source === "aladhan") return t("prayer.disclaimerAladhan");
  if (source === "local") return t("prayer.disclaimerLocal");
  if (source === "fallback") return t("prayer.disclaimerFallback");
  return t("prayer.disclaimer");
}

export function PrayerTimesPanel({
  initial,
  compact = false,
  showCalendar = false,
}: {
  initial?: PrayerTimes | null;
  compact?: boolean;
  showCalendar?: boolean;
}) {
  const t = useTranslations();
  const [slug, setSlug] = React.useState(initial?.province || "ankara");
  const [district, setDistrict] = React.useState(initial?.district || "");
  const query = district
    ? `/api/prayer-times?province=${slug}&district=${district}`
    : `/api/prayer-times?province=${slug}`;
  const { data, isError, isFetching } = useQuery({
    queryKey: ["prayer", slug, district],
    queryFn: async () => {
      const result = await apiTry<PrayerTimes>(query, { cache: "no-store" });
      if (!result.ok) throw new Error(result.error);
      return result.data;
    },
    initialData: slug === initial?.province && district === (initial?.district || "") ? initial ?? undefined : undefined,
    refetchInterval: 60_000,
  });
  const { data: provinces } = useQuery({
    queryKey: ["provinces"],
    queryFn: async () => {
      const result = await apiTry<ProvinceSummary[]>("/api/provinces");
      return result.ok ? result.data : [];
    },
    staleTime: 5 * 60_000,
  });
  const { data: detail } = useQuery({
    queryKey: ["province-detail", slug],
    queryFn: async () => {
      const result = await apiTry<ProvinceDetail>(`/api/provinces/${slug}`);
      return result.ok ? result.data : null;
    },
    staleTime: 5 * 60_000,
  });
  const { data: calendar } = useQuery({
    queryKey: ["prayer-calendar", slug, district],
    queryFn: async () => {
      const qs = new URLSearchParams({ province: slug });
      if (district) qs.set("district", district);
      const result = await apiTry<PrayerCalendar>(`/api/prayer-times/calendar?${qs}`, { cache: "no-store" });
      if (!result.ok) throw new Error(result.error);
      return result.data;
    },
    enabled: showCalendar,
  });
  const times = data?.times;
  const highlight = data?.currentPrayer || data?.nextPrayer?.name;

  if (isError) {
    return <ErrorState description={t("prayer.unavailable")} />;
  }

  return (
    <section className="border border-line bg-white">
      <div className="flex flex-wrap items-end justify-between gap-4 border-b border-line px-4 py-4 md:px-5">
        <div>
          <p className="kicker">{t("home.prayerTimes")}</p>
          <h2 className="mt-1 font-serif text-2xl">{data?.cityLabel ?? slug}</h2>
          <p className="text-xs text-muted">
            {data?.date} · {hijriLabel(data, t)}
            {isFetching ? ` · ${t("prayer.updating")}` : ""}
          </p>
        </div>
        <div className="flex flex-wrap items-center gap-3">
          <label className="text-sm">
            <span className="sr-only">{t("home.changeLocation")}</span>
            <Select
              value={slug}
              onChange={(e) => {
                setSlug(e.target.value);
                setDistrict("");
              }}
            >
              {(provinces ?? []).map((p) => (
                <option key={p.slug} value={p.slug}>
                  {p.name}
                </option>
              ))}
            </Select>
          </label>
          {detail?.districts?.length ? (
            <label className="text-sm">
              <span className="sr-only">{t("prayer.district")}</span>
              <Select value={district} onChange={(e) => setDistrict(e.target.value)}>
                <option value="">{t("prayer.cityCenter")}</option>
                {detail.districts.map((d) => (
                  <option key={d.slug} value={d.slug}>
                    {d.name}
                  </option>
                ))}
              </Select>
            </label>
          ) : null}
          {!showCalendar ? (
            <Link href="/namaz-vakitleri" className="text-sm text-forest hover:underline">
              {t("home.monthlyCalendar")}
            </Link>
          ) : null}
        </div>
      </div>
      <div className="px-4 py-4 md:px-5">
        {!times ? (
          <div className={compact ? "grid grid-cols-3 gap-px bg-line md:grid-cols-6" : "grid grid-cols-2 gap-px bg-line sm:grid-cols-3 md:grid-cols-6"}>
            {NAMES.map((name) => (
              <Skeleton key={name} className="h-20 bg-paper-2" />
            ))}
          </div>
        ) : (
          <div className={compact ? "grid grid-cols-3 md:grid-cols-6" : "grid grid-cols-2 sm:grid-cols-3 md:grid-cols-6"}>
            {NAMES.map((name) => {
              const active = highlight === name;
              const next = data?.nextPrayer?.name === name;
              return (
                <div key={name} className={`border-b border-line px-3 py-3 md:border-b-0 md:border-e last:border-e-0 ${active ? "bg-forest-soft" : ""}`}>
                  <p className="text-[11px] uppercase tracking-wider text-muted">{t(`prayer.${name}`)}</p>
                  <p className="mt-1 font-serif text-2xl tabular-nums">{formatTime(times[name])}</p>
                  {next ? <p className="mt-1 text-[11px] font-medium text-forest">{t("prayer.next")}</p> : null}
                </div>
              );
            })}
          </div>
        )}
        {data?.nextPrayer ? (
          <PrayerCountdown
            key={`${slug}-${district}-${data.nextPrayer.name}-${data.nextPrayer.time}`}
            remainingSeconds={data.nextPrayer.remainingSeconds}
            label={`${t("home.nextPrayer")}: ${t(`prayer.${data.nextPrayer.name as "imsak"}`)}`}
            hoursLabel={t("prayer.hours")}
            minutesLabel={t("prayer.minutes")}
          />
        ) : null}
        <p className="mt-3 text-xs text-muted">{disclaimerFor(data?.source, t)}</p>
        {showCalendar && calendar ? (
          <div className="mt-6 overflow-x-auto">
            <table className="min-w-full text-left text-sm">
              <thead>
                <tr className="border-b border-line text-xs uppercase tracking-wider text-muted">
                  <th className="py-2 pr-3">{t("common.date")}</th>
                  {NAMES.map((name) => (
                    <th key={name} className="py-2 pr-3">
                      {t(`prayer.${name}`)}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {calendar.days.map((day) => (
                  <tr key={day.date} className="border-b border-line">
                    <td className="py-2 pr-3 whitespace-nowrap">
                      {day.date}
                      <span className="block text-[11px] text-muted">
                        {day.hijriDay && day.hijriMonth && day.hijriYear
                          ? `${day.hijriDay} ${t(`prayer.hijriMonths.${day.hijriMonth}`)} ${day.hijriYear}`
                          : day.hijriDate}
                      </span>
                    </td>
                    {NAMES.map((name) => (
                      <td key={name} className="py-2 pr-3 tabular-nums">
                        {formatTime(day.times[name])}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : null}
      </div>
    </section>
  );
}

function PrayerCountdown({
  remainingSeconds,
  label,
  hoursLabel,
  minutesLabel,
}: {
  remainingSeconds: number;
  label: string;
  hoursLabel: string;
  minutesLabel: string;
}) {
  const [tick, setTick] = React.useState(0);
  React.useEffect(() => {
    const id = window.setInterval(() => setTick((value) => value + 1), 1000);
    return () => window.clearInterval(id);
  }, []);
  const remaining = Math.max(0, remainingSeconds - tick);
  const hh = Math.floor(remaining / 3600);
  const mm = Math.floor((remaining % 3600) / 60);
  return (
    <p className="mt-4 text-sm text-forest" aria-live="polite">
      {label} · {hh}{hoursLabel} {mm}{minutesLabel}
    </p>
  );
}
