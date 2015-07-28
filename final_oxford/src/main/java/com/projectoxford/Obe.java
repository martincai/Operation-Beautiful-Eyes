package com.projectoxford;

import com.obe.bean.CatchData;

public class Obe{

	
	public static void main(String[] args) throws Exception  {		  
        Oxford oxford=new Oxford();
        CatchData catchData=new CatchData();
        boolean message=oxford.getMessage("https://obe.blob.core.chinacloudapi.cn/jack/1e2.jpg");
        if(message==true){
        	catchData=oxford.catchData;
        	//send service bus
        }else{
        	//do nothing
        }
	}
	
}
