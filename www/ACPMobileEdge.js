cordova.define("cordova-aepmobileedge.cordova-aepmobileedge", function(require, exports, module) {
    var ACPMobileEdge = (function() {
        var ACPMobileEdge = (typeof exports !== 'undefined') && exports || {};
        var exec = cordova.require('cordova/exec'); // eslint-disable-line no-undef
    
        var PLUGIN_NAME = "AEPMobileEdge_Cordova";
    
        // ===========================================================================
        // public objects
        // ===========================================================================
        ACPMobileEdge.createEvent = function(name, type, source, data) {
            return {
                name: name,
                type: type,
                source: source,
                data: data
            };
        };
        return ACPMobileEdge;

    }());    
    module.exports = ACPMobileEdge;

});