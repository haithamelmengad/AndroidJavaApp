<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
package="uk.ac.ucl.cege.cegeg077.zceshel.mapsexample">

<permission
android:name="uk.ac.ucl.cege.cegeg077.zceshel.mapsexample.MAPS_RECEIVE"
android:protectionLevel="signature" />

<uses-permission android:name="uk.ac.ucl.cege.cegeg077.zceshel.mapsexample.MAPS_RECEIVE" />
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES"/>
<!-- The following two permissions are not required to use
Google Maps Android API v2, but are recommended. -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION"/>


<uses-feature
android:glEsVersion="0x00020000"
android:required="true"/>


<application
android:allowBackup="true"
android:icon="@mipmap/ic_launcher"
android:label="@string/app_name"
android:supportsRtl="true"
android:theme="@style/AppTheme">

<activity android:name=".ShowGeoJSONOnMapWithInfoActivity">
<intent-filter>
<action android:name="android.intent.action.MAIN" />

<category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
</activity>

<activity android:name=".GPSSensorActivity">
<intent-filter>
<action android:name="android.intent.action.MAIN" />

<category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
</activity>

<activity android:name=".SendCheckboxToServer">
<intent-filter>
<action android:name="android.intent.action.MAIN" />

<category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
</activity>

<meta-data
android:name="com.google.android.maps.v2.API_KEY"
android:value="AIzaSyD9O0su-RAw4bS7uiV4gcwl4E45Vh0Jr7w"/>


</application>

</manifest>





