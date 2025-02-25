<%-- 
    Document   : signup
    Created on : Feb 17, 2025, 10:06:38 AM
    Author     : HuuVan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SignUp</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/signup.css"/>
    </head>
    <body>
        <div id="signup-frame">
            <h2>Sign Up</h2>
            <form action="Signup" method="POST" id="signup-form">
                <p>
                    <label for="txtFullName">Full Name</label><br>
                    <input type="text" id="txtFullName" name="txtFullName" value="">
                </p>
                <p>
                    <label for="txtUserName">Username</label><br>
                    <input type="text" id="txtUserName" name="txtUserName" value="">
                </p>
                <p>
                    <label for="txtPassword">Password</label><br>
                <div class="password-container">
                    <input type="password" id="txtPassword" name="txtPassword" value="">
                    <span class="toggle-password" onclick="togglePassword('txtPassword', this)">👁️</span>
                </div>
                </p>

                <p>
                    <label for="txtPasswordSecond">Re-Enter password</label><br>
                <div class="password-container">
                    <input type="password" id="txtPasswordSecond" name="txtPasswordSecond" value="">
                    <span class="toggle-password" onclick="togglePassword('txtPasswordSecond', this)">👁️</span>
                </div>
                </p>

                <p>
                    <label for="txtEmail">Email</label><br>
                    <input type="text" id="txtEmail" name="txtEmail" value="">
                </p>
                <p>
                    <label for="txtPhone">Phone</label><br>
                    <input type="text" id="txtPhone" name="txtPhone" value="">
                </p>
                <p>
                    <label for="txtAddress">Address</label><br>
                    <input type="text" id="txtAddress" name="txtAddress" value="">
                </p>
                <h7 style="color: red">${requestScope.message}</h7>
                <button type="submit" name="btnAction" value="signup">Sign Up</button>
            </form>
        </div>

        <script src="${pageContext.request.contextPath}/JS/guest/login.js"></script>
    </body>
</html>


