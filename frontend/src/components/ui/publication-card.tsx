"use client";

import { useTranslations } from "next-intl";
import { Link } from "@/i18n/navigation";
import { formatDate } from "@/lib/utils";
import type { Publication } from "@/types/api";
import { CoverImage } from "@/components/ui/cover-image";

export function publicationTypeLabel(type?: string | null) {
  if (!type) return "";
  return type;
}

export function PublicationCard({ item }: { item: Publication }) {
  const t = useTranslations("types");
  return (
    <Link href={`/yayinlar/${item.slug}`} className="group block">
      <CoverImage
        src={item.coverUrl}
        alt={item.title}
        ratio="portrait"
        sizes="(max-width: 640px) 50vw, (max-width: 1024px) 25vw, 220px"
      />
      <div className="pt-3">
        <p className="kicker">{item.type ? t(item.type) : ""}</p>
        <h2 className="mt-1 font-serif text-lg leading-snug group-hover:text-forest">{item.title}</h2>
        {item.author ? <p className="mt-1 text-sm text-muted">{item.author}</p> : null}
        {item.publishedAt ? <p className="text-xs text-muted">{formatDate(item.publishedAt)}</p> : null}
      </div>
    </Link>
  );
}
