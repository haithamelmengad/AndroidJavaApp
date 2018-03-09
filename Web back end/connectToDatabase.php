<?php
error_reporting( E_ALL ); ini_set( "display_errors", "on" );
// connect to the PostGreSQL database
// remember that variables in php are named with a $ as the first character
// we use localhost as the database and webserver are on the same machine 
$db = pg_connect('host=localhost user=user42 port=5432 dbname=user42db
password=user42password');
// check if the connection worked – the ! means not 
// in other words – 'if not $db' then exit 
if (!$db) {echo "An error occurred connecting to the database.\n";
exit; }

// get hold of the data from the table
// note that the SQL of the $query is the same as we typed above

$query = "select name, description, ST_astext(coords) as coords from firstTable"; 
$result = pg_query($db,$query);

// check if it worked, and if not exit 
if (!$result) {
echo "An error occurred running the query.\n";
exit; }
// now fetch the data
while ($row = pg_fetch_array($result)) {
	$name = $row['name']; 
	$description = $row['description']; 
	$coords = $row['coords']; 
	echo($name);
echo(" "); // put a space in between echo
($description);
echo(" "); // put a space in between 
echo($coords);
echo("<br>"); // put an HTML line break in
} // end of while loop
?>