/**
 * User
 */

$(document).ready(function () {
    logger.d("Loaded user.js");
});

// Static variables
var DELETE_REST_URL = "/api/v1/user/";
var ROLE_API_URL = "/api/v1/role/";

/*
 * Controllers
 */

var user = new Object();

user.list = function list(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/user", function (data) {
        $("#content").html(data);
        $("#menu-user").addClass("active");
    });
};

user.add = function add(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    $.post("/user/add", function (data) {
        $("#content").html(data);
        $("#menu-user").addClass("active");
    });
};

user.edit = function edit(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    var id = ctx.params.id;
    $.post("/user/" + id, function (data) {
        $("#content").html(data);
        $("#menu-user").addClass("active");
    });
};

user.role = function edit(ctx) {
    logger.i("Current page set to: " + ctx.pathname);
    var id = ctx.params.id;
    $.post("/user/" + id + "/role", function (data) {
        $("#content").html(data);
        $("#menu-user").addClass("active");
    });
};

/*
 *  Handlers
 */

function addUserHandler() {
    //Create User object
    var user = new Object();
    user.username = $("#username").val();
    user.password = $("#password").val();
    user.firstname = $("#firstname").val();
    user.lastname = $("#lastname").val();
    user.email = $("#email").val();

    //Make the add call
    $.post({
        data: JSON.stringify(user),
        url: REGISTER_REST_URL,
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        //Check if the new account is created
        if ("SUCCESS" === data.code) {
            logger.i("Registration is success");
            //Redirect to login
            notifier.notify(data.message);
            page.redirect("/user");
        } else {
            notifier.notify(data.message, {mode: "notification-error"});
        }

    });
}

function editUserHandler() {
    //Create User object
    var user = new Object();
    user.id = $("#uid").val();
    user.username = $("#username").val();
    user.password = $("#password").val();
    user.firstname = $("#firstname").val();
    user.lastname = $("#lastname").val();
    user.email = $("#email").val();

    //Make the add call
    $.ajax({
        type: 'PUT',
        data: JSON.stringify(user),
        url: REGISTER_REST_URL,
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        //Check if the new account is created
        if ("SUCCESS" === data.code) {
            logger.i("Registration is success");
            //Redirect to login
            notifier.notify(data.message);
            page.redirect("/user");
        } else {
            notifier.notify(data.message, {mode: "notification-error"});
        }

    });
}

function deleteUserHandler(ID) {
    logger.d("Trying to delete user with id: " + ID);
    $.ajax({
        type: 'DELETE',
        url: DELETE_REST_URL + ID,
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        notifier.notify(data.message);
        //Reload users page
        page.redirect("/user");
    }).fail(function (error) {
        var response = JSON.parse(error.responseText);
        notifier.notify(response.message + " [" + response.status + "]", {mode: "notification-error"});
        logger.e("Code: " + error.status + " Message: " + error.responseText);
    });
}

function assignRoleHandler(id) {
    logger.d("Trying to assign user role with id: " + id);
    var role = {id: $("#assignableRole").val()};
    $.ajax({
        type: 'PUT',
        url: ROLE_API_URL,
        data: JSON.stringify(role),
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        notifier.notify(data.message);
        //Redirect to user role
        page.redirect("/user/" + id + "/role");
    }).fail(function (error) {
        var response = JSON.parse(error.responseText);
        notifier.notify(response.message + " [" + response.status + "]", {mode: "notification-error"});
        logger.e("Code: " + error.status + " Message: " + error.responseText);
    });
}

function unassignRoleHandler(id) {
    logger.d("Trying to unassign user role with id: " + id);
    var role = {id: id};
    $.ajax({
        type: 'DELETE',
        url: ROLE_API_URL,
        data: JSON.stringify(role),
        contentType: "application/json; charset=utf-8"
    }).success(function (data, status, xhr) {
        notifier.notify(data.message);
        //Redirec to user role
        page.redirect("/user/" + id + "/role");
    }).fail(function (error) {
        var response = JSON.parse(error.responseText);
        notifier.notify(response.message + " [" + response.status + "]", {mode: "notification-error"});
        logger.e("Code: " + error.status + " Message: " + error.responseText);
    });
}

/*
 *  Actions
 */