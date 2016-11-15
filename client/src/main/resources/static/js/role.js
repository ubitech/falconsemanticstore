/**
 * Role
 */

$(document).ready(function () {
    logger.d("Loaded role.js");
});

// Static variables
var ROLE_API_URL = "/api/v1/role/";

/*
 * Controllers
 */

var role = new Object();

role.list = function list(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/role", function (data) {
        $("#content").html(data);
        $("#menu-role").addClass("active");
    });
};

role.add = function add(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/role/add", function (data) {
        $("#content").html(data);
        $("#menu-role").addClass("active");
    });
};

role.edit = function edit(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    var id = ctx.params.id;
    $.post("/role/" + id, function (data) {
        $("#content").html(data);
        $("#menu-role").addClass("active");
    });
};

/*
 *  Handlers
 */

function roleAddHandler(edit) {
    //Create role object
    var role = new Object();
    role.actor = $("#actor").val();
    role.description = $("#description").val();

    //Make the add call
    $.post({
        data: JSON.stringify(role),
        url: ROLE_API_URL,
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        //Check if the new account is created
        if ("SUCCESS" === data.code) {
            logger.i("Role has been created.");
            //Redirect to role list
            notifier.notify(data.message);
            page.redirect("/role");
        } else {
            notifier.notify(data.message, {mode: "notification-error"});
        }

    });
}

function roleEditHandler() {
    //Create role object
    var role = new Object();
    role.actor = $("#actor").val();
    role.description = $("#description").val();

    //Make the edit call
    $.ajax({
        type: 'PUT',
        data: JSON.stringify(role),
        url: ROLE_API_URL,
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        //Check if the new account is created
        if ("SUCCESS" === data.code) {
            logger.i("Role has been updated.");
            //Redirect to role list
            notifier.notify(data.message);
            page.redirect("/role");
        } else {
            notifier.notify(data.message, {mode: "notification-error"});
        }

    });
}

function roleDeleteHandler(id) {
    logger.d("Trying to delete role with id: " + id);
    $.ajax({
        type: 'DELETE',
        url: ROLE_API_URL + id,
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        notifier.notify(data.message);
        //Redirect to role list
        page.redirect("/role");
    }).fail(function (error) {
        var response = JSON.parse(error.responseText);
        notifier.notify(response.message + " [" + response.status + "]", {mode: "notification-error"});
        logger.e("Code: " + error.status + " Message: " + error.responseText);
    });
}

/*
 *  Actions
 */