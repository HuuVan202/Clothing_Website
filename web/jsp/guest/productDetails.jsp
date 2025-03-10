<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Product Details</title>

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/common/layout/layout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/home.css"/>
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="../common/layout/header.jsp" />

        <!-- Product Details Section -->
        <div class="container my-5">
            <h1 class="text-center mb-4">Product Details</h1>

            <div class="row justify-content-center">
                <div class="col-lg-10 col-md-12 border p-4 rounded shadow-sm bg-light">
                    <c:choose>
                        <c:when test="${not empty productDetails}">
                            <c:set var="pd" value="${productDetails}" />
                            <div class="row align-items-center">
                                <!-- Product Image -->
                                <div class="col-md-6 text-center">
                                    <img src="${pd.image}" class="img-fluid rounded" alt="${pd.pro_name}">
                                </div>
                                <!-- Product Info -->
                                <div class="col-md-6">
                                    <p><strong>Product ID:</strong> ${pd.pro_id}</p>
                                    <p><strong>Name:</strong> ${pd.pro_name}</p>
                                    <p><strong>Size:</strong> ${pd.size}</p>
                                    <p><strong>Type:</strong> ${pd.type.type_name}</p>
                                    <p><strong>Stock:</strong> ${pd.stock}</p>
                                    <p><strong>Price:</strong> 
                                        <c:if test="${pd.discount > 0}">
                                            <span class="text-decoration-line-through text-muted">${pd.formattedPrice} VND</span>
                                            <span class="text-danger fw-bold">${pd.formattedDiscountedPrice} VND</span>
                                            <span class="badge bg-danger ms-2">-${pd.discount}%</span>
                                        </c:if>
                                        <c:if test="${pd.discount == 0}">
                                            <span class="fw-bold">${pd.formattedPrice} VND</span>
                                        </c:if>
                                    </p>
                                    <p><strong>Rating:</strong> ${pd.averageRating} 
                                        <span class="text-warning fs-5">★</span> (${pd.feedbackCount})
                                    </p>

                                    <form action="Cart" method="post">
                                        <input type="hidden" name="pro_id" value="${pd.pro_id}" />
                                        <input type="hidden" name="action" value="add" />
                                        <button type="submit" class="btn btn-success mt-auto">Add to Cart</button>
                                    </form>
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
        </div>

        <!-- Feedback Section -->
        <div class="container my-5">
            <h3>Feedbacks:</h3>
            <div class="d-flex justify-content-center">
                <div class="col-lg-6 col-md-8 col-sm-12 border rounded p-4 bg-light shadow-sm" style="max-height: 250px; overflow-y: auto;">
                    <c:choose>
                        <c:when test="${not empty feedbackOfProduct}">
                            <c:forEach var="f" items="${feedbackOfProduct}">
                                <div class="border-bottom pb-2 mb-2">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <span class="fw-bold">${f.cus_name}</span>
                                        <span class="text-warning">
                                            <c:forEach begin="1" end="${f.rating}">★</c:forEach>
                                            <c:forEach begin="${f.rating + 1}" end="5">☆</c:forEach>
                                            </span>
                                            <span class="text-muted small">${f.feedback_date}</span>
                                    </div>
                                    <p class="mt-1">${f.comment}</p>
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

        <!-- Suggested Products Section -->
        <div class="container my-5">
            <h3>Suggestions:</h3>
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5 g-3">
                <c:choose>
                    <c:when test="${not empty suggestProducts}">
                        <c:forEach items="${suggestProducts}" var="s">
                            <div class="col">
                                <div class="border rounded p-3 text-center h-100 d-flex flex-column justify-content-between position-relative bg-light shadow-sm">
                                    <!-- Discount Badge -->
                                    <c:if test="${s.discount > 0}">
                                        <div class="position-absolute top-0 start-0 bg-danger text-white px-2 py-1 rounded">
                                            -${s.discount}%
                                        </div>
                                    </c:if>
                                    <!-- Product Image -->
                                    <a class="text-decoration-none text-dark" href="detail?id=${s.pro_id}">
                                        <img src="${s.image}" class="img-fluid rounded mb-2" alt="${s.pro_name}">
                                    </a>
                                    <!-- Product Name (Giới hạn chiều cao để tránh lệch) -->
                                    <a class="text-decoration-none text-dark" href="detail?id=${s.pro_id}">
                                        <h6 class="fw-bold" style="max-height: 100px">
                                            ${s.pro_name}
                                        </h6>
                                    </a>
                                    <!-- Price & Add to Cart (Căn đều nhau) -->
                                    <div class="mt-auto">
                                        <div>
                                            <c:if test="${s.discount > 0}">
                                                <span class="original-price text-decoration-line-through">${s.formattedPrice} VND</span>
                                                <span class="discount-price">${s.formattedDiscountedPrice} VND</span>
                                            </c:if>
                                            <c:if test="${s.discount == 0}">
                                                <div class="text-danger fw-bold">${s.formattedPrice} VND</div>
                                            </c:if>
                                        </div>
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
