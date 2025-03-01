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
                <div class="col-md-6"> </div>
                <div class="col-md-3 text-end">
                    <a href="" class="btn btn-primary">Add New</a>
                </div>
            </div>

            <!-- Product Table -->
            <table class="table table-bordered text-center">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Product</th>
                        <th>Size</th>
                        <th>Gender</th>
                        <th>Brand</th>
                        <th>Type</th>
                        <th>Price</th>
                        <th>Discount</th>
                        <th>Stock</th>
                        <th>Status</th>
                        <th>Action</th>
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
                                                <img src="${p.image}" alt="${p.pro_name}" class="img-fluid rounded" width="100">
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
                                        <a href="updateProduct.jsp?id=${p.pro_id}" class="btn btn-warning">Update</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <p class="text-muted text-center">${productListMessage}</p>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>

            <!-- Pagination -->
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="productsManagement?page=1">&laquo;</a>
                        </li>
                        <li class="page-item">
                            <a class="page-link" href="productsManagement?page=${currentPage-1}">&lt;</a>
                        </li>
                    </c:if>
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="productsManagement?page=${i}">${i}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="productsManagement?page=${currentPage+1}">&gt;</a>
                        </li>
                        <li class="page-item">
                            <a class="page-link" href="productsManagement?page=${totalPages}">&raquo;</a>
                        </li>
                    </c:if>
                </ul>
            </nav>

        </div>
        <!-- Footer -->
        <jsp:include page="../common/layout/footer.jsp" />

        <script>
            function updateLabel() {
                let filterSelect = document.getElementById("filterFilter");
                if (filterSelect.value === "") {
                    genderSelect.firstElementChild.hidden = false;
                } else {
                    genderSelect.firstElementChild.hidden = true;
                }
            }
        </script>
    </body>
</html>

