"use client";

import * as React from "react";
import { useRouter } from "@/i18n/navigation";
import { apiSend } from "@/lib/api";

export default function AdminLoginPage() {
  const router = useRouter();
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [error, setError] = React.useState("");

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    try {
      const res = await apiSend<{ token: string }>("/api/auth/login", "POST", { email, password });
      sessionStorage.setItem("portal-token", res.token);
      router.push("/admin");
    } catch {
      setError("Giriş başarısız. E-posta veya parolayı kontrol edin.");
    }
  }

  return (
    <main className="mx-auto max-w-sm px-4 py-16">
      <h1 className="text-xl font-semibold">Yönetici girişi</h1>
      <p className="mt-2 text-sm text-zinc-600">Demo ortamı. Üretim parolası kullanmayın.</p>
      <form onSubmit={onSubmit} className="mt-6 grid gap-3">
        <label className="grid gap-1 text-sm">
          E-posta
          <input
            className="h-10 rounded border px-3"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            type="email"
            autoComplete="username"
            required
          />
        </label>
        <label className="grid gap-1 text-sm">
          Parola
          <input
            className="h-10 rounded border px-3"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            type="password"
            autoComplete="current-password"
            required
          />
        </label>
        {error ? <p className="text-sm text-red-700">{error}</p> : null}
        <button className="h-10 rounded bg-zinc-900 text-white" type="submit">
          Giriş
        </button>
      </form>
    </main>
  );
}
