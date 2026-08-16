import { notFound } from "next/navigation";
import { HtmlContent } from "@/components/ui/html-content";
import { Breadcrumb } from "@/components/ui/page-intro";
import { apiGet } from "@/lib/api";
import type { EventItem } from "@/types/api";
import { getTranslations } from "next-intl/server";

export default async function EventDetail({ params }: { params: Promise<{ slug: string }> }) {
  const t = await getTranslations();
  const { slug } = await params;
  let item: EventItem;
  try {
    item = await apiGet<EventItem>(`/api/events/${slug}`);
  } catch {
    notFound();
  }
  return (
    <article className="mx-auto max-w-3xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { href: "/etkinlikler", label: t("pages.eventsTitle") }, { label: item.title }]} />
      <p className="text-xs text-muted">{item.startsAt} · {item.location} · {item.province}</p>
      <h1 className="mt-2 font-serif text-4xl">{item.title}</h1>
      <p className="mt-4 text-muted">{item.summary}</p>
      <HtmlContent className="prose-portal mt-6" html={item.body} />
    </article>
  );
}
