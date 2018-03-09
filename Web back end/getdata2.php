   <?php 
        $db = pg_connect('host=localhost user=user42 port=5432 dbname=user42db password=user42password');

		//$latitude = $_REQUEST['latitude'];
		//$longitude = $_REQUEST['lognitude'];
		//echo "mmmmmmm".$longitude;
		//$coords ="ST_geomfromtext('POINT(".$latitude." ".$longitude.")')";
        $query = "SELECT id,question, answer1, answer2,answer3,answer4,correct, st_astext(coords) FROM Coords;";
        //echo $query;
		$rows = array();
		
        $result = pg_query($query); 
        if (!$result) { 
            echo "Problem with query " . $query . "<br/>"; 
            echo pg_last_error(); 
            exit(); 
        } 
        //st_astext(coords)=$latitude AND st_astext(coords)=$longitude
        //ST_PointFromText('POINT('||$longitude||' '||$latitude||')',4326)
        //AND ST_Y(coords.geometry)=".$longitude; 
        //$JSONresponse = array('data' => array('response' => $response));
	    //echo json_encode($JSONresponse);
	    //exit;
	    //echo $result;
        while($myrow = pg_fetch_assoc($result)) { 
            $rows[] = $myrow;
        }
         $JSONresponse = $rows;
        echo json_encode($JSONresponse);
        
        exit;
        ?> 
        