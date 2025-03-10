<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Customer Account Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/admin/managerLayout.css"/>
    </head>
    <body>
        <div class="d-flex h-100">
            <!-- Sidebar -->
            <jsp:include page="../common/layout/managerSidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="flex-grow-1">
                    <!-- Header -->
                <jsp:include page="../common/layout/managerHeader.jsp"></jsp:include>
                    <div class="content-area">
                        <!-- Content Area -->
                        <div class="container mt-4">
                            <h2 class="text-center">Customer Account Management</h2>

                            <!-- Success message -->
                        <c:if test="${param.updateSuccess == 'true'}">
                            <div class="alert alert-success">Account updated successfully!</div>
                        </c:if>
                        <!-- Search & Filters -->
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <form action="filterAccountManagement" method="get">
                                    <select name="status" class="form-select" onchange="this.form.submit()">
                                        <option value="all" ${selectedStatus == 'all' ? 'selected' : ''}>All</option>
                                        <option value="active" ${selectedStatus == 'active' ? 'selected' : ''}>Active</option>
                                        <option value="inactive" ${selectedStatus == 'inactive' ? 'selected' : ''}>Inactive</option>
                                    </select>
                                </form>
                            </div>
                            <div class="col-md-6">
                                <form action="searchAccountManagement" method="get" class="d-flex">
                                    <input type="text" name="username" class="form-control me-2" placeholder="Search Username"
                                           value="${search}">
                                    <button type="submit" class="btn btn-primary" style="background: #233446" >Search</button>
                                </form>
                            </div>
                        </div>
                        <!-- Account & Customer Table -->
                        <table class="table table-bordered">
                            <thead class="table-dark">
                                <tr>
                                    <th>Customer ID</th>
                                    <th>Customer Name</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                    <th>Address</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${ not empty errorMessage}">
                                        <tr>
                                            <td colspan="9" class="text-center text-danger">${errorMessage}</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="entry" items="${accounts}">
                                            <c:set var="customer" value="${entry[0]}" />
                                            <c:set var="account" value="${entry[1]}" />                                  
                                            <tr>
                                                <td>${customer.cus_id}</td>
                                                <td>${customer.cus_name}</td>
                                                <td>${account.username}</td>
                                                <td>${customer.email}</td>
                                                <td>${customer.phone}</td>
                                                <td>${customer.address}</td>
                                                <td>${account.role}</td>
                                                <td>${account.acc_status}</td>
                                                <td>
                                                    <a style="background: #233446" href="updateAccountManagement?cus_id=${customer.cus_id}" class="btn btn-primary btn-sm">Update</a>
                                                </td>                                       
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
