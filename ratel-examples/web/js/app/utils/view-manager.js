define(["jquery", 'domReady!'], function($) {

    function ViewManager() {

        var callStack = [];

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

            };
            view.onInit(dom, args);
        };

        function attachView(target, view, template, attachedComplete, notifyViewReady) {
            $(target).empty();

            $(target).html(template);

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