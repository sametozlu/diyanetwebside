import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { Link } from "@/i18n/navigation";
import { apiTry } from "@/lib/api";
import { pagesMeta } from "@/lib/page-meta";
import type { Fatwa, Hadith, Paged, Publication, Sermon, SurahSummary } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const generateMetadata = () => pagesMeta("religiousTitle");

export default async function ReligiousPage() {
  const t = await getTranslations();
  const [surahs, hadith, fatwas, sermons, publications] = await Promise.all([
    apiTry<SurahSummary[]>("/api/quran/surahs"),
    apiTry<Hadith>("/api/hadith/daily"),
    apiTry<Paged<Fatwa>>("/api/fatwas?size=6"),
    apiTry<Paged<Sermon>>("/api/sermons?size=3"),
    apiTry<Paged<Publication>>("/api/publications?size=3"),
  ]);
  return (
    <PageShell>
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.religiousTitle") }]} />
      <PageIntro title={t("pages.religiousTitle")} lead={t("pages.religiousLead")} />
      <div className="grid gap-px border border-line bg-line sm:grid-cols-2">
        <HubCard
          title={t("pages.quranTitle")}
          href="/kuran"
          ok={surahs.ok}
          text={surahs.ok ? t("pages.surahCount", { count: surahs.data.length }) : t("pages.surahListFailed")}
        />
        <HubCard
          title={t("pages.hadithTitle")}
          href="/hadis"
          ok={hadith.ok}
          text={hadith.ok ? hadith.data.title : t("pages.hadithUnavailable")}
        />
        <HubCard
          title={t("pages.fatwaTitle")}
          href="/fetva"
          ok={fatwas.ok}
          text={fatwas.ok ? t("pages.fatwaCount", { count: fatwas.data.totalElements }) : t("pages.fatwaListFailed")}
        />
        <HubCard
          title={t("pages.sermonsTitle")}
          href="/hutbeler"
          ok={sermons.ok}
          text={sermons.ok ? sermons.data.content[0]?.title ?? t("pages.archive") : t("pages.sermonFailed")}
        />
      </div>
      <h2 className="mt-10 font-serif text-xl">{t("pages.latestFatwas")}</h2>
      {!fatwas.ok ? <ErrorState retryHref="/fetva" /> : (fatwas.data.content.length ? (
        <ul className="mt-3 divide-y divide-line border-y border-line">
          {fatwas.data.content.map((f) => (
            <li key={f.id} className="py-3"><Link href={`/fetva/${f.slug}`} className="text-forest hover:underline">{f.question}</Link></li>
          ))}
        </ul>
      ) : <EmptyState title={t("pages.noFatwas")} />)}
      <h2 className="mt-10 font-serif text-xl">{t("pages.publicationsTitle")}</h2>
      {!publications.ok ? <ErrorState retryHref="/yayinlar" /> : (publications.data.content.length ? (
        <ul className="mt-3 divide-y divide-line border-y border-line">
          {publications.data.content.map((p) => (
            <li key={p.id} className="py-3"><Link href={`/yayinlar/${p.slug}`} className="text-forest hover:underline">{p.title}</Link></li>
          ))}
        </ul>
      ) : <EmptyState title={t("pages.noPublications")} />)}
    </PageShell>
  );
}

function HubCard({ title, href, ok, text }: { title: string; href: string; ok: boolean; text: string }) {
  return (
    <Link href={href} className="bg-white p-5 hover:bg-paper-2">
      <h2 className="font-serif text-xl">{title}</h2>
      <p className={`mt-1 text-sm ${ok ? "text-muted" : "text-danger"}`}>{text}</p>
    </Link>
  );
}
