//define(["require", "jquery", "text!./ClientSearch.htm", "../../utils/utils", "domReady!"], function(require, $, template, utils) {
define(function(require) {

    var $ = require("jquery");
    var Handlebars = require("handlebars");
    var template = require("text!./ClientSearch.htm");
    var ClientEdit = require("./ClientEdit");
    var utils = require("spamd/utils/utils");
    var viewManager = require("spamd/view/view-manager");
    var errorUtils = require("spamd/utils/error-utils");
    require("domReady!");

    function ClientSearch() {

        var that = this;

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

                var tmpl = Handlebars.compile(template);
                var context = {one: "Firstname", two: "Lastname", three: "Action"};
                template = tmpl(context);

                dom.attach(template).then( function() {
                   that.onAttached(data);
                });
            });

            request.fail(function(jqXHR, textStatus, errorThrown) {
                dom.stay();
                console.log("Request failed: " + textStatus);
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
            var table = $('#table > tbody:last');
            $.each(data, function(i, obj) {
                var actionLinks = '<a href="#" value=' + obj.id + ' class="edit">Edit</a> | <a href="#" value=' + obj.id + ' class="delete">Delete</a>';
                table.append('<tr id="row-' + obj.id + '"><td>' + obj.firstname + '</td><td>' + obj.lastname + '</td><td>' + actionLinks + '</td></tr>');
            });

            $("#table a.edit").click(function(e) {
                e.preventDefault();
                var id = $(e.target).attr('value');
                console.log("Edit id:", id);
                var args = {id: id, ClientSearch: ClientSearch};
                viewManager.showView({view: ClientEdit, args: args });
            });

            $("#table a.delete").click(function(e) {
                e.preventDefault();
                var id = $(e.target).attr('value');
                var val = confirm('Are you sure?');
                if (val) {
                    $('#row-' + id).remove();
                }
            });
            console.log("onAttached done");
        };
    }
    return ClientSearch;
});