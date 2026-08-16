import type { Metadata } from "next";
import { CmsPage } from "@/components/cms-page";
export const metadata: Metadata = { title: "Çerez Politikası" };
export default function Page() {
  return <CmsPage slug="cerez-politikasi" />;
}
