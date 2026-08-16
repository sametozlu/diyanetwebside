export function apiUrl(path: string) {
  const normalized = path.startsWith("/") ? path : `/${path}`;
  if (typeof window === "undefined") {
    const base = process.env.API_INTERNAL_URL || "http://localhost:8080";
    return `${base}${normalized}`;
  }
  return normalized;
}

export type ApiResult<T> = { ok: true; data: T } | { ok: false; error: string };

export async function apiGet<T>(path: string, init?: RequestInit): Promise<T> {
  const { cache, next, headers, ...rest } = init ?? {};
  const controller = new AbortController();
  const timeout = setTimeout(() => controller.abort(), 12000);
  try {
    const res = await fetch(apiUrl(path), {
      ...rest,
      signal: rest.signal ?? controller.signal,
      headers: { Accept: "application/json", ...(headers ?? {}) },
      ...(cache === "no-store"
        ? { cache: "no-store" as const }
        : { next: next ?? { revalidate: 60 } }),
    });
    if (!res.ok) {
      throw new Error(`API ${res.status}: ${path}`);
    }
    return res.json() as Promise<T>;
  } finally {
    clearTimeout(timeout);
  }
}

export async function apiTry<T>(path: string, init?: RequestInit): Promise<ApiResult<T>> {
  try {
    return { ok: true, data: await apiGet<T>(path, init) };
  } catch {
    return { ok: false, error: "Veriler şu anda alınamıyor." };
  }
}

export async function apiGetSafe<T>(path: string, fallback: T, init?: RequestInit): Promise<T> {
  const result = await apiTry<T>(path, init);
  return result.ok ? result.data : fallback;
}

export async function apiSend<T>(
  path: string,
  method: string,
  body?: unknown,
  token?: string,
): Promise<T> {
  const res = await fetch(apiUrl(path), {
    method,
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: body === undefined ? undefined : JSON.stringify(body),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `API ${res.status}`);
  }
  if (res.status === 204) {
    return undefined as T;
  }
  return res.json() as Promise<T>;
}
