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

package me.henrytao.rxsharedpreferences.adapter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

/**
 * Created by henrytao on 11/22/15.
 */
public class StringPreference extends BasePreference<String> {

  public StringPreference(SharedPreferences sharedPreferences) {
    super(sharedPreferences);
  }

  @Override
  protected String getValue(String key, String defValue) {
    return mSharedPreferences.getString(key, defValue);
  }

  @SuppressLint("CommitPrefEdits")
  @Override
  protected void putValue(String key, String value) {
    mSharedPreferences.edit().putString(key, value).commit();
  }
}