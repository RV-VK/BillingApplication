<!DOCTYPE html>
<html>
<head><title>STORE PAGE</title>
<style>
.header{
position: absolute;
top: 0;
left: 0;
background-color: #485582;
width: 100%;
height: 25%;
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
%>
<div class="header">
</div>
</body>
</html>