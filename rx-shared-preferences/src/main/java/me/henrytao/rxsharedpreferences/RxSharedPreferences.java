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

import android.content.SharedPreferences;

import rx.Observable;

/**
 * Created by henrytao on 11/22/15.
 */
public class RxSharedPreferences {

  protected final BooleanPreference mBooleanPreference;

  protected final SharedPreferences mSharedPreferences;

  public RxSharedPreferences(SharedPreferences sharedPreferences) {
    if (sharedPreferences == null) {
      throw new RuntimeException("SharedPreferences can not be null");
    }
    mSharedPreferences = sharedPreferences;
    mBooleanPreference = new BooleanPreference(mSharedPreferences);
  }

  public Boolean getBoolean(String key, Boolean defValue) {
    return mBooleanPreference.get(key, defValue);
  }

  public Observable<Boolean> observeBoolean(String key, Boolean defValue) {
    return mBooleanPreference.observe(key, defValue);
  }

  public void putBoolean(String key, Boolean value) {
    mBooleanPreference.put(key, value);
  }

  public void putBooleanInBackground(String key, Boolean value) {
    mBooleanPreference.putInBackground(key, value);
  }
}
