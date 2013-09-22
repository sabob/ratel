define(['text', 'handlebars'], function(text, handlebars) {

    var buildCache = {};
    var buildCompileTemplate = 'define("{{pluginName}}!{{moduleName}}", ["handlebars"], function(handlebars) {return handlebars.template({{{fn}}})});';
    var buildTemplate;

    // When in Node, always get the full version of Handlebars
    if (typeof process !== "undefined" && process.versions && !!process.versions.node) {
        handlebars = require.nodeRequire('handlebars') || handlebars;
        //print("OH NO:" + handlebars);
    } else {
        //print("OH YES:" + handlebars);
    }

    var load = function(moduleName, parentRequire, load, config) {
        console.log("GET:" + parentRequire.get);
        var url = parentRequire.toUrl('handlebars.js');

        //parentRequire(['handlebars-build'], function(d) {
        //handlebars = d;
        //console.log("D:" + d.compile);
        //print(d.precompile);
        //print(handlebars.precompile);
        text.get(parentRequire.toUrl(moduleName), function(data) {
            if (config.isBuild) {
                buildCache[moduleName] = data;
                load();
            } else {
                load(handlebars.compile(data));
            }

        });
        //});

    };

    var write = function(pluginName, moduleName, write) {

        if (moduleName in buildCache) {

            if (!buildTemplate) {
                buildTemplate = handlebars.compile(buildCompileTemplate);
            }
            //print(buildTemplate);

            write(buildTemplate({
                pluginName: pluginName,
                moduleName: moduleName,
                fn: handlebars.precompile(buildCache[moduleName])
            }));
        }
    };

    return {
        load: load,
        write: write
    };
});