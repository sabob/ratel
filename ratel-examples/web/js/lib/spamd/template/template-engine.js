define(function(require) {
    var $ = require("jquery");
    require("jquery.address");
    var utils = require("../utils/utils");
    var Handlebars = require("handlebars");
    var moment = require("moment");
    var numeral = require("numeral");
    require("domReady!");

    function TemplateEngine() {

        var registered = false;

        var actionRegistry = [];
        this.hasActions = function() {
            return actionRegistry.length > 0;
        };
        //var actionRegistryTypes = {};
        //var mostRecentTarget = null;

        this.reset = function() {
            //target = target || "#container";

            //for (var container in actionRegistryTypes) {
            /*
            var container = actionRegistryTypes[target];
                for (var type in container.types) {
                    $(container).off(type, "[data-kv-action]");
                }*/
            //}
            actionRegistry = [];
            //delete actionRegistryTypes[target];
            //console.log("TemplateEngine was reset!");
        };

        this.render = function(template, context, options) {
            //var tmpl = Handlebars.compile(template);
            //var target = options.bindtarget;
            //target = target || "#container";
            //mostRecentTarget = target;

            var html = template(context, options);

            //mostRecentTarget=null;

            //if (options.bind === false) {
              //  return html;
            //}

            //autobind(target);
            return html;
        };
/*
        function autobind(target) {

            var container = actionRegistryTypes[target];
            console.log("container:", container, "target:", target);
            for (var type in container) {
                console.log("type:", type);
                $(target).off(type, "[data-kv-action]");
                $(target).on(type, "[data-kv-action]", function(e) {
                    var currentID = this.attributes["data-kv-action"].value;
                    //console.log(currentID);
                    var currentAction = actionRegistry[currentID];
                    if (type !== currentAction.on) {
                        return;
                    }
                    currentAction.action(e, currentAction.objectRef);
                });
            }
        }*/

        this.bind = function(target) {
            target = target || "body";
            //console.log("binding target:", target);
            
            // Select target with data-kv-attribute and all children with data-kv-attribute, hence the ',' in the selector below. Note the
            // space between the target and data-attribute.
            $("[data-kv-action]", target).addBack("[data-kv-action]").each(function(i, item) {
                var currentID = this.attributes["data-kv-action"].value;
                // TODO remove data-kv-acion
                var currentAction = actionRegistry[currentID];
                
                //$(this).off(currentAction.on);
                var node = $(this);
                node.on(currentAction.on, function(e) {
                    if (currentAction.on === "click") {
                        //e.preventDefault();
                    }
                   currentAction.action(e, currentAction.objectRef, currentAction.options); 
                 /*
                    var tagName = node.prop("tagName").toLowerCase();
                    var isAnchor = 'a' === tagName;
                    if (isAnchor) {
                        var href = node.attr('href');
                        //$.address.value(href);
                        e.preventDefault();
                        //console.log(href);
                    }
                    */
                });
                // remove the action attribute
                node.removeAttr("data-kv-action");
            });
            
            this.reset();
            
        };

        this.registerHelpers = function() {
            if (registered) {
                return;
            }
            registered = true;

            checkHelper('action');
            Handlebars.registerHelper('action', function(action, options) {
                var on = options.hash.on || "click";

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
                var actionData = {
                    on: on,
                    action: actionRef,
                    options: options,
                    objectRef: this
                };
                /*
                var target = mostRecentTarget;
                console.log("target", target);
                var container = actionRegistryTypes[target];
                if (!container) {
                    container = {};
                    actionRegistryTypes[target] = container;
                }
                container[on] = null;
                */
                
                var length = actionRegistry.push(actionData);
                var id = length - 1;

                return new Handlebars.SafeString("data-kv-action=\"" + id + "\"");
            });

            checkHelper('formatDate');
            Handlebars.registerHelper('formatDate', function(context, block) {
                //console.log("dateFormat", context);

                var f = block.hash.format || "MMM Do, YYYY";
                //console.log(f, " ", context);
                //var c = moment(context);
                //console.log("val", c);
                //c = moment.utc(c).local();
                //context = 18376333;
                //var day = moment.unix(context);
                //var day = moment(12345678900);
                //var day = moment("1977-01-01T00:00:00.000+0000");
                var day = moment(context);
                return day.format(f);
            });

            checkHelper('formatNumber');
            Handlebars.registerHelper('formatNumber', function(context, block) {
                //console.log("numberFormat ", context);

                var f = block.hash.format || "#";
                //console.log(f, " ", context);
                //var c = moment(context);
                //console.log("val", c);
                //c = moment.utc(c).local();
                //context = 18376333;
                //var day = moment.unix(context);
                //var day = moment(12345678900);
                //var day = moment("1977-01-01T00:00:00.000+0000");
                var str = numeral(context).format(f);
                return str;

            });

            function checkHelper(name) {
                if (name in Handlebars.helpers) {
                    throw new Error('Helper, "' + name + '" is already registered as a Handlebars helper!');
                }
            }

        };
    }

    var engine = new TemplateEngine();
    engine.registerHelpers();

    return engine;
});