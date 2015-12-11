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

import org.json.JSONObject;

import android.content.SharedPreferences;

import java.util.Locale;

import me.henrytao.rxsharedpreferences.util.log.Ln;

/**
 * Created by henrytao on 11/22/15.
 */
public class JSONObjectPreference extends BasePreference<JSONObject> {

  public JSONObjectPreference(SharedPreferences sharedPreferences) {
    super(sharedPreferences);
  }

  @Override
  protected JSONObject getValue(String key, JSONObject defValue) {
    String jsonString = mSharedPreferences.getString(key, null);
    try {
      return new JSONObject(jsonString);
    } catch (Exception e) {
      if (RxSharedPreferences.DEBUG) {
        Ln.d(String.format(Locale.US, "%s/%s", JSONObjectPreference.class.getName(), "getValue"), e);
      }
      return defValue;
    }
  }

  @Override
  protected void putValue(String key, JSONObject value) {
    try {
      String jsonString = value.toString();
      mSharedPreferences.edit().putString(key, jsonString).apply();
    } catch (Exception ignore) {
      if (RxSharedPreferences.DEBUG) {
        Ln.d(String.format(Locale.US, "%s/%s", JSONObjectPreference.class.getName(), "putValue"), ignore);
      }
    }
  }
}
