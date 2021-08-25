/*
 Copyright 2020 Adobe. All rights reserved.
 This file is licensed to you under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License. You may obtain a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under
 the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 OF ANY KIND, either express or implied. See the License for the specific language
 governing permissions and limitations under the License.
 */

package com.adobe.marketing.mobile.cordova;

import android.os.Handler;
import android.os.Looper;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class echoes a string called from JavaScript.
 */
public class AEPMobileEdge_Cordova extends CordovaPlugin {

  final static String METHOD_EDGE_SEND_EVENT = "sendEvent";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
      if (METHOD_EDGE_SEND_EVENT.equals(action)) {
        this.sendEvent(args, callbackContext);
        return true;
      }

      return false;

    }

    private void sendEvent(final JSONArray args, final CallbackContext callbackContext) {
      cordova.getThreadPool().execute(new Runnable() {
          @Override
          public void run() {
              try {
                  final HashMap<String, Object> eventMap = getObjectMapFromJSON(args.getJSONObject(0));
                  final Event event = getEventFromMap(eventMap);

                  MobileCore.dispatchEvent(event, new ExtensionErrorCallback<ExtensionError>() {
                      @Override
                      public void error(ExtensionError extensionError) {
                          callbackContext.error(extensionError.getErrorName());
                      }
                  });

                  callbackContext.success();
              } catch (Exception ex) {
                  final String errorMessage = String.format("Exception in call to dispatchEvent: %s", ex.getLocalizedMessage());
                  MobileCore.log(LoggingMode.WARNING, "AEP SDK", errorMessage);
                  callbackContext.error(errorMessage);
              }
          }
      });
  }

}
