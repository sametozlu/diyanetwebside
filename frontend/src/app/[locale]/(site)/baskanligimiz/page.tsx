import type { Metadata } from "next";
import { CmsPage } from "@/components/cms-page";
export const metadata: Metadata = { title: "Başkanlığımız" };
export default function Page() {
  return <CmsPage slug="baskanligimiz" />;
}
