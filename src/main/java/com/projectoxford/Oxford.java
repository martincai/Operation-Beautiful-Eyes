package com.projectoxford;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.azure.storage.AzureBlob;
import com.obe.bean.Candidate;
import com.obe.bean.Identification_result;
import com.obe.bean.Person;
import com.obe.bean.PersonApi_result;
import com.obe.bean.CatchData;

public class Oxford {
    private String imageUrl;
    private String personGroupId;
    public Oxford(String imageUrl){
    	this.imageUrl=imageUrl;
    	this.personGroupId="opentech";
    }	
    public String getMessage(){   	
    	JudgePersonGroup judge=new JudgePersonGroup();
    	judge.InitiatePersonGroup(this.personGroupId);
 	    String[] filepaths=imageUrl.split("/");
  	    String fileName=filepaths[filepaths.length-1];
  	    String containerName=filepaths[filepaths.length-2];
  	    //get FaceIds
 	    Detection detection=new Detection(imageUrl);
 	    if(!detection.getErr()){
		 		Person person=detection.getPerson();
		 		String[] faceIds=new String[1];
		 		faceIds[0]=person.getFaceId();
		      
		 	    Identification identification=new Identification(faceIds,this.personGroupId,3);
		 	    Identification_result result=identification.getIdentificationResult();
		 	    Candidate[] candidates=result.getCandidates();
		 	    
		 	    // there is no candidate, delete the image in the azure blob
		 	    if(candidates.length==0){
		 	    	AzureBlob azureBlob;
					try {
						azureBlob = new AzureBlob(Config.AzureBlobName,Config.AzureBlobKey);
						azureBlob.deleteBlob(containerName,fileName);
					} catch (InvalidKeyException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}		 	    
		 	    	return "No";
		 	    }else{
		 	        float max=0.0f;
		 	        int indice=0;
		 	    	for (int i=0;i<candidates.length;i++){
		 	    		if(candidates[i].getConfidence()>max){
		 	    			indice=i; 			
		 	    		}
		 	    	}
		 	    	Candidate candidate=candidates[indice];
		 	    	String personId=candidate.getPersonId();
		 	    	PersonApi personApi=new PersonApi();
		 	    	personApi.getPerson(personGroupId, personId);
		 	    	PersonApi_result personApiResult=personApi.getPersonApiResult();
		 	    	String name=personApiResult.getName();
		 	    	Date date=new Date();
		 	    	DateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 			String Currenttime=format.format(date);
		 			CatchData catchData=new CatchData();
		 			catchData.setImageUrl(imageUrl);
		 			catchData.setTime(Currenttime);
		 			catchData.setName(name);
		 			catchData.setConfidence(candidate.getConfidence());
		 			/*send to service bus
		 	    	   String message="有个相似度为"+String.valueOf(candidate.getConfidence())+"名叫"+name+"正在看电视在"+Currenttime;
		 	    	   ServiceBus service=new ServiceBus(Config.ServiceBusName,Config.SASName,Config.SASKey,Config.SASUrl);
		 	    	    service.sendMessage("test12", message);	*/	 	   	    
		 	   	    String json_str=JSON.toJSONString(catchData);
		 	    	return json_str;	    
		 	    }
 	    }else{
 	    	 return "No";
 	    }
    }
}
