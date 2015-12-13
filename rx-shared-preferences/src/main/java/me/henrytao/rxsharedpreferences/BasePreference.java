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

import java.util.ArrayList;
import java.util.List;

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

  protected List<String> mIndexes;

  protected PublishSubject<String> mSubject;

  public BasePreference(SharedPreferences sharedPreferences) {
    if (sharedPreferences == null) {
      throw new RuntimeException("SharedPreferences can not be null");
    }
    mIndexes = new ArrayList<>();
    mSubject = PublishSubject.create();
    mSharedPreferences = sharedPreferences;
    mSharedPreferences.registerOnSharedPreferenceChangeListener((preferences, key) -> mSubject.onNext(key));
  }

  public T get(String key, T defValue) {
    addToIndex(key);
    return getValue(key, defValue);
  }

  public Observable<T> observe(String key, T defValue) {
    addToIndex(key);
    return Observable.create(subscriber -> {
      subscriber.onNext(get(key, defValue));
      Subscription subjectSubscription = mSubject
          .filter(k -> TextUtils.equals(k, key))
          .map(k -> get(key, defValue))
          .subscribe(subscriber::onNext);
      subscriber.add(Subscriptions.create(subjectSubscription::unsubscribe));
    });
  }

  public void put(String key, T value) {
    addToIndex(key);
    putValue(key, value);
  }

  public void reset() {
    if (mIndexes.size() > 0) {
      int i = 0;
      for (int n = mIndexes.size(); i < n; i++) {
        mSharedPreferences.edit().remove(mIndexes.get(i)).commit();
      }
      mIndexes.clear();
    }
  }

  protected void addToIndex(String key) {
    if (mIndexes.indexOf(key) < 0) {
      mIndexes.add(key);
    }
  }
}
