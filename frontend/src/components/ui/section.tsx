"use client";

import * as React from "react";
import { useTranslations } from "next-intl";
import { Link } from "@/i18n/navigation";
import { cn } from "@/lib/utils";

export function Input({ className, ...props }: React.ComponentProps<"input">) {
  return (
    <input
      className={cn(
        "h-11 w-full rounded-sm border border-line bg-white px-3 text-sm text-ink placeholder:text-muted focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-forest",
        className,
      )}
      {...props}
    />
  );
}

export function Select({ className, ...props }: React.ComponentProps<"select">) {
  return (
    <select
      className={cn(
        "h-10 rounded-sm border border-line bg-white px-2 text-sm text-ink focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-forest",
        className,
      )}
      {...props}
    />
  );
}

export function Badge({
  className,
  children,
}: {
  className?: string;
  children: React.ReactNode;
}) {
  return (
    <span
      className={cn(
        "inline-flex items-center bg-forest-soft px-2 py-0.5 text-[11px] font-semibold uppercase tracking-wide text-forest",
        className,
      )}
    >
      {children}
    </span>
  );
}

export function Chip({
  href,
  active,
  children,
}: {
  href: string;
  active?: boolean;
  children: React.ReactNode;
}) {
  return (
    <Link
      href={href}
      className={cn(
        "inline-flex h-8 items-center border px-3 text-sm transition-colors duration-150",
        active ? "border-forest bg-forest text-white" : "border-line bg-white text-ink hover:border-forest",
      )}
    >
      {children}
    </Link>
  );
}

export function SectionHeader({
  kicker,
  title,
  href,
  action,
}: {
  kicker?: string;
  title: string;
  href?: string;
  action?: React.ReactNode;
}) {
  const t = useTranslations("common");
  return (
    <div className="mb-5 flex items-end justify-between gap-4 border-b border-line pb-3">
      <div>
        {kicker ? <p className="kicker mb-1">{kicker}</p> : null}
        <h2 className="font-serif text-xl font-semibold text-ink md:text-2xl">{title}</h2>
      </div>
      {action}
      {href ? (
        <Link href={href} className="text-sm font-medium text-forest hover:underline">
          {t("all")}
        </Link>
      ) : null}
    </div>
  );
}

export function Pagination({
  page,
  totalPages,
  hrefFor,
}: {
  page: number;
  totalPages: number;
  hrefFor: (page: number) => string;
}) {
  const t = useTranslations("common");
  if (totalPages <= 1) return null;
  return (
    <nav className="mt-10 flex items-center justify-center gap-4 text-sm" aria-label={t("pagination")}>
      {page > 0 ? (
        <Link href={hrefFor(page - 1)} className="border border-line px-3 py-2 hover:border-forest">
          {t("previous")}
        </Link>
      ) : (
        <span className="border border-transparent px-3 py-2 text-muted">{t("previous")}</span>
      )}
      <span className="tabular-nums text-muted">
        {page + 1} / {totalPages}
      </span>
      {page + 1 < totalPages ? (
        <Link href={hrefFor(page + 1)} className="border border-line px-3 py-2 hover:border-forest">
          {t("next")}
        </Link>
      ) : (
        <span className="border border-transparent px-3 py-2 text-muted">{t("next")}</span>
      )}
    </nav>
  );
}

export function SearchBar({
  name = "q",
  defaultValue,
  placeholder,
  action,
  children,
}: {
  name?: string;
  defaultValue?: string;
  placeholder: string;
  action?: string;
  children?: React.ReactNode;
}) {
  const t = useTranslations("common");
  return (
    <form method="get" action={action} className="mb-6 flex flex-col gap-2 sm:flex-row">
      <Input name={name} defaultValue={defaultValue} placeholder={placeholder} aria-label={placeholder} />
      {children}
      <button type="submit" className="h-11 shrink-0 bg-forest px-5 text-sm font-medium text-white hover:bg-forest-mid">
        {t("searchAction")}
      </button>
    </form>
  );
}
