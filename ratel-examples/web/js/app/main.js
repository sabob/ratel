define(function(require) {
    var $ = require("jquery");
    var ClientSearch = require("./views/client/ClientSearch");
    var ClientSearchNoAnim = require("./views/clientNoAnim/ClientSearch");
    var ClientSearchMoment = require("./views/client/ClientSearchMoment");
    var ClientSearchAction = require("./views/client/ClientSearchAction");
    var ClientSearch = require("./views/client/ClientSearch");
    var Home = require("./views/home/Home");
    var viewManager = require("./utils/view-manager");
    require("jquery.alpha");
    require("jquery.beta");
    require("domReady!");

    $("#home").click(function(e) {
        e.preventDefault();
        viewManager.showView(Home);
    });

    $("#clients").click(function(e) {
        e.preventDefault();
        viewManager.showView(ClientSearch);
    });

    $("#clientsNoAnim").click(function(e) {
        e.preventDefault();
        viewManager.showView(ClientSearchNoAnim);
    });

    $("#clientsMoment").click(function(e) {
        e.preventDefault();
        viewManager.showView(ClientSearchMoment);
    });
    
    $("#clientsAction").click(function(e) {
        e.preventDefault();
        viewManager.showView(ClientSearchAction);
    });

    viewManager.showView(Home);
});