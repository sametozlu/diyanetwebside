import { expect, test } from "@playwright/test";

const routes = [
  "/",
  "/haberler",
  "/namaz-vakitleri",
  "/kuran",
  "/hadis",
  "/fetva",
  "/hutbeler",
  "/yayinlar",
  "/medya",
  "/etkinlikler",
  "/il-muftulukleri",
  "/arama",
  "/iletisim",
  "/dini-bilgiler",
  "/dini-gunler",
];

test.describe("public pages", () => {
  for (const route of routes) {
    test(`loads ${route}`, async ({ page }) => {
      const response = await page.goto(route, { waitUntil: "domcontentloaded" });
      expect(response?.ok()).toBeTruthy();
      await expect(page.locator("body")).toBeVisible();
    });
  }
});
