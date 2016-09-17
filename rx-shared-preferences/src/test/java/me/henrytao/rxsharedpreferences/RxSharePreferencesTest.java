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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = RobolectricGradleTestRunner.SDK)
public class RxSharePreferencesTest {

  //private static final String PREFERENCES_KEY = RxSharePreferencesTest.class.getName();
  //
  //private static final String TEST_KEY = "key";
  //
  //private static final String TEST_KEY_2 = "key_2";
  //
  //private RxSharedPreferences mRxSharedPreferences;
  //
  //private SharedPreferences mSharedPreferences;
  //
  //@After
  //public void after() {
  //  mRxSharedPreferences.reset();
  //}
  //
  //@Before
  //public void before() {
  //  Context context = RuntimeEnvironment.application;
  //  mSharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
  //  mRxSharedPreferences = new RxSharedPreferences(mSharedPreferences);
  //}
  //
  //@Test
  //public void getBooleanTest() {
  //  assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, true), equalTo(true));
  //  assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(false));
  //}
  //
  //@Test
  //public void getFloatTest() {
  //  assertThat(mRxSharedPreferences.getFloat(TEST_KEY, 10f), equalTo(10f));
  //  assertThat(mRxSharedPreferences.getFloat(TEST_KEY, 20f), equalTo(20f));
  //}
  //
  //@Test
  //public void getIntTest() {
  //  assertThat(mRxSharedPreferences.getInt(TEST_KEY, 10), equalTo(10));
  //  assertThat(mRxSharedPreferences.getInt(TEST_KEY, 20), equalTo(20));
  //}
  //
  //@Test
  //public void getJSONObjectTest() throws JSONException {
  //  JSONObject o1 = new JSONObject();
  //  o1.put("a", "hello moto");
  //  o1.put("b", 2);
  //
  //  JSONObject o2 = new JSONObject();
  //  o2.put("a", "hello moto");
  //  o2.put("b", 2);
  //  o2.put("c", true);
  //
  //  assertThat(mRxSharedPreferences.getJSONObject(TEST_KEY, o1).get("a"), equalTo("hello moto"));
  //  assertThat(mRxSharedPreferences.getJSONObject(TEST_KEY, o1).get("b"), equalTo(2));
  //  assertThat(mRxSharedPreferences.getJSONObject(TEST_KEY, o2).get("a"), equalTo("hello moto"));
  //  assertThat(mRxSharedPreferences.getJSONObject(TEST_KEY, o2).get("b"), equalTo(2));
  //  assertThat(mRxSharedPreferences.getJSONObject(TEST_KEY, o2).get("c"), equalTo(true));
  //}
  //
  //@Test
  //public void getLongTest() {
  //  assertThat(mRxSharedPreferences.getLong(TEST_KEY, 10l), equalTo(10l));
  //  assertThat(mRxSharedPreferences.getLong(TEST_KEY, 20l), equalTo(20l));
  //}
  //
  //@Test
  //public void getStringSetTest() {
  //  List<String> o1 = Arrays.asList("a");
  //  List<String> o2 = Arrays.asList("b");
  //
  //  assertThat(new ArrayList<>(mRxSharedPreferences.getStringSet(TEST_KEY, new HashSet<>(o1))).get(0), equalTo("a"));
  //  assertThat(new ArrayList<>(mRxSharedPreferences.getStringSet(TEST_KEY, new HashSet<>(o2))).get(0), equalTo("b"));
  //}
  //
  //@Test
  //public void getStringTest() {
  //  assertThat(mRxSharedPreferences.getString(TEST_KEY, "a"), equalTo("a"));
  //  assertThat(mRxSharedPreferences.getString(TEST_KEY, "b"), equalTo("b"));
  //}
  //
  //@Test
  //public void observeBooleanTest() {
  //  TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
  //  Subscription subscription = mRxSharedPreferences.observeBoolean(TEST_KEY, false).subscribe(testSubscriber);
  //  mRxSharedPreferences.putBoolean(TEST_KEY, true);
  //  mRxSharedPreferences.putBoolean(TEST_KEY, false);
  //  mRxSharedPreferences.putBoolean(TEST_KEY_2, true);
  //  subscription.unsubscribe();
  //  mRxSharedPreferences.putBoolean(TEST_KEY, false);
  //  testSubscriber.assertReceivedOnNext(Arrays.asList(false, true, false));
  //}
  //
  //@Test
  //public void observeFloatTest() {
  //  TestSubscriber<Float> testSubscriber = new TestSubscriber<>();
  //  Subscription subscription = mRxSharedPreferences.observeFloat(TEST_KEY, 10f).subscribe(testSubscriber);
  //  mRxSharedPreferences.putFloat(TEST_KEY, 20f);
  //  mRxSharedPreferences.putFloat(TEST_KEY, 30f);
  //  mRxSharedPreferences.putFloat(TEST_KEY_2, 40f);
  //  subscription.unsubscribe();
  //  mRxSharedPreferences.putFloat(TEST_KEY, 50f);
  //  testSubscriber.assertReceivedOnNext(Arrays.asList(10f, 20f, 30f));
  //}
  //
  //@Test
  //public void observeIntTest() {
  //  TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
  //  Subscription subscription = mRxSharedPreferences.observeInt(TEST_KEY, 10).subscribe(testSubscriber);
  //  mRxSharedPreferences.putInt(TEST_KEY, 20);
  //  mRxSharedPreferences.putInt(TEST_KEY, 30);
  //  mRxSharedPreferences.putInt(TEST_KEY_2, 40);
  //  subscription.unsubscribe();
  //  mRxSharedPreferences.putInt(TEST_KEY, 50);
  //  testSubscriber.assertReceivedOnNext(Arrays.asList(10, 20, 30));
  //}
  //
  //@Test
  //public void observeJSONObjectTest() throws JSONException {
  //  JSONObject o1 = new JSONObject();
  //  o1.put("a", "o1");
  //
  //  JSONObject o2 = new JSONObject();
  //  o2.put("a", "o2");
  //
  //  JSONObject o3 = new JSONObject();
  //  o3.put("a", "o3");
  //
  //  JSONObject o4 = new JSONObject();
  //  o4.put("a", "o4");
  //
  //  JSONObject o5 = new JSONObject();
  //  o5.put("a", "o5");
  //
  //  TestSubscriber<JSONObject> testSubscriber = new TestSubscriber<>();
  //  Subscription subscription = mRxSharedPreferences.observeJSONObject(TEST_KEY, o1).subscribe(testSubscriber);
  //  mRxSharedPreferences.putJSONObject(TEST_KEY, o2);
  //  mRxSharedPreferences.putJSONObject(TEST_KEY, o3);
  //  mRxSharedPreferences.putJSONObject(TEST_KEY_2, o4);
  //  subscription.unsubscribe();
  //  mRxSharedPreferences.putJSONObject(TEST_KEY, o5);
  //  List<JSONObject> responses = testSubscriber.getOnNextEvents();
  //  assertThat(responses.size(), equalTo(3));
  //  assertThat(responses.get(0).get("a"), equalTo("o1"));
  //  assertThat(responses.get(1).get("a"), equalTo("o2"));
  //  assertThat(responses.get(2).get("a"), equalTo("o3"));
  //}
  //
  //@Test
  //public void observeLongTest() {
  //  TestSubscriber<Long> testSubscriber = new TestSubscriber<>();
  //  Subscription subscription = mRxSharedPreferences.observeLong(TEST_KEY, 10l).subscribe(testSubscriber);
  //  mRxSharedPreferences.putLong(TEST_KEY, 20l);
  //  mRxSharedPreferences.putLong(TEST_KEY, 30l);
  //  mRxSharedPreferences.putLong(TEST_KEY_2, 40l);
  //  subscription.unsubscribe();
  //  mRxSharedPreferences.putLong(TEST_KEY, 50l);
  //  testSubscriber.assertReceivedOnNext(Arrays.asList(10l, 20l, 30l));
  //}
  //
  //@Test
  //public void observeStringSetTest() {
  //  List<String> o1 = Arrays.asList("a");
  //  List<String> o2 = Arrays.asList("b");
  //  List<String> o3 = Arrays.asList("c");
  //  List<String> o4 = Arrays.asList("d");
  //  List<String> o5 = Arrays.asList("e");
  //
  //  TestSubscriber<Set<String>> testSubscriber = new TestSubscriber<>();
  //  Subscription subscription = mRxSharedPreferences.observeStringSet(TEST_KEY, new HashSet<>(o1)).subscribe(testSubscriber);
  //  mRxSharedPreferences.putStringSet(TEST_KEY, new HashSet<>(o2));
  //  mRxSharedPreferences.putStringSet(TEST_KEY, new HashSet<>(o3));
  //  mRxSharedPreferences.putStringSet(TEST_KEY_2, new HashSet<>(o4));
  //  subscription.unsubscribe();
  //  mRxSharedPreferences.putStringSet(TEST_KEY, new HashSet<>(o5));
  //  List<Set<String>> responses = testSubscriber.getOnNextEvents();
  //  assertThat(responses.size(), equalTo(3));
  //  assertThat(new ArrayList<>(responses.get(0)).get(0), equalTo("a"));
  //  assertThat(new ArrayList<>(responses.get(1)).get(0), equalTo("b"));
  //  assertThat(new ArrayList<>(responses.get(2)).get(0), equalTo("c"));
  //}
  //
  //@Test
  //public void observeStringTest() {
  //  TestSubscriber<String> testSubscriber = new TestSubscriber<>();
  //  Subscription subscription = mRxSharedPreferences.observeString(TEST_KEY, "a").subscribe(testSubscriber);
  //  mRxSharedPreferences.putString(TEST_KEY, "b");
  //  mRxSharedPreferences.putString(TEST_KEY, "c");
  //  mRxSharedPreferences.putString(TEST_KEY_2, "d");
  //  subscription.unsubscribe();
  //  mRxSharedPreferences.putString(TEST_KEY, "e");
  //  testSubscriber.assertReceivedOnNext(Arrays.asList("a", "b", "c"));
  //}
  //
  //@Test
  //public void putBooleanTest() {
  //  assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(false));
  //  mRxSharedPreferences.putBoolean(TEST_KEY, true);
  //  assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(true));
  //  mRxSharedPreferences.putBoolean(TEST_KEY, false);
  //  assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, true), equalTo(false));
  //}
  //
  //@Test
  //public void putFloatTest() {
  //  assertThat(mRxSharedPreferences.getFloat(TEST_KEY, 10f), equalTo(10f));
  //  mRxSharedPreferences.putFloat(TEST_KEY, 20f);
  //  assertThat(mRxSharedPreferences.getFloat(TEST_KEY, 30f), equalTo(20f));
  //}
  //
  //@Test
  //public void putIntTest() {
  //  assertThat(mRxSharedPreferences.getInt(TEST_KEY, 10), equalTo(10));
  //  mRxSharedPreferences.putInt(TEST_KEY, 20);
  //  assertThat(mRxSharedPreferences.getInt(TEST_KEY, 30), equalTo(20));
  //}
  //
  //@Test
  //public void putJSONObjectTest() throws JSONException {
  //  JSONObject o1 = new JSONObject();
  //  o1.put("a", "o1");
  //
  //  JSONObject o2 = new JSONObject();
  //  o2.put("a", "o2");
  //
  //  JSONObject o3 = new JSONObject();
  //  o3.put("a", "o3");
  //
  //  assertThat(mRxSharedPreferences.getJSONObject(TEST_KEY, o1).get("a"), equalTo("o1"));
  //  mRxSharedPreferences.putJSONObject(TEST_KEY, o2);
  //  assertThat(mRxSharedPreferences.getJSONObject(TEST_KEY, o3).get("a"), equalTo("o2"));
  //}
  //
  //@Test
  //public void putLongTest() {
  //  assertThat(mRxSharedPreferences.getLong(TEST_KEY, 10l), equalTo(10l));
  //  mRxSharedPreferences.putLong(TEST_KEY, 20l);
  //  assertThat(mRxSharedPreferences.getLong(TEST_KEY, 30l), equalTo(20l));
  //}
  //
  //@Test
  //public void putStringSetTest() {
  //  List<String> o1 = Arrays.asList("a");
  //  List<String> o2 = Arrays.asList("b");
  //  List<String> o3 = Arrays.asList("c");
  //
  //  assertThat(new ArrayList<>(mRxSharedPreferences.getStringSet(TEST_KEY, new HashSet<>(o1))).get(0), equalTo("a"));
  //  mRxSharedPreferences.putStringSet(TEST_KEY, new HashSet<>(o2));
  //  assertThat(new ArrayList<>(mRxSharedPreferences.getStringSet(TEST_KEY, new HashSet<>(o3))).get(0), equalTo("b"));
  //}
  //
  //@Test
  //public void putStringTest() {
  //  assertThat(mRxSharedPreferences.getString(TEST_KEY, "a"), equalTo("a"));
  //  mRxSharedPreferences.putString(TEST_KEY, "b");
  //  assertThat(mRxSharedPreferences.getString(TEST_KEY, "c"), equalTo("b"));
  //}
  //
  //@Test
  //public void resetKeepTest() {
  //  mRxSharedPreferences.putBoolean("a", true);
  //  mRxSharedPreferences.putFloat("b", 1.5f);
  //  mRxSharedPreferences.putInt("c", 10);
  //
  //  assertThat(mRxSharedPreferences.getBoolean("a", false), equalTo(true));
  //  assertThat(mRxSharedPreferences.getFloat("b", 0f), equalTo(1.5f));
  //  assertThat(mRxSharedPreferences.getInt("c", 0), equalTo(10));
  //
  //  mRxSharedPreferences.resetButKeep(Arrays.asList("a"));
  //  assertThat(mRxSharedPreferences.getBoolean("a", false), equalTo(true));
  //  assertThat(mRxSharedPreferences.getFloat("b", 0f), equalTo(0f));
  //  assertThat(mRxSharedPreferences.getInt("c", 0), equalTo(0));
  //}
  //
  //@Test
  //public void resetTest() {
  //  TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
  //  Subscription subscription = mRxSharedPreferences.observeBoolean(TEST_KEY, false).subscribe(testSubscriber);
  //  mRxSharedPreferences.putBoolean(TEST_KEY, true);
  //  mRxSharedPreferences.putBoolean(TEST_KEY, true);
  //  mRxSharedPreferences.putBoolean(TEST_KEY_2, true);
  //  mRxSharedPreferences.reset();
  //
  //  assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, true), equalTo(true));
  //  assertThat(mRxSharedPreferences.getBoolean(TEST_KEY_2, false), equalTo(false));
  //
  //  subscription.unsubscribe();
  //
  //  mRxSharedPreferences.putBoolean(TEST_KEY, false);
  //
  //  testSubscriber.assertReceivedOnNext(Arrays.asList(false, true));
  //}
}