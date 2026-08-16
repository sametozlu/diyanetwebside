import type { ReactNode } from "react";
import { getTranslations } from "next-intl/server";
import { Footer } from "@/components/layout/footer";
import { Header } from "@/components/layout/header";

export default async function SiteLayout({ children }: { children: ReactNode }) {
  const t = await getTranslations("demo");
  return (
    <div className="flex min-h-screen flex-col">
      <div className="bg-forest-deep px-4 py-2 text-center text-[11px] leading-5 text-white/80">{t("banner")}</div>
      <Header />
      <div id="icerik" className="flex-1 pb-16 md:pb-0" tabIndex={-1}>
        {children}
      </div>
      <Footer />
    </div>
  );
}
