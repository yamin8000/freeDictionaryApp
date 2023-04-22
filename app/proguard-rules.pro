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

-keep class io.github.yamin8000.owl.model.** { <fields>; }

# This is generated automatically by the Android Gradle plugin.
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

# Tapsell

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

##---------------End: proguard configuration for Gson  ----------


##---------------Begin: proguard configuration for Retrofit  ----------
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.-KotlinExtensions
##---------------End: proguard configuration for Retrofit  ----------


##---------------Begin: proguard configuration for okhttp3  ----------
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
##---------------End: proguard configuration for okhttp3  ----------


##---------------Begin: proguard configuration for okio  ----------
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
##---------------End: proguard configuration for okio  ----------



##---------------Begin: proguard configuration for admob  ----------
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# The following rules are used to strip any non essential Google Play Services classes and method.

# For Google Play Services
-keep public class com.google.android.gms.ads.**{
   public *;
}

# For old ads classes
-keep public class com.google.ads.**{
   public *;
}

# For mediation
-keepattributes *Annotation*

# Other required classes for Google Play Services
# Read more at http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
   public static final ** CREATOR;
}
##---------------End: proguard configuration for admob  ----------


##---------------Begin: proguard configuration for chartboost  ----------
-keep class com.chartboost.** { *; }
##---------------End: proguard configuration for chartboost  ----------


##---------------Begin: proguard configuration for tapsell  ----------
-keepclassmembers enum * { *; }
-keep class **.R$* { *; }
-keep interface ir.tapsell.sdk.NoProguard
-keep interface ir.tapsell.sdk.NoNameProguard
-keep class * implements ir.tapsell.sdk.NoProguard { *; }
-keep interface * extends ir.tapsell.sdk.NoProguard { *; }
-keep enum * implements ir.tapsell.sdk.NoProguard { *; }
-keepnames class * implements ir.tapsell.sdk.NoNameProguard { *; }
-keepnames class * extends android.app.Activity
-keep class ir.tapsell.plus.model.** { *; }
-keep class ir.tapsell.sdk.models.** { *; }

-keep class ir.tapsell.sdk.nativeads.TapsellNativeVideoAdLoader$Builder {*;}
-keep class ir.tapsell.sdk.nativeads.TapsellNativeBannerAdLoader$Builder {*;}

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keep interface ir.tapsell.plus.NoProguard
-keep interface * extends ir.tapsell.plus.NoProguard { *; }
-keep class * implements ir.tapsell.plus.NoProguard { *; }

##---------------End: proguard configuration for tapsell  ----------

##---------------Begin: proguard configuration for AppLovin  ----------

-dontwarn com.applovin.**
-keep class com.applovin.** { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }

##---------------End: proguard configuration for AppLovin  ----------

-keep public class com.bumptech.glide.**

# For communication with AdColony's WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# TapsellPlus needs their formal names
-keepnames public class com.google.android.gms.ads.MobileAds
-keepnames public class com.unity3d.services.monetization.IUnityMonetizationListener
-keepnames public class com.adcolony.sdk.AdColony
-keepnames public class com.google.android.gms.ads.identifier.AdvertisingIdClient
-keepnames public class com.chartboost.sdk.Chartboost
-keepnames public class com.applovin.sdk.AppLovinSdkSettings

-dontwarn com.adcolony.sdk.AdColony
-dontwarn com.adcolony.sdk.AdColonyAdSize
-dontwarn com.adcolony.sdk.AdColonyAdView
-dontwarn com.adcolony.sdk.AdColonyAdViewListener
-dontwarn com.adcolony.sdk.AdColonyAppOptions
-dontwarn com.adcolony.sdk.AdColonyInterstitial
-dontwarn com.adcolony.sdk.AdColonyInterstitialListener
-dontwarn com.adcolony.sdk.AdColonyRewardListener
-dontwarn com.android.vending.billing.IInAppBillingService$Stub
-dontwarn com.android.vending.billing.IInAppBillingService
-dontwarn com.chartboost.sdk.Chartboost
-dontwarn com.chartboost.sdk.ChartboostDelegate
-dontwarn com.chartboost.sdk.Libraries.CBLogging$Level
-dontwarn com.chartboost.sdk.Privacy.model.DataUseConsent
-dontwarn com.chartboost.sdk.Privacy.model.GDPR$GDPR_CONSENT
-dontwarn com.chartboost.sdk.Privacy.model.GDPR
-dontwarn com.chartboost.sdk.a
-dontwarn com.facebook.FacebookSdk
-dontwarn com.facebook.ads.AudienceNetworkAds$InitListener
-dontwarn com.facebook.ads.AudienceNetworkAds$InitSettingsBuilder
-dontwarn com.facebook.ads.AudienceNetworkAds
-dontwarn com.facebook.ads.InterstitialAd
-dontwarn com.facebook.ads.InterstitialAdListener
-dontwarn com.facebook.ads.RewardedVideoAd
-dontwarn com.facebook.ads.RewardedVideoAdListener
-dontwarn com.google.ads.mediation.admob.AdMobAdapter
-dontwarn com.google.android.gms.ads.AdListener
-dontwarn com.google.android.gms.ads.AdLoader$Builder
-dontwarn com.google.android.gms.ads.AdLoader
-dontwarn com.google.android.gms.ads.AdRequest$Builder
-dontwarn com.google.android.gms.ads.AdRequest
-dontwarn com.google.android.gms.ads.AdSize
-dontwarn com.google.android.gms.ads.AdView
-dontwarn com.google.android.gms.ads.FullScreenContentCallback
-dontwarn com.google.android.gms.ads.MobileAds
-dontwarn com.google.android.gms.ads.OnUserEarnedRewardListener
-dontwarn com.google.android.gms.ads.interstitial.InterstitialAd
-dontwarn com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
-dontwarn com.google.android.gms.ads.nativead.MediaView
-dontwarn com.google.android.gms.ads.nativead.NativeAd$Image
-dontwarn com.google.android.gms.ads.nativead.NativeAd$OnNativeAdLoadedListener
-dontwarn com.google.android.gms.ads.nativead.NativeAd
-dontwarn com.google.android.gms.ads.nativead.NativeAdView
-dontwarn com.google.android.gms.ads.rewarded.RewardedAd
-dontwarn com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
-dontwarn com.squareup.picasso.Picasso
-dontwarn com.squareup.picasso.RequestCreator
-dontwarn com.squareup.picasso.Target
-dontwarn com.unity3d.ads.IUnityAdsInitializationListener
-dontwarn com.unity3d.ads.IUnityAdsLoadListener
-dontwarn com.unity3d.ads.IUnityAdsShowListener
-dontwarn com.unity3d.ads.UnityAds
-dontwarn com.unity3d.ads.metadata.MetaData
-dontwarn com.unity3d.services.banners.BannerView$IListener
-dontwarn com.unity3d.services.banners.BannerView
-dontwarn com.unity3d.services.banners.UnityBannerSize
-dontwarn com.vungle.warren.AdConfig
-dontwarn com.vungle.warren.InitCallback
-dontwarn com.vungle.warren.LoadAdCallback
-dontwarn com.vungle.warren.PlayAdCallback
-dontwarn com.vungle.warren.Vungle$Consent
-dontwarn com.vungle.warren.Vungle

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation