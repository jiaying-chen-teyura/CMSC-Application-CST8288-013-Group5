# About This Fork

This is a personal fork of a 4-person group project for CST8288 (OOP with
Design Patterns), Algonquin College, Summer 2026 — forked here to showcase
my individual contribution.

**Team:** Jiaying Chen, Tianzhu Li, Le Bao Thach Nguyen, Oladimeji Durojaiye
**Original repository:** [CMSC-Application-CST8288-013-Group5](https://github.com/jiaying-chen-teyura/CMSC-Application-CST8288-013-Group5)

## My Contribution — Equipment, Sessions & Maintenance Module

- **Business Layer:** `EquipmentBusinessLogic`, `UsageSessionBusinessLogic`, `MaintenanceBusinessLogic`
- **DAO Layer:** `EquipmentDao`/`Impl`, `EquipmentUsageSessionDao`/`Impl`, `MaintenanceDao`/`Impl`
- **DTOs:** `EquipmentDTO`, `EquipmentComponentDTO`, `EquipmentUsageSessionDTO`, `MaintenanceTaskDTO`
- **Design Patterns implemented:** Builder (`EquipmentBuilder`), Simple Factory (`EquipmentFactory`),
  Observer (`MaintenanceAlertService`, `MaintenanceListener`, `ShopTechAlertListener`)
- **Business Domain modeling:** `EquipmentProfile` + category-specific subtypes
  (`CncMachineProfile`, `LaserCutterProfile`, `ThreeDPrinterProfile`)

All layers include full Javadoc with authorship attribution. See commit
history for the incremental development of this module.