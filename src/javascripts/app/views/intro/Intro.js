define(function(require) {

    var $ = require("jquery");
    var Handlebars = require("handlebars");
    var template = require("hb!./Intro.htm");
    var utils = require("../../utils/utils");
    var viewManager = require("../../utils/view-manager");
    require("domReady!");

    function Intro() {

        var that = this;

        this.getTemplate = function() {
            return template;
        };

        this.onInit = function(dom, args) {
            dom.attachWithAnim(this.getTemplate(), function() {
                
            });
        };
    }
    return Intro;
});