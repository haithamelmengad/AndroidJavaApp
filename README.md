# AndroidJavaApp

This is a Master's level project that I completed individually at UCL for the Web and Mobile GIS module. 
It was a semester long project and the only assessed component of that class.

## Website (PHP, PostGre SQL): ##

The internet part of this application serves three purposes:
* User can create new questions and answers at given locations, and save the inputs in a PostGreSQL database (**haithamquiz_processCoords.php**)
* Each data point(question, answers, correct answer, coordinates) is processed and converted into a GeoJSON format (**haithamquiz_GeoJSON.php**)
* User responses in the app are recorded in the database (**getdata2.php**)



## Application (Java): ##

There are three main parts to the Android application:

* **ShowGeoJSONOnMapWithInfoActivity** processes the GeoJSon data to display red marker points at the locations of the questions. 
It also displays the questions when the markers are clicked, without showing the answer choices. 
When the blue marker is clicked, the activity passes data to GPSSensorActivity 
and the page switches to the latter activity’s layout.

* **GPSSensorActivity** receives the latitude and longitude of the question points. 
It then assesses the user’s location data and calculates the distance to a given question point and displays that distance. 
At 10 meters from the question, it generates a pop up with the question and answer choices. 
At a distance of 5 meters, it passes the correct answer data to the SendCheckboxToServer activity and starts the latter activity.

* **SendCheckboxToServer** prompts the User to select an answer form A, B, C and D. 
The user is also required to input his name and surname to track the input as required by the assignment. 
The data is saved in the process_form.php file in an SQL server. 
The activity then displays the correct answer to inform the user.
