package com.azure.storage;
import java.io.File;


import com.microsoft.azure.storage.blob.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
public class AzureBlob {
	private String stoarage_account;
	private String storage_account_key;
	private String storageConnectionString;
	private CloudStorageAccount storageAccount;
	private CloudBlobClient blobClient;
	
	public AzureBlob(String storage_account,String storage_account_key) throws InvalidKeyException, URISyntaxException{
		this.stoarage_account=storage_account;
		this.storage_account_key=storage_account_key;
		this.storageConnectionString= "DefaultEndpointsProtocol=http;" + 
			    "AccountName="+ this.stoarage_account + ";" + 
			    "AccountKey="+ this.storage_account_key;
        this.storageAccount = CloudStorageAccount.parse(this.storageConnectionString);
        this.blobClient=storageAccount.createCloudBlobClient();
	}
	public void createContainer(String containerName){
		try
		{
		   // Get a reference to a container.
		   // The container name must be lower case
		   CloudBlobContainer container = blobClient.getContainerReference(containerName);
		   for (CloudBlobContainer containerItem : blobClient.listContainers()) {
		       System.out.println(containerItem.getUri());
		   }

		   // Create the container if it does not exist.
		    container.createIfNotExists();
		    
		  /*+++++++++++++++++Optional: Configure a container for public access++++++++++*/
		 // Create a permissions object.
		    BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

		    // Include public access in the permissions object.
		    containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

		    // Set the permissions on the container.
		    container.uploadPermissions(containerPermissions);
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}
	
	public CloudBlobClient getBlobClient() {
		return blobClient;
	}
	public void setBlobClient(CloudBlobClient blobClient) {
		this.blobClient = blobClient;
	}
	public void  uploadBlob(String containerName,String filePath){
		try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount = CloudStorageAccount.parse(this.storageConnectionString);

		    // Create the blob client.
		    CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

		   // Retrieve reference to a previously created container.
		    CloudBlobContainer container = blobClient.getContainerReference(containerName);

		    // Define the path to a local file.
		    String[] paths=filePath.split("\\\\");
		    System.out.println(paths[paths.length-1]);
            String blobName=paths[paths.length-1];
		    // Create or overwrite the "myimage.jpg" blob with contents from a local file.
		    CloudBlockBlob blob = container.getBlockBlobReference(blobName);
		    File source = new File(filePath);
		    blob.upload(new FileInputStream(source), source.length());
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}
	
	public void listBlob(String containerName){
		try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount = CloudStorageAccount.parse(this.storageConnectionString);

		    // Create the blob client.
		    CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

		    // Retrieve reference to a previously created container.
		    CloudBlobContainer container = blobClient.getContainerReference(containerName);

		    // Loop over blobs within the container and output the URI to each of them.
		    for (ListBlobItem blobItem : container.listBlobs()) {
		       System.out.println(blobItem.getUri());
		   }
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}
	
	public void downloadBlob(String filepath,String containerName){
		try
		{
		    // Retrieve storage account from connection-string.
		   CloudStorageAccount storageAccount = CloudStorageAccount.parse(this.storageConnectionString);

		   // Create the blob client.
		   CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

		   // Retrieve reference to a previously created container.
		   CloudBlobContainer container = blobClient.getContainerReference(containerName);

		   // Loop through each blob item in the container.
		   for (ListBlobItem blobItem : container.listBlobs()) {
		       // If the item is a blob, not a virtual directory.
		       if (blobItem instanceof CloudBlob) {
		           // Download the item and save it to a file with the same name.
		            CloudBlob blob = (CloudBlob) blobItem;
		            blob.download(new FileOutputStream(filepath + blob.getName()));
		        }
		    }
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}
	
	public void deleteBlob(String containerName,String blobName){
		try
		{
		   // Retrieve storage account from connection-string.
		   CloudStorageAccount storageAccount = CloudStorageAccount.parse(this.storageConnectionString);

		   // Create the blob client.
		   CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

		   // Retrieve reference to a previously created container.
		   CloudBlobContainer container = blobClient.getContainerReference(containerName);

		   // Retrieve reference to a blob named "myimage.jpg".
		   CloudBlockBlob blob = container.getBlockBlobReference(blobName);

		   // Delete the blob.
		   blob.deleteIfExists();
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}
	
	public void deleteContainer(String containerName){
		try
		{
		   // Retrieve storage account from connection-string.
		   CloudStorageAccount storageAccount = CloudStorageAccount.parse(this.storageConnectionString);

		   // Create the blob client.
		   CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

		   // Retrieve reference to a previously created container.
		   CloudBlobContainer container = blobClient.getContainerReference(containerName);

		   // Delete the blob container.
		   container.deleteIfExists();
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}

}
