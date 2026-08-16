CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE news_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(160) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE news (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    slug VARCHAR(220) NOT NULL UNIQUE,
    summary TEXT,
    body TEXT,
    image_url VARCHAR(1000),
    category_id BIGINT REFERENCES news_categories (id),
    published_at TIMESTAMPTZ,
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    read_count INTEGER NOT NULL DEFAULT 0,
    locale VARCHAR(5) NOT NULL DEFAULT 'tr',
    status VARCHAR(32) NOT NULL DEFAULT 'PUBLISHED',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE provinces (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(160) NOT NULL UNIQUE,
    plate_code INTEGER NOT NULL UNIQUE,
    lat DOUBLE PRECISION,
    lng DOUBLE PRECISION,
    address VARCHAR(500),
    phone VARCHAR(64),
    email VARCHAR(255),
    website VARCHAR(255),
    about TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE districts (
    id BIGSERIAL PRIMARY KEY,
    province_id BIGINT NOT NULL REFERENCES provinces (id) ON DELETE CASCADE,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(160) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (province_id, slug)
);

CREATE TABLE prayer_times (
    id BIGSERIAL PRIMARY KEY,
    province_id BIGINT NOT NULL REFERENCES provinces (id) ON DELETE CASCADE,
    date DATE NOT NULL,
    imsak TIME NOT NULL,
    gunes TIME NOT NULL,
    ogle TIME NOT NULL,
    ikindi TIME NOT NULL,
    aksam TIME NOT NULL,
    yatsi TIME NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (province_id, date)
);

CREATE TABLE quran_surahs (
    id BIGSERIAL PRIMARY KEY,
    number INTEGER NOT NULL UNIQUE,
    name_ar VARCHAR(120) NOT NULL,
    name_tr VARCHAR(120) NOT NULL,
    name_en VARCHAR(120) NOT NULL,
    ayah_count INTEGER NOT NULL,
    revelation_type VARCHAR(16) NOT NULL,
    juz_start INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE quran_ayahs (
    id BIGSERIAL PRIMARY KEY,
    surah_id BIGINT NOT NULL REFERENCES quran_surahs (id) ON DELETE CASCADE,
    number INTEGER NOT NULL,
    text_ar TEXT NOT NULL,
    text_tr TEXT NOT NULL,
    juz INTEGER,
    page INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (surah_id, number)
);

CREATE TABLE hadith_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(160) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE hadiths (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(220) NOT NULL UNIQUE,
    title VARCHAR(500) NOT NULL,
    text_ar TEXT,
    text_tr TEXT NOT NULL,
    source VARCHAR(255),
    narrator VARCHAR(255),
    category_id BIGINT REFERENCES hadith_categories (id),
    published_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE fatwa_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(160) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE fatwas (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(220) NOT NULL UNIQUE,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    category_id BIGINT REFERENCES fatwa_categories (id),
    published_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE sermon_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(160) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE sermons (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(220) NOT NULL UNIQUE,
    title VARCHAR(500) NOT NULL,
    summary TEXT,
    body TEXT,
    preacher VARCHAR(255),
    sermon_date DATE,
    pdf_url VARCHAR(1000),
    audio_url VARCHAR(1000),
    category_id BIGINT REFERENCES sermon_categories (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE publication_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(160) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE publications (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(220) NOT NULL UNIQUE,
    title VARCHAR(500) NOT NULL,
    summary TEXT,
    body TEXT,
    author VARCHAR(255),
    published_at TIMESTAMPTZ,
    cover_url VARCHAR(1000),
    file_url VARCHAR(1000),
    type VARCHAR(32) NOT NULL,
    category_id BIGINT REFERENCES publication_categories (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE event_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(160) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(220) NOT NULL UNIQUE,
    title VARCHAR(500) NOT NULL,
    summary TEXT,
    body TEXT,
    starts_at TIMESTAMPTZ,
    ends_at TIMESTAMPTZ,
    location VARCHAR(500),
    province_id BIGINT REFERENCES provinces (id),
    category_id BIGINT REFERENCES event_categories (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE media_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(160) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE media (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(220) NOT NULL UNIQUE,
    title VARCHAR(500) NOT NULL,
    summary TEXT,
    type VARCHAR(32) NOT NULL,
    video_url VARCHAR(1000),
    thumbnail_url VARCHAR(1000),
    duration_seconds INTEGER,
    published_at TIMESTAMPTZ,
    category_id BIGINT REFERENCES media_categories (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(220) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    summary TEXT,
    href VARCHAR(500),
    icon VARCHAR(120),
    category VARCHAR(120),
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE organizations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(220) NOT NULL UNIQUE,
    summary TEXT,
    body TEXT,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE pages (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(220) NOT NULL UNIQUE,
    title VARCHAR(500) NOT NULL,
    body TEXT,
    locale VARCHAR(5) NOT NULL DEFAULT 'tr',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE menu_items (
    id BIGSERIAL PRIMARY KEY,
    label VARCHAR(255) NOT NULL,
    href VARCHAR(500),
    sort_order INTEGER NOT NULL DEFAULT 0,
    parent_id BIGINT REFERENCES menu_items (id),
    locale VARCHAR(5) NOT NULL DEFAULT 'tr',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE search_index (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(64) NOT NULL,
    entity_id BIGINT NOT NULL,
    slug VARCHAR(220),
    title VARCHAR(500) NOT NULL,
    summary TEXT,
    body TEXT,
    locale VARCHAR(5) NOT NULL DEFAULT 'tr',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (entity_type, entity_id)
);
