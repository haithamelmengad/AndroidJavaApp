package uk.ac.ucl.cege.cegeg077.zceshel.mapsexample;
//=============================

//Note that code in this file has been adapted from: https://github.com/googlemaps/android-maps-utils/blob/master/demo/src/com/google/maps/android/utils/demo/GeoJsonDemoActivity.java
//This code has been readapted from Claire Ellul by Haitham El Mengad
//This activity is the first to appear when the app is launched: the user sees a map centered on UCL
//The start-point is defined by a blue marker on the map
//The quiz locations are uploaded from GeoJSON and are represented by red markers on the map
//When the user clicks on a red marker the question appears in a text box above the marker asking
//to come closer
//When the user clicks on the blue marker the GPSSensorActivity is launched


import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import android.webkit.WebView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;  // import google maps package
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;  // allows to use a fragment of Google Map
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener; // Tracks marker clicks
import com.google.android.gms.common.ConnectionResult;
import com.google.maps.android.geojson.GeoJsonFeature;  // allows to use GeoJSON
import com.google.maps.android.geojson.GeoJsonPointStyle;
import com.google.maps.android.kml.KmlContainer;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;
import com.google.maps.android.kml.KmlPolygon;


import org.json.JSONObject;  // allows to use JSON formatting for data transfer from one activity to another
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import android.content.Intent;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPointStyle;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class ShowGeoJSONOnMapWithInfoActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener {
    private static TextView latitude;
    private static TextView longitude;

    private GeoJsonLayer mLayer;
    private GoogleMap map;
    private final static String mLogTag = "GeoJsonDemo";
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign the map fragment to a variable so that we can manipulate it
        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        // set up the code to call onMapReady once the map is drawn
        // Async here refers to Asynchronous -i.e. the system will only call the next method once
        // the map is drawn (normally a line of code runs immediately after the previous one, i.e. is synchronous)
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(final GoogleMap newmap) {
        // centre the map on UCL at the start
        // NB - at this point the map hasn't initialised so we also need to define the map size
        // this is set to the screen size by default so we first need to get the size
        // of the screen in pixels
        LatLngBounds UK = new LatLngBounds(new LatLng(51, -0.5), new LatLng(51.5, 0.5));
        LatLng mapCentre = new LatLng(51.5246, -0.1340); // centers the map on UCL
        map = newmap;
        Marker marker1;
        marker1 = map.addMarker(new MarkerOptions() // define blue marker characteristics
                .position(mapCentre)
                .title("A message for the adventurer") // text above first marker
                .snippet("Your journey begins here")
                // the following line colors the marker in blue
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(UK, 1, 1, 0));
        retrieveFileFromUrl();
        map.setOnMarkerClickListener((OnMarkerClickListener) this);
    }

    // these 4 lines serve to upload GeoJSON formatted data from the UCL server to the app
    private void retrieveFileFromUrl() {
        String mGeoJsonUrl
                = "http://developer.cege.ucl.ac.uk:30522/teaching/user42/haithamquiz_GeoJSON.php";
        DownloadGeoJsonFile downloadGeoJsonFile = new DownloadGeoJsonFile();
        downloadGeoJsonFile.execute(mGeoJsonUrl);
    }



    // Here the GeoJSon data is used to generate points on the map
    // The GeoJSon features associated with the point are used for the options
    private void addColorsToMarkers() {
        // Iterate over all the features stored in the layer
        for (GeoJsonFeature feature : mLayer.getFeatures()) {

            // Get the icon for the feature: the marker is red
            BitmapDescriptor pointIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

            // Create a new point style
            GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();

            // Set options for the point style
            pointStyle.setIcon(pointIcon);
            // Sets the GeoJSON feature "question" as the title of the point
            pointStyle.setTitle(feature.getProperty("question"));
            pointStyle.setSnippet("Come closer to answer the question");

            // Assign the point style to the feature
            feature.setPointStyle(pointStyle);

        }
    }

    // The following is used to convert the GeoJSON file contents to JSON Objects
    private class DownloadGeoJsonFile extends AsyncTask<String, Void, JSONObject> {
        protected JSONObject doInBackground(String... params) {
            try {
                // Open a stream from the URL
                InputStream stream = new URL(params[0]).openStream();

                String line;
                StringBuilder result = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                while ((line = reader.readLine()) != null) {
                    // Read and save each line of the stream
                    result.append(line);
                }

                // Close the stream
                reader.close();
                stream.close();

                // Convert result to JSONObject
                return new JSONObject(result.toString());

            } catch (IOException e) {
                Log.e(mLogTag, "GeoJSON file could not be read");
            } catch (JSONException e) {
                Log.e(mLogTag, "GeoJSON file could not be converted to a JSONObject");
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                // Create a new GeoJsonLayer, pass in downloaded GeoJSON file as JSONObject
                mLayer = new GeoJsonLayer(map, jsonObject);
                // Add the layer onto the map
                addColorsToMarkers();
                mLayer.addLayerToMap();
            }
        }


    }
    //
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.getTitle().equals("A message for the adventurer")) {
            Intent intent = new Intent();
            // set the class name for the intent - this will tell Android which Java file to use
            // to start the activity "GPSSensorActivity
            intent.setClassName("uk.ac.ucl.cege.cegeg077.zceshel.mapsexample.ShowGeoJSONOnMapWithInfoActivity", "uk.ac.ucl.cege.cegeg077.zceshel.MapsExample.GPSSensorActivity");

            // pass the URL to the next activity using the intent
            intent.putExtra("question",JSONObject.class); // transfers the question
            // tranfer the answer choices
            intent.putExtra("answer_a",JSONObject.class);
            intent.putExtra("answer_b",JSONObject.class);
            intent.putExtra("answer_c",JSONObject.class);
            intent.putExtra("answer_d",JSONObject.class);
            // transfer the correct answer
            intent.putExtra("correct_ans",JSONObject.class);
            // transfer the point coordinates
            intent.putExtra("pointcoords",JSONObject.class);

            // start the activity
            startActivity(intent);
            return true;
        }

        return false;
    }
}





