<!DOCTYPE html>
<html>
	<head>
	<title>Add a person</title>
	<style type="text/css">
	#back{
		position:absolute; 
		top:50%; 
		left:50%; 
		margin:-300px 0 0 -250px; 
		width:500px; 
		height:500px; 
		border:5px solid blue; 
	}
	#person{
		position:absolute; 
		top:50%; 
		left:50%; 
		margin:-110px 0 0 -120px; 
		border:1px solid white; 
	}
	</style>
	</head>
	<body >
		<div id="back">
			<form id="form1"  action="oxford.php">
			<script language="javascript" type="text/javascript">
				var count=0 ;
				function additem(id)
				{
					var row,cell,str;
					row = document.getElementById(id).insertRow();
					if(row != null )
					{
				cell = row.insertCell();
				cell.innerHTML="<input id=\"St"+count+"\" type=\"text\" name=\"St"+count+"\" value= \"St"+count+"\"><input type=\"button\" value=\"delete\" onclick=\'deleteitem(this);\'>";
				count ++;
					}
				}
				function deleteitem(obj)
				{
					var curRow = obj.parentNode.parentNode;
					tb.deleteRow(curRow.rowIndex);
				}

				function getsub()
				{
				var re="";
				for (var    i = 0 ;i<count;i++)
				{
				 
				  re += document.getElementsByName("St"+i)[0].value;
				  re+="@";
				}
				document.getElementById("Hidden1").value=re;
				}
			</script>
			<div align=center id="person">
			PersonName:<input name="personName"><br/><br/>
			图片URL:<br/><br/>
			<table id="tb"></table>
			<input name="add" type="button" style="width:100px;height:50px" onclick='additem("tb")' value="add"/>&nbsp;&nbsp;<input type="submit" name="submit" onclick="getsub()"" value="submit"/>
					<input id="Hidden1" name="urls" type="hidden" value="" />
			</div>
			</form>
			
		</div>
	</body>
</html>