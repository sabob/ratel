({
    "appDir": "../src",
   baseUrl: "js/lib",
    dir: "../deploy",
    optimize: 'uglify',
    //optimize: 'none',
    removeCombined: true,
    skipDirOptimize: true,
    optimizeCss: 'standard',
    "mainConfigFile": "../src/js/app.js",
    paths: {
        "requireLib": "require",
        "handlebars-runtime": "handlebars.runtime"
    },
    
    modules: [
        {
            name: "app",
            include: [
                "requireLib",
                "handlebars-runtime"
            ]
        }
    ],
    
    onBuildWrite: function( name, path, contents ) {
 
        if (name === 'handlebars') {
            return null;
        }
        return contents;
    },
})