//define(["require", "jquery", "text!./ClientSearch.htm", "../../utils/utils", "domReady!"], function(require, $, template, utils) {
define(function(require) {

    var $ = require("jquery");
    var Handlebars = require("handlebars");
    var moment = require("moment");
    var numeral = require("numeral");
    var template = require("text!./ClientSearch2.htm");
    var html = null;
    var ClientEdit = require("./ClientEdit");
    var utils = require("../../utils/utils");
    var viewManager = require("../../utils/view-manager");
    require("domReady!");

    function ClientSearch() {

        var that = this;
        
        this.getHTML = function() {
            return html;
        };

        this.getTemplate = function() {
            return template;
        };

        this.onInit = function(ready, args) {
            var request = $.ajax({
                url: "/ratel-examples/clientService/getClients",
                type: "GET",
                dataType: "json"
                        //contentType: "application/json"
            });

            request.done(function(data, textStatus, jqXHR) {
                console.log("clients fetched", data);

                var tmpl = Handlebars.compile(template);
                var context = {"customers": data};
                html = tmpl(context);

                ready.attachWithAnim(html, function() {
                    that.onAttached(data);
                    //html = tmpl({"customers2": [{"firstname": "MOOO"}]});
                });
            });

            request.fail(function(jqXHR, textStatus, errorThrown) {
                console.log("Request failed: " + textStatus);
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
                viewManager.showView(ClientEdit, args);
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

    Handlebars.registerHelper('dateFormat', function(context, block) {
        //console.log("dateFormat", context);

        var f = block.hash.format || "MMM Do, YYYY";
        //console.log(f, " ", context);
        //var c = moment(context);
        //console.log("val", c);
        //c = moment.utc(c).local();
        //context = 18376333;
        //var day = moment.unix(context);
        //var day = moment(12345678900);
        //var day = moment("1977-01-01T00:00:00.000+0000");
        var day = moment(context);
        return day.format(f);
    });

    Handlebars.registerHelper('action', function(context, block) {
        var id = "#" + block.hash.id;
        id += block.data.index;
        //console.dir(block);
        console.log(id);
        $("body").on("click", id, function(e) {
            e.preventDefault();
            alert(context.name);
        });
        console.log(context);
    });

    Handlebars.registerHelper('numFormat', function(context, block) {
        //console.log("numberFormat ", context);

        var f = block.hash.format || "#";
        //console.log(f, " ", context);
        //var c = moment(context);
        //console.log("val", c);
        //c = moment.utc(c).local();
        //context = 18376333;
        //var day = moment.unix(context);
        //var day = moment(12345678900);
        //var day = moment("1977-01-01T00:00:00.000+0000");
        var str = numeral(context).format(f);
        return str;

    });
    return ClientSearch;
});