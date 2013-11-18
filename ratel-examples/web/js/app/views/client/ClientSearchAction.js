//define(["require", "jquery", "text!./ClientSearch.htm", "../../utils/utils", "domReady!"], function(require, $, template, utils) {
define(function(require) {

    var $ = require("jquery");
    var Handlebars = require("handlebars");
    var moment = require("moment");
    var numeral = require("numeral");
    var template = require("hb!./ClientSearchAction.htm");
    var html = null;

    var ClientEdit = require("./ClientEdit");
    var utils = require("spamd/utils/utils");
    var templateEngine = require("spamd/template/template-engine");
    var viewManager = require("spamd/view/view-manager");
    var errorUtils = require("spamd/utils/error-utils");
    require("domReady!");

    function ClientSearch() {

        var that = this;

        this.getHTML = function() {
            return html;
        };

        this.getTemplate = function() {
            return template;
        };

        this.onInit = function(dom, args) {
            var request = $.ajax({
                url: "/ratel-examples/service/clientservice/clients",
                type: "GET",
                dataType: "json"
                        //contentType: "application/json"
            });

            request.done(function(data, textStatus, jqXHR) {
                console.log("clients fetched", data);

                //var tmpl = Handlebars.compile(template);
                var context = {
                    "customers": data,
                    action: function(customer) {
                        console.log("inline LOCAL action: customer", customer);
                    }
                };
                
                var moo = {
                    pok: "bobby"
                };
                var options = {};
                options.data = {
                    target: that,
                            moo: moo,
                     action: function(customer) {
                        console.log("inline GLOBAL action: customer", customer);
                    }
                };
                //html = tmpl(context, options);
                html = templateEngine.render(template, context, options);

                dom.attach(html).then( function() {
                    that.onAttached(data);
                    templateEngine.bind();
                    //html = tmpl({"customers2": [{"firstname": "MOOO"}]});
                });
            });

            request.fail(function(jqXHR, textStatus, errorThrown) {
                console.log("Request failed: " + textStatus);
                dom.stay();
                var text = jqXHR.responseText;
                errorUtils.showError(text);
            });
            request.always(function(arg1, textStatus, arg3) {
                console.log("Request completed: ", textStatus);
            });
        };

        this.action = function(customer) {
            console.log("this.action: customer", customer);
        };

        this.onAttached = function(data) {
            console.log("onAttached done");
        };
    }

    return ClientSearch;
});