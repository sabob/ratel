define(function(require) {
    var $ = require("jquery");
    var ClientSearch = require("./views/client/ClientSearch");
    var ClientSearchNoAnim = require("./views/clientNoAnim/ClientSearch");
    var ClientSearchExp = require("./views/client/ClientSearch2");
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

    $("#clientsExp").click(function(e) {
        e.preventDefault();
        viewManager.showView(ClientSearchExp);
    });

    viewManager.showView(Home);
});