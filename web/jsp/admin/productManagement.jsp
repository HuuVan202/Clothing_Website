<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%-- 
    Document   : productManagement
    Created on : Feb 17, 2025, 10:03:29 AM
    Author     : Vu_Hoang
--%>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Product Management</title>

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/common/layout/layout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/admin/managerLayout.css"/>
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="../common/layout/header.jsp" />


        <div class="container mt-4">
            <h2 class="text-center">Product Management</h2>

            <!-- Success message -->
            <c:if test="${param.updateSuccess == 'true'}">
                <div class="alert alert-success text-center">Product updated successfully!</div>
            </c:if>

            <!-- Search & Filters -->
            <div class="row g-2 mb-3">
                <div class="col-md-2">
                    <select id="genderFilter" class="form-select" onchange="updateLabel()">
                        <option value="" selected hidden>Gender</option>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                        <option value="Unisex">Unisex</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <select id="brandFilter" class="form-select" onchange="updateLabel()">
                        <option value="" selected hidden>Brand</option>
                        <option value="All Brand">All Brand</option>
                        <option value="Adidas">Adidas</option>
                        <option value="Calvin Klein">Calvin Klein</option>
                        <option value="Lacoste">Lacoste</option>
                        <option value="MLB">MLB</option>
                        <option value="New Era">New Era</option>
                        <option value="Nike">Nike</option>
                        <option value="Puma">Puma</option>
                        <option value="Tommy Hilfiger">Tommy Hilfiger</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <select id="typeFilter" class="form-select" onchange="updateLabel()"">
                        <option value="" selected hidden>Type</option>
                        <option value="All Type">All Type</option>
                        <option value="Shirt">Shirt</option>
                        <option value="T-Shirt">T-Shirt</option>
                        <option value="Jacket">Jacket</option>
                        <option value="Pants">Pants</option>
                        <option value="Shorts">Shorts</option>
                        <option value="Sunglasses">Sunglasses</option>
                        <option value="Wallet">Wallet</option>
                        <option value="Bag">Bag</option>
                        <option value="Hat">Hat</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <select id="inStockFilter" class="form-select" onchange="updateLabel()" >
                        <option value="">In Stock</option>
                        <option value="Admin">No Stock</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <select id="statusFilter" class="form-select" onchange="updateLabel()">
                        <option value="" selected hidden>Status</option>
                        <option value="All Status">All Status</option>
                        <option value="Active">Active</option>
                        <option value="Inactive">Inactive</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <select id="sortFilter" class="form-select" onchange="updateLabel()">
                        <option value="" selected hidden>Sort by</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <input type="text" id="search" class="form-control" placeholder="Search">
                </div>
            </div>

            <!-- Product Table -->
            <table class="table table-bordered text-center">
    <thead class="table-dark">
        <tr>
            <th class="align-middle">ID</th>
            <th class="align-middle">Product</th>
            <th class="align-middle">Size</th>
            <th class="align-middle">Gender</th>
            <th class="align-middle">Brand</th>
            <th class="align-middle">Type</th>
            <th class="align-middle">Price</th>
            <th class="align-middle">Discount</th>
            <th class="align-middle">Stock</th>
            <th class="align-middle">Status</th>
            <th class="align-middle">Action</th>
        </tr>
    </thead>

    <tbody>
        <c:choose>
            <c:when test="${not empty productList}">
                <c:forEach items="${productList}" var="p">
                    <tr>
                        <td class="align-middle">${p.pro_id}</td>
                        <td class="align-middle">
                            <div class="row align-items-center">
                                <div class="col-md-5 text-center">
                                    <img src="${p.image}" alt="${p.pro_name}" class="img-fluid rounded">
                                </div>
                                <div class="col-md-7">
                                    <div class="product-name">${p.pro_name}</div>
                                </div>
                            </div>
                        </td>
                        <td class="align-middle">${p.size}</td>
                        <td class="align-middle">${p.gender}</td>
                        <td class="align-middle">${p.brand}</td>
                        <td class="align-middle">${p.type.type_name}</td>
                        <td class="align-middle">${p.formattedPrice}</td>
                        <td class="align-middle">${p.discount}%</td>
                        <td class="align-middle">${p.stock}</td>
                        <td class="align-middle">${p.status}</td>
                        <td class="align-middle">
                            <a href="#"> Update </a>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="11" class="text-center text-muted align-middle">${productListMessage}</td>
                </tr>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>

        </div>
        <!-- Footer -->
        <jsp:include page="../common/layout/footer.jsp" />

        <script>
            function upda                                                                                                                teLabel() {
            let genderSelect = document.getElementById("gend                                                                                erFilter");
            if (genderSelect.valu                                                                                e === "") {
            genderSelect.firstElementChild.hidd                                                                                en = false;
            } else {
            genderSelect.firstElementChild.hid                                                                                den = true;
            }
            }
        </script>
    </body>
</html>

