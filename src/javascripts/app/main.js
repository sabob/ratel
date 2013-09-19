define(function(require) {
    var $ = require("jquery");
    var Intro = require("./views/intro/Intro");
    var Home = require("./views/home/Home");
    var viewManager = require("./utils/view-manager");
    var prettify = require("prettify");
    require("domReady!");
    
    viewManager.setOnAttached(function() {
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

    viewManager.showView(Home);
});