// Place third party dependencies in the lib folder
//
// Configure loading modules from the lib directory,
// except 'app' ones, 
requirejs.config({
    //"baseUrl": "lib",
    "paths": {
      //"app": "../app",
      "hb": "spamd/hb",
        "moment": "moment",
        "numeral": "numeral"
    },
    "shim": {
        "handlebars": { exports: "Handlebars"}
        //"jquery.address": { exports: "address"}
    }
});

// Load the main app module to start the app
//console.log("Loading SPAMD");
requirejs(['spamd/onResourceLoad']);
requirejs(["jquery"]);
requirejs(["jquery.deserialize"]);
//requirejs([ "app/main"]);
