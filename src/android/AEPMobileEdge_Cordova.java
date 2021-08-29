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
import java.util.List;
import com.adobe.marketing.mobile.Edge;
import com.adobe.marketing.mobile.ExperienceEvent;
import com.adobe.marketing.mobile.EdgeCallback;
import com.adobe.marketing.mobile.EdgeEventHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;  
import java.io.Serializable;
import java.io.IOException;
import java.io.*;
import java.util.Base64;






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
        MobileCore.log(LoggingMode.WARNING, "AEP SDK", "Got a call to send Message");
      cordova.getThreadPool().execute(new Runnable() {
          @Override
          public void run() {
              try {
                final HashMap<String, Object> eventMap = getObjectMapFromJSON(args.getJSONObject(0));
                final Event event = getEventFromMap(eventMap);
                MobileCore.log(LoggingMode.WARNING, "AEP SDK", "Actual Message");
                MobileCore.log(LoggingMode.WARNING, "AEP SDK", new JSONObject(event.getEventData()).toString() );

                //   Map<String, Object> reviewXdmData = new HashMap<>();
                //   reviewXdmData.put("productSku", "demo123");
                //   reviewXdmData.put("rating", 5);
                //   reviewXdmData.put("reviewText", "I love this demo!");
                //   reviewXdmData.put("reviewerId", "Anonymous user");

                //   Map<String, Object> xdmData = new HashMap<>();
                //   xdmData.put("eventType", "MyFirstXDMExperienceEvent");
                //   xdmData.put("_aepgdcdevenablement2", reviewXdmData);

            //       ExperienceEvent experienceEvent = new ExperienceEvent.Builder().setXdmSchema(getMapFromEvent(event)).build();
            //       MobileCore.log(LoggingMode.WARNING, "AEP SDK", "Sending the Message");
            //       Edge.sendEvent(experienceEvent, new EdgeCallback() {
            //         @Override
            //         public void onComplete(final List<EdgeEventHandle> handles) {
            //              MobileCore.log(LoggingMode.WARNING, "AEP SDK", "Received Network Response");
            //         }
            //       });
            //       callbackContext.success();
            MobileCore.log(LoggingMode.WARNING, "AEP SDK", "Sending the Message");
                // String jsonString = "{" +
                // "'eventType':'Mobile SDK Page View'," +
                // "'web':{" +
                // "'webPageDetails':{" +
                // "'pageViews':{" +
                // "'id':'" + "/uri456789" + "', 'value': 1" +
                // "}" +
                // "}" +
                // "}" +
                // "}";
                // MobileCore.log(LoggingMode.WARNING, "AEP SDK", event.getEventData());
                Map<String, Object> xdmData = new Gson().fromJson(
                    new JSONObject(event.getEventData()).toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
                );

                // ExperienceEvent experienceEvent = new ExperienceEvent.Builder()
                //         .setXdmSchema(xdmData)
                //         .build();
                // Edge.sendEvent(experienceEvent, null);
                ExperienceEvent experienceEvent = new ExperienceEvent.Builder().setXdmSchema(event.getEventData()).build();
                MobileCore.log(LoggingMode.WARNING, "AEP SDK", "Sending the Message");
                Edge.sendEvent(experienceEvent, new EdgeCallback() {
                @Override
                public void onComplete(final List<EdgeEventHandle> handles) {
                        MobileCore.log(LoggingMode.WARNING, "AEP SDK", "Received Network Response");
                        Iterator<EdgeEventHandle> eventIterator = handles.iterator();
                        while (eventIterator.hasNext()) {
                            for (Map<String, Object> map : eventIterator.next().getPayload()) {
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    String key = entry.getKey();
                                    Object value = entry.getValue();
                                    MobileCore.log(LoggingMode.WARNING, "AEP SDK", key);
                                    MobileCore.log(LoggingMode.WARNING, "AEP SDK", value.toString());

                                }
                            }
                        }
                }
                });
                callbackContext.success();
                MobileCore.log(LoggingMode.WARNING, "AEP SDK", "Sent the Message");
              } catch (Exception ex) {
                  final String errorMessage = String.format("Exception in call to dispatchEvent: %s", ex.getLocalizedMessage());
                  MobileCore.log(LoggingMode.WARNING, "AEP SDK", errorMessage);
                  callbackContext.error(errorMessage);
              }                       // handle the Edge Network response 


    
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

  private String serialize(Serializable o) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(o);
    oos.close();
    return Base64.getEncoder().encodeToString(baos.toByteArray());
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
