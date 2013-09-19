({
    "appDir": "../",
    baseUrl: "javascripts/lib",
    dir: "../deploy",
        //optimize: 'uglify',
        optimize: 'none',
        //optimizeCss: 'standard',
        "mainConfigFile": "../javascripts/app.js",
        
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