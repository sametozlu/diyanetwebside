import { notFound } from "next/navigation";
import { Breadcrumb } from "@/components/ui/page-intro";
import { EmptyState } from "@/components/ui/states";
import { PrayerTimesPanel } from "@/features/prayer-times/prayer-times-panel";
import { Link } from "@/i18n/navigation";
import { apiGet } from "@/lib/api";
import type { ProvinceDetail } from "@/types/api";
import { getTranslations } from "next-intl/server";

export const revalidate = 120;

export async function generateMetadata({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = await params;
  const t = await getTranslations("pages");
  try {
    const item = await apiGet<ProvinceDetail>(`/api/provinces/${slug}`);
    return { title: t("provinceOfficeNamed", { name: item.name }) };
  } catch {
    return { title: t("provincesTitle") };
  }
}

export default async function ProvinceDetailPage({ params }: { params: Promise<{ slug: string }> }) {
  const t = await getTranslations();
  const { slug } = await params;
  let item: ProvinceDetail;
  try {
    item = await apiGet<ProvinceDetail>(`/api/provinces/${slug}`);
  } catch {
    notFound();
  }
  return (
    <main className="mx-auto max-w-5xl px-4 py-10">
      <Breadcrumb items={[{ href: "/", label: t("common.home") }, { href: "/il-muftulukleri", label: t("pages.provincesTitle") }, { label: item.name }]} />
      <p className="kicker">{t("pages.provinceOffice")}</p>
      <h1 className="mt-2 font-serif text-3xl">{t("pages.provinceOfficeNamed", { name: item.name })}</h1>
      <p className="mt-3 max-w-2xl text-muted">{item.about}</p>
      <dl className="mt-6 grid gap-3 sm:grid-cols-2">
        <Info label={t("pages.address")} value={item.address} empty={t("pages.noVerifiedRecord")} />
        <Info label={t("pages.phone")} value={item.phone} empty={t("pages.noVerifiedRecord")} />
        <Info label={t("pages.email")} value={item.email} empty={t("pages.noVerifiedRecord")} />
        <Info label={t("pages.web")} value={item.website} empty={t("pages.noVerifiedRecord")} />
      </dl>
      <div className="mt-8">
        <PrayerTimesPanel initial={item.prayerTimes} compact />
      </div>
      <h2 className="mt-8 font-serif text-2xl">{t("pages.districts")}</h2>
      {item.districts.length ? (
        <div className="mt-3 flex flex-wrap gap-2">
          {item.districts.map((d) => (
            <span key={d.slug} className="border border-line px-3 py-1 text-sm">{d.name}</span>
          ))}
        </div>
      ) : (
        <EmptyState title={t("pages.noDistricts")} description={t("pages.noDistrictsHint")} />
      )}
      <h2 className="mt-8 font-serif text-2xl">{t("pages.announcements")}</h2>
      {item.latestNews.length ? (
        <ul className="mt-3 space-y-2">
          {item.latestNews.map((n) => (
            <li key={n.id}><Link href={`/haberler/${n.slug}`} className="text-forest hover:underline">{n.title}</Link></li>
          ))}
        </ul>
      ) : (
        <EmptyState title={t("pages.noAnnouncements")} />
      )}
    </main>
  );
}

function Info({ label, value, empty }: { label: string; value?: string | null; empty: string }) {
  return (
    <div className="border border-line bg-white p-4">
      <dt className="text-xs text-muted">{label}</dt>
      <dd>{value || empty}</dd>
    </div>
  );
}
