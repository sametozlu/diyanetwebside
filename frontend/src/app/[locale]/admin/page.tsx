"use client";

import * as React from "react";
import { Link, useRouter } from "@/i18n/navigation";
import { apiGet, apiSend } from "@/lib/api";
import type { NewsSummary, Paged } from "@/types/api";

export default function AdminHome() {
  const router = useRouter();
  const [news, setNews] = React.useState<NewsSummary[]>([]);
  const [title, setTitle] = React.useState("");
  const [slug, setSlug] = React.useState("");

  React.useEffect(() => {
    const token = sessionStorage.getItem("portal-token");
    if (!token) {
      router.replace("/admin/login");
      return;
    }
    apiGet<Paged<NewsSummary>>("/api/admin/news", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((d) => setNews(d.content))
      .catch(() => router.replace("/admin/login"));
  }, [router]);

  async function createNews(e: React.FormEvent) {
    e.preventDefault();
    const token = sessionStorage.getItem("portal-token") ?? "";
    await apiSend("/api/admin/news", "POST", {
      title,
      slug,
      summary: "Demo kayıt",
      body: "<p>Demo gövde</p>",
      featured: false,
      locale: "tr",
      status: "PUBLISHED",
    }, token);
    const d = await apiGet<Paged<NewsSummary>>("/api/admin/news", {
      headers: { Authorization: `Bearer ${token}` },
    });
    setNews(d.content);
    setTitle("");
    setSlug("");
  }

  async function remove(id: number) {
    const token = sessionStorage.getItem("portal-token") ?? "";
    await apiSend(`/api/admin/news/${id}`, "DELETE", undefined, token);
    setNews((rows) => rows.filter((r) => r.id !== id));
  }

  return (
    <main className="mx-auto max-w-5xl px-4 py-8">
      <div className="mb-6 flex gap-4 text-sm">
        <Link href="/">Siteye dön</Link>
        <span>Haberler</span>
      </div>
      <h1 className="text-2xl font-semibold">İçerik yönetimi</h1>
      <form onSubmit={createNews} className="mt-4 flex flex-wrap gap-2">
        <label className="grid gap-1 text-sm">
          Başlık
          <input className="h-10 rounded border px-3" value={title} onChange={(e) => setTitle(e.target.value)} required />
        </label>
        <label className="grid gap-1 text-sm">
          Slug
          <input className="h-10 rounded border px-3" value={slug} onChange={(e) => setSlug(e.target.value)} required />
        </label>
        <button className="mt-5 h-10 rounded bg-zinc-900 px-4 text-white" type="submit">Ekle</button>
      </form>
      <table className="mt-6 w-full text-left text-sm">
        <thead>
          <tr className="border-b">
            <th className="py-2">Başlık</th>
            <th>Slug</th>
            <th>İşlem</th>
          </tr>
        </thead>
        <tbody>
          {news.map((n) => (
            <tr key={n.id} className="border-b">
              <td className="py-2">{n.title}</td>
              <td>{n.slug}</td>
              <td>
                <button type="button" className="text-red-700" onClick={() => remove(n.id)} aria-label={`${n.title} kaydını sil`}>
                  Sil
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </main>
  );
}
