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

import java.util.HashMap;
import java.util.Map;

import io.noice.tornado.util.log.Ln;
import io.noice.tornado.util.rx.transformer.SchedulerTransformer;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by henrytao on 11/22/15.
 */
public abstract class BasePreference<T> {

  protected abstract T getValue(String key, T defValue);

  protected abstract void putValue(String key, T value);

  protected final Map<String, T> mCached;

  protected final SharedPreferences mSharedPreferences;

  protected PublishSubject<KeyValue<T>> mSubject;

  public BasePreference(SharedPreferences sharedPreferences) {
    if (sharedPreferences == null) {
      throw new RuntimeException("SharedPreferences can not be null");
    }
    mSubject = PublishSubject.create();
    mSharedPreferences = sharedPreferences;
    mCached = new HashMap<>();
  }

  public T get(String key, T defValue) {
    if (mCached.containsKey(key)) {
      return mCached.get(key);
    }
    T value = getValue(key, defValue);
    addToCache(key, value);
    return value;
  }

  public Observable<T> observe(String key, T defValue) {
    BehaviorSubject<T> subject = BehaviorSubject.create(get(key, defValue));
    mSubject
        .filter(keyValue -> keyValue != null && TextUtils.equals(keyValue.key, key))
        .map(keyValue -> keyValue.value)
        .compose(SchedulerTransformer.computationToMainThread())
        .subscribe(t -> {
          subject.onNext(t);
        }, throwable -> {
          Ln.d(this.getClass().getName(), "observe error | key: %s | value: %s", key, defValue.toString());
        });
    return subject;
  }

  public void put(String key, T value) {
    putValue(key, value);
    addToCache(key, value);
  }

  public void putInBackground(String key, T value) {
    Observable.create(subscriber -> put(key, value))
        .subscribeOn(Schedulers.computation())
        .subscribe(o -> {
          Ln.d(this.getClass().getName(), "putInBackground succeed | key: %s | value: %s", key, value.toString());
        }, throwable -> {
          Ln.w(this.getClass().getName(), "putInBackground error | key: %s | value: %s", key, value.toString());
        });
  }

  protected void addToCache(String key, T value) {
    mCached.put(key, value);
  }

  protected static class KeyValue<E> {

    String key;

    E value;

    public KeyValue(String key, E value) {
      this.key = key;
      this.value = value;
    }
  }
}
