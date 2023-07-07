<!DOCTYPE html>
<html>
<head>
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
label{
position: relative;
font-family: 'Courier New', monospace;
color: #303136;
left: 20px;
top: 20px;
font-size: 90%;
}
#head{
font-family: 'Courier New', monospace;
color: white;
font-size: 150%;
margin-top: -20px;
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
margin-top: 5%;
}
#title{
position: relative;
left: 80px;
top: 20px;
font-family: 'Courier New', monospace;
color: #303136;
font-style: bold;
font-size: 130%;
}
</style>
</head>
<body bgcolor="#303136">
 <div class="register">
         <p id="title"> Register Admin</p>
         <form  method="post" action="createAdmin">
             <label for="username">Username</label><br>
             <input type="text" class="registerForm" name="username" placeholder="username" oninvalid="this.setCustomValidity('Invalid format for username')" oninput="setCustomValidity('')" minlength="3" maxlength="30" pattern="^[a-zA-Z\s]{3,30}$" required>
             <br><br>
             <label for="password">Password</label><br>
             <input type="password" class="registerForm" name="password" placeholder="password" oninvalid="this.setCustomValidity('Invalid format for password')" oninput="setCustomValidity('')" minlength="8" maxlength="30" pattern="^[a-zA-Z0-9]{8,30}$" onChange="onChange()" required>
             <br><br>
             <label for="re-password">Re-Enter Password</label><br>
             <input type="password" class="registerForm" name="re-password" placeholder="password"  oninput="setCustomValidity('')" minlength="8" maxlength="30" pattern="^[a-zA-Z0-9]{8,30}$" onChange="onChange()" required>
             <br><br>
             <label for="firstname">FirstName</label><br>
             <input type="text" class="registerForm" name="firstname" placeholder="firstname" oninvalid="this.setCustomValidity('Invalid format for firstname')" oninput="setCustomValidity('')"  pattern="^[a-zA-Z\s]{3,30}$" required>
             <br><br>
             <label for="lastname">LastName</label><br>
             <input type="text" class="registerForm" name="lastname" placeholder="lastname" oninvalid="this.setCustomValidity('Invalid format for lastname')" oninput="setCustomValidity('')"    pattern="^[a-zA-Z\s]{1,30}$" required>
             <br><br>
             <label for="phonenumber">PhoneNumber</label><br>
             <input type="text" class="registerForm" name="phonenumber" placeholder="phonenumber" oninvalid="this.setCustomValidity('Invalid format for phoneNumber')" oninput="setCustomValidity('')" minlength="10" maxlength="10" pattern="^[6789]\d{9}$" required>
             <br><br>
             <input id="registerButton" type="submit" value="Register">
         </form>
         </div>
</body>
<script>
function onChange() {
  const password = document.querySelector('input[name=password]');
  const confirm = document.querySelector('input[name=re-password]');
  if (confirm.value === password.value) {
    confirm.setCustomValidity('');
  } else {
    confirm.setCustomValidity('Passwords do not match');
  }
}
</script>
</html>