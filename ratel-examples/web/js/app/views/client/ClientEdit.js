define(function(require) {
    var $ = require("jquery");
    var template = require("text!./ClientEdit.htm");
    var utils = require("spamd/utils/utils");
    var viewManager = require("spamd/view/view-manager");
    var errorUtils = require("spamd/utils/error-utils");
    require("domReady!");


    function ClientEdit() {
        
        var that = this;
        var id = null;
        var ClientSearch;

        this.getTemplate = function() {
            return template;
        };

        this.onInit = function(dom, args) {
            id = args.id;
            ClientSearch = args.ClientSearch;

            getClient(dom);
        };

        function onAttached() {
            $("#save").click(function(e) {
                saveClient(ClientSearch);


            });
        }

        function getClient(dom) {
            var request = $.ajax({
                url: "/ratel-examples/clientService/getClient",
                data: "id=" + id,
                type: "GET",
                dataType: "json"
                        //contentType: "application/json"
            });

            request.done(function(data, textStatus, jqXHR) {
                dom.attach(that.getTemplate()).then(function() {
                    //copy data to form
                    utils.fromObject("form", data);
                    onAttached();
                });
                console.log('done', data);
            });

            request.fail(function(jqXHR, textStatus, errorThrown) {
                console.log("getClient() failed: " + textStatus);
                dom.stay();
                var text = jqXHR.responseText;
                errorUtils.showError(text);
            });
            request.always(function(arg1, textStatus, arg3) {
                console.log("getClient() completed: ", textStatus);
            });
        }

        function saveClient(ClientSearch) {
            var json = utils.toJson("form");

            var request = $.ajax({
                url: "/ratel-examples/clientService/saveClient",
                data: json,
                type: "POST",
                dataType: "json",
                contentType: "application/json"
            });

            request.done(function(data, textStatus, jqXHR) {
                console.log('done', data);
                viewManager.showView(ClientSearch);
            });

            request.fail(function(jqXHR, textStatus, errorThrown) {
                console.log("SaveClient failed: " + textStatus);
            });
        }
    }
    return ClientEdit;
});