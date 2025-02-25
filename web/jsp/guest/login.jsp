<%-- 
    Document   : login
    Created on : Feb 17, 2025, 10:06:29 AM
    Author     : HuuVan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>lOGIN</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/login.css"/>
    </head>
    <body>
        <div id="login-frame">
            <h2>Login</h2>
            <form action="Login" method="POST" id="login-form">
                <p>
                    <label for="txtUserName">Username or Email</label><br>
                    <input type="text" id="txtUserName" name="txtUserName" 
                           placeholder="Enter Username or Email" 
                           value="${cookie.cUserName.value}">
                </p>
                <h7 style="color: red">${requestScope.msg}</h7>
                <p>
                    <label for="txtPassword">Password</label><br>
                <div class="password-container">
                    <input type="password" id="txtPassword" name="txtPassword" 
                           placeholder="Enter Your Password" value="${cookie.cPassword.value}" >
                    <span class="toggle-password" onclick="togglePassword('txtPassword', this)">👁️</span>
                </div>
                </p>
                <button type="submit" name="btnAction" value="Login">Login</button>
                <p>
                    <label for="remember">
                        <input ${cookie.reMem.value==null?"":"checked"} id="remember" name="remember" type="checkbox">
                        Remember me
                    </label>
                </p>
            </form>
        </div>
        <script src="${pageContext.request.contextPath}/JS/guest/login.js"></script>
    </body>
</html>
