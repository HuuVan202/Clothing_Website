<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="sidebar text-light p-3">
    <h3 class="text-center">Shop Manager</h3>
    <div class="nav flex-column">
        <a href="${pageContext.request.contextPath}/Dashboard" class="nav-link text-light p-3 border-0 rounded">
            <i class="bi bi-speedometer2"></i>Dashboard
        </a>
        <a href="accountHome" class="nav-link text-light p-3 border-0 rounded">
            <i class="bi bi-people"></i>Account Management
        </a>
        <a href="${pageContext.request.contextPath}/productM" class="nav-link text-light p-3 border-0 rounded">
            <i class="bi bi-box-seam"></i>Product Management
        </a>
        <a href="${pageContext.request.contextPath}/orderM" class="nav-link text-light p-3 border-0 rounded">
            <i class="bi bi-cart3"></i>Order Management
        </a>
        <a href="feedbackHome" class="nav-link text-light p-3 border-0 rounded">
            <i class="bi bi-chat-dots"></i>Feedback Management
        </a>
        <a href="${pageContext.request.contextPath}/revenue" class="nav-link text-light p-3 border-0 rounded">
            <i class="bi bi-graph-up"></i>Revenue
        </a>
        <a href="https://business.facebook.com/latest/inbox/messenger?asset_id=626403237211918" class="nav-link text-light p-3 border-0 rounded">
            <i class="bi bi-chat"></i>Chat with Customer
        </a>
    </div>
</div>