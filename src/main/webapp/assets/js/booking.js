// Author: Jiaying Chen
/*
  CMSC — Book Equipment page helper (front-end only; the real validation
  still happens server-side in BookingBusinessLogic.bookEquipment()).
  Uses the shared initTimeRange() helper (time-range.js), which in turn
  uses initQuarterHourField() (quarter-hour-field.js) so Start/End are
  always picked from a date + Hour + Minute(:00/:15/:30/:45) control and
  End can never be at or before Start.
*/
(function () {
  if (typeof initTimeRange === "function") {
    initTimeRange("startTimeField", "endTimeField", "bookingForm", 15);
  }
})();
