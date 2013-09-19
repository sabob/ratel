// Place third party dependencies in the lib folder
//
// Configure loading modules from the lib directory,
// except 'app' ones, 
requirejs.config({
    "baseUrl": "javascripts/lib",
    "paths": {
      "app": "../app",
      //"prettify": "../lib/prettify"
    },
    "shim": {
        "handlebars": { exports: "Handlebars"}
    }
});

// Load the main app module to start the app
requirejs(["app/main"]);