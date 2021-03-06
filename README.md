Adila
=====

Adila (Advanced Device Information Library for Android) is a *Java library* for getting information about an Android device. It supports all Android versions (API 1+), requires no permissions, and does not depend on any third-party library.

Introduction
------------

Android provides developers with a [`Build`][1] class for getting information about the current build. But it does not provide a `Device` class for getting information about the device.

Adila tries to solve this by providing developers with a [`Device`](library/src/main/java/com/karimeldeeb/adila/Device.java) class. This class will try to identify the current device, at runtime, and fill the class fields with the device information (e.g. name, series) from its database.

Information
-----------

The library currently provides the following information:

- Identity (manufacturer, name, series)

Setup
-----

Gradle:

```groovy
compile 'com.karimeldeeb.adila:library:16.8.30'
compile 'com.karimeldeeb.adila:database:16.9.10'
```

Instead of using the whole `database` artifact, you should use one that matches your application `minSdkVersion`. For example, if your application `minSdkVersion` is **19**, you should use `database-v19` artifact. This way, you will be using a smaller database that only includes devices supporting API level 19 or higher.

**Note:** The database is included separately in case you want to use a different one, or make your own.

Usage
-----

To get information about the device, simply call any of `Device` class methods, or access any of its fields directly. *For example:*

```java
// Get the device name
String name = Device.NAME;
```

Database
--------

The database is just a Java-based (.jar) database, where each device has its own class. The classes are named using each device `Build.DEVICE` and `Build.MODEL`.

The [devices](devices.csv) database is included as a *.csv* file. This file is based on Google's list of [supported devices][2] and can be found [here][3]. The database is then converted to *.java* files using a Python [script](devices.py).

**Note:** The `devices.csv` file includes additional information that might, or might not, be used in the future.

ProGuard
--------

If you are using ProGuard with your application, you must add [proguard-adila.pro](proguard-adila.pro) file to `proguardFiles`. This file will stop ProGuard from removing all classes in the database.

*Example:*

```groovy
buildTypes {
    release {
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-adila.pro'
    }
}
```

[1]: http://developer.android.com/reference/android/os/Build.html
[2]: http://storage.googleapis.com/play_public/supported_devices.csv
[3]: https://support.google.com/googleplay/answer/1727131
