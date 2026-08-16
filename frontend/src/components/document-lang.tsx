"use client";

import { useLocale } from "next-intl";
import * as React from "react";

export function DocumentLang() {
  const locale = useLocale();
  React.useEffect(() => {
    document.documentElement.lang = locale;
    document.documentElement.dir = locale === "ar" ? "rtl" : "ltr";
  }, [locale]);
  return null;
}
