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
-dontwarn androidx.room.**
-dontwarn androidx.sqlite.db.**

# Kotlin Coroutines
-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }

# Google Sign-In and Firebase
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }
-keep class androidx.credentials.** { *; }
-keep class com.google.android.libraries.identity.** { *; }
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable { *; }
-keep public enum com.google.android.gms.common.annotation.KeepName { *; }
-keep class com.google.android.gms.common.util.DynamiteApi { *; }
-keep class com.google.android.gms.measurement.** { *; }
-keepattributes Signature

# Firebase Crashlytics
-keep class com.google.firebase.crashlytics.** { *; }
-keep class com.google.firebase.components.ComponentRegistrar { *; }
-keepclassmembers class com.google.firebase.crashlytics.** { *; }
-keepattributes SourceFile,LineNumberTable
-dontwarn com.google.firebase.crashlytics.**

# Annotation
-keepattributes *Annotation*

# Parcelable
-keepclassmembers class ** implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Android Framework Classes
-keep public class * extends android.app.Application { *; }
-keep public class * extends android.app.Activity { *; }
-keep public class * extends android.app.Service { *; }
-keep public class * extends android.content.BroadcastReceiver { *; }
-keep public class * extends android.content.ContentProvider { *; }
-keep public class * extends androidx.fragment.app.Fragment { *; }

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

# Custom Models (Domain and Data Layers)
-keep class com.azizahfzahrr.eleccart.data.model.** { *; }
-keep class com.azizahfzahrr.eleccart.domain.model.** { *; }

# General Keep Rules
-keep class **.R$* { *; }
-keep class **.R { *; }
-keepclassmembers class ** {
    *;
}
-keep class ** {
    <init>(...);
}

# Prevent Obfuscation of Methods Called via Reflection
-keepnames class ** {
    <methods>;
}