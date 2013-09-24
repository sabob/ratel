//define(["require", "jquery", "text!./ClientSearch.htm", "../../utils/utils", "domReady!"], function(require, $, template, utils) {
define(function(require) {

    var $ = require("jquery");
    var Handlebars = require("handlebars");
    var moment = require("moment");
    var numeral = require("numeral");
    var template = require("hbs!./ClientSearchAction.htm");
    var html = null;

    var ClientEdit = require("./ClientEdit");
    var utils = require("../../utils/utils");
    var templateEngine = require("../../utils/template-engine");
    var viewManager = require("../../utils/view-manager");
    var errorUtils = require("../../utils/error-utils");
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
                url: "/ratel-examples/clientService/getClients",
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

                dom.attachWithAnim(html, function() {
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
                //attach(template); 
                //that.onAttached();
                //onReady();
            });
        };

        this.action = function(customer) {
            console.log("this.action: customer", customer);
        };

        this.onAttached = function(data) {

            /*
             $("#table").on("click", ".edit-link", function(e) {
             e.preventDefault();
             var id = $(e.target).attr('value');
             console.log("ID", id);
             var args = {id: id, ClientSearch: ClientSearch};
             viewManager.showView(ClientEdit, args);
             });
             
             $("#table").on("click", ".delete-link", function(e) {
             e.preventDefault();
             var index = $(e.target).attr('value');
             var val = confirm('Are you sure?' + index);
             if (val) {
             $("#table #" + index).remove();
             }
             });*/

            console.log("onAttached done");
        };
    }
    
    /*
    if ('action' in Handlebars.helpers) {
        throw new Error('Action, "action" is already registered as a Handlebars helper!');
    }

    Handlebars.registerHelper('action', function(action, options) {
        var id = incrementID();
        var type = options.hash.type || "click";

        var actionRef = action;
        if (typeof actionRef === "string") {
            var target = options.data.target;
            actionRef = target[action];
            if (!utils.exist(actionRef)) {
                actionRef = window[action];
                if (utils.exist(actionRef)) {
                    console.log("The action name, '" + action + "', was found on the window object. It is not advisable to set actions globally!");
                }
            }
        }
        actionRegistry[id] = {
            type: type,
            action: actionRef,
            opions: options,
            objectRef: this
        };

        return new Handlebars.SafeString("data-ratel-action=\"" + id + "\"");
    });

    var actionRegistry = {
        id: 0
    };

    function incrementID() {
        return ++actionRegistry.id;
    }

    function bindIDs() {
        $("[data-ratel-action]").each(function() {
            var currentID = this.attributes["data-ratel-action"].value;
            var currentAction = actionRegistry[currentID];

            $(this).bind(currentAction.type, function() {
                currentAction.action(currentAction.objectRef);
            });
        });
    }*/


    return ClientSearch;
});