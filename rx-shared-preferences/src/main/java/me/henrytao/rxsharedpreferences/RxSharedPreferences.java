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
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.henrytao.rxsharedpreferences.adapter.Adapter;
import me.henrytao.rxsharedpreferences.adapter.BooleanPreference;
import me.henrytao.rxsharedpreferences.adapter.BundlePreference;
import me.henrytao.rxsharedpreferences.adapter.FloatPreference;
import me.henrytao.rxsharedpreferences.adapter.IntegerPreference;
import me.henrytao.rxsharedpreferences.adapter.JSONObjectPreference;
import me.henrytao.rxsharedpreferences.adapter.LongPreference;
import me.henrytao.rxsharedpreferences.adapter.ObjectPreference;
import me.henrytao.rxsharedpreferences.adapter.StringPreference;
import me.henrytao.rxsharedpreferences.adapter.StringSetPreference;
import me.henrytao.rxsharedpreferences.type.ObjectConverter;
import me.henrytao.rxsharedpreferences.type.ObjectDeserializer;
import me.henrytao.rxsharedpreferences.type.ObjectSerializer;
import rx.Observable;

/**
 * Created by henrytao on 11/22/15.
 */
public class RxSharedPreferences {

  public static boolean DEBUG = true;

  protected final BooleanPreference mBooleanPreference;

  protected final BundlePreference mBundlePreference;

  protected final FloatPreference mFloatPreference;

  protected final IntegerPreference mIntegerPreference;

  protected final JSONObjectPreference mJSONPreference;

  protected final LongPreference mLongPreference;

  protected final ObjectPreference mObjectPreference;

  protected final List<Adapter> mRegisteredPreferences;

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
    mStringPreference = new StringPreference(mSharedPreferences);
    mStringSetPreference = new StringSetPreference(mSharedPreferences);
    mObjectPreference = new ObjectPreference(mSharedPreferences);
    mBundlePreference = new BundlePreference(mSharedPreferences);

    mRegisteredPreferences = new ArrayList<>();
    register(mBooleanPreference);
    register(mFloatPreference);
    register(mIntegerPreference);
    register(mJSONPreference);
    register(mLongPreference);
    register(mStringPreference);
    register(mStringSetPreference);
    register(mObjectPreference);
    register(mBundlePreference);
  }

  public Observable<Boolean> getBoolean(String key, Boolean defValue) {
    return mBooleanPreference.get(key, defValue);
  }

  public Observable<Bundle> getBundle(String key, Bundle defValue) {
    return mBundlePreference.get(key, defValue);
  }

  public Observable<Float> getFloat(String key, Float defValue) {
    return mFloatPreference.get(key, defValue);
  }

  public Observable<Integer> getInt(String key, Integer defValue) {
    return mIntegerPreference.get(key, defValue);
  }

  public Observable<JSONObject> getJSONObject(String key, JSONObject defValue) {
    return mJSONPreference.get(key, defValue);
  }

  public Observable<Long> getLong(String key, Long defValue) {
    return mLongPreference.get(key, defValue);
  }

  public <T> Observable<T> getObject(Class<T> tClass, String key, T defValue) {
    return mObjectPreference.get(tClass, key, defValue);
  }

  public Observable<String> getString(String key, String defValue) {
    return mStringPreference.get(key, defValue);
  }

  public Observable<Set<String>> getStringSet(String key, Set<String> defValue) {
    return mStringSetPreference.get(key, defValue);
  }

  public Observable<Boolean> observeBoolean(String key, Boolean defValue) {
    return mBooleanPreference.observe(key, defValue);
  }

  public Observable<Bundle> observeBundle(String key, Bundle defValue) {
    return mBundlePreference.observe(key, defValue);
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

  public <T> Observable<T> observeObject(Class<T> tClass, String key, T defValue) {
    return mObjectPreference.observe(tClass, key, defValue);
  }

  public Observable<String> observeString(String key, String defValue) {
    return mStringPreference.observe(key, defValue);
  }

  public Observable<Set<String>> observeStringSet(String key, Set<String> defValue) {
    return mStringSetPreference.observe(key, defValue);
  }

  public Observable<Void> putBoolean(String key, Boolean value) {
    return mBooleanPreference.put(key, value);
  }

  public Observable<Void> putBundle(String key, Bundle value) {
    return mBundlePreference.put(key, value);
  }

  public Observable<Void> putFloat(String key, Float value) {
    return mFloatPreference.put(key, value);
  }

  public Observable<Void> putInt(String key, Integer value) {
    return mIntegerPreference.put(key, value);
  }

  public Observable<Void> putJSONObject(String key, JSONObject value) {
    return mJSONPreference.put(key, value);
  }

  public Observable<Void> putLong(String key, Long value) {
    return mLongPreference.put(key, value);
  }

  public <T> Observable<Void> putObject(Class<T> tClass, String key, T value) {
    return mObjectPreference.put(tClass, key, value);
  }

  public Observable<Void> putString(String key, String value) {
    return mStringPreference.put(key, value);
  }

  public Observable<Void> putStringSet(String key, Set<String> value) {
    return mStringSetPreference.put(key, value);
  }

  public <T> void register(Class<T> tClass, ObjectSerializer<T> serialize, ObjectDeserializer<T> deserialize) {
    mObjectPreference.register(tClass, serialize, deserialize);
  }

  public <T> void register(Class<T> tClass, ObjectConverter<T> converter) {
    register(tClass, converter, converter);
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

  protected void register(Adapter adapter) {
    mRegisteredPreferences.add(adapter);
  }
}
