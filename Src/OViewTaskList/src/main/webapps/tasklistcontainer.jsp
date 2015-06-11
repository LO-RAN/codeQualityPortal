<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Insert title here</title>
</head>
<body>
<table width="100%">
<tr>
<td width="50%">
<iframe src='tasklist.do?userId=<%=request.getParameter("userId")%>' name="OFTaskList" height="500" width="100%" frameborder="1"></iframe>
</td>
<td width="50%">
<iframe src="taskexec.htm" name="OFTaskExec" height="500" width="100%" frameborder="1">Ex&eacute;cutez vos t&acirc;ches ici</iframe>
</td>
</tr>
</table>
</body>
</html>