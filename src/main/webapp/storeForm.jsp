<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ page import="Service.StoreService,Service.StoreServiceImplementation,java.util.List,Entity.Store,DAO.StoreDAO,java.util.Arrays"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>Store Form</title>
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
 .storeForm{
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
.storeEntry{
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
<body bgcolor="#303136">
</body>
<p id="message">${message}</p>
<p id="message">Please Register your Store to Proceed with the POS Application!</p>
<div class="storeEntry">
<p id="title"> Store Details</p>
<form method="post" action="createStore">
<label for="name">Store Name</label><br>
<input type="text" class="storeForm" name="name"  placeholder="store name" oninvalid="this.setCustomValidity('Invalid format for Store Name')" oninput="setCustomValidity('')" pattern="^[a-zA-Z0-9\s]{3,30}$"  required>
<br><br><br>
<label for="number">Phone Number</label><br>
<input type="text" class="storeForm" name="number" placeholder="phone number" oninvalid="this.setCustomValidity('Invalid format for Phone Number')" oninput="setCustomValidity('')" pattern="^[6789]\d{9}$" required>
<br><br><br>
<label for="address">Address</label><br>
<textarea name="address" placeholder="Drop your Address" rows="4" cols="49" required></textarea>
<br><br><br>
<label for="GSTCode">GST Code</label><br>
<input type="text"  class="storeForm" name="GSTCode" placeholder="GST Number"  oninvalid="this.setCustomValidity('Invalid format for GST Code')" oninput="setCustomValidity('')" pattern="^[a-zA-Z0-9]{15}$"  required>
<br><br><br>
<input id="registerButton" type="submit" value="Submit">
</form>
</div>
<script>
</script>
</html>