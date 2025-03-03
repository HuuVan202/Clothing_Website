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
                        <i class="bi bi-display"></i> Welcome to Dashboard
                    </h2>
                        <div class="container-fluid">
                            <h2 class="text-center mb-4">Product Management</h2>

                            <!-- Success message -->
                        <c:if test="${param.updateSuccess == 'true'}">
                            <div class="alert alert-success text-center">Product updated successfully!</div>
                        </c:if>

                        <!-- Search & Filters -->
                        <div class="row g-2 mb-3">
                            <div class="col-md-2">
                                <select id="genderFilter" class="form-select" onchange="updateLabel()">
                                    <option value="" selected hidden>Gender</option>
                                    <option value="male">Male</option>
                                    <option value="female">Female</option>
                                    <option value="unisex">Unisex</option>
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
                                <select id="typeFilter" class="form-select" onchange="updateLabel()">
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
                                <select id="inStockFilter" class="form-select" onchange="updateLabel()">
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
                                                        <div class="product-cell">
                                                            <img src="${p.image}" alt="${p.pro_name}" class="product-image">
                                                            <div class="product-name">${p.pro_name}</div>
                                                        </div>
                                                    </td>
                                                    <td class="align-middle">${p.type.type_name}</td>
                                                    <td class="align-middle">${p.size}</td>
                                                    <td class="align-middle">${p.gender}</td>
                                                    <td class="align-middle">${p.brand}</td>
                                                    <td class="align-middle">${p.formattedPrice}</td>
                                                    <td class="align-middle">${p.discount}%</td>
                                                    <td class="align-middle">${p.stock}</td>
                                                    <td class="align-middle">${p.status}</td>
                                                    <td class="align-middle">
                                                        <button class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#updateProductModal${p.pro_id}">Update</button>
                                                    </td>
                                                </tr>

                                                <!-- Update Product Modal -->
                                            <div class="modal" id="updateProductModal${p.pro_id}">
                                                <div class="modal-dialog">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <div class="product-id-header">
                                                                <h4 class="modal-title">Update Product</h4>
                                                                <h5 class="product-id-value">(ID: ${p.pro_id})</h5>
                                                            </div>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <form action="${pageContext.request.contextPath}/updateProduct" method="post" enctype="multipart/form-data">
                                                                <input type="hidden" name="pro_id" value="${p.pro_id}">
                                                                <div class="mb-3">
                                                                    <label>Name</label>
                                                                    <input type="text" class="form-control" name="name" value="${p.pro_name}" required>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label>Current Image</label>
                                                                    <img src="${p.image}" alt="${p.pro_name}" class="img-fluid rounded mb-2">
                                                                    <input type="file" class="form-control" name="image">
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label>Type</label>
                                                                    <select class="form-select type-select" name="type_id" required onchange="updateSizeOptions(this)">
                                                                        <c:forEach var="type" items="${typeList}">
                                                                            <option value="${type.type_id}" ${type.type_id == p.type.type_id ? 'selected' : ''}>${type.type_name}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label>Size</label>
                                                                    <div class="size-options">
                                                                        <div class="clothing-sizes">
                                                                            <label class="me-2">
                                                                                <input type="checkbox" name="size" value="S" ${fn:contains(p.size, 'S') ? 'checked' : ''}> S
                                                                            </label>
                                                                            <label class="me-2">
                                                                                <input type="checkbox" name="size" value="M" ${fn:contains(p.size, 'M') ? 'checked' : ''}> M
                                                                            </label>
                                                                            <label class="me-2">
                                                                                <input type="checkbox" name="size" value="L" ${fn:contains(p.size, 'L') ? 'checked' : ''}> L
                                                                            </label>
                                                                            <label class="me-2">
                                                                                <input type="checkbox" name="size" value="XL" ${fn:contains(p.size, 'XL') ? 'checked' : ''}> XL
                                                                            </label>
                                                                            <label class="me-2">
                                                                                <input type="checkbox" name="size" value="XXL" ${fn:contains(p.size, 'XXL') ? 'checked' : ''}> XXL
                                                                            </label>
                                                                        </div>
                                                                        <div class="accessory-size">
                                                                            <input type="text" class="form-control" name="size" value="One Size" readonly>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label>Gender</label>
                                                                    <select class="form-select" name="gender" required>
                                                                        <option value="male" ${p.gender eq 'male' ? 'selected' : ''}>male</option>
                                                                        <option value="female" ${p.gender eq 'female' ? 'selected' : ''}>female</option>
                                                                        <option value="unisex" ${p.gender eq 'unisex' ? 'selected' : ''}>unisex</option>
                                                                    </select>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label>Brand</label>
                                                                    <select class="form-select" name="brand" required>
                                                                        <option value="Adidas" ${p.brand == 'Adidas' ? 'selected' : ''}>Adidas</option>
                                                                        <option value="Calvin Klein" ${p.brand == 'Calvin Klein' ? 'selected' : ''}>Calvin Klein</option>
                                                                        <option value="Lacoste" ${p.brand == 'Lacoste' ? 'selected' : ''}>Lacoste</option>
                                                                        <option value="MLB" ${p.brand == 'MLB' ? 'selected' : ''}>MLB</option>
                                                                        <option value="New Era" ${p.brand == 'New Era' ? 'selected' : ''}>New Era</option>
                                                                        <option value="Nike" ${p.brand == 'Nike' ? 'selected' : ''}>Nike</option>
                                                                        <option value="Puma" ${p.brand == 'Puma' ? 'selected' : ''}>Puma</option>
                                                                        <option value="Tommy Hilfiger" ${p.brand == 'Tommy Hilfiger' ? 'selected' : ''}>Tommy Hilfiger</option>
                                                                    </select>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label>Price</label>
                                                                    <input type="number" class="form-control number-input" name="price" value="${p.price}" min="0" required>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label>Discount (%)</label>
                                                                    <input type="number" class="form-control number-input" name="discount" value="${p.discount}" min="0" max="100" required>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label>Stock</label>
                                                                    <input type="number" class="form-control number-input" name="stock" value="${p.stock}" min="0" required>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label>Status</label>
                                                                    <div>
                                                                        <label class="status-radio">
                                                                            <input type="radio" name="status" value="active" ${p.status eq 'active' ? 'checked' : ''} required>
                                                                            active
                                                                        </label>
                                                                        <label class="status-radio">
                                                                            <input type="radio" name="status" value="inactive" ${p.status eq 'inactive' ? 'checked' : ''} required>
                                                                            inactive
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="submit" class="btn btn-primary">Update</button>
                                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                                </div>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
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
                        </div>

                        <!-- Pagination -->
                        <nav aria-label="Page navigation">
                            <ul class="pagination justify-content-center">
                                <c:if test="${currentPage > 1}">
                                    <li class="page-item">
                                        <a class="page-link" href="productM?page=1">&laquo;</a>
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="productM?page=${currentPage-1}">&lt;</a>
                                    </li>
                                </c:if>
                                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="productM?page=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                                <c:if test="${currentPage < totalPages}">
                                    <li class="page-item">
                                        <a class="page-link" href="productM?page=${currentPage+1}">&gt;</a>
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="productM?page=${totalPages}">&raquo;</a>
                                    </li>
                                </c:if>
                            </ul>
                        </nav>
                    </div>


                    <!-- Modal Add New Product -->
                    <div class="modal" id="addProductModal">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4 class="modal-title">Add New Product</h4>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <form action="${pageContext.request.contextPath}/addProduct" method="post" enctype="multipart/form-data">
                                        <div class="mb-3">
                                            <label>Name</label>
                                            <input type="text" class="form-control" name="name" required>
                                        </div>
                                        <div class="mb-3">
                                            <label>Image</label>
                                            <input type="file" class="form-control" name="image" required>
                                        </div>
                                        <div class="mb-3">
                                            <label>Type</label>
                                            <select class="form-select type-select" name="type_id" required onchange="updateSizeOptions(this)">
                                                <c:forEach var="type" items="${typeList}">
                                                    <option value="${type.type_id}">${type.type_name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="mb-3">
                                            <label>Size</label>
                                            <div class="size-options">
                                                <div class="clothing-sizes">
                                                    <label class="me-2"><input type="checkbox" name="size" value="S"> S</label>
                                                    <label class="me-2"><input type="checkbox" name="size" value="M"> M</label>
                                                    <label class="me-2"><input type="checkbox" name="size" value="L"> L</label>
                                                    <label class="me-2"><input type="checkbox" name="size" value="XL"> XL</label>
                                                    <label class="me-2"><input type="checkbox" name="size" value="XXL"> XXL</label>
                                                </div>
                                                <div class="accessory-size">
                                                    <input type="text" class="form-control" name="size" value="One Size" readonly>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="mb-3">
                                            <label>Gender</label>
                                            <select class="form-select" name="gender" required>
                                                <option value="male">male</option>
                                                <option value="female">female</option>
                                                <option value="unisex">unisex</option>
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
                                            <input type="number" class="form-control number-input" name="price" value="0" min="0" required>
                                        </div>
                                        <div class="mb-3">
                                            <label>Discount (%)</label>
                                            <input type="number" class="form-control number-input" name="discount" value="0" min="0" max="100" required>
                                        </div>
                                        <div class="mb-3">
                                            <label>Stock</label>
                                            <input type="number" class="form-control number-input" name="stock" value="0" min="0" required>
                                        </div>
                                        <div class="mb-3">
                                            <label>Status</label>
                                            <div>
                                                <label class="status-radio">
                                                    <input type="radio" name="status" value="active" checked required>
                                                    active
                                                </label>
                                                <label class="status-radio">
                                                    <input type="radio" name="status" value="inactive" required>
                                                    inactive
                                                </label>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="submit" class="btn btn-primary">Add Product</button>
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                        </div>
                                    </form>
                                </div>


                                <script>
                                    function updateSizeOptions(selectElement) {
                                        const typeId = parseInt(selectElement.value);
                                        const modal = selectElement.closest('.modal-content');
                                        const clothingSizes = modal.querySelector('.clothing-sizes');
                                        const accessorySize = modal.querySelector('.accessory-size');

                                        if (typeId >= 1 && typeId <= 5) {
                                            clothingSizes.style.display = 'flex';
                                            accessorySize.style.display = 'none';
                                            // Enable all checkboxes in clothing sizes
                                            clothingSizes.querySelectorAll('input[type="checkbox"]').forEach(cb => {
                                                cb.disabled = false;
                                            });
                                            // Disable the One Size input
                                            accessorySize.querySelector('input').disabled = true;
                                        } else {
                                            clothingSizes.style.display = 'none';
                                            accessorySize.style.display = 'block';
                                            // Disable all checkboxes in clothing sizes
                                            clothingSizes.querySelectorAll('input[type="checkbox"]').forEach(cb => {
                                                cb.disabled = true;
                                                cb.checked = false;
                                            });
                                            // Enable the One Size input
                                            accessorySize.querySelector('input').disabled = false;
                                        }
                                    }

                                    // Initialize size options for all modals when the page loads
                                    document.addEventListener('DOMContentLoaded', function () {
                                        document.querySelectorAll('.type-select').forEach(select => {
                                            updateSizeOptions(select);
                                        });
                                    });
                                </script>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>