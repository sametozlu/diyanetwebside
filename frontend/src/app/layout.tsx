import type { Metadata } from "next";
import type { ReactNode } from "react";
import { Noto_Naskh_Arabic, Source_Sans_3, Source_Serif_4 } from "next/font/google";
import { AccessibilityProvider } from "@/components/a11y/accessibility-provider";
import { QueryProvider } from "@/components/providers";
import "./globals.css";

const sans = Source_Sans_3({
  subsets: ["latin", "latin-ext"],
  variable: "--font-source-sans",
  display: "swap",
});

const serif = Source_Serif_4({
  subsets: ["latin", "latin-ext"],
  variable: "--font-source-serif",
  display: "swap",
});

const naskh = Noto_Naskh_Arabic({
  subsets: ["arabic"],
  variable: "--font-naskh",
  display: "swap",
  weight: ["400", "500", "600", "700"],
});

export const metadata: Metadata = {
  metadataBase: new URL(process.env.NEXT_PUBLIC_SITE_URL || "http://localhost:3000"),
  title: {
    default: "Kurumsal Dijital Kapı — Kavramsal Kamu Portalı",
    template: "%s · Dijital Kapı",
  },
  description:
    "Kavramsal demo: din hizmetleri, haber, namaz vakitleri, Kur’an, hadis ve 81 il müftülüğü tek kapıda. Resmi kurum sitesi değildir.",
  robots: { index: false, follow: false },
  alternates: { canonical: "/" },
  openGraph: {
    type: "website",
    locale: "tr_TR",
    siteName: "Dijital Kapı",
    title: "Kurumsal Dijital Kapı",
    description: "Kavramsal kamu dijital kapısı. Resmi Diyanet sitesi değildir.",
  },
  twitter: { card: "summary_large_image" },
};

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="tr" className={`${sans.variable} ${serif.variable} ${naskh.variable} h-full antialiased`}>
      <body className="min-h-full bg-canvas font-sans text-ink">
        <QueryProvider>
          <AccessibilityProvider>{children}</AccessibilityProvider>
        </QueryProvider>
      </body>
    </html>
  );
}
