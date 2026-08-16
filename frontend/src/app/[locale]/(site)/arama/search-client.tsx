"use client";

import { useQuery } from "@tanstack/react-query";
import { useTranslations } from "next-intl";
import { useSearchParams } from "next/navigation";
import * as React from "react";
import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { Chip, Input } from "@/components/ui/section";
import { EmptyState, ErrorState, ListSkeleton } from "@/components/ui/states";
import { Link, useRouter } from "@/i18n/navigation";
import { apiTry } from "@/lib/api";
import type { SearchResponse } from "@/types/api";

const FILTERS = ["", "news", "sermon", "publication", "hadith", "fatwa", "event"] as const;

export function SearchClient() {
  const t = useTranslations();
  const params = useSearchParams();
  const router = useRouter();
  const qParam = params.get("q") ?? "";
  const type = params.get("type") ?? "";

  const enabled = qParam.trim().length >= 2;
  const { data, isFetching, isError } = useQuery({
    queryKey: ["search-page", qParam, type],
    queryFn: async () => {
      const qs = new URLSearchParams({ q: qParam, limit: "40" });
      if (type) qs.set("type", type);
      const result = await apiTry<SearchResponse>(`/api/search?${qs}`);
      if (!result.ok) throw new Error(result.error);
      return result.data;
    },
    enabled,
  });

  function submit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const value = String(new FormData(event.currentTarget).get("q") ?? "").trim();
    const next = new URLSearchParams();
    if (value) next.set("q", value);
    if (type) next.set("type", type);
    router.push(`/arama?${next.toString()}`);
  }

  function typeLabel(value: string) {
    return value ? t(`types.${value}`) : t("common.all");
  }

  return (
    <PageShell width="narrow">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.searchTitle") }]} />
      <PageIntro title={t("pages.searchTitle")} lead={t("pages.searchLead")} />
      <form onSubmit={submit} className="mb-6 flex flex-col gap-2 sm:flex-row">
        <Input key={qParam} name="q" defaultValue={qParam} placeholder={t("search.minChars")} aria-label={t("pages.searchTitle")} />
        <button type="submit" className="h-11 shrink-0 bg-forest px-5 text-sm font-medium text-white hover:bg-forest-mid">
          {t("common.searchAction")}
        </button>
      </form>
      <div className="mb-8 flex flex-wrap gap-2">
        {FILTERS.map((value) => {
          const href = qParam
            ? `/arama?q=${encodeURIComponent(qParam)}${value ? `&type=${value}` : ""}`
            : value
              ? `/arama?type=${value}`
              : "/arama";
          return (
            <Chip key={value || "all"} href={href} active={type === value}>
              {typeLabel(value)}
            </Chip>
          );
        })}
      </div>
      {!enabled ? (
        <EmptyState title={t("search.startTitle")} description={t("search.startLead")} />
      ) : isFetching && !data ? (
        <ListSkeleton />
      ) : isError ? (
        <ErrorState description={t("search.serviceError")} />
      ) : !data?.groups.length ? (
        <EmptyState title={t("search.noResults")} description={t("search.noMatch", { q: qParam })} />
      ) : (
        <div className="grid gap-8">
          {data.groups.map((group) => (
            <section key={group.type}>
              <h2 className="mb-3 font-serif text-xl">{typeLabel(group.type)}</h2>
              <ul className="divide-y divide-line border-y border-line">
                {group.items.map((item) => (
                  <li key={`${group.type}-${item.slug}`}>
                    <Link href={item.href} className="block py-4 hover:text-forest">
                      <p className="kicker">{typeLabel(group.type)}</p>
                      <p className="mt-1 font-medium">{item.title}</p>
                      {item.summary ? <p className="mt-1 line-clamp-2 text-sm text-muted">{item.summary}</p> : null}
                    </Link>
                  </li>
                ))}
              </ul>
            </section>
          ))}
        </div>
      )}
    </PageShell>
  );
}
