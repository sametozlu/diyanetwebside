from pathlib import Path

p = Path(__file__).resolve().parents[1] / "public" / "covers"
p.mkdir(parents=True, exist_ok=True)

covers = [
    ("gundem", "#0c3b2e", "#d4a017", "Gündem", "haber"),
    ("egitim", "#123f4a", "#e8d7a8", "Eğitim", "kitap"),
    ("hac", "#3a2a12", "#e0c36a", "Hac ve Umre", "kaabe"),
    ("kultur", "#3a1f2a", "#e8d7a8", "Kültür", "yildiz"),
    ("ibadet", "#0e3328", "#c9a227", "İbadet", "hilal"),
    ("dunya", "#10243a", "#8ec5c0", "Dünya", "dunya"),
    ("kurum", "#071f18", "#c9a227", "Kurum", "bina"),
    ("kitap", "#2a2418", "#e8d7a8", "Yayın", "kitap"),
    ("dergi", "#1a3a32", "#f4efe6", "Dergi", "dergi"),
    ("cocuk", "#24543f", "#f0d78c", "Çocuk", "gunes"),
    ("medya", "#121a18", "#c9a227", "Medya", "oynat"),
]

motifs = {
    "haber": """
    <rect x="620" y="250" width="360" height="280" rx="8" fill="#f4efe6"/>
    <rect x="650" y="280" width="180" height="12" fill="#0c3b2e"/>
    <rect x="650" y="310" width="300" height="8" fill="#c9a227"/>
    <rect x="650" y="336" width="280" height="8" fill="#c4b89a"/>
    <rect x="650" y="362" width="300" height="8" fill="#c4b89a"/>
    <rect x="650" y="388" width="220" height="8" fill="#c4b89a"/>
    <rect x="650" y="430" width="90" height="70" fill="#c9a227"/>
    <rect x="760" y="430" width="190" height="8" fill="#c4b89a"/>
    <rect x="760" y="454" width="170" height="8" fill="#c4b89a"/>
    """,
    "kitap": """
    <path d="M560 520 L560 280 Q800 330 1040 280 L1040 520 Q800 570 560 520 Z" fill="#f4efe6" stroke="#c9a227" stroke-width="6"/>
    <path d="M800 300 L800 540" stroke="#c9a227" stroke-width="4"/>
    <path d="M620 340 Q800 380 980 340" fill="none" stroke="#0c3b2e" stroke-width="4"/>
    <path d="M620 380 Q800 420 980 380" fill="none" stroke="#0c3b2e" stroke-width="4"/>
    """,
    "kaabe": """
    <rect x="700" y="250" width="200" height="260" fill="#1a1208" stroke="#e0c36a" stroke-width="6"/>
    <rect x="700" y="330" width="200" height="28" fill="#e0c36a"/>
    <rect x="860" y="430" width="22" height="36" fill="#e0c36a"/>
    """,
    "yildiz": """
    <path d="M800 230 L848 380 L1008 380 L878 470 L928 620 L800 530 L672 620 L722 470 L592 380 L752 380 Z" fill="#f4efe6" stroke="#c9a227" stroke-width="4"/>
    """,
    "hilal": """
    <circle cx="800" cy="400" r="150" fill="#f4efe6"/>
    <circle cx="850" cy="370" r="118" fill="#0e3328"/>
    <circle cx="890" cy="300" r="18" fill="#c9a227"/>
    """,
    "dunya": """
    <circle cx="800" cy="400" r="160" fill="#f4efe6" stroke="#8ec5c0" stroke-width="8"/>
    <ellipse cx="800" cy="400" rx="70" ry="160" fill="none" stroke="#10243a" stroke-width="4"/>
    <ellipse cx="800" cy="400" rx="160" ry="55" fill="none" stroke="#10243a" stroke-width="4"/>
    <line x1="640" y1="400" x2="960" y2="400" stroke="#10243a" stroke-width="4"/>
    """,
    "bina": """
    <rect x="620" y="280" width="360" height="260" fill="#f4efe6" stroke="#c9a227" stroke-width="5"/>
    <rect x="760" y="430" width="80" height="110" fill="#071f18"/>
    <rect x="660" y="320" width="50" height="50" fill="#c9a227"/>
    <rect x="740" y="320" width="50" height="50" fill="#c9a227"/>
    <rect x="820" y="320" width="50" height="50" fill="#c9a227"/>
    <rect x="900" y="320" width="50" height="50" fill="#c9a227"/>
    """,
    "dergi": """
    <rect x="670" y="250" width="280" height="340" fill="#f4efe6" transform="rotate(-6 810 420)"/>
    <rect x="690" y="250" width="280" height="340" fill="#fffaf3" stroke="#c9a227" stroke-width="5"/>
    <rect x="730" y="300" width="200" height="18" fill="#1a3a32"/>
    <rect x="730" y="340" width="160" height="10" fill="#c9a227"/>
    <rect x="730" y="370" width="200" height="10" fill="#c4b89a"/>
    <rect x="730" y="400" width="180" height="10" fill="#c4b89a"/>
    """,
    "gunes": """
    <circle cx="800" cy="400" r="90" fill="#f0d78c"/>
    <g stroke="#f0d78c" stroke-width="10" stroke-linecap="round">
      <line x1="800" y1="250" x2="800" y2="290"/>
      <line x1="800" y1="510" x2="800" y2="550"/>
      <line x1="650" y1="400" x2="690" y2="400"/>
      <line x1="910" y1="400" x2="950" y2="400"/>
      <line x1="694" y1="294" x2="722" y2="322"/>
      <line x1="878" y1="478" x2="906" y2="506"/>
      <line x1="694" y1="506" x2="722" y2="478"/>
      <line x1="878" y1="322" x2="906" y2="294"/>
    </g>
    """,
    "oynat": """
    <circle cx="800" cy="400" r="150" fill="#f4efe6"/>
    <polygon points="760,330 760,470 890,400" fill="#121a18"/>
    """,
}

template = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1600 900" role="img" aria-label="{label}">
  <defs>
    <linearGradient id="bg-{name}" x1="0" y1="0" x2="1" y2="1">
      <stop offset="0" stop-color="{bg}"/>
      <stop offset="1" stop-color="#071f18"/>
    </linearGradient>
  </defs>
  <rect width="1600" height="900" fill="url(#bg-{name})"/>
  <rect x="48" y="48" width="1504" height="804" fill="none" stroke="{accent}" stroke-width="4"/>
  <rect x="72" y="72" width="1456" height="756" fill="none" stroke="#f4efe6" stroke-width="1" opacity=".35"/>
  {motif}
  <rect x="480" y="620" width="640" height="110" rx="8" fill="#f4efe6"/>
  <text x="800" y="668" text-anchor="middle" fill="#0c3b2e" font-family="Georgia, serif" font-size="42">{label}</text>
  <text x="800" y="704" text-anchor="middle" fill="{accent}" font-family="Georgia, serif" font-size="16" letter-spacing="6">DİJİTAL KAPI</text>
</svg>
"""

for name, bg, accent, label, motif_key in covers:
    svg = template.format(name=name, bg=bg, accent=accent, label=label, motif=motifs[motif_key])
    (p / f"{name}.svg").write_text(svg, encoding="utf-8")

print("wrote", len(covers), "covers")
