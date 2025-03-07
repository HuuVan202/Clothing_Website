<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard - Product Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/admin/managerLayout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/admin/productManagement.css"/>
    </head>
    <body>
        <div class="d-flex h-100">
            <!-- Sidebar -->
            <jsp:include page="../common/layout/managerSidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="flex-grow-1">
                    <!-- Header -->
                <jsp:include page="../common/layout/managerHeader.jsp"></jsp:include>

                    <!-- Content Area -->
                    <div class="content-area">
                        <h2 class="mb-4">
                            <i class="bi bi-display"></i> Product Management
                        </h2>

                        <!-- Success message -->
                    <c:if test="${param.addSuccess == 'true'}">
                        <div class="alert alert-success text-center">Product added successfully!</div>
                    </c:if>
                    <c:if test="${param.updateSuccess == 'true'}">
                        <div class="alert alert-success text-center">Product updated successfully!</div>
                    </c:if>

                    <!-- Filters, Sort & Search -->
                    <div class="row g-2 mb-3">
                        <div class="col-md-2">
                            <select id="typeFilter" class="form-select" onchange="applyFilters()">
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
                            <select id="genderFilter" class="form-select" onchange="applyFilters()">
                                <option value="" selected hidden>Gender</option>
                                <option value="All Gender">All Gender</option>
                                <option value="male">Male</option>
                                <option value="female">Female</option>
                                <option value="unisex">Unisex</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <select id="brandFilter" class="form-select" onchange="applyFilters()">
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
                            <select id="stockFilter" class="form-select" onchange="applyFilters()">
                                <option value="" selected hidden>Stock</option>
                                <option value="All Stock">All Stock</option>
                                <option value="In Stock">In Stock</option>
                                <option value="No Stock">No Stock</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <select id="statusFilter" class="form-select" onchange="applyFilters()">
                                <option value="" selected hidden>Status</option>
                                <option value="All Status">All Status</option>
                                <option value="Active">Active</option>
                                <option value="Inactive">Inactive</option>
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
                            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addProductModal">Add New</button>
                        </div>
                    </div>

                    <!-- Product Table -->
                    <div class="table-responsive">
                        <table class="table table-bordered text-center">
                            <thead class="table-dark">
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
                                                    <div class="d-flex align-items-center">
                                                        <img src="${p.image}" alt="${p.pro_name}" class="product-image" />
                                                        <div class="product-name ms-3">
                                                            <p class="mb-1">${p.pro_name}</p>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="align-middle">${p.type.type_name}</td>
                                                <td class="align-middle">${p.size}</td>
                                                <td class="align-middle">${p.gender}</td>
                                                <td class="align-middle">${p.brand}</td>
                                                <td class="align-middle">${p.formattedPrice} VND</td>
                                                <td class="align-middle">${p.discount}%</td>
                                                <td class="align-middle">${p.stock}</td>
                                                <td class="align-middle">${p.status}</td>
                                                <td class="align-middle">
                                                    <button class="btn btn-primary btn-sm" onclick="openUpdateModal(${p.pro_id})">Update</button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="11" class="text-center">
                                                <div class="alert alert-info mb-0">No product found.</div>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
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
                <div class="modal fade" id="addProductModal">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title">Add New Product</h4>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body">
                                <form id="addForm" action="${pageContext.request.contextPath}/addProduct" method="post" enctype="multipart/form-data" onsubmit="return validateForm(this)">
                                    <div class="mb-3">
                                        <label>Name</label>
                                        <input type="text" class="form-control" name="name" required>
                                    </div>
                                    <div class="mb-3">
                                        <label>Image</label>
                                        <input type="file" class="form-control" name="image" accept="image/*" required>
                                    </div>
                                    <div class="mb-3">
                                        <label>Type</label>
                                        <select class="form-select" id="addProductType" name="type_id" onchange="handleTypeChange(this)" required>
                                            <option value="" selected disabled>Select Type</option>
                                            <c:forEach items="${typeList}" var="type">
                                                <option value="${type.type_id}">${type.type_name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <input type="hidden" name="size" value="">

                                    <div class="mb-3 size-options" style="display: none;">
                                        <label>Size</label><br>
                                        <div class="form-check form-check-inline">
                                            <input type="checkbox" class="form-check-input" name="size" value="S">
                                            <label class="form-check-label">S</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input type="checkbox" class="form-check-input" name="size" value="M">
                                            <label class="form-check-label">M</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input type="checkbox" class="form-check-input" name="size" value="L">
                                            <label class="form-check-label">L</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input type="checkbox" class="form-check-input" name="size" value="XL">
                                            <label class="form-check-label">XL</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input type="checkbox" class="form-check-input" name="size" value="XXL">
                                            <label class="form-check-label">XXL</label>
                                        </div>
                                    </div>

                                    <div class="mb-3 one-size" style="display: none;">
                                        <label>Size</label>
                                        <input type="text" class="form-control" value="One Size" readonly>
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
                                        <select class="form-select" name="brand" required>
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
                                        <input type="number" class="form-control" name="price" value="1" required min="1" max="99999999" oninput="validatePrice(this)">
                                        <div class="invalid-feedback">Price must be between 1 and under 100.000.000</div>
                                    </div>

                                    <div class="mb-3">
                                        <label>Discount (%)</label>
                                        <input type="number" class="form-control" name="discount" value="0" required min="0" max="99" oninput="validateDiscount(this)">
                                        <div class="invalid-feedback">Discount must be between 0 and 99</div>
                                    </div>

                                    <div class="mb-3">
                                        <label>Stock</label>
                                        <input type="number" class="form-control" name="stock" value="1" required min="1" max="99999999" oninput="validateStock(this)">
                                        <div class="invalid-feedback">Stock must be between 1 and under 100.000.000</div>
                                    </div>
                                    <div class="mb-3">
                                        <label>Status</label>
                                        <div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="status" value="active" checked required>
                                                <label class="form-check-label">Active</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="status" value="inactive" required>
                                                <label class="form-check-label">Inactive</label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary">Add Product</button>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
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
                                <h5 class="modal-title">Update Product</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body">
                                <form id="updateForm" action="${pageContext.request.contextPath}/updateProduct" method="post" enctype="multipart/form-data" onsubmit="return validateForm(this)">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="pro_id" value="">
                                    <input type="hidden" name="currentImage" value="">
                                    <input type="hidden" name="size" value="">

                                    <!-- Product ID Display -->
                                    <div class="mb-3">
                                        <label class="fw-bold">Product ID: <span id="displayProductId"></span></label>
                                    </div>

                                    <div class="mb-3">
                                        <label>Name</label>
                                        <input type="text" class="form-control" name="name" required>
                                    </div>

                                    <div class="mb-3">
                                        <label>Image</label>
                                        <div class="d-flex align-items-center mb-2">
                                            <img id="currentProductImage" src="" alt="Current Product" style="width: 100px; height: 100px; object-fit: cover;" class="rounded me-2">
                                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="resetImage()">
                                                <i class="bi bi-arrow-counterclockwise"></i> Reset Image
                                            </button>
                                        </div>
                                        <input type="file" class="form-control" name="image" accept="image/*" onchange="previewImage(this)">
                                        <input type="hidden" name="currentImage" id="currentImagePath">
                                        <small class="text-muted">Leave empty to keep current image</small>
                                    </div>

                                    <div class="mb-3">
                                        <label>Type</label>
                                        <select class="form-select" name="type_id" onchange="handleTypeChange(this)" required>
                                            <c:forEach items="${typeList}" var="type">
                                                <option value="${type.type_id}">${type.type_name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="mb-3 size-options" style="display: none;">
                                        <label>Size</label><br>
                                        <div class="form-check form-check-inline">
                                            <input type="checkbox" class="form-check-input" name="size" value="S">
                                            <label class="form-check-label">S</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input type="checkbox" class="form-check-input" name="size" value="M">
                                            <label class="form-check-label">M</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input type="checkbox" class="form-check-input" name="size" value="L">
                                            <label class="form-check-label">L</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input type="checkbox" class="form-check-input" name="size" value="XL">
                                            <label class="form-check-label">XL</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input type="checkbox" class="form-check-input" name="size" value="XXL">
                                            <label class="form-check-label">XXL</label>
                                        </div>
                                    </div>

                                    <div class="mb-3 one-size" style="display: none;">
                                        <label>Size</label>
                                        <input type="text" class="form-control" value="One Size" readonly>
                                    </div>

                                    <!-- Rest of the form fields -->
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
                                        <select class="form-select" name="brand" required>
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
                                        <label>Price</label>
                                        <input type="number" class="form-control" name="price" required min="1" max="99999999" oninput="validatePrice(this)">
                                        <div class="invalid-feedback">Price must be between 1 and under 100.000.000</div>
                                    </div>

                                    <div class="mb-3">
                                        <label>Discount (%)</label>
                                        <input type="number" class="form-control" name="discount" required min="0" max="99" oninput="validateDiscount(this)">
                                        <div class="invalid-feedback">Discount must be between 0 and 99</div>
                                    </div>

                                    <div class="mb-3">
                                        <label>Stock</label>
                                        <input type="number" class="form-control" name="stock" required min="1" max="99999999" oninput="validateStock(this)">
                                        <div class="invalid-feedback">Stock must be between 1 and under 100.000.000</div>
                                    </div>

                                    <div class="mb-3">
                                        <label>Status</label>
                                        <div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="updateProductStatus" value="active" required>
                                                <label class="form-check-label">Active</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="updateProductStatus" value="inactive" required>
                                                <label class="form-check-label">Inactive</label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                        <button type="submit" class="btn btn-primary">Update</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            // Store data in JavaScript
            const products = [
            <c:forEach items="${productList}" var="p" varStatus="status">
            {
            pro_id: ${p.pro_id},
                    pro_name: "${p.pro_name}",
                    pro_image: "${p.image}",
                    type: {
                    type_id: ${p.type.type_id},
                            type_name: "${p.type.type_name}"
                    },
                    size: "${p.size}",
                    pro_gender: "${p.gender}",
                    pro_brand: "${p.brand}",
                    pro_price: ${p.price},
                    pro_discount: ${p.discount},
                    pro_stock: ${p.stock},
                    status: "${p.status}"
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];
        </script>
        <script src="${pageContext.request.contextPath}/JS/admin/productManagement.js"></script>
    </body>
</html>