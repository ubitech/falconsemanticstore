/**
 * Token
 */

$(document).ready(function () {
    logger.d("Loaded token.js");
});

// Static variables
var TOKEN_API_URL = "/api/v1/token/";

/*
 * Controllers
 */
token = new Object();

token.list = function list(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post(ctx.canonicalPath, function (data) {
        $("#content").html(data);
        $("#menu-token").addClass("active");
    });
};




/*
 *  Handlers
 */


/*
 *  Actions
 */