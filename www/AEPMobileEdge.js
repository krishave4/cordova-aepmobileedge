var AEPMobileEdge = (function() {

    var exec = cordova.require('cordova/exec');
    var AEPMobileEdge = (typeof exports !== 'undefined') && exports || {};

    var PLUGIN_NAME = "AEPMobileEdge_Cordova";

    // ===========================================================================
    // public objects
    // ===========================================================================

    AEPMobileEdge.sendEvent = function(sdkEvent, success, fail) {
        var FUNCTION_NAME = "sendEvent";

        return exec(success, fail, PLUGIN_NAME, FUNCTION_NAME, [sdkEvent]);
    };

    return AEPMobileEdge;

}());

module.exports =  AEPMobileEdge;

