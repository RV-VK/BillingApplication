<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ page import="Service.ProductService,Service.ProductServiceImplementation,java.util.HashMap,java.util.List,Entity.Product,java.util.Arrays"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head><title>Product List</title>
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
right: 0;
border: 0px outset #485582;
background-color: #485582;
text-align: center;
height: 50px;
width: 100%;
}
#title{
margin-top: 80px;
margin-left: 630px;
font-size: 120%;
font-family: 'Courier New', monospace;
color: white;
}
#attribute{
margin-top: 50px;
margin-left: 130px;
appearance: auto;
outline: 0;
background: Antique-white;
color: black;
height: 35px;
width: 205px;
cursor: pointer;
padding: 7px 10px;
border: 2px solid black;
}
#searchText{
margin-top: 50px;
margin-left: 100px;
color: black;
padding: 0px 10px;
height: 30px;
width: 205px;
border: 2px solid black;
}
#search{
position: absolute;
display: inline;
margin-top: 50px;
margin-left:505px;
height: 35px;
width: 35px;
}
.mainTable{
border-collapse: collapse;
width: 80%;
margin-top: 10px;
margin-left: 130px;
}
td, th {
    border-bottom: 1px solid #ddd;
    padding: 8px;
    text-align: center;
    font-family: 'Courier New', monospace;
}
tr:nth-child(odd) {
    background: #eee;
}
tr:nth-child(even) {
    background: #D3D3D3;
}
tr:nth-child(odd):hover {
    background: coral;
}
tr:nth-child(even):hover {
    background: #cd5b45;
}
caption {
    font-size: 0.8rem;
}
.logout{
position: relative;
height: 30px;
width: 30px;
top: -65px;
right: -650px;
}
#head{
font-family: 'Courier New', monospace;
color: white;
font-size: 150%;
padding: 12px 16px;
margin-top: 0px;
margin-right: 1200px;
}
ul {
  position: absolute;
  list-style-type: none;
  display: flex;
  padding: 0 0;
  margin: 0;
  height: 50px;
  overflow: hidden;
  background-color: #D3D3D3;
  margin-top: 40px;
  margin-left: 530px;
}
li {
  float: left;
}
li a,span{
  display: block;
  color: black;
  padding: 14px 16px;
  text-align: center;
  text-decoration: none;
}

#img{
height: 30px;
width: 30px;
}
li a:hover {
  background-color: #485582;
}
li span:hover {
   background-color: #485582;
}
#empty{
position: absolute;
margin-left: 500px;
margin-top: 30px;
font-family: 'Courier New', monospace;
color: Red;
}
</style>
</head>
<%
response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"  );

      if(session.getAttribute("username")==null)
      {
        response.sendRedirect("index.jsp");
      }
List<String> productAttributes = Arrays.asList("id","code","name","unitcode","type","stock","price");
request.setAttribute("attributes",productAttributes);
%>
<body bgcolor="#303136">
<p id="title">PRODUCTS</p>
<form style="display: inline;" action="productList" method="get">
<select name="attribute" id="attribute">
<option value="Search Attribute" disabled selected>Search Attribute</option>
<c:forEach items="${attributes}" var="attribute">
<option value="${attribute}">${attribute}</option>
</c:forEach>
</select>
<input type="text" name="searchText" id="searchText" placeholder="Search String">
<input type="image" id="search" src="Images/search.png" alt="submit">
</form>
<table class="mainTable" border="1">
<tr class="mainRow"><th>ID</th><th>CODE</th><th>NAME</th><th>UNITCODE</th><th>TYPE</th><th>STOCK</th><th>PRICE</th>
<c:forEach items="${productList}" var="product">
<tr><td>${product.getId()}</td><td>${product.getCode()}</td><td>${product.getName()}</td>
<td>${product.getunitcode()}</td><td>${product.getType()}</td><td>${product.getAvailableQuantity()}</td>
<td>${product.getPrice()}</td>
</tr>
</c:forEach>
</table>
<c:if test="${noOfPages eq 0}">
<p id="empty"> No Records Found with Given Specifications!</p>
</c:if>
<c:if test="${noOfPages gt 1}">
<ul>
<c:if test="${currentPage!=1}">
<li><a href="productList?page=${currentPage - 1}&attribute=${Attribute}&searchText=${Searchtext}">Previous</a></li>
</c:if>
<c:if test="${currentPage==1}">
<li><span style="color: grey;">Previous</span></li>
</c:if>
<c:forEach begin="1" end="${(noOfPages < 3) ? noOfPages : 3}" var="i">
<c:choose>
<c:when test="${currentPage eq i}">
<li><span style="color: grey;">${i}</span></li>
</c:when>
<c:otherwise>
<li><a href = "productList?page=${i}&attribute=${Attribute}&searchText=${Searchtext}">${i}</a><li>
</c:otherwise>
</c:choose>
</c:forEach>
<c:if test="${currentPage  lt noOfPages}">
<li><a href="productList?page=${currentPage + 1}&attribute=${Attribute}&searchText=${Searchtext}">Next</a></li>
</c:if>
<c:if test="${currentPage eq noOfPages}">
<li><span style="color: grey;">Next</span></li>
</c:if>
</ul>
</c:if>
</body>
<script>
</script>
</html>