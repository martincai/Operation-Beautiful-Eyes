package com.projectoxford;
//This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat; 

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Findsimilars {

	//记录匹配成功
	int flag=0;
	public Findsimilars(String faceId,String[] faceIds) {
		HttpClient httpclient = HttpClients.createDefault();
		try
		{
		   URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/findsimilars");
		   // Specify your subscription key
		   builder.setParameter("subscription-key", Config.Subscription_key);
		   URI uri = builder.build();
		   HttpPost request = new HttpPost(uri);
		   String body="{'faceId':'"+faceId+"','faceIds':[";
		   for(int i=0;i<faceIds.length;i++)
		   {
			   if(i!=faceIds.length-1) {			      
			    	body=body+"'"+faceIds[i]+"',";
			   }else{
				   body=body+"'"+faceIds[i]+"']}"; 
			   }
		   }
		   StringEntity reqEntity = new StringEntity(body, ContentType.create("application/json"));
		   request.setEntity(reqEntity);
		   HttpResponse response = httpclient.execute(request);
		   HttpEntity entity = response.getEntity();
		   String result=EntityUtils.toString(entity);
		   if (result.isEmpty()) {
			   flag=1;
		   }
		}
		catch (Exception e)
		{
		   System.out.println(e.getMessage());
		}
	}

	
	public String isYou(){
		if(flag==1){
			    Date dt=new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//设置显示格式
				String nowTime="";
				nowTime= df.format(dt);//用DateFormat的format()方法在dt中获取并以yyyy/MM/dd HH:mm:ss格式显示
			    return "Martin's daughter has been watching TV in a wrong position in "+nowTime;
		}else{
			    return "No";
		}
	}
       
}

