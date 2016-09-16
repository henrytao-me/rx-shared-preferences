/*
 * Copyright 2016 "Henry Tao <hi@henrytao.me>"
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

package me.henrytao.rxsharedpreferences.adapter;

import android.content.SharedPreferences;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by henrytao on 7/29/16.
 */
public class ObjectPreference extends StringPreference {

  public ObjectPreference(SharedPreferences sharedPreferences) {
    super(sharedPreferences);
  }

  public <T> T get(String key, T defValue, Func1<String, T> func1) {
    String value = get(key, null);
    if (value == null) {
      return defValue;
    }
    return func1.call(value);
  }

  public <T> Observable<T> observe(String key, T defValue, Func1<String, T> func1) {
    return observe(key, null).map(value -> value == null ? defValue : func1.call(value));
  }

  public <T> void put(String key, T value, Func1<T, String> func1) {
    put(key, func1.call(value));
  }
}
