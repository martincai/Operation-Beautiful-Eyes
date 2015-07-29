<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>显示图片</title>

</head>
<body>
<?php
define('ROOT',dirname(__FILE__).'/'); 
if($_POST['tijiao']){
 extract($_POST);
 $i=0;
 $urls=Array();
 $personName=$_POST['personName'];
foreach ($_FILES["upfile"]["error"] as $key => $error) {
   $url="";
   if ($error == UPLOAD_ERR_OK) {
       $temp_url=upload_multi($_FILES["upfile"],$i); 
	   $url="http://obeubuntu.chinacloudapp.cn/oxford/image/".$temp_url;
	   array_push($urls,$url);
   }
   $i++;
}
}
receive($urls,$personName);
?>
<?php
function upload_multi($file,$i){
        //全局变量
		$arrType=array('image/jpg','image/gif','image/png','image/bmp','image/jpeg');
		$max_size='3670016';      // 最大文件限制（单位：byte）
		$upfile='./image'; //图片目录路径
		  
	    if($_SERVER['REQUEST_METHOD']=='POST'){ //判断提交方式是否为POST
		 if(!is_uploaded_file($file['tmp_name'][$i])){ //判断上传文件是否存在
		 echo "<font color='#FF0000'>文件不存在！</font>";
		 exit;
		}
	     
	    if($file['size'][$i]>$max_size){  //判断文件大小是否大于500000字节
		echo "<font color='#FF0000'>上传文件太大！</font>";
		exit;
	    } 
	    if(!in_array($file['type'][$i],$arrType)){  //判断图片文件的格式
		 echo "<font color='#FF0000'>上传文件格式不对！</font>";
		 exit;
	   }
	   if(!file_exists($upfile)){  // 判断存放文件目录是否存在
	   mkdir($upfile,0777,true);
	   } 
	   $imageSize=getimagesize($file['tmp_name'][$i]);
	   $img=$imageSize[0].'*'.$imageSize[1];
	   $fname=$file['name'][$i];
	   $ftype=explode('.',$fname);
	   $today=time();
	   $returnUrl=$today.rand(1,999).$fname;
	   $picName=$upfile."/".$returnUrl;
	   if(file_exists($picName)){
		echo "<font color='#FF0000'>同文件名已存在！</font>";
		exit;
		 }
	   if(!move_uploaded_file($file['tmp_name'][$i],$picName)){  
		echo "<font color='#FF0000'>移动文件出错！</font>";
		exit;
		}
	   else{
		echo "图片预览：<br><div style='border:#F00 1px solid; width:200px;height:200px'>
		<img src=\"".$picName."\" width=200px height=200px></div>";
		}
		}
		return $returnUrl;		
}	
	
?>
<?php
    function receive($arrayUrl,$personName){
		
	$faceIds=Array();
        
	for($i=0;$i<count($arrayUrl);$i++){
                $url=$arrayUrl[$i];
		$faceId=detection($url);
		array_push($faceIds,$faceId);
	}
	 
	createPerson($faceIds,$personName);
	
	trainGroup();
	}
	
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
</body>
</html>