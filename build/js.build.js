({
    "appDir": "../src",
    baseUrl: "javascripts/lib",
    dir: "../deploy",
    //optimize: 'uglify',
    optimize: 'none',
    removeCombined: true,
    skipDirOptimize: true,
    optimizeCss: 'standard',
    "mainConfigFile": "../src/javascripts/app.js",
    paths: {
        requireLib: "require"
        //handlebars: 'handlebars.runtime'
    },
    //"shim": {
        //"handlebars-build": { exports: "Handlebars"}
    //},
    modules: [
        {
            name: "app",
            include: [
                "requireLib"
            ]
        }
    ]
})