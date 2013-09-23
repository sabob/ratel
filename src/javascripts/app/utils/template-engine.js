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

        this.reset = function() {
            actionRegistry = [];
            console.log("TemplatEngine was reset!");
        };

        this.render = function(template, context, options) {
            //var tmpl = Handlebars.compile(template);
            var html = template(context, options);
            return html;
        };

        // TODO pass in a selector and use LIVE
        this.bind = function() {
            $("[data-ratel-action]").each(function() {
                var currentID = this.attributes["data-ratel-action"].value;
                var currentAction = actionRegistry[currentID];

                $(this).bind(currentAction.type, function(e) {
                    currentAction.action(e, currentAction.objectRef);
                });
            });
        };

        this.registerHelpers = function() {
            if (registered) {
                return;
            }
            registered = true;

            checkHelper('action');
            Handlebars.registerHelper('action', function(action, options) {
                //var id = incrementID();
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
                    opions: options,
                    objectRef: this
                };
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