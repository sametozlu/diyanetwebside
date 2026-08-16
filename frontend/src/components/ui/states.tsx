"use client";

import { useTranslations } from "next-intl";
import { Link } from "@/i18n/navigation";
import { cn } from "@/lib/utils";

export function EmptyState({
  title,
  description,
  actionHref,
  actionLabel,
}: {
  title?: string;
  description?: string;
  actionHref?: string;
  actionLabel?: string;
}) {
  const t = useTranslations("common");
  return (
    <div className="border border-dashed border-line bg-white px-6 py-12 text-center">
      <p className="font-serif text-lg">{title ?? t("emptyTitle")}</p>
      <p className="mt-2 text-sm text-muted">{description ?? t("emptyDescription")}</p>
      {actionHref && actionLabel ? (
        <Link href={actionHref} className="mt-4 inline-flex h-10 items-center bg-forest px-4 text-sm text-white hover:bg-forest-mid">
          {actionLabel}
        </Link>
      ) : null}
    </div>
  );
}

export function ErrorState({
  title,
  description,
  retryHref,
}: {
  title?: string;
  description?: string;
  retryHref?: string;
}) {
  const t = useTranslations("common");
  return (
    <div className="border border-line bg-white px-6 py-10 text-center">
      <p className="font-serif text-lg">{title ?? t("errorTitle")}</p>
      <p className="mt-2 text-sm text-muted">{description ?? t("errorDescription")}</p>
      {retryHref ? (
        <Link href={retryHref} className="mt-4 inline-flex h-10 items-center border border-forest px-4 text-sm text-forest">
          {t("retry")}
        </Link>
      ) : (
        <button
          type="button"
          className="mt-4 inline-flex h-10 items-center border border-forest px-4 text-sm text-forest"
          onClick={() => window.location.reload()}
        >
          {t("retry")}
        </button>
      )}
    </div>
  );
}

export function Skeleton({ className }: { className?: string }) {
  return <div className={cn("animate-pulse bg-paper-2", className)} />;
}

export function NewsSkeleton({ featured = false }: { featured?: boolean }) {
  return (
    <div className={featured ? "grid gap-5 md:grid-cols-2" : "flex flex-col gap-3"}>
      <Skeleton className="aspect-[16/9] w-full" />
      <div className="space-y-2">
        <Skeleton className="h-3 w-24" />
        <Skeleton className="h-6 w-4/5" />
        <Skeleton className="h-4 w-full" />
      </div>
    </div>
  );
}

export function ListSkeleton({ rows = 6 }: { rows?: number }) {
  return (
    <div className="grid gap-3">
      {Array.from({ length: rows }).map((_, i) => (
        <Skeleton key={i} className="h-20 w-full" />
      ))}
    </div>
  );
}
