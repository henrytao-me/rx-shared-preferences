/*
 * Copyright 2015 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.henrytao.rxsharedpreferences;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by henrytao on 11/22/15.
 */
public class MapPreference extends BasePreference<Map<String, ?>> {

  public MapPreference(SharedPreferences sharedPreferences) {
    super(sharedPreferences);
  }

  @Override
  protected Map<String, ?> getValue(String key, Map<String, ?> defValue) {
    Map<String, Object> res = new HashMap<>();
    JSONObject jsonObject = getValue(key, new JSONObject(defValue));
    if (jsonObject != null) {
      Iterator<String> keys = jsonObject.keys();
      while (keys.hasNext()) {
        String k = keys.next();
        try {
          res.put(k, jsonObject.get(k));
        } catch (JSONException ignore) {
        }
      }
    }
    return res;
  }

  @Override
  protected void putValue(String key, Map<String, ?> value) {
    putValue(key, new JSONObject(value));
  }

  private JSONObject getValue(String key, JSONObject defValue) {
    String jsonString = mSharedPreferences.getString(key, null);
    try {
      return new JSONObject(jsonString);
    } catch (Exception e) {
      return defValue;
    }
  }

  private void putValue(String key, JSONObject value) {
    try {
      String jsonString = value.toString();
      mSharedPreferences.edit().putString(key, jsonString).apply();
    } catch (Exception ignore) {
    }
  }
}
