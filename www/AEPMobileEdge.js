cordova.define("cordova-aepmobileedge.cordova-aepmobileedge", function(require, exports, module) {
    var AEPMobileEdge = (function() {
        var AEPMobileEdge = (typeof exports !== 'undefined') && exports || {};
        var exec = cordova.require('cordova/exec'); // eslint-disable-line no-undef
    
        var PLUGIN_NAME = "AEPMobileEdge_Cordova";
    
        // ===========================================================================
        // public objects
        // ===========================================================================
        AEPMobileEdge.createEvent = function(name, type, source, data) {
            return {
                name: name,
                type: type,
                source: source,
                data: data
            };
        };
        return AEPMobileEdge;

    }());    
    module.exports = AEPMobileEdge;

});