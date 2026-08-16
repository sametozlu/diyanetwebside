export function Mark({ className = "h-9 w-9" }: { className?: string }) {
  return (
    <svg viewBox="0 0 48 48" className={className} aria-hidden>
      <rect width="48" height="48" rx="2" fill="#0B3D2E" />
      <path
        d="M10 32V16.5c0-1.2.9-2.2 2.1-2.4L24 12l11.9 2.1c1.2.2 2.1 1.2 2.1 2.4V32"
        fill="none"
        stroke="#F4F5F3"
        strokeWidth="2.2"
        strokeLinecap="round"
      />
      <path d="M16 22h16M16 27h10" stroke="#E7EFE9" strokeWidth="2" strokeLinecap="round" />
    </svg>
  );
}
