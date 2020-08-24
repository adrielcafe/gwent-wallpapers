@file:Suppress("Unused", "MayBeConstant", "MemberVisibilityCanBePrivate")

private object Version {
    // Gradle plugins
    const val GRADLE_ANDROID = "4.0.1"
    const val GRADLE_GOOGLE_SERVICES = "4.3.3"
    const val GRADLE_CRASHLYTICS = "2.2.0"
    const val GRADLE_DETEKT = "1.10.0"
    const val GRADLE_KTLINT = "9.3.0"
    const val GRADLE_VERSIONS = "0.29.0"

    // Kotlin
    const val KOTLIN = "1.4.0"
    const val COROUTINES = "1.3.9"

    // Firebase
    const val ANALYTICS = "17.5.0"
    const val CRASHLYTICS = "17.2.1"

    // Android
    const val MATERIAL = "1.3.0-alpha02"
    const val APP_COMPAT = "1.3.0-alpha02"
    const val CONSTRAINT_LAYOUT = "2.0.0"
    const val CORE = "1.3.1"
    const val LIFECYCLE = "2.2.0"
    const val WORK = "2.4.0"

    // UI
    const val FAST_ADAPTER = "5.2.2"
    const val GLIDE = "4.11.0"
    const val PEKO = "2.1.1"
    const val SNEAKER = "2.0.0"
    const val DRESS_CODE = "1.0.1"

    // Others
    const val HAL = "1.0.1"
    const val BROKER = "1.1.1"
    const val SATCHEL = "1.0.3"
    const val KOIN = "2.1.6"
    const val LEAK_CANARY = "2.4"

    // Test
    const val JUNIT = "5.6.2"
    const val STRIKT = "0.26.1"
    const val MOCKK = "1.10.0"
}

object ProjectLib {
    const val ANDROID = "com.android.tools.build:gradle:${Version.GRADLE_ANDROID}"
    const val GOOGLE_SERVICES = "com.google.gms:google-services:${Version.GRADLE_GOOGLE_SERVICES}"
    const val CRASHLYTICS = "com.google.firebase:firebase-crashlytics-gradle:${Version.GRADLE_CRASHLYTICS}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.KOTLIN}"
    const val DETEKT = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Version.GRADLE_DETEKT}"
    const val KTLINT = "org.jlleitschuh.gradle:ktlint-gradle:${Version.GRADLE_KTLINT}"
    const val VERSIONS = "com.github.ben-manes:gradle-versions-plugin:${Version.GRADLE_VERSIONS}"

    val all = setOf(ANDROID, GOOGLE_SERVICES, CRASHLYTICS, KOTLIN, DETEKT, KTLINT, VERSIONS)
}

object ModuleLib {
    // Kotlin
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.KOTLIN}"
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.COROUTINES}"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES}"

    // Firebase
    const val ANALYTICS = "com.google.firebase:firebase-analytics-ktx:${Version.ANALYTICS}"
    const val CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx:${Version.CRASHLYTICS}"

    // Android
    const val MATERIAL = "com.google.android.material:material:${Version.MATERIAL}"
    const val CORE = "androidx.core:core-ktx:${Version.CORE}"
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Version.APP_COMPAT}"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINT_LAYOUT}"
    const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.LIFECYCLE}"
    const val LIFECYCLE_VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.LIFECYCLE}"
    const val WORK = "androidx.work:work-runtime-ktx:${Version.WORK}"

    // UI
    const val FAST_ADAPTER = "com.mikepenz:fastadapter:${Version.FAST_ADAPTER}"
    const val FAST_ADAPTER_BINDING = "com.mikepenz:fastadapter-extensions-binding:${Version.FAST_ADAPTER}"
    const val GLIDE = "com.github.bumptech.glide:glide:${Version.GLIDE}"
    const val GLIDE_OKHTTP = "com.github.bumptech.glide:okhttp3-integration:${Version.GLIDE}"
    const val GLIDE_RECYCLER_VIEW = "com.github.bumptech.glide:recyclerview-integration:${Version.GLIDE}"
    const val PEKO = "com.markodevcic.peko:peko:${Version.PEKO}"
    const val SNEAKER = "com.irozon.sneaker:sneaker:${Version.SNEAKER}"
    const val DRESS_CODE = "com.github.daio-io:dresscode:${Version.DRESS_CODE}"

    // Others
    const val HAL = "com.github.adrielcafe.hal:hal-core:${Version.HAL}"
    const val BROKER = "com.github.adrielcafe.broker:broker-core:${Version.BROKER}"
    const val BROKER_LIFECYCLE = "com.github.adrielcafe.broker:broker-lifecycle:${Version.BROKER}"
    const val SATCHEL = "com.github.adrielcafe.satchel:satchel-core:${Version.SATCHEL}"
    const val SATCHEL_GZIP = "com.github.adrielcafe.satchel:satchel-serializer-gzip:${Version.SATCHEL}"
    const val KOIN = "org.koin:koin-core:${Version.KOIN}"
    const val KOIN_ANDROID = "org.koin:koin-android:${Version.KOIN}"
    const val KOIN_ANDROID_SCOPE = "org.koin:koin-androidx-scope:${Version.KOIN}"
    const val KOIN_ANDROID_VIEWMODEL = "org.koin:koin-androidx-viewmodel:${Version.KOIN}"
    const val PLUMBER = "com.squareup.leakcanary:plumber-android:${Version.LEAK_CANARY}"
    const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:${Version.LEAK_CANARY}"

    val kotlin = setOf(KOTLIN, COROUTINES_CORE)
    val android = setOf(COROUTINES_ANDROID, MATERIAL, CORE, APP_COMPAT, CONSTRAINT_LAYOUT, LIFECYCLE_RUNTIME, LIFECYCLE_VIEW_MODEL, WORK)
    val ui = setOf(FAST_ADAPTER, FAST_ADAPTER_BINDING, GLIDE, GLIDE_OKHTTP, GLIDE_RECYCLER_VIEW, PEKO, SNEAKER, DRESS_CODE)
}

object TestLib {
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.COROUTINES}"
    const val JUNIT_API = "org.junit.jupiter:junit-jupiter-api:${Version.JUNIT}"
    const val JUNIT_ENGINE = "org.junit.jupiter:junit-jupiter-engine:${Version.JUNIT}"
    const val STRIKT = "io.strikt:strikt-core:${Version.STRIKT}"
    const val MOCKK = "io.mockk:mockk:${Version.MOCKK}"

    val unitTest = setOf(COROUTINES, JUNIT_API, JUNIT_ENGINE, STRIKT, MOCKK)
}
