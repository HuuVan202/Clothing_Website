<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- header -->

<header class="p-3 mb-3 border-bottom">
    <div class="container">
        <h4 class="text-center">
            <a class="text-decoration-none text-white" href="${pageContext.request.contextPath}/home"
               >ONLINE SHOP</a
            >
        </h4>
        <hr class="my-3" />
        <div
            class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start"
            >
            <ul
                class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0 gap-2"
                >
                <li class="nav-item">
                    <a
                        class="nav-link px-2 link-dark main-link border rounded-3"
                        id="allProducts"
                        href="products"
                        >
                        All Products
                    </a>
                </li>

                <!-- set điều kiện nếu là admin -->
                <c:if test="${sessionScope.admin.role.toLowerCase() == 'admin'}">
                    <li class="nav-item">
                        <a
                            class="nav-link px-2 link-dark main-link border rounded-3"
                            href="Dashboard">
                            Back to Dashboard
                        </a>
                    </li>
                </c:if>

                <!-- set điều kiện nếu là staff -->
                <c:if test="${sessionScope.staff.role.toLowerCase() == 'staff'}">
                    <li class="nav-item">
                        <a
                            class="nav-link px-2 link-dark main-link border rounded-3"
                            href="DashboardS">
                            Back to Dashboard
                        </a>
                    </li>
                </c:if>
            </ul>

            <div class="cart me-3 position-relative">
                <a href="Cart" class="d-block link-dark text-decoration-none" style="color: white">
                    <img class="border" 
                         src="${pageContext.request.contextPath}/img/icon/header/icons8-shopping-cart-100.png" 
                         title="Cart" 
                         alt="cart-icon" 
                         style="width: 40px; height: 40px;"/>
                </a>
                <c:if test="${not empty sessionScope.customer && size > 0}">
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" 
                          style="font-size: 0.6em;">
                        ${size}
                    </span>
                </c:if>
            </div>

            <div class="dropdown text-end user-services">
                <a
                    class="d-block link-dark text-decoration-none"
                    id="dropdownUser"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                    >
                    <!-- nếu chưa đăng nhập src giữ nguyên -->
                    <!-- sau khi đăng nhập đổi lại thành  icons8-male-user-100.png -->
                    <c:choose>
                        <c:when test="${empty sessionScope.customer}">
                            <img
                                src="${pageContext.request.contextPath}/img/icon/header/icons8-user-not-found-100.png"
                                alt="user-icon"
                                class="border"
                                onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/img/common/icon/header/icons8-user-not-found-100.png';"
                                />
                        </c:when>
                        <c:otherwise>
                            <img
                                src="${pageContext.request.contextPath}/img/icon/header/icons8-male-user-100.png"
                                alt="user-icon"
                                class="border"
                                onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/img/common/icon/header/icons8-male-user-100.png';"
                                />                          
                        </c:otherwise>
                    </c:choose>
                </a>

                <ul class="dropdown-menu">
                    <!-- Không đăng nhập -->
                    <c:choose>
                        <c:when test="${empty sessionScope.customer}">
                            <li id="loginItem">
                                <a class="dropdown-item" href="Login">Login</a>
                            </li>
                            <li id="signupItem">
                                <a class="dropdown-item" href="Signup">Sign up</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <!-- Khi đã đăng nhập -->
                            <li id="profileItem" >
                                <a class="dropdown-item" href="profile">Profile</a>
                            </li>
                            <li id="logoutItem">
                                <a class="dropdown-item" href="Logout">Logout</a>
                            </li>                         
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </div>
</header>
