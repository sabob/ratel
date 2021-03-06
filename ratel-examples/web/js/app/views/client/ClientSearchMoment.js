//define(["require", "jquery", "text!./ClientSearch.htm", "../../utils/utils", "domReady!"], function(require, $, template, utils) {
define(function(require) {

    var $ = require("jquery");
    var templateEngine = require("spamd/template/template-engine");
    var template = require("hb!./ClientSearchMoment.htm");
    var html = null;
    var ClientEdit = require("./ClientEdit");
    var utils = require("spamd/utils/utils");
    var viewManager = require("spamd/view/view-manager");
    var errorUtils = require("spamd/utils/error-utils");
    require("domReady!");

    function ClientSearch() {

        var that = this;
        
        this.getHTML = function() {
            return html;
        };

        this.getTemplate = function() {
            return template;
        };

        this.onInit = function(dom, args) {
            var request = $.ajax({
                url: "/ratel-examples/service/clientservice/clients",
                type: "GET",
                dataType: "json"
                        //contentType: "application/json"
            });

            request.done(function(data, textStatus, jqXHR) {
                console.log("clients fetched", data);

                var context = {"customers": data};
                html = templateEngine.render(template, context);

                dom.attach(html).then(function() {
                    that.onAttached(data);
                    //html = tmpl({"customers2": [{"firstname": "MOOO"}]});
                });
            });

            request.fail(function(jqXHR, textStatus, errorThrown) {
                console.log("Request failed: " + textStatus);
                dom.stay();
                var text = jqXHR.responseText;
                errorUtils.showError(text);
            });
            request.always(function(arg1, textStatus, arg3) {
                console.log("Request completed: ", textStatus);
                //attach(template); 
                //that.onAttached();
                //onReady();
            });
        };

        this.onAttached = function(data) {

            $("#table").on("click", ".edit-link", function(e) {
                e.preventDefault();
                var id = $(e.target).attr('value');
                console.log("ID", id);
                var args = {id: id, ClientSearch: ClientSearch};
                viewManager.showView({view: ClientEdit, args: args});
            });

            $("#table").on("click", ".delete-link", function(e) {
                e.preventDefault();
                var index = $(e.target).attr('value');
                var val = confirm('Are you sure?' + index);
                if (val) {
                    $("#table #" + index).remove();
                }
            });

            console.log("onAttached done");
        };
    }

    return ClientSearch;
});