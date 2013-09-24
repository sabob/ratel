define(function(require) {
    var $ = require("jquery");
    require("domReady!");
    var templateEngine = require("./template-engine");

    function ViewManager() {

        var callStack = [];
        
        var globalOnAttached = null;
        
        this.setGlobalOnAttached = function(callback) {
            globalOnAttached = callback;
        };

        this.showView = function(View, args, notifyViewReady, target) {
            target = target || "#container";

            // TODO setup window.error
            var curr = window.onerror;
            window.onerror = function(message, file, lineNumber) {
                callStack.pop();
                if (curr) {
                    curr(arguments);
                    window.onerror = curr;
                }
                return false;
            };

            if (callStack.length !== 0) {
                console.log('We are already busy processing a showView request', callStack);
                return;
            }
            
            if (templateEngine.hasActions()) {
                console.log("It's been detected that there are unbounded actions in the TemplateEngine! Resetting to remove memory leaks!")
                templateEngine.reset(target);
            }

            callStack.push(1);


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
                    callStack.pop();
                };

            };
            view.onInit(dom, args);
        };

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

            callStack.pop();
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
                    callStack.pop();
                });
            });
        }
    }
    return new ViewManager();
});