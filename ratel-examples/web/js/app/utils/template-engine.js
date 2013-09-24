define(function(require) {
    var $ = require("jquery");
    var utils = require("./utils");
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
                    $(container).off(type, "[data-ratel-action]");
                }*/
            //}
            actionRegistry = [];
            //delete actionRegistryTypes[target];
            console.log("TemplateEngine was reset!");
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
                $(target).off(type, "[data-ratel-action]");
                $(target).on(type, "[data-ratel-action]", function(e) {
                    var currentID = this.attributes["data-ratel-action"].value;
                    //console.log(currentID);
                    var currentAction = actionRegistry[currentID];
                    if (type !== currentAction.type) {
                        return;
                    }
                    currentAction.action(e, currentAction.objectRef);
                });
            }
        }*/

        this.bind = function(target) {
            target = target || "body";
            console.log("binding target:", target);
            
            // Select target with data-ratel-attributge and all children with data-ratel-attributge, hence the ',' in the selector below
            $(target + "[data-ratel-action], " + target + " [data-ratel-action]").each(function() {
                var currentID = this.attributes["data-ratel-action"].value;
                // TODO remove data-ratel-acion
                var currentAction = actionRegistry[currentID];
                
                //$(this).off(currentAction.type);
                $(this).on(currentAction.type, function(e) {
                    currentAction.action(e, currentAction.objectRef, currentAction.options);
                });
                // remove the action attribute
                $(this).removeAttr("data-ratel-action");
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
                var actionData = {
                    type: type,
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
                container[type] = null;
                */
                
                var length = actionRegistry.push(actionData);
                var id = length - 1;

                return new Handlebars.SafeString("data-ratel-action=\"" + id + "\"");
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