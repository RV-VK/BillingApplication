<%@ page import="Service.LoginService,Service.LoginServiceImplementation" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Admin Login</title>
<div class="header">
<img src="Images/cart.png" id="cart">
<img src="Images/bill.png" id="bill">
<p id="head" style="font-size: 50%'">SmartPOS</p>
</div>
<style>
#cart{
position: relative;
height: 30px;
width: 30px;
top: 10px;
left: -630px;
}
#bill{
position: relative;
height: 30px;
width: 30px;
top: 10px;
right: -630px;
}
.header{
position: absolute;
top: 0;
left: 0;
background-color: #485582;
text-align: center;
height: 50px;
width: 100%;
}
#error{
position: absolute;
top: 10%;
left: 32%;
color: red;
font-family: 'Courier New', monospace;
}
#head{
font-family: 'Courier New', monospace;
color: white;
font-size: 150%;
margin-top: -20px;
}
</style>
</head>
<body bgcolor="#303136">
<% String errorMessage = String.valueOf(request.getAttribute("Error"));%>
<% if(errorMessage.equals("Error")) { %>
<p id="error"> Invalid Credentials! Please try with Valid Credentials</p>
<% } %>
<% if(errorMessage.equals("Success")) { %>
<% out.println("<p style = 'color: green'> Admin Created Successfully</p>");%>
<% }%>
<% LoginService loginService = new LoginServiceImplementation();%>
 <% boolean adminCreated = loginService.checkIfInitialSetup(); %>
  <% if (!adminCreated) { %>
        <jsp:include page="register.jsp"></jsp:include>
  <% } else { %>
        <jsp:include page="login.jsp"></jsp:include>
      <% } %>
</body>
</html>
