"use client";

import { BookOpen, Clock, Home, Search } from "lucide-react";
import { useTranslations } from "next-intl";
import { Link } from "@/i18n/navigation";

export function MobileDock({ onSearch }: { onSearch: () => void }) {
  const t = useTranslations();
  return (
    <nav className="fixed inset-x-0 bottom-0 z-30 border-t border-line bg-white px-2 py-2 md:hidden" aria-label={t("common.mobileMenu")}>
      <ul className="mx-auto flex max-w-md items-center justify-around">
        <li>
          <Link href="/" className="flex flex-col items-center text-[10px]">
            <Home className="h-5 w-5" /> {t("nav.home")}
          </Link>
        </li>
        <li>
          <Link href="/namaz-vakitleri" className="flex flex-col items-center text-[10px]">
            <Clock className="h-5 w-5" /> {t("nav.prayerTimes")}
          </Link>
        </li>
        <li>
          <button type="button" onClick={onSearch} className="flex flex-col items-center text-[10px]" aria-label={t("nav.search")}>
            <Search className="h-5 w-5" /> {t("nav.search")}
          </button>
        </li>
        <li>
          <Link href="/kuran" className="flex flex-col items-center text-[10px]">
            <BookOpen className="h-5 w-5" /> {t("nav.quran")}
          </Link>
        </li>
      </ul>
    </nav>
  );
}
