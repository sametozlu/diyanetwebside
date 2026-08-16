import Image from "next/image";
import { getTranslations } from "next-intl/server";
import { NewsCard } from "@/components/ui/news-card";
import { EventCard } from "@/components/ui/event-card";
import { PublicationCard } from "@/components/ui/publication-card";
import { CoverImage } from "@/components/ui/cover-image";
import { SectionHeader } from "@/components/ui/section";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { QUICK_SERVICES } from "@/lib/quick-services";
import { PrayerTimesPanel } from "@/features/prayer-times/prayer-times-panel";
import { Link } from "@/i18n/navigation";
import { apiTry } from "@/lib/api";
import { SITE_IMAGES } from "@/lib/images";
import { siteUrl } from "@/lib/site";
import type { EventItem, Hadith, NewsSummary, Paged, PrayerTimes, Publication, SurahDetail } from "@/types/api";

export const revalidate = 60;

export default async function HomePage() {
  const t = await getTranslations("home");
  const tQuick = await getTranslations("quick");
  const tMeta = await getTranslations("meta");
  const tPages = await getTranslations("pages");
  const [news, featured, mostRead, prayer, hadith, publications, events, fatiha] =
    await Promise.all([
      apiTry<Paged<NewsSummary>>("/api/news?size=8"),
      apiTry<Paged<NewsSummary>>("/api/news?featured=true&size=4"),
      apiTry<NewsSummary[]>("/api/news/most-read"),
      apiTry<PrayerTimes>("/api/prayer-times?province=ankara", { cache: "no-store" }),
      apiTry<Hadith>("/api/hadith/daily"),
      apiTry<Paged<Publication>>("/api/publications?size=2"),
      apiTry<Paged<EventItem>>("/api/events?size=4"),
      apiTry<SurahDetail>("/api/quran/surahs/1"),
    ]);

  const newsItems = news.ok ? news.data.content : [];
  const featuredItems = featured.ok ? featured.data.content : [];
  const lead = featuredItems[0] ?? newsItems[0];
  const latest = newsItems.filter((n) => n.slug !== lead?.slug).slice(0, 3);
  const mostReadItems = mostRead.ok ? mostRead.data : [];
  const jsonLd = {
    "@context": "https://schema.org",
    "@type": "GovernmentOrganization",
    name: tMeta("siteName"),
    description: tMeta("description"),
    url: siteUrl(),
  };

  return (
    <div>
      <script type="application/ld+json" dangerouslySetInnerHTML={{ __html: JSON.stringify(jsonLd) }} />

      <section className="relative overflow-hidden text-white">
        <div className="relative min-h-[22rem] md:min-h-[28rem] lg:min-h-[32rem]">
          <Image
            src={SITE_IMAGES.hero.src}
            alt={SITE_IMAGES.hero.alt}
            fill
            priority
            sizes="100vw"
            className="object-cover object-[center_35%]"
          />
          <div className="absolute inset-0 bg-gradient-to-r from-forest-deep/92 via-forest-deep/70 to-forest-deep/30" />
          <div className="relative mx-auto flex min-h-[22rem] max-w-7xl flex-col justify-end px-4 py-10 md:min-h-[28rem] md:py-14 lg:min-h-[32rem]">
            <p className="text-xs font-semibold uppercase tracking-[0.16em] text-white/70">{t("heroKicker")}</p>
            <h1 className="mt-3 max-w-xl font-serif text-3xl font-semibold leading-tight md:text-4xl lg:text-[2.6rem]">
              {t("heroTitle")}
            </h1>
            <p className="mt-4 max-w-xl text-[15px] leading-7 text-white/85">{t("heroLead")}</p>
            <div className="mt-6 flex flex-wrap gap-3">
              <Link href="/namaz-vakitleri" className="inline-flex h-11 items-center bg-white px-4 text-sm font-medium text-forest hover:bg-paper-2">
                {t("prayerTimes")}
              </Link>
              <Link href="/haberler" className="inline-flex h-11 items-center border border-white/40 px-4 text-sm font-medium text-white hover:bg-white/10">
                {t("allNews")}
              </Link>
            </div>
            <p className="mt-5 max-w-xl text-[11px] text-white/55">
              {SITE_IMAGES.hero.alt} · Wikimedia Commons, CC BY-SA 4.0
            </p>
          </div>
        </div>
      </section>

      <section className="border-b border-line bg-white">
        <div className="mx-auto max-w-7xl px-4 py-6">
          <h2 className="sr-only">{t("quickAccess")}</h2>
          <ul className="grid grid-cols-3 gap-x-3 gap-y-4 md:grid-cols-9">
            {QUICK_SERVICES.map((s) => {
              const Icon = s.icon;
              return (
                <li key={s.href}>
                  <Link href={s.href} className="flex flex-col items-center gap-2 text-center hover:text-forest">
                    <Icon className="h-5 w-5 text-forest" aria-hidden />
                    <span className="text-[12px] font-medium leading-tight">{tQuick(s.key)}</span>
                  </Link>
                </li>
              );
            })}
          </ul>
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-4 py-10">
        {prayer.ok ? (
          <PrayerTimesPanel initial={prayer.data} />
        ) : (
          <ErrorState retryHref="/namaz-vakitleri" description={tPages("prayerUnavailable")} />
        )}
      </section>

      <section className="mx-auto max-w-7xl px-4 pb-10">
        <div className="grid gap-10 lg:grid-cols-[minmax(0,1fr)_18rem]">
          <div>
            <SectionHeader title={t("latestNews")} href="/haberler" />
            {!news.ok && !featured.ok ? (
              <ErrorState retryHref="/" description={t("newsUnavailable")} />
            ) : lead ? (
              <div className="grid gap-8 lg:grid-cols-[1.35fr_0.85fr]">
                <NewsCard item={lead} variant="featured" />
                <div className="flex flex-col gap-5">
                  {latest.map((item) => (
                    <NewsCard key={item.id} item={item} variant="row" />
                  ))}
                </div>
              </div>
            ) : (
              <EmptyState title={t("noNews")} actionHref="/haberler" actionLabel={t("allNews")} />
            )}
          </div>
          <aside>
            <SectionHeader title={t("mostRead")} />
            <ol className="divide-y divide-line border-y border-line">
              {(mostReadItems.length ? mostReadItems : latest).slice(0, 6).map((item, i) => (
                <li key={item.id} className="flex gap-3 py-3">
                  <span className="w-6 font-serif text-lg text-forest">{String(i + 1).padStart(2, "0")}</span>
                  <Link href={`/haberler/${item.slug}`} className="text-sm font-medium leading-snug hover:text-forest">
                    {item.title}
                  </Link>
                </li>
              ))}
            </ol>
          </aside>
        </div>
      </section>

      <section className="border-y border-line bg-white">
        <div className="mx-auto grid max-w-7xl gap-10 px-4 py-10 lg:grid-cols-2">
          <article>
            <p className="kicker">{t("quranToday")}</p>
            {fatiha.ok && fatiha.data.ayahs[1] ? (
              <>
                <p className="arabic mt-4 text-right text-2xl leading-[1.9] md:text-3xl" dir="rtl">
                  {fatiha.data.ayahs[1].textAr}
                </p>
                <p className="mt-4 max-w-xl text-[15px] leading-7 text-muted">{fatiha.data.ayahs[1].textTr}</p>
                <Link href="/kuran/1" className="mt-4 inline-block text-sm font-medium text-forest hover:underline">
                  {t("fatiha")}
                </Link>
              </>
            ) : (
              <p className="mt-4 text-sm text-muted">{t("quranUnavailable")}</p>
            )}
          </article>
          <article>
            <p className="kicker">{t("hadithToday")}</p>
            {hadith.ok ? (
              <>
                <p className="arabic mt-4 text-right text-2xl leading-[1.9]" dir="rtl">{hadith.data.textAr}</p>
                <h2 className="mt-4 font-serif text-xl">{hadith.data.title}</h2>
                <p className="mt-2 max-w-xl text-[15px] leading-7 text-muted">{hadith.data.textTr}</p>
                <p className="mt-3 text-xs text-muted">{hadith.data.source} · {hadith.data.narrator}</p>
              </>
            ) : (
              <p className="mt-4 text-sm text-muted">{t("hadithUnavailable")}</p>
            )}
          </article>
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-4 py-10">
        <div className="grid gap-12 lg:grid-cols-2">
          <div>
            <SectionHeader title={t("publications")} href="/yayinlar" />
            {publications.ok ? (
              publications.data.content.length ? (
                <div className="grid grid-cols-2 gap-6">
                  {publications.data.content.slice(0, 2).map((p) => (
                    <PublicationCard key={p.id} item={p} />
                  ))}
                </div>
              ) : (
                <EmptyState title={t("noPublications")} />
              )
            ) : (
              <ErrorState retryHref="/yayinlar" description={t("publicationsUnavailable")} />
            )}
          </div>
          <div>
            <SectionHeader title={t("events")} href="/etkinlikler" />
            {events.ok ? (
              events.data.content.length ? (
                <div>
                  <CoverImage
                    src={SITE_IMAGES.mosqueInterior.src}
                    alt={SITE_IMAGES.mosqueInterior.alt}
                    ratio="banner"
                    sizes="(max-width: 1024px) 100vw, 560px"
                  />
                  <div className="mt-2">
                    {events.data.content.slice(0, 4).map((e) => (
                      <EventCard key={e.id} item={e} />
                    ))}
                  </div>
                </div>
              ) : (
                <EmptyState title={t("noEvents")} />
              )
            ) : (
              <ErrorState retryHref="/etkinlikler" description={t("eventsUnavailable")} />
            )}
          </div>
        </div>
      </section>

      <section className="border-t border-line bg-white">
        <div className="mx-auto max-w-7xl px-4 py-10">
          <SectionHeader title={t("provinces")} href="/il-muftulukleri" />
          <p className="max-w-2xl text-sm leading-6 text-muted">{t("provincesLead")}</p>
          <div className="mt-5 flex flex-wrap gap-2">
            {["ankara", "istanbul", "izmir", "bursa", "konya", "antalya"].map((slug) => (
              <Link key={slug} href={`/il-muftulukleri/${slug}`} className="border border-line px-3 py-1.5 text-sm capitalize hover:border-forest">
                {slug}
              </Link>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
