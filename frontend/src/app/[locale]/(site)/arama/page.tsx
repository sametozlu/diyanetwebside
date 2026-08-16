import { Suspense } from "react";
import { ListSkeleton } from "@/components/ui/states";
import { pagesMeta } from "@/lib/page-meta";
import { SearchClient } from "./search-client";

export const generateMetadata = () => pagesMeta("searchTitle");

export default function SearchPage() {
  return (
    <Suspense
      fallback={
        <main className="mx-auto max-w-4xl px-4 py-10">
          <ListSkeleton />
        </main>
      }
    >
      <SearchClient />
    </Suspense>
  );
}
