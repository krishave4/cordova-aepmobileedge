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

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.Event;
import com.adobe.marketing.mobile.Event.Builder;
import com.adobe.marketing.mobile.ExtensionError;
import com.adobe.marketing.mobile.ExtensionErrorCallback;
import com.adobe.marketing.mobile.LoggingMode;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.MobilePrivacyStatus;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.adobe.marketing.mobile.Edge;
import com.adobe.marketing.mobile.ExperienceEvent;



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

                  Map<String, Object> reviewXdmData = new HashMap<>();
                  reviewXdmData.put("productSku", "demo123");
                  reviewXdmData.put("rating", 5);
                  reviewXdmData.put("reviewText", "I love this demo!");
                  reviewXdmData.put("reviewerId", "Anonymous user");

                  Map<String, Object> xdmData = new HashMap<>();
                  xdmData.put("eventType", "MyFirstXDMExperienceEvent");
                  xdmData.put("", reviewXdmData);

                  ExperienceEvent experienceEvent = new ExperienceEvent.Builder().setXdmSchema(xdmData).build();
                  Edge.sendEvent(experienceEvent, null);

                  callbackContext.success();
              } catch (Exception ex) {
                  final String errorMessage = String.format("Exception in call to dispatchEvent: %s", ex.getLocalizedMessage());
                  MobileCore.log(LoggingMode.WARNING, "AEP SDK", errorMessage);
                  callbackContext.error(errorMessage);
              }
          }
      });
  }

      // ===============================================================
    // Helpers
    // ===============================================================
    private HashMap<String, String> getStringMapFromJSON(JSONObject data) {
      HashMap<String, String> map = new HashMap<String, String>();
      @SuppressWarnings("rawtypes")
      Iterator it = data.keys();
      while (it.hasNext()) {
          String n = (String) it.next();
          try {
              map.put(n, data.getString(n));
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }

      return map;
  }

  private HashMap<String, Object> getObjectMapFromJSON(JSONObject data) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      @SuppressWarnings("rawtypes")
      Iterator it = data.keys();
      while (it.hasNext()) {
          String n = (String) it.next();
          try {
              map.put(n, data.getString(n));
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }

      return map;
  }

  private Event getEventFromMap(final HashMap<String, Object> event) throws Exception {
      return new Event.Builder(
              event.get("name").toString(),
              event.get("type").toString(),
              event.get("source").toString()
      ).setEventData(getObjectMapFromJSON(new JSONObject(event.get("data").toString()))).build();
  }

  private HashMap<String, Object> getMapFromEvent(final Event event) {
      final HashMap<String, Object> eventMap = new HashMap<>();
      eventMap.put("name", event.getName());
      eventMap.put("type", event.getType());
      eventMap.put("source", event.getSource());
      eventMap.put("data", event.getEventData());

      return eventMap;
  }

  // ===============================================================
  // Plugin lifecycle events
  // ===============================================================
  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
      super.initialize(cordova, webView);
      MobileCore.setApplication(this.cordova.getActivity().getApplication());
  }

  @Override
  public void onPause(boolean multitasking) {
      super.onPause(multitasking);
  }

  @Override
  public void onResume(boolean multitasking) {
      super.onResume(multitasking);
  }


}
