package com.projectoxford;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.obe.bean.Candidate;
import com.obe.bean.Identification_result;
import com.obe.bean.Person;
import com.obe.bean.PersonApi_result;
import com.obe.bean.CatchData;

public class Oxford {	
	public CatchData catchData;
    public boolean getMessage(String imageUrl){   	
  	    //get FaceIds
 	    Detection detection=new Detection(imageUrl);  
 	    if(!detection.getErr()){
		 	Person person=detection.getPerson();
		    String[] faceIds=new String[1];
		    faceIds[0]=person.getFaceId();
		      
		 	Identification identification=new Identification(faceIds,Config.PersonGroupId,3);
		 	Identification_result result=identification.getIdentificationResult();
		 	Candidate[] candidates=result.getCandidates();
		 	    
		 	if(candidates.length==0){
		 	    	return false;
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
 	    	personApi.getPerson(Config.PersonGroupId, personId);
 	    	PersonApi_result personApiResult=personApi.getPersonApiResult();
 	    	String name=personApiResult.getName();
 	    	Date date=new Date();
 	    	DateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
 			String Currenttime=format.format(date);
 			this.catchData.setImageUrl(imageUrl);
 			this.catchData.setTime(Currenttime);
 			this.catchData.setName(name);
 			this.catchData.setPersonId(personId);
 			this.catchData.setConfidence(candidate.getConfidence()); 		
 	    	return true;	    
		 	    }
 	    }else{
 	    	 return false;
 	    }
    }
}
