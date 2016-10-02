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

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.content.SharedPreferences;

import me.henrytao.rxsharedpreferences.BuildConfig;
import me.henrytao.rxsharedpreferences.RobolectricGradleTestRunner;
import me.henrytao.rxsharedpreferences.RxSharePreferencesTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by henrytao on 9/17/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = RobolectricGradleTestRunner.SDK)
public class ObjectPreferenceTest {

  private static final String PREFERENCES_KEY = RxSharePreferencesTest.class.getName();

  private ObjectPreference mPreferences;

  private SharedPreferences mSharedPreferences;

  @Before
  public void before() {
    Context context = RuntimeEnvironment.application;
    Gson gson = new Gson();
    mSharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    mPreferences = new ObjectPreference(mSharedPreferences);
    mPreferences.register(Bird.class, gson::toJson, s -> gson.fromJson(s, Bird.class));
    mPreferences.register(Animal.class, gson::toJson, s -> gson.fromJson(s, Animal.class));
  }

  @Test
  public void put_() {
    Bird bird = new Bird("abc");
    mPreferences.put(Bird.class, "key", bird).toBlocking().subscribe();
    Bird bird2 = mPreferences.get(Bird.class, "key", null).toBlocking().first();
    Animal animal = mPreferences.get(Animal.class, "key", null).toBlocking().first();
    assertThat(bird2.name, equalTo(bird.name));
    assertThat(animal.name, equalTo(bird.name));
  }

  @Test
  public void put_null() {
    Animal animal = new Animal("unknown");
    mPreferences.put(Animal.class, "key", animal).toBlocking().subscribe();
    Animal animal2 = mPreferences.get(Animal.class, "key", null).toBlocking().first();
    Bird bird = mPreferences.get(Bird.class, "key", null).toBlocking().first();
    assertThat(animal2.name, equalTo(animal.name));
    assertThat(bird.name, equalTo(animal.name));
  }

  private static class Animal {

    final String name;

    Animal(String name) {
      this.name = name;
    }
  }

  public static class Bird extends Animal {

    Bird(String name) {
      super(name);
    }
  }
}