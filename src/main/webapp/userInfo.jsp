<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Info</title>
</head>
<body>

<h3>Hello: ${user.userName}</h3>
<p>User Name: <b>${user.userName}</b>></p>
<p>Last login: <b>${user.loginDate}</b></p>
<p>Last logout: <b>${user.logoutDate}</b></p>
<br />
<a href="logout">Log out</a>
</body>
</html>