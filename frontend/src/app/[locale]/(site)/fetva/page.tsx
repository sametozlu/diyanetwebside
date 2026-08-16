import { Breadcrumb, PageIntro, PageShell } from "@/components/ui/page-intro";
import { Chip, SearchBar } from "@/components/ui/section";
import { EmptyState, ErrorState } from "@/components/ui/states";
import { Link } from "@/i18n/navigation";
import { apiTry } from "@/lib/api";
import { pagesMeta } from "@/lib/page-meta";
import { emptyPage, type Category, type Fatwa, type Paged } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 120;
export const generateMetadata = () => pagesMeta("fatwaTitle");

export default async function FatwaPage({
  searchParams,
}: {
  searchParams: Promise<{ q?: string; category?: string }>;
}) {
  const t = await getTranslations();
  const sp = await searchParams;
  const qs = new URLSearchParams({ size: "20" });
  if (sp.q) qs.set("q", sp.q);
  if (sp.category) qs.set("category", sp.category);
  const [data, categories] = await Promise.all([
    apiTry<Paged<Fatwa>>(`/api/fatwas?${qs}`),
    apiTry<Category[]>("/api/fatwas/categories"),
  ]);
  const items = data.ok ? data.data : emptyPage<Fatwa>();
  return (
    <PageShell width="narrow">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.fatwaTitle") }]} />
      <PageIntro title={t("pages.fatwaTitle")} lead={t("pages.fatwaLead")} />
      <SearchBar defaultValue={sp.q} placeholder={t("pages.fatwaSearch")}>
        {sp.category ? <input type="hidden" name="category" value={sp.category} /> : null}
      </SearchBar>
      <div className="mb-8 flex flex-wrap gap-2">
        <Chip href={sp.q ? `/fetva?q=${encodeURIComponent(sp.q)}` : "/fetva"} active={!sp.category}>{t("common.all")}</Chip>
        {(categories.ok ? categories.data : []).map((c) => {
          const href = sp.q ? `/fetva?category=${c.slug}&q=${encodeURIComponent(sp.q)}` : `/fetva?category=${c.slug}`;
          return (
            <Chip key={c.slug} href={href} active={sp.category === c.slug}>
              {c.name}
            </Chip>
          );
        })}
      </div>
      {!data.ok ? (
        <ErrorState retryHref="/fetva" />
      ) : items.content.length === 0 ? (
        <EmptyState title={t("pages.emptyFilter")} />
      ) : (
        <ul className="divide-y divide-line border-y border-line">
          {items.content.map((f) => (
            <li key={f.id}>
              <Link href={`/fetva/${f.slug}`} className="block py-4 hover:text-forest">
                <p className="kicker">{f.category}</p>
                <h3 className="mt-1 font-serif text-lg">{f.question}</h3>
              </Link>
            </li>
          ))}
        </ul>
      )}
    </PageShell>
  );
}
