<!DOCTYPE html>
<html>
<head>
<style>
#loginBadge{
position: relative;
width: 100px;
padding 10px;
bottom: 170px;
left: 102px;
}
.login{
margin: auto;
border-radius: 25px;
border 2px white;
display: flex;
justify-content: center;
align-items: center;
height: 350px;
width: 250px;
background-color: #D3D3D3;
margin-top: 12%;
}
.badges{
position: relative;
height: 22px;
width: 22px;
left: -90px;
top: 32px;
}
input[type=text]{
  background-color: white;
  padding-left: 30px;
  height: 30px;
  text-align: left;
}
input[type=password]{
  background-color: white;
  padding-left: 30px;
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
.loginForm{
position: relative;
left: -50px;
top: 30px;
}
</style>
</head>
<body>
<div class="login">
          <img src="Images/user.png" id="loginBadge">
          <form align="center"class="loginForm" method="post" action="login">
              <img class="badges" src="Images/userbadge.png">
                  <input type="text" id="username" name="username" placeholder="username" oninvalid="this.setCustomValidity('Invalid format for username')" oninput="setCustomValidity('')"  pattern="^[a-zA-Z\s]{3,30}$" required>
              <br><br>
              <img class="badges" src="Images/passwordbadge.png">
              <input type="password" id="password" name="password" placeholder="password" oninvalid="this.setCustomValidity('Invalid format for password')" oninput="setCustomValidity('')"      pattern="^[a-zA-Z0-9]{8,30}$" required>
              <br><br><br><br>
              <input type="submit" value="Login">
</form>
</div>
</body>
</html>