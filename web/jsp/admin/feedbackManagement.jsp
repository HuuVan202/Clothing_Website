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
        <title>Feedback Management</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
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
        <div class="container mt-4">
            <h2 class="text-center">Feedback Management</h2>

            <!-- Search & Filters -->
            <div class="d-flex mb-3">
                <input type="text" id="search" class="form-control me-2" placeholder="Search">
                <select id="ratingFilter" class="form-select me-2">
                    <option value="">Select Rating</option>
                    <option value="5">5 Stars</option>
                    <option value="4">4 Stars</option>
                    <option value="3">3 Stars</option>
                    <option value="2">2 Stars</option>
                    <option value="1">1 Star</option>
                </select>
                <select id="sortFilter" class="form-select">
                    <option value="asc">Sort by ▲</option>
                    <option value="desc">Sort by ▼</option>
                </select>
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
                    <c:forEach var="feedback" items="${feedbacks}">
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

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
