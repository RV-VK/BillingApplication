<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="DAO.StoreDAO,Entity.Store,Entity.Product,java.util.List,java.util.ArrayList,java.util.stream.Stream,java.util.stream.Collectors,java.util.HashMap,java.util.Arrays,Service.ProductService,Service.ProductServiceImplementation"%>
<!DOCTYPE html>
<html>
<head>
<title>Billing Menu</title>
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
.contentHolder{
position: absolute;
width: 100%;
height: 170px;
top: 50px;
left: 0;
background-color:  #F9DED7;
border: 0px outset white;
}
.mainTable{
position: absolute;
border-collapse: collapse;
width: 100%;
top: 0px;
left: 0px;
overflow-y: scroll;
overflow-x: hidden;
}
.tableholder{
position: absolute;
height: 250px;
width: 100%;
background: white;
border-top : 0px outset black;
border-bottom : 2px outset black;
top: 220px;
left: 0px;
overflow-y: scroll;
overflow-x: hidden;
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
#title{
font-family: 'Courier New', monospace;
color: black;
font-size: 150%;
text-align: center;
font-weight: bold;
}
label{
font-family: 'Courier New', monospace;
color: black;
font-size: 110%;
}
#storeName{
position: absolute;
left: 370px;
}
#date{
position: absolute;
left: 820px;
}
#storeBox{
position: absolute;
top: 67px;
left: 520px;
width: 200px;
border : 1px solid black;
height: 30px;
border : 1px solid black;
color: black;
}
#dateBox{
position: absolute;
top: 67px;
width: 200px;
height: 30px;
border : 1px solid black;
left: 900px;
}
#searchBarLabel{
position: absolute;
left: 50px;
top: 136px;
}
#quantityLabel {
position: absolute;
left: 510px;
top: 7px;
}
#searchBar{
position: absolute;
left: 270px;
padding: 0px 3px;
height: 30px;
width: 200px;
border : 1px solid black;
}
#quantity{
position: absolute;
left: 630px;
padding: 0px 3px;
height: 30px;
width: 200px;
border: 1px solid black;
}
#add{
position: absolute;
background-color: #485582;
font-family: 'Courier New', monospace;
left: 860px;
width: 30px;
font-size: 150%;
height: 30px;
border-radius: 30px;
transition-duration: 0.4s;
color: white;
}

#add:hover{
background-color: #303136;
}
#error{
position: absolute;
font-family: 'Courier New', monospace;
color: red;
font-size: 100%;
top: 170px;
left: 950px;
}
.total{
position: absolute;
width: 300px;
height: 50px;
top: 467px;
left: 1053px;
padding: 0 4px;
background-color: white;
border: 2px outset black;
}
#grandTotal{
position: absolute;
font-family: 'Courier New', monospace;
color: black;
font-size: 100%;
left: 10px;
}
#grandTotalValue{
position: absolute;
font-family: 'Courier New', monospace;
color: black;
font-size: 100%;
left: 160px;
}
.generateBill{
position: absolute;
background-color: #485582;
width: 170px;
height: 40px;
border-radius: 10px;
bottom: 40px;
left: 600px;
transition-duration: 0.4s;
color: white;
}
.generateBill:hover {
background-color: #303136;
}
#img{
height: 30px;
width: 30px;
}
.autocomplete {
  position: absolute;
  display: inline-block;
  height: 30px;
  width: 200px;
}
.autocomplete-items {
  position: absolute;
  z-index: 2;
  top: 30px;
  left: 270px;
  height: 30px;
  width: 205px;
  right: 0px;
}
.autocomplete-items div {
  padding: 10px;
  cursor: pointer;
  background-color: #fff;
  border-bottom: 1px solid #d4d4d4;
}
.autocomplete-items div:hover {
  background-color: #e9e9e9;
}
.autocomplete-active {
  background-color: DodgerBlue !important;
  color: #ffffff;
}
</style>
</head>
<%
StoreDAO storeDAO = new StoreDAO();
Store store = storeDAO.view();
request.setAttribute("store",store);
List<Product> selectedList = new ArrayList<>();
ProductService productService = new ProductServiceImplementation();
HashMap<String,String> listAttributes = new HashMap<>();
listAttributes.put("Attribute","id");
listAttributes.put("Searchtext",null);
listAttributes.put("Pagelength",String.valueOf(Integer.MAX_VALUE));
listAttributes.put("Pagenumber","1");
List<Product> productList = productService.list(listAttributes);
productList = productList.stream().filter(product-> product.getAvailableQuantity()>0).collect(Collectors.toList());
List<String> productKeyList = new ArrayList<>();
for(Product product: productList){
String name = product.getName();
String code = product.getCode();
productKeyList.add(name);
productKeyList.add(code);
}
session.setAttribute("selectedList",request.getAttribute("selectedList"));
request.setAttribute("productList",productKeyList);
%>
<body bgcolor="#303136">
<div class="contentHolder">
<p id="title">Sales Billing</p>
<form autocomplete="off" action="addToSale" method="post">
<label id="storeName" for="storeName">Store Name :</label>&nbsp&nbsp
<input type="text" name="storeName" id="storeBox"  value="${store.getName()}" disabled>
<label id="date" for="currentDate"> Date : </label>&nbsp&nbsp
<input type="date" name="currentDate" id="dateBox" value="${date}"><br><br><br>
<label id="searchBarLabel" for="searchBar"> Product Name/Code : </label>
<div class="autocomplete" style="width: 200px;">
<input type="text" name="searchBar" id="searchBar" placeholder="Id/Name" pattern="^[a-zA-Z0-9\s]{3,30}$" oninvalid="this.setCustomValidity('Invalid format for Product Code/Name')" oninput="setCustomValidity('')" required>
<label id="quantityLabel" for="quantity"> Quantity: </label>
<input type="text" name="quantity" id="quantity" placeholder="quantity" pattern="^(\d*\.)?\d+$" oninvalid=this.setCustomValidity('Invalid format for Quantity') required>
<input type="submit" id="add" title="add" value="+">
</form>
</div>
</div>
<div class="tableholder">
<table class="mainTable" border="1">
<tr><th>S.NO</th><th>PRODUCT CODE</th><th>PRODUCT NAME</th><th>PRICE</th><th>QUANTITY</th><th>AMOUNT</th><th>REMOVE</th></tr>
<c:set var="count" value="0" scope="page" />
<c:forEach items="${selectedList}" var="salesItem">
<c:set var="count" value="${count + 1}" scope="page"/>
<tr><td>${count}</td><td>${salesItem.getProduct().getCode()}</td><td>${salesItem.getProduct().getName()}</td><td>${salesItem.getProduct().getPrice()}</td><td>${salesItem.getQuantity()}</td><td>${salesItem.getProduct().getPrice() * salesItem.getQuantity()}</td><td><form action="addToSale" method="post"><input type="hidden" value="${salesItem.getProduct().getCode()}" name="deleteCode"><input type="image" id="img" src="Images/delete.png" alt="delete"></form></td></tr>
</c:forEach>
</table>
</div>
<div class="total">
<p id="grandTotal"> Grand Total : </p>
<p id="grandTotalValue">${grandTotal}</p>
</div>
<p id="error">${Error}</p>
<form action="createSales" method="post">
<input type="hidden" id="dateParam" name="dateValue" value="">
<c:if test="${selectedList.size() > 0}">
<input type="submit" class="generateBill" value="Generate Bill">
</c:if>
</form>
</body>
<script>
var date = document.getElementById("dateParam");
var currentDate =document.getElementById("dateBox");
date.value = currentDate.value;
function autocomplete(inp, arr) {
  var currentFocus;
  inp.addEventListener("input", function(e) {
      var a, b, i, val = this.value;
      closeAllLists();
      if (!val) { return false;}
      currentFocus = -1;
      a = document.createElement("DIV");
      a.setAttribute("id", this.id + "autocomplete-list");
      a.setAttribute("class", "autocomplete-items");
      this.parentNode.appendChild(a);
      for (i = 0; i < arr.length; i++) {
        if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
          b = document.createElement("DIV");
          b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
          b.innerHTML += arr[i].substr(val.length);
          b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
          b.addEventListener("click", function(e) {
              inp.value = this.getElementsByTagName("input")[0].value;
              closeAllLists();
          });
          a.appendChild(b);
        }
      }
  });
  inp.addEventListener("keydown", function(e) {
      var x = document.getElementById(this.id + "autocomplete-list");
      if (x) x = x.getElementsByTagName("div");
      if (e.keyCode == 40) {
        currentFocus++;
        addActive(x);
      } else if (e.keyCode == 38) { //up
        currentFocus--;
        addActive(x);
      } else if (e.keyCode == 13) {
        e.preventDefault();
        if (currentFocus > -1) {
          if (x) x[currentFocus].click();
        }
      }
  });
  function addActive(x) {
    if (!x) return false;
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    x[currentFocus].classList.add("autocomplete-active");
  }
  function removeActive(x) {
    for (var i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }
  function closeAllLists(elmnt) {
    var x = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
      }
    }
  }
  document.addEventListener("click", function (e) {
      closeAllLists(e.target);
  });
}
var products = [];
<%for(int i=0; i<productKeyList.size(); i++) { %>
products[<%= i %>] = "<%= productKeyList.get(i) %>";
<% } %>
autocomplete(document.getElementById("searchBar"), products);
</script>
</html>