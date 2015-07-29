<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Monitor.aspx.cs" Inherits="MonitorWebRole.Monitor" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Monitoring TV</title>
</head>
<body style="align-items:center;align-content:center;text-align:center">
    <div>
    <form id="form1" runat="server">
        <div>
        <br />
        <br />
        </div> 
        <div style="align-items:center;align-content:center;text-align:center">
        <asp:Button ID="btnMonitor" runat="server" Text="Start Monitor" OnClick="btnMonitor_Click" />
        <asp:Button ID="btnTV" runat="server"  Text="Turn On" Width="111px" OnClick="btnTV_Click"/>
        <br />
        <br />
        </div>
        <div style="align-items:center;align-content:center;text-align:center">
        <asp:Label ID="lblTV" runat="server" Text="TV IS OFF" ForeColor="Blue"></asp:Label>
        <br />
        <br />
        <asp:Label ID="lblWarning" runat="server" Text="Warning:Kids Are Near the TV!" ForeColor="#CC9900"></asp:Label>
        <br />
        <br />
        <br />
         <div style="align-items:center;align-content:center;text-align:center">
         <asp:Image ID="lbImage" runat="server" ImageUrl="" width=200px height=200px/>
        </div>
         <br />
        <asp:Label ID="lblTime" runat="server" ForeColor="#FF3300" Text="Watching TV for a long Time!"></asp:Label>
        <br />
        <br />
        <asp:Label ID="lblException" runat="server" ForeColor="#FF3300" Text=""></asp:Label>
        <br />
        <br />
        </div>
        <asp:Timer ID="Timer1" runat="server" Interval="5000" OnTick="Timer1_Tick" Enabled="False">
        </asp:Timer>
        <br />
        <asp:ScriptManager ID="ScriptManager1" runat="server">
        </asp:ScriptManager>
        <br />

    </form>
    
    </div>
</body>
</html>
