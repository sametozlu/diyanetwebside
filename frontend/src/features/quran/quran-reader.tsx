"use client";

import { useQuery } from "@tanstack/react-query";
import { Bookmark, Minus, Plus, Share2 } from "lucide-react";
import { useTranslations } from "next-intl";
import * as React from "react";
import { apiGetSafe } from "@/lib/api";
import { quranAudioUrl } from "@/lib/utils";
import type { Ayah, SurahDetail, SurahSummary } from "@/types/api";
import { Link } from "@/i18n/navigation";
import { Input } from "@/components/ui/section";
import { EmptyState } from "@/components/ui/states";

export function QuranReader({ initial, surahs }: { initial: SurahDetail; surahs: SurahSummary[] }) {
  const t = useTranslations("pages");
  const tCommon = useTranslations("common");
  const [number, setNumber] = React.useState(initial.number);
  const [size, setSize] = React.useState(28);
  const [dark, setDark] = React.useState(false);
  const [q, setQ] = React.useState("");
  const [playing, setPlaying] = React.useState<Ayah | null>(null);
  const audioRef = React.useRef<HTMLAudioElement>(null);
  const { data } = useQuery({
    queryKey: ["surah", number],
    queryFn: () => apiGetSafe<SurahDetail | null>(`/api/quran/surahs/${number}`, null),
    initialData: number === initial.number ? initial : undefined,
  });
  const surah = data ?? initial;

  React.useEffect(() => {
    const raw = localStorage.getItem("quran-history");
    const hist: number[] = raw ? JSON.parse(raw) : [];
    localStorage.setItem("quran-history", JSON.stringify([number, ...hist.filter((n) => n !== number)].slice(0, 12)));
  }, [number]);

  const filtered = surahs.filter(
    (s) =>
      s.nameTr.toLowerCase().includes(q.toLowerCase()) ||
      s.nameEn.toLowerCase().includes(q.toLowerCase()) ||
      String(s.number) === q,
  );

  function play(ayah: Ayah) {
    setPlaying(ayah);
    const el = audioRef.current;
    if (!el) return;
    el.src = quranAudioUrl(surah.number, ayah.number);
    el.play().catch(() => undefined);
  }

  function bookmark(ayah: Ayah) {
    const key = "quran-bookmarks";
    const raw = localStorage.getItem(key);
    const items: string[] = raw ? JSON.parse(raw) : [];
    const id = `${surah.number}:${ayah.number}`;
    localStorage.setItem(key, JSON.stringify(items.includes(id) ? items.filter((x) => x !== id) : [...items, id]));
  }

  async function share(ayah: Ayah) {
    const text = `${surah.nameTr} ${ayah.number}: ${ayah.textTr}`;
    if (navigator.share) await navigator.share({ text });
    else await navigator.clipboard.writeText(text);
  }

  return (
    <div className={`grid gap-8 lg:grid-cols-[240px_minmax(0,1fr)] ${dark ? "bg-forest-deep p-4 text-[#efe6d4]" : ""}`}>
      <aside className="border border-line bg-white p-3">
        <Input
          value={q}
          onChange={(e) => setQ(e.target.value)}
          placeholder={t("searchSurah")}
          aria-label={t("searchSurah")}
          className="mb-3 h-10"
        />
        <div className="mb-3 flex flex-wrap gap-1">
          {Array.from({ length: 30 }, (_, i) => i + 1).map((j) => (
            <button
              key={j}
              type="button"
              className="border border-line px-1.5 py-0.5 text-[11px] text-ink hover:border-forest"
              onClick={() => {
                const first = surahs.find((s) => s.juzStart === j);
                if (first) setNumber(first.number);
              }}
            >
              {j}
            </button>
          ))}
        </div>
        <ul className="max-h-[70vh] overflow-auto text-sm">
          {filtered.map((s) => (
            <li key={s.number}>
              <button
                type="button"
                onClick={() => setNumber(s.number)}
                className={`flex w-full items-center justify-between px-2 py-1.5 text-start ${s.number === number ? "bg-forest text-white" : "hover:bg-paper-2 text-ink"}`}
              >
                <span>
                  {s.number}. {s.nameTr}
                </span>
                <span className="arabic" dir="rtl">
                  {s.nameAr}
                </span>
              </button>
            </li>
          ))}
        </ul>
      </aside>
      <div>
        <div className="mb-6 flex flex-wrap items-end justify-between gap-3 border-b border-line pb-4">
          <div>
            <h2 className="font-serif text-2xl md:text-3xl">
              {surah.number}. {surah.nameTr}
            </h2>
            <p className="arabic text-2xl" dir="rtl">
              {surah.nameAr}
            </p>
            <p className="text-xs text-muted">
              {surah.revelationType} · {t("ayahCount", { count: surah.ayahCount })} · {t("juz", { n: surah.juzStart ?? 1 })}
            </p>
          </div>
          <div className="flex items-center gap-2">
            <button type="button" className="border border-line p-2" onClick={() => setSize((s) => Math.max(18, s - 2))} aria-label={t("decreaseText")}>
              <Minus className="h-4 w-4" />
            </button>
            <button type="button" className="border border-line p-2" onClick={() => setSize((s) => Math.min(48, s + 2))} aria-label={t("increaseText")}>
              <Plus className="h-4 w-4" />
            </button>
            <button type="button" className="border border-line px-3 py-2 text-xs" onClick={() => setDark((v) => !v)}>
              {dark ? t("lightBg") : t("darkBg")}
            </button>
            <Link href={`/kuran/${surah.number}`} className="text-xs text-forest hover:underline">
              {t("permalink")}
            </Link>
          </div>
        </div>
        {surah.ayahs.length === 0 ? (
          <EmptyState title={t("quranTextFailed")} description={t("quranTextFailedHint")} />
        ) : (
          <ol className="space-y-8">
            {surah.ayahs.map((ayah) => (
              <li key={ayah.number} id={`ayet-${ayah.number}`} className="border-b border-line pb-6">
                <p className="arabic text-right leading-[2.15]" dir="rtl" style={{ fontSize: size }}>
                  {ayah.textAr}
                  <span className="ms-2 inline-flex h-7 w-7 items-center justify-center border border-forest text-sm">
                    {ayah.number}
                  </span>
                </p>
                <p className="mt-3 max-w-3xl text-[16px] leading-8">{ayah.textTr}</p>
                <div className="mt-3 flex gap-3 text-xs text-forest">
                  <button type="button" onClick={() => play(ayah)}>
                    {t("audio")}
                  </button>
                  <button type="button" onClick={() => bookmark(ayah)} aria-label={tCommon("bookmark")}>
                    <Bookmark className="inline h-3.5 w-3.5" />
                  </button>
                  <button type="button" onClick={() => share(ayah)} aria-label={tCommon("share")}>
                    <Share2 className="inline h-3.5 w-3.5" />
                  </button>
                </div>
              </li>
            ))}
          </ol>
        )}
        <p className="mt-6 text-xs text-muted">{surah.translationNote}</p>
        <audio ref={audioRef} className="mt-4 w-full" controls src={playing ? quranAudioUrl(surah.number, playing.number) : undefined} />
      </div>
    </div>
  );
}
