import { expect, test, type Page } from "@playwright/test";
import fs from "node:fs";
import path from "node:path";

const OUT = path.resolve(__dirname, "../../docs/screenshots");

async function shot(page: Page, name: string, width: number, height: number) {
  await page.setViewportSize({ width, height });
  await page.addStyleTag({
    content: "nextjs-portal, [data-next-badge-root] { display: none !important; }",
  });
  await page.waitForTimeout(600);
  await page.screenshot({ path: path.join(OUT, name), fullPage: true });
}

async function firstSlug(apiPath: string): Promise<string | null> {
  const res = await fetch(`http://localhost:8080${apiPath}`);
  if (!res.ok) return null;
  const data = (await res.json()) as { content?: { slug?: string }[] };
  return data.content?.[0]?.slug ?? null;
}

test.beforeAll(() => {
  fs.mkdirSync(OUT, { recursive: true });
});

test("capture live application screenshots", async ({ page }) => {
  const newsSlug = await firstSlug("/api/news?size=1");

  await page.goto("/", { waitUntil: "networkidle" });
  await expect(page.locator("body")).toBeVisible();
  await expect(page.locator("h2").filter({ hasText: /Ankara|Adana/i }).first()).toBeVisible({ timeout: 20_000 });
  await page.waitForTimeout(800);
  await shot(page, "homepage-desktop.png", 1440, 900);
  await shot(page, "homepage-mobile.png", 390, 844);

  await page.setViewportSize({ width: 1440, height: 900 });
  await page.goto("/haberler", { waitUntil: "networkidle" });
  await shot(page, "news-desktop.png", 1440, 900);
  await shot(page, "news-mobile.png", 390, 844);

  if (newsSlug) {
    await page.setViewportSize({ width: 1440, height: 900 });
    await page.goto(`/haberler/${newsSlug}`, { waitUntil: "networkidle" });
    await shot(page, "news-detail-desktop.png", 1440, 900);
    await shot(page, "news-detail-mobile.png", 390, 844);
  }

  await page.setViewportSize({ width: 1440, height: 900 });
  await page.goto("/kuran", { waitUntil: "networkidle" });
  await shot(page, "quran-desktop.png", 1440, 900);
  await shot(page, "quran-mobile.png", 390, 844);

  await page.setViewportSize({ width: 1440, height: 900 });
  await page.goto("/hadis", { waitUntil: "networkidle" });
  await shot(page, "hadith-desktop.png", 1440, 900);

  await page.goto("/fetva", { waitUntil: "networkidle" });
  await shot(page, "fatwa-desktop.png", 1440, 900);

  await page.goto("/hutbeler", { waitUntil: "networkidle" });
  await shot(page, "sermons-desktop.png", 1440, 900);
  await shot(page, "sermons-mobile.png", 390, 844);

  await page.setViewportSize({ width: 1440, height: 900 });
  await page.goto("/yayinlar", { waitUntil: "networkidle" });
  await shot(page, "publications-desktop.png", 1440, 900);
  await shot(page, "publications-mobile.png", 390, 844);

  await page.setViewportSize({ width: 1440, height: 900 });
  await page.goto("/etkinlikler", { waitUntil: "networkidle" });
  await shot(page, "events-desktop.png", 1440, 900);
  await shot(page, "events-mobile.png", 390, 844);

  await page.setViewportSize({ width: 1440, height: 900 });
  await page.goto("/il-muftulukleri", { waitUntil: "networkidle" });
  await shot(page, "provinces-desktop.png", 1440, 900);
  await shot(page, "provinces-mobile.png", 390, 844);

  await page.setViewportSize({ width: 1440, height: 900 });
  await page.goto("/namaz-vakitleri", { waitUntil: "networkidle" });
  await expect(page.locator("h1")).toBeVisible();
  await expect(page.locator("h2").filter({ hasText: /Ankara|Adana/i }).first()).toBeVisible({ timeout: 20_000 });
  await page.waitForTimeout(800);
  await shot(page, "prayer-times-desktop.png", 1440, 900);

  await page.goto("/medya", { waitUntil: "networkidle" });
  await shot(page, "media-desktop.png", 1440, 900);

  await page.goto("/arama", { waitUntil: "networkidle" });
  await shot(page, "search-page-desktop.png", 1440, 900);

  await page.setViewportSize({ width: 1440, height: 900 });
  await page.goto("/", { waitUntil: "networkidle" });
  await page.keyboard.press("Control+KeyK");
  await expect(page.locator("[cmdk-input], input[placeholder]").first()).toBeVisible({ timeout: 10_000 });
  await page.addStyleTag({
    content: "nextjs-portal, [data-next-badge-root] { display: none !important; }",
  });
  await page.waitForTimeout(400);
  await page.screenshot({ path: path.join(OUT, "search-desktop.png") });
});
