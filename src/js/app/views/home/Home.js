define(function(require) {
    var $ = require("jquery");
    var template = require("hb!./Home.htm");
    var bobPartial = require("hb!./BobPartial.htm");
    var utils = require("../../utils/utils");
    var Handlebars = require("handlebars");
        var te = require("../../utils/template-engine");
    var viewManager = require("../../utils/view-manager");
    require("domReady!");

    function Home() {

        // private variables
        var that = this;

        // priviledged methods

        this.onInit = function(dom, args) {
                Handlebars.registerPartial("bobPartial", bobPartial);
            //onReady();
            var context = {'name': 'Bob'};
            var options = {
                bindtarget: "#container",
                data : {
                    one: "two",
                    testAction: function(e, context, options) {
                        e.preventDefault();
                        //console.log("Hi Bob!" + Date.now());
                        console.log("context:", context, "options:", options);
                    }
                }
            };
            var html = te.render(this.getTemplate(), context, options);

            dom.attachWithAnim(html, function() {
               //te.bind("#cont");
                //te.bind();
                //te.bind();
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
