/**
 * Dashboard
 */

$(document).ready(function () {
    logger.d("Loaded dashboard.js");
});


/*
 * Controllers
 */

var dashboard = new Object();

dashboard.load = function load(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/dashboard", function (data) {
        $("#content").html(data);
        $("#menu-dashboard").addClass("active");
    });
};

/*
 *  Handlers
 */


/*
 *  Actions
 */
