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
import android.text.TextUtils;

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
 * Created by henrytao on 7/29/16.
 */
public class ObjectPreference implements Adapter {

  protected final Map<Class, Func1> mDeserializers = new HashMap<>();

  protected final Map<Class, Func1> mSerializers = new HashMap<>();

  private final ConcurrentMap<String, Object> mCaches;

  private final SharedPreferences mSharedPreferences;

  private final PublishSubject<String> mSubject;

  public ObjectPreference(SharedPreferences sharedPreferences) {
    if (sharedPreferences == null) {
      throw new RuntimeException("SharedPreferences can not be null");
    }
    mSharedPreferences = sharedPreferences;
    mSubject = PublishSubject.create();
    mCaches = new ConcurrentHashMap<>();
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
      for (Map.Entry<String, Object> entry : mCaches.entrySet()) {
        if (keeps.indexOf(entry.getKey()) < 0) {
          mSharedPreferences.edit().remove(entry.getKey()).commit();
          mCaches.put(entry.getKey(), null);
        }
      }
    });
  }

  public <T> Observable<T> get(Class<T> tClass, String key, T defValue) {
    return Observable.create(subscriber -> {
      Object value = mCaches.containsKey(key) ? mCaches.get(key) : null;
      value = value != null ? value : getValue(tClass, key, defValue);
      try {
        SubscriptionUtils.onNextAndComplete(subscriber, tClass.cast(value));
      } catch (ClassCastException ignore) {
        SubscriptionUtils.onNextAndComplete(subscriber, defValue);
      }
    });
  }

  public <T> Observable<T> observe(Class<T> tClass, String key, T defValue) {
    return get(tClass, key, defValue)
        .mergeWith(mSubject
            .filter(k -> TextUtils.equals(k, key))
            .flatMap(k -> get(tClass, key, defValue)))
        .distinctUntilChanged();
  }

  public <T> Observable<Void> put(Class<T> tClass, String key, T value) {
    return Observable.create(subscriber -> {
      putValue(tClass, key, value);
      SubscriptionUtils.onNextAndComplete(subscriber);
      mSubject.onNext(key);
    });
  }

  public <T> void register(Class<T> tClass, Func1<T, String> serialize, Func1<String, T> deserialize) {
    if (serialize != null) {
      mSerializers.put(tClass, serialize);
    }
    if (deserialize != null) {
      mDeserializers.put(tClass, deserialize);
    }
  }

  @SuppressWarnings("unchecked")
  protected <T> T getValue(Class<T> tClass, String key, T defValue) {
    String rawValue = mSharedPreferences.getString(key, null);
    T value = null;
    if (mDeserializers.containsKey(tClass)) {
      try {
        value = tClass.cast(mDeserializers.get(tClass).call(rawValue));
      } catch (Exception ignore) {
      }
    }
    return value != null ? value : defValue;
  }

  @SuppressLint("CommitPrefEdits")
  @SuppressWarnings("unchecked")
  protected <T> void putValue(Class<T> tClass, String key, T value) {
    if (mSerializers.containsKey(tClass)) {
      try {
        mSharedPreferences.edit().putString(key, (String) mSerializers.get(tClass).call(value)).commit();
      } catch (Exception ignore) {
      }
    }
  }
}
