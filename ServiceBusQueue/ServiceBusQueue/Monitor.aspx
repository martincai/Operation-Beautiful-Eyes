<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Monitor.aspx.cs" Inherits="MonitorWebRole.Monitor" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Monitoring TV</title>
</head>
<body>
    <div>
    <form id="form1" runat="server">
        <div>

            <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>

        </div> 
        <asp:Button ID="btnMonitor" runat="server" Text="Start Monitor" OnClick="btnMonitor_Click" />
        <asp:Button ID="btnTV" runat="server"  Text="Turn On" Width="111px" OnClick="btnTV_Click"/>
        <br />
        <br />
        <asp:Label ID="lblTV" runat="server" Text="TV IS OFF" ForeColor="Blue"></asp:Label>
        <br />
        <br />
        <asp:Label ID="lblWarning" runat="server" Text="Warning:Kids Are Near the TV!" ForeColor="#CC9900"></asp:Label>
        <asp:Timer ID="Timer1" runat="server" Interval="5000" OnTick="Timer1_Tick">
        </asp:Timer>
        <br />
        <asp:ScriptManager ID="ScriptManager1" runat="server">
        </asp:ScriptManager>
        <br />
        <asp:Label ID="lblTime" runat="server" ForeColor="#FF3300" Text="Watching TV for a long Time!"></asp:Label>
        <br />

    </form>
    
    </div>
</body>
</html>
