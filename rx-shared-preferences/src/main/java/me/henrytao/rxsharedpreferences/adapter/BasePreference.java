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
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.henrytao.rxsharedpreferences.util.SubscriptionUtils;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by henrytao on 11/22/15.
 */
public abstract class BasePreference<T> implements Adapter {

  protected abstract T getValue(String key, T defValue);

  protected abstract void putValue(String key, T value);

  protected final SharedPreferences mSharedPreferences;

  protected final PublishSubject<String> mSubject;

  private final ConcurrentMap<String, T> mCaches;

  public BasePreference(SharedPreferences sharedPreferences) {
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
      for (Map.Entry<String, T> entry : mCaches.entrySet()) {
        if (keeps.indexOf(entry.getKey()) < 0) {
          mSharedPreferences.edit().remove(entry.getKey()).commit();
          mCaches.put(entry.getKey(), null);
        }
      }
    });
  }

  public Observable<T> get(String key, T defValue) {
    return Observable.create(subscriber -> {
      T value = mCaches.containsKey(key) ? mCaches.get(key) : null;
      SubscriptionUtils.onNextAndComplete(subscriber, value != null ? value : getValue(key, defValue));
    });
  }

  public Observable<T> observe(String key, T defValue) {
    return get(key, defValue)
        .mergeWith(mSubject
            .filter(k -> TextUtils.equals(k, key))
            .flatMap(k -> get(key, defValue)))
        .distinctUntilChanged();
  }

  public Observable<Void> put(String key, T value) {
    return Observable.create(subscriber -> {
      putValue(key, value);
      SubscriptionUtils.onNextAndComplete(subscriber);
      mSubject.onNext(key);
    });
  }
}
