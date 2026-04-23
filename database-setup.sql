-- ============================================================
--  FaithLink Core – Database Setup Script (Enterprise Edition)
--  Architecture: UUID-based (BINARY(16)), Multi-tenant
--  Compatibility: MySQL 8.0+, MariaDB 10.4+
-- ============================================================

CREATE DATABASE IF NOT EXISTS faithlink_db 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE faithlink_db;

-- ── Cleanup ──────────────────────────────────────────────────
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS announcements;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS prayer_requests;
DROP TABLE IF EXISTS sermons;
DROP TABLE IF EXISTS user_groups;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS donations;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS churches;
SET FOREIGN_KEY_CHECKS = 1;

-- ── Churches ──────────────────────────────────────────────────
CREATE TABLE churches (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    email VARCHAR(100),
    phone VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ── Roles ─────────────────────────────────────────────────────
CREATE TABLE roles (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- ── Users ─────────────────────────────────────────────────────
CREATE TABLE users (
    id BINARY(16) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    church_id BINARY(16),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_church FOREIGN KEY (church_id) REFERENCES churches(id) ON DELETE SET NULL
);

-- ── User Roles ────────────────────────────────────────────────
CREATE TABLE user_roles (
    user_id BINARY(16) NOT NULL,
    role_id BINARY(16) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_role_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- ── Church Groups ─────────────────────────────────────────────
CREATE TABLE church_groups (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    leader_id BINARY(16),
    church_id BINARY(16) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_group_leader FOREIGN KEY (leader_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_group_church FOREIGN KEY (church_id) REFERENCES churches(id) ON DELETE CASCADE
);

-- ── User Groups (Membership) ──────────────────────────────────
CREATE TABLE user_groups (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    group_id BINARY(16) NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ug_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ug_group FOREIGN KEY (group_id) REFERENCES church_groups(id) ON DELETE CASCADE
);

-- ── Donations ─────────────────────────────────────────────────
CREATE TABLE donations (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    fund_type VARCHAR(50) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100) UNIQUE,
    church_id BINARY(16) NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    donation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_don_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_don_church FOREIGN KEY (church_id) REFERENCES churches(id) ON DELETE CASCADE
);

-- ── Events ────────────────────────────────────────────────────
CREATE TABLE events (
    id BINARY(16) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    location VARCHAR(200) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    church_id BINARY(16) NOT NULL,
    CONSTRAINT fk_evt_church FOREIGN KEY (church_id) REFERENCES churches(id) ON DELETE CASCADE
);

-- ── Prayer Requests ───────────────────────────────────────────
CREATE TABLE prayer_requests (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    church_id BINARY(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prayer_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_prayer_church FOREIGN KEY (church_id) REFERENCES churches(id) ON DELETE CASCADE
);

-- ── Sermons ───────────────────────────────────────────────────
CREATE TABLE sermons (
    id BINARY(16) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    preacher VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    content TEXT,
    video_url VARCHAR(500),
    audio_url VARCHAR(500),
    church_id BINARY(16) NOT NULL,
    CONSTRAINT fk_sermon_church FOREIGN KEY (church_id) REFERENCES churches(id) ON DELETE CASCADE
);

-- ── Announcements ─────────────────────────────────────────────
CREATE TABLE announcements (
    id BINARY(16) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    expires_at DATETIME,
    church_id BINARY(16) NOT NULL,
    CONSTRAINT fk_ann_church FOREIGN KEY (church_id) REFERENCES churches(id) ON DELETE CASCADE
);

SELECT 'Database schema migrated to UUID & Multi-tenancy successfully' AS status;
