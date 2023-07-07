<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ page import="Service.UnitService,Service.UnitServiceImplementation,java.util.List,Entity.Unit,Entity.Product,DAO.ProductDAO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>Product Form</title>
<%
      response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"  );
      response.setHeader("Pragma","no-cache");
      response.setHeader("Expires","0");

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
 font-size: 80%;
 }
 .productForm{
 position: relative;
 left    : 20px;
 top:    30px;
 width: 388px;
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
width: 405px;
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
.productEntry{
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
left: 135px;
top: 20px;
font-family: 'Courier New', monospace;
color: #303136;
font-style: bold;
font-size: 120%;
}
</style>
</head>
<%
Product product = null;
ProductDAO productDAO = new ProductDAO();
UnitService unitService =  new UnitServiceImplementation();
List<Unit> unitList = unitService.list();
request.setAttribute("unitList",unitList);
if(request.getParameter("editCode")==null || request.getParameter("editCode").isEmpty())
{
request.setAttribute("action","createProduct");
request.setAttribute("page",request.getParameter("page"));
}
else {
request.setAttribute("editCode",request.getParameter("editCode"));
request.setAttribute("action","editProduct");
product = productDAO.findByCode(request.getParameter("editCode"));
request.setAttribute("product",product);
request.setAttribute("id",product.getId());
request.setAttribute("page",request.getParameter("page"));
}
%>
<body bgcolor="#303136">
</body>
<p id="message">${Error}</p>
<div class="productEntry">
<p id="title"> Product Details</p>
<form method="post" action="${action}">
<input type="hidden" name="id" value="${product.getId()}">
<input type="hidden" name="page" value="${page}">
<input type="hidden" name="editCode" value="${editCode}">
<label for="code">Code</label><br>
<input type="text" class="productForm" name="code" value="${product.getCode()}" placeholder="product code" oninvalid="this.setCustomValidity('Invalid format for product code')" oninput="setCustomValidity('')" pattern="^[a-zA-Z0-9\s]{2,6}$"  required>
<br><br>
<label for="name">Name</label><br>
<input type="text" class="productForm" name="name" placeholder="Name" oninvalid="this.setCustomValidity('Invalid format for product name')" oninput="setCustomValidity('')" pattern="^[a-zA-Z\s]{3,30}$" value="${product.getName()}" required>
<br><br>
<label for="unitcode">Unit Code</label><br>
<select name="unitcode" id="unit">
<c:forEach items="${unitList}" var="unit">
<option value="${unit.getCode()}">${unit.getCode()}</option>
</c:forEach>
</select>
<br><br>
<label for="type">Type</label><br>
<input type="text" class="productForm" name="type" placeholder="Type" oninvalid="this.setCustomValidity('Invalid format for product type')" oninput="setCustomValidity('')" pattern="^[a-zA-Z\s]{3,30}$" value="${product.getType()}" required/>
<br><br>
<label for="stock">Stock</label><br>
<input type="text" class="productForm" name="stock" placeholder="Stock Left" oninvalid="this.setCustomValidity('Invalid format for numeric type')" oninput="setCustomValidity('')" pattern="^(\d*\.)?\d+$" value="${product.getAvailableQuantity()}" required/>
<br><br>
<label for="type">Price</label><br>
<input type="text" class="productForm" name="price" placeholder="Price" oninvalid="this.setCustomValidity('Invalid format for numeric type')" oninput="setCustomValidity('')" pattern="^(\d*\.)?\d+$" value="${product.getPrice()}" required/>
<br><br>
<input id="registerButton" type="submit" value="Submit">
</form>
</div>
<script>
    document.getElementById("unit"). value="${product.getunitcode()}";
</script>
</html>