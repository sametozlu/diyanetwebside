import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatDate(value?: string | null, locale = "tr-TR") {
  if (!value) return "";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return new Intl.DateTimeFormat(locale, {
    day: "numeric",
    month: "long",
    year: "numeric",
  }).format(date);
}

export function formatTime(value?: string | null) {
  if (!value) return "—";
  return value.slice(0, 5);
}

export function eventDateParts(value?: string | null, locale = "tr-TR") {
  if (!value) return { day: "—", month: "" };
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return { day: "—", month: "" };
  return {
    day: new Intl.DateTimeFormat(locale, { day: "2-digit" }).format(date),
    month: new Intl.DateTimeFormat(locale, { month: "short" }).format(date).replace(".", ""),
  };
}

export function pad(n: number, size = 3) {
  return String(n).padStart(size, "0");
}

export function quranAudioUrl(surah: number, ayah: number) {
  return `https://everyayah.com/data/Alafasy_128kbps/${pad(surah)}${pad(ayah)}.mp3`;
}
