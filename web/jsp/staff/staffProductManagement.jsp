<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard - Product Management for Staff</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/staff/assistantLayout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/staff/staffProductManagement.css"/>
    </head>
    <body>
        <div class="d-flex h-100">
            <!-- Sidebar -->
            <jsp:include page="../common/layout/staffSidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="flex-grow-1">
                    <!-- Header -->
                <jsp:include page="../common/layout/staffHeader.jsp"></jsp:include>

                    <!-- Content Area -->
                    <div class="content-area">
                        <h2 class="mb-4">
                            <i class="bi bi-box-seam"></i> Product Management
                        </h2>

                    <!-- Filters, Sort & Search -->
                    <div class="row g-2 mb-3">
                        <div class="col-md-2">
                            <select id="typeFilter" class="form-select" onchange="applyFilters()">
                                <option value="">Type</option>
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
                            <select id="genderFilter" class="form-select" onchange="applyFilters()">
                                <option value="">Gender</option>
                                <option value="male">Male</option>
                                <option value="female">Female</option>
                                <option value="unisex">Unisex</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <select id="brandFilter" class="form-select" onchange="applyFilters()">
                                <option value="">Brand</option>
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
                            <select id="stockFilter" class="form-select" onchange="applyFilters()">
                                <option value="">Stock</option>
                                <option value="In Stock">In Stock</option>
                                <option value="No Stock">Out of Stock</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <select id="statusFilter" class="form-select" onchange="applyFilters()">
                                <option value="">Status</option>
                                <option value="Active">Active</option>
                                <option value="Inactive">Inactive</option>
                                <option value="Pendind">Pending</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <select id="sortFilter" class="form-select" onchange="applyFilters()">
                                <option value="" ${empty param.sortBy ? 'selected' : ''} hidden>Sort By</option>
                                <option value="price_asc" ${param.sortBy == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                                <option value="price_desc" ${param.sortBy == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                                <option value="id_desc" ${param.sortBy == 'id_desc' ? 'selected' : ''}>Newest First</option>
                                <option value="id_asc" ${param.sortBy == 'id_asc' ? 'selected' : ''}>Oldest First</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="search" class="form-control" placeholder="Search">
                        </div>
                        <div class="col-md-9 text-end">
                            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#requestAddProductModal">Request Add New</button>
                        </div>
                    </div>

                    <!-- Product Table -->
                    <div class="table-responsive text-center">
                        <table class="table table-hover table-bordered">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Product</th>
                                    <th>Type</th>
                                    <th>Size</th>
                                    <th>Gender</th>
                                    <th>Brand</th>
                                    <th>Price</th>
                                    <th>Discount</th>
                                    <th>Stock</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty productList}">
                                    <tr>
                                        <td colspan="11" class="text-center">No products found</td>
                                    </tr>
                                </c:if>
                                <c:if test="${not empty productList}">
                                    <c:forEach items="${productList}" var="p">
                                        <tr>
                                            <td class="align-middle">${p.pro_id}</td>
                                            <td class="align-middle">
                                                <div class="d-flex align-items-center">
                                                    <img src="${p.image}" alt="${p.pro_name}" class="product-image" />
                                                    <div class="product-name ms-3">
                                                        <p class="product-name" title="${p.pro_name}">${p.pro_name}</p>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="align-middle">${p.type.type_name}</td>
                                            <td class="align-middle">
                                                <c:choose>
                                                    <c:when test="${not empty p.productSizes}">
                                                        <c:set var="hasStock" value="false" />
                                                        <c:set var="sizesWithStock" value="" />
                                                        <c:forEach items="${p.productSizes}" var="size">
                                                            <c:if test="${size.stock > 0}">
                                                                <c:set var="hasStock" value="true" />
                                                                <c:set var="sizesWithStock" value="${sizesWithStock}${not empty sizesWithStock ? ', ' : ''}${size.size}" />
                                                            </c:if>
                                                        </c:forEach>
                                                        <c:choose>
                                                            <c:when test="${hasStock}">
                                                                ${sizesWithStock}
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-danger">None</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-danger">None</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="align-middle">${p.gender}</td>
                                            <td class="align-middle">${p.brand}</td>
                                            <td class="align-middle">${p.formattedPrice} VND</td>
                                            <td class="align-middle">${p.discount}%</td>
                                            <td class="align-middle">
                                                <c:choose>
                                                    <c:when test="${not empty p.productSizes}">
                                                        <c:set var="totalStock" value="0" />
                                                        <c:forEach items="${p.productSizes}" var="size">
                                                            <c:set var="totalStock" value="${totalStock + size.stock}" />
                                                        </c:forEach>
                                                        ${totalStock}
                                                    </c:when>
                                                    <c:otherwise>
                                                        0
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="align-middle">
                                                <c:choose>
                                                    <c:when test="${p.status eq 'active'}">
                                                        <span class="text-success fw-bold text-capitalize">${p.status}</span>
                                                    </c:when>
                                                    <c:when test="${p.status eq 'inactive'}">
                                                        <span class="text-danger fw-bold text-capitalize">${p.status}</span>
                                                    </c:when>
                                                    <c:when test="${p.status eq 'pending'}">
                                                        <span class="text-warning fw-bold text-capitalize">${p.status}</span>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td class="align-middle">
                                                <c:choose>
                                                    <c:when test="${p.status eq 'inactive'}">
                                                        <button class="btn btn-secondary btn-sm" type="button" disabled>Update</button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button class="btn btn-primary btn-sm" onclick="openUpdateModal(${p.pro_id})" type="button">Update</button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <!-- Pagination -->
                    <c:if test="${not empty productList}">
                        <div class="d-flex justify-content-center mt-4">
                            <nav>
                                <ul class="pagination">
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="javascript:void(0)" onclick="goToPage(${currentPage == 1})">&laquo;</a>
                                        </li>
                                    </c:if>
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="javascript:void(0)" onclick="goToPage(${currentPage - 1})">&lt;</a>
                                        </li>
                                    </c:if>

                                    <c:set var="startPage" value="${currentPage <= 5 ? 1 : currentPage - 5}" />
                                    <c:set var="endPage" value="${startPage + 9}" />
                                    <c:if test="${endPage > totalPages}">
                                        <c:set var="endPage" value="${totalPages}" />
                                        <c:set var="startPage" value="${totalPages - 9 > 1 ? totalPages - 9 : 1}" />
                                    </c:if>

                                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                            <a class="page-link" href="javascript:void(0)" onclick="goToPage(${i})">${i}</a>
                                        </li>
                                    </c:forEach>

                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link" href="javascript:void(0)" onclick="goToPage(${currentPage + 1})">&gt;</a>
                                        </li>
                                    </c:if>

                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link" href="javascript:void(0)" onclick="goToPage(${totalPages})">&raquo;</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
                        </div>
                    </c:if>
                </div>


                <!-- Modal Add New Product -->
                <div class="modal fade" id="requestAddProductModal">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title">Request Add New Product</h4>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body">
                                <form id="addForm" action="${pageContext.request.contextPath}/addProductByStaff" method="post" enctype="multipart/form-data">
                                    <div class="mb-3">
                                        <label>Name</label>
                                        <input type="text" class="form-control" name="name" required maxlength="255">
                                    </div>
                                    <div class="mb-3">
                                        <label>Image</label>
                                        <input type="file" class="form-control" name="image" accept="image/*" onchange="previewImage(this)" required>
                                        <div class="d-flex justify-content-center mt-2">
                                            <img id="currentProductImage" src="" alt="Product Image" style="max-width: 200px; margin-top: 10px; border: 2px solid #ccc;">
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <label>Type</label>
                                        <select class="form-select" name="type_id" onchange="handleTypeChange(this)" required>
                                            <option value="" selected hidden>Select Type</option>
                                            <option value="1">Shirt</option>
                                            <option value="2">T-Shirt</option>
                                            <option value="3">Jacket</option>
                                            <option value="4">Pants</option>
                                            <option value="5">Shorts</option>
                                            <option value="6">Sunglasses</option>
                                            <option value="7">Wallet</option>
                                            <option value="8">Bag</option>
                                            <option value="9">Hat</option>
                                        </select>
                                    </div>

                                    <!-- Size Options for Clothing -->
                                    <div class="mb-3 size-options" style="display: none;">
                                        <div class="size-stock-grid">
                                            <div class="size-stock-item">
                                                <div class="form-check">
                                                    <input type="checkbox" class="form-check-input" name="size_S" value="S" id="sizeS">
                                                    <label class="form-check-label" for="sizeS">S</label>
                                                </div>
                                                <input type="number" class="form-control form-control-sm" name="stock_S" value="0" min="0" max="100000000" required>
                                            </div>
                                            <div class="size-stock-item">
                                                <div class="form-check">
                                                    <input type="checkbox" class="form-check-input" name="size_M" value="M" id="sizeM">
                                                    <label class="form-check-label" for="sizeM">M</label>
                                                </div>
                                                <input type="number" class="form-control form-control-sm" name="stock_M" value="0" min="0" max="100000000" required>
                                            </div>
                                            <div class="size-stock-item">
                                                <div class="form-check">
                                                    <input type="checkbox" class="form-check-input" name="size_L" value="L" id="sizeL">
                                                    <label class="form-check-label" for="sizeL">L</label>
                                                </div>
                                                <input type="number" class="form-control form-control-sm" name="stock_L" value="0" min="0" max="100000000" required>
                                            </div>
                                            <div class="size-stock-item">
                                                <div class="form-check">
                                                    <input type="checkbox" class="form-check-input" name="size_XL" value="XL" id="sizeXL">
                                                    <label class="form-check-label" for="sizeXL">XL</label>
                                                </div>
                                                <input type="number" class="form-control form-control-sm" name="stock_XL" value="0" min="0" max="100000000" required>
                                            </div>
                                            <div class="size-stock-item">
                                                <div class="form-check">
                                                    <input type="checkbox" class="form-check-input" name="size_XXL" value="XXL" id="sizeXXL">
                                                    <label class="form-check-label" for="sizeXXL">XXL</label>
                                                </div>
                                                <input type="number" class="form-control form-control-sm" name="stock_XXL" value="0" min="0" max="100000000" required>
                                            </div>
                                        </div>
                                        <div class="mt-2" style="font-weight: bold;">
                                            <span>Total Stock:</span>
                                            <span class="total-stock"></span>
                                        </div>
                                    </div>

                                    <!-- One Size Option for Accessories -->
                                    <div class="mb-3 one-size-option" style="display: none;">
                                        <label>Stock</label>
                                        <input type="number" class="form-control" name="stock_one_size" value="0" min="0" max="600000000" required>
                                    </div>

                                    <div class="mb-3">
                                        <label>Gender</label>
                                        <select class="form-select" name="gender" required>
                                            <option value="male">Male</option>
                                            <option value="female">Female</option>
                                            <option value="unisex">Unisex</option>
                                        </select>
                                    </div>
                                    <div class="mb-3">
                                        <label>Brand</label>
                                        <select class="form-select" id="brand" name="brand" required>
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
                                    <div class="mb-3">
                                        <label>Price (VND)</label>
                                        <input type="number" class="form-control" name="price" value="1" min="1" max="99999999" required>
                                    </div>
                                    <div class="mb-3">
                                        <label>Discount (%)</label>
                                        <input type="number" class="form-control" name="discount" value="0" min="0" max="99" required>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                        <button type="submit" class="btn btn-primary">Add Product</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Modal Update Product -->
                <div class="modal fade" id="updateProductModal">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Update Product #<span id="displayProductId"></span></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body">
                                <form id="updateForm" action="${pageContext.request.contextPath}/updateProductByStaff" method="post" enctype="multipart/form-data">
                                    <input type="hidden" id="pro_id" name="pro_id">
                                    <div class="mb-3">
                                        <label for="update_type">Type</label>
                                        <select class="form-select" id="update_type" name="type_id" onchange="handleTypeChange(this)" required>
                                            <option value="1">Shirt</option>
                                            <option value="2">T-Shirt</option>
                                            <option value="3">Jacket</option>
                                            <option value="4">Pants</option>
                                            <option value="5">Shorts</option>
                                            <option value="6">Sunglasses</option>
                                            <option value="7">Wallet</option>
                                            <option value="8">Bag</option>
                                            <option value="9">Hat</option>
                                        </select>
                                    </div>

                                    <!-- Size Options for Clothing -->
                                    <div class="mb-3 size-options">
                                        <div class="size-stock-grid">
                                            <div class="size-stock-item">
                                                <div class="form-check">
                                                    <input type="checkbox" class="form-check-input" id="update_size_S" name="size_S" value="S">
                                                    <label class="form-check-label" for="update_size_S">S</label>
                                                </div>
                                                <input type="number" class="form-control form-control-sm" id="update_stock_S" name="stock_S" value="0" min="0" max="100000000" required>
                                            </div>
                                            <div class="size-stock-item">
                                                <div class="form-check">
                                                    <input type="checkbox" class="form-check-input" id="update_size_M" name="size_M" value="M">
                                                    <label class="form-check-label" for="update_size_M">M</label>
                                                </div>
                                                <input type="number" class="form-control form-control-sm" id="update_stock_M" name="stock_M" value="0" min="0" max="100000000" required>
                                            </div>
                                            <div class="size-stock-item">
                                                <div class="form-check">
                                                    <input type="checkbox" class="form-check-input" id="update_size_L" name="size_L" value="L">
                                                    <label class="form-check-label" for="update_size_L">L</label>
                                                </div>
                                                <input type="number" class="form-control form-control-sm" id="update_stock_L" name="stock_L" value="0" min="0" max="100000000" required>
                                            </div>
                                            <div class="size-stock-item">
                                                <div class="form-check">
                                                    <input type="checkbox" class="form-check-input" id="update_size_XL" name="size_XL" value="XL">
                                                    <label class="form-check-label" for="update_size_XL">XL</label>
                                                </div>
                                                <input type="number" class="form-control form-control-sm" id="update_stock_XL" name="stock_XL" value="0" min="0" max="100000000" required>
                                            </div>
                                            <div class="size-stock-item">
                                                <div class="form-check">
                                                    <input type="checkbox" class="form-check-input" id="update_size_XXL" name="size_XXL" value="XXL">
                                                    <label class="form-check-label" for="update_size_XXL">XXL</label>
                                                </div>
                                                <input type="number" class="form-control form-control-sm" id="update_stock_XXL" name="stock_XXL" value="0" min="0" max="100000000" required>
                                            </div>
                                        </div>
                                        <div class="mt-2" style="font-weight: bold;">
                                            <span>Total Stock:</span>
                                            <span class="total-stock"></span>
                                        </div>
                                    </div>

                                    <!-- One Size Option for Accessories -->
                                    <div class="mb-3 one-size-option">
                                        <label for="update_stock_one_size">Stock</label>
                                        <input type="number" class="form-control" id="update_stock_one_size" name="stock_one_size" value="0" min="0" max="600000000" required>
                                    </div>

                                    <div class="mb-3">
                                        <label for="update_price">Price (VND)</label>
                                        <input type="number" class="form-control" id="update_price" name="price" value="1" min="1" max="99999999" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="update_discount">Discount (%)</label>
                                        <input type="number" class="form-control" id="update_discount" name="discount" min="0" max="99" value="0" required>
                                    </div>
                                    <div class="mb-3 modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                        <div id="deleteButtonContainer">
                                            <button type="button" class="btn btn-danger me-2" onclick="deletePendingProduct()">Delete Product</button>
                                        </div>
                                        <button type="submit" class="btn btn-primary">Update Product</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                // Initialize products array with proper JSON format
                const products = ${productListJson};
            </script>

            <!-- Include JavaScript file -->
            <script src="${pageContext.request.contextPath}/JS/staff/staffProductManagement.js"></script>
    </body>
</html>

