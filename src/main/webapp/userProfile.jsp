<%@ page import="DAO.UserDAO,Entity.User" isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css" />
<link rel="stylesheet" href="css/style.css" />
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
#head{
font-family: 'Courier New', monospace;
color: white;
font-size: 150%;
margin-top: -20px;
}
label{
position: relative;
font-family: 'Courier New', monospace;
color: #303136;
left: 20px;
top: 20px;
font-size: 90%;
}
.registerForm{
position: relative;
left    : 20px;
top:    30px;
width: 290px;
}
#registerButton{
position: relative;
left: 120px;
top: 45px;
}input[type=text]{
   background-color: white;
   padding-left: 10px;
   height: 30px;
   text-align: left;
 }
 input[type=password]{
   background-color: white;
   padding-left: 10px;
   height: 30px;
   text-align: left;
 }
 input[type=submit]{
    background-color: #485582;
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
.register{
margin: auto;
border-radius: 25px;
border 2px white;
height: 570px;
width: 350px;
background-color: #D3D3D3;
margin-top: 90px;
}
#profile{
position: absolute;

width: 100px;
padding 10px;
top: 55px;
left: 632px;
}
form i {
    position: absolute;
    left: 800px;
    top: 335px;
    cursor: pointer;
}
#title{
position: relative;
left: 130px;
top: 70px;
font-family: 'Courier New', monospace;
font-weight: bold;
color: #303136;
font-style: bold;
font-size: 130%;
}
<%
response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"  );

      if(session.getAttribute("username")==null)
      {
        response.sendRedirect("index.jsp");
      }
    String userId = String.valueOf(session.getAttribute("userId"));
    UserDAO userDAO = new UserDAO();
    User user = userDAO.findById(Integer.parseInt(userId)   );
    request.setAttribute("user",user);
%>
</style>
</head>
<body bgcolor="#303136">
 <div class="register">
         <img src="Images/user.png" id="profile">
         <p id="title">Profile</p>
         <form>
             <br><br><br>
             <label for="username">Username</label><br>
             <input type="text" class="registerForm" name="username" value="${user.getUserName()}" disabled>
             <br><br><br>
             <label for="password">Password</label><br>
             <input type="password" id="passWord" class="registerForm" name="password" value="${user.getPassWord()}" disabled>
             <i class="bi bi-eye-slash" id="togglePassword"></i>
             <br><br><br>
             <label for="firstname">FirstName</label><br>
             <input type="text" class="registerForm" name="firstname" value="${user.getFirstName()}" disabled>
             <br><br><br>
             <label for="lastname">LastName</label><br>
             <input type="text" class="registerForm" name="lastname" value="${user.getLastName()}" disabled>
             <br><br><br>
             <label for="phonenumber">PhoneNumber</label><br>
             <input type="text" class="registerForm" name="phonenumber" value="${user.getPhoneNumber()}" disabled>
             <br><br><br>
         </form>
         </div>
</body>
<script>
const togglePassword = document.querySelector("#togglePassword");
        const password = document.querySelector("#passWord");

        togglePassword.addEventListener("click", function () {
            // toggle the type attribute
            const type = password.getAttribute("type") === "password" ? "text" : "password";
            password.setAttribute("type", type);

            // toggle the icon
            this.classList.toggle("bi-eye");
        });
</script>
</html>