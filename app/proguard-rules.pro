# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*

-dontobfuscate

# Keep Model
-keep class me.rerere.xland.data.model.** { *; }
-keepclasseswithmembers class me.rerere.xland.** {
    public ** component1();
    <fields>;
}

# Disable ServiceLoader reproducibility-breaking optimizations
-keep class kotlinx.coroutines.CoroutineExceptionHandler
-keep class kotlinx.coroutines.internal.MainDispatcherFactory