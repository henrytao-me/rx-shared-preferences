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
import android.text.TextUtils;

import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

/**
 * Created by henrytao on 11/22/15.
 */
public abstract class BasePreference<T> {

  protected abstract T getValue(String key, T defValue);

  protected abstract void putValue(String key, T value);

  protected final SharedPreferences mSharedPreferences;

  protected PublishSubject<String> mSubject;

  public BasePreference(SharedPreferences sharedPreferences) {
    if (sharedPreferences == null) {
      throw new RuntimeException("SharedPreferences can not be null");
    }
    mSubject = PublishSubject.create();
    mSharedPreferences = sharedPreferences;

    mSharedPreferences.registerOnSharedPreferenceChangeListener((preferences, key) -> mSubject.onNext(key));
  }

  public T get(String key, T defValue) {
    return getValue(key, defValue);
  }

  public Observable<T> observe(String key, T defValue) {
    return Observable.create(subscriber -> {
      subscriber.onNext(get(key, defValue));
      Subscription subjectSubscription = mSubject
          .filter(k -> TextUtils.equals(k, key))
          .map(k -> getValue(key, defValue))
          .subscribe(subscriber::onNext);
      subscriber.add(Subscriptions.create(subjectSubscription::unsubscribe));
    });
  }

  public void put(String key, T value) {
    putValue(key, value);
  }

  public Observable<T> putInBackground(String key, T value) {
    return Observable.create(subscriber -> {
      try {
        put(key, value);
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(value);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onError(e);
        }
      }
    });
  }
}
