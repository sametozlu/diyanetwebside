"use client";

import Image from "next/image";
import { useTranslations } from "next-intl";
import * as React from "react";
import { cn } from "@/lib/utils";

const RATIOS = {
  hero: "aspect-[16/9]",
  news: "aspect-[16/9]",
  card: "aspect-[4/3]",
  portrait: "aspect-[3/4]",
  square: "aspect-square",
  video: "aspect-video",
  banner: "aspect-[5/3] md:aspect-[16/9]",
} as const;

function Fallback() {
  const t = useTranslations("brand");
  const tPages = useTranslations("pages");
  return (
    <div className="absolute inset-0 flex flex-col items-center justify-center bg-forest px-4 text-center">
      <span className="font-serif text-lg text-white">{t("name")}</span>
      <span className="mt-1 text-[11px] uppercase tracking-widest text-white/70">{tPages("coverInstitutional")}</span>
    </div>
  );
}

export function CoverImage({
  src,
  alt,
  ratio = "news",
  className,
  sizes,
  priority = false,
}: {
  src?: string | null;
  alt: string;
  ratio?: keyof typeof RATIOS;
  className?: string;
  sizes?: string;
  priority?: boolean;
}) {
  const [failedSrc, setFailedSrc] = React.useState<string | null>(null);
  const [loadedSrc, setLoadedSrc] = React.useState<string | null>(null);
  const showImage = Boolean(src) && failedSrc !== src;
  const loaded = showImage && loadedSrc === src;
  const isSvg = Boolean(src?.endsWith(".svg"));

  return (
    <div className={cn("relative overflow-hidden bg-forest-soft", RATIOS[ratio], className)}>
      {showImage ? (
        <>
          {!loaded ? (
            <div className="absolute inset-0 animate-pulse bg-forest-soft" aria-hidden />
          ) : null}
          <Image
            src={src!}
            alt={alt}
            fill
            priority={priority}
            sizes={sizes ?? "(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 640px"}
            unoptimized={isSvg}
            className="object-cover object-center"
            onLoad={() => setLoadedSrc(src ?? null)}
            onError={() => setFailedSrc(src ?? null)}
          />
        </>
      ) : (
        <Fallback />
      )}
    </div>
  );
}
