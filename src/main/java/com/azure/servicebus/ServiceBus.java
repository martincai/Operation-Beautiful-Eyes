package com.azure.servicebus;
import com.microsoft.windowsazure.services.servicebus.*;
import com.microsoft.windowsazure.services.servicebus.models.*;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;

public class ServiceBus 
{
   private Configuration config;
   private ServiceBusContract service;
   
  // you can get a connectstring from azure portal 
   //Endpoint=sb://wangdimmy.servicebus.chinacloudapi.cn/;SharedAccessKeyName=RootManageSharedAccessKey;
   //SharedAccessKey=Vr/FSTY81MlI21f+EqDk0hFDiQehPqZROIG2SHooK0Q=
   //azure service buse's namespace,eg,wangdimmy
   
   String namespace;
   //azure service buse's SharedAccessKeyName,eg.,RootManageSharedAccessKey
   String shareAccessKeyName;
   //azure service bus's ShareAccessKey,eg.Vr/FSTY81MlI21f+EqDk0hFDiQehPqZROIG2SHooK0Q=
   String shareAccessKey;
   //azure service bus's serviceBusRootUri,eg.,.servicebus.chinacloudapi.cn
   String serviceBusRootUri;
   public ServiceBus(String namespace,String shareAccessKeyName,String shareAccessKey,String serviceBusRootUri)
   {
	  this.namespace=namespace;
	  this.shareAccessKeyName=shareAccessKeyName;
	  this.shareAccessKey=shareAccessKey;
	  this.serviceBusRootUri=serviceBusRootUri;
	  this.config =
		         ServiceBusConfiguration.configureWithSASAuthentication(
		         namespace,this.shareAccessKeyName,this.shareAccessKey,this.serviceBusRootUri);
	  this.service = ServiceBusService.create(config);
      
   }
    public Configuration getConfig() {
	return config;
    }
	public void setConfig(Configuration config) {
		this.config = config;
	}
	public ServiceBusContract getService() {
		return service;
	}
	public void setService(ServiceBusContract service) {
		this.service = service;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getShareAccessKeyName() {
		return shareAccessKeyName;
	}
	public void setShareAccessKeyName(String shareAccessKeyName) {
		this.shareAccessKeyName = shareAccessKeyName;
	}
	public String getShareAccessKey() {
		return shareAccessKey;
	}
	public void setShareAccessKey(String shareAccessKey) {
		this.shareAccessKey = shareAccessKey;
	}
	public String getServiceBusRootUri() {
		return serviceBusRootUri;
	}
	public void setServiceBusRootUri(String serviceBusRootUri) {
		this.serviceBusRootUri = serviceBusRootUri;
	}
	
	public void createQueue(String queueName){
		long maxSizeInMegabytes = 5120;
		QueueInfo queueInfo = new QueueInfo(queueName);
		queueInfo.setMaxSizeInMegabytes(maxSizeInMegabytes);
		try
		{
		    this.service.createQueue(queueInfo);
		}
		catch (ServiceException e)
		{
		    System.out.print("ServiceException encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
	}
	public void sendMessage(String queueName,String queueMessage){
		BrokeredMessage message = new BrokeredMessage(queueMessage);
	    try {
			this.getService().sendQueueMessage(queueName, message);
		} catch (ServiceException e) {		
			e.printStackTrace();
		}

	}
	public void receiveMessage(String queueName){
		 try
		 {
		     ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
		     opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

		     while(true)  {
		          ReceiveQueueMessageResult resultQM =
		                 service.receiveQueueMessage(queueName, opts);
		         BrokeredMessage message = resultQM.getValue();
		         if (message != null && message.getMessageId() != null)
		         {
		             System.out.println("MessageID: " + message.getMessageId());
		             // Display the queue message.
		             System.out.print("From queue: ");
		             byte[] b = new byte[200];
		             String s = null;
		             int numRead = message.getBody().read(b);
		             while (-1 != numRead)
		             {
		                 s = new String(b);
		                 s = s.trim();
		                 System.out.print(s);
		                 numRead = message.getBody().read(b);
		             }
		             System.out.println();
		             System.out.println("Custom Property: " +
		                 message.getProperty("MyProperty"));
		             // Remove message from queue.
		             System.out.println("Deleting this message.");
		             //service.deleteMessage(message);
		         }  
		         else  
		         {
		             System.out.println("Finishing up - no more messages.");
		             break;
		             // Added to handle no more messages.
		             // Could instead wait for more messages to be added.
		         }
		     }
		 }
		 catch (ServiceException e) {
		     System.out.print("ServiceException encountered: ");
		     System.out.println(e.getMessage());
		     System.exit(-1);
		 }
		 catch (Exception e) {
		     System.out.print("Generic exception encountered: ");
		     System.out.println(e.getMessage());
		     System.exit(-1);
		 }
	}

}