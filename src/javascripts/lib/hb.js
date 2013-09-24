define(['text', 'handlebars'], function(text, handlebars) {
    function log(args) {
        if (typeof env !== "undefined") {
            print(args);
        }
        console.log(args);
    }

    var buildCache = {};
    //var templateExtension = ".htm";
    var buildCompileTemplate = 'define("{{pluginName}}!{{moduleName}}", ["handlebars"], function(handlebars) {return handlebars.template({{{fn}}})});';
    var buildTemplate;

    var load = function(moduleName, parentRequire, load, config) {
        // Get the template extension.
        //var ext = (config.hb && config.hb.templateExtension ? config.hb.templateExtension : templateExtension);

        /*if (endsWith(moduleName, ext)) {
            throw new Error("template '" + moduleName + "' already contains the extension '" + ext + "'! Remove the extension from the path!");
        }*/
        //var fullName = moduleName + ext;
        var fullName = moduleName;

        text.get(parentRequire.toUrl(fullName), function(data) {
            if (config.isBuild) {
                buildCache[fullName] = data;
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

    function endsWith(str, suffix) {
        return str.indexOf(suffix, str.length - suffix.length) !== -1;
    }

    return {
        load: load,
        write: write
    };
});