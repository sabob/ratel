define(['text', 'handlebars'], function(text, handlebars) {
    function log(args) {
        if (typeof env !== "undefined") {
            print(args);
        }
        console.log(args);
    }

    var buildCache = {};
    var buildCompileTemplate = 'define("{{pluginName}}!{{moduleName}}", ["handlebars"], function(handlebars) {return handlebars.template({{{fn}}})});';
    var buildTemplate;

    var load = function(moduleName, parentRequire, load, config) {

        text.get(parentRequire.toUrl(moduleName), function(data) {
            if (config.isBuild) {
                buildCache[moduleName] = data;
                load();
            } else {
                var tmpl = handlebars.compile(data);
                load(tmpl);
            }
        });
    };

    var write = function(pluginName, moduleName, write) {

        if (moduleName in buildCache) {

            if (!buildTemplate) {
                buildTemplate = handlebars.compile(buildCompileTemplate);
            }

            var tmpl = buildTemplate({
                pluginName: pluginName,
                moduleName: moduleName,
                fn: handlebars.precompile(buildCache[moduleName])
            });

            write(tmpl);
        }
    };

    return {
        load: load,
        write: write
    };
});