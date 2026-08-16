"use client";

import * as DropdownMenu from "@radix-ui/react-dropdown-menu";
import { Menu, Search, X } from "lucide-react";
import { useLocale, useTranslations } from "next-intl";
import * as React from "react";
import { Mark } from "@/components/brand/mark";
import { AccessibilityMenu } from "@/components/a11y/accessibility-menu";
import { MobileDock } from "@/components/layout/mobile-dock";
import { SearchPalette } from "@/features/search/search-palette";
import { Link, usePathname, useRouter } from "@/i18n/navigation";
import { cn } from "@/lib/utils";

const RELIGIOUS = [
  { href: "/hadis", key: "hadith" as const },
  { href: "/fetva", key: "fatwa" as const },
  { href: "/hutbeler", key: "sermons" as const },
  { href: "/namaz-vakitleri", key: "prayerTimes" as const },
  { href: "/dini-bilgiler", key: "religious" as const },
  { href: "/hac-umre", key: "hajj" as const },
];

const PUBLISHING = [
  { href: "/yayinlar", key: "publications" as const },
  { href: "/medya", key: "media" as const },
];

const INSTITUTIONAL = [
  { href: "/baskanligimiz", key: "presidency" as const },
  { href: "/il-muftulukleri", key: "provinces" as const },
  { href: "/hizmetler", key: "services" as const },
  { href: "/iletisim", key: "contact" as const },
];

const MOBILE_NAV = [
  { href: "/", key: "home" as const },
  { href: "/haberler", key: "news" as const },
  { href: "/kuran", key: "quran" as const },
  { href: "/etkinlikler", key: "events" as const },
  ...RELIGIOUS,
  ...PUBLISHING,
  ...INSTITUTIONAL,
];

function isActive(pathname: string, href: string) {
  return href === "/" ? pathname === "/" : pathname === href || pathname.startsWith(`${href}/`);
}

function NavDropdown({
  label,
  items,
  pathname,
  t,
}: {
  label: string;
  items: { href: string; key: string }[];
  pathname: string;
  t: (key: string) => string;
}) {
  const active = items.some((item) => isActive(pathname, item.href));
  return (
    <DropdownMenu.Root>
      <DropdownMenu.Trigger className={cn("nav-link", active && "is-active")} aria-haspopup="menu">
        {label}
      </DropdownMenu.Trigger>
      <DropdownMenu.Portal>
        <DropdownMenu.Content
          align="start"
          sideOffset={4}
          className="z-50 min-w-52 border border-line bg-white py-1 shadow-[0_8px_24px_rgb(6_36_28_/_0.08)]"
        >
          {items.map((item) => (
            <DropdownMenu.Item key={item.href} asChild>
              <Link
                href={item.href}
                className={cn(
                  "block px-3 py-2 text-sm text-ink outline-none hover:bg-paper-2 hover:text-forest",
                  isActive(pathname, item.href) && "text-forest",
                )}
              >
                {t(`nav.${item.key}`)}
              </Link>
            </DropdownMenu.Item>
          ))}
        </DropdownMenu.Content>
      </DropdownMenu.Portal>
    </DropdownMenu.Root>
  );
}

export function Header() {
  const t = useTranslations();
  const locale = useLocale();
  const pathname = usePathname();
  const router = useRouter();
  const [scrolled, setScrolled] = React.useState(false);
  const [open, setOpen] = React.useState(false);
  const [searchOpen, setSearchOpen] = React.useState(false);

  React.useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 8);
    onScroll();
    window.addEventListener("scroll", onScroll, { passive: true });
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  React.useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === "k") {
        e.preventDefault();
        setSearchOpen(true);
      }
      if (e.key === "Escape") setOpen(false);
    };
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, []);

  React.useEffect(() => {
    document.body.style.overflow = open ? "hidden" : "";
    return () => {
      document.body.style.overflow = "";
    };
  }, [open]);

  return (
    <>
      <a href="#icerik" className="skip-link">
        {t("common.skipToContent")}
      </a>
      <header className={cn("site-header", scrolled && "is-scrolled")}>
        <div className="bg-forest-deep text-white">
          <div className="mx-auto flex max-w-7xl items-center justify-between gap-3 px-4 py-1.5 text-[12px]">
            <div className="flex items-center gap-4">
              <span className="font-medium">{t("utility.brand")}</span>
              <Link href="/baskanligimiz" className="hidden hover:underline sm:inline">
                {t("utility.presidency")}
              </Link>
              <Link href="/erisilebilirlik" className="hidden hover:underline md:inline">
                {t("utility.accessibility")}
              </Link>
            </div>
            <div className="flex items-center gap-3">
              <AccessibilityMenu compact />
              <select
                aria-label={t("nav.language")}
                className="bg-transparent text-white focus:outline-none focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-white"
                value={locale}
                onChange={(e) => router.replace(pathname, { locale: e.target.value as "tr" | "en" | "ar" })}
              >
                <option value="tr" className="text-ink">
                  TR
                </option>
                <option value="en" className="text-ink">
                  EN
                </option>
                <option value="ar" className="text-ink">
                  AR
                </option>
              </select>
            </div>
          </div>
        </div>
        <div className="mx-auto flex max-w-7xl items-center gap-4 px-4 py-2.5">
          <Link href="/" className="flex min-w-0 items-center gap-3">
            <Mark />
            <span className="leading-tight">
              <span className="block font-serif text-lg text-forest">{t("brand.name")}</span>
              <span className="hidden text-[11px] uppercase tracking-[0.14em] text-muted sm:block">
                {t("brand.tagline")}
              </span>
            </span>
          </Link>
          <nav className="hidden flex-1 items-center justify-center xl:flex" aria-label={t("common.mainMenu")}>
            <Link href="/" className={cn("nav-link", isActive(pathname, "/") && "is-active")}>
              {t("nav.home")}
            </Link>
            <Link href="/haberler" className={cn("nav-link", isActive(pathname, "/haberler") && "is-active")}>
              {t("nav.news")}
            </Link>
            <NavDropdown label={t("nav.religiousServices")} items={RELIGIOUS} pathname={pathname} t={t} />
            <Link href="/kuran" className={cn("nav-link", isActive(pathname, "/kuran") && "is-active")}>
              {t("nav.quran")}
            </Link>
            <NavDropdown label={t("nav.publications")} items={PUBLISHING} pathname={pathname} t={t} />
            <Link href="/etkinlikler" className={cn("nav-link", isActive(pathname, "/etkinlikler") && "is-active")}>
              {t("nav.events")}
            </Link>
            <NavDropdown label={t("nav.institutional")} items={INSTITUTIONAL} pathname={pathname} t={t} />
          </nav>
          <div className="ml-auto flex items-center gap-1">
            <Link
              href="/arama"
              className="hidden h-10 items-center border border-line px-3 text-sm text-muted hover:border-forest hover:text-forest sm:inline-flex"
            >
              <Search className="mr-2 h-4 w-4" />
              {t("nav.search")}
              <kbd className="ms-3 hidden border border-line px-1.5 text-[10px] text-muted lg:inline">Ctrl K</kbd>
            </Link>
            <button
              type="button"
              aria-label={t("nav.search")}
              onClick={() => setSearchOpen(true)}
              className="inline-flex h-10 w-10 items-center justify-center hover:bg-paper-2 sm:hidden"
            >
              <Search className="h-5 w-5" />
            </button>
            <button
              type="button"
              className="inline-flex h-10 w-10 items-center justify-center hover:bg-paper-2 xl:hidden"
              aria-label={open ? t("common.closeMenu") : t("nav.menu")}
              aria-expanded={open}
              aria-controls="mobile-nav"
              onClick={() => setOpen((v) => !v)}
            >
              {open ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
            </button>
          </div>
        </div>
      </header>
      {open ? (
        <div className="fixed inset-0 z-50 xl:hidden">
          <button type="button" className="absolute inset-0 bg-forest-deep/40" aria-label={t("common.closeMenu")} onClick={() => setOpen(false)} />
          <div
            id="mobile-nav"
            role="dialog"
            aria-modal="true"
            aria-label={t("common.mobileMenu")}
            className="absolute inset-y-0 end-0 w-[min(20rem,88vw)] overflow-y-auto bg-white p-4 shadow-lg"
          >
            <div className="mb-4 flex items-center justify-between">
              <p className="font-serif text-lg text-forest">{t("common.menu")}</p>
              <button type="button" className="h-10 w-10" aria-label={t("common.close")} onClick={() => setOpen(false)}>
                <X className="mx-auto h-5 w-5" />
              </button>
            </div>
            <nav className="grid gap-1">
              {MOBILE_NAV.map((item) => (
                <Link
                  key={item.href}
                  href={item.href}
                  className={cn("px-2 py-2.5 text-sm", isActive(pathname, item.href) ? "bg-forest-soft text-forest" : "hover:bg-paper-2")}
                  onClick={() => setOpen(false)}
                >
                  {t(`nav.${item.key}`)}
                </Link>
              ))}
            </nav>
          </div>
        </div>
      ) : null}
      <SearchPalette open={searchOpen} onOpenChange={setSearchOpen} />
      <MobileDock onSearch={() => setSearchOpen(true)} />
    </>
  );
}
