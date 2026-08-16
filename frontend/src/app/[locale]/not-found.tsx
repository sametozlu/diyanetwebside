import { getTranslations } from "next-intl/server";
import { Link } from "@/i18n/navigation";

export default async function NotFound() {
  const t = await getTranslations("pages");
  return (
    <main className="mx-auto max-w-xl px-4 py-24 text-center">
      <p className="kicker">404</p>
      <h1 className="mt-3 font-serif text-4xl">{t("notFoundTitle")}</h1>
      <p className="mt-3 text-muted">{t("notFoundLead")}</p>
      <Link href="/" className="mt-6 inline-block text-forest underline">
        {t("notFoundHome")}
      </Link>
    </main>
  );
}
