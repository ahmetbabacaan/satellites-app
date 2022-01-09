object Plugins {

    /*
       Project Level
    */
    const val CLASSPATH_GRADLE = "com.android.tools.build:gradle:${PluginVersion.GRADLE_VERSION}"
    const val CLASSPATH_DAGGER_HILT =
        "com.google.dagger:hilt-android-gradle-plugin:${Version.DAGGER_HILT_VERSION}"
    const val CLASSPATH_NAV_SAFE_ARGS =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${PluginVersion.NAV_SAFE_ARGS_VERSION}"

    /*
        Module Level
     */
    const val DAGGER_HILT_PLUGIN = "dagger.hilt.android.plugin"
    const val ANDROID_APPLICATION_PLUGIN = "com.android.application"
    const val KOTLIN_ANDROID_PLUGIN = "kotlin-android"
    const val KOTLIN_KAPT_PLUGIN = "kotlin-kapt"
    const val KOTLIN_PARCELIZE_PLUGIN = "kotlin-parcelize"
    const val NAVIGATION_SAFE_ARGS = "androidx.navigation.safeargs.kotlin"
}
