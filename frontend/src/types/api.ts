export type Category = { id: number; name: string; slug: string };

export type Paged<T> = {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
};

export type NewsSummary = {
  id: number;
  title: string;
  slug: string;
  summary: string | null;
  imageUrl: string | null;
  category: Category | null;
  publishedAt: string | null;
  featured: boolean;
  readCount: number;
  locale: string;
  status: string;
};

export type NewsDetail = NewsSummary & { body: string | null; related?: NewsSummary[] };

export type PrayerTimes = {
  province: string;
  district?: string | null;
  cityLabel?: string | null;
  date: string;
  hijriDate: string;
  hijriDay?: number | null;
  hijriMonth?: number | null;
  hijriYear?: number | null;
  times: {
    imsak: string;
    gunes: string;
    ogle: string;
    ikindi: string;
    aksam: string;
    yatsi: string;
  };
  nextPrayer: { name: string; time: string; remainingSeconds: number };
  currentPrayer?: string | null;
  source?: string;
  disclaimer: string;
};

export type PrayerCalendar = {
  province: string;
  district?: string | null;
  cityLabel?: string | null;
  year: number;
  month: number;
  days: { date: string; hijriDate: string; hijriDay?: number | null; hijriMonth?: number | null; hijriYear?: number | null; times: PrayerTimes["times"] }[];
  source: string;
  disclaimer: string;
};

export type SurahSummary = {
  number: number;
  nameAr: string;
  nameTr: string;
  nameEn: string;
  ayahCount: number;
  revelationType: string;
  juzStart: number | null;
};

export type Ayah = {
  number: number;
  textAr: string;
  textTr: string;
  juz: number | null;
  page: number | null;
};

export type SurahDetail = SurahSummary & {
  ayahs: Ayah[];
  translationNote: string;
  source?: string;
};

export type Hadith = {
  id: number;
  slug: string;
  title: string;
  textAr: string | null;
  textTr: string;
  source: string | null;
  narrator: string | null;
  category: string | null;
  publishedAt: string | null;
};

export type Fatwa = {
  id: number;
  slug: string;
  question: string;
  answer: string;
  category: string | null;
  publishedAt: string | null;
  related?: Fatwa[];
};

export type Sermon = {
  id: number;
  slug: string;
  title: string;
  summary: string | null;
  body: string | null;
  preacher: string | null;
  sermonDate: string | null;
  pdfUrl: string | null;
  audioUrl: string | null;
  category: string | null;
};

export type Publication = {
  id: number;
  slug: string;
  title: string;
  summary: string | null;
  body: string | null;
  author: string | null;
  publishedAt: string | null;
  coverUrl: string | null;
  fileUrl: string | null;
  type: string;
  category: string | null;
};

export type EventItem = {
  id: number;
  slug: string;
  title: string;
  summary: string | null;
  body: string | null;
  startsAt: string | null;
  endsAt: string | null;
  location: string | null;
  province: string | null;
  category: string | null;
};

export type MediaItem = {
  id: number;
  slug: string;
  title: string;
  summary: string | null;
  type: string;
  videoUrl: string | null;
  thumbnailUrl: string | null;
  durationSeconds: number | null;
  publishedAt: string | null;
  category: string | null;
};

export type ServiceItem = {
  id: number;
  slug: string;
  title: string;
  summary: string | null;
  href: string | null;
  icon: string | null;
  category: string | null;
  sortOrder: number;
};

export type ProvinceSummary = {
  id: number;
  name: string;
  slug: string;
  plateCode: number;
  lat: number | null;
  lng: number | null;
};

export type ProvinceDetail = ProvinceSummary & {
  address: string | null;
  phone: string | null;
  email: string | null;
  website: string | null;
  about: string | null;
  districts: { name: string; slug: string }[];
  latestNews: NewsSummary[];
  prayerTimes: PrayerTimes;
};

export type SearchResponse = {
  query: string;
  type?: string | null;
  total?: number;
  groups: { type: string; items: { slug: string; title: string; summary: string; href: string }[] }[];
};

export type PageDto = { slug: string; title: string; body: string | null; locale: string };

export type ReligiousDay = {
  title: string;
  gregorianDate: string;
  hijriDate: string;
  type: string;
  note: string;
};

export const emptyPage = <T,>(): Paged<T> => ({
  content: [],
  page: 0,
  size: 12,
  totalElements: 0,
  totalPages: 0,
});
