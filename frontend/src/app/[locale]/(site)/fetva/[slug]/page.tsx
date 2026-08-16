import { notFound } from "next/navigation";
import { Breadcrumb } from "@/components/ui/page-intro";
import { apiGet } from "@/lib/api";
import type { Fatwa } from "@/types/api";
import { Link } from "@/i18n/navigation";
import { formatDate } from "@/lib/utils";
import { getTranslations } from "next-intl/server";

export default async function FatwaDetail({ params }: { params: Promise<{ slug: string }> }) {
  const t = await getTranslations();
  const { slug } = await params;
  let item: Fatwa;
  try {
    item = await apiGet<Fatwa>(`/api/fatwas/${slug}`);
  } catch {
    notFound();
  }
  const related = (item.related ?? []).filter((x) => x.slug !== item.slug);
  return (
    <article className="mx-auto max-w-3xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { href: "/fetva", label: t("pages.fatwaTitle") }, { label: item.question }]} />
      <p className="kicker">{item.category} · {formatDate(item.publishedAt)}</p>
      <h1 className="mt-2 font-serif text-3xl">{item.question}</h1>
      <p className="mt-6 leading-8">{item.answer}</p>
      <h2 className="mt-10 font-serif text-xl">{t("pages.relatedQuestions")}</h2>
      {related.length ? (
        <ul className="mt-3 space-y-2">
          {related.map((x) => (
            <li key={x.id}>
              <Link href={`/fetva/${x.slug}`} className="text-forest hover:underline">{x.question}</Link>
            </li>
          ))}
        </ul>
      ) : (
        <p className="mt-3 text-sm text-muted">{t("pages.noRelatedInCategory")}</p>
      )}
    </article>
  );
}
