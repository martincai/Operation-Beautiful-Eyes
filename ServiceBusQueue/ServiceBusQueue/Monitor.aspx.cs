using System;
using System.Configuration;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Threading;
using Microsoft.ServiceBus;
using Microsoft.ServiceBus.Messaging;
using Newtonsoft.Json;
using System.IO;
using Newtonsoft.Json.Linq;

namespace MonitorWebRole
{
    public enum TVState
    {
        ON,
        OFF
    }
    public partial class Monitor : System.Web.UI.Page
    {
        //int conTimes;

        private int conTimes //conTimes:ContinuousTimes in Web.Config
        {
            get { if (Application["ContinuousTimes"] == null) return 5; else return (int)Application["ContinuousTimes"]; }
            set { Application["ContinuousTimes"] = value; }
        }

        private int conPeriod //conPeriod:ContinuousPeriodInSecond in Web.Config
        {
            get { if (Application["ContinuousPeriodInSecond"] == null) return 1800; else return (int)Application["ContinuousPeriodInSecond"]; }
            set { Application["ContinuousPeriodInSecond"] = value; }
        }
        private int sleepInterval //sleepInterval:MonitorIntervalInMilliSecond in Web.Config
        {
            get { if (Application["MonitorIntervalInMilliSecond"] == null) return 1000; else return (int)Application["MonitorIntervalInMilliSecond"]; }
            set { Application["MonitorIntervalInMilliSecond"] = value; }
        }
        private bool isMonitoring //isMonitoring indicates whether start monitoring. true: Monitoring, false: Not Monitoring
        {
            get { if (ViewState["Monitor"] == null) return false; else return (bool)ViewState["Monitor"]; }
            set { ViewState["Monitor"] = value; }
        }
        bool lastCapture //lastCapture indicates whether capture a photo last time. true: Captured last time, false: Not Captured last time
        {
            get { if (ViewState["LastCapture"] == null) return false; else return (bool)ViewState["LastCapture"]; }
            set { ViewState["LastCapture"] = value; }
        }
        int curCaptureTimes //curCaptureTimes indicates the current continuously times when capture a photo.
        {
            get { if (ViewState["CurCaptureTimes"] == null) return 0; else return (int)ViewState["CurCaptureTimes"]; }
            set { ViewState["CurCaptureTimes"] = value; }
        }
        DateTime firstCaptureTime //firstCaptureTime indicates the time when the first capture happened in continuous captures
        {
            get { if (ViewState["FirstCaptureTime"] == null) return DateTime.Now; else return (DateTime)ViewState["FirstCaptureTime"]; }
            set { ViewState["FirstCaptureTime"] = value; }
        }

        string personId
        {
            get { if (ViewState["PersonId"] == null) return string.Empty; else return (string)ViewState["PersonId"]; }
            set { ViewState["PersonId"] = value; }
        }
        string personName
        {
            get { if (ViewState["PersonName"] == null) return string.Empty; else return (string)ViewState["PersonName"]; }
            set { ViewState["PersonName"] = value; }
        }
        string imageUrl
        {
            get { if (ViewState["ImageUrl"] == null) return string.Empty; else return (string)ViewState["ImageUrl"]; }
            set { ViewState["ImageUrl"] = value; }
        }
        DateTime lastTime
        {
            get { if (ViewState["LastTime"] == null) return DateTime.Now; else return (DateTime)ViewState["LastTime"]; }
            set { ViewState["LastTime"] = value; }
        }

        TimeSpan capturePeriod //capturePeriod indicates the TimeSpan of continuous captures
        {
            get { return DateTime.Now - firstCaptureTime; }
        }
        TVState tvState
        {
            get { if (ViewState["TVState"] == null) return TVState.OFF; else return (TVState)ViewState["TVState"]; }
            set { ViewState["TVState"] = value; }
        }
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                try
                {
                    Initial();
                    GetConfig();
                    //Thread monitorThread = new Thread(new ThreadStart(MonitorTV));
                    //monitorThread.Start();
                    //monitorThread.Join();
                }
                catch (Exception ex)
                {
                    Response.Write(ex.Message);
                }
            }
            Timer1.Interval = sleepInterval;
            DisplayInfo();
            
        }


        private void Initial()
        {
            conTimes = 0; //conTimes:ContinuousTimes in Web.Config
            conPeriod = 0; //conPeriod:ContinuousPeriodInSecond in Web.Config
            sleepInterval = 0; //sleepInterval:MonitorIntervalInMilliSecond in Web.Config
            isMonitoring = false; //isMonitoring indicates whether start monitoring. true: Monitoring, false: Not Monitoring
            Timer1.Enabled = false;
            lastCapture = false; //lastCapture indicates whether capture a photo last time. true: Captured last time, false: Not Captured last time
            curCaptureTimes = 0; //curCaptureTimes indicates the current continuously times when capture a photo.
            firstCaptureTime = DateTime.Now; //firstCaptureTime indicates the time when the first capture happened in continuous captures
            //capturePeriod = new TimeSpan(); //capturePeriod indicates the TimeSpan of continuous captures
            tvState = TVState.OFF;
            lastTime = DateTime.Now;
            personId = string.Empty;
            personName = string.Empty;
            imageUrl = string.Empty;

        }

        /// <summary>
        /// Start or Stop Monitoring
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected void btnMonitor_Click(object sender, EventArgs e)
        {
            //Switch Start/Stop Monitoring
            isMonitoring = !isMonitoring;
            Timer1.Enabled = isMonitoring;
            DisplayInfo();
        }

        /// <summary>
        /// Read Capture Result from Service Bus Queue.
        /// </summary>
        /// <returns></returns>
        private bool GetCaptureResultFromSBQueue(out string strID, out string strName, out DateTime dtTime, out string strImageUrl)
        {
            string connectionString = ConfigurationManager.AppSettings["Microsoft.ServiceBus.ConnectionString"];
            SubscriptionClient Client =
              SubscriptionClient.CreateFromConnectionString(connectionString, ConfigurationManager.AppSettings["ServiceBus.Topic.Name"],"children");
            strID = string.Empty;
            strName = string.Empty;
            dtTime = DateTime.Now;
            strImageUrl = string.Empty;
            try
            {
                BrokeredMessage message = Client.Receive(new TimeSpan(0, 0, 3));
                if (message != null)
                {
                    string payLoad = (string)message.Properties["payload"];
                    string str_message = payLoad.Substring(1, payLoad.Length - 2);
                    JObject jo = JObject.Parse(str_message);
                    string[] values = jo.Properties().Select(item => item.Value.ToString()).ToArray();
                    strID = values[3];                 
                    strName = values[2];
                    strImageUrl = values[1];
                    dtTime = DateTime.ParseExact(values[4], "yyyy-MM-dd HH:mm:ss", null);
                    message.Complete();
                    return true;
                }
                else
                {
                    return false;
                }// Label1.Text = "NULL";
            }
            catch (Exception exc)
            {
                lblException.Text = exc.Message;
            }
            Client.Close();

            return false;
        }

        private TVState GetTVState()
        {
            return tvState;
        }

        protected void btnTV_Click(object sender, EventArgs e)
        {
            try
            {
                if (tvState == TVState.OFF)
                {
                    tvState = TVState.ON; firstCaptureTime = DateTime.Now;
                }
                else
                {
                    tvState = TVState.OFF; curCaptureTimes = 0;
                }
                DisplayInfo();
            }
            catch(Exception exc)
            {
                lblException.Text = exc.Message;
            }
            //DisplayInfo();
        }

        private void GetConfig()
        {
            System.Configuration.AppSettingsReader asr = new System.Configuration.AppSettingsReader();
            int nTemp;
            if (!Int32.TryParse(asr.GetValue("ContinuousTimes", typeof(Int32)).ToString(), out nTemp)) conTimes = 5; else conTimes = nTemp;
            if (!Int32.TryParse(asr.GetValue("ContinuousPeriodInSecond", typeof(Int32)).ToString(), out nTemp)) conPeriod = 1800; else conPeriod = nTemp;
            if (!Int32.TryParse(asr.GetValue("MonitorIntervalInMilliSecond", typeof(Int32)).ToString(), out nTemp)) sleepInterval = 5000; else sleepInterval = nTemp;
        }

        private void DisplayInfo()
        {
            try
            {
                if (GetTVState() == TVState.OFF)
                {
                    btnTV.Text = "Turn On"; lblTV.Text = "TV IS OFF";
                }
                else {
                    btnTV.Text = "Turn Off"; lblTV.Text = "TV IS ON";
                }
                if (isMonitoring)
                {
                    btnMonitor.Text = "Stop Monitor";
                }
                else
                {
                    btnMonitor.Text = "Start Monitor";
                }

                if (GetTVState() == TVState.ON && curCaptureTimes >= conTimes)
                {
                    lblWarning.Text = "Warning: " + personName + " Is Near TV From " + lastTime.ToString("yyyy-MM-dd HH:mm:ss") + " !";
                    lbImage.ImageUrl = imageUrl;
                    lblException.Text = "";
                }
                else
                {
                    lblWarning.Text = string.Empty;
                }
                if (GetTVState() == TVState.ON && capturePeriod.TotalSeconds >= conPeriod)
                {
                    lblTime.Text = personName + " Has Been Watching TV For A Long Time, Please Turn Off TV Now!";
                    lblException.Text = "";
                }
                else {
                    lblTime.Text = string.Empty;
                }
            }
            catch(Exception exc)
            {
                lblException.Text = exc.Message;
            }
            
        }

        protected void Timer1_Tick(object sender, EventArgs e)
        {
            if (GetTVState() == TVState.OFF)
            {
                DisplayInfo();
                return;
            }
            string strId, strName, strImageUrl;
            DateTime dtTime;
            if (GetCaptureResultFromSBQueue(out strId, out strName, out dtTime,out strImageUrl))
            {
                if (lastCapture)
                {
                    if (string.Compare(strId, personId) == 0)
                    {
                        curCaptureTimes++;
                    }
                    else
                    {
                        curCaptureTimes = 1;
                        personName = strName;
                        personId = strId;
                        imageUrl = strImageUrl;
                    }
                }
                else
                {
                    curCaptureTimes = 1;
                    personName = strName;
                    personId = strId;
                    imageUrl = strImageUrl;
                }
                lastCapture = true;
            }
            else //Reset values of lastCapture,curCaptureTimes,capturePeriod if GetCaptureResultFromSBQueue() returns false;
            {
                lastCapture = false;
                //curCaptureTimes = 0;
                //personName = string.Empty;
                //personId = string.Empty;
                //capturePeriod = new TimeSpan(0, 0, 0);
            }

            DisplayInfo();
        }
    }
}