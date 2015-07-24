<?php
        error_reporting( E_ALL&~E_NOTICE );
        $urls=$_GET['urls'];
        $personName=$_GET['personName'];
	$arrayUrl=explode("@",$urls);
	$faceIds=Array();
        
	for($i=0;$i<count($arrayUrl)-1;$i++){
                $url=$arrayUrl[$i];
		$faceId=detection($url);
		array_push($faceIds,$faceId);
	}
	 
	createPerson($faceIds,$personName);
	
	trainGroup();
?>
<?php
   function detection($imageurl){
		// This sample uses the HTTP_Request2 package. (for more information: http://pear.php.net/package/HTTP_Request2)
		require_once 'HTTP/Request2.php';
		$headers = array(
		   'Content-Type' => 'application/json',
		);
		$query_params = array(
		   // Specify your subscription key
		   'subscription-key' => '7184de8434ce429788cc5b6320182368',
		   // Specify values for optional parameters, as needed
		   //'analyzesFaceLandmarks' => 'false',
		   //'analyzesAge' => 'false',
		   //'analyzesGender' => 'false',
		   //'analyzesHeadPose' => 'false',
		);

		$request = new Http_Request2('https://api.projectoxford.ai/face/v0/detections');
		$request->setMethod(HTTP_Request2::METHOD_POST);
		// Basic Authorization Sample
		// $request-setAuth('{username}', '{password}');
		$request->setHeader($headers);
		$url = $request->getUrl();
		$url->setQueryVariables($query_params);
		$body='{"url":"'.$imageurl.'"}';
		$request->setBody($body);

		try
		{
		   $request->setAdapter("curl");
		   $response = $request->send();   
		   $postArray=$response->getBody();
		   $de_json = json_decode($postArray,TRUE);
		   return $de_json[0]['faceId'];
		}
		catch (HttpException $ex)
		{
		   echo $ex;
		}
   }
   
   function createPerson($faceIds,$personName){

		// This sample uses the HTTP_Request2 package. (for more information: http://pear.php.net/package/HTTP_Request2)
		require_once 'HTTP/Request2.php';
		$headers = array(
		   'Content-Type' => 'application/json',
		);

		$query_params = array(
		   // Specify your subscription key
		   'subscription-key' => '7184de8434ce429788cc5b6320182368',
		);

		$request = new Http_Request2('https://api.projectoxford.ai/face/v0/persongroups/opentech/persons');
		$request->setMethod(HTTP_Request2::METHOD_POST);
		// Basic Authorization Sample
		// $request-setAuth('{username}', '{password}');
		$request->setHeader($headers);

		$url = $request->getUrl();
		$url->setQueryVariables($query_params);
        $body='{"faceIds":["';
		$count=count($faceIds)-1;
		for($i=0;$i<$count;$i++){
			if($i!=($count-1)){
				$body=$body.$faceIds[$i].'","';
			}else{
			   $body=$body.$faceIds[$i].'"],"name":"'.$personName.'","userData":"No data"}';
			}
		}
                echo $body;
		$request->setBody($body);

		try
		{
		   $request->setAdapter("curl");
		   $response = $request->send();
		   
		  // echo $response->getBody();
		}
		catch (HttpException $ex)
		{
		   echo $ex;
		}

   }
   
   function trainGroup(){
		// This sample uses the HTTP_Request2 package. (for more information: http://pear.php.net/package/HTTP_Request2)
		require_once 'HTTP/Request2.php';
		$headers = array(
		   'Content-Type' => 'application/json',
		);

		$query_params = array(
		   // Specify your subscription key
		   'subscription-key' => '7184de8434ce429788cc5b6320182368',
		);

		$request = new Http_Request2('https://api.projectoxford.ai/face/v0/persongroups/opentech/training');
		$request->setMethod(HTTP_Request2::METHOD_POST);
		// Basic Authorization Sample
		// $request-setAuth('{username}', '{password}');
		$request->setHeader($headers);

		$url = $request->getUrl();
		$url->setQueryVariables($query_params);
		$request->setBody("");

		try
		{
		   $request->setAdapter("curl");
		   $response = $request->send();
                   header("Location:http://obeubuntu.chinacloudapp.cn/oxford/index.php");
		   // echo $response->getBody();
		}
		catch (HttpException $ex)
		{
		   echo $ex;
		}

   }
?>
