import { PageBanner } from "@/components/ui/page-banner";
import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { Chip, SearchBar } from "@/components/ui/section";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { Link } from "@/i18n/navigation";
import { apiTry } from "@/lib/api";
import { SITE_IMAGES } from "@/lib/images";
import { pagesMeta } from "@/lib/page-meta";
import { emptyPage, type Category, type Hadith, type Paged } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 120;
export const generateMetadata = () => pagesMeta("hadithTitle");

export default async function HadithPage({
  searchParams,
}: {
  searchParams: Promise<{ q?: string; category?: string }>;
}) {
  const t = await getTranslations();
  const sp = await searchParams;
  const qs = new URLSearchParams({ size: "20" });
  if (sp.q) qs.set("q", sp.q);
  if (sp.category) qs.set("category", sp.category);
  const [daily, list, categories] = await Promise.all([
    apiTry<Hadith>("/api/hadith/daily"),
    apiTry<Paged<Hadith>>(`/api/hadith?${qs}`),
    apiTry<Category[]>("/api/hadith/categories"),
  ]);
  const items = list.ok ? list.data : emptyPage<Hadith>();
  return (
    <PageShell width="narrow">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.hadithTitle") }]} />
      <PageIntro title={t("pages.hadithTitle")} lead={t("pages.hadithLead")} />
      <PageBanner src={SITE_IMAGES.quranManuscript.src} alt={SITE_IMAGES.quranManuscript.alt} />
      <SearchBar defaultValue={sp.q} placeholder={t("pages.hadithSearch")}>
        {sp.category ? <input type="hidden" name="category" value={sp.category} /> : null}
      </SearchBar>
      <div className="mb-8 flex flex-wrap gap-2">
        <Chip href="/hadis" active={!sp.category}>{t("common.all")}</Chip>
        {(categories.ok ? categories.data : []).map((c) => (
          <Chip key={c.slug} href={`/hadis?category=${c.slug}`} active={sp.category === c.slug}>
            {c.name}
          </Chip>
        ))}
      </div>
      {daily.ok ? (
        <article className="mb-10 border-b border-line pb-10">
          <p className="kicker">{t("pages.dailyHadith")}</p>
          <p className="arabic mt-4 text-right text-2xl leading-[1.9]" dir="rtl">{daily.data.textAr}</p>
          <h2 className="mt-4 font-serif text-2xl">{daily.data.title}</h2>
          <p className="mt-3 leading-7 text-muted">{daily.data.textTr}</p>
          <p className="mt-3 text-xs text-muted">{daily.data.source} · {daily.data.narrator}</p>
        </article>
      ) : (
        <ErrorState retryHref="/hadis" description={t("pages.hadithUnavailable")} />
      )}
      {!list.ok ? (
        <ErrorState retryHref="/hadis" />
      ) : items.content.length === 0 ? (
        <EmptyState title={t("pages.noHadith")} description={t("pages.noHadithHint")} />
      ) : (
        <ul className="divide-y divide-line border-y border-line">
          {items.content.map((h) => (
            <li key={h.id}>
              <Link href={`/hadis/${h.slug}`} className="block py-4 hover:text-forest">
                <h3 className="font-serif text-lg">{h.title}</h3>
                <p className="mt-1 line-clamp-2 text-sm text-muted">{h.textTr}</p>
              </Link>
            </li>
          ))}
        </ul>
      )}
    </PageShell>
  );
}
