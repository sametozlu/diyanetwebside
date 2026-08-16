import { Link } from "@/i18n/navigation";
import { cn, formatDate } from "@/lib/utils";
import type { NewsSummary } from "@/types/api";
import { CoverImage } from "@/components/ui/cover-image";
import { Badge } from "@/components/ui/section";

export function NewsCard({
  item,
  variant = "card",
  featured = false,
}: {
  item: NewsSummary;
  variant?: "featured" | "card" | "row";
  featured?: boolean;
}) {
  const mode = featured ? "featured" : variant;
  if (mode === "row") {
    return (
      <article className="grid grid-cols-[7.5rem_1fr] gap-3 sm:grid-cols-[9rem_1fr]">
        <Link href={`/haberler/${item.slug}`} className="block overflow-hidden">
          <CoverImage src={item.imageUrl} alt={item.title} ratio="news" sizes="9rem" />
        </Link>
        <div className="min-w-0">
          <div className="flex flex-wrap items-center gap-2">
            {item.category ? <Badge>{item.category.name}</Badge> : null}
            <time className="text-xs text-muted">{formatDate(item.publishedAt)}</time>
          </div>
          <Link href={`/haberler/${item.slug}`} className="mt-1 block hover:text-forest">
            <h3 className="font-serif text-base leading-snug md:text-lg">{item.title}</h3>
          </Link>
        </div>
      </article>
    );
  }

  const isFeatured = mode === "featured";
  return (
    <article className={cn("flex flex-col gap-3", isFeatured && "gap-4")}>
      <Link href={`/haberler/${item.slug}`} className="group block overflow-hidden">
        <CoverImage
          src={item.imageUrl}
          alt={item.title}
          ratio={isFeatured ? "hero" : "news"}
          sizes={isFeatured ? "(max-width: 1024px) 100vw, 720px" : "(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 400px"}
        />
      </Link>
      <div className="flex flex-col gap-2">
        <div className="flex flex-wrap items-center gap-2">
          {item.category ? <Badge>{item.category.name}</Badge> : null}
          <time className="text-xs text-muted">{formatDate(item.publishedAt)}</time>
        </div>
        <Link href={`/haberler/${item.slug}`} className="hover:text-forest">
          <h3 className={isFeatured ? "font-serif text-2xl leading-tight md:text-3xl" : "font-serif text-lg leading-snug"}>
            {item.title}
          </h3>
        </Link>
        {item.summary ? (
          <p className={isFeatured ? "max-w-2xl text-[15px] leading-7 text-muted" : "line-clamp-2 text-sm text-muted"}>
            {item.summary}
          </p>
        ) : null}
      </div>
    </article>
  );
}
