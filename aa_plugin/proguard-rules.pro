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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-repackageclasses com.futureworkshops.mobileworkflow.plugin.aa

-keep class * extends com.futureworkshops.mobileworkflow.model.step.Step { *; }
-keep class * extends com.futureworkshops.mobileworkflow.model.step.PluginStep { *; }
-keep class * extends com.futureworkshops.mobileworkflow.model.result.StepResult { public *; }
-keep class * extends com.futureworkshops.mobileworkflow.model.result.FragmentStepResult { *; }
-keep class * extends com.futureworkshops.mobileworkflow.model.result.AnswerResult { *; }

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.futureworkshops.mobileworkflow.plugin.aa.step.* { *; }

-keep class * extends com.futureworkshops.mobileworkflow.domain.PluginFactory { *; }
-keep class * extends com.futureworkshops.mobileworkflow.domain.DeserializeStep { *; }

-keepclassmembers class **.R$* {
       public static <fields>;
}

-keep class * extends android.os.Parcelable { *; }

-keep class com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration { public *; }