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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.henrytao.rxsharedpreferences.adapter.BasePreference;
import me.henrytao.rxsharedpreferences.adapter.BooleanPreference;
import me.henrytao.rxsharedpreferences.adapter.FloatPreference;
import me.henrytao.rxsharedpreferences.adapter.IntegerPreference;
import me.henrytao.rxsharedpreferences.adapter.JSONObjectPreference;
import me.henrytao.rxsharedpreferences.adapter.LongPreference;
import me.henrytao.rxsharedpreferences.adapter.MapPreference;
import me.henrytao.rxsharedpreferences.adapter.StringPreference;
import me.henrytao.rxsharedpreferences.adapter.StringSetPreference;
import rx.Observable;

/**
 * Created by henrytao on 11/22/15.
 */
public class RxSharedPreferences {

  public static boolean DEBUG = true;

  protected final BooleanPreference mBooleanPreference;

  protected final FloatPreference mFloatPreference;

  protected final IntegerPreference mIntegerPreference;

  protected final JSONObjectPreference mJSONPreference;

  protected final LongPreference mLongPreference;

  protected final MapPreference mMapPreference;

  protected final List<BasePreference> mRegisteredPreferences;

  protected final SharedPreferences mSharedPreferences;

  protected final StringPreference mStringPreference;

  protected final StringSetPreference mStringSetPreference;

  public RxSharedPreferences(SharedPreferences sharedPreferences) {
    if (sharedPreferences == null) {
      throw new RuntimeException("SharedPreferences can not be null");
    }
    mSharedPreferences = sharedPreferences;
    mBooleanPreference = new BooleanPreference(mSharedPreferences);
    mFloatPreference = new FloatPreference(mSharedPreferences);
    mIntegerPreference = new IntegerPreference(mSharedPreferences);
    mJSONPreference = new JSONObjectPreference(mSharedPreferences);
    mLongPreference = new LongPreference(mSharedPreferences);
    mMapPreference = new MapPreference(mSharedPreferences);
    mStringPreference = new StringPreference(mSharedPreferences);
    mStringSetPreference = new StringSetPreference(mSharedPreferences);

    mRegisteredPreferences = new ArrayList<>();
    register(mBooleanPreference);
    register(mFloatPreference);
    register(mIntegerPreference);
    register(mJSONPreference);
    register(mLongPreference);
    register(mMapPreference);
    register(mStringPreference);
    register(mStringSetPreference);
  }

  public Boolean getBoolean(String key, Boolean defValue) {
    return mBooleanPreference.get(key, defValue);
  }

  public Float getFloat(String key, Float defValue) {
    return mFloatPreference.get(key, defValue);
  }

  public Integer getInt(String key, Integer defValue) {
    return mIntegerPreference.get(key, defValue);
  }

  public JSONObject getJSONObject(String key, JSONObject defValue) {
    return mJSONPreference.get(key, defValue);
  }

  public Long getLong(String key, Long defValue) {
    return mLongPreference.get(key, defValue);
  }

  public Map<String, ?> getMap(String key, Map<String, ?> defValue) {
    return mMapPreference.get(key, defValue);
  }

  public String getString(String key, String defValue) {
    return mStringPreference.get(key, defValue);
  }

  public Set<String> getStringSet(String key, Set<String> defValue) {
    return mStringSetPreference.get(key, defValue);
  }

  public Observable<Boolean> observeBoolean(String key, Boolean defValue) {
    return mBooleanPreference.observe(key, defValue);
  }

  public Observable<Float> observeFloat(String key, Float defValue) {
    return mFloatPreference.observe(key, defValue);
  }

  public Observable<Integer> observeInt(String key, Integer defValue) {
    return mIntegerPreference.observe(key, defValue);
  }

  public Observable<JSONObject> observeJSONObject(String key, JSONObject defValue) {
    return mJSONPreference.observe(key, defValue);
  }

  public Observable<Long> observeLong(String key, Long defValue) {
    return mLongPreference.observe(key, defValue);
  }

  public Observable<Map<String, ?>> observeMap(String key, Map<String, ?> defValue) {
    return mMapPreference.observe(key, defValue);
  }

  public Observable<String> observeString(String key, String defValue) {
    return mStringPreference.observe(key, defValue);
  }

  public Observable<Set<String>> observeStringSet(String key, Set<String> defValue) {
    return mStringSetPreference.observe(key, defValue);
  }

  public void putBoolean(String key, Boolean value) {
    mBooleanPreference.put(key, value);
  }

  public void putFloat(String key, Float value) {
    mFloatPreference.put(key, value);
  }

  public void putInt(String key, Integer value) {
    mIntegerPreference.put(key, value);
  }

  public void putJSONObject(String key, JSONObject value) {
    mJSONPreference.put(key, value);
  }

  public void putLong(String key, Long value) {
    mLongPreference.put(key, value);
  }

  public void putMap(String key, Map<String, ?> value) {
    mMapPreference.put(key, value);
  }

  public void putString(String key, String value) {
    mStringPreference.put(key, value);
  }

  public void putStringSet(String key, Set<String> value) {
    mStringSetPreference.put(key, value);
  }

  public void reset() {
    int i = 0;
    for (int n = mRegisteredPreferences.size(); i < n; i++) {
      mRegisteredPreferences.get(i).reset();
    }
  }

  public void resetButKeep(List<String> keys) {
    int i = 0;
    for (int n = mRegisteredPreferences.size(); i < n; i++) {
      mRegisteredPreferences.get(i).resetButKeep(keys);
    }
  }

  protected void register(BasePreference basePreference) {
    mRegisteredPreferences.add(basePreference);
  }
}
