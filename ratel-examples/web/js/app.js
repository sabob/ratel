// Place third party dependencies in the lib folder
//
// Configure loading modules from the lib directory,
// except 'app' ones, 
requirejs.config({
    "baseUrl": "lib",
    "paths": {
      "app": "../app"
    }
    /*
    , "shim": {
    }*/
});

// Load the main app module to start the app
console.log("Loading MAIN!@");
//requirejs(['spamd/onResourceLoad', "app/main"]);
requirejs(["spamd/spamd"]);
requirejs([ "app/main"]);
