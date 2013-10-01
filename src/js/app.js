// Place third party dependencies in the lib folder
//
// Configure loading modules from the lib directory,
// except 'app' ones, 
requirejs.config({
    "baseUrl": "js/lib",
    "paths": {
      "app": "../app"
    },
    "shim": {
        "handlebars": { exports: "Handlebars"}
    }
});

// Load the main app module to start the app
requirejs(["app/main"]);
