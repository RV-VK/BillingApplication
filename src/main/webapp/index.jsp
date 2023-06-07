<%@ page import="Service.LoginService,Service.LoginServiceImplementation" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Admin Login</title></head>
<body>
<% String errorMessage = String.valueOf(request.getAttribute("Error"));%>
<% if(errorMessage.equals("Error")) { %>
<% out.println("<p style=\"color: red">Login credentials invalid ! You should try with a valid user name and password. If you have any issues contact software administrator.</p>"); %>
<% } %>
 <h1>WELCOME TO THE BILLING SOFTWARE!<h1>
 <% LoginService loginService = new LoginServiceImplementation();%>
 <% boolean adminCreated = loginService.checkIfInitialSetup(); %>
  <% if (!adminCreated) { %>
         <h2>Create Admin Account</h2>
         <form method="post" action="createAdmin">
             <label for="username">Username:</label>
             <input type="text" id="username" name="username" required>
             <br>
             <label for="password">Password:</label>
             <input type="password" id="password" name="password" required>
             <br>
             <input type="submit" value="Create Admin">
         </form>

  <% } else { %>
          <h2>Admin Login</h2>
          <form method="post" action="login">
              <label for="username">Username:</label>
              <input type="text" id="username" name="username" required>
              <br>
              <label for="password">Password:</label>
              <input type="password" id="password" name="password" required>
              <br>
              <input type="submit" value="Login">
          </form>
      <% } %>

</body>
</html>
