-- ===========================================
-- V1__create_users.sql
-- Creates the first table for Users to be sent here
-- Default User is with the role USER
-- ===========================================

CREATE TABLE IF NOT EXISTS users (
                       id            UUID PRIMARY KEY,
                       username      VARCHAR(80)  NOT NULL UNIQUE,
                       email         VARCHAR(160) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
                       role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
                       created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                       updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

