"use client";

import * as Dialog from "@radix-ui/react-dialog";
import { useQuery } from "@tanstack/react-query";
import { Command } from "cmdk";
import { useTranslations } from "next-intl";
import * as React from "react";
import { Link } from "@/i18n/navigation";
import { apiGetSafe } from "@/lib/api";
import type { SearchResponse } from "@/types/api";

const GROUP_KEYS = [
  "news",
  "fatwa",
  "hadith",
  "sermon",
  "publication",
  "event",
  "media",
  "province",
  "service",
  "quran",
  "page",
] as const;

export function SearchPalette({
  open,
  onOpenChange,
}: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}) {
  const t = useTranslations("search");
  const tTypes = useTranslations("types");
  const [q, setQ] = React.useState("");
  const [debounced, setDebounced] = React.useState("");

  React.useEffect(() => {
    const id = window.setTimeout(() => setDebounced(q), 300);
    return () => window.clearTimeout(id);
  }, [q]);

  const { data, isFetching } = useQuery({
    queryKey: ["search", debounced],
    queryFn: () =>
      apiGetSafe<SearchResponse>(`/api/search?q=${encodeURIComponent(debounced)}&limit=20`, {
        query: debounced,
        groups: [],
      }),
    enabled: open && debounced.trim().length >= 2,
  });

  function handleOpenChange(next: boolean) {
    if (!next) setQ("");
    onOpenChange(next);
  }

  function groupLabel(type: string) {
    return GROUP_KEYS.includes(type as (typeof GROUP_KEYS)[number]) ? tTypes(type) : type;
  }

  return (
    <Dialog.Root open={open} onOpenChange={handleOpenChange}>
      <Dialog.Portal>
        <Dialog.Overlay className="fixed inset-0 z-50 bg-forest-deep/40" />
        <Dialog.Content className="fixed left-1/2 top-[12vh] z-50 w-[calc(100%-2rem)] max-w-xl -translate-x-1/2 overflow-hidden border border-line bg-white shadow-[0_12px_40px_rgb(6_36_28_/_0.18)] focus:outline-none">
          <Dialog.Title className="sr-only">{t("placeholder")}</Dialog.Title>
          <Command shouldFilter={false}>
            <Command.Input
              autoFocus
              value={q}
              onValueChange={setQ}
              placeholder={t("placeholder")}
              className="h-14 w-full border-b border-line bg-transparent px-4 text-base outline-none"
            />
            <Command.List className="max-h-[50vh] overflow-auto p-2">
              {q.trim().length < 2 ? (
                <p className="px-2 py-6 text-center text-sm text-muted">{t("empty")}</p>
              ) : isFetching && !data?.groups.length ? (
                <p className="px-2 py-6 text-center text-sm text-muted">{t("searching")}</p>
              ) : !data?.groups.length ? (
                <p className="px-2 py-6 text-center text-sm text-muted">{t("noResults")}</p>
              ) : (
                data.groups.map((group) => (
                  <Command.Group key={group.type} heading={groupLabel(group.type)} className="mb-3">
                    {group.items.map((item) => (
                      <Command.Item key={`${group.type}-${item.slug}`} asChild>
                        <Link
                          href={item.href}
                          onClick={() => onOpenChange(false)}
                          className="block rounded-md px-2 py-2 hover:bg-paper-2"
                        >
                          <span className="block text-sm font-medium">{item.title}</span>
                          {item.summary ? <span className="line-clamp-1 text-xs text-muted">{item.summary}</span> : null}
                        </Link>
                      </Command.Item>
                    ))}
                  </Command.Group>
                ))
              )}
            </Command.List>
          </Command>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  );
}
