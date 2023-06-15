<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ page import="Service.UnitService,Service.UnitServiceImplementation,java.util.List,Entity.Unit,DAO.UnitDAO,java.util.Arrays"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>Unit Form</title>
<%
      response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"  );

      if(session.getAttribute("username")==null)
      {
        response.sendRedirect("index.jsp");
      }
%>
<div class="header">
<p id="head" style="font-size: 50%'">SmartPOS</p>
<form action="logout" method="POST">
<input type="image" class="logout" src="Images/logout.png" alt="submit"/>
</form>
</div>
<style>
.header{
position: absolute;
top: 0;
left: 0;
background-color: #485582;
text-align: center;
height: 50px;
width: 100%;
}
.logout{
position: relative;
height: 30px;
width: 30px;
top: -50px;
right: -650px;
}
#head{
font-family: 'Courier New', monospace;
color: white;
font-size: 150%;
margin-top: 10px;
margin-right: 1200px;
}
#message{
position: relative;
top: 50px;
text-align: center;
font-family: 'Courier New', monospace;
color: red;
}
input[type=text]{
   background-color: Antique-white;
   padding-left: 10px;
   height: 30px;
   text-align: left;
 }
 label{
 position: relative;
 font-family: 'Courier New', monospace;
 color: #303136;
 left: 20px;
 top: 20px;
 font-size: 100%;
 }
 .unitForm{
 position: relative;
 left    : 20px;
 top:    30px;
 width: 395px;
 }
 textarea{
 position: relative;
 left: 20px;
 top: 30px;
 }
 #true{
 position: relative;
 left: 20px;
 top:30px;
 }
 #false{
 position: relative;
  left: 20px;
  top:35px;
 }
 #labelTrue{
 top: 30px;
 left: 30px;
 }
 #labelFalse{
 top: 35px;
 left: 30px;
 }
input[type=submit]{
background-color: #485582;
height: 40px;
width: 100px;
color: white;
padding: 10px 24px;
border-radius: 10px;
transition-duration: 0.4s;
}
input[type=submit]:hover{
background-color: #303136 ;
color: white;
}
#registerButton{
position: relative;
left: 170px;
top: 40px;
}
.unitEntry{
position: relative;
margin: auto;
border-radius: 15px;
border 2px white;
height: 550px;
width: 450px;
background-color: #D3D3D3;
margin-top: 5%;
}
#title{
position: relative;
left: 150px;
top: 20px;
font-family: 'Courier New', monospace;
color: #303136;
font-style: bold;
font-size: 120%;
}
</style>
</head>
<%
Unit unit = null;
UnitDAO unitDAO = new UnitDAO();
if(request.getParameter("editCode") == null ||  (request.getParameter("editCode").isEmpty()))
{
request.setAttribute("action","createUnit");
request.setAttribute("page",request.getParameter("page"));
}
else {
request.setAttribute("editCode",request.getParameter("editCode"));
request.setAttribute("action","editUnit");
unit = unitDAO.findByCode(request.getParameter("editCode"));
request.setAttribute("unit",unit);
request.setAttribute("page",request.getParameter("page"));
}
%>
<body bgcolor="#303136">
</body>
<p id="message">${Error}</p>
<div class="unitEntry">
<p id="title"> Unit Details</p>
<form method="post" action="${action}">
<input type="hidden" name="id" value="${unit.getId()}">
<input type="hidden" name="page" value="${page}">
<input type="hidden" name="editCode" value="${editCode}">
<label for="name">Name</label><br>
<input type="text" class="unitForm" name="name" value="${unit.getName()}" placeholder="name" oninvalid="this.setCustomValidity('Invalid format for Name')" oninput="setCustomValidity('')" pattern="^[a-zA-Z0-9\s]{3,30}$"  required>
<br><br><br>
<label for="code">Unit Code</label><br>
<input type="text" class="unitForm" name="code" placeholder="unit code" oninvalid="this.setCustomValidity('Invalid format for Code')" oninput="setCustomValidity('')" pattern="^[a-zA-Z\s]{1,4}$" value="${unit.getCode()}" required>
<br><br><br>
<label for="description">Description</label><br>
<textarea name="description" placeholder="Drop your Description" rows="4" cols="49" required>${unit.getDescription()}</textarea>
<br><br><br>
<label for="isdividable">IsDividable</label><br>
<input type="radio"  id="true" name="isdividable" value="true" required>
<label id="labelTrue" for="true">True</label>
<br>
<input type="radio"  id="false" name="isdividable" value="false" required>
<label id="labelFalse" for="false">False</label>
<br><br><br>
<input id="registerButton" type="submit" value="Submit">
</form>
</div>
<script>
    var checked = ${unit.getIsDividable()};
    if(checked == true){
        document.getElementById("true").checked = true;
    }
    else if(checked == false) {
        document.getElementById("false").checked = true;
        console.log("false");
    }
</script>
</html>