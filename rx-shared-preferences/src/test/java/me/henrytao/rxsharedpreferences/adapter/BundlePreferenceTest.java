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

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import me.henrytao.rxsharedpreferences.BaseTest;
import me.henrytao.rxsharedpreferences.BuildConfig;
import me.henrytao.rxsharedpreferences.RobolectricGradleTestRunner;

import static org.junit.Assert.assertThat;

/**
 * Created by henrytao on 10/2/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = RobolectricGradleTestRunner.SDK)
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class BundlePreferenceTest extends BaseTest {

  private static final String PREFERENCES_KEY = BundlePreferenceTest.class.getName();

  private BundlePreference mPreferences;

  private SharedPreferences mSharedPreferences;

  @Override
  public void onBefore() {
    super.onBefore();
    Context context = RuntimeEnvironment.application;
    Gson gson = new Gson();
    mSharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    mPreferences = new BundlePreference(mSharedPreferences);
  }

  @Test
  public void test() throws Exception {
    Bundle input = new Bundle();
    input.putString("hello", "moto");
    mPreferences.put("input", input).toBlocking().subscribe();

    Bundle output = mPreferences.get("input", new Bundle()).toBlocking().first();
    assertThat(output.getString("hello", null), CoreMatchers.equalTo("moto"));
  }
}
