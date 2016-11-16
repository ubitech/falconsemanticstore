/**
 * Auth
 */

$(document).ready(function () {
    logger.d("Loaded auth.js");
});

// Static variables
var LOGIN_REST_URL = "/api/v1/auth/login";
var REGISTER_REST_URL = "/api/v1/user";

/*
 * Controllers
 */

var auth = new Object();

auth.login = function login(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/login", function (data) {
        $("#content").html(data);
        $("body").removeClass("home");
    });
};

auth.logout = function logout(ctx) {
    logger.d("Logout requested, removing token from localstorage...");
    removeAccessToken();
    goHome();
}

auth.create = function create(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/register", function (data) {
        $("#content").html(data);
        $("body").removeClass("home");
    });
}


/*
 * Handlers
 */

// Handle login
function loginHandler() {
//Crate Credentials JSON object
    var credentials = new Object();
    credentials.username = $("#username").val();
    credentials.password = $("#password").val();

    logger.d("Trying to login using credentials: " + JSON.stringify(credentials));

    $.post({
        data: JSON.stringify(credentials),
        url: LOGIN_REST_URL,
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        logger.i("Sign-in is success");
        //Save authorization token to localstorage
        addAccessToken(xhr.getResponseHeader(AUTHORIZATION_HEADER));
        //Redirect user to dashboard page
        goHome();
    }).fail(function (error) {
        var response = JSON.parse(error.responseText);
        notifier.notify(response.message + " [" + response.status + "]", {mode: "notification-error"});
        logger.e("Code: " + error.status + " Message: " + error.responseText);
    });
}

// Handle register
function registerHandler() {
    //Create User object
    var user = new Object();
    user.username = $("#username").val();
    user.password = $("#password").val();
    user.firstname = $("#firstname").val();
    user.lastname = $("#lastname").val();
    user.email = $("#email").val();

    //Make the register call
    $.post({
        data: JSON.stringify(user),
        url: REGISTER_REST_URL,
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        //Check if the new account is created
        if ("SUCCESS" === data.code) {
            logger.i("Registration is success");
            //Redirect user to login page
            notifier.notify(data.message);
            page.redirect("/login");
        } else {
            notifier.notify(data.message, {mode: "notification-error"});
        }

    }).fail(function (error) {
        var response = JSON.parse(error.responseText);
        notifier.notify(response.message + " [" + response.status + "]", {mode: "notification-error"});
        logger.e("Code: " + error.status + " Message: " + error.responseText);
    });
}

/*
 *  Actions
 */

/**
 * Function to trigger Signin when a user press enter on password text field
 *
 * @param {type} e
 * @returns {undefined}
 */

function keyPressedOnLoginPasswordField(e) {
    var key = e.keyCode || e.which;
    //On enter pressed send login request
    if (key === 13) {
        loginHandler();
    }
}