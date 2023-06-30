<%@ page import="DAO.StoreDAO,Entity.Store" isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Store Details</title>
<style>
.header{
position: absolute;
top: 0;
left: 0;
background-color: #485582;
width: 100%;
height: 50px;
}
#head{
font-family: 'Courier New', monospace;
color: white;
font-size: 150%;
margin-top: -20px;
margin-left: 610px;
}
#cart{
position: relative;
height: 30px;
width: 30px;
top: 10px;
left: 1320px;
}
#bill{
position: relative;
height: 30px;
width: 30px;
top: 10px;
right: 30px;
}
#profile{
position: absolute;
font-family: 'Courier New', monospace;
color: black;
font-weight: bold;
font-size: 150%;
margin-top: -400px;
margin-left: -10px;
}
#name{
position: absolute;
font-family: 'Courier New', monospace;
color: #28282B;
border-radius: 2px;
background-color: #adb5d2;
border-bottom: 2px solid red;
font-style: italic;
text-align: center;
font-size: 150%;
margin-top: -300px;
margin-left: 0px;
}
#phone{
position: absolute;
font-family: 'Courier New', monospace;
color: black;
font-style: italic;
border-bottom: 1px solid green;
height: 30px;
width: 310px;
font-size: 120%;
margin-top: -100px;
margin-left: 0px;
}
#contact{
position: absolute;
height: 30px;
width: 30px;
margin-top: -95px;
margin-left: -380px;
}
#loc{
position: absolute;
font-family: 'Courier New', monospace;
color: black;
font-style: italic;
border-bottom: 1px solid green;
font-size: 100%;
margin-top: 80px;
margin-left: 0px;
}
#location{
position: absolute;
height: 40px;
width: 30px;
margin-top: 80px;
margin-left: -380px;
}
#gst{
position: absolute;
font-family: 'Courier New', monospace;
color: black;
font-style: italic;
height: 30px;
width: 315px;
border-bottom: 1px solid green;
font-size: 120%;
margin-top: 270px;
margin-left: 0px;
}
#gstcode{
position: absolute;
font-family: 'Courier New', monospace;
color: black;
font-style: italic;
font-size: 150%;
margin-top: 270px;
margin-left: -385px;
}
#edit{
position: absolute;
font-family: 'Courier New', monospace;
color: white;
margin-top: 200px;
margin-left: -110px;
transition-duration: 0.4s;
background-color: #485582;
width: 100px;
border-radius: 10px;
padding: 10px 24px;
}
#edit:hover{
background-color: #303136 ;
color: white;
}
#delete{
position: absolute;
font-family: 'Courier New', monospace;
color: white;
margin-top: 440px;
margin-left: 110px;
transition-duration: 0.4s;
background-color: #485582;
width: 100px;
border-radius: 10px;
padding: 10px 24px;
}
#delete:hover{
background-color: #303136 ;
color: white;
}
#message{
position: relative;
top: 50px;
text-align: center;
font-family: 'Courier New', monospace;
color: red;
}
#Success{
position: relative;
top: 50px;
text-align: center;
font-family: 'Courier New', monospace;
color: green;
}
.profile{
margin: auto;
border-radius: 25px;
border 2px white;
display: flex;
justify-content: center;
align-items: center;
height: 550px;
width: 450px;
background-color: #D3D3D3;
margin-top: 5%;
}
</style>
</head>
<body bgcolor="303136">
<%
response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"  );

      if(session.getAttribute("username")==null)
      {
        response.sendRedirect("index.jsp");
      }
    StoreDAO storeDAO = new StoreDAO();
    Store store = storeDAO.view();
    request.setAttribute("store",store);
%>
<div class="header">
<img src="Images/cart.png" id="cart">
<img src="Images/bill.png" id="bill">
<p id="head" style="font-size: 50%'">SmartPOS</p>
</div>
</body>
<div class="profile">
<p id="profile">STORE DETAILS</p>
<input type ="text" id="name" name="name" value="${store.getName()}" disabled>
<img id="contact" src="Images/contact.png">
<input type="tel" id="phone" name="phoneNumber" value="${store.getPhoneNumber()}" disabled>
<img id="location" src="Images/locationit.png">
<textarea id="loc" name="address" rows="2" cols="30" disabled>${store.getAddress()}</textarea>
<label id="gstcode">GST: </label>
<input type="text" id="gst" name="gstNumber" value="${store.getPhoneNumber()}" disabled>
<script>
</script>
</html>