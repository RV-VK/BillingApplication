<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ page import="Service.UserService,Service.UserServiceImplementation,java.util.List,Entity.User,DAO.UserDAO,java.util.Arrays"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>User Form</title>
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
input[type=text],[type=tel],[type=password]{
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
 font-size: 80%;
 }
 .userForm{
 position: relative;
 left    : 20px;
 top:    30px;
 width: 395px;
 }
select {
position: relative;
left    : 20px;
top:    30px;
appearance: none;
outline: 0;
background: Antique-white;
color: black;
height: 35px;
width: 410px;
cursor: pointer;
padding: 7px 10px;
border: 1px outset black;
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
top: 20px;
}
.userEntry{
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
User user = null;
UserDAO userDAO = new UserDAO();
List<String> userTypeList = Arrays.asList("Admin","Purchase","Sales");
request.setAttribute("userTypeList",userTypeList);
if(request.getParameter("id") == null || request.getParameter("id").isEmpty())
{
request.setAttribute("action","createUser");
request.setAttribute("page",request.getParameter("page"));
}
else {
request.setAttribute("id",request.getParameter("id"));
request.setAttribute("action","editUser");
user = userDAO.findById(Integer.parseInt(request.getParameter("id")));
request.setAttribute("user",user);
request.setAttribute("page",request.getParameter("page"));
}
%>
<body bgcolor="#303136">
</body>
<p id="message">${Error}</p>
<div class="userEntry">
<p id="title"> User Details</p>
<form method="post" action="${action}">
<input type="hidden" name="id" value="${user.getId()}">
<input type="hidden" name="page" value="${page}">
<label for="username">Username</label><br>
<input type="text" class="userForm" name="username" value="${user.getUserName()}" placeholder="username" oninvalid="this.setCustomValidity('Invalid format for username')" oninput="setCustomValidity('')" pattern="^[a-zA-Z0-9\s]{3,30}$"  required>
<br><br>
<label for="usertype">User Type</label><br>
<select name="usertype" id="user">
<c:forEach items="${userTypeList}" var="user">
<option value="${user}">${user}</option>
</c:forEach>
</select>
<br><br>
<label for="password">Password</label><br>
<input type="password" class="userForm" name="password" placeholder="password" oninvalid="this.setCustomValidity('Invalid format for password')" oninput="setCustomValidity('')" pattern="^[a-zA-Z0-9\s]{8,30}$" value="${user.getPassWord()}" required>
<br><br>
<label for="firstname">First Name</label><br>
<input type="text" class="userForm" name="firstname" placeholder="firstname" oninvalid="this.setCustomValidity('Invalid format for firstname')" oninput="setCustomValidity('')" pattern="^[a-zA-Z\s]{3,30}$" value="${user.getFirstName()}" required/>
<br><br>
<label for="lastname">Last Name</label><br>
<input type="text" class="userForm" name="lastname" placeholder="lastname" oninvalid="this.setCustomValidity('Invalid format for lastname')" oninput="setCustomValidity('')" pattern="^[a-zA-Z\s]{1,30}$" value="${user.getLastName()}" required/>
<br><br>
<label for="phonenumber">Phone Number</label><br>
<input type="tel" class="userForm" name="phonenumber" placeholder="phonenumber" oninvalid="this.setCustomValidity('Invalid format for PhoneNumber')" oninput="setCustomValidity('')" pattern="^[6789]\d{9}$" value="${user.getPhoneNumber()}" required/>
<br><br>
<input id="registerButton" type="submit" value="Submit">
</form>
</div>
<script>
    document.getElementById("user"). value="${user.getUserType()}";
</script>
</html>