<%-- 
    Document   : feedbackManagement
    Created on : Feb 17, 2025, 10:03:11 AM
    Author     : Dinh_Hau
--%>


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
                <jsp:include page="../common/layout/managerHeaderFeedback.jsp"></jsp:include>

                    <!-- Content Area -->
                    <div class="content-area">
                        <div class="container mt-4">
                            <h2 class="text-center">Customer Feedback Management</h2>

                            <!-- Search & Filters -->

                            <form action="searchFeedbackCustomer" method="get" class="d-flex mb-3">

                                <select id="ratingFilter" class="form-select me-2">
                                    <option value="">Select Rating</option>
                                    <option value="5">5 Stars</option>
                                    <option value="4">4 Stars</option>
                                    <option value="3">3 Stars</option>
                                    <option value="2">2 Stars</option>
                                    <option value="1">1 Star</option>
                                </select>
                                <select id="sortFilter" class="form-select">
                                    <option value="asc">Sort by time</option>
                                    <option value="desc">Sort by time</option>
                                </select>
                                <input type="text" name="cus_name"  id="search" class="form-control me-2" placeholder="Search customer name"
                                       value="${search}">
                            <button type="submit" class="btn btn-primary ms-2">Search</button>


                        </form>


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
                                <c:forEach var="feedback" items="${feedback}">
                                    <tr>
                                        <td>${feedback[0].feedback_id}</td>
                                        <td>${feedback[1].cus_name}</td>

                                        <td>
                                            <img src="${feedback[2].image}" alt="Product Image" class="product-img">
                                            ${feedback[2].pro_name}
                                        </td>
                                        <td>
                                            ${feedback[0].rating}
                                            <span class="star">★</span>
                                        </td>

                                        <td>${feedback[0].comment}</td>

                                        <td>${feedback[0].feedback_date}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

            </div>
        </div>
    </body>
</html>
