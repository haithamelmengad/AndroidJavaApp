<?php

/* This script retrieves data from the quizquestions table in the user42 database on the CEGE developer server. This data is then outputed as a geoJSON file, which
is downloaded by the phone based app.
 */
                                                                         
    // connect to the PostGreSQL database
    // remember that variables in php are named with a $ as the first character
    // we use localhost as the database and webserver are on the same machine
    $db = pg_connect('host=localhost user=user42 port=5432 dbname=user42db password=user42password');

    // check if the connection worked, if not then exit
    if (!$db) {
        echo("An error occured connecting to your database.\n");
        exit;
    }

    // Get hold of the data from the table
    // Note that the SQL of the $query is the same as we typed above
    $query = "SELECT question, Answer_A, Answer_B, Answer_C, Answer_D, Correct_Ans, ST_astext(pointcoords) as pointcoords from questionsTable";
    $result = pg_query($db, $query);
    
    // Check if it worked, if not then exit
    if (!$result) {
        echo("An error occurred running the database query.\n");
        exit;
    }

# Build GeoJSON feature collection array
$geojson = array(
    'type' => 'FeatureCollection',
    'features' => array());

// add a simple counter to count the number of points
// this is used by GoogleMaps on Android to display the correct pop-up on the point
$id = 0;

# Loop through rows to build feature arrays

while($row = pg_fetch_array($result)) {
    
    // first get all the attributes - these are the 'properties of the point'
    $properties = $row;
    
    // we don't need the coordinates, so remove these from the attributes as we are handling them separately
    unset($properties['pointcoords']);
    
    // now handle the coords: these are currently a string so we just need to extract the lat/lng
    $coords = $row['pointcoords'];
    
    // first remove the word 'POINT' and replace it with " (i.e. no text)
    $coords = str_replace('POINT(','',$coords);
    
    // then we need to remove the right bracket and replace it with "
    $coords = str_replace(')','',$coords);

    // now extract the $lat and $lng values from the remaining string
    // this can be done by creating an array using the explode function
    $coordvalues = explode(' ',$coords);

    $lat = $coordvalues[0];
    $lng = $coordvalues[1];
    
    // add 1 to the point counter
    $id = $id+1;
    
    $feature = array(
    'type' => 'Feature',
    'properties' => $properties,
    'geometry' => array(
        'type' => 'Point',
        'coordinates' => array(
            $lat,
            $lng
            )
        ),
        'id' => $id);
    
    # Add feature arrays to feature collection array
    array_push($geojson['features'], $feature);
}

// now echo the data out - note that we are setting the type of file to a JSON file using the 'header' statement
header('Content-type: application/json');
echo json_encode($geojson, JSON_NUMERIC_CHECK);

// disconnect from the database: this is good practice
$db = NULL;

?>
    