-- Drop legacy tables from previous structure
DROP TABLE IF EXISTS faqs, footer_ctas, hero, social_links, specializations, statistics, support_options, team, testimonial CASCADE;

CREATE TABLE IF NOT EXISTS pages (
    id VARCHAR(64) PRIMARY KEY,
    slug VARCHAR(255) NOT NULL UNIQUE,
    version INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sections (
    id VARCHAR(64) PRIMARY KEY,
    page_id VARCHAR(64) NOT NULL REFERENCES pages(id) ON DELETE CASCADE,
    display_order INT NOT NULL,
    component JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_sections_page_id_order ON sections(page_id, display_order ASC);
