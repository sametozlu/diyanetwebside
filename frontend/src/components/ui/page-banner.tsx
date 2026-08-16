import { CoverImage } from "@/components/ui/cover-image";

export function PageBanner({
  src,
  alt,
  priority = false,
}: {
  src: string;
  alt: string;
  priority?: boolean;
}) {
  return (
    <div className="mb-8 overflow-hidden">
      <CoverImage
        src={src}
        alt={alt}
        ratio="banner"
        priority={priority}
        sizes="(max-width: 768px) 100vw, 1152px"
      />
    </div>
  );
}
