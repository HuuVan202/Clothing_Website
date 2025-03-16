<%-- 
    Document   : verifyMail
    Created on : Mar 10, 2025, 2:32:31 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="https://unpkg.com/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://unpkg.com/bs-brain@2.0.4/components/password-resets/password-reset-12/assets/css/password-reset-12.css">
    </head>
    <body>
        <!-- Password Reset 12 - Bootstrap Brain Component -->
        <section class="py-3 py-md-5 py-xl-8">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="mb-5">
                            <h2 class="display-5 fw-bold text-center">Password Reset</h2>
                            <p class="text-center m-0">Provide the email address associated with your account to recover your password.</p>
                        </div>
                    </div>
                </div>
                <div class="row justify-content-center">
                    <div class="col-12 col-lg-10 col-xl-8">
                        <div class="row gy-5 justify-content-center">
                            <div class="col-12 col-lg-5">
                                <form action="ResetPassword" method="POST">
                                    <div class="row gy-3 overflow-hidden">
                                        <div class="col-12">
                                            <div class="form-floating mb-3">
                                                <input type="email" class="form-control border-0 border-bottom rounded-0" name="email" id="email" value="${sessionScope.mail}" readonly>
                                                <label for="email" class="form-label">Email</label>
                                            </div>
                                            <div class="form-floating mb-3">
                                                <input type="password" class="form-control border-0 border-bottom rounded-0" name="newPassword" id="newPassword" required>
                                                <label for="newPassword" class="form-label">Password</label>
                                            </div>
                                            <div class="form-floating mb-3">
                                                <input type="password" class="form-control border-0 border-bottom rounded-0" name="rePassword" id="rePassword" required>
                                                <label for="rePassword" class="form-label">Re-Password</label>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="d-grid">
                                                <span>${requestScope.mess}</span>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="d-grid">
                                                <button class="btn btn-lg btn-dark rounded-0 fs-6" type="submit">Reset Password</button>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="row justify-content-between">
                                                <div class="col-6">
                                                    <a href="#!" class="link-secondary text-decoration-none">Login</a>
                                                </div>
                                                <div class="col-6">
                                                    <div class="text-end">
                                                        <a href="#!" class="link-secondary text-decoration-none">Register</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                           
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </body>
</html>
