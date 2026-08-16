"use client";

import type { ProvinceSummary } from "@/types/api";
import { useRouter } from "@/i18n/navigation";
import * as React from "react";
import "leaflet/dist/leaflet.css";

export function TurkeyMap({ provinces }: { provinces: ProvinceSummary[] }) {
  const router = useRouter();
  const [ready, setReady] = React.useState(false);
  const ref = React.useRef<HTMLDivElement>(null);

  React.useEffect(() => {
    let map: import("leaflet").Map | undefined;
    (async () => {
      const L = await import("leaflet");
      if (!ref.current) return;
      map = L.map(ref.current, { scrollWheelZoom: false }).setView([39.0, 35.2], 6);
      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "&copy; OpenStreetMap",
      }).addTo(map);
      provinces.forEach((p) => {
        if (p.lat == null || p.lng == null) return;
        L.circleMarker([p.lat, p.lng], {
          radius: 5,
          color: "#0B3D2E",
          fillColor: "#0B3D2E",
          fillOpacity: 0.9,
          weight: 1,
        })
          .addTo(map!)
          .bindTooltip(p.name)
          .on("click", () => router.push(`/il-muftulukleri/${p.slug}`));
      });
      setReady(true);
    })();
    return () => {
      map?.remove();
    };
  }, [provinces, router]);

  return (
    <div className="overflow-hidden border border-line">
      <div ref={ref} className="h-[420px] w-full bg-paper-2" />
      {!ready ? <p className="p-3 text-xs text-muted">Harita yükleniyor…</p> : null}
    </div>
  );
}
