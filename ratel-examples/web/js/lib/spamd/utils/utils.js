define(function(require) {
    var $ = require("jquery");
    require("jquery.deserialize");
    require("domReady!");

    function Utils() {
        
        //console.log("Utils:");
        //$('body').alpha().beta();
        
        this.toJson = function(formId) {
            var object = this.toObject(formId);
            var json = JSON.stringify(object);
            return json;
        };
        
        this.fromJson = function(formId, json) {
            var obj = JSON.parse(json);
            this.fromObject(formId, obj);
            return json;
        };

        this.toObject = function(formId) {
            var result = {};
            var array = $('#' + formId).serializeArray();
            $.each(array, function() {
                if (result[this.name]) {
                    if (!result[this.name].push) {
                        result[this.name] = [result[this.name]];
                    }
                    result[this.name].push(this.value || '');
                } else {
                    result[this.name] = this.value || '';
                }
            });
            return result;
        };
        
        this.fromObject = function(formId, obj) {
             $('#' + formId).deserialize(obj);
        };
        
        this.exist = function(val) {
                if (typeof(val) === 'undefined' || val === null) {
                    return false;
                }
                return true;
            };
            
            this.getViewName = function(view) {
            if (typeof  view === 'string') {
                return view;
            }
            var funcNameRegex = /function (.{1,})\(/;
            var results = (funcNameRegex).exec((view).constructor.toString());
            return (results && results.length > 1) ? results[1] : "";
        };
    }
    
    return new Utils();
});