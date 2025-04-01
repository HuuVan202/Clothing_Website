<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css" rel="stylesheet" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/admin/managerLayout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/admin/orderManagement.css" />


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
                        <h2 class="order-title">
                            <i class="bi bi-cart-fill"></i> Order Management
                        </h2>

                        <!-- Filter Row -->
                        <div class="filter-row d-flex flex-column flex-md-row align-items-center justify-content-between gap-2 gap-md-1">
                            <input id="searchInput" type="text" class="form-control w-100 w-md-50" placeholder="Search" />
                            <div class="d-flex flex-column flex-md-row gap-2 w-100 w-md-auto">
                                <select id="timeRange" class="form-select" style="min-width: 180px">
                                    <option selected disabled="true">Select Time Range</option>
                                    <option value="all">All Time</option>
                                    <option value="today">Today</option>
                                    <option value="last7Days">Last 7 days</option>
                                    <option value="last30Days">Last 30 days</option>
                                </select>
                                <select id="statusFilter" class="form-select" style="min-width: 180px">
                                    <option value="" selected>All Statuses</option>
                                    <option value="processing">Processing</option>
                                    <option value="shipping">Shipping</option>
                                    <option value="delivered">Delivered</option>
                                    <option value="canceled">Canceled</option>
                                    <option value="pending_delivery">Pending Delivery</option>

                                </select>
                                <select id="sortSelect" class="form-select" style="min-width: 180px">
                                    <option selected disabled="true">Sort by</option>
                                    <option value="newest">Date (Newest)</option>
                                    <option value="oldest">Date (Oldest)</option>
                                    <option value="price_desc">Price (High to Low)</option>
                                    <option value="price_asc">Price (Low to High)</option>
                                </select>
                            </div>
                        </div>

                        <!-- Orders Table -->
                        <div class="table-container">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Order ID</th>
                                        <th>Customer</th>
                                        <th>Total Price</th>
                                        <th>Payment method</th>
                                        <th>Tracking status</th>
                                        <th>Order date</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Data will be loaded via AJAX -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Order Details Modal -->
            <div id="orderDetailsModal" class="modal">
                <div class="modal-content">
                    <div id="orderDetailsContent"></div>
                </div>
            </div>

            <!-- Update Modal -->
            <div id="updateModal" class="modal">
                <div class="modal-content">

                    <div id="updateContent"></div>
                    <button id="confirmUpdate" class="btn btn-primary mt-3">Update</button>
                </div>
            </div>

            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
            <script src="${pageContext.request.contextPath}/JS/admin/orderManagement.js"></script>
    </body>
</html>