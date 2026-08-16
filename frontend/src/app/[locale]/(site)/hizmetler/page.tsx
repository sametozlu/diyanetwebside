import { Breadcrumb, PageIntro } from "@/components/ui/page-intro";
import { Link } from "@/i18n/navigation";
import { apiGetSafe } from "@/lib/api";
import { pagesMeta } from "@/lib/page-meta";
import type { ServiceItem } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 300;
export const generateMetadata = () => pagesMeta("servicesTitle");

export default async function ServicesPage() {
  const t = await getTranslations();
  const services = await apiGetSafe<ServiceItem[]>("/api/services", []);
  return (
    <main className="mx-auto max-w-5xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { label: t("pages.servicesTitle") }]} />
      <PageIntro title={t("pages.servicesTitle")} lead={t("pages.servicesLead")} />
      <div className="grid gap-px border border-line bg-line sm:grid-cols-2 lg:grid-cols-3">
        {services.map((s) => (
          <Link key={s.slug} href={s.href || "/"} className="bg-white p-5 hover:bg-paper-2">
            <p className="kicker">{s.category}</p>
            <h2 className="mt-1 font-serif text-lg">{s.title}</h2>
            <p className="mt-1 text-sm text-muted">{s.summary}</p>
          </Link>
        ))}
      </div>
    </main>
  );
}
