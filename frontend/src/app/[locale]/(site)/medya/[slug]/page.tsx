import { notFound } from "next/navigation";
import { Breadcrumb } from "@/components/ui/page-intro";
import { CoverImage } from "@/components/ui/cover-image";
import { EmptyState } from "@/components/ui/states";
import { apiGet, apiGetSafe } from "@/lib/api";
import { emptyPage, type MediaItem, type Paged } from "@/types/api";
import { Link } from "@/i18n/navigation";
import { getTranslations } from "next-intl/server";

export default async function MediaDetail({ params }: { params: Promise<{ slug: string }> }) {
  const t = await getTranslations();
  const { slug } = await params;
  let item: MediaItem;
  try {
    item = await apiGet<MediaItem>(`/api/media/${slug}`);
  } catch {
    notFound();
  }
  const related = await apiGetSafe<Paged<MediaItem>>("/api/media?size=6", emptyPage());
  return (
    <main className="mx-auto max-w-5xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { href: "/medya", label: t("pages.mediaTitle") }, { label: item.title }]} />
      <div className="overflow-hidden bg-forest-deep">
        {item.videoUrl ? (
          <div className="aspect-video">
            <iframe
              title={item.title}
              src={item.videoUrl}
              className="h-full w-full"
              allowFullScreen
              referrerPolicy="no-referrer"
              sandbox="allow-scripts allow-same-origin allow-presentation"
            />
          </div>
        ) : (
          <div className="flex aspect-video flex-col items-center justify-center gap-3 p-6 text-center text-white/80">
            <CoverImage src={item.thumbnailUrl} alt={item.title} ratio="video" className="w-full max-w-xl" />
            <p className="text-sm">{t("pages.noVideo")}</p>
          </div>
        )}
      </div>
      <h1 className="mt-6 font-serif text-3xl">{item.title}</h1>
      <p className="mt-2 text-muted">{item.summary}</p>
      <h2 className="mt-8 font-serif text-xl">{t("common.related")}</h2>
      {related.content.filter((x) => x.slug !== item.slug).length ? (
        <div className="mt-3 grid gap-3 sm:grid-cols-3">
          {related.content
            .filter((x) => x.slug !== item.slug)
            .slice(0, 3)
            .map((x) => (
              <Link key={x.id} href={`/medya/${x.slug}`} className="rounded-lg border border-line p-3 text-sm">
                {x.title}
              </Link>
            ))}
        </div>
      ) : (
        <EmptyState title={t("pages.noRelatedMedia")} />
      )}
    </main>
  );
}
