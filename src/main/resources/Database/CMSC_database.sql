/*Author: Tianzhu Li*/

DROP DATABASE IF EXISTS cmsc;
CREATE DATABASE IF NOT EXISTS cmsc;
USE cmsc;

/* 
   This file is the canonical database schema for the CMSC application.
   It includes all schema definitions and the formerly separate migration
   additions (inventory_transactions.credit_earned and work_orders.started_at).

   1. USER AND AUTHENTICATION TABLES
 */

CREATE TABLE IF NOT EXISTS users (
    user_id             INT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(100) NOT NULL,
    email               VARCHAR(150) NOT NULL,
    password_hash       VARCHAR(255) NOT NULL,
    user_type           ENUM(
                            'USER',
                            'TRAINER',
                            'SHOP_TECH'
                        ) NOT NULL,
    account_status      ENUM(
                            'ACTIVE',
                            'SUSPENDED',
                            'INACTIVE'
                        ) NOT NULL DEFAULT 'ACTIVE',
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_at       DATETIME NULL,

    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS login_sessions (
    session_id          VARCHAR(128) PRIMARY KEY,
    user_id             INT NOT NULL,
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at          DATETIME NOT NULL,
    logged_out_at       DATETIME NULL,
    session_status      ENUM(
                            'ACTIVE',
                            'EXPIRED',
                            'LOGGED_OUT'
                        ) NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT fk_login_sessions_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    INDEX idx_login_sessions_user_status (user_id, session_status)
);

/* 
   2. EQUIPMENT TABLES
 */

CREATE TABLE IF NOT EXISTS equipment (
    asset_tag               VARCHAR(30) PRIMARY KEY,
    make                    VARCHAR(60) NOT NULL,
    model                   VARCHAR(60) NOT NULL,
    category                ENUM(
                                'THREE_D_PRINTER',
                                'LASER_CUTTER',
                                'CNC'
                            ) NOT NULL,
    equipment_name          VARCHAR(100) NOT NULL,
    status                  ENUM(
                                'AVAILABLE',
                                'IN_USE',
                                'UNAVAILABLE',
                                'MAINTENANCE'
                            ) NOT NULL DEFAULT 'AVAILABLE',
    access_credit_rate      DECIMAL(8,2) NOT NULL,
    total_usage_hours       DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    location                VARCHAR(100) NULL,
    registered_by           INT NOT NULL,
    registered_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active                  BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT chk_equipment_access_rate
        CHECK (access_credit_rate >= 0),
    CONSTRAINT chk_equipment_usage_hours
        CHECK (total_usage_hours >= 0),
    CONSTRAINT fk_equipment_registered_by
        FOREIGN KEY (registered_by)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    INDEX idx_equipment_status (status),
    INDEX idx_equipment_category (category)
);

CREATE TABLE IF NOT EXISTS equipment_bookings (
    booking_id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id             INT NOT NULL,
    asset_tag           VARCHAR(30) NOT NULL,
    start_time          DATETIME NOT NULL,
    end_time            DATETIME NOT NULL,
    booking_status      ENUM(
                            'BOOKED',
                            'IN_PROGRESS',
                            'CANCELLED',
                            'COMPLETED',
                            'NO_SHOW'
                        ) NOT NULL DEFAULT 'BOOKED',
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_equipment_booking_time
        CHECK (end_time > start_time),
    CONSTRAINT fk_equipment_bookings_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_equipment_bookings_equipment
        FOREIGN KEY (asset_tag)
        REFERENCES equipment(asset_tag)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    INDEX idx_equipment_bookings_user (user_id),
    INDEX idx_equipment_bookings_schedule
        (asset_tag, start_time, end_time)
);

CREATE TABLE IF NOT EXISTS equipment_usage_sessions (
    usage_session_id    INT AUTO_INCREMENT PRIMARY KEY,
    booking_id          INT NULL,
    user_id             INT NOT NULL,
    asset_tag           VARCHAR(30) NOT NULL,
    check_in_time       DATETIME NOT NULL,
    check_out_time      DATETIME NULL,
    elapsed_minutes     INT NULL,
    hourly_rate         DECIMAL(8,2) NOT NULL,
    equipment_debit     DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    session_status      ENUM(
                            'ACTIVE',
                            'COMPLETED',
                            'INTERRUPTED'
                        ) NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT chk_usage_elapsed_minutes
        CHECK (elapsed_minutes IS NULL OR elapsed_minutes >= 0),
    CONSTRAINT chk_usage_hourly_rate
        CHECK (hourly_rate >= 0),
    CONSTRAINT chk_usage_equipment_debit
        CHECK (equipment_debit >= 0),
    CONSTRAINT chk_usage_checkout_time
        CHECK (
            check_out_time IS NULL
            OR check_out_time >= check_in_time
        ),
    CONSTRAINT fk_usage_sessions_booking
        FOREIGN KEY (booking_id)
        REFERENCES equipment_bookings(booking_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_usage_sessions_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_usage_sessions_equipment
        FOREIGN KEY (asset_tag)
        REFERENCES equipment(asset_tag)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    INDEX idx_usage_sessions_user (user_id),
    INDEX idx_usage_sessions_equipment_status
        (asset_tag, session_status)
);

/*
   3. CONSUMABLE AND INVENTORY TABLES
 */

CREATE TABLE IF NOT EXISTS consumables (
    consumable_id       INT AUTO_INCREMENT PRIMARY KEY,
    material_name       VARCHAR(100) NOT NULL,
    unit                ENUM(
                            'GRAM',
                            'MILLILITRE',
                            'SHEET',
                            'PIECE'
                        ) NOT NULL,
    current_stock       DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    restock_level       DECIMAL(12,2) NOT NULL,
    unit_debit_rate     DECIMAL(10,2) NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uq_consumables_material_name
        UNIQUE (material_name),
    CONSTRAINT chk_consumables_current_stock
        CHECK (current_stock >= 0),
    CONSTRAINT chk_consumables_restock_level
        CHECK (restock_level >= 0),
    CONSTRAINT chk_consumables_unit_rate
        CHECK (unit_debit_rate >= 0)
);

CREATE TABLE IF NOT EXISTS equipment_consumables (
    asset_tag           VARCHAR(30) NOT NULL,
    consumable_id       INT NOT NULL,

    PRIMARY KEY (asset_tag, consumable_id),

    CONSTRAINT fk_equipment_consumables_equipment
        FOREIGN KEY (asset_tag)
        REFERENCES equipment(asset_tag)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_equipment_consumables_consumable
        FOREIGN KEY (consumable_id)
        REFERENCES consumables(consumable_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    INDEX idx_equipment_consumables_consumable (consumable_id)
);

CREATE TABLE IF NOT EXISTS material_usage (
    material_usage_id   INT AUTO_INCREMENT PRIMARY KEY,
    usage_session_id    INT NOT NULL,
    consumable_id       INT NOT NULL,
    quantity_used       DECIMAL(12,2) NOT NULL,
    unit_rate           DECIMAL(10,2) NOT NULL,
    material_debit      DECIMAL(10,2) NOT NULL,
    recorded_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_material_usage_quantity
        CHECK (quantity_used > 0),
    CONSTRAINT chk_material_usage_unit_rate
        CHECK (unit_rate >= 0),
    CONSTRAINT chk_material_usage_debit
        CHECK (material_debit >= 0),
    CONSTRAINT fk_material_usage_session
        FOREIGN KEY (usage_session_id)
        REFERENCES equipment_usage_sessions(usage_session_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_material_usage_consumable
        FOREIGN KEY (consumable_id)
        REFERENCES consumables(consumable_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    INDEX idx_material_usage_session (usage_session_id),
    INDEX idx_material_usage_consumable_date
        (consumable_id, recorded_at)
);

CREATE TABLE IF NOT EXISTS inventory_transactions (
    inventory_transaction_id    INT AUTO_INCREMENT PRIMARY KEY,
    consumable_id               INT NOT NULL,
    transaction_type            ENUM(
                                    'RESTOCK',
                                    'DONATION',
                                    'USAGE',
                                    'ADJUSTMENT'
                                ) NOT NULL,
    quantity_change             DECIMAL(12,2) NOT NULL,
    performed_by               INT NOT NULL,
    transaction_time            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes                       VARCHAR(255) NULL,
    credit_earned                DECIMAL(10,2) NULL,

    CONSTRAINT chk_inventory_quantity_change
        CHECK (quantity_change <> 0),
    CONSTRAINT chk_inventory_credit_earned
        CHECK (credit_earned IS NULL OR credit_earned >= 0),
    CONSTRAINT fk_inventory_transactions_consumable
        FOREIGN KEY (consumable_id)
        REFERENCES consumables(consumable_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_inventory_transactions_user
        FOREIGN KEY (performed_by)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    INDEX idx_inventory_consumable_date
        (consumable_id, transaction_time),
    INDEX idx_inventory_performed_by (performed_by)
);

/*
   4. MAINTENANCE TABLES
*/

CREATE TABLE IF NOT EXISTS equipment_components (
    component_id                   INT AUTO_INCREMENT PRIMARY KEY,
    asset_tag                      VARCHAR(30) NOT NULL,
    component_name                 VARCHAR(100) NOT NULL,
    usage_hours                    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    maintenance_threshold_hours    DECIMAL(10,2) NOT NULL,
    component_status               ENUM(
                                        'HEALTHY',
                                        'MAINTENANCE_REQUIRED',
                                        'BROKEN'
                                    ) NOT NULL DEFAULT 'HEALTHY',
    last_maintained_at              DATETIME NULL,

    CONSTRAINT uq_equipment_component
        UNIQUE (asset_tag, component_name),
    CONSTRAINT chk_component_usage_hours
        CHECK (usage_hours >= 0),
    CONSTRAINT chk_component_threshold
        CHECK (maintenance_threshold_hours > 0),
    CONSTRAINT fk_equipment_components_equipment
        FOREIGN KEY (asset_tag)
        REFERENCES equipment(asset_tag)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    INDEX idx_components_status (component_status)
);

CREATE TABLE IF NOT EXISTS maintenance_tasks (
    maintenance_id             INT AUTO_INCREMENT PRIMARY KEY,
    asset_tag                  VARCHAR(30) NOT NULL,
    component_id               INT NULL,
    assigned_shop_tech_id      INT NULL,
    maintenance_type           ENUM(
                                    'PREVENTIVE',
                                    'REPAIR',
                                    'INSPECTION'
                                ) NOT NULL,
    description                VARCHAR(500) NOT NULL,
    priority                   ENUM(
                                    'LOW',
                                    'MEDIUM',
                                    'HIGH',
                                    'URGENT'
                                ) NOT NULL DEFAULT 'MEDIUM',
    scheduled_start            DATETIME NULL,
    started_at                 DATETIME NULL,
    completed_at               DATETIME NULL,
    maintenance_hours          DECIMAL(8,2) NULL,
    status                     ENUM(
                                    'ALERTED',
                                    'SCHEDULED',
                                    'IN_PROGRESS',
                                    'COMPLETED',
                                    'CANCELLED'
                                ) NOT NULL DEFAULT 'ALERTED',
    credit_earned              DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    CONSTRAINT chk_maintenance_hours
        CHECK (
            maintenance_hours IS NULL
            OR maintenance_hours >= 0
        ),
    CONSTRAINT chk_maintenance_credit
        CHECK (credit_earned >= 0),
    CONSTRAINT chk_maintenance_dates
        CHECK (
            completed_at IS NULL
            OR started_at IS NULL
            OR completed_at >= started_at
        ),
    CONSTRAINT fk_maintenance_equipment
        FOREIGN KEY (asset_tag)
        REFERENCES equipment(asset_tag)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_maintenance_component
        FOREIGN KEY (component_id)
        REFERENCES equipment_components(component_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_maintenance_shop_tech
        FOREIGN KEY (assigned_shop_tech_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    INDEX idx_maintenance_equipment_status (asset_tag, status),
    INDEX idx_maintenance_shop_tech
        (assigned_shop_tech_id, status),
    INDEX idx_maintenance_schedule (scheduled_start)
);

/*
   5. CREDIT AND DEBIT TABLE
*/

CREATE TABLE IF NOT EXISTS account_transactions (
    account_transaction_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id                    INT NOT NULL,
    transaction_type           ENUM(
                                    'CREDIT',
                                    'DEBIT',
                                    'PAYMENT',
                                    'ADJUSTMENT'
                                ) NOT NULL,
    activity_type              ENUM(
                                    'EQUIPMENT_USAGE',
                                    'MATERIAL_USAGE',
                                    'DONATION',
                                    'TRAINING',
                                    'MAINTENANCE',
                                    'WORK_ORDER',
                                    'SETTLEMENT'
                                ) NOT NULL,
    amount                     DECIMAL(10,2) NOT NULL,
    description                VARCHAR(255) NOT NULL,
    transaction_date           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    settled                    BOOLEAN NOT NULL DEFAULT FALSE,
    settled_at                 DATETIME NULL,

    CONSTRAINT chk_account_transaction_amount
        CHECK (amount > 0),
    CONSTRAINT chk_account_transaction_settlement
        CHECK (
            (settled = FALSE AND settled_at IS NULL)
            OR settled = TRUE
        ),
    CONSTRAINT fk_account_transactions_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    INDEX idx_account_user_date (user_id, transaction_date),
    INDEX idx_account_user_type (user_id, transaction_type),
    INDEX idx_account_activity (activity_type)
);

/*
   6. TRAINING TABLES
 */

CREATE TABLE IF NOT EXISTS training_sessions (
    training_session_id     INT AUTO_INCREMENT PRIMARY KEY,
    trainer_id              INT NOT NULL,
    category                ENUM(
                                'THREE_D_PRINTER',
                                'LASER_CUTTER',
                                'CNC'
                            ) NOT NULL,
    title                   VARCHAR(150) NOT NULL,
    scheduled_start         DATETIME NOT NULL,
    scheduled_end           DATETIME NOT NULL,
    location                VARCHAR(100) NULL,
    capacity                INT NOT NULL,
    status                  ENUM(
                                'SCHEDULED',
                                'IN_PROGRESS',
                                'COMPLETED',
                                'CANCELLED'
                            ) NOT NULL DEFAULT 'SCHEDULED',
    trainer_credit          DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    CONSTRAINT chk_training_time
        CHECK (scheduled_end > scheduled_start),
    CONSTRAINT chk_training_capacity
        CHECK (capacity > 0),
    CONSTRAINT chk_training_credit
        CHECK (trainer_credit >= 0),
    CONSTRAINT fk_training_sessions_trainer
        FOREIGN KEY (trainer_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    INDEX idx_training_trainer (trainer_id),
    INDEX idx_training_category_schedule
        (category, scheduled_start),
    INDEX idx_training_status (status)
);

CREATE TABLE IF NOT EXISTS training_bookings (
    training_booking_id       INT AUTO_INCREMENT PRIMARY KEY,
    training_session_id       INT NOT NULL,
    trainee_id                INT NOT NULL,
    booking_status            ENUM(
                                  'BOOKED',
                                  'CONFIRMED',
                                  'ATTENDED',
                                  'ABSENT',
                                  'CANCELLED'
                              ) NOT NULL DEFAULT 'BOOKED',
    booked_at                 DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    trainee_confirmed_at      DATETIME NULL,
    trainer_confirmed_at      DATETIME NULL,

    CONSTRAINT uq_training_session_trainee
        UNIQUE (training_session_id, trainee_id),
    CONSTRAINT fk_training_bookings_session
        FOREIGN KEY (training_session_id)
        REFERENCES training_sessions(training_session_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_training_bookings_trainee
        FOREIGN KEY (trainee_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    INDEX idx_training_bookings_trainee
        (trainee_id, booking_status)
);

CREATE TABLE IF NOT EXISTS user_qualifications (
    qualification_id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id                   INT NOT NULL,
    category                  ENUM(
                                  'THREE_D_PRINTER',
                                  'LASER_CUTTER',
                                  'CNC'
                              ) NOT NULL,
    training_session_id       INT NOT NULL,
    qualified_at              DATETIME NOT NULL,
    expires_at                DATETIME NULL,
    qualification_status      ENUM(
                                  'ACTIVE',
                                  'EXPIRED',
                                  'REVOKED'
                              ) NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT uq_user_equipment_qualification
        UNIQUE (user_id, category),
    CONSTRAINT chk_qualification_expiry
        CHECK (
            expires_at IS NULL
            OR expires_at > qualified_at
        ),
    CONSTRAINT fk_user_qualifications_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_user_qualifications_training
        FOREIGN KEY (training_session_id)
        REFERENCES training_sessions(training_session_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    INDEX idx_qualifications_status
        (user_id, qualification_status)
);

/*
   7. EXTERNAL CLIENT AND WORK ORDER TABLES
*/

CREATE TABLE IF NOT EXISTS external_clients (
    client_id           INT AUTO_INCREMENT PRIMARY KEY,
    client_name         VARCHAR(100) NOT NULL,
    organization        VARCHAR(100) NULL,
    phone               VARCHAR(30) NOT NULL,
    email               VARCHAR(150) NOT NULL,
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS work_orders (
    work_order_id               INT AUTO_INCREMENT PRIMARY KEY,
    client_id                   INT NULL,
    member_user_id              INT NULL,
    assigned_shop_tech_id       INT NULL,
    description                 VARCHAR(1000) NOT NULL,
    priority                    ENUM(
                                    'STANDARD',
                                    'RUSH'
                                ) NOT NULL DEFAULT 'STANDARD',
    status                      ENUM(
                                    'SUBMITTED',
                                    'QUOTED',
                                    'ACCEPTED',
                                    'IN_PROGRESS',
                                    'COMPLETED',
                                    'CANCELLED'
                                ) NOT NULL DEFAULT 'SUBMITTED',
    estimated_equipment_cost    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estimated_material_cost     DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estimated_labour_cost       DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    quoted_price                DECIMAL(10,2) NULL,
    credit_earned				DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    agreement_accepted          BOOLEAN NOT NULL DEFAULT FALSE,
    agreement_accepted_at       DATETIME NULL,
    submitted_at                DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at                  DATETIME NULL,
    completed_at                DATETIME NULL,

    CONSTRAINT chk_work_order_equipment_cost
        CHECK (estimated_equipment_cost >= 0),
    CONSTRAINT chk_work_order_material_cost
        CHECK (estimated_material_cost >= 0),
    CONSTRAINT chk_work_order_labour_cost
        CHECK (estimated_labour_cost >= 0),
    CONSTRAINT chk_work_order_quoted_price
        CHECK (quoted_price IS NULL OR quoted_price >= 0),
    CONSTRAINT chk_work_order_agreement
        CHECK (
            (agreement_accepted = FALSE AND agreement_accepted_at IS NULL)
            OR agreement_accepted = TRUE
        ),
	CONSTRAINT chk_credit_earned
		CHECK ( credit_earned >= 0 ),
    CONSTRAINT fk_work_orders_client
        FOREIGN KEY (client_id)
        REFERENCES external_clients(client_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_work_orders_member
        FOREIGN KEY (member_user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_work_orders_shop_tech
        FOREIGN KEY (assigned_shop_tech_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    INDEX idx_work_orders_client (client_id),
    INDEX idx_work_orders_member (member_user_id),
    INDEX idx_work_orders_shop_tech
        (assigned_shop_tech_id, status),
    INDEX idx_work_orders_status_date (status, submitted_at)
);

/*
   8. NOTIFICATION TABLE
 */

CREATE TABLE IF NOT EXISTS notifications (
    notification_id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id                INT NOT NULL,
    notification_type      ENUM(
                                'LOW_STOCK',
                                'MAINTENANCE',
                                'TRAINING_REMINDER',
                                'CONFIRMATION_REQUIRED'
                            ) NOT NULL,
    title                  VARCHAR(150) NOT NULL,
    message                VARCHAR(500) NOT NULL,
    created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at                DATETIME NULL,
    notification_status    ENUM(
                                'UNREAD',
                                'READ',
                                'ARCHIVED'
                            ) NOT NULL DEFAULT 'UNREAD',

    CONSTRAINT fk_notifications_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    INDEX idx_notifications_user_status
        (user_id, notification_status),
    INDEX idx_notifications_created_at (created_at)
);

/*
   9. REPORTING VIEWS
*/

CREATE OR REPLACE VIEW v_equipment_live_status AS
SELECT
    e.asset_tag,
    e.equipment_name,
    e.category,
    e.status,
    u.user_id AS current_user_id,
    u.name AS current_user_name,
    s.check_in_time,
    CASE
        WHEN s.session_status = 'ACTIVE'
        THEN TIMESTAMPDIFF(MINUTE, s.check_in_time, CURRENT_TIMESTAMP)
        ELSE NULL
    END AS elapsed_minutes,
    COALESCE(SUM(mu.quantity_used), 0.00) AS active_material_quantity
FROM equipment e
LEFT JOIN equipment_usage_sessions s
    ON s.asset_tag = e.asset_tag
    AND s.session_status = 'ACTIVE'
LEFT JOIN users u
    ON u.user_id = s.user_id
LEFT JOIN material_usage mu
    ON mu.usage_session_id = s.usage_session_id
GROUP BY
    e.asset_tag,
    e.equipment_name,
    e.category,
    e.status,
    u.user_id,
    u.name,
    s.check_in_time,
    s.session_status;

CREATE OR REPLACE VIEW v_consumable_inventory_report AS
SELECT
    c.consumable_id,
    c.material_name,
    c.unit,
    c.current_stock,
    c.restock_level,
    CASE
        WHEN c.current_stock <= c.restock_level
        THEN 'RESTOCK_REQUIRED'
        ELSE 'SUFFICIENT'
    END AS stock_status,
    ROUND(COALESCE(recent.recent_qty, 0) / 30, 2) AS average_daily_consumption,
    -- projected_days_until_depletion: use the last-30-day consumption rate when there is one;
    -- otherwise fall back to the all-time average rate (so a single early check-out still
    -- produces an estimate instead of requiring 30 days of history); NULL only when the
    -- consumable has never actually been used yet, since there is no rate to project from.
    CASE
        WHEN COALESCE(recent.recent_qty, 0) > 0
            THEN ROUND(c.current_stock / (recent.recent_qty / 30), 1)
        WHEN COALESCE(alltime.total_qty, 0) > 0
            THEN ROUND(
                c.current_stock /
                (alltime.total_qty / GREATEST(DATEDIFF(CURRENT_TIMESTAMP, alltime.first_used), 1)),
                1
            )
        ELSE NULL
    END AS projected_days_until_depletion
FROM consumables c
LEFT JOIN (
    SELECT consumable_id, SUM(quantity_used) AS recent_qty
    FROM material_usage
    WHERE recorded_at >= CURRENT_TIMESTAMP - INTERVAL 30 DAY
    GROUP BY consumable_id
) recent ON recent.consumable_id = c.consumable_id
LEFT JOIN (
    SELECT consumable_id, SUM(quantity_used) AS total_qty, MIN(recorded_at) AS first_used
    FROM material_usage
    GROUP BY consumable_id
) alltime ON alltime.consumable_id = c.consumable_id;

CREATE OR REPLACE VIEW v_user_monthly_account_report AS
SELECT
    u.user_id,
    u.name,
    u.email,
    DATE_FORMAT(atx.transaction_date, '%Y-%m') AS report_month,
    SUM(
        CASE
            WHEN atx.transaction_type = 'CREDIT'
            THEN atx.amount
            ELSE 0
        END
    ) AS total_credits,
    SUM(
        CASE
            WHEN atx.transaction_type = 'DEBIT'
            THEN atx.amount
            ELSE 0
        END
    ) AS total_debits,
    SUM(
        CASE
            WHEN atx.transaction_type = 'PAYMENT'
            THEN atx.amount
            ELSE 0
        END
    ) AS total_payments,
    SUM(
        CASE
            WHEN atx.transaction_type = 'DEBIT'
            THEN atx.amount
            WHEN atx.transaction_type IN ('CREDIT', 'PAYMENT')
            THEN -atx.amount
            ELSE 0
        END
    ) AS amount_to_settle
FROM users u
JOIN account_transactions atx
    ON atx.user_id = u.user_id
GROUP BY
    u.user_id,
    u.name,
    u.email,
    DATE_FORMAT(atx.transaction_date, '%Y-%m');