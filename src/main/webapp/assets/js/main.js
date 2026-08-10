// Author: Jiaying Chen
/*
  CMSC — front-end only behavior.
  No fetch/AJAX calls here yet.

  Highlights the sidebar link matching the current page, by
  comparing file names (not full paths), since each page's
  file name is unique across sections. This keeps working no
  matter which folder depth a page is served from.
*/
(function () {
  var currentFile = window.location.pathname.split("/").pop();

  document.querySelectorAll(".nav-link").forEach(function (link) {
    var linkFile = link.getAttribute("href").split("/").pop();
    if (linkFile === currentFile) {
      link.classList.add("active");
    }
  });
})();
