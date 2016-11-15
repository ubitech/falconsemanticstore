/**
 * Router ( client-side router )
 * 
 * Handles all the requests for each client-side '/path'
 *   
 * For more information abouting using router please
 * refer to https://github.com/visionmedia/page.js
 */

$(document).ready(function () {
    logger.d("Loaded router.js");
});

page.base('/');

page('/', home);

page('login', auth.login);

page('logout', auth.logout);
page('register', auth.create);

page('account/profile', account.profile.list);
page('account/profile/edit', account.profile.edit);

page('account/token', account.token.list);
page('account/token/add', account.token.add);

page('user', user.list);
page('user/add', user.add);
page('user/:id', user.edit);
page('user/:id/role', user.role);

page('role', role.list);
page('role/add', role.add);
page('role/:id', role.edit);

page('token', token.list);

page('dashboard', dashboard.load);

page('*', error);

page();

// home
function home(ctx) {
    if (ctx.init) {
        logger.i("Current page set to: " + ctx.pathname);
        //Urgent bound of Interceptor
        bindAJAXCallInterceptor();
        //Urgent setup of ajax filter
        setupAjaxCallFiltering();

        //Load header content
//        $.post({url: "/header", beforeSend: function(xhr, settings) {}} , function (data) {
//            $("#header").html(data);
//        });

        $.post("/header", function (data) {
            $("#header").html(data);
        });


        //Load footer content
//        $.post("/footer", function (data) {
//            $("#footer").html(data);
//        });

        //Check for possible page redirections
        if (undefined !== redirectTopage) {
            logger.i("Found redirect-to-page : " + redirectTopage);
            page.redirect(redirectTopage);
            return;
        }

        //Check if user is authenticated immediately redirect to /dashboard
        if (hasAccessToken()) {
            logger.d("User is authenticated, redirecting user to dashboard page")
            page.redirect("/dashboard");
            return;
        }
        //Load home page content
        else
        {
            $.post("/content", function (data) {
                $("#content").html(data);
                $("body").addClass("home");
            });
        }

    } else {
        location.reload();
    }
}

// error
function error(ctx) {
    logger.e("Could not find pathname: " + ctx.pathname);
    logger.d("Current state: " + JSON.stringify(ctx));
}