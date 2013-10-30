define(function(require) {
    window.onerror = function MOO() {
        console.log("MOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    }
    var $ = require("jquery");
    require("domReady!");
    require("jquery.address");
    var utils = require("../utils/utils");
    var templateEngine = require("../template/template-engine");
    function ViewManager() {

        var that = this;
        var currentView = {
            view: null,
            options: null
        };
        var processHashChange = true;
        var initialized = false;
        var routesByName = {};
        var routesByPath = {};
        var callStack = {};
        var errorHandlerStack = [];
        var globalOnAttached = null;
        this.setRoutes = function(map) {
            if (!map) {
                return;
            }

            routesByName = map;
            routesByPath = {};
            for (var prop in routesByName) {
                routesByPath[routesByName[prop]] = prop;
            }
            //console.log("routesByPath", routesByPath);
        };
        this.init = function(options) {
            if (initialized) {
                // If no options are specified, and view-manager has been initialized before, we can skip initialization
                if (!options) {
                    return;
                }
            }

            initialized = true;
            var defaultView;
            if (options) {
                defaultView = options.defaultView;
                var routes = options.routes;
                //console.log("setting Routes", routes);
                this.setRoutes(routes);
            }

            $.address.strict(false);
            //$.address.autoUpdate(false);
            //$.address.change(function(event) {
            $.address.change(function(event) {
                //event.preventDefault();
                //event.stopPropagation();

                //console.log("E:", event);
                if (processHashChange) {
                    var viewName = event.path;
                    //console.log("name", name);
                    //console.log("hash", $.address.hash());
                    //console.log("path", $.address.path());
                    //console.log("value", $.address.value());
                    //console.log("parameterNames", $.address.parameterNames());
                    var viewPath = routesByName[viewName];
                    if (!viewPath) {
                        viewPath = viewName;
                    }
                    //console.log("address.change ViewName", viewName);

                    if (viewPath) {// ensure path is not blank
                        //if (name !== event.path) { // ensure we don't process path twice
                        //console.log("name", viewPath, "event.path", event.path);
                        var params = event.parameters;
                        //console.log("URL PArams", params);
                        that.showView({view: viewPath, params: params});
                        //}
                    }
                }
            });
            //console.log("address path", $.address.path());
            if ($.address.path()) {
                //viewManager.showView({view: Home});
                //$.address.value($.address.value());
                //console.log("updating");
                $.address.update();
            } else {

                if (defaultView) {
                    options.view = defaultView;
                    this.showView(options);
                    //$.address.value(Home.id);
                    //$.address.update();
                }
            }
        };
        this.setGlobalOnAttached = function(callback) {
            globalOnAttached = callback;
        };
        this.showView = function(options) {
            var view = options.view;
            if (!view) {
                throw new Error("options.view must be specified");
            }

            this.init();
            //var args = options.args;
            //var params = options.params;

            //var onViewReady = options.onViewReady;
            var target = options.target || "#container";
            // Make copy
            var defaults = {};
            defaults = $.extend({}, defaults, options);
            defaults.target = target;
            defaults._options = options;

            // Setup global error handler in case user doen't use try/catch logic
            addGlobalErrorHandler(target);

            if (typeof (callStack[target]) === 'undefined') {
                callStack[target] = [];
            }

            if (callStack[target].length !== 0) {
                console.log("ViewManager is  already processing a showView/showHTML request for the target '" + target + "'. Use ViewManager.clear('" + target + "') to force a showView/showHTML request.", callStack[target]);
                var deferred = $.Deferred();
                deferred.reject();
                return deferred.promise();
            }

            if (templateEngine.hasActions()) {
                console.log("It's been detected that there are unbounded actions in the TemplateEngine! Make sure to call templateEngine.bind() after template is added to DOM. Resetting to remove memory leaks!");
                templateEngine.reset(target);
            }

            callStack[target].push(1);
            if (typeof view === 'string') {
                var View = require(view);
                defaults.view = new View();
            } else if (view instanceof Function) {
                defaults.view = new view();
            }

            options.viewInstance = defaults.view;
            var mainDeferred = $.Deferred();
            defaults.mainDeferred = mainDeferred;
            //setTimeout(function() {
            that.showViewInstance(defaults);
            //});

            return mainDeferred.promise();
        };

        this.showViewInstance = function(options) {
            var view = options.view;
            setCurrentView(view, options);
            var viewPath = view.id;
            var args = options.args;
            var params = options.params;
            //var onViewReady = options.onViewReady;
            //var target = options.target;

            processHashChange = false;
            var route = routesByPath[viewPath] || viewPath;
            $.address.autoUpdate(false);
            $.address.value(route);
            for (var param in params) {
                var val = params[param];
                if ($.isArray(val)) {
                    for (var i = 0; i < val.length; i++) {
                        var item = val[i];
                        $.address.parameter(param, item, true);
                    }
                } else {
                    $.address.parameter(param, val);
                }
            }
            $.address.autoUpdate(true);
            $.address.update();
            processHashChange = true;
            //$.address.value(viewName);
            //route[viewName] = arguments;
            //$.address.parameter("pok", "moo");
            //console.log("param", $.address.parameter("pok"));


            var dom = new function Dom() {
                this.attach = function(html, domOptions) {
                    var domDefaults = {anim: true};
                    domDefaults = $.extend({}, domDefaults, domOptions);
                    var deferred = $.Deferred();
                    setTimeout(function() {
                        var onAttached = function() {
                            //that.clear(options.target);
                            deferred.resolve();
                            // In case user forgot to bind. TODO this call could be slow if DOM is large, so make autobind configurable
                            if (templateEngine.hasActions()) {
                                console.info("autobinding template actions since templateEngine has unbounded actions!");
                                // TODO shoulld we auto bind at all?? Simply warn the user?
                                templateEngine.bind(options.target);
                            }
                        };
                        options.onAttached = onAttached;
                        if (domDefaults.anim) {
                            that.attachViewWithAnim(html, options);
                        } else {
                            that.attachView(html, options);
                        }

                    });
                    return deferred.promise();
                    //return this;
                };

                this.stay = function() {
                    var stayDeferred = $.Deferred();
                    setTimeout(function() {
                        var target = options.target;
                        that.clear(target);
                        stayDeferred.resolve();
                    });
                    return stayDeferred.promise();
                };
            };
            if (!view.onInit) {
                throw new Error("Views must have a public 'onInit' method!");
            }
            var initOptions = {args: args, params: params};
            view.onInit(dom, initOptions);
        };

        this.showHTML = function(options) {
            this.init();
            var target = options.target || "#container";
            var defaults = {anim: true};
            defaults = $.extend({}, defaults, options);
            defaults._options = options;
            defaults.target = target;
            // TODO setup window.error
            addGlobalErrorHandler(target);
            /*
             var curr = window.onerror;
             window.onerror = function(message, file, lineNumber) {
             that.clear(target);
             if (curr) {
             curr(arguments);
             window.onerror = curr;
             }
             return false;
             };*/

            if (typeof (callStack[target]) === 'undefined') {
                callStack[target] = [];
            }

            if (callStack[target].length !== 0) {
                console.log("ViewManager is already processing a showView/showHTML request for the target '" + target + "'. Use ViewManager.clear('" + target + "') to force a showView/showHTML request.", callStack[target]);
                var deferred = $.Deferred();
                deferred.reject();
                return deferred.promise();
            }

            if (templateEngine.hasActions()) {
                //console.warn("It's been detected in showHTML that there are unbounded actions in the TemplateEngine! Make sure to call templateEngine.bind() after template is added to DOM. Resetting to remove memory leaks!");
                //templateEngine.reset(target);
            }

            callStack[target].push(1);
            var deferred = $.Deferred();
            setTimeout(function() {
                var onAttached = function() {
                    //that.clear(options.target);
                    deferred.resolve();
                    // In case user forgot to bind. TODO this call could be slow if DOM is large, so make autobind configurable
                    if (templateEngine.hasActions()) {
                        //console.info("autobinding template actions since templateEngine has unbounded actions!");
                        //templateEngine.bind(target);
                    }
                };
                var html = defaults.html;
                defaults.onAttached = onAttached;
                if (defaults.anim) {
                    that.attachViewWithAnim(html, defaults);
                } else {
                    that.attachView(html, defaults);
                }

            });
            //var view = null;
            //var onAttached = null;
            //var options = {};
            //options.view = template;
            //options.target = target;
            //this.attachViewWithAnim(target, view, template, onAttached, notifyTemplateReady);
            //this.attachViewWithAnim(template, options);

            return deferred.promise();
        };
        this.attachView = function(html, options) {
            var target = options.target;
            $(target).empty();
            $(target).html(html);
            that.viewAttached(options);
            that.viewComplete(options);
        };
        this.viewAttached = function(options) {
            var onAttached = options.onAttached;
            if (globalOnAttached) {
                var origOptions = options._options;
                globalOnAttached(origOptions);
            }

            if (onAttached) {
                onAttached();
            }

            // In case user forgot to bind. TODO this call could be slow if DOM is large, so make autobind configurable
            /*if (templateEngine.hasActions()) {
             console.info("autobinding template actions since templateEngine has unbounded actions!");
             templateEngine.bind(target);
             }*/
        };
        this.viewComplete = function(options) {

            var target = options.target;
            var view = options.view;
            //var onViewReady = options.onViewReady;
            var mainDeferred = options.mainDeferred;
            if (mainDeferred) {
                mainDeferred.resolve(view);
            }
            that.clear(target);
            removeGlobalErrorHandler(target);
        };
        // TODO Replace this method for alternate animation
        this.attachViewWithAnim = function(html, options) {

            var target = options.target;
            console.log("T:", target);
            $(target).fadeOut('fast', function() {

                $(target).empty();
                $(target).html(html);
                that.viewAttached(options);
                $(target).fadeIn('fast', function() {
                    that.viewComplete(options);
                });
            });
        };
        this.clear = function(target) {
            var obj = callStack[target];
            if (obj) {
                obj.pop();
                if (obj.length === 0) {
                    delete callStack[target];
                }
            }
        };
        this.getCurrentView = function() {
            if (currentView) {
                return currentView.view;
            }
            return null;
        };
        function setCurrentView(view, options) {
            if (currentView.view && currentView.view.onDestroy) {

                currentView.onDestroy(currentView.options);
            }
            currentView = {view: view, options: options};
        }

        function removeGlobalErrorHandler(target) {
            console.log("globalErrorHandler removing", target);
            var i = $.inArray(target, errorHandlerStack);
            if (i !== -1) {
                errorHandlerStack.splice(i, 1);
                console.log("globalErrorHandler removed", target);
            }
        }

        function addGlobalErrorHandler(target) {
            console.log("addGlobalErrorHandler", target);
            var i = $.inArray(target, errorHandlerStack);
            if (i !== -1) {
                return;
            }

            if (errorHandlerStack.length >= 1) {
                errorHandlerStack.push(target);
                console.log("addGlobalErrorHandler already present", target);
                return;
            }

            errorHandlerStack.push(target);
            if (window.onerror === globalErrorHandler) {
                console.log("globalErrorHandler is already se as window.onerror");
                return;
            }

            var prevError = window.onerror;
            globalErrorHandler.prevError = prevError;
            window.onerror = globalErrorHandler;
        }

        function globalErrorHandler(message, url, lineNumber) {
            console.log("Global called");
            console.log("Old error", globalErrorHandler.prevError);
            for (var i = 0; i < errorHandlerStack.length; i++) {
                var target = errorHandlerStack[i];
                targetErrorHandler(message, url, lineNumber, target);
            }
            
            var prevError = globalErrorHandler.prevError;

            if (prevError) {
                //window.onerror = currentError;
                prevError(message, url, lineNumber);
                console.log("Current error called " + target);
            }
        }
        
        function targetErrorHandler(message, url, lineNumber, target) {
                console.log("targetErrorHandler for " + target, message, url, lineNumber);
                that.clear(target);
                $(target).finish();
                $(target).clearQueue().stop(true, true);
                setTimeout(function() {
                    $(target).css({'opacity': '1', 'display': 'block'});
                }, 10);
                return false;
            }
    }

    var manager = new ViewManager();
    //manager.init();
    return manager;
});