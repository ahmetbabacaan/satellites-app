plugins {
    id(Plugins.ANDROID_APPLICATION_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_PLUGIN)
    id(Plugins.KOTLIN_PARCELIZE_PLUGIN)
    id(Plugins.KOTLIN_KAPT_PLUGIN)
    id(Plugins.DAGGER_HILT_PLUGIN)
    id(Plugins.NAVIGATION_SAFE_ARGS)
}

android {
    compileSdkVersion(AndroidVersion.COMPILE_SDK_VERSION)
    buildToolsVersion = AndroidVersion.BUILD_TOOLS_VERSION

    defaultConfig {
        applicationId = AndroidVersion.APPLICATION_ID
        minSdkVersion(AndroidVersion.MIN_SDK_VERSION)
        targetSdkVersion(AndroidVersion.TARGET_SDK_VERSION)
        versionCode = AndroidVersion.VERSION_CODE
        versionName = AndroidVersion.VERSION_NAME

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    buildFeatures {
        dataBinding = true
    }
    lintOptions {
        isAbortOnError = false
    }
    sourceSets {
        getByName("androidTest").java.srcDirs("src/test-common/java")
        getByName("test").java.srcDirs("src/test-common/java")
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.time.ExperimentalTime"
        freeCompilerArgs =
            freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }

    kotlin.sourceSets.matching {
        it.name.endsWith("Test")
    }.configureEach {
        languageSettings.useExperimentalAnnotation("kotlin.time.ExperimentalTime")
        languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
    }

    tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinCompile::class)
        .forEach {
            it.kotlinOptions {
                freeCompilerArgs = listOf(
                    "-Xopt-in=kotlin.time.ExperimentalTime",
                    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
                )
            }
        }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Deps.KOTLIN)
    implementation(Deps.ANDROIDX_CORE_KTX)
    implementation(Deps.ANDROIDX_FRAGMENT_KTX)

    implementation(Deps.APPCOMPAT)
    implementation(Deps.MATERIAL)
    implementation(Deps.CONSTRAINT_LAYOUT)
    implementation(Deps.RECYCLER_VIEW)

    implementation(Deps.LIFECYCLE_LIVEDATA_KTX)

    implementation(Deps.NAVIGATION_FRAGMENT)

    implementation(Deps.DAGGER_HILT_ANDROID)
    kapt(Deps.DAGGER_HILT_COMPILER)
    implementation(Deps.DAGGER_HILT_VIEWMODEL)
    kapt(Deps.DAGGER_HILT_ANDROIDX_HILT_COMPILER)

    implementation(Deps.COROUTINES_CORE)
    implementation(Deps.COROUTINES_ANDROID)

    implementation(Deps.RETROFIT)
    implementation(Deps.RETROFIT_MOSHI_CONVERTER)

    implementation(Deps.CHUCKER_DEBUG)

    implementation(Deps.MOSHI)
    kapt(Deps.MOSHI_COMPILER)

    implementation(Deps.BINDABLES)

    implementation(Deps.DATASTORE)

    //Testing

    testImplementation(TestDeps.JUNIT4)
    testImplementation(TestDeps.ANDROIDX_JUNIT)
    testImplementation(TestDeps.ANDROIDX_TEST_CORE)
    testImplementation(TestDeps.ANDROIDX_TEST_CORE_KTX)
    testImplementation(TestDeps.ANDROIDX_CORE_TESTING)

    testImplementation(TestDeps.COROUTINES_TEST)
    testImplementation(Deps.COROUTINES_ANDROID)

    testImplementation(TestDeps.MOCK_WEB_SERVER)

    testImplementation(TestDeps.MOCK_K)

    testImplementation(TestDeps.MOCKITO)
    testImplementation(TestDeps.MOCKITO_INLINE)

    testImplementation(TestDeps.TURBINE)
}