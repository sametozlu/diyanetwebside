"use client";

import * as React from "react";

const KEY = "portal-a11y";

type Prefs = {
  scale: "sm" | "md" | "lg" | "xl";
  contrast: boolean;
  underline: boolean;
  reduceMotion: boolean;
};

const defaultPrefs: Prefs = {
  scale: "md",
  contrast: false,
  underline: false,
  reduceMotion: false,
};

const listeners = new Set<() => void>();

let cachedRaw: string | null = null;
let cachedPrefs: Prefs = defaultPrefs;
let hasClientSnapshot = false;

function emit() {
  listeners.forEach((listener) => listener());
}

function subscribe(listener: () => void) {
  listeners.add(listener);
  return () => {
    listeners.delete(listener);
  };
}

function parsePrefs(raw: string | null): Prefs {
  if (!raw) return defaultPrefs;
  try {
    return { ...defaultPrefs, ...(JSON.parse(raw) as Partial<Prefs>) };
  } catch {
    return defaultPrefs;
  }
}

function getClientSnapshot(): Prefs {
  let raw: string | null = null;
  try {
    raw = localStorage.getItem(KEY);
  } catch {
    return defaultPrefs;
  }
  if (hasClientSnapshot && raw === cachedRaw) {
    return cachedPrefs;
  }
  hasClientSnapshot = true;
  cachedRaw = raw;
  cachedPrefs = parsePrefs(raw);
  return cachedPrefs;
}

function getServerSnapshot(): Prefs {
  return defaultPrefs;
}

function apply(p: Prefs) {
  const root = document.documentElement;
  if (p.scale === "md") delete root.dataset.textScale;
  else root.dataset.textScale = p.scale;
  root.dataset.contrast = p.contrast ? "high" : "";
  root.dataset.underlineLinks = p.underline ? "true" : "";
  root.dataset.reduceMotion = p.reduceMotion ? "true" : "";
}

export function AccessibilityProvider({ children }: { children: React.ReactNode }) {
  React.useEffect(() => {
    apply(getClientSnapshot());
  }, []);
  return <>{children}</>;
}

export function useA11y() {
  const prefs = React.useSyncExternalStore(subscribe, getClientSnapshot, getServerSnapshot);

  const update = (next: Prefs) => {
    const raw = JSON.stringify(next);
    localStorage.setItem(KEY, raw);
    cachedRaw = raw;
    cachedPrefs = next;
    hasClientSnapshot = true;
    apply(next);
    emit();
  };

  return { prefs, update, reset: () => update(defaultPrefs) };
}
