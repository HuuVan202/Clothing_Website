<%-- 
    Document   : profile
    Created on : Apr 8, 2025, 3:16:08 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <!-- Profile Section -->
        <div class="col-lg-4 mb-4">
            <div class="card">
                <div class="card-header">            
                    <h6 class="mb-0"><i class="bi bi-person-circle"></i> Profile</h6>
                </div>
                <div class="card-body">
                    <div class="text-center">
                        <img src="${pageContext.request.contextPath}/img/icon/header/shipper.png" 
                             class="rounded-circle mb-3" width="120" height="120" alt="Shipper Avatar">
                        <h5>${sessionScope.customer.cus_name}</h5>
                    </div>

                    <hr>
                    <div class="text-start px-3">
                        <p ><i class="bi bi-envelope me-2"></i> ${sessionScope.customer.email}</p>
                        <p><i class="bi bi-phone me-2"></i> ${sessionScope.customer.phone}</p>
                        <p><i class="bi bi-geo-alt me-2"></i> ${sessionScope.customer.address}</p>
                    </div>

                    <div class="text-center mt-3">
                        <button
                            type="button"
                            class="btn btn-outline-primary edit-btn" 
                            data-bs-toggle="modal" 
                            data-bs-target="#updateModal">
                            <i class="bi bi-pencil-square"></i> Edit Profile
                        </button>
                    </div>
                </div>

                <div>
                    <c:if test="${not empty sessionScope.customer}">
                        <form action="profile" method="POST">
                            <input hidden value="dashboardcontroller" name="from"/>
                            <input hidden id="txtUserName" name="txtUserName" value="${sessionScope.customer.username}"/>
                            <table>
                                <tbody>
                                    <tr class="customer-name">
                                        <td><input class="editable" value="${sessionScope.customer.cus_name}" id="txtFullName" name="txtFullName" hidden /></td>
                                    </tr>
                                    <tr class="customer-email">
                                        <td><input value="${sessionScope.customer.email}" type="text" id="txtEmail" name="txtEmail" hidden /></td>
                                    </tr>
                                    <tr class="customer-phone">
                                        <td><input class="editable" value="${sessionScope.customer.phone}" id="txtPhone" name="txtPhone" hidden /></td>
                                    </tr>
                                    <tr class="customer-address">
                                        <td><input class="editable" value="${sessionScope.customer.address}" id="txtAddress" name="txtAddress" hidden /></td>
                                    </tr>
                                </tbody>
                            </table>

                        </form>      
                        <c:if test="${not empty requestScope.message}">
                            <span class="message container ${requestScope.message == 'Update Successful!' || requestScope.message == 'Password Change Successful' ? 'success' : 'error'}">
                                ${requestScope.message}
                            </span>
                        </c:if>
                    </c:if> 
                </div>
                <!-- Modal -->
                <div class="modal fade" id="updateModal" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content rounded-4 shadow">
                            <div class="modal-header">
                                <h5 class="modal-title" id="updateModalLabel">Update Information</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <form action="profile" method="POST">
                                <div class="modal-body">
                                    <input type="hidden" name="from" value="dashboardcontroller">
                                    <input type="hidden" id="txtUserName" name="txtUserName" value="${sessionScope.customer.username}">

                                    <div class="mb-3">
                                        <label for="modalFullName" class="form-label">Full Name</label>
                                        <input type="text" class="form-control" id="modalFullName" name="txtFullName" value="${sessionScope.customer.cus_name}">
                                    </div>
                                    <div class="mb-3">
                                        <label for="modalEmail" class="form-label">Email</label>
                                        <input type="email" class="form-control" id="modalEmail" name="txtEmail" value="${sessionScope.customer.email}" readonly>
                                    </div>
                                    <div class="mb-3">
                                        <label for="modalPhone" class="form-label">Phone</label>
                                        <input type="text" class="form-control" id="modalPhone" name="txtPhone" value="${sessionScope.customer.phone}">
                                    </div>
                                    <div class="mb-3">
                                        <label for="modalAddress" class="form-label">Address</label>
                                        <input type="text" class="form-control" id="modalAddress" name="txtAddress" value="${sessionScope.customer.address}">
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                    <button type="submit" class="btn btn-success">Save Changes</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>



            </div>
        </div>
    </body>
</html>
