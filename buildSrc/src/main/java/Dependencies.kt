object Versions {
    val compileSdk = 29
    val minSdk = 19
    val targetSdk = 29
    val kotlin = "1.3.61"
    val appCompat = "1.1.0"
    val androidxCore = "1.1.0"
    val androidxLegacy = "1.0.0"
    val constraintLayout = "1.1.3"
    val recyclerView = "1.1.0"
    val fragment = "1.2.2"
    val dagger = "2.25.4"
    val coroutines = "1.3.2"
    val navigation = "2.3.0-alpha02"
    val lifecycle = "2.2.0-rc03"
    val retrofit = "2.6.3"
    val glide = "4.10.0"
    val room = "2.2.3"
    val paging = "2.1.1"
    val archCoreTest = "2.1.0"
    val androidxTest = "1.2.0"
    val espresso = "3.2.0"
    val junit = "4.12"
    val androidxJunitExt = "1.1.1"
}

object Deps {

    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val androidxCore = "androidx.core:core-ktx:${Versions.androidxCore}"
    val androidxLegacy = "androidx.legacy:legacy-support-v4:${Versions.androidxLegacy}"

    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"

    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"

    val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    val navigationDynamicFeatures =
        "androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigation}"

    val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    val daggerKapt = "com.google.dagger:dagger-compiler:${Versions.dagger}"

    val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    val pagingKtx = "androidx.paging:paging-runtime-ktx:${Versions.paging}"

    val lifecycleExt = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    val lifecycleKapt = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"

    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"

    val room = "androidx.room:room-runtime:${Versions.room}"
    val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    val roomKapt = "androidx.room:room-compiler:${Versions.room}"

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"

    val junit = "junit:junit:${Versions.junit}"
    val androidxTestRunner = "androidx.test:runner:${Versions.androidxTest}"
    val androidxTestRules = "androidx.test:rules:${Versions.androidxTest}"
    val androidxCoreTest = "androidx.arch.core:core-testing:${Versions.archCoreTest}"
    val androidxJunitExt = "androidx.test.ext:junit:${Versions.androidxJunitExt}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}  