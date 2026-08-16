"use client";

import { useTranslations } from "next-intl";
import * as React from "react";
import { useA11y } from "@/components/a11y/accessibility-provider";

export function AccessibilityMenu({ compact = false }: { compact?: boolean }) {
  const { prefs, update, reset } = useA11y();
  const t = useTranslations("a11y");
  const [open, setOpen] = React.useState(false);
  const rootRef = React.useRef<HTMLDivElement>(null);

  React.useEffect(() => {
    if (!open) return;
    const onDoc = (event: MouseEvent) => {
      if (rootRef.current && !rootRef.current.contains(event.target as Node)) {
        setOpen(false);
      }
    };
    const onKey = (event: KeyboardEvent) => {
      if (event.key === "Escape") setOpen(false);
    };
    document.addEventListener("mousedown", onDoc);
    document.addEventListener("keydown", onKey);
    return () => {
      document.removeEventListener("mousedown", onDoc);
      document.removeEventListener("keydown", onKey);
    };
  }, [open]);

  return (
    <div className="relative" ref={rootRef}>
      <button
        type="button"
        className="hover:underline"
        aria-expanded={open}
        aria-haspopup="true"
        onClick={() => setOpen((v) => !v)}
      >
        {compact ? "A11Y" : t("title")}
      </button>
      {open ? (
        <div className="absolute end-0 z-50 mt-2 w-64 border border-line bg-white p-3 text-start text-ink shadow-[0_8px_24px_rgb(6_36_28_/_0.1)]">
          <p className="mb-2 text-xs font-semibold uppercase tracking-widest text-muted">{t("title")}</p>
          <div className="grid gap-2 text-sm">
            <button
              type="button"
              className="text-start"
              onClick={() =>
                update({
                  ...prefs,
                  scale: prefs.scale === "xl" ? "xl" : prefs.scale === "lg" ? "xl" : prefs.scale === "md" ? "lg" : "md",
                })
              }
            >
              {t("increase")}
            </button>
            <button
              type="button"
              className="text-start"
              onClick={() =>
                update({
                  ...prefs,
                  scale: prefs.scale === "sm" ? "sm" : prefs.scale === "md" ? "sm" : prefs.scale === "lg" ? "md" : "lg",
                })
              }
            >
              {t("decrease")}
            </button>
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={prefs.contrast}
                onChange={(e) => update({ ...prefs, contrast: e.target.checked })}
              />
              {t("contrast")}
            </label>
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={prefs.underline}
                onChange={(e) => update({ ...prefs, underline: e.target.checked })}
              />
              {t("underline")}
            </label>
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={prefs.reduceMotion}
                onChange={(e) => update({ ...prefs, reduceMotion: e.target.checked })}
              />
              {t("motion")}
            </label>
            <button type="button" className="text-start text-forest" onClick={reset}>
              {t("reset")}
            </button>
          </div>
        </div>
      ) : null}
    </div>
  );
}
