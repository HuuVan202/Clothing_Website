<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
            />
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css"
            rel="stylesheet"
            />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/admin/managerLayout.css"/>
        <style>
            .star {
                color: gold;
                font-size: 20px;
            }
            .product-img {
                width: 50px;
                height: 50px;
                object-fit: cover;
            }
        </style>
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
                        <div class="container mt-4">
                            <h2 class="text-center">Customer Feedback Management</h2>


                            <!-- Search, Filter & Sort -->
                            <div class="row mb-3">
                                <!-- Filter Rating -->
                                <div class="col-md-4">
                                    <form action="filterFeedback" method="get">
                                        <input type="hidden" name="pro_name" value="${search}">
                                    <input type="hidden" name="order" value="${sortOrder}">
                                    <select id="rating" name="rating" class="form-select" onchange="this.form.submit()">
                                        <option value="">Select Rating</option>
                                        <option value="5" ${selectedRating == '5' ? 'selected' : ''}>5 Stars</option>
                                        <option value="4" ${selectedRating == '4' ? 'selected' : ''}>4 Stars</option>
                                        <option value="3" ${selectedRating == '3' ? 'selected' : ''}>3 Stars</option>
                                        <option value="2" ${selectedRating == '2' ? 'selected' : ''}>2 Stars</option>
                                        <option value="1" ${selectedRating == '1' ? 'selected' : ''}>1 Star</option>
                                    </select>
                                </form>
                            </div>

                            <!-- Sort Feedback -->
                            <div class="col-md-4">
                                <form action="filterFeedback" method="get">
                                    <input type="hidden" name="pro_name" value="${search}">
                                    <input type="hidden" name="rating" value="${selectedRating}">
                                    <select name="order" class="form-select" onchange="this.form.submit()">
                                        <option value="asc" ${sortOrder == 'asc' ? 'selected' : ''}>Oldest</option>
                                        <option value="desc" ${sortOrder == 'desc' ? 'selected' : ''}>Newest</option>
                                    </select>
                                </form>
                            </div>

                            <!-- Search Product Name -->
                            <div class="col-md-4">
                                <form action="filterFeedback" method="get" class="d-flex">
                                    <input type="hidden" name="rating" value="${selectedRating}">
                                    <input type="hidden" name="order" value="${sortOrder}">
                                    <input type="text" name="pro_name" id="search" class="form-control me-2" 
                                           placeholder="Search Feedback by Product Name" value="${search}">
                                    <button type="submit" class="btn btn-primary" style="background: #233446" >Search</button>
                                </form>
                            </div>
                        </div>




                        <!-- Feedback Table -->
                        <table class="table table-bordered">
                            <thead class="table-dark">
                                <tr>
                                    <th>ID</th>
                                    <th>Customer</th>
                                    <th>Product</th>
                                    <th>Rating</th>
                                    <th>Comment</th>
                                    <th>Date</th>
                                </tr>

                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty errorMessage}">
                                        <tr>
                                            <td colspan="9" class="text-center text-danger">${errorMessage}</td>
                                        </tr>
                                    </c:when>

                                    <c:otherwise>
                                        <c:forEach var="feedback" items="${feedback}">
                                            <tr>
                                                <td>${feedback[0].feedback_id}</td>
                                                <td>${feedback[1].cus_name}</td>
                                                <td>
                                                    <img src="${feedback[2].image}" alt="Product Image" class="product-img">
                                                    <a href="viewFeedbackDetails?pro_name=${feedback[2].pro_name}" style="text-decoration: none ;color: black">${feedback[2].pro_name}</a>
                                                </td>
                                                <td>
                                                    ${feedback[0].rating}
                                                    <span class="star">★</span>
                                                </td>
                                                <td>${feedback[0].comment}</td>
                                                <td>${feedback[0].feedback_date}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>

                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
