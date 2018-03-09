<?php

/* NOTE:  To run this example, you need to have created a new table in your database, using the following SQL

create table personCoords
(question varchar(1000),
answer A varchar(50),
answer B varchar(50),
answer C varchar(50),
answer D varchar(50), 
coords geometry);

*/


// return the data to the user so that they know something is happening

echo("This is your data<br>");
print_r($_REQUEST);
echo("<br><br>");

// now connect to the database
	$db = pg_connect('host=localhost user=user42 port=5432 dbname=user42db password=user42password');

	// check if the connection worked – the ! means not
	// in other words – 'if not $db' then exit

	if (!$db) {
	  echo "An error occurred connecting to the database.\n";
	  exit;
	}

// extract the values from the form
// we use $_REQUEST as this works no matter whether the user has used POST or GET
$latitude = $_REQUEST['latitude'];
$longitude = $_REQUEST['longitude'];
$question = $_REQUEST['question'];
$Answer_A = $_REQUEST['Answer_A'];
$Answer_B = $_REQUEST['Answer_B'];
$Answer_C = $_REQUEST['Answer_C'];
$Answer_D = $_REQUEST['Answer_D'];
$Correct_Ans = $_REQUEST['Correct_Ans'];


// format the coordinate string so that it is correctly structures for the SQL statement
// this should look something like:  geomfromtext('POINT(-0.11475563049316 51.574621144602)')
$coords ="ST_geomfromtext('POINT(".$latitude." ".$longitude.")')";

/* write the SQL insert script for the point
the general form of this statement is as follows:

insert into personCoords (name, surname, university, coords) values
*/

$query = "insert into questionsTable (question, Answer_A, Answer_B, Answer_C, Answer_D, Correct_Ans, pointcoords) values (";
$query = $query."'".$question."','".$Answer_A."','".$Answer_B."','".$Answer_C."','".$Answer_D."','".$Correct_Ans."',".$coords.")";

// show the SQL query to the user (this is useful for debugging purposes)
echo($query);
echo("<br><br>");

// run the insert query
// if the result is TRUE it has worked
if (pg_query($db,$query)) {
	echo("Your data has been saved to the databse");
}
else {
	echo("There was an error saving your data");
}


?>