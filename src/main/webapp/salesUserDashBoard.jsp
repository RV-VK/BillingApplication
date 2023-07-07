<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ page import="DAO.StoreDAO,Entity.Store" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>DashBoard</title>
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
<input type="image" class="logout" title="logout" src="Images/logout.png" alt="submit"/>
</form>
</div>
<style>
.header{
position: absolute;
top: 0;
left: 0;
border: 0px outset #485582;
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
left: 650px;
}
#head{
font-family: 'Courier New', monospace;
color: white;
font-size: 150%;
margin-top: 10px;
margin-right: 1200px;
}
#welcome{
font-family: 'Courier New', monospace;
color: white;
font-size: 130%;
margin-top: 70px;
margin-left: 560px;
}
#store{
position: absolute;
height: 180px;
width: 180px;
}
.store{
position: absolute;
height: 180px;
width: 180px;
margin-top: 15px;
transition: transform .2s;
margin-left: 200px;
}
.store:hover {
transform: scale(1.3);
}
#storeText{
position: absolute;
top: 180px;
left: 70px;
color: white;
font-family: 'Courier New', monospace;
}
#user{
position: absolute;
height: 180px;
width: 180px;
}
#userText{
position: absolute;
top: 180px;
left: 70px;
font-family: 'Courier New', monospace;
color: white;
}
.user:hover {
transform: scale(1.3);
}
.user{
position: absolute;
border: 0px outset #303136;
transition: transform .2s;
height: 180px;
width: 180px;
top: 135px;
left: 580px;
}
#product{
position: absolute;
height: 180px;
width: 180px;
}
.product{
position: absolute;
border: 0px outset #303136;
transition: transform .2s;
height: 180px;
width: 180px;
top: 130px;
left: 950px;
}
.product:hover {
transform: scale(1.3);
}
#productText{
position: absolute;
font-family: 'Courier New', monospace;
color: white;
top: 170px;
left: 60px;
}
#sales{
position: absolute;
height: 180px;
width: 180px;
}
.sales{
position: absolute;
height: 180px;
width: 180px;
transition: transform .2s;
top: 390px;
left: 580px;
}
.sales:hover {
transform: scale(1.3);
}
#salesText{
position: absolute;
font-family: 'Courier New', monospace;
color: white;
top: 180px;
left: 55px;
}
</style>
</head>
<p id="welcome"> WELCOME ${fn:toUpperCase(username)}!</p>
<body bgcolor="#303136">
<div class="store">
<a href="storeReadOnly.jsp"><input type="image" id="store" src="Images/storeshop.png" alt="submit"/></a>
<p id="storeText"> Store </p>
</div>
<div class="user">
<a href="userProfile.jsp"><input type="image" id="user" src="Images/users.png" alt="submit"/></a>
<p id="userText">User</p>
</div>
<div class="product">
<a href="productList"><input type="image" id="product" src="Images/product.png" alt="submit"/></a>
<p id="productText">Product</p>
</div>
<div class="sales">
<a href="listSales"><input type="image" id="sales" src="Images/sales.png" alt="submit"/></a>
<p id="salesText">Sales</p>
</div>
</body>
</html>