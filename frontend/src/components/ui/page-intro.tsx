"use client";

import type { ReactNode } from "react";
import { useTranslations } from "next-intl";
import { Link } from "@/i18n/navigation";
import { cn } from "@/lib/utils";

export function Breadcrumb({ items }: { items: { href?: string; label: string }[] }) {
  const t = useTranslations("common");
  return (
    <nav aria-label={t("breadcrumb")} className="mb-5 text-sm text-muted">
      <ol className="flex flex-wrap items-center gap-2">
        {items.map((item, i) => (
          <li key={`${item.label}-${i}`} className="flex items-center gap-2">
            {i > 0 ? <span aria-hidden>/</span> : null}
            {item.href ? (
              <Link href={item.href} className="hover:text-forest">
                {item.label}
              </Link>
            ) : (
              <span className="text-ink">{item.label}</span>
            )}
          </li>
        ))}
      </ol>
    </nav>
  );
}

export function PageIntro({ title, lead }: { title: string; lead?: string }) {
  return (
    <header className="mb-8 max-w-3xl">
      <h1 className="font-serif text-2xl font-semibold tracking-tight text-ink md:text-3xl">{title}</h1>
      {lead ? <p className="mt-3 text-[15px] leading-7 text-muted">{lead}</p> : null}
    </header>
  );
}

export function PageShell({
  children,
  width = "default",
  className,
}: {
  children: ReactNode;
  width?: "narrow" | "default" | "wide";
  className?: string;
}) {
  const max = width === "narrow" ? "max-w-3xl" : width === "wide" ? "max-w-7xl" : "max-w-6xl";
  return <main className={cn("mx-auto px-4 py-8 md:py-10", max, className)}>{children}</main>;
}
