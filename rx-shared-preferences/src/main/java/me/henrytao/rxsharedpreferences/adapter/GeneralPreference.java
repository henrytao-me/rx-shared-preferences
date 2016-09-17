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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.henrytao.rxsharedpreferences.util.SubscriptionUtils;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Created by henrytao on 9/17/16.
 */
public class GeneralPreference<D, S> implements Adapter {

  protected final Map<Class<?>, Func1<String, ?>> mConverters;

  private final ConcurrentMap<String, D> mCaches;

  private final SharedPreferences mSharedPreferences;

  private final PublishSubject<String> mSubject;

  public GeneralPreference(SharedPreferences sharedPreferences) {
    if (sharedPreferences == null) {
      throw new RuntimeException("SharedPreferences can not be null");
    }
    mSharedPreferences = sharedPreferences;
    mSubject = PublishSubject.create();
    mCaches = new ConcurrentHashMap<>();
    mConverters = new HashMap<>();
  }

  @Override
  public Observable<Void> reset() {
    return resetButKeep(null);
  }

  @SuppressLint("CommitPrefEdits")
  @Override
  public Observable<Void> resetButKeep(List<String> keys) {
    return Observable.create(subscriber -> {
      List<String> keeps = keys != null ? keys : new ArrayList<>();
      for (Map.Entry<String, D> entry : mCaches.entrySet()) {
        if (keeps.indexOf(entry.getKey()) < 0) {
          mSharedPreferences.edit().remove(entry.getKey()).commit();
          mCaches.put(entry.getKey(), null);
        }
      }
    });
  }

  public D get(String key, D defValue) {
    D value = mCaches.containsKey(key) ? mCaches.get(key) : null;
    return value != null ? value : getValue(key, defValue);
  }

  public Observable<Void> put(String key, D value) {
    return Observable.create(subscriber -> {
      putValue(key, value);
      SubscriptionUtils.onNextAndComplete(subscriber);
      mSubject.onNext(key);
    });
  }

  //public void register(Class<?> tClass, Func1<String, ?> converter) {
  //  mConverters.put(tClass, converter);
  //}

  protected D getValue(String key, D defValue) {
    return null;
  }

  protected void putValue(String key, D value) {

  }
}
