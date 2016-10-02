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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Base64;

import me.henrytao.rxsharedpreferences.RxSharedPreferences;

/**
 * Created by henrytao on 10/2/16.
 */
public class BundlePreference extends BasePreference<Bundle> {

  public BundlePreference(SharedPreferences sharedPreferences) {
    super(sharedPreferences);
  }

  @Override
  protected Bundle getValue(String key, Bundle defValue) {
    try {
      String serialized = mSharedPreferences.getString(key, null);
      if (serialized != null) {
        Parcel parcel = Parcel.obtain();
        byte[] data = Base64.decode(serialized, 0);
        parcel.unmarshall(data, 0, data.length);
        parcel.setDataPosition(0);
        Bundle bundle = new Bundle();
        bundle.readFromParcel(parcel);
        return bundle;
      }
    } catch (Exception ignore) {
      if (RxSharedPreferences.DEBUG) {
        ignore.printStackTrace();
      }
    }
    return defValue;
  }

  @SuppressLint("CommitPrefEdits")
  @Override
  protected void putValue(String key, Bundle bundle) {
    Parcel parcel = Parcel.obtain();
    bundle.writeToParcel(parcel, 0);
    byte[] data = parcel.marshall();
    String serialized = Base64.encodeToString(data, 0);
    mSharedPreferences.edit().putString(key, serialized).commit();
  }
}
