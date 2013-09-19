define(function(require) {
    var $ = require("jquery");
    var template = require("text!./Home.htm");
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

/*
            $("#manageProducts").click(function(evt) {
            });

            $("#manageServices").click(function(evt) {
            });

            $("#manageOpportunities").click(function(evt) {
            });*/
        }
    }



    return Home;

});
