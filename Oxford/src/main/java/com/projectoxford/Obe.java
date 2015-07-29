package com.projectoxford;

import com.obe.bean.CatchData;

public class Obe{

	
	public static void main(String[] args) throws Exception  {		  
        Oxford oxford=new Oxford();
        String message=oxford.getMessage("https://obe.blob.core.chinacloudapi.cn/jack/1e2.jpg");
        if(message==){
           //not catch,do nothing
        }else{
           //send the message to the service bus queue
        }

	}
	
}
