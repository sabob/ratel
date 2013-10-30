define(function(require) {
    var $ = require("jquery");
    var ClientSearch = require("./views/client/ClientSearch");
    var ClientSearchNoAnim = require("./views/clientNoAnim/ClientSearch");
    var ClientSearchMoment = require("./views/client/ClientSearchMoment");
    var ClientSearchAction = require("./views/client/ClientSearchAction");
    var ClientSearch = require("./views/client/ClientSearch");
    var Home = require("./views/home/Home");
    var viewManager = require("spamd/view/view-manager");
    require("domReady!");

    $("#home").click(function(e) {
        e.preventDefault();
        viewManager.showView({view:Home});
    });

    $("#clients").click(function(e) {
        e.preventDefault();
        viewManager.showView({view:ClientSearch});
    });

    $("#clientsNoAnim").click(function(e) {
        e.preventDefault();
        viewManager.showView({view:ClientSearchNoAnim});
    });

    $("#clientsMoment").click(function(e) {
        e.preventDefault();
        viewManager.showView({view:ClientSearchMoment});
    });
    
    $("#clientsAction").click(function(e) {
        e.preventDefault();
        viewManager.showView({view:ClientSearchAction});
    });

    viewManager.showView({view:Home});
});