package com.projectoxford;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import com.azure.storage.AzureBlob;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;
import com.obe.bean.Person;

public class UpdateFile {
	
	String personGroupId;
	public UpdateFile(String personGroupId){
		this.personGroupId=personGroupId;
	}
	public void UpdatePersonGroup(){	
	    //create a personGroup
		PersonGroupApi personGroup=new PersonGroupApi();
	    personGroup.createGroup(personGroupId);
	    try {
	    //update faceId
				AzureBlob azureBlob=new AzureBlob(Config.AzureBlobName,Config.AzureBlobKey);
				CloudBlobClient blobClient=azureBlob.getBlobClient();
				for(CloudBlobContainer containerItem:blobClient.listContainers()){
					List<String> list=new ArrayList<String>();
					String containerUrl=String.valueOf(containerItem.getUri());
					String[] containerUrls=containerUrl.split("/");
					String containerName=containerUrls[containerUrls.length-1];
					CloudBlobContainer container;
					try {
						container = blobClient.getContainerReference(containerName);
						for (ListBlobItem blobItem : container.listBlobs()) {
							   String url=String.valueOf(blobItem.getUri());
							   Detection detection=new Detection(url);
							   if(!detection.getErr()){
								   Person person=detection.getPerson();
								   String faceId=person.getFaceId();
								   list.add(faceId);}				
						   }	
					} catch (StorageException e) {
						e.printStackTrace();
					}					
				    String[] faceIds = (String[])list.toArray(new String[list.size()]);
					PersonApi personApi=new PersonApi();
					personApi.createPerson(faceIds, personGroupId, containerName);	
				}
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}	
	    }
	    	 
}
