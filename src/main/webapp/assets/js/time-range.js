// Author: Jiaying Chen
/*
  CMSC — shared helper for any form with a Start/End quarter-hour pair.
  Wraps two initQuarterHourField() pickers (see assets/js/quarter-hour-
  field.js, load it first) and keeps End from ever landing at or before
  Start. Front-end convenience only; the authoritative check is always
  done server-side in the business layer (end-after-start rule plus
  businesslayer.TimeSlotValidation).
*/
function initTimeRange(startFieldId, endFieldId, formId, slotMinutes) {
  var slot = (slotMinutes || 15) * 60 * 1000;
  var form = formId ? document.getElementById(formId) : null;

  function pad(n) { return String(n).padStart(2, "0"); }
  function toDate(value) { return value ? new Date(value) : null; }

  var endField = initQuarterHourField(endFieldId, {});

  var startField = initQuarterHourField(startFieldId, {
    onChange: function (value) {
      var start = toDate(value);
      if (!start) return;
      var end = toDate(endField.getValue());
      if (!end || end.getTime() <= start.getTime()) {
        var earliest = new Date(start.getTime() + slot);
        endField.setValue(
          earliest.getFullYear() + "-" + pad(earliest.getMonth() + 1) + "-" + pad(earliest.getDate()),
          pad(earliest.getHours()),
          pad(earliest.getMinutes())
        );
      }
    }
  });

  if (form) {
    form.addEventListener("submit", function (e) {
      var start = toDate(startField.getValue());
      var end = toDate(endField.getValue());
      if (!start || !end || end.getTime() <= start.getTime()) {
        e.preventDefault();
        alert("End time must be after start time.");
      }
    });
  }

  return { startField: startField, endField: endField };
}
