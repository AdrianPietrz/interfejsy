<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot password</title>
</head>
<body>

<h3>Forgot Page</h3>
<p style="color: red;">${errorString}</p>
<form method="POST" action="doForget">
    <table border="0">
        <tr>
            <td>User Name</td>
            <td><input type="text" name="userName" value=
                    "${user.userName}" /> </td>
        </tr>
        <tr>
            <td colspan ="2">
                <input type="submit" value= "Submit" />
            </td>
        </tr>
    </table>
</form>
</body>
</html>