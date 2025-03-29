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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/productDetails.css"/>
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="../common/layout/header.jsp" />

        <!-- Product Details Section -->
        <div class="container my-5">
            <h1 class="text-center mb-4">Product Details</h1>
            <c:if test="${not empty sessionScope.error or not empty sessionScope.successMessage}">
                <div id="flash-message" class="alert text-center ${not empty sessionScope.error ? 'alert-danger' : 'alert-success'}">
                    ${not empty sessionScope.error ? sessionScope.error : sessionScope.successMessage}
                </div>
                <c:remove var="error" scope="session"/>
                <c:remove var="successMessage" scope="session"/>
                <script>
                    setTimeout(function () {
                        var element = document.getElementById('flash-message');
                        element.style.transition = 'opacity 0.3s ease-out';
                        element.style.opacity = '0';
                        setTimeout(function () {
                            element.remove();
                        }, 300);
                    }, 3000);
                </script>
            </c:if>
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
                                    <p>
                                        <c:if test="${pd.stock >0}">
                                            <strong>Stock:</strong> ${pd.stock}
                                        </c:if>
                                        <c:if  test="${pd.stock <=0}">
                                            <strong>Stock:</strong> <span class="text-danger">Out of stock</span>
                                        </c:if>
                                    </p>
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
                                    <div class="d-flex flex-column gap-2 w-100">
                                        <form action="Cart" method="post" class="m-0">
                                            <input type="hidden" name="pro_id" value="${pd.pro_id}" />
                                            <input type="hidden" name="action" value="add" />
                                            <button type="submit" class="btn btn-success w-50">Add to Cart</button>
                                        </form>
                                    </div>
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

        <!-- Footer -->
        <jsp:include page="../common/layout/footer.jsp" />
    </body>
</html>