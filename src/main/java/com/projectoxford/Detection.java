package com.projectoxford;
//This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
//Face - Detection
//Detects human faces in an image and returns face locations, face landmarks, and optional attributes including head-pose, gender, and age. Detection is an essential API that provides faceId to other APIs like Identification, Verification, and Find Similar. 
//•The supported input image formats includes JPEG, PNG, GIF(the first frame), BMP, and image file size should be no larger than 4MB.
//•Input parameter face landmarks, head-poses, gender, and age are optional. You need to specify them in the URL to turn on the functions. Otherwise, only face rectangles will be returned.
//•The detectable face size range is 36x36 to 4096x4096 pixels. The faces out of this range will not be detected.
//•For each image, the maximum number of faces returned is 64 and the faces are ranked by face rectangle size in descending order.
//•Some faces may not be detected for technical challenges, e.g. very large face angles (head-pose), large occlusion. Frontal and near-frontal faces have the best results.
//•The attribute headPose's pitch value is reserved as 0.0
//•The attributes gender and age are still experimental and may not be very accurate for now.
//•The face ID will expire 24 hours after detection.

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.obe.bean.Person;

public class Detection {
    private Person person;
    private boolean err=true;
    public Detection(String imageUrl) {
    	HttpClient httpclient = HttpClients.createDefault();	
		try
		{
		   URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/detections");
		   // Specify your subscription key
		   builder.setParameter("subscription-key", Config.Subscription_key);
		   // Specify values for optional parameters, as needed
		   // builder.setParameter("analyzesFaceLandmarks", "false");
		   builder.setParameter("analyzesAge", "true");
		   builder.setParameter("analyzesGender", "true");
		   builder.setParameter("analyzesHeadPose", "true");
		   URI uri = builder.build();
		   HttpPost request = new HttpPost(uri);
		   String body="{'url':'"+imageUrl+"'}";
		   StringEntity reqEntity = new StringEntity(body, ContentType.create("application/json"));
		   request.setEntity(reqEntity);
		   HttpResponse response = httpclient.execute(request);
		   HttpEntity entity = response.getEntity();
		   String responseStatus=String.valueOf(response.getStatusLine());

		   if (responseStatus.indexOf("200")!=-1) {		
		      String jsonStr=EntityUtils.toString(entity);
		      jsonStr=jsonStr.substring(1,jsonStr.length()-1);
		      this.person=JSON.parseObject(jsonStr,Person.class);
		      err=false;
		   }
		   if(responseStatus.indexOf("429")!=-1){
			   System.out.println("rate time is out ,wait for a minute.....");
			   Thread.sleep(40000);
			   System.out.println("end");
		   }
		}
		catch (Exception e)
		{
		   System.out.println(e.getMessage());
		}
		
	}
	public boolean getErr() {
		return err;
	}
	public void setErr(boolean err) {
		this.err = err;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}

	
  
}

