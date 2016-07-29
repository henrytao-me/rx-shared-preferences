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

import rx.Observable;
import rx.subjects.PublishSubject;

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
  }

  public T get(String key, T defValue) {
    addToIndex(key);
    return getValue(key, defValue);
  }

  public Observable<T> observe(String key, T defValue) {
    addToIndex(key);
    return Observable.just(null)
        .map(o -> get(key, defValue))
        .mergeWith(mSubject
            .filter(k -> TextUtils.equals(k, key))
            .map(k -> get(k, defValue)))
        .distinctUntilChanged();
  }

  public void put(String key, T value) {
    addToIndex(key);
    putValue(key, value);
    mSubject.onNext(key);
  }

  public void reset() {
    resetButKeep(null);
  }

  @SuppressLint("CommitPrefEdits")
  public void resetButKeep(List<String> keys) {
    keys = keys != null ? keys : new ArrayList<>();
    if (mIndexes.size() > 0) {
      List<String> keepIndexes = new ArrayList<>();
      int i = 0;
      for (int n = mIndexes.size(); i < n; i++) {
        if (keys.indexOf(mIndexes.get(i)) < 0) {
          mSharedPreferences.edit().remove(mIndexes.get(i)).commit();
        } else {
          keepIndexes.add(mIndexes.get(i));
        }
      }
      mIndexes.clear();
      mIndexes.addAll(keepIndexes);
    }
  }

  protected void addToIndex(String key) {
    if (mIndexes.indexOf(key) < 0) {
      mIndexes.add(key);
    }
  }
}
