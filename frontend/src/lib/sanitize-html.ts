const DANGEROUS_TAGS =
  /<\/?(?:script|style|iframe|object|embed|link|meta|form|input|textarea|button|svg|math)[^>]*>/gi;

export function sanitizeHtml(input?: string | null): string {
  if (!input) return "";
  return input
    .replace(DANGEROUS_TAGS, "")
    .replace(/\son\w+\s*=\s*("[^"]*"|'[^']*'|[^\s>]+)/gi, "")
    .replace(/(href|src)\s*=\s*(['"]?)\s*(javascript|data|vbscript)\s*:/gi, "$1=$2")
    .replace(/javascript\s*:/gi, "")
    .replace(/data\s*:/gi, "");
}
