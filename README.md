[ ![Download](https://api.bintray.com/packages/henrytao-me/maven/rx-shared-preferences/images/download.svg) ](https://bintray.com/henrytao-me/maven/rx-shared-preferences/_latestVersion) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-rx--shared--preferences-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2914)

rx-shared-preferences
================

RxJava SharedPreferences for Android, lightweight and extendable 


## Installation

``` groovy
compile "me.henrytao:rx-shared-preferences:<latest-version>"
```

`rx-shared-preferences` is deployed to `jCenter`. Make sure you have `jcenter()` in your project gradle.

## Features

- Boolean
- Float
- Integer
- JSONObject
- Long
- Map
- String
- StringSet
- Object
- Bundle
- ResetButKeep


## Usage

``` java
// init
mSharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
mRxSharedPreferences = new RxSharedPreferences(mSharedPreferences);

// test get
assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, true), equalTo(true));
assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(false));

// test put
assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(false));
mRxSharedPreferences.putBoolean(TEST_KEY, true);
assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(true));
mRxSharedPreferences.putBoolean(TEST_KEY, false);
assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, true), equalTo(false));

// test observe
TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
Subscription subscription = mRxSharedPreferences.observeBoolean(TEST_KEY, false).subscribe(testSubscriber);
mRxSharedPreferences.putBoolean(TEST_KEY, true);
mRxSharedPreferences.putBoolean(TEST_KEY, false);
mRxSharedPreferences.putBoolean(TEST_KEY_2, true);
subscription.unsubscribe();
mRxSharedPreferences.putBoolean(TEST_KEY, false);
testSubscriber.assertReceivedOnNext(Arrays.asList(false, true, false));

// test resetButKeep
mRxSharedPreferences.putBoolean("a", true);
mRxSharedPreferences.putFloat("b", 1.5f);
mRxSharedPreferences.putInt("c", 10);

assertThat(mRxSharedPreferences.getBoolean("a", false), equalTo(true));
assertThat(mRxSharedPreferences.getFloat("b", 0f), equalTo(1.5f));
assertThat(mRxSharedPreferences.getInt("c", 0), equalTo(10));

mRxSharedPreferences.resetButKeep(Arrays.asList("a"));
assertThat(mRxSharedPreferences.getBoolean("a", false), equalTo(true));
assertThat(mRxSharedPreferences.getFloat("b", 0f), equalTo(0f));
assertThat(mRxSharedPreferences.getInt("c", 0), equalTo(0));
```


## Contributing

Any contributions are welcome!  
Please check the [CONTRIBUTING](CONTRIBUTING.md) guideline before submitting a new PR.


## License

    Copyright 2015 "Henry Tao <hi@henrytao.me>"

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


