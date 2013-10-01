define(function(require) {
    var $ = require("jquery");
    require("domReady!");
    var templateEngine = require("./template-engine");

    function ViewManager() {

        var callStack = {};
        
        var globalOnAttached = null;
        
        this.setGlobalOnAttached = function(callback) {
            globalOnAttached = callback;
        };

        this.showView = function(View, args, notifyViewReady, target) {
            target = target || "#container";

            // TODO setup window.error
            var curr = window.onerror;
            window.onerror = function(message, file, lineNumber) {
                popCallStack(target);
                if (curr) {
                    curr(arguments);
                    window.onerror = curr;
                }
                return false;
            };
            
            if (typeof(callStack[target]) === 'undefined') {
                callStack[target] = [];
            }

            if (callStack[target].length !== 0) {
                console.log('We are already busy processing a showView request', callStack[target]);
                return;
            }
            
            if (templateEngine.hasActions()) {
                console.log("It's been detected that there are unbounded actions in the TemplateEngine! Resetting to remove memory leaks!")
                templateEngine.reset(target);
            }

            callStack[target].push(1);


            var view = new View();

            var dom = new function Dom() {
                this.attach = function(template, done) {
                    attachView(target, view, template, done, notifyViewReady);
                    return this;
                };

                this.attachWithAnim = function(template, done) {
                    attachViewWithAnim(target, view, template, done, notifyViewReady);
                };
                
                this.stay = function() {
                    popCallStack(target);
                };
            };

            if (!view.onInit) {
                throw new Error("Views must have a public 'onInit' method!");
            }
            view.onInit(dom, args);
        };
        
        this.showTemplate = function(template, notifyTemplateReady, target) {
            target = target || "#container";

            // TODO setup window.error
            var curr = window.onerror;
            window.onerror = function(message, file, lineNumber) {
                popCallStack(target);
                if (curr) {
                    curr(arguments);
                    window.onerror = curr;
                }
                return false;
            };
            
            if (typeof(callStack[target]) === 'undefined') {
                callStack[target] = [];
            }

            if (callStack[target].length !== 0) {
                console.log('We are already busy processing a showTemplate request', callStack[target]);
                return;
            }
            
            if (templateEngine.hasActions()) {
                console.log("It's been detected that there are unbounded actions in the TemplateEngine! Resetting to remove memory leaks!")
                templateEngine.reset(target);
            }

            callStack[target].push(1);
            
            var view = null;
            var attachedComplete = null;
            attachViewWithAnim(target, view, template, attachedComplete, notifyTemplateReady);
        };
        
        function popCallStack(target) {
            callStack[target].pop();
            if (callStack[target].length === 0) {
                delete callStack[target];
            }
        }

        function attachView(target, view, template, attachedComplete, notifyViewReady) {
            $(target).empty();

            $(target).html(template);
            if (globalOnAttached) {
                globalOnAttached();
            }

            if (attachedComplete) {
                attachedComplete();
            }

            if (notifyViewReady) {
                notifyViewReady(view);
            }

            popCallStack(target);
        }

        function attachViewWithAnim(target, view, template, attachedComplete, notifyViewReady) {
            $(target).fadeOut('fast', function() {

                $(target).empty();

                $(target).html(template);
                
                if (globalOnAttached) {
                globalOnAttached();
            }

                if (attachedComplete) {
                    attachedComplete();
                }

                $(target).fadeIn('fast', function() {
                    if (notifyViewReady) {
                        notifyViewReady(view);
                    }
                    popCallStack(target);
                });
            });
        }
    }
    return new ViewManager();
});