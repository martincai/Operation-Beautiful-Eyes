package com.projectoxford;

//This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
public class PersonGroupApi {

	public void createGroup(String personGroupId) {
		try
		{
		   URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/persongroups/"+personGroupId);
		   // Specify your subscription key
		   builder.setParameter("subscription-key", Config.Subscription_key);
		   URI uri = builder.build();
		   HttpPut request = new HttpPut(uri);
		   String body="{'name':'"+personGroupId+"','userData':'no data'}";	  
		   StringEntity reqEntity = new StringEntity(body, ContentType.create("application/json"));
		   request.setEntity(reqEntity);
		}
		catch (Exception e)
		{
		   System.out.println(e.getMessage());
		}
    }
	
	  public boolean trainGroup(String groupName){
		  
		   HttpClient httpclient = HttpClients.createDefault();
		   try
		   {
		      URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/persongroups/"+groupName+"/training");
		      // Specify your subscription key
		      builder.setParameter("subscription-key", Config.Subscription_key);
		      URI uri = builder.build();
		      HttpPost request = new HttpPost(uri);
		      StringEntity reqEntity = new StringEntity("", ContentType.create("application/json"));
		      request.setEntity(reqEntity);
		      HttpResponse response = httpclient.execute(request);
		      String err=String.valueOf(response.getStatusLine());
		      if(err.indexOf("201")!=-1|err.indexOf("200")!=-1){
		    	  return false;
		      }else{
		    	  return true;
		      }
		   }
		   catch (Exception e)
		   {
		      System.out.println(e.getMessage());
		   }
		   return true;
	  }
	  public boolean getTrainStatus(String groupName){
		  HttpClient httpclient = HttpClients.createDefault();

		   try
		   {
		      URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/persongroups/"+groupName+"/training");
		      // Specify your subscription key
		      builder.setParameter("subscription-key", Config.Subscription_key);
		      URI uri = builder.build();
		      HttpGet request = new HttpGet(uri);
		      HttpResponse response = httpclient.execute(request);
		      String result=String.valueOf(response.getStatusLine());
		      int flag=result.indexOf("200");		      
		      if (flag!=-1) {
		         return true;
		      }else{
		    	 return false;
		      }
		   }
		   catch (Exception e)
		   {
			 
		      System.out.println(e.getMessage());
		      return false;
		   }
	  }
	  
	  
}

