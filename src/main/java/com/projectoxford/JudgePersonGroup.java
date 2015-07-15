package com.projectoxford;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JudgePersonGroup {
	
	public void InitiatePersonGroup(String personGroupId){
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String Currenttime=format.format(date);
		PersonGroupApi personGroup=new PersonGroupApi();
        if(Config.datetime.equals(Currenttime)){
        	if(!personGroup.getTrainStatus(personGroupId)){
        		while(true){
                	boolean flag=personGroup.trainGroup(personGroupId);
                	if(flag){
                		break;
                	}
                }
        	}
        }else{
        	UpdateFile update=new UpdateFile(personGroupId);
            update.UpdatePersonGroup();
            try {
				Thread.sleep(40000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            while(true){
            	boolean flag=personGroup.trainGroup(personGroupId);
            	if(flag){
            		break;
            	}
            }
        }
	}

}
