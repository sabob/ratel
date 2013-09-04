// Place third party dependencies in the lib folder
//
// Configure loading modules from the lib directory,
// except 'app' ones, 
requirejs.config({
    "baseUrl": "lib",
    "paths": {
      "app": "../app",
        "moment": "moment",
        "numeral": "numeral"
               
    },
    "shim": {
        "jquery.alpha": ["jquery"],
        "jquery.beta": ["jquery"],
        "jquery.deserialize": ["jquery"],
        "handlebars": { exports: "Handlebars"}
    }
});

// Load the main app module to start the app
requirejs(["app/main"]);
