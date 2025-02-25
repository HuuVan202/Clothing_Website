<%-- 
    Document   : accountManagement
    Created on : Feb 17, 2025, 10:03:02 AM
    Author     : NhatQuy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Account Management</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h2 class="text-center">Customer Account Management</h2>

    <!-- Success message -->
    <c:if test="${param.updateSuccess == 'true'}">
        <div class="alert alert-success">Account updated successfully!</div>
    </c:if>

    <!-- Search & Filters -->
    <div class="d-flex mb-3">
        <input type="text" id="search" class="form-control me-2" placeholder="Search">
        <select id="roleFilter" class="form-select me-2">
            <option value="">Select role</option>
            <option value="Admin">Admin</option>
            <option value="Customer">Customer</option>
        </select>
        <select id="statusFilter" class="form-select">
            <option value="">All Statuses</option>
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
        </select>
    </div>

    <!-- Account & Customer Table -->
    <table class="table table-bordered">
        <thead class="table-dark">
            <tr>
                <th>Customer ID</th>
                <th>Customer Name</th>
                <th>Username</th>
                <th>Password</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Address</th>
                <th>Role</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="entry" items="${accounts}">
                <tr>
                    <td>${entry[0].cus_id}</td>
                    <td>${entry[0].cus_name}</td>
                    <td>${entry[1].username}</td>
                    <td>${entry[1].password}</td>
                    <td>${entry[0].email}</td>
                    <td>${entry[0].phone}</td>
                    <td>${entry[0].address}</td>
                    <td>${entry[1].role}</td>
                    <td>${entry[1].acc_status}</td>
                    <td>
                        <a href="#"> Update </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

