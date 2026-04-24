-- ============================================================
-- FaithLink Core – Database Seeding Script (Default Data)
-- ============================================================

USE faithlink_db;

-- ── 1. Create Default Church ──────────────────────────────────
SET @church_id = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO churches (id, name, address, email, phone) 
VALUES (@church_id, 'Central Community Church', '123 Faith Avenue, Kigali', 'contact@centralchurch.rw', '+250780000001');

-- ── 2. Create Roles ──────────────────────────────────────────
SET @role_admin = UNHEX(REPLACE(UUID(), '-', ''));
SET @role_pastor = UNHEX(REPLACE(UUID(), '-', ''));
SET @role_treasurer = UNHEX(REPLACE(UUID(), '-', ''));
SET @role_member = UNHEX(REPLACE(UUID(), '-', ''));

INSERT INTO roles (id, name, description) VALUES 
(@role_admin, 'ROLE_ADMIN', 'Full system access'),
(@role_pastor, 'ROLE_PASTOR', 'Spiritual leadership and member management'),
(@role_treasurer, 'ROLE_TREASURER', 'Financial management and reporting'),
(@role_member, 'ROLE_MEMBER', 'Standard congregation member');

-- ── 3. Create Default Users (5 Users) ───────────────────────
-- Password is 'password123' BCrypt hashed: $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnC
SET @user1 = UNHEX(REPLACE(UUID(), '-', ''));
SET @user2 = UNHEX(REPLACE(UUID(), '-', ''));
SET @user3 = UNHEX(REPLACE(UUID(), '-', ''));
SET @user4 = UNHEX(REPLACE(UUID(), '-', ''));
SET @user5 = UNHEX(REPLACE(UUID(), '-', ''));

INSERT INTO users (id, first_name, last_name, email, password, phone, church_id) VALUES 
(@user1, 'Simeon', 'Admin', 'admin@faithlink.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnC', '+250781111111', @church_id),
(@user2, 'Jean', 'Pastor', 'pastor@faithlink.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnC', '+250782222222', @church_id),
(@user3, 'Marie', 'Treasurer', 'finance@faithlink.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnC', '+250783333333', @church_id),
(@user4, 'David', 'Member', 'david@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnC', '+250784444444', @church_id),
(@user5, 'Sarah', 'Member', 'sarah@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnC', '+250785555555', @church_id);

-- ── 4. Assign Roles ──────────────────────────────────────────
INSERT INTO user_roles (user_id, role_id) VALUES 
(@user1, @role_admin),
(@user2, @role_pastor),
(@user3, @role_treasurer),
(@user4, @role_member),
(@user5, @role_member);

-- ── 5. Create Events (5 Events) ──────────────────────────────
INSERT INTO events (id, title, description, location, start_time, end_time, church_id) VALUES 
(UNHEX(REPLACE(UUID(), '-', '')), 'Sunday Morning Worship', 'Weekly spiritual gathering', 'Main Hall', '2026-04-26 09:00:00', '2026-04-26 12:00:00', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Mid-Week Bible Study', 'Deep dive into Scripture', 'Chapel', '2026-04-29 18:00:00', '2026-04-29 20:00:00', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Youth Fellowship Night', 'Fun and faith for teenagers', 'Youth Center', '2026-05-01 17:00:00', '2026-05-01 19:30:00', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Community Food Drive', 'Helping those in need', 'Church Parking Lot', '2026-05-02 08:00:00', '2026-05-02 14:00:00', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Prayer and Fasting Summit', 'Dedicated time for intercession', 'Sanctuary', '2026-05-03 14:00:00', '2026-05-03 17:00:00', @church_id);

-- ── 6. Create Church Groups (5 Groups) ───────────────────────
INSERT INTO church_groups (id, name, description, leader_id, church_id) VALUES 
(UNHEX(REPLACE(UUID(), '-', '')), 'Worship Team', 'Choir and instrumentalists', @user2, @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Men of Valor', 'Mens fellowship group', @user4, @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Sisters in Christ', 'Womens fellowship group', @user5, @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Sunday School Teachers', 'Educators for our children', @user1, @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Hospitality Committee', 'Welcoming newcomers', @user3, @church_id);

-- ── 7. Create Sermons (5 Sermons) ───────────────────────────
INSERT INTO sermons (id, title, preacher_name, sermon_date, content, church_id) VALUES 
(UNHEX(REPLACE(UUID(), '-', '')), 'The Walk of Faith', 'Pastor Jean', '2026-04-19 09:00:00', 'A sermon about trusting God in difficult times.', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Living by Grace', 'Evangelist Paul', '2026-04-12 10:30:00', 'Exploring the unmerited favor of God.', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'The Power of Prayer', 'Pastor Jean', '2026-04-05 11:00:00', 'How prayer changes things and us.', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Fruit of the Spirit', 'Sister Marie', '2026-03-29 09:15:00', 'Developing character through the Holy Spirit.', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'The Great Commission', 'Pastor Jean', '2026-03-22 10:00:00', 'Our call to reach the world for Christ.', @church_id);

-- ── 8. Create Donations (5 Donations) ────────────────────────
INSERT INTO donations (id, user_id, amount, fund_type, payment_method, church_id, is_verified) VALUES 
(UNHEX(REPLACE(UUID(), '-', '')), @user4, 5000.00, 'TITHE', 'MOBILE_MONEY', @church_id, TRUE),
(UNHEX(REPLACE(UUID(), '-', '')), @user5, 2000.00, 'OFFERING', 'CASH', @church_id, TRUE),
(UNHEX(REPLACE(UUID(), '-', '')), @user4, 15000.00, 'BUILDING_FUND', 'BANK_TRANSFER', @church_id, FALSE),
(UNHEX(REPLACE(UUID(), '-', '')), @user5, 1000.00, 'TITHE', 'MOBILE_MONEY', @church_id, TRUE),
(UNHEX(REPLACE(UUID(), '-', '')), @user4, 3000.00, 'MISSIONS', 'CASH', @church_id, TRUE);

-- ── 9. Create Prayer Requests (5 Requests) ──────────────────
INSERT INTO prayer_requests (id, user_id, subject, content, status, church_id) VALUES 
(UNHEX(REPLACE(UUID(), '-', '')), @user4, 'Family Healing', 'My mother is undergoing surgery tomorrow.', 'PENDING', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), @user5, 'Job Opportunity', 'I have a final interview on Friday.', 'ACTIVE', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), @user4, 'Financial Breakthrough', 'Trusting God for provision this month.', 'PENDING', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), @user5, 'Travel Mercies', 'Traveling upcountry for a week.', 'ACTIVE', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), @user4, 'Spiritual Growth', 'Seeking a deeper relationship with God.', 'ACTIVE', @church_id);

-- ── 10. Create Announcements (5 Announcements) ──────────────
INSERT INTO announcements (id, title, content, expires_at, church_id) VALUES 
(UNHEX(REPLACE(UUID(), '-', '')), 'New Service Times', 'Starting next month, our first service will begin at 8:00 AM.', '2026-05-31 00:00:00', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Youth Camp Registration', 'Sign up at the info desk for our summer camp!', '2026-06-15 00:00:00', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Marriage Seminar', 'Join us for a two-day workshop on healthy marriages.', '2026-05-10 00:00:00', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Baptismal Service', 'Candidates for baptism should meet the pastor this Saturday.', '2026-05-03 00:00:00', @church_id),
(UNHEX(REPLACE(UUID(), '-', '')), 'Building Project Update', 'We have raised 60% of our goal! Thank you for your generosity.', '2026-05-20 00:00:00', @church_id);

SELECT 'Seed data inserted successfully' AS status;
