<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
    Document   : productDetails
    Created on : Feb 17, 2025, 10:06:23 AM
    Author     : Vu_Hoang
--%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Home</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/common/layout/layout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/home.css"/>
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
            crossorigin="anonymous"
            />
        <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"
        ></script>
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="../common/layout/header.jsp" />
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success" style="
                 padding: 10px;
                 background-color: #4caf50;
                 color: white;
                 text-align: center;
                 border-radius: 5px;
                 margin: 15px auto;
                 width: 300px;
                 display: block;
                 font-weight: bold;">
                ${sessionScope.successMessage}
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>

        <div class="container mt-5">
            <h1 class="text-center">Product Details</h1>

            <!-- PHẦN 1: Chi tiết sản phẩm -->
            <div class="row justify-content-center">
                <div class="col-md-10 border p-4 mb-5">
                    <c:choose>
                        <c:when test="${not empty productDetails}">
                            <c:set var="pd" value="${productDetails}" />
                            <div class="row">
                                <div class="col-md-6 text-center">
                                    <img src="${pd.image}" class="img-fluid" alt="${pd.pro_name}" />
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Product ID:</strong> ${pd.pro_id}</p>
                                    <p><strong>Name:</strong> ${pd.pro_name}</p>
                                    <p><strong>Size:</strong> ${pd.size}</p>
                                    <p><strong>Type:</strong> ${pd.type.type_name}</p>
                                    <p><strong>Stock:</strong> ${pd.stock}</p>
                                    <p><strong>Price:</strong> 
                                        <c:if test="${pd.discount > 0}">
                                            <span class="original-price text-decoration-line-through">${pd.formattedPrice} VND</span>
                                            <span class="discount-price text-danger">${pd.formattedDiscountedPrice} VND</span>
                                            <!-- Nút hiển thị % giảm giá -->
                                            <span class="badge bg-danger ms-2">-${pd.discount}%</span>
                                        </c:if>
                                        <c:if test="${pd.discount == 0}">
                                            <span class="discount-price">${pd.formattedPrice} VND</span>
                                        </c:if>
                                    </p>
                                    <p><strong>Rating:</strong> ${pd.averageRating} <span class="text-warning fs-5">★</span> (${pd.feedbackCount})</p>

                                    <p>
                                    <form action="Cart" method="post">
                                        <input type="hidden" name="pro_id" value="${pd.pro_id}" />
                                        <input type="hidden" name="action" value="add" />
                                        <button type="submit" class="btn btn-success mt-auto">Add to Cart</button>
                                    </form>
                                    </p>
                                    <button class="btn btn-primary">Give Feedback</button>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p class="text-danger text-center">${productDetailsMessage}</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- PHẦN 2: Feedback của khách hàng -->
            <div class="container rounded p3 mt-5 product-wrapper">
                <h3 class="mt-5">Feedbacks:</h3>
            </div>
            <div>
                <div class="container d-flex justify-content-center">
                    <div class="col-md-5 border rounded p-4" style="max-height: 200px; overflow-y: auto;">
                        <c:choose>
                            <c:when test="${not empty feedbackOfProduct}">
                                <c:forEach var="f" items="${feedbackOfProduct}">
                                    <div class="border-bottom pb-2 mb-2">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <!-- Tên khách hàng (góc trái) -->
                                            <span class="fw-bold">${f.cus_name}</span>

                                            <!-- Số sao đánh giá (ở giữa) -->
                                            <span class="text-warning">
                                                <c:forEach begin="1" end="${f.rating}">★</c:forEach>
                                                <c:forEach begin="${f.rating + 1}" end="5">☆</c:forEach>
                                                </span>

                                                <!-- Ngày feedback (góc phải) -->
                                                <span class="text-muted small">${f.feedback_date}</span>
                                        </div>

                                        <!-- Nội dung feedback (nửa dưới) -->
                                        <p class="mt-1 text-dark">${f.comment}</p>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted text-center">${feedbackOfProductMessage}</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- PHẦN 3: Sản phẩm gợi ý -->
            <div class="container rounded p3 mt-5 product-wrapper">
                <h3 class="mt-5">Suggestions:</h3>
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5 g-3">
                    <c:choose>
                        <c:when test="${not empty suggestProducts}">
                            <c:forEach items="${suggestProducts}" var="s">

                                <div class="col">
                                    <div class="border rounded p-3 text-center h-100 d-flex flex-column justify-content-between position-relative">
                                        <!-- Hiển thị phần trăm giảm giá -->
                                        <c:if test="${s.discount > 0}">
                                            <div class="position-absolute top-0 start-0 bg-danger text-white px-2 py-1 rounded">
                                                -${s.discount}%
                                            </div>
                                        </c:if>
                                        <a class="text-decoration-none text-dark" href="detail?id=${s.pro_id}">
                                            <img src="${s.image}" class="img-fluid" alt="${s.pro_name}" />
                                        </a>
                                        <a class="text-decoration-none text-dark" href="detail?id=${s.pro_id}">
                                            <h5 class="card-title">${s.pro_name}</h5>
                                        </a>
                                        <div>
                                            <c:if test="${s.discount > 0}">
                                                <span class="original-price text-decoration-line-through">${s.formattedPrice} VND</span>
                                                <span class="discount-price text-danger">${s.formattedDiscountedPrice} VND</span>
                                            </c:if>
                                            <c:if test="${s.discount == 0}">
                                                <div class="discount-price">${s.formattedPrice} VND</div>
                                            </c:if>
                                        </div>
                                        <div>
                                            <form action="Cart" method="post">
                                                <input type="hidden" name="pro_id" value="${s.pro_id}" />
                                                <input type="hidden" name="action" value="add" />
                                                <button type="submit" class="btn btn-success mt-auto">Add to Cart</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted text-center">${suggestProductsMessage}</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Footer -->
            <jsp:include page="../common/layout/footer.jsp" />
    </body>
</html>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
