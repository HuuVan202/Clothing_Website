<%-- 
    Document   : orders
    Created on : Feb 17, 2025, 10:04:56 AM
    Author     : Dinh_Hau
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Orders</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/customer/profile.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/common/layout/layout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/home.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/customer/orders.css"/>

        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
            crossorigin="anonymous"
            />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/4.5.3/css/bootstrap.min.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/4.5.3/js/bootstrap.min.js"></script>
        <style>
            .order-header {
                display: flex;
                align-items: center; /* Căn giữa theo chiều dọc */
                justify-content: space-between; /* Đẩy các phần tử về hai bên */
                gap: 10px; /* Khoảng cách giữa các phần tử */
            }

            .order-header h3 {
                margin: 0; /* Xóa margin mặc định */
                flex-grow: 1; /* Đẩy select về sát mép trái */
            }

            .order-header select {
                width: auto; /* Đảm bảo kích thước không quá lớn */
                padding: 5px 10px;
            }

        </style>
    </head>
    <body>

        <jsp:include page="../common/layout/header.jsp"></jsp:include>
            <section class="main_content_area">
                <div class="account_dashboard">
                    <div class="row w-100">
                        <div class="col-sm-12 col-md-3 col-lg-3">
                            <!-- Sidebar -->
                            <div class="dashboard_tab_button">
                                <ul role="tablist" class="nav flex-column dashboard-list">
                                    <li style="margin-bottom: 20px">
                                        <img style="border: 5px solid #2d336b; height: 255px" src="img/icon/header/user.png" width="100%">
                                    </li>
                                    <li><a href="profile" class="nav-link">Account Details</a></li>
                                    <c:if test="${sessionScope.admin.role.toLowerCase() != 'admin'}">
                                    <li><a href="Order" class="nav-link active">Orders</a></li>
                                    </c:if>
                                <li><a href="ChangePassword" class="nav-link">Change Password</a></li>
                            </ul>
                        </div>    
                    </div>

                    <div class="col-sm-12 col-md-9 col-lg-9">
                        <!-- Tab Content -->
                        <div class="tab-content dashboard_content">
                            <div class="tab-pane fade show active" id="account-details">

                                <!-- Xử lý các bảng khi không có sản phẩm bên trong -->
                                <c:if test="${empty requestScope.listOrder}">
                                    <span class="message warn center-screen">
                                        You currently have no orders.
                                    </span>
                                    <a href="products" class="shopping">
                                        Shopping Now
                                    </a>
                                </c:if>

                                <c:set var="hasProcessing" value="false"/>
                                <c:set var="hasShipping" value="false"/>
                                <c:set var="hasDelivered" value="false"/>
                                <c:set var="hasCanceled" value="false"/>

                                <c:forEach items="${requestScope.listOrder}" var="o">
                                    <c:if test="${o.tracking eq 'processing'}">
                                        <c:set var="hasProcessing" value="true"/>
                                    </c:if>
                                    <c:if test="${o.tracking eq 'shipping'}">
                                        <c:set var="hasShipping" value="true"/>
                                    </c:if>
                                    <c:if test="${o.tracking eq 'delivered'}">
                                        <c:set var="hasDelivered" value="true"/>
                                    </c:if>
                                    <c:if test="${o.tracking.toLowerCase() eq 'canceled'}">
                                        <c:set var="hasCanceled" value="true"/>
                                    </c:if>
                                </c:forEach>


                                <c:if test="${not empty requestScope.listOrder}">
                                    <div class="order-header">
                                        <h3></h3>
                                        <select id="orderStatus" onchange="filterOrders()">
                                            <option value="processing">Processing</option>
                                            <option value="shipping">Shipping</option>
                                            <option value="delivered">Delivered</option>
                                            <option value="canceled">Canceled</option>
                                        </select>
                                    </div>

                                    <!-- Table Processing -->
                                    <div id="processingTable">
                                        <h3>Processing Orders</h3>
                                        <c:if test="${hasProcessing}">
                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <th>Full Name</th>
                                                        <th>Date</th>
                                                        <th>Status</th>
                                                        <th>Payment</th>
                                                        <th>Total</th>
                                                        <th>Actions</th> 
                                                    </tr>
                                                </thead>
                                                <tbody>   
                                                    <c:forEach items="${requestScope.listOrder}" var="o">                                
                                                        <c:if test="${o.tracking eq 'processing'}">
                                                            <tr>
                                                                <td>${sessionScope.customer.cus_name}</td>
                                                                <td>${o.order_date}</td>
                                                                <td>${o.tracking}</td>
                                                                <td>${o.payment_method}</td>
                                                                <td>${o.getFormattedPrice()} VND</td>
                                                                <td>
                                                                    <a href="OrderDetail?id=${o.order_id}" class="view">View</a>
                                                                    <c:if test="${o.tracking eq 'processing'}">
                                                                        <a href="#" onclick="confirmCancel(event, '${o.order_id}')" class="cancel">Cancel</a>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>  
                                                </tbody>
                                            </table>
                                        </c:if>
                                        <c:if test="${not hasProcessing}">
                                            <p class="alert alert-warning" style="width: 400px">There are currently no orders processing.</p>
                                        </c:if>
                                    </div>

                                    <!-- Table Shipping -->
                                    <div id="shippingTable" style="display: none;">
                                        <h3>Shipping</h3>
                                        <c:if test="${hasShipping}">
                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <th>Full Name</th>
                                                        <th>Date</th>
                                                        <th>Status</th>
                                                        <th>Payment</th>
                                                        <th>Total</th>
                                                        <th>Actions</th> 
                                                    </tr>
                                                </thead>
                                                <tbody>   
                                                    <c:forEach items="${requestScope.listOrder}" var="o">
                                                        <c:if test="${o.tracking eq 'shipping'}">
                                                            <tr>
                                                                <td>${sessionScope.customer.cus_name}</td>
                                                                <td>${o.order_date}</td>
                                                                <td>${o.tracking}</td>
                                                                <td>${o.payment_method}</td>
                                                                <td>${o.getFormattedPrice()} VND</td>
                                                                <td><a href="OrderDetail?id=${o.order_id}" class="view">View</a></td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>  
                                                </tbody>
                                            </table>
                                        </c:if>

                                        <!-- Thêm thông báo nếu không có đơn hàng -->
                                        <c:if test="${not hasShipping}">
                                            <p class="alert alert-warning" style="width: 400px">There are currently no shipping orders.</p>
                                        </c:if>
                                    </div>

                                    <!-- Table Delivered -->
                                    <div id="deliveredTable" style="display: none;">
                                        <h3>Delivered</h3>
                                        <c:if test="${hasDelivered}">
                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <th>Full Name</th>
                                                        <th>Date</th>
                                                        <th>Status</th>
                                                        <th>Payment</th>
                                                        <th>Total</th>
                                                        <th>Actions</th> 
                                                    </tr>
                                                </thead>
                                                <tbody>   
                                                    <c:forEach items="${requestScope.listOrder}" var="o">
                                                        <c:if test="${o.tracking eq 'delivered'}">
                                                            <tr>
                                                                <td>${sessionScope.customer.cus_name}</td>
                                                                <td>${o.order_date}</td>
                                                                <td>${o.tracking}</td>
                                                                <td>${o.payment_method}</td>
                                                                <td>${o.getFormattedPrice()} VND</td>
                                                                <td><a href="OrderDetail?id=${o.order_id}" class="view">View</a></td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>  
                                                </tbody>
                                            </table>
                                        </c:if>
                                        <c:if test="${not hasDelivered}">
                                            <p class="alert alert-warning" style="width: 400px">There are currently no orders delivered.</p>
                                        </c:if>
                                    </div>

                                    <!-- Table Canceled -->
                                    <div id="canceledTable" style="display: none;">
                                        <h3>Canceled Orders</h3>
                                        <c:if test="${hasCanceled}">
                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <th>Full Name</th>
                                                        <th>Date</th>
                                                        <th>Status</th>
                                                        <th>Payment</th>
                                                        <th>Total</th>
                                                        <th>Actions</th> 
                                                    </tr>
                                                </thead>
                                                <tbody>   
                                                    <c:forEach items="${requestScope.listOrder}" var="o">
                                                        <c:if test="${o.tracking.toLowerCase() eq 'canceled'}">
                                                            <tr>
                                                                <td>${sessionScope.customer.cus_name}</td>
                                                                <td>${o.order_date}</td>
                                                                <td>${o.tracking}</td>
                                                                <td>${o.payment_method}</td>
                                                                <td>${o.getFormattedPrice()} VND</td>
                                                                <td><a href="OrderDetail?id=${o.order_id}" class="view">View</a></td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>  
                                                </tbody>
                                            </table> 
                                        </c:if>
                                        <c:if test="${not hasCanceled}">
                                            <p class="alert alert-warning" style="width: 400px">There are currently no orders canceled.</p>
                                        </c:if>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>      	
        </section>
        <!-- footer -->
        <jsp:include page="../common/layout/footer.jsp"></jsp:include>
        <script>
            function confirmCancel(event, orderId) {
                event.preventDefault(); // Ngăn chặn chuyển hướng mặc định
                if (confirm("Are you sure you want to cancel this order?")) {
                    window.location.href = "Cancel?id=" + orderId; // 
                }
            }

            function filterOrders() {
                // Lấy giá trị của option đang được chọn
                var selectedStatus = document.getElementById("orderStatus").value;

                // Danh sách các bảng
                var tables = {
                    processing: document.getElementById("processingTable"),
                    shipping: document.getElementById("shippingTable"),
                    delivered: document.getElementById("deliveredTable"),
                    canceled: document.getElementById("canceledTable")
                };

                // Ẩn tất cả các bảng
                for (var key in tables) {
                    if (tables[key]) {
                        tables[key].style.display = "none";
                    }
                }

                // Hiển thị bảng tương ứng với trạng thái được chọn
                if (tables[selectedStatus]) {
                    tables[selectedStatus].style.display = "block";
                }

                // Lưu trạng thái vào localStorage để duy trì trạng thái khi load lại trang
                localStorage.setItem("selectedOrderStatus", selectedStatus);
            }

            // Khi tải trang, tự động hiển thị bảng theo trạng thái đã lưu
            document.addEventListener("DOMContentLoaded", function () {
                var savedStatus = localStorage.getItem("selectedOrderStatus") || "processing";
                document.getElementById("orderStatus").value = savedStatus;
                filterOrders();
            });
        </script>
    </body>
</html>
