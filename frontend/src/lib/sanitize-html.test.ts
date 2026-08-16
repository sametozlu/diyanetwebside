import { describe, expect, it } from "vitest";
import { sanitizeHtml } from "@/lib/sanitize-html";

describe("sanitizeHtml", () => {
  it("keeps ordinary markup", () => {
    expect(sanitizeHtml("<p>Merhaba <strong>dünya</strong></p>")).toBe(
      "<p>Merhaba <strong>dünya</strong></p>",
    );
  });

  it("strips script tags and event handlers", () => {
    const dirty = `<p onclick="alert(1)">x</p><script>alert(1)</script>`;
    const clean = sanitizeHtml(dirty);
    expect(clean).not.toMatch(/script/i);
    expect(clean).not.toMatch(/onclick/i);
  });

  it("neutralizes javascript URLs", () => {
    expect(sanitizeHtml('<a href="javascript:alert(1)">x</a>')).not.toMatch(/javascript/i);
  });

  it("returns empty string for missing input", () => {
    expect(sanitizeHtml(null)).toBe("");
    expect(sanitizeHtml(undefined)).toBe("");
  });
});
