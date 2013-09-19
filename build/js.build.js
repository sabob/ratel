({
    "appDir": "../src",
    baseUrl: "javascripts/lib",
    dir: "../deploy",
    optimize: 'uglify',
    //optimize: 'none',
    removeCombined: true,
    skipDirOptimize: true,
    optimizeCss: 'standard',
    "mainConfigFile": "../src/javascripts/app.js",
    paths: {
        requireLib: "require"
    },
    modules: [
        {
            name: "app",
            include: [
                "requireLib"
            ]
        }
    ]
})