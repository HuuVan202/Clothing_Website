<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="sidebar bg-dark text-white">
    <div class="sidebar-header p-3 d-flex align-items-center">
        <i class="bi bi-truck fs-4 me-2"></i>
        <span class="fs-5 fw-bold">Shipper Portal</span>
    </div>
    <hr class="my-0 bg-secondary"/>
    <ul class="nav flex-column p-2">
        <li class="nav-item active">
            <a class="nav-link active" href="DashBoardcontroller">
                <i class="bi bi-speedometer2 me-2"></i>Dashboard
            </a>
        </li>
        <li class="nav-item">

            <a href="${pageContext.request.contextPath}/orderServlet" class="nav-link "> 
                <i class="bi bi-list-task me-2"></i>My Deliveries
            </a>

        </li>
        <li class="nav-item">

            <a href="${pageContext.request.contextPath}/shippingOrderServlet" class="nav-link "> 
                <i class="bi bi-truck me-2"></i>In transit
            </a>

        </li>

        <li class="nav-item">
            <a class="nav-link" href="orderHistory">
                <i class="bi bi-clock-history me-2"></i>Delivery History
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link" href="Login">
                <i class="bi bi-gear me-2"></i>Logout
            </a>
        </li>
    </ul>
</div>