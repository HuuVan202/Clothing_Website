<%-- 
    Document   : checkOTP
    Created on : Mar 10, 2025, 2:57:59 PM
    Author     : Admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/checkOTP.css">
    </head>
    <body>
        <div class="container height-100 d-flex justify-content-center align-items-center">
            <div class="position-relative">
                <div class="card p-2 text-center">
                    <h6>Please enter the OTP <br> to verify your account</h6>
                    <div> <span>A code has been sent to</span> <small>${requestScope.mail}</small> </div>
                    <form action="CheckOTP" method="POST" onsubmit="combineOTP()">
                        <input type="hidden" name="otp" id="otpHidden" />
                        <div id="otp" class="inputs d-flex flex-row justify-content-center mt-2">
                            <input class="m-2 text-center form-control rounded" type="text" id="first" maxlength="1" />
                            <input class="m-2 text-center form-control rounded" type="text" id="second" maxlength="1" />
                            <input class="m-2 text-center form-control rounded" type="text" id="third" maxlength="1" />
                            <input class="m-2 text-center form-control rounded" type="text" id="fourth" maxlength="1" />
                            <input class="m-2 text-center form-control rounded" type="text" id="fifth" maxlength="1" />
                            <input class="m-2 text-center form-control rounded" type="text" id="sixth" maxlength="1" />
                        </div>
                        <c:if test="${not empty requestScope.mess}">
                            <div class="mt-4">
                                <span class="error-message">${requestScope.mess}</span>
                            </div>
                        </c:if>
                        <div class="mt-4"> <button class="btn btn-danger px-4 validate" type="submit">Validate</button> </div>
                    </form>
                </div>
                <div class="card-2">
                    <div class="content d-flex justify-content-center align-items-center">
                        <span>Didn't get the code</span>
                        <span class="ms-3 text-danger fw-bold">Resend in <span id="counter" style="display: inline">60</span>s
                        </span>
                    </div>
                </div>

            </div>
        </div>
    </body>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const otpInputs = document.querySelectorAll("#otp input");
            const hiddenOtp = document.getElementById("otpHidden");

            function combineOTP() {
                let otpValue = "";
                otpInputs.forEach(input => otpValue += input.value);
                hiddenOtp.value = otpValue;
            }

            function OTPInput() {
                otpInputs.forEach((input, index) => {
                    input.addEventListener("input", function (event) {
                        this.value = this.value.replace(/[^0-9]/g, ""); // Chỉ cho nhập số
                        if (this.value && index < otpInputs.length - 1) {
                            otpInputs[index + 1].focus();
                        }
                        combineOTP();
                    });

                    input.addEventListener("keydown", function (event) {
                        if (event.key === "Backspace" && !this.value && index > 0) {
                            otpInputs[index - 1].focus();
                        }
                    });
                });
            }

            OTPInput();
        });


    </script>
</html>
