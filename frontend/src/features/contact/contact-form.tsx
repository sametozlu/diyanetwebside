"use client";

import * as React from "react";
import { apiSend } from "@/lib/api";
import { Input } from "@/components/ui/section";

export function ContactForm() {
  const [status, setStatus] = React.useState<"idle" | "sending" | "ok" | "error">("idle");
  const [message, setMessage] = React.useState("");

  async function onSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = event.currentTarget;
    const data = new FormData(form);
    setStatus("sending");
    try {
      await apiSend("/api/contact", "POST", {
        name: data.get("name"),
        email: data.get("email"),
        subject: data.get("subject"),
        message: data.get("message"),
      });
      setStatus("ok");
      setMessage("Mesajınız alındı. Bu form kavramsal portala aittir.");
      form.reset();
    } catch {
      setStatus("error");
      setMessage("Mesaj gönderilemedi. Lütfen yeniden deneyin.");
    }
  }

  return (
    <form onSubmit={onSubmit} className="mt-8 grid max-w-xl gap-4 border border-line bg-white p-5">
      <label className="grid gap-1 text-sm">
        Ad soyad
        <Input name="name" required autoComplete="name" />
      </label>
      <label className="grid gap-1 text-sm">
        E-posta
        <Input name="email" type="email" required autoComplete="email" />
      </label>
      <label className="grid gap-1 text-sm">
        Konu
        <Input name="subject" required />
      </label>
      <label className="grid gap-1 text-sm">
        Mesaj
        <textarea name="message" required rows={5} className="rounded-sm border border-line px-3 py-2 text-sm focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-forest" />
      </label>
      <button
        type="submit"
        disabled={status === "sending"}
        className="h-11 bg-forest text-sm font-medium text-white hover:bg-forest-mid disabled:opacity-60"
      >
        {status === "sending" ? "Gönderiliyor…" : "Gönder"}
      </button>
      {message ? (
        <p className={status === "error" ? "text-sm text-danger" : "text-sm text-forest"} role="status">
          {message}
        </p>
      ) : null}
    </form>
  );
}
