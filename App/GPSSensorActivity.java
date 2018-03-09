package uk.ac.ucl.cege.cegeg077.zceshel.mapsexample;


// This is the activity that launches upon clicking on the blue marker (ShowGeoJSonOnMapWithInfoActivity)
// The activity tracks the location of the user and the distance to the quiz location
// It displays the distance between the user and the quiz location (see layout)

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;


public class GPSSensorActivity extends AppCompatActivity {

    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
    private LocationManager locationManager;

    // Get data from other activity

    Intent intent= getIntent();
    // Select the coordinates of the point(s) from ShowGeoJSONOnMapWithInfoActivity and
    // Explode into two variables one for latitude, one for longitude
    Double longitude= intent.getDoubleExtra("coordinates", 51.5246);
    Double latitude= intent.getDoubleExtra("coordinates", -0.1340);
    // Upload question, answers and correct answer
    String question= intent.getStringExtra("question");
    String answer_a= intent.getStringExtra("answer_a");
    String answer_b= intent.getStringExtra("answer_b");
    String answer_c= intent.getStringExtra("answer_c");
    String answer_d= intent.getStringExtra("answer_d");
    String correct_ans= intent.getStringExtra("correct_ans");

    // This class formats the point coordinates as (latitude, longitude)
    public class GeoPoint {
        private Double latitude;
        private Double longitude;

        public GeoPoint(Double lat, Double lng) {
            latitude = lat;
            longitude = lng;
        }
        public Double getLongitude() {
            return longitude;
        }
        public Double getLatitude() {
            return latitude;
        }
    }

    // This class is used to track the user's location using the phone's location services
    public class CustomLocationListener extends GPSSensorActivity {
        public GPSSensorActivity parentActivity;
        public ArrayList<GeoPoint> pointList;
        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_basic_gpssensor);

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            CustomLocationListener customLL = new CustomLocationListener();
            customLL.parentActivity = this;

            // the listener will be checking at regular intervals to see if the
            // device has changed location.v
            // create the hard-coded list of points of interest based on the data above
            ArrayList<GeoPoint> pointList;
            pointList = new ArrayList<GeoPoint>();
            // checkpoint: the distance between the user and this point will be tracked
            GeoPoint gMapPoint = new GeoPoint(longitude, latitude);


            // add these points to the list
            pointList.add(gMapPoint);


            // Set up the location manager and listener
            customLL.pointList = pointList;
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MINIMUM_TIME_BETWEEN_UPDATE,
                    MINIMUM_DISTANCECHANGE_FOR_UPDATE,
                    (LocationListener) customLL
            );
        }



        GPSSensorActivity.GeoPoint gMapPoint = new GPSSensorActivity.GeoPoint(longitude, latitude);
        // this method is called every time the user moves around - i.e. changes location
        // it pops up a toast message with the new coordinates
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            Toast.makeText(parentActivity.getBaseContext(),
                    "You have moved to: Latitude/longitude:" + location.getLatitude() + " " + location.getLongitude(),
                    Toast.LENGTH_LONG).show();

            // now measure distance from all the pre-set proximity alerts
            for (int i = 0; i < pointList.size(); i++) {
                GeoPoint gp = pointList.get(i);
                Location fixedLoc = new Location("one");

                Double lat = Double.valueOf(String.valueOf(gp.getLatitude()));
                Double lng = Double.valueOf(String.valueOf(gp.getLongitude()));

                fixedLoc.setLatitude(lat);
                fixedLoc.setLongitude(lng);
                Log.i("location", lat + " " + location.getLatitude());
                Log.i("location", lng + " " + location.getLongitude());

                // the Android distanceTo function is used to calculate the distances

                float distance = location.distanceTo(fixedLoc);

                // This loop displays the distance from the quiz location

                if (i == 0); {

                    TextView tv1 = (TextView)
                            parentActivity.findViewById(R.id.textView1);

                    tv1.setText("Distance from Checkpoint:" + String.valueOf(distance));

                }

                // When the user is within 10 meters of the checkpoint, the answer choices pop up
                if (distance < 10) {

                    Toast.makeText(parentActivity.getBaseContext(),
                            question + answer_a + answer_b + answer_c + answer_d, Toast.LENGTH_LONG).show();

                }

                // When the user is within 5 meters of the checkpoint, the activity
                //  SendCheckBoxToSer is started
                if (distance < 5); {

                    Intent intent = new Intent();

                    // set the class name for the intent - this will tell Android which Java file to use
                    // to start the activity

                    intent.setClassName("uk.ac.ucl.cege.cegeg077.zceshel.mapsexample.GPSSensorActivity", "uk.ac.ucl.cege.cegeg077.zceshel.mapsexample.SendCheckBoxToServer");

                    // send the correct answer to SendCheckboxToServer
                    intent.putExtra("correct_ans", correct_ans);

                    // start the activity
                    startActivity(intent);
                    return true;
                }
            }
        }


    }
    // these methods are called when the GPS is switched on or off
    // and will allow the App to warn the user and then
    // shut down without an error

    public void onProviderDisabled(String s) {
    }

    public void onProviderEnabled(String s) {
    }

    // this method is required by the LocationListener
    // we do not need to do anything here
    // but in a full implementation this could be used to react when the GPS signal is not available
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }
}
