using System;
using System.Configuration;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Microsoft.ServiceBus;
using Microsoft.ServiceBus.Messaging;
using Microsoft.WindowsAzure;
using Microsoft.WindowsAzure.Storage;
//using Microsoft.WindowsAzure.ServiceRuntime;

namespace ServiceBusQueue
{
    public partial class _Default : Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        /*
        private void CreateQueue2()
        {
            TokenProvider credentials = TokenProvider.CreateSharedAccessSignatureTokenProvider("", "");//sasKeyName, sasKeyValue
            NamespaceManager namespaceClient = new NamespaceManager(ServiceBusEnvironment.CreateServiceUri("sb", ServiceNamespace, string.Empty), credentials);

            QueueDescription myQueue;
            myQueue = namespaceClient.CreateQueue("IssueTrackingQueue");


        }

        private void SendQueue2()
        {
            TokenProvider credentials = TokenProvider.CreateSharedAccessSignatureTokenProvider("", "");//sasKeyName, sasKeyValue
            NamespaceManager namespaceClient = new NamespaceManager(ServiceBusEnvironment.CreateServiceUri("sb", ServiceNamespace, string.Empty), credentials);

            //QueueDescription myQueue;
            //myQueue = namespaceClient.CreateQueue("IssueTrackingQueue");

            MessagingFactory factory = MessagingFactory.Create(ServiceBusEnvironment.CreateServiceUri("sb", ServiceNamespace, string.Empty), credentials);
            QueueClient myQueueClient = factory.CreateQueueClient("IssueTrackingQueue");

            // Send messages
            Console.WriteLine("Now sending messages to the Queue.");
            for (int count = 0; count < 6; count++)
            {
                var issue = MessageList[count];
                issue.Label = issue.Properties["IssueTitle"].ToString();
                myQueueClient.Send(issue);
                Console.WriteLine(string.Format("Message sent: {0}, {1}", issue.Label, issue.MessageId));
            }


        }
        */
        private void ReceiveQueue2()
        {
            string sasKeyName = "RootManageSharedAccessKey";
            string sasKeyValue = "qzapcH0H/raPCvI7/5XwVh3XHqxsdfUuH09MMg+QYp8=";
            string ServiceNamespace = "juntacmobilemsg-ns";
            TokenProvider credentials = TokenProvider.CreateSharedAccessSignatureTokenProvider(sasKeyName, sasKeyValue);
            NamespaceManager namespaceClient = new NamespaceManager(ServiceBusEnvironment.CreateServiceUri("sb", ServiceNamespace, string.Empty), credentials);

            /*
            QueueDescription myQueue;
            myQueue = namespaceClient.CreateQueue("FaceQueue");
            */

            MessagingFactory factory = MessagingFactory.Create(ServiceBusEnvironment.CreateServiceUri("sb", ServiceNamespace, string.Empty), credentials);
            QueueClient myQueueClient = factory.CreateQueueClient("FaceQueue");

            /*
            // Send messages
            Console.WriteLine("Now sending messages to the Queue.");
            for (int count = 0; count < 6; count++)
            {
                var issue = MessageList[count];
                issue.Label = issue.Properties["IssueTitle"].ToString();
                myQueueClient.Send(issue);
                Console.WriteLine(string.Format("Message sent: {0}, {1}", issue.Label, issue.MessageId));
            }
            */

            //Console.WriteLine("Now receiving messages from Queue.");
            BrokeredMessage message;
            while ((message = myQueueClient.Receive(new TimeSpan(hours: 0, minutes: 0, seconds: 5))) != null)
            {
                Console.WriteLine(string.Format("Message received: {0}, {1}, {2}", message.SequenceNumber, message.Label, message.MessageId));
                message.Complete();

                Console.WriteLine("Processing message (sleeping...)");
                //Thread.Sleep(1000);
            }

            factory.Close();
            myQueueClient.Close();
            //namespaceClient.DeleteQueue("IssueTrackingQueue");

        }
        

        private void CreateQueue()
        {
            string connectionString = ConfigurationManager.AppSettings["Microsoft.ServiceBus.ConnectionString"];
            string queueName = ConfigurationManager.AppSettings["ServiceBus.Queue.Name"];
    //CloudConfigurationManager.GetSetting("Microsoft.ServiceBus.ConnectionString");

            // Configure queue settings
            QueueDescription qd = new QueueDescription(queueName);
            qd.MaxSizeInMegabytes = 5120;
            qd.DefaultMessageTimeToLive = new TimeSpan(1, 0, 0);
            
            var namespaceManager =
                NamespaceManager.CreateFromConnectionString(connectionString);

            if (!namespaceManager.QueueExists(queueName))
            {
                namespaceManager.CreateQueue(qd);
            }
        }

        private void SendQueueMessage()
        {
            string connectionString = ConfigurationManager.AppSettings["Microsoft.ServiceBus.ConnectionString"];
    //CloudConfigurationManager.GetSetting("Microsoft.ServiceBus.ConnectionString");

            QueueClient Client =
                QueueClient.CreateFromConnectionString(connectionString, ConfigurationManager.AppSettings["ServiceBus.Queue.Name"]);
            BrokeredMessage message = new BrokeredMessage("CaptureFace");
                        // Set some addtional custom app-specific properties
            message.Properties["confidence"] = 0.5997;
            message.Properties["imageUrl"] = "https://obe.blob.core.chinacloudapi.cn/jack/123.jpg";
            message.Properties["name"] = "dimmy";
            message.Properties["personId"] = "e22a0aae-501e-484f-b6b8-a68af9ffe18f";
            message.Properties["Time"] = DateTime.Now;

            // Send message to the queue
            Client.Send(message);
            Client.Close();
        }

        private void GetQueueMessage()
        {
            string connectionString = ConfigurationManager.AppSettings["Microsoft.ServiceBus.ConnectionString"];
  //CloudConfigurationManager.GetSetting("Microsoft.ServiceBus.ConnectionString");
            QueueClient Client =
              QueueClient.CreateFromConnectionString(connectionString, ConfigurationManager.AppSettings["ServiceBus.Queue.Name"]);

            // Configure the callback options
            OnMessageOptions options = new OnMessageOptions();
            options.AutoComplete = false;
            options.AutoRenewTimeout = TimeSpan.FromMinutes(1);

            /*
            // Callback to handle received messages
            Client.OnMessage((message) =>
            {
                try
                {
                    // Process message from queue
                    Response.Write("Body: " + message.GetBody<string>());
                    Response.Write("MessageID: " + message.MessageId);
                    Response.Write("Name: " +
                    message.Properties["Name"]);
                    Label1.Text = "MessageID: " + message.MessageId + " Name: " + message.Properties["Name"];
                    // Remove message from queue
                    message.Complete();
                }
                catch (Exception exc)
                {
                    // Indicates a problem, unlock message in queue
                    Label1.Text = "Recv: " + exc.Message;
                    message.Abandon();
                }
            }, options);
            */
            BrokeredMessage message = Client.Receive(new TimeSpan(0,0,3));
            try
            {
                if (message != null) 
                { 
                    //Label1.Text = " Name: " + message.Properties["Name"] + " Time: " + message.Properties["Time"]; 
                    message.Complete(); 
                }
                else Label1.Text = "NULL";
            }
            catch(Exception exc)
            {
                Label1.Text = exc.Message;
            }
            Client.Close();
        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            CreateQueue();
        }

        protected void Button2_Click(object sender, EventArgs e)
        {
            SendQueueMessage();
        }

        protected void Button3_Click(object sender, EventArgs e)
        {
            //ReceiveQueue2();
            GetQueueMessage();
        }
    }
}