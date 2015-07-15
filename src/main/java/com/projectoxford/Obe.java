package com.projectoxford;

public class Obe{

	
	public static void main(String[] args) throws Exception  {

			  
        Oxford oxford=new Oxford("https://obe.blob.core.chinacloudapi.cn/martin/4.jpg");
        String message=oxford.getMessage();
        if(message=="No"){
        	
        }else{
        	//sent the message to the service bus
           System.out.println(message);
        }
	}
	
}
