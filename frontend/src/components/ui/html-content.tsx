import { sanitizeHtml } from "@/lib/sanitize-html";

export function HtmlContent({
  html,
  className,
}: {
  html?: string | null;
  className?: string;
}) {
  return <div className={className} dangerouslySetInnerHTML={{ __html: sanitizeHtml(html) }} />;
}
