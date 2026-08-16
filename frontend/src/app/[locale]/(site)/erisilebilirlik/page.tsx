import type { Metadata } from "next";
import { AccessibilityMenu } from "@/components/a11y/accessibility-menu";
import { CmsPage } from "@/components/cms-page";

export const metadata: Metadata = { title: "Erişilebilirlik" };

export default function Page() {
  return (
    <div>
      <div className="mx-auto max-w-3xl px-4 pt-10">
        <AccessibilityMenu />
      </div>
      <CmsPage slug="erisilebilirlik" />
    </div>
  );
}
