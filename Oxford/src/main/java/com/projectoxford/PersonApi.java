package com.projectoxford;

//This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSON;
import com.obe.bean.PersonApi_result;

public class PersonApi {
	private PersonApi_result personApiResult;

	public void createPerson(String[] faceIds,String personGroupId,String personName) {
			HttpClient httpclient = HttpClients.createDefault();
			
			try
			{
			   URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/persongroups/"+personGroupId+"/persons");
			   // Specify your subscription key
			   builder.setParameter("subscription-key", "7184de8434ce429788cc5b6320182368");
			   URI uri = builder.build();
			   HttpPost request = new HttpPost(uri);
			   String body="{'faceIds':[";
			   for(int i=0;i<faceIds.length;i++)
			   {
				   if(i!=faceIds.length-1) {			      
				    	body=body+"'"+faceIds[i]+"',";
				   }else{
					   body=body+"'"+faceIds[i]+"'],'name':'"+personName+"','userData':'no data'}"; 
				   }
			   }
			   StringEntity reqEntity = new StringEntity(body, ContentType.create("application/json"));
			   request.setEntity(reqEntity);
			   HttpResponse response = httpclient.execute(request);
			   HttpEntity entity = response.getEntity();
			   if (entity != null) {
			      //System.out.println(EntityUtils.toString(entity));
			   }
			}
			catch (Exception e)
			{
			   System.out.println(e.getMessage());
			}
    }
	
	public void addFace(String personGroupId,String personId,String faceId){
		HttpClient httpclient = HttpClients.createDefault();
		   try
		   {
		      URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/persongroups/"+personGroupId+"/persons/"+personId+"/faces/"+faceId);
		      // Specify your subscription key
		      builder.setParameter("subscription-key", "7184de8434ce429788cc5b6320182368");
		      URI uri = builder.build();
		      HttpPut request = new HttpPut(uri);
		      String body="{'userData':'no data'}";
		      StringEntity reqEntity = new StringEntity(body, ContentType.create("application/json"));
		      request.setEntity(reqEntity);
		      HttpResponse response = httpclient.execute(request);
		      HttpEntity entity = response.getEntity();
		      if (entity != null) {
		         System.out.println(EntityUtils.toString(entity));
		      }
		   }
		   catch (Exception e)
		   {
		      System.out.println(e.getMessage());
		   }
	 }
	
	public void getPerson(String personGroupId,String personId){
		   HttpClient httpclient = HttpClients.createDefault();
		   try
		   {
		      URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/persongroups/"+personGroupId+"/persons/"+personId);
		      // Specify your subscription key
		      builder.setParameter("subscription-key", "7184de8434ce429788cc5b6320182368");
		      URI uri = builder.build();
		      HttpGet request = new HttpGet(uri);
		      HttpResponse response = httpclient.execute(request);
		      HttpEntity entity = response.getEntity();
		      if (entity != null) {
		    	  String jsonStr=EntityUtils.toString(entity);
			      this.personApiResult=JSON.parseObject(jsonStr,PersonApi_result.class);
		      }
		   }
		   catch (Exception e)
		   {
		      System.out.println(e.getMessage());
		   }
	}

	public PersonApi_result getPersonApiResult() {
		return personApiResult;
	}

	public void setPersonApiResult(PersonApi_result personApiResult) {
		this.personApiResult = personApiResult;
	}
}

