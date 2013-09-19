({
    "appDir": "../",
    baseUrl: "javascripts/lib",
    dir: "../deploy",
    modules: [
        {
            name: "app",
            requireLib: "require",
            include: [
                requireLib
            ]
        }
    ]
})