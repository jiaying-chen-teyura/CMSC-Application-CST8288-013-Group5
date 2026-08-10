/*
 * CMSC_test_data.sql
 * ------------------------------------------------------------
 * Sample/demo data for development and demos.
 *
 * HOW TO RUN:
 *   1. Run CMSC_database.sql first (it DROPs and recreates the "cmsc"
 *      database from scratch, so it must run first, on an empty server).
 *   2. Then run this file against the same "cmsc" database.
 *
 * This relies on AUTO_INCREMENT ids starting at 1 in insertion order,
 * so it must be run against a freshly-created schema (never on top of
 * existing data) or the hardcoded FK references below will point at the
 * wrong rows.
 *
 * All seeded users share the password:  Passw0rd1!
 * (SHA-256 hash below, matching businesslayer.PasswordUtil.hash())
 * ------------------------------------------------------------
 *  Author: Tianzhu Li
 */

USE cmsc;

/*
   1. USERS
   user_id 1 = Shop-Tech, 2 = Trainer, 3-5 = Members
*/
INSERT INTO users (name, email, password_hash, user_type, account_status, created_at, last_login_at) VALUES
('Alex Rivera',   'alex.rivera@cmsc.local',  '9892c5339d13a94cc03384e609798f4e4688dacf95bb896fe60931255f054e20', 'SHOP_TECH', 'ACTIVE', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 90 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 HOUR)),
('Morgan Lee',    'morgan.lee@cmsc.local',   '9892c5339d13a94cc03384e609798f4e4688dacf95bb896fe60931255f054e20', 'TRAINER',   'ACTIVE', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 85 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY)),
('Sam Patel',     'sam.patel@cmsc.local',    '9892c5339d13a94cc03384e609798f4e4688dacf95bb896fe60931255f054e20', 'USER',      'ACTIVE', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 60 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY)),
('Jordan Kim',    'jordan.kim@cmsc.local',   '9892c5339d13a94cc03384e609798f4e4688dacf95bb896fe60931255f054e20', 'USER',      'ACTIVE', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 40 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 MINUTE)),
('Taylor Brooks', 'taylor.brooks@cmsc.local','9892c5339d13a94cc03384e609798f4e4688dacf95bb896fe60931255f054e20', 'USER',      'ACTIVE', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), NULL);

/*
   2. EQUIPMENT (FR-02)
   Two 3D printers (so members have a real choice to book), one laser
   cutter, one CNC - one of each category's default component set (FR-05)
   gets seeded below to mirror what EquipmentFactory would generate.
*/
INSERT INTO equipment (asset_tag, make, model, category, equipment_name, status, access_credit_rate, total_usage_hours, location, registered_by, registered_at, active) VALUES
('3DP-001',   'Prusa',     'MK4',     'THREE_D_PRINTER', 'Prusa MK4 #1',        'AVAILABLE', 2.50, 156.00, 'Maker Lab - Bench 1',     1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 88 DAY), TRUE),
('3DP-002',   'Bambu Lab', 'X1 Carbon','THREE_D_PRINTER','Bambu X1C #1',        'AVAILABLE', 3.00, 48.00,  'Maker Lab - Bench 2',     1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 70 DAY), TRUE),
('LASER-001', 'Glowforge', 'Pro',     'LASER_CUTTER',     'Glowforge Pro',       'IN_USE',    4.50, 210.00, 'Maker Lab - Laser Room',  1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 80 DAY), TRUE),
('CNC-001',   'Carbide 3D','Shapeoko 4','CNC',            'Shapeoko 4',          'AVAILABLE', 5.00, 340.00, 'Maker Lab - CNC Bay',     1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 75 DAY), TRUE),
('3DP-000',   'Creality',  'Ender 3', 'THREE_D_PRINTER',  'Ender 3 (retired)',   'UNAVAILABLE', 1.50, 512.00, 'Storage',               1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 89 DAY), FALSE);

/*
   3. EQUIPMENT KEY COMPONENTS (FR-05)
   Mirrors businesslayer.domain.*Profile default components/thresholds.
   3DP-001's Nozzle is deliberately over its threshold to demo an active
   predictive-maintenance alert; CNC-001's Cutting Bit is deliberately
   close to its threshold (but not yet crossed) to show "healthy but
   watch this one" without a stray alert.
*/
INSERT INTO equipment_components (asset_tag, component_name, usage_hours, maintenance_threshold_hours, component_status, last_maintained_at) VALUES
('3DP-001', 'Nozzle',      156.00, 150.00, 'MAINTENANCE_REQUIRED', NULL),
('3DP-001', 'Belt',         90.00, 400.00, 'HEALTHY', NULL),
('3DP-001', 'Build Plate', 156.00, 800.00, 'HEALTHY', NULL),
('3DP-002', 'Nozzle',       40.00, 150.00, 'HEALTHY', NULL),
('3DP-002', 'Belt',         48.00, 400.00, 'HEALTHY', NULL),
('3DP-002', 'Build Plate',  48.00, 800.00, 'HEALTHY', NULL),
('LASER-001', 'Laser Tube', 210.00, 500.00, 'HEALTHY', NULL),
('LASER-001', 'Lens',       210.00, 300.00, 'HEALTHY', NULL),
('LASER-001', 'Belt/Rail',  210.00, 600.00, 'HEALTHY', NULL),
('CNC-001', 'Spindle',      340.00, 600.00,  'HEALTHY', NULL),
('CNC-001', 'Cutting Bit',   95.00, 100.00,  'HEALTHY', NULL),
('CNC-001', 'Ball Screw',   340.00, 1000.00, 'HEALTHY', NULL);

/*
   4. MAINTENANCE TASK - the open ALERTED task matching 3DP-001's Nozzle
   above (component_id = 1, since it's the first component row inserted).
*/
INSERT INTO maintenance_tasks (asset_tag, component_id, assigned_shop_tech_id, maintenance_type, description, priority, scheduled_start, started_at, completed_at, maintenance_hours, status, credit_earned) VALUES
('3DP-001', 1, NULL, 'PREVENTIVE', 'Nozzle reached its predictive-maintenance alert threshold (156.0 / 150.0 hrs).', 'MEDIUM', NULL, NULL, NULL, NULL, 'ALERTED', 0.00);

-- Historical completed maintenance task, for ledger/report variety.
INSERT INTO maintenance_tasks (asset_tag, component_id, assigned_shop_tech_id, maintenance_type, description, priority, scheduled_start, started_at, completed_at, maintenance_hours, status, credit_earned) VALUES
('3DP-002', 4, 1, 'PREVENTIVE', 'Routine nozzle swap and bed leveling.', 'LOW',
 DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY) + INTERVAL 1 HOUR,
 1.00, 'COMPLETED', 15.00);

/*
   5. CONSUMABLES & EQUIPMENT-CONSUMABLE BUNDLES (FR-02/FR-04)
   Cutting Fluid is seeded below its restock level on purpose, to
   demonstrate the LOW_STOCK / v_consumable_inventory_report alert path.
*/
INSERT INTO consumables (material_name, unit, current_stock, restock_level, unit_debit_rate, active) VALUES
('PLA Filament',            'GRAM',       8500.00, 2000.00, 0.02, TRUE),
('ABS Filament',            'GRAM',       4200.00, 1500.00, 0.025, TRUE),
('Baltic Birch Plywood 3mm','SHEET',        42.00,   10.00, 6.50, TRUE),
('Acrylic Sheet 5mm',       'SHEET',        18.00,    8.00, 9.00, TRUE),
('Aluminum Stock 6061',     'PIECE',        25.00,   10.00, 12.00, TRUE),
('Cutting Fluid',           'MILLILITRE',  1800.00, 2000.00, 0.01, TRUE);

INSERT INTO equipment_consumables (asset_tag, consumable_id) VALUES
('3DP-001', 1), ('3DP-001', 2),
('3DP-002', 1),
('LASER-001', 3), ('LASER-001', 4),
('CNC-001', 5), ('CNC-001', 6);

-- Initial stock-in for every consumable, for the inventory ledger.
INSERT INTO inventory_transactions (consumable_id, transaction_type, quantity_change, performed_by, transaction_time, notes) VALUES
(1, 'RESTOCK', 8500.00, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY), 'Initial stock'),
(2, 'RESTOCK', 4200.00, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY), 'Initial stock'),
(3, 'RESTOCK',   42.00, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY), 'Initial stock'),
(4, 'RESTOCK',   18.00, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY), 'Initial stock'),
(5, 'RESTOCK',   25.00, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY), 'Initial stock'),
(6, 'RESTOCK', 2000.00, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY), 'Initial stock'),
(6, 'USAGE',    -200.00, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY),  'CNC jobs since last restock');

/*
   6. BOOKINGS (FR-03)
*/
INSERT INTO equipment_bookings (user_id, asset_tag, start_time, end_time, booking_status, created_at) VALUES
(3, '3DP-002',   DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 10 HOUR,
              DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 12 HOUR,
              'BOOKED', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY)),
(4, 'LASER-001', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 MINUTE), DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 95 MINUTE), 'IN_PROGRESS', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 HOUR)),
(5, '3DP-001',   DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY),
              DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY) + INTERVAL 2 HOUR, 'COMPLETED', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY)),
(3, 'CNC-001',   DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY) + INTERVAL 1 HOUR, 'CANCELLED', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));

/*
   7. USAGE SESSIONS + MATERIALS CONSUMED (FR-03/FR-04)
   Session 1: completed, tied to booking 3 (Sam Patel on 3DP-001, 3 days ago).
   Session 2: still ACTIVE right now, tied to booking 2 (Jordan Kim on
   LASER-001) - shows up on the Live Equipment / Session Report.
*/
INSERT INTO equipment_usage_sessions (booking_id, user_id, asset_tag, check_in_time, check_out_time, elapsed_minutes, hourly_rate, equipment_debit, session_status) VALUES
(3, 5, '3DP-001', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY),
                  DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY) + INTERVAL 195 MINUTE,
                  195, 2.50, 8.13, 'COMPLETED'),
(2, 4, 'LASER-001', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 MINUTE), NULL, NULL, 4.50, 0.00, 'ACTIVE');

INSERT INTO material_usage (usage_session_id, consumable_id, quantity_used, unit_rate, material_debit, recorded_at) VALUES
(1, 1, 120.00, 0.02, 2.40, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY) + INTERVAL 190 MINUTE);

/*
   8. TRAINING (FR-06/FR-07 style qualification gating)
*/
INSERT INTO training_sessions (trainer_id, category, title, scheduled_start, scheduled_end, location, capacity, status, trainer_credit) VALUES
(2, 'CNC',             'CNC Machining Basics',              DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY) + INTERVAL 2 HOUR, 'Maker Lab - Classroom', 8,  'COMPLETED', 30.00),
(2, 'THREE_D_PRINTER', '3D Printing Safety & Fundamentals',  DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 DAY),  DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 DAY) + INTERVAL 2 HOUR,  'Maker Lab - Classroom', 10, 'SCHEDULED', 0.00);

INSERT INTO training_bookings (training_session_id, trainee_id, booking_status, booked_at, trainee_confirmed_at, trainer_confirmed_at) VALUES
(1, 3, 'ATTENDED', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY)),
(2, 4, 'BOOKED',   DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY),  NULL, NULL),
(2, 5, 'CONFIRMED',DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY),  DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 HOUR), NULL);

INSERT INTO user_qualifications (user_id, category, training_session_id, qualified_at, expires_at, qualification_status) VALUES
(3, 'CNC', 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY), DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 345 DAY), 'ACTIVE');

/*
   9. EXTERNAL CLIENTS & WORK ORDERS (FR external job intake)
*/
INSERT INTO external_clients (client_name, phone, email, created_at) VALUES
('Acme Robotics Inc.',          '613-555-0142', 'projects@acmerobotics.example', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY)),
('Northside Makerspace Co-op',  '613-555-0198', 'hello@northsidemakerspace.example', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));

INSERT INTO work_orders (client_id, member_user_id, assigned_shop_tech_id, description, priority, status,
                          estimated_equipment_cost, estimated_material_cost, estimated_labour_cost, quoted_price,
                          credit_earned, agreement_accepted, agreement_accepted_at, submitted_at, completed_at) VALUES
(1, NULL, 1, 'Laser-cut 50 acrylic enclosure panels, 5mm, per supplied DXF.', 'RUSH', 'COMPLETED',
 45.00, 180.00, 60.00, 285.00, 25.00, TRUE, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY),
 DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY)),
(NULL, 3, 1, 'CNC-mill a custom skateboard truck bracket prototype.', 'STANDARD', 'IN_PROGRESS',
 25.00, 40.00, 30.00, 95.00, 0.00, TRUE, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY),
 DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY), NULL),
(2, NULL, NULL, '3D print 20 conference-badge holders, PLA, client-supplied STL.', 'STANDARD', 'SUBMITTED',
 0.00, 0.00, 0.00, NULL, 0.00, FALSE, NULL,
 DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY), NULL);

/*
   10. ACCOUNT TRANSACTIONS / LEDGER (FR ledger & settlement)
*/
INSERT INTO account_transactions (user_id, transaction_type, activity_type, amount, description, transaction_date, settled, settled_at) VALUES
(5, 'DEBIT',  'EQUIPMENT_USAGE', 8.13,  '3D printer usage - 3DP-001', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY) + INTERVAL 195 MINUTE, TRUE,  DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY)),
(5, 'DEBIT',  'MATERIAL_USAGE',  2.40,  'PLA filament used - 3DP-001', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY) + INTERVAL 195 MINUTE, TRUE,  DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY)),
(5, 'PAYMENT','SETTLEMENT',      10.53, 'Account settlement',          DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY), TRUE, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY)),
(2, 'CREDIT', 'TRAINING',        30.00, 'Conducted CNC Machining Basics', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY), FALSE, NULL),
(1, 'CREDIT', 'MAINTENANCE',     15.00, 'Maintenance on 3DP-002 (1.00 hrs)', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY), TRUE, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY)),
(1, 'CREDIT', 'WORK_ORDER',      25.00, 'Work order #1 completed (Acme Robotics Inc.)', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY), FALSE, NULL);

/*
   11. NOTIFICATIONS
*/
INSERT INTO notifications (user_id, notification_type, title, message, created_at, read_at, notification_status) VALUES
(1, 'LOW_STOCK', 'Cutting Fluid running low', 'Cutting Fluid is at 1800.00 mL, below its 2000.00 mL restock level.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY), NULL, 'UNREAD'),
(1, 'MAINTENANCE', 'Nozzle maintenance required on 3DP-001', 'Nozzle reached its predictive-maintenance alert threshold (156.0 / 150.0 hrs).', CURRENT_TIMESTAMP, NULL, 'UNREAD'),
(4, 'TRAINING_REMINDER', 'Upcoming: 3D Printing Safety & Fundamentals', 'Your training session is coming up in 5 days.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY), NULL, 'UNREAD'),
(1, 'CONFIRMATION_REQUIRED', 'New work order awaiting review', 'Work order #3 (Northside Makerspace Co-op) needs a quote.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY), NULL, 'UNREAD');
