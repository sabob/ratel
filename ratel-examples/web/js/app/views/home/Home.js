define(function(require) {
    var $ = require("jquery");
    var template = require("text!./Home.htm");
    var ClientSearch = require("../client/ClientSearch");
    var utils = require("../../utils/utils");
    var viewManager = require("../../utils/view-manager");
    require("domReady!");

    function Home() {

        // private variables
        var that = this;

        // priviledged methods

        this.onInit = function(dom, args) {
            //onReady();
            dom.attachWithAnim(this.getTemplate(), function() {
                onAttached(args);
                });
        };

        this.getTemplate = function() {
            return template;
        };

        // private methods
        function onAttached(args) {
            $("#manageClients").click(function(evt) {
                viewManager.showView(ClientSearch);
            });

            $("#manageProducts").click(function(evt) {
            });

            $("#manageServices").click(function(evt) {
            });

            $("#manageOpportunities").click(function(evt) {
            });
        }
    }



    return Home;

});
