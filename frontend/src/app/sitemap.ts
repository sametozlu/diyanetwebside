import type { MetadataRoute } from "next";

const paths = [
  "",
  "/haberler",
  "/kuran",
  "/hadis",
  "/fetva",
  "/hutbeler",
  "/hac-umre",
  "/il-muftulukleri",
  "/hizmetler",
  "/yayinlar",
  "/medya",
  "/etkinlikler",
  "/namaz-vakitleri",
  "/dini-bilgiler",
  "/dini-gunler",
  "/arama",
  "/iletisim",
  "/gorsel-kaynaklari",
];

export default function sitemap(): MetadataRoute.Sitemap {
  const base = process.env.NEXT_PUBLIC_SITE_URL || "http://localhost:3000";
  return paths.map((path) => ({
    url: `${base}${path}`,
    lastModified: new Date(),
    changeFrequency: path === "" ? "hourly" : "daily",
    priority: path === "" ? 1 : 0.7,
  }));
}
