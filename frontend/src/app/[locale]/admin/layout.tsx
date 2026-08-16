import type { ReactNode } from "react";

export default function AdminLayout({ children }: { children: ReactNode }) {
  return (
    <div className="min-h-screen bg-zinc-100 text-zinc-900">
      <div className="border-b bg-zinc-900 px-4 py-3 text-sm text-white">
        Yönetim paneli · DEMO · kamu sitesinden ayrı arayüz
      </div>
      {children}
    </div>
  );
}
