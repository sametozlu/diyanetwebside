import type { Metadata } from "next";
import { CmsPage } from "@/components/cms-page";
import { ContactForm } from "@/features/contact/contact-form";

export const metadata: Metadata = { title: "İletişim" };

export default function Page() {
  return (
    <div className="mx-auto max-w-3xl px-4 pb-16">
      <CmsPage slug="iletisim" />
      <ContactForm />
    </div>
  );
}
