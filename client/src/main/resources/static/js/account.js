/**
 * Account
 */

$(document).ready(function () {
    logger.d("Loaded account.js");
});

// Static variables
var ACCOUNT_REST_URL = "/api/v1/account/";

/*
 * Controllers
 */

var account = new Object();
account.profile = new Object();

account.profile.list = function list(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/account/profile", function (data) {
        $("#content").html(data);
        $("#menu-profile").addClass("active");
    });
};

account.profile.edit = function add(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/account/profile/edit", function (data) {
        $("#content").html(data);
        $("#menu-profile").addClass("active");
    });
};


account.token = new Object();

account.token.list = function list(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/account/token", function (data) {
        $("#content").html(data);
        $("#menu-token").addClass("active");
    });
};

account.token.add = function add(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/account/token/add", function (data) {
        $("#content").html(data);
        $("#menu-token").addClass("active");
    });
};

/*
 *  Handlers
 */

function tokenAddHandler() {
    //Create User object
    var token = new Object();
    token.description = $("#token-description").val();

    //Check if description is empty, not send request
    if ('' === token.description) {
        notifier.notify("Description cannot be empty.", {mode: "notification-error"});
        return;
    }

    //Make the add call
    $.post({
        data: JSON.stringify(token),
        url: ACCOUNT_REST_URL + "token",
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        //Check if the new account is created
        if ("SUCCESS" === data.code) {
            logger.i("AccessToken is success");
            //Redirect user to login
            notifier.notify(data.message);
            $("#token-value").show();
            $("#token-value").html("<span class='oi oi-check'></span> " + data.returnobject)
            $("#token-add").hide();
            $("#token-description").prop('disabled', true);
        } else {
            notifier.notify(data.message, {mode: "notification-error"});
        }

    });
}

function tokenDeleteHandler(ID) {
    logger.d("Trying to delete token with id: " + ID);
    $.ajax({
        type: 'DELETE',
        url: ACCOUNT_REST_URL + "token/" + ID,
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        notifier.notify(data.message);
        //Reload access tokens page
        page.redirect("/account/token");
    }).fail(function (error) {
        var response = JSON.parse(error.responseText);
        notifier.notify(response.message + " [" + response.status + "]", {mode: "notification-error"});
        logger.e("Code: " + error.status + " Message: " + error.responseText);
    });
}

/*
 *  Actions
 */

function keyPressedOnTokenDescriptionField(e) {
    var key = e.keyCode || e.which;
    //On enter pressed call tokenAddHandler()
    if (key === 13) {
        tokenAddHandler();
    }
}