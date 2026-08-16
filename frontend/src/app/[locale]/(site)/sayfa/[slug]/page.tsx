import { CmsPage } from "@/components/cms-page";

export default async function GenericPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = await params;
  return <CmsPage slug={slug} />;
}
