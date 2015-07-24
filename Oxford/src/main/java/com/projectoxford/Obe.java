package com.projectoxford;

public class Obe{

	
	public static void main(String[] args) throws Exception  {		  
        Oxford oxford=new Oxford();
        String message=oxford.getMessage("https://obe.blob.core.chinacloudapi.cn/jack/123.jpg");
        if(message=="No"){
        	//do nothing
        }else{
        	//sent the message to the service bus
        	//data style:{"confidence":1,"imageUrl":"https://obe.blob.core.chinacloudapi.cn/martin/4.jpg","name":"martin","time":"2015-07-21 10:26:17"}
            System.out.println(message);
        }
	}
	
}
