# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Retrofit and OkHttp
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-keep class com.squareup.okhttp3.** { *; }
-keep class * implements retrofit2.Call
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Room Database
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Database class * { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static **_Impl();
}

# Kotlin Coroutines
-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }

# Google Sign-In and Firebase
-keep class androidx.credentials.** { *; }
-keep class com.google.android.libraries.identity.googleid.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }
-keep class com.google.android.libraries.identity.** { *; }

# LoginActivity
-keep class com.azizahfzahrr.eleccart.presentation.view.login.LoginActivity

# Annotation
-keepattributes *Annotation*

# Parcelable
-keepclassmembers class ** implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Android Classes
-keep public class * extends android.app.Application { *; }
-keep public class * extends android.app.Activity { *; }
-keep public class * extends android.app.Service { *; }
-keep public class * extends android.app.Fragment { *; }
-keep public class * extends androidx.fragment.app.Fragment { *; }
-keep public class * extends android.content.BroadcastReceiver { *; }
-keep public class * extends android.content.ContentProvider { *; }

# Hilt and Dagger
-keep class dagger.hilt.** { *; }
-keep class dagger.internal.** { *; }
-dontwarn dagger.hilt.**
-dontwarn javax.inject.**
-dontwarn dagger.internal.**

-keep @dagger.hilt.InstallIn class * { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keepclassmembers class * {
    @dagger.Provides <methods>;
    @dagger.Binds <methods>;
}

# Preserve line number information for debugging stack traces
# Uncomment if debugging stack trace information is needed
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Custom Models (Domain and Data Layers)
-keep class com.azizahfzahrr.eleccart.data.model.** { *; }
-keep class com.azizahfzahrr.eleccart.domain.model.** { *; }

# General Keep Rules for Project Classes
-keep class ** {
    <init>(...);
}
-keep class **.R$* { *; }
-keep class **.R { *; }
-keepclassmembers class ** {
    *;
}

# Additional Rules for Room
-dontwarn androidx.sqlite.db.**
-dontwarn androidx.room.**

# Prevent Obfuscation of Methods Called via Reflection
-keepnames class ** {
    <methods>;
}