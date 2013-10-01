define(function(require) {
    var $ = require("jquery");
    var Intro = require("./views/intro/Intro");
    var Home = require("./views/home/Home");
    var footer = require("hb!./views/footer/footer.htm");
    var errorUtils = require("./utils/error-utils");
    var viewManager = require("./utils/view-manager");
    var prettify = require("prettify");
    require("domReady!");

    setupActiveMenu();

    viewManager.setGlobalOnAttached(function() {
        prettify.prettyPrint();
    });

    $("#home").click(function(e) {
        e.preventDefault();
        viewManager.showView(Home);
    });

    $("#intro").click(function(e) {
        e.preventDefault();
        viewManager.showView(Intro);
    });

    $("#menu-home").click(function(e) {
        e.preventDefault();
        viewManager.showView(Home);
        //var link = e.target;
        //setActiveMenu(link);

    });

    $("#menu-intro").click(function(e) {
        e.preventDefault();
        viewManager.showView(Intro);
        //var link = e.target;
        //setActiveMenu(link);
    });

    $("#menu-docs").click(function(e) {
        e.preventDefault();
        viewManager.showView(Intro);
        //var link = e.target;
        //setActiveMenu(link);
    });

    $("#menu-api").click(function(e) {
        e.preventDefault();
        viewManager.showView(Intro);
        //var link = e.target;
        //setActiveMenu(link);
    });

    $("#menu-download").click(function(e) {
        e.preventDefault();
        viewManager.showView(Intro);
        //var link = e.target;
        //setActiveMenu(link);
    });

    /*function setActiveMenu(link) {
     console.log(link);
     $("#navbar a.active").removeClass("active");
     $(link).addClass("active");
     }*/

    function setupActiveMenu() {
        //var offsetLeft = $(homeItem).offset().left - $('#navbar').offset().left;
        //var right = $('#navbar').width() - $(homeItem).width() - offsetLeft;
        //$('#nav-ind').css({right: right});


        $('#navbar li').click(function() {
            if (!$(this).hasClass('active')) {
                $('li.active').removeClass('active');
                slideToActive($(this));
            }
        });
    }

    function slideToActive(li) {
        $(li).addClass('active');
        //var offsetTop = $(li).offset().top - $('#navbar').offset().top;
        var location = getActiveMenuLocation(li);
        $('#nav-ind').animate(location, 'fast', 'linear');
    }

    function getActiveMenuLocation(li) {
        var offsetTop = 50;
        var offsetLeft = $(li).offset().left - $('#navbar').offset().left;
        var location = {
            top: offsetTop,
            left: offsetLeft,
            right: $('#navbar').width() - $(li).width() - offsetLeft,
            bottom: $('#navbar').height() - $(li).height() - offsetTop
        };
        return location;
    }

    viewManager.showView(Home, null, function() {
        /*$("body").show();*/
        // Set first menu item to active
        var homeItem = $("#navbar li:first");
        var location = getActiveMenuLocation(homeItem);
        $("#nav-ind").css(location);
    });
    viewManager.showTemplate(footer, null, "#footer-holder");
    
    
});