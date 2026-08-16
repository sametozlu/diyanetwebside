import {
  BookOpen,
  Building2,
  CalendarDays,
  Clock,
  FileText,
  Landmark,
  MapPin,
  MessageCircleQuestion,
  Scroll,
} from "lucide-react";

export const QUICK_SERVICES = [
  { href: "/namaz-vakitleri", key: "prayerTimes", icon: Clock },
  { href: "/kuran", key: "quran", icon: BookOpen },
  { href: "/hadis", key: "hadith", icon: Scroll },
  { href: "/fetva", key: "fatwa", icon: MessageCircleQuestion },
  { href: "/hutbeler", key: "sermons", icon: FileText },
  { href: "/hac-umre", key: "hajj", icon: Landmark },
  { href: "/dini-gunler", key: "religiousDays", icon: CalendarDays },
  { href: "/cami-bul", key: "findMosque", icon: MapPin },
  { href: "/il-muftulukleri", key: "provinces", icon: Building2 },
] as const;
