<%@ page import="DAO.StoreDAO,Entity.Store" isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="deleteOverlay.css">
<title>STORE PAGE</title>
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
margin-top: -250px;
margin-left: -50px;
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
margin-top: -200px;
margin-left: -150px;
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
margin-left: -150px;
}
#contact{
position: absolute;
height: 30px;
width: 30px;
margin-top: -95px;
margin-left: -200px;
}
#loc{
position: absolute;
font-family: 'Courier New', monospace;
color: black;
font-style: italic;
border-bottom: 1px solid green;
font-size: 100%;
margin-top: -20px;
margin-left: -150px;
}
#location{
position: absolute;
height: 40px;
width: 30px;
margin-top: -20px;
margin-left: -200px;
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
margin-top: 70px;
margin-left: -150px;
}
#gstcode{
position: absolute;
font-family: 'Courier New', monospace;
color: black;
font-style: italic;
font-size: 150%;
margin-top: 75px;
margin-left: -215px;
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
    request.setAttribute("Success",request.getParameter("Success"));
%>
<div class="header">
<img src="Images/cart.png" id="cart">
<img src="Images/bill.png" id="bill">
<p id="head" style="font-size: 50%'">SmartPOS</p>
</div>
</body>
<div class="modal">
<div class="modalContent">
<span class="close">×</span>
<p>Are you sure you want to delete the Store data ?  This will delete all your product/purchase/sales data</p>
<button class="del" onclick="openPrompt()">Yes</button>
<button class="cancel" onclick="hideModal()">Cancel</button>
</div>
</div>
<div class="modal2">
<div class="modalContent2">
<span class="close" onclick="closeModal()">×</span>
<p>Enter the Admin Password to delete</p>
<input type="password" id="password"   style="width: 240px; height: 30px; margin-left: -20px; padding: 0 20px; " placeholder="Admin password" name="password"><br><br>
<button class="delete" onclick="deleteStore()"> Delete </button>
</div>
</div>
<p id="message">${Error}</p>
<p id="Success">${Success}</p>
<div class="profile">
<form action="editStore" method="post">
<p id="profile">PROFILE</p>
<input type ="text" id="name" name="name" placeholder="Store name" oninvalid="this.setCustomValidity('Invalid format for Store name')" oninput="setCustomValidity('')" value="${store.getName()}"  pattern="^[a-zA-Z\s]{3,30}$" required disabled>
<img id="contact" src="Images/contact.png">
<input type="tel" id="phone" name="phoneNumber" placeholder="phone number" oninvalid="this.setCustomValidity('Invalid format for Contact number')" oninput="setCustomValidity('')" value="${store.getPhoneNumber()}" pattern="^[6789]\d{9}$"  required disabled>
<img id="location" src="Images/locationit.png">
<textarea id="loc" name="address" name="address" placeholder="Address" rows="2" cols="30"  required disabled>${store.getAddress()}</textarea>
<label id="gstcode">GST: </label>
<input type="text" id="gst" placeholder="gstCode" name="gstNumber" oninvalid="this.setCustomValidity('Invalid format for GST  code')" oninput="setCustomValidity('')" value="${store.getGstCode()}" pattern="^[a-zA-Z0-9]{15}$"  required disabled>
<input type="button" value="Edit" id="edit" onclick="enable()">
</form>
<button id="delete" class="openModal" onclick="myFunction()">Delete</button>
</div>
</body>
<script>
var flag = false;
var editButton = document.getElementById("edit");
function enable() {
    if(editButton.value == "Edit") {
    document.getElementById("name").disabled = false;
    document.getElementById("phone").disabled = false;
    document.getElementById("loc").disabled = false;
    document.getElementById("gst").disabled = false;
    editButton.value = "Submit";
    console.log("edit");
    }
    else if(editButton.value == "Submit") {
    editButton.setAttribute("type","Submit");
    }
}

   var modal = document.querySelector(".modal");
   var modal2 = document.querySelector(".modal2")
   var span = document.querySelector(".close");
   var btn = document.querySelector(".openModal")
   var btn2 = document.querySelector(".del")
   var passwordBox = document.getElementById("password");
   function myFunction() {
      modal.style.display = "block";
  }
   span.addEventListener("click", () => {
      hideModal();
   });
  function deleteStore() {
    window.location.href = "deleteStore?password="+passwordBox.value;
  }
  function closeModal() {
    hideModal();
  }
   function hideModal() {
      modal.style.display = "none";
      modal2.style.display = "none";
   }
   function openPrompt() {
     modal2.style.display = "block";
   }
   window.onclick = function(event) {
      if (event.target == modal || event.target == modal2) {
         hideModal();
      }
   };
</script>
</html>