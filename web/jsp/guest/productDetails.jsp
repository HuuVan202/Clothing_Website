<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.lang.Math" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Product Details</title>

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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
                                    <p><strong>Name:</strong> ${pd.pro_name}</p>
                                    <div class="rating-info mb-2">
                                        <c:set var="rating" value="${pd.averageRating}" />
                                        <c:set var="fullStars" value="${rating - (rating % 1)}" />
                                        <c:set var="fraction" value="${rating % 1}" />
                                        <c:set var="hasHalfStar" value="${fraction >= 0.5}" />
                                        <c:set var="emptyStars" value="${5 - fullStars - (hasHalfStar ? 1 : 0)}" />
                                        <span class="rating-number text-decoration-underline fw-bold">${pd.averageRating}</span>
                                        <span class="rating-stars text-warning">
                                            <c:forEach begin="1" end="${fullStars}">
                                                <i class="fas fa-star"></i>
                                            </c:forEach>
                                            <c:if test="${hasHalfStar}">
                                                <i class="fas fa-star-half-alt"></i>
                                            </c:if>
                                            <c:forEach begin="1" end="${emptyStars}">
                                                <i class="far fa-star"></i>
                                            </c:forEach>
                                        </span>
                                        <span class="rating-divider">|</span>
                                        <span class="feedback-count"><span class="text-decoration-underline fw-bold">${pd.feedbackCount}</span> Feedback</span>
                                        <span class="rating-divider">|</span>
                                        <span class="sold-count fw-bold">${pd.soldProduct}</span> Sold
                                    </div>

                                    <c:if test="${pd.discount > 0}">
                                        <div class="d-flex align-items-center gap-2">
                                            <span class="fs-4 fw-bold text-danger">${pd.formattedDiscountedPrice} VND</span>
                                            <span class="text-muted text-decoration-line-through">${pd.formattedPrice} VND</span>
                                            <span class="badge bg-danger text-white">${pd.discount}%</span>
                                        </div>
                                    </c:if>
                                    <c:if test="${pd.discount == 0}">
                                        <span class="fs-4 fw-bold text-dark">${pd.formattedPrice} VND</span>
                                    </c:if>
                                    <p><strong>Type:</strong> ${pd.type.type_name}</p>
                                    <form action="Cart" method="post" class="m-0">
                                        <input type="hidden" name="pro_id" value="${pd.pro_id}" />
                                        <input type="hidden" name="action" value="add" />
                                        <p><strong>Size:</strong> 
                                            <select id="size" name="size" required class="form-select w-50" onchange="updateMaxQuantity()">
                                                <option value="" selected disabled>Please choose size</option>
                                                <c:forEach var="size" items="${productSizes}">
                                                    <option value="${size.size}" 
                                                            data-stock="${size.stock}"
                                                            ${param.size eq size.size ? 'selected' : ''}
                                                            ${size.stock == 0 ? 'disabled style="color:red;"' : ''}>
                                                        ${size.size} 
                                                        <c:choose>
                                                            <c:when test="${size.stock == 0}">(Out of stock)</c:when>
                                                            <c:otherwise>(Stock ${size.stock})</c:otherwise>
                                                        </c:choose>
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </p>
                                        <p><strong>Quantity:</strong> 
                                            <input class="form-control w-50" type="number" id="quantityInput" name="quantity" min="1" disabled/>
                                        </p>               
                                        <div class="d-flex flex-column gap-2 w-100">
                                            <button type="submit" class="btn btn-success w-50">Add to Cart</button>

                                        </div>
                                    </form>
                                    <div class="mt-2">
                                        <button type="button" class="btn btn-primary feedback-btn w-50" data-product-id="${pd.pro_id}">Give Feedback</button>
                                        <div class="feedback-warning fw-bold text-danger mt-1"></div>
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

        <!-- Feedback Section -->
        <div class="container mt-5 product-wrapper">
            <h2 class="mb-4">Feedbacks:</h2>

            <input type="hidden" name="pro_id" value="${pd.pro_id}">

            <!-- Filter Buttons -->
            <div class="d-flex flex-wrap justify-content-center gap-2 mb-4">
                <c:forEach var="f" items="${['all','5star','4star','3star','2star','1star']}">
                    <button class="btn btn-sm btn-outline-secondary feedback-filter-btn" data-filter="${f}" onclick="filterFeedback('${f}')">
                        <c:choose>
                            <c:when test="${f == 'all'}">All</c:when>
                            <c:otherwise>${fn:substring(f, 0, 1)} Star</c:otherwise>
                        </c:choose>
                    </button>
                </c:forEach>
            </div>

            <!-- Feedback Content -->
            <div class="d-flex justify-content-center">
                <div class="col-lg-6 col-md-8 col-sm-12 border rounded p-4 bg-light shadow-sm" style="max-height: 400px; overflow-y: auto;" id="feedback-container">
                    <c:forEach var="f" items="${feedbackOfProduct}">
                        <div class="border-bottom pb-2 mb-2">
                            <div class="d-flex justify-content-between">
                                <span class="fw-bold">${f.cus_name}</span>
                                <c:if test="${sessionScope.customer != null && sessionScope.customer.cus_id eq f.cus_id}">
                                    <button class="btn btn-sm btn-outline-primary edit-feedback-btn" 
                                            data-feedback-id="${f.feedback_id}"
                                            data-rating="${f.rating}"
                                            data-comment="${f.comment}">
                                        Edit
                                    </button>
                                </c:if>
                            </div>
                            <div class="text-warning my-1">
                                <c:forEach begin="1" end="${f.rating}">★</c:forEach>
                                <c:forEach begin="${f.rating + 1}" end="5">☆</c:forEach>
                                </div>
                                <div class="text-muted small mb-1">
                                ${f.feedback_date} | Variation(s): ${f.purchasedSizes}
                            </div>
                            <p class="mb-0" style="white-space: pre-line; word-break: break-word;">${f.comment}</p>
                        </div>
                    </c:forEach>
                    <c:if test="${empty feedbackOfProduct}">
                        <p class="text-muted text-center">No feedback yet</p>
                    </c:if>
                </div>
            </div>

            <!-- Feedback Modal -->
            <div class="modal fade" id="feedbackModal" tabindex="-1" aria-labelledby="feedbackModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="feedbackModalLabel">Give Feedback</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="feedbackForm">
                                <input type="hidden" name="pro_id" value="${pd.pro_id}">
                                <div class="mb-3">
                                    <label class="form-label">Rating:</label>
                                    <div class="rating d-flex justify-content-center">
                                        <input type="radio" name="rating" value="5" id="star5"><label for="star5">★</label>
                                        <input type="radio" name="rating" value="4" id="star4"><label for="star4">★</label>
                                        <input type="radio" name="rating" value="3" id="star3"><label for="star3">★</label>
                                        <input type="radio" name="rating" value="2" id="star2"><label for="star2">★</label>
                                        <input type="radio" name="rating" value="1" id="star1"><label for="star1">★</label>
                                    </div>
                                    <div id="ratingWarning" class="feedback-warning text-danger fw-bold" style="display: none;"></div>
                                </div>
                                <div class="mb-3">
                                    <label for="comment" class="form-label">Comment:</label>
                                    <textarea class="form-control" id="comment" name="comment" rows="3" maxlength="500"></textarea>
                                    <div class="d-flex justify-content-between">
                                        <div id="commentWarning" class="feedback-warning text-danger fw-bold" style="display: none;"></div>
                                        <small class="text-muted">Characters remaining: <span id="charCount">500</span></small>
                                    </div>
                                </div>

                                <!-- Error Message -->
                                <div id="feedbackError" class="alert alert-danger d-none"></div>
                                <div id="successMessage" class="alert alert-success" style="display: none;"></div>

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary">Submit Feedback</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Suggest Products -->
            <div class="container mt-5 product-wrapper">
                <h2 class="container mt-5">Suggest Products</h2>
                <c:choose>
                    <c:when test="${not empty suggestProducts}">
                        <button class="scroll-btn scroll-left d-none d-md-flex" onclick="scrollCards('suggestProducts', -1)">
                            &#8249;
                        </button>
                        <div class="product-container" id="suggestProducts">
                            <c:forEach items="${suggestProducts}" var="p">
                                <div class="product-card position-relative">
                                    <c:if test="${p.discount > 0}">
                                        <span class="discount-badge">${p.discount}%</span>
                                    </c:if>
                                    <img src="${p.image}" class="product-img" alt="${p.pro_name}" />
                                    <div class="card-body">
                                        <div>
                                            <a class="text-decoration-none text-dark" href="detail?id=${p.pro_id}">
                                                <h5 class="card-title">${p.pro_name}</h5>
                                            </a>
                                        </div>
                                        <div class="mt-1">
                                            <c:if test="${p.discount > 0}">
                                                <div class="discount-price">${p.formattedDiscountedPrice} VND</div>
                                            </c:if>
                                            <c:if test="${p.discount == 0}">
                                                <div class="discount-price">${p.formattedPrice} VND</div>
                                            </c:if>
                                            <!-- Hiển thị averageRating nếu > 0 -->
                                            <c:if test="${p.averageRating > 0}">
                                                <div class="mt-1">
                                                    <span class="text-warning">★</span>
                                                    <span class="text-muted">${p.averageRating}</span>
                                                </div>
                                            </c:if>
                                        </div>
                                        <!--  Hiển thị tổng số đơn hàng (total_order) -->
                                        <div class="text-muted small">
                                            <strong>Sold: ${p.soldProduct}</strong>
                                        </div>

                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <button class="scroll-btn scroll-right" onclick="scrollCards('suggestProducts', 1)">
                            &#8250;
                        </button>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted">${suggestProductsMessage}</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>



        <!-- Footer -->
        <jsp:include page="../common/layout/footer.jsp" />

        <script src="${pageContext.request.contextPath}/JS/guest/productDetails.js" defer></script>
        <script>
                            function updateMaxQuantity() {
                                const sizeSelect = document.getElementById("size");
                                const selectedOption = sizeSelect.options[sizeSelect.selectedIndex];
                                const quantityInput = document.getElementById("quantityInput");
                                quantityInput.value = 1;

                                if (selectedOption.value && selectedOption.getAttribute("data-stock") > 0) {
                                    const stock = parseInt(selectedOption.getAttribute("data-stock"));

                                    quantityInput.disabled = false;

                                    quantityInput.max = stock;

                                    if (parseInt(quantityInput.value) > stock) {

                                        quantityInput.value = stock;

                                    }

                                } else {
                                    quantityInput.disabled = true;
                                }
                            }

                            document.addEventListener("DOMContentLoaded", function () {
                                document.getElementById("size").addEventListener("change", updateMaxQuantity);

                                if (document.getElementById("size").value) {
                                    updateMaxQuantity();
                                }
                            });

                            function scrollCards(containerId, direction) {
                                const container = document.getElementById(containerId);
                                if (container) {
                                    container.scrollBy({left: direction * 250, behavior: "smooth"});
                                }
                            }

                            document.querySelectorAll('.feedback-btn').forEach(button => {
                                button.addEventListener('click', async function (e) {
                                    e.preventDefault();
                                    const productId = this.dataset.productId;
                                    const warningDiv = this.parentElement.querySelector('.feedback-warning');

                                    // 👉 Đây là Give Feedback nên cần check
                                    try {
                                        const response = await fetch(`checkPurchase?pro_id=${productId}`, {
                                            method: 'GET',
                                            headers: {'Accept': 'application/json'}
                                        });

                                        if (!response.ok)
                                            throw new Error('Network response was not ok');

                                        const data = await response.json();

                                        this.classList.remove('btn-danger');
                                        this.classList.add('btn-primary');
                                        warningDiv.style.display = 'none';

                                        if (data.status === 'error') {
                                            this.classList.remove('btn-primary');
                                            this.classList.add('btn-danger');

                                            if (data.message === 'not_logged_in') {
                                                window.location.href = 'Login';
                                                return;
                                            }

                                            warningDiv.textContent = data.message === 'already_reviewed'
                                                    ? 'Sorry, you can only feedback once for this product.'
                                                    : 'Sorry, you must have ordered and received this product to be able to give feedback.';
                                            warningDiv.style.display = 'block';
                                            return;
                                        }

                                        const modal = new bootstrap.Modal(document.getElementById('feedbackModal'));
                                        modal.show();
                                    } catch (error) {
                                        const errorDiv = document.getElementById('feedbackError');
                                        errorDiv.style.display = 'block';
                                        errorDiv.textContent = 'An error occurred. Please try again later.';
                                    }
                                });
                            });
        </script>
    </body>
</html>


