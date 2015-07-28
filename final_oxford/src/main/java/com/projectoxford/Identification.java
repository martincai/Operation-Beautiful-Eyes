package com.projectoxford;

//This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
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
import com.obe.bean.Identification_result;

public class Identification {

	    private Identification_result identificationResult;
		public Identification_result getIdentificationResult() {
			return identificationResult;
		}
		public void setIdentificationResult(Identification_result identificationResult) {
			this.identificationResult = identificationResult;
		}
		public Identification(String[] faceIds,String faceGroupId,int maxNumOfCandidatesReturned) {
		HttpClient httpclient = HttpClients.createDefault();
		try
		{
		   URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/identifications");
		   // Specify your subscription key
		   builder.setParameter("subscription-key", Config.Subscription_key);
		   URI uri = builder.build();
		   HttpPost request = new HttpPost(uri);
		   String body="{'faceIds':[";
		   for(int i=0;i<faceIds.length;i++)
		   {
			   if(i!=faceIds.length-1) {			      
			    	body=body+"'"+faceIds[i]+"',";
			   }else{
				   body=body+"'"+faceIds[i]+"'],'personGroupId':'"+faceGroupId+"','maxNumOfCandidatesReturned':"+maxNumOfCandidatesReturned+"}"; 
			   }
		   }
		   
		   StringEntity reqEntity = new StringEntity(body, ContentType.create("application/json"));
		   request.setEntity(reqEntity);
		   HttpResponse response = httpclient.execute(request);
		   HttpEntity entity = response.getEntity();
		   if (entity != null) {
		      String jsonStr=EntityUtils.toString(entity);
		      jsonStr=jsonStr.substring(1,jsonStr.length()-1);
		      this.identificationResult=JSON.parseObject(jsonStr,Identification_result.class);		     
		   }
		}
		catch (Exception e)
		{
		   System.out.println(e.getMessage());
		}
}
}
