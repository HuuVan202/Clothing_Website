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
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
            crossorigin="anonymous"
            />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/4.5.3/css/bootstrap.min.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/4.5.3/js/bootstrap.min.js"></script>
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
                                    <li><a href="Order" class="nav-link active">Orders</a></li>
                                    <li><a href="ChangePassword" class="nav-link">Change Password</a></li>
                                </ul>
                            </div>    
                        </div>

                        <div class="col-sm-12 col-md-9 col-lg-9">
                            <!-- Tab Content -->
                            <div class="tab-content dashboard_content">
                                <!-- Account Details -->
                                <div class="tab-pane fade show active" id="account-details">
                                    <h3>List Order</h3>
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
                                            <tr>
                                                <td>${sessionScope.customer.cus_name}</td>
                                                <td>${o.order_date}</td>
                                                <td>${o.tracking}</td>
                                                <td>${o.payment_method}</td>
                                                <td>${o.total_price}</td>
                                                <td><a href="OrderDetail?id=${o.order_id != null ? o.order_id : ''}" class="view">View</a></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>      	
        </section>
        <!-- footer -->
        <jsp:include page="../common/layout/footer.jsp"></jsp:include>
    </tbody>
</table>
</body>
</html>
