define(["handlebars"], function(Handlebars) {
    var buildMap = {},
            templateExtension = ".htm";

    return {
        // http://requirejs.org/docs/plugins.html#apiload
        load: function(name, parentRequire, onload, config) {

            // Get the template extension.
            var ext = (config.hbs && config.hbs.templateExtension ? config.hbs.templateExtension : templateExtension);

            if (config.isBuild) {
                // Use Rhino or node.js or file system module to load the template.

                var baseDir = config.dirBaseUrl;

                if (endsWith(baseDir, "/")) {
                    baseDir = baseDir.slice(0, -1);
                }

                var fileName = name + ext;
                var fullPath = baseDir + "/" + fileName;

                if (!exists(fullPath)) {
                    print("template, '" + fullPath + "', not found. Scanning in parent dir.");
                    // Move to parent baseDir and try again
                    baseDir = getParentDir(baseDir);

                    var fullPath = baseDir + "/" + fileName;                                        
                    if (!exists(fullPath)) {
                        throw new Error("Cannot find template, '" + fullPath + "' to precompile!");
                    }
                }

                var fileContent = null;

                if (nodeRequire) {
                    // use node.js
                    var fs = nodeRequire("fs");
                    fileContent = fs.readFileSync(fullPath).toString();
                } else if (readFile) {
                    // use Rhino
                    var file = readFile(fullPath, "utf-8");

                    fileContent = file.toString();

                } else {
                    throw new Error("Cannot read file, '" + fullPath + "', as neither Rhino nor Node.js has been detected! Precompile skipped!");
                }

                buildMap[name] = fileContent;
                onload();
            } else {
                // In browsers use the text-plugin to the load template. This way we
                // don't have to deal with ajax stuff
                if (endsWith(name, ext)) {
                    throw new Error("template '" + name + "' already contains the extension '" + ext + "'! Remove the extension from the path!");
                }
                console.log("loading:", name + ext);
                parentRequire(["text!" + name + ext], function(raw) {
                    // Just return the compiled template
                    var compiled = Handlebars.compile(raw);
                    onload(compiled);
                });
            }

        },
        // http://requirejs.org/docs/plugins.html#apiwrite
        write: function(pluginName, name, write) {
            var compiled = Handlebars.precompile(buildMap[name]);
            // Write out precompiled version of the template function as AMD
            // definition.
            write(
                    "define('hbs!" + name + "', ['handlebars'], function(Handlebars){ \n" +
                    "return Handlebars.template(" + compiled.toString() + ");\n" +
                    "});\n"
                    );
        }
    };

    function endsWith(str, suffix) {
        return str.indexOf(suffix, str.length - suffix.length) !== -1;
    }

    function getParentDir(str) {
        var index = str.lastIndexOf("/");
        if (index > 0) {
            str = str.substring(0, index);
        }
        return str;
    }

});