<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/crawler/resources/easyui/1.4/jquery.min.js"></script>
<script src="/crawler/resources/easyui/1.4/jquery.easyui.min.js"></script>
<title>Add User By Json</title>
<script type="text/javascript">
	$(document).ready(function(){
		$("#add").click(function(){
			var name = $("#name").val();//attr("value");
			var password = $("#password").val();//attr("title");
			//alert(name);
			//alert(password);
			
			var task = {name:name , password:password};//封装成json数据
			//利用ajax技术提交数据(jquery中)
			$.ajax({
				url:"/crawler/task/123456",
				type:"post",//get方式可能出现编码问题
				contentType: "application/json",
				data:task,	//提交的数据
				success:function(data){//data为服务器返回的数据
					alert("userName--->" + data.name + "password--->" + data.password);
				}
			});
		});
	});
</script>
</head>
<body>
	<h2>json添加用户</h2>
	姓名：<input type="text" id="name" name="name" />
	密码：<input type="text" id="password" name="password"></input>
	<input type="button" id="add" value="添加">
</body>
</html>