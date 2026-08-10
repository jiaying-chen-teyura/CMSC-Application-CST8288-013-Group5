/*
  CMSC — shared "quarter-hour" date/time picker.
  Used everywhere a member/trainer/shop-tech picks a scheduled time
  (Book Equipment, Schedule Maintenance, Schedule Training) so the
  front end only ever offers four minute choices per hour: :00, :15,
  :30, :45.

  Renders a <input type="date"> plus an Hour <select> (00-23) and a
  Minute <select> (00/15/30/45) inside an empty wrapper element, e.g.:
      <div class="qh-picker" id="startTimeField" data-name="startTime"></div>
  and keeps a hidden input (named after data-name, formatted the same
  way a datetime-local input would: yyyy-MM-ddTHH:mm) in sync, so the
  existing Command classes - which just call
  LocalDateTime.parse(request.getParameter(...)) - don't need to change.

  Front-end convenience only; businesslayer.TimeSlotValidation is the
  authoritative server-side check.
*/
function initQuarterHourField(wrapperId, options) {
  var opts = options || {};
  var wrap = document.getElementById(wrapperId);
  if (!wrap) return null;

  var fieldName = wrap.getAttribute("data-name");

  var dateInput = document.createElement("input");
  dateInput.type = "date";
  dateInput.className = "qh-date";
  dateInput.required = true;
  if (opts.min) dateInput.min = opts.min;

  var hourSelect = document.createElement("select");
  hourSelect.className = "qh-hour";
  hourSelect.setAttribute("aria-label", "Hour");
  for (var h = 0; h < 24; h++) {
    var hh = String(h).padStart(2, "0");
    var hourOpt = document.createElement("option");
    hourOpt.value = hh;
    hourOpt.textContent = hh;
    hourSelect.appendChild(hourOpt);
  }

  var minuteSelect = document.createElement("select");
  minuteSelect.className = "qh-minute";
  minuteSelect.setAttribute("aria-label", "Minute");
  ["00", "15", "30", "45"].forEach(function (m) {
    var minuteOpt = document.createElement("option");
    minuteOpt.value = m;
    minuteOpt.textContent = ":" + m;
    minuteSelect.appendChild(minuteOpt);
  });

  var hidden = document.createElement("input");
  hidden.type = "hidden";
  hidden.name = fieldName;

  wrap.appendChild(dateInput);
  wrap.appendChild(hourSelect);
  wrap.appendChild(minuteSelect);
  wrap.appendChild(hidden);

  function sync() {
    hidden.value = dateInput.value ? (dateInput.value + "T" + hourSelect.value + ":" + minuteSelect.value) : "";
    if (typeof opts.onChange === "function") opts.onChange(hidden.value);
  }

  dateInput.addEventListener("change", sync);
  hourSelect.addEventListener("change", sync);
  minuteSelect.addEventListener("change", sync);
  sync();

  return {
    dateInput: dateInput,
    hourSelect: hourSelect,
    minuteSelect: minuteSelect,
    getValue: function () { return hidden.value; },
    setValue: function (date, hour, minute) {
      dateInput.value = date;
      hourSelect.value = hour;
      minuteSelect.value = minute;
      sync();
    }
  };
}
