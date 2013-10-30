require.onResourceLoad = function(context, map, depArray) {
    //console.log("GRR:::", map);
    //console.log("GRR:::", map.name);
    //console.log("CONTEXT:::", context);
    var obj = context.defined[map.name];
    //console.log("obj", obj);

    if (obj) {
        if (obj.prototype) {
            if (!obj.prototype.id) {
                obj.prototype.id = map.id;
                obj.id = map.id;
            }
        } else {
            if (!obj.id) {
                obj.id = map.id;
            }
        }
        //console.log(obj);
        //console.log("ID", new obj().id);
    }
    //require([], function () {

    //});
};