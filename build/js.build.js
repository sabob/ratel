({
    "appDir": "../",
    baseUrl: "../javascripts/lib",
    dir: "../../../deploy/scripts",
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