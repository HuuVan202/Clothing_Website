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
                    <input type="text" id="txtFullName" name="txtFullName" 
                           value="${not empty requestScope.fullName ? requestScope.fullName : (not empty requestScope.accountGG.given_name ? requestScope.accountGG.given_name : '')} 
                           ${not empty requestScope.accountGG.family_name ? requestScope.accountGG.family_name : ''}">
                </p>
                <p>
                    <label for="txtUserName">Username</label><br>
                    <input type="text" id="txtUserName" name="txtUserName" 
                           value="${not empty requestScope.userName ? requestScope.userName : (not empty requestScope.accountGG.email ? requestScope.accountGG.email : '')}">
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
                    <input type="text" id="txtEmail" name="txtEmail" 
                           value="${not empty requestScope.email ? requestScope.email : (not empty requestScope.accountGG.email ? requestScope.accountGG.email : '')}">
                </p>
                <p>
                    <label for="txtPhone">Phone</label><br>
                    <input type="text" id="txtPhone" name="txtPhone" 
                           value="${not empty requestScope.phone ? requestScope.phone : ''}">
                </p>
                <p>
                    <label for="txtAddress">Address</label><br>
                    <input type="text" id="txtAddress" name="txtAddress" 
                           value="${not empty requestScope.address ? requestScope.address : ''}">
                </p>

                <!-- Hiển thị thông báo lỗi nếu có -->
                <p style="color: red">${requestScope.message}</p>

                <button type="submit" name="btnAction" value="signup">Sign Up</button>
            </form>
            <a href="home">Back to Home</a>
        </div>

        <script src="${pageContext.request.contextPath}/JS/guest/login.js"></script>
        <script>
                            function togglePassword(fieldId, element) {
                                let input = document.getElementById(fieldId);
                                if (input.type === "password") {
                                    input.type = "text";
                                    element.textContent = "🙈";
                                } else {
                                    input.type = "password";
                                    element.textContent = "👁️";
                                }
                            }
        </script>
    </body>
</html>
