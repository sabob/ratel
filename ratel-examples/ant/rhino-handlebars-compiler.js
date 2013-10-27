importPackage(java.io);

function scanDir(dir,dirHandler) {
      var lst=new File(dir).listFiles();
      var i;
      for(i=0;i<lst.length;i++) {
            // If it's a directory, recursive call scanDir()
            // so that we end up doing a scan of
            // the directory tree
         if(lst[i].isDirectory()) {
            scanDir(lst[i].getAbsolutePath(),dirHandler);
         }
            // Pass the File object to the handler that
            // the caller has specified regardless of whether
            // the File object is a directory.
         dirHandler(lst[i]);
      }
   }

(function(args) {
    var templateFileExtension = 'htm',
            output = ['// This file is auto-generated and should be ignored from version control.\n'],
            console = {
        log: print
    },
    showUsage = function() {
        console.log('Usage: java -jar <rhino.jar> rhino-handlebars-compiler.js --handlebars <handlebars library path> --templates <templates directory> --output <output file>');
    },
            templatesDirectory,
            outputFile,
            templateFiles,
            outStream,
            index,
            templateFile,
            templateContents,
            argumentsParser,
            options;

    argumentsParser = function() {
        var arg, parse = function(args) {
            var options = {};
            args = Array.prototype.slice.call(args);
            arg = args.shift();
            while (arg) {
                if (arg.indexOf("--") === 0) {
                    options[arg.substring(2)] = args.shift();
                }
                arg = args.shift();
            }
            return options;
        };

        return {parse: parse};
    };

    options = new argumentsParser().parse(args);
    handlebarsLibrary = options.handlebars;
    templatesDirectory = options.templates;
    outputFile = options.output;
    
    var templateFiles = [];
    scanDir(templatesDirectory, function(file) {
        //print(file.getName());
        //var ext = file.getName().substr(-11);
        //print(ext);
        var ext = "." + templateFileExtension;
        if (file.getName().endsWith(ext)) {
            templateFiles.push(file);
        }
    });

    //templateFiles = new File(templatesDirectory).listFiles();
    outStream = new BufferedWriter(new FileWriter(outputFile));

    if (undefined === handlebarsLibrary || undefined === templatesDirectory) {
        showUsage();
        java.lang.System.exit(1);
    }
    load(handlebarsLibrary);

    output.push('(function(){');
    output.push('\n var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};\n');

    //templateFiles = templateFiles.filter(function(fileName) {
 //       return(fileName.getName().substr(-11) === ("." + templateFileExtension));
 //   });
 //Handlebars.registerHelper('mommy', function() {
     //console.log("MOOPOK");
     //
 //});
 var options = {};
 var helpers = {};
 options.helpers = helpers;

    for (index = 0; index < templateFiles.length; index++) {
        templateFile = templateFiles[index];
        print("precompiling: " + templateFile.getName());
        templateName = templateFile.getName().replaceAll(('\\.' + templateFileExtension + '$'), '');
        var fileContent = readFile(templateFile.getAbsolutePath());

        templateContents = Handlebars.precompile(fileContent, options);
        output.push(' templates[\'' + templateName + '\'] = template(' + templateContents + ');\n');
    }

    output.push('}());');

    outStream.write(output.join(''));
    outStream.close();
}(arguments));
