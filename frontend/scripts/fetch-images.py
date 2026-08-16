"""Download CC-licensed Wikimedia Commons photos and convert to WebP."""
from __future__ import annotations

import io
import ssl
import time
from pathlib import Path
from urllib.request import Request, urlopen

from PIL import Image

ROOT = Path(__file__).resolve().parents[1] / "public" / "images"
UA = "DijitalKapi/1.0 (conceptual educational portal; image attribution in docs/image-sources.md)"
CTX = ssl._create_unverified_context()

# 1280px is a Wikimedia standard thumbnail step (https://w.wiki/GHai).
ASSETS = [
    {
        "path": "hero/mosque-courtyard.webp",
        "url": "https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Innenhof_Blaue_Moschee_Istanbul.jpg/1280px-Innenhof_Blaue_Moschee_Istanbul.jpg",
        "max": (1280, 960),
    },
    {
        "path": "news/mosque-exterior.webp",
        "url": "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/Exterior_of_Sultan_Ahmed_I_Mosque.jpg/1280px-Exterior_of_Sultan_Ahmed_I_Mosque.jpg",
        "max": (1280, 960),
    },
    {
        "path": "news/mosque-interior.webp",
        "url": "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Suleymaniye_Mosque.jpg/1280px-Suleymaniye_Mosque.jpg",
        "max": (1280, 960),
    },
    {
        "path": "news/mosque-dome.webp",
        "url": "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Sultan_Ahmed_Mosque_interior_-_Istanbul%2C_Turkey_-_panoramio.jpg/1280px-Sultan_Ahmed_Mosque_interior_-_Istanbul%2C_Turkey_-_panoramio.jpg",
        "max": (1280, 1280),
    },
    {
        "path": "news/masjid-al-haram.webp",
        "url": "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Great_Mosque_of_Mecca.jpg/1280px-Great_Mosque_of_Mecca.jpg",
        "max": (1280, 960),
    },
    {
        "path": "quran/mamluk-manuscript.webp",
        "url": "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bd/Mamluk_era_Quran%2C_circa_1380%2C_open_to_sura_16.jpg/1280px-Mamluk_era_Quran%2C_circa_1380%2C_open_to_sura_16.jpg",
        "max": (1280, 960),
    },
    {
        "path": "publications/ottoman-quran-leaf.webp",
        "url": "https://upload.wikimedia.org/wikipedia/commons/f/f4/Quran.jpg",
        "max": (914, 1469),
    },
]


def fetch(url: str) -> bytes:
    req = Request(url, headers={"User-Agent": UA, "Accept": "image/jpeg,image/*,*/*"})
    with urlopen(req, timeout=90, context=CTX) as response:
        data = response.read()
        ctype = response.headers.get("Content-Type", "")
        if "html" in ctype.lower() or data[:15].lstrip().startswith(b"<"):
            raise RuntimeError(f"Not an image from {url}: {ctype} {data[:120]!r}")
        return data


def to_webp(data: bytes, dest: Path, max_size: tuple[int, int]) -> None:
    dest.parent.mkdir(parents=True, exist_ok=True)
    image = Image.open(io.BytesIO(data))
    image = image.convert("RGB")
    image.thumbnail(max_size, Image.Resampling.LANCZOS)
    image.save(dest, "WEBP", quality=78, method=6)
    print(f"{dest.relative_to(ROOT.parent)} {image.size} {dest.stat().st_size // 1024}KB")


def main() -> None:
    for i, asset in enumerate(ASSETS):
        dest = ROOT / asset["path"]
        print("downloading", asset["path"])
        to_webp(fetch(asset["url"]), dest, asset["max"])
        if i < len(ASSETS) - 1:
            time.sleep(4)


if __name__ == "__main__":
    main()
