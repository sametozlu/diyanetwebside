# Technical Audit Report

## Executive Summary

The repository is a conceptual institutional public portal: Next.js 16 (App Router) in front of a Spring Boot 4 modular monolith and PostgreSQL. The architecture was kept. This pass connected public prayer, Quran, and optional hadith APIs, replaced placeholder pages with loading/error/empty states, removed fabricated contact details and stock photos, and added a working search page plus contact form.

This is not an official Diyanet website. Remote religious-service data is attributed to Aladhan / alquran.cloud. CMS editorial content is not a live official press feed.

## Existing Architecture

- **Frontend:** Next.js 16, React 19, TypeScript, Tailwind CSS 4, next-intl (`tr` / `en` / `ar`), TanStack Query
- **Backend:** Java 21, Spring Boot 4.0.7, Spring Security, Spring Data JPA, Flyway, JWT, springdoc OpenAPI, Caffeine cache
- **Database:** PostgreSQL 16
- **API:** REST under `/api`, public GET, JWT for `/api/admin/**`
- **Auth:** Email/password login, JWT in `sessionStorage` on the admin UI
- **State:** Server Components for most pages; client state for search, prayer countdown, accessibility prefs, admin forms
- **Styling:** Custom forest / paper / gold tokens in `globals.css`
- **Routing:** `[locale]/(site)` public routes, `[locale]/admin` CMS demo
- **Testing:** Vitest, Playwright, JUnit + `@SpringBootTest`
- **Build:** `next build` (standalone), `mvn package`
- **Deploy:** Docker Compose (`db`, `backend`, `frontend`)

## Problems Found

| Item | Severity | Status |
| --- | --- | --- |
| next-intl middleware at project root did not run with `src/app`, so `/` and unprefixed routes 404ed | Critical | Fixed |
| Skip link targeted `#icerik` but the content wrapper had no id | High | Fixed |
| CMS/news/sermon/publication/event HTML rendered unsanitized | High | Fixed |
| `cache: "no-store"` on prayer fetches was overwritten by `revalidate: 60` | High | Fixed |
| Admin login pre-filled demo password and echoed it on error | High | Fixed |
| JWT secret hardcoded in Compose; default still exists for local demo | Medium | Fixed (Compose uses env); default in `application.yml` accepted for local demo |
| Search overlay lacked dialog semantics, debounce, and Escape/focus trap | Medium | Fixed |
| News `page` query had no pagination UI | Medium | Fixed |
| Fatwa search had no submit control and dropped category when querying | Medium | Fixed |
| Prayer countdown did not tick | Medium | Fixed |
| “Aylık takvim” linked to a daily-times page | Medium | Fixed (label) |
| News cards used CSS backgrounds with no `alt` | Medium | Fixed |
| Homepage JSON-LD URL hardcoded to localhost | Medium | Fixed |
| `robots.ts` allowed indexing while metadata said `noindex` | Medium | Fixed |
| Media seed used an unprofessional placeholder embed | Low | Fixed |
| Fatwa `JOIN FETCH` + pagination | Low | Fixed (`@EntityGraph`) |
| Missing generic API 500 handler (could leak internals) | Medium | Fixed |
| Login rate limit was 60/minute | Low | Fixed (10/minute) |
| Language `<select>` had `outline-none` with no visible focus | Low | Fixed |
| Mobile menu missing `aria-expanded` | Low | Fixed |
| Homepage imported `QUICK_SERVICES` from a Client Component module | Medium | Fixed |
| Unused scaffold packages (recharts, framer-motion, extra Radix, etc.) | Low | Deferred |
| Root `html lang` stays `tr` until client locale helper runs | Low | Accepted |
| No monthly prayer calendar | Low | Accepted |
| Local Postgres on :5432 in this workspace used credentials that did not match `.env.example` | Medium | Accepted (documented; Compose still uses the example values) |

## Bugs Found

1. Unprefixed routes 404 because middleware was not in `src/`.
2. Skip link had no destination.
3. Unsanitized HTML in several detail pages.
4. Prayer times could be served stale because of fetch cache options.
5. Static prayer countdown.
6. Admin password prefill / error text.
7. Search UX/a11y gaps and undebounced requests.
8. News pagination parameter unused in the UI.
9. Fatwa GET form incomplete.
10. Misleading monthly-calendar link.
11. Missing image alt text on news cards.
12. SEO robots conflict.
13. Media embed placeholder URL.
14. Missing API 500 mapping.

## Bugs Fixed

- Moved next-intl middleware to `frontend/src/middleware.ts` and included `/` in the matcher.
- Set `id="icerik"` on the site content wrapper.
- Added `sanitizeHtml` + `HtmlContent` for CMS bodies.
- Honored `cache: "no-store"` in `apiGet`.
- Live countdown via a remounted `PrayerCountdown`.
- Admin login: empty fields, labels, generic error.
- Search: Radix Dialog, 300ms debounce, loading copy.
- News pagination + empty state.
- Fatwa form method GET, submit button, hidden category, empty state.
- News/media images via `next/image` with alt text.
- JSON-LD and canonical site URL from `NEXT_PUBLIC_SITE_URL`.
- `robots.ts` disallows all crawlers.
- Media seeder no longer stores the placeholder video URL; existing demo rows are scrubbed on startup.
- Fatwa search query uses `@EntityGraph` instead of collection-unsafe fetch + page.
- API `Exception` handler returns a generic 500.
- Login rate limit 10 requests / 60s.
- Accessibility menu: expanded state, outside click, Escape.
- Frontend security headers in `next.config.ts`.
- Compose `JWT_SECRET` from environment.

## Architecture Improvements

No rewrite. Small separations only:

- `QUICK_SERVICES` extracted from the header Client Component
- HTML sanitizer isolated in `lib/sanitize-html.ts`
- Playwright added for smoke coverage and live screenshots

The backend remains a modular monolith.

## Security Findings

| Finding | Severity | Status |
| --- | --- | --- |
| Stored HTML XSS via `dangerouslySetInnerHTML` | High | Fixed (stripper; not a full HTML policy engine) |
| Demo JWT default in application config | Medium | Accepted for local demo; Compose now reads env |
| Admin password in login form state | High | Fixed |
| CSRF disabled | Low | Accepted (stateless JWT API) |
| Token in `sessionStorage` | Medium | Accepted for demo admin |
| Login brute-force window | Medium | Improved (10/min) |
| Unhandled exceptions could leak messages | Medium | Fixed |

No production secrets were found in the repository. `.env` remains gitignored. Demo administrator credentials are not documented in README.

## Performance Findings

- Prayer fetch cache conflict could hide updates — fixed
- Search fired on every keystroke — debounced
- News images now go through Next.js image optimization
- Homepage no longer imports a Client Component barrel for static service links
- Unused chart/animation libraries still ship in `package.json` — deferred (no blind uninstall)

## Accessibility Findings

| Finding | Severity | Status |
| --- | --- | --- |
| Broken skip link | High | Fixed |
| Search not a modal | Medium | Fixed |
| Menu `aria-expanded` missing | Medium | Fixed |
| Language control lacked visible focus | Low | Fixed |
| News images without alt | Medium | Fixed |
| Homepage lacked an `h1` | Medium | Fixed (visually hidden) |
| Footer email field unlabeled | Low | Fixed |
| `html lang`/`dir` not SSR for `en`/`ar` | Low | Accepted (client `DocumentLang`) |

## SEO Findings

- Title/description/Open Graph already present
- Canonical via `metadataBase` + `alternates.canonical`
- JSON-LD site URL now uses `NEXT_PUBLIC_SITE_URL`
- `robots.txt` aligned with `noindex` (conceptual demo)
- Sitemap kept as a URL inventory, not advertised to crawlers

## Testing Results

Commands actually executed:

| Command | Result |
| --- | --- |
| `backend`: `mvn test` | PASS — Tests run: 8, Failures: 0 |
| `frontend`: `npm test` (Vitest) | PASS — 7 tests |
| `frontend`: `npm run lint` | PASS (after hook-rule fixes) |
| `frontend`: `npm run typecheck` | PASS |
| `frontend`: `npm run build` | PASS |
| `frontend`: `npx playwright test e2e/smoke.spec.ts` | PASS — 11 tests |
| `frontend`: `npx playwright test e2e/screenshots.spec.ts` | PASS — 1 test |
| `backend`: `mvn -DskipTests package` | PASS |
| Live `GET /`, `/haberler`, `/namaz-vakitleri`, `/api/health` | HTTP 200 |

## Visual QA

Screenshots captured from the running app at 1440×900 (and homepage also 390×844) into `docs/screenshots/`:

- homepage-desktop.png, homepage-mobile.png
- news-desktop.png, news-detail-desktop.png
- prayer-times-desktop.png, quran-desktop.png, hadith-desktop.png
- fatwa-desktop.png, sermons-desktop.png, publications-desktop.png
- media-desktop.png, events-desktop.png, provinces-desktop.png
- search-desktop.png

Observed: demo banner visible, prayer times panel with countdown copy, search palette returning grouped demo hits for “namaz”.

## README Changes

README replaced with a full description of the real stack, run commands, env vars, security notes, and screenshot gallery. Demo passwords are not listed.

## Remaining Technical Debt

- Unused frontend packages from the original scaffold
- Sanitizer is not DOMPurify / a full policy engine
- No official data sources for prayer times or Quran
- No monthly calendar
- Root document `lang` is `tr` until client hydration
- Admin is a thin demo, not a full CMS
- Playwright smoke spec exists but is environment-dependent (needs live servers)

## Recommended Next Steps

1. Point prayer and Quran modules at official feeds
2. Drop unused npm packages after a usage audit
3. Move `html lang`/`dir` to a locale-aware document shell
4. Replace the HTML stripper with a maintained sanitizer if editors can enter arbitrary markup
5. Provision production secrets outside `application.yml`
