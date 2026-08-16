import { describe, expect, it } from "vitest";
import { formatTime, pad } from "@/lib/utils";

describe("utils", () => {
  it("formats prayer times", () => {
    expect(formatTime("04:12:00")).toBe("04:12");
    expect(formatTime(null)).toBe("—");
  });

  it("pads quran audio numbers", () => {
    expect(pad(1)).toBe("001");
    expect(pad(114)).toBe("114");
  });
});

describe("search grouping", () => {
  it("keeps category keys", () => {
    const groups = [
      { type: "news", items: [{ slug: "a", title: "A", summary: "", href: "/haberler/a" }] },
      { type: "fatwa", items: [{ slug: "b", title: "B", summary: "", href: "/fetva/b" }] },
    ];
    expect(groups.map((g) => g.type)).toEqual(["news", "fatwa"]);
  });
});
