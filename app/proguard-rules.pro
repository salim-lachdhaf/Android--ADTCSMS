-keep class com.google.ads.** # Don't proguard AdMob classes
-dontwarn com.google.ads.**
-keep public class com.google.android.gms.ads.** {
    public *;
}

-keep public class com.google.ads.** {
    public *;
}