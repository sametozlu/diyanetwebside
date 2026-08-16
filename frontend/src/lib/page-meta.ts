import type { Metadata } from "next";
import { getTranslations } from "next-intl/server";

export async function pagesMeta(key: string): Promise<Metadata> {
  const t = await getTranslations("pages");
  return { title: t(key) };
}
