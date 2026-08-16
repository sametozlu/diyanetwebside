import { Link } from "@/i18n/navigation";
import { eventDateParts } from "@/lib/utils";
import type { EventItem } from "@/types/api";

export function EventCard({ item }: { item: EventItem }) {
  const date = eventDateParts(item.startsAt);
  return (
    <Link href={`/etkinlikler/${item.slug}`} className="flex gap-4 border-b border-line py-4 hover:bg-white/80">
      <div className="flex h-[4.25rem] w-[4.25rem] shrink-0 flex-col items-center justify-center border border-line bg-white text-center">
        <span className="font-serif text-2xl leading-none text-forest">{date.day}</span>
        <span className="mt-1 text-[10px] font-semibold uppercase tracking-wider text-muted">{date.month}</span>
      </div>
      <div className="min-w-0">
        <h3 className="font-serif text-lg leading-snug">{item.title}</h3>
        <p className="mt-1 text-sm text-muted">
          {item.location}
          {item.province ? ` · ${item.province}` : ""}
        </p>
        {item.summary ? <p className="mt-1 line-clamp-2 text-sm text-muted">{item.summary}</p> : null}
      </div>
    </Link>
  );
}
