# CMSC - Campus Maker Space Co-op

CST8288 Final Project. 3-tier Java EE web app (Presentation / Business / Data),
Java 21, Java Servlets + JSP, MySQL 8.0.40, Payara Server 7.2026.6+.

## 1. Setup

1. **Database.** In MySQL 8.0.40, run `sql/CMSC_database.sql`. It drops/recreates
   the `cmsc` database, creates all tables/views, and seeds a handful of demo
   rows (5 users, 4 equipment items, their wear components, 4 consumables,
   1 external client). Delete section 10 of the script if you don't want seed
   data.
2. **Connection settings.** Edit `src/main/resources/database.properties` to
   match your MySQL host/port/credentials (defaults to
   `jdbc:mysql://localhost:3307/cmsc`, matching the port you were already using).
3. **Open in NetBeans** as a Maven project, set the target server to Payara
   7.2026.6+, Run.
4. **Demo logins** (seeded, password shown in the SQL comment):
   - `admin@cmsc.local` / `admin123` (Admin)
   - `jane@abc.com` / `user123` (User)
   - `tara@abc.com` / `trainer123` (Trainer)
   - `theo@abc.com` / `tech123` (Shop-Tech)
   - External clients don't log in - they use `external-request.jsp`.

## 2. Architecture

Matches the team's original package plan:

```
com.algonquin.cmsc
├── controller/              Presentation tier entry point
│   ├── ControllerServlet    Front Controller (@WebServlet "/controller")
│   ├── SessionUtil          session helper (current logged-in user)
│   └── command/             one Command per use case (+ CommandFactory)
├── businesslayer/           Business tier
│   ├── *BusinessLogic.java  one per functional area (FR-01..FR-06)
│   ├── strategy/            Strategy pattern - credit calculation
│   ├── factory/              Simple Factory pattern - equipment profiles
│   ├── builder/              Builder pattern - EquipmentDTO / WorkOrderDTO
│   ├── observer/             Observer pattern - maintenance/inventory alerts
│   ├── adapter/               Adapter pattern - diagnostics + external job requests
│   └── domain/                per-category equipment profiles used by the factory
├── dataaccesslayer/         Data tier - DAO pattern (interface + JDBC impl per table)
├── transferobjects/         DTOs shared across all three tiers
└── webapp/                  Presentation tier - JSPs (WEB-INF/views is not
                              directly URL-accessible, per the JSP best practice)
```

JSPs never call the Data tier directly - every request goes
`JSP form -> ControllerServlet -> Command -> *BusinessLogic -> *Dao -> MySQL`,
and results come back the same way via request attributes.

## 3. Required Design Patterns - file table

| Pattern | Where it's used | Key file(s) |
|---|---|---|
| **DAO** | Every table has an interface + JDBC implementation, isolating SQL from the business layer. | `dataaccesslayer/*Dao.java`, `dataaccesslayer/*DaoImpl.java`, `dataaccesslayer/DataSource.java` |
| **Builder** | Assembling `EquipmentDTO` (FR-02 registration) and `WorkOrderDTO` (member or external-client submission) without telescoping constructors. | `businesslayer/builder/EquipmentBuilder.java`, `businesslayer/builder/WorkOrderBuilder.java` |
| **Strategy** | Swappable credit-calculation math per contribution type (donation, maintenance, training, work order), selected at runtime via `CreditContext`. | `businesslayer/strategy/CreditStrategy.java`, `CreditContext.java`, `DonationCreditStrategy.java`, `MaintenanceCreditStrategy.java`, `TrainingCreditStrategy.java`, `WorkOrderCreditStrategy.java` |
| **Simple Factory** | `EquipmentFactory.createProfile(category)` returns the category-specific `EquipmentProfile` (default wear components/thresholds) used when registering new equipment (FR-02/FR-05). | `businesslayer/factory/EquipmentFactory.java`, `businesslayer/domain/EquipmentProfile.java` + its 3 subclasses |
| **Adapter** | (1) Converts third-party equipment telemetry (`ThirdPartyDiagnosticsPacket`) into our `DiagnosticsReading` shape for FR-03/FR-05. (2) Converts the external "request a job" form (`ExternalJobRequestForm`) into a `WorkOrderDTO` via the Builder, for the External Client actor. | `businesslayer/adapter/EquipmentDiagnosticsAdapter.java`, `businesslayer/adapter/ExternalJobRequestAdapter.java` |
| **Observer** | `MaintenanceAlertService`/`InventoryAlertService` (subjects) notify registered listeners the instant a component crosses its maintenance threshold (FR-05) or a consumable hits its restock level (FR-04), decoupling the business logic that detects the condition from whoever needs to react to it. | `businesslayer/observer/MaintenanceAlertService.java`, `InventoryAlertService.java`, `MaintenanceListener.java`, `InventoryListener.java`, `ShopTechAlertListener.java`, `InventoryAlertListener.java` |
| **Front Controller** (required "controller class") | `ControllerServlet` is the single servlet all Presentation-tier requests go through; it never touches the Data tier directly. | `controller/ControllerServlet.java` |
| Command *(supporting, not one of the 4 graded patterns - documented for completeness)* | One `Command` implementation per use-case bubble on the diagram; `CommandFactory` (a second, infrastructure-only Simple Factory - the graded Simple Factory instance is `EquipmentFactory` above) maps the `action` request parameter to a `Command`. | `controller/command/Command.java`, `CommandFactory.java`, and the 27 concrete `*Command.java` classes |

## 4. Functional requirement -> code map

| Requirement | Business logic | Commands / JSPs |
|---|---|---|
| FR-01 Registration & Auth | `UserBusinessLogic` | `RegisterCommand`, `LoginCommand`, `LogoutCommand`, `login.jsp`, `register.jsp` |
| FR-02 Equipment & Resource Mgmt | `EquipmentBusinessLogic`, `BookingBusinessLogic` | `RegisterEquipmentCommand`, `EditEquipmentCommand`, `DeleteEquipmentCommand`, `BookEquipmentCommand`, `CancelBookingCommand`, `equipment.jsp`, `booking.jsp` |
| FR-03 Usage & Session Tracking | `UsageSessionBusinessLogic` | `CheckInEquipmentCommand`, `CheckOutEquipmentCommand`, `ViewActiveSessionsCommand`, `sessions.jsp` |
| FR-04 Consumables & Inventory | `ConsumableBusinessLogic` | `DonateConsumableCommand`, `ViewInventoryCommand`, `RegisterConsumableCommand`, `EditConsumableCommand`, `DeleteConsumableCommand` (Shop-Tech only), `consumables.jsp` (uses `v_consumable_inventory_report`) |
| FR-05 Predictive Maintenance | `MaintenanceBusinessLogic` + Observer pattern | `ScheduleMaintenanceCommand`, `PerformMaintenanceCommand`, `ViewMaintenanceAlertsCommand`, `maintenance.jsp` |
| FR-06 Reporting & Analytics | `LedgerBusinessLogic`, `TrainingBusinessLogic` | `ViewLedgerCommand`, `SettleAccountCommand`, `ViewTrainerReportCommand`, `ViewShopTechReportCommand`, `ViewEquipmentInventoryStatusReportCommand`, `ledger.jsp`, `training.jsp`, `reports/*.jsp` (uses `v_user_monthly_account_report`) |
| Work Orders (member + external client) | `WorkOrderBusinessLogic` + Adapter pattern | `SubmitWorkOrderCommand`, `AcceptWorkOrderCommand`, `CompleteWorkOrderCommand`, `workorders.jsp`, `external-request.jsp` |

## 5. What was carried over from the starter project

- `assets/css/main.css` and `assets/css/variables.css` are your original files,
  untouched, with new rules **appended** at the bottom (tables, badges, alerts,
  form-grid) so the new screens match the existing look. Nothing was deleted
  or renamed.
- `assets/js/main.js` copied as-is.
- The `login.jsp` / `register.jsp` markup and field layout mirror your
  originals; they now `POST` to `ControllerServlet` instead of a placeholder.
- The `sql/CMSC_database.sql` schema and views are your teammate's, unmodified
  in structure - only a guarded seed-data block was appended.

## 6. Known simplifications (documented, not hidden)

- Passwords are hashed with SHA-256 (no per-user salt) - fine for a course
  project, called out in `PasswordUtil.java`'s javadoc as not
  production-grade.
- `EquipmentDaoImpl.deleteEquipment` is a soft delete (`active = FALSE`) so
  booking/session/maintenance history referencing that asset tag is preserved.
- The checkout screen lets a member report 0..N consumables used in one
  submission; quantities left at 0 are ignored.
- Diagnostics ingestion (`MaintenanceBusinessLogic.ingestDiagnostics`) is
  exposed as a plain business-layer method with no servlet endpoint yet,
  since the assignment leaves "equipment reports its real-time status" open
  to each team's design - wire it to a `/diagnostics` command + real/simulated
  telemetry source if your team wants it reachable over HTTP.

## 7. Bug fixes (post-review)

- **Check-in no longer completes the booking.** `EquipmentBookingDTO.BookingStatus`
  gained an `IN_PROGRESS` value (also added to the `booking_status` ENUM in
  `sql/CMSC_database.sql`). `UsageSessionBusinessLogic.checkIn` now sets the
  linked booking to `IN_PROGRESS`; `checkOut` is what sets it to `COMPLETED`.
  `EquipmentBookingDaoImpl.getOverlappingBookings` was updated to also block
  new bookings against `IN_PROGRESS` slots, not just `BOOKED` ones.
- **Walk-in Check-In removed.** Deleted that card from `booking.jsp` - check-in
  now only happens from the Check In button on a My Bookings row.
- **Confirm Check Out no longer "logs you out."** It never actually did -
  `web.xml` mapped *both* 404 and 500 errors to `login.jsp`, so any unhandled
  server error anywhere in the app (not just checkout) landed a still-logged-in
  user back on the login form, which looked exactly like a logout. Error pages
  now point at a real `error.jsp` that shows what happened and links back to
  the Dashboard or Login. This is also the most likely explanation for the
  Trainer → Training-page report (issue reported alongside checkout); no
  logout call exists anywhere in that code path.
- **Consumable management added (Shop-Tech only).** `ConsumableDao`/`ConsumableDaoImpl`
  gained `addConsumable`/`updateConsumable`/`deleteConsumable` (soft delete,
  same shape as `EquipmentDaoImpl`), `ConsumableBusinessLogic` gained
  `registerConsumable`/`editConsumable`/`deleteConsumable`, and three new
  commands (`RegisterConsumableCommand`, `EditConsumableCommand`,
  `DeleteConsumableCommand`) enforce `isShopTech()` before doing anything -
  `consumables.jsp` now has an "Add Consumable" form and a "Manage Consumables"
  table, both shown only to Shop-Techs. While matching this to equipment
  management, `EditEquipmentCommand`/`DeleteEquipmentCommand` turned out to be
  missing the same `isShopTech()` check that `RegisterEquipmentCommand` already
  had - added there too.
- **Stray "h" after every hour.** `quarter-hour-field.js` literally appended
  `"h"` to each hour option's label (`hh + "h"`); now just `hh`. Affects every
  screen that uses the shared quarter-hour picker (booking, maintenance,
  training).
- **`LedgerDaoImpl.settleDebits` broke FIFO order.** When a payment couldn't
  fully cover the oldest unsettled debit, the loop skipped it and kept
  scanning forward, settling a later *smaller* debit instead - silently
  violating the "oldest debits first" rule in the method's own comment. Now
  `break`s out of the loop the first time a debit exceeds the remaining
  payment, so nothing out of order ever gets settled.
- **`ControllerServlet.process` NPE on a missing `action` param.**
  `PUBLIC_ACTIONS` is a `Set.of(...)`, and `Set.of(...).contains(null)`
  throws `NullPointerException` rather than returning `false`. Any request to
  `/controller` with no `action` (bad link, hitting the URL bare) crashed
  before it ever reached `CommandFactory.getCommand(null)`, which was already
  written to default to the dashboard gracefully. Fixed by short-circuiting
  with `action != null &&` before the `contains` check.
