<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传图片</title>
</head>

<body>
<div  align="center" style="width:50%; height:300px; font-size:13px">
<h3>创建一个Person</h3>
<script language="javascript">
function go_up(){	
    document.getElementById('new_up').innerHTML+='请选择图片：<input type="file" name="upfile[]" align="center"/><br><br>';
}
</script>
<form name="frm" method="post" enctype="multipart/form-data" action="img.php">
PersonName: <input name="personName" type="text" align="center"/><br/><br/>
<font style="letter-spacing:1px" color="#FF0000">*只允许上传jpg|png|bmp|pjpeg|gif格式的图片</font><br><br>
<div id="new_up">
请选择图片：
 <input name='upfile[]' type='file' align="center"/><br><br>
请选择图片：
 <input name='upfile[]' type='file' align="center"/><br><br>
请选择图片：
 <input name='upfile[]' type='file' align="center"/><br><br>
 </div>
 <br/>
 <br/>
 <input type="button"" name="add_img" value="继续添加" onclick="go_up()"/><br><br/><br/>  <input name="tijiao" type="submit" value="上传" /><br />
</form>
</div>
</body>
</html>