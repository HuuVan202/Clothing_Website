
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%-- 
    Document   : home
    Created on : Feb 17, 2025, 10:06:23 AM
    Author     : Dinh_Hau
--%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Home</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/common/layout/layout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/home.css"/>
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
            crossorigin="anonymous"
            />
        <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"
        ></script>
    </head>
    <body>

        <!-- header -->
        <jsp:include page="../common/layout/header.jsp"></jsp:include>
            <!-- New Arrivals -->
            <div class="container mt-5 product-wrapper">
                <h2 class="container mt-5">New Arrivals</h2>
            <c:choose>
                <c:when test="${not empty newArrivalsList}">
                    <button class="scroll-btn scroll-left d-none d-md-flex" onclick="scrollCards('newArrivals', -1)">
                        &#8249;
                    </button>
                    <div class="product-container" id="newArrivals">
                        <c:forEach items="${newArrivalsList}" var="p">
                            <div class="product-card position-relative">
                                <c:if test="${p.discount > 0}">
                                    <span class="discount-badge">${p.discount}%</span>
                                </c:if>
                                <img src="${p.image}" class="product-img" alt="${p.pro_name}" />
                                <div class="card-body">
                                    <a class="text-decoration-none text-dark" href="detail?id=${p.pro_id}">
                                        <h5 class="card-title">${p.pro_name}</h5>
                                    </a>
                                    <div>
                                        <c:if test="${p.discount > 0}">
                                            <div class="discount-price">${p.formattedDiscountedPrice} VND</div>
                                            <div class="original-price text-decoration-line-through">${p.formattedPrice} VND</div>
                                        </c:if>
                                        <c:if test="${p.discount == 0}">
                                            <div class="discount-price">${p.formattedPrice} VND</div>
                                        </c:if>
                                    </div>
                                    <div>
                                        <form method="post" action="cartServlet?id=${p.pro_id}" >
                                            <input type="hidden" name="pro_id" value="${p.pro_id}"/>
                                            <input type="hidden" name="action" value="add" />
                                            <button type="submit">Buy</button>
                                        </form>       
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <button class="scroll-btn scroll-right" onclick="scrollCards('newArrivals', 1)">
                        &#8250;
                    </button>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">${newArrivalsMessage}</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Best-Sellers -->
        <div class="container mt-5 product-wrapper">
            <h2 class="container mt-5">Best-Sellers</h2>
            <c:choose>
                <c:when test="${not empty bestSellers}">
                    <button class="scroll-btn scroll-left d-none d-md-flex" onclick="scrollCards('bestSellers', -1)">
                        &#8249;
                    </button>
                    <div class="product-container" id="bestSellers">
                        <c:forEach items="${bestSellers}" var="p">
                            <div class="product-card position-relative">
                                <c:if test="${p.discount > 0}">
                                    <span class="discount-badge">${p.discount}%</span>
                                </c:if>
                                <img src="${p.image}" class="product-img" alt="${p.pro_name}" />
                                <div class="card-body">
                                    <a class="text-decoration-none text-dark" href="detail?id=${p.pro_id}">
                                        <h5 class="card-title">${p.pro_name}</h5>
                                    </a>
                                    <div>
                                        <c:if test="${p.discount > 0}">
                                            <div class="discount-price">${p.formattedDiscountedPrice} VND</div>
                                            <div class="original-price text-decoration-line-through">${p.formattedPrice} VND</div>
                                        </c:if>
                                        <c:if test="${p.discount == 0}">
                                            <div class="discount-price">${p.formattedPrice} VND</div>
                                        </c:if>
                                    </div>
                                    <div>
                                        <form method="post" action="cartServlet?id=${p.pro_id}" >
                                            <input type="hidden" name="pro_id" value="${p.pro_id}"/>
                                            <input type="hidden" name="action" value="add" />
                                            <button type="submit">Buy</button>
                                        </form>       
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <button class="scroll-btn scroll-right" onclick="scrollCards('bestSellers', 1)">
                        &#8250;
                    </button>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">${bestSellersMessage}</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Discounted Products -->
        <div class="container mt-5 product-wrapper">
            <h2 class="container mt-5">Discounted Products</h2>
            <c:choose>
                <c:when test="${not empty discountedProducts}">
                    <button class="scroll-btn scroll-left d-none d-md-flex" onclick="scrollCards('discountedProducts', -1)">
                        &#8249;
                    </button>
                    <div class="product-container" id="discountedProducts">
                        <c:forEach items="${discountedProducts}" var="p">
                            <div class="product-card position-relative">
                                <c:if test="${p.discount > 0}">
                                    <span class="discount-badge">${p.discount}%</span>
                                </c:if>
                                <img src="${p.image}" class="product-img" alt="${p.pro_name}" />
                                <div class="card-body">
                                    <a class="text-decoration-none text-dark" href="detail?id=${p.pro_id}">
                                        <h5 class="card-title">${p.pro_name}</h5>
                                    </a>
                                    <div>
                                        <c:if test="${p.discount > 0}">
                                            <div class="discount-price">${p.formattedDiscountedPrice} VND</div>
                                            <div class="original-price text-decoration-line-through">${p.formattedPrice} VND</div>
                                        </c:if>
                                        <c:if test="${p.discount == 0}">
                                            <div class="discount-price">${p.formattedPrice} VND</div>
                                        </c:if>
                                    </div>
                                    <div>
                                        <form action="cartServlet?id=${p.pro_id}" method="post">
                                            <input type="hidden" name="pro_id" value="${p.pro_id}"/>
                                            <input type="hidden" name="action" value="add" />
                                            <button type="submit">Buy</button>
                                        </form>       
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <button class="scroll-btn scroll-right" onclick="scrollCards('discountedProducts', 1)">
                        &#8250;
                    </button>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">${discountedProductsMessage}</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Recommend Products -->
        <div class="container mt-5 product-wrapper">
            <h2 class="container mt-5">Recommend Products</h2>
            <c:choose>
                <c:when test="${not empty recommendProducts}">
                    <button class="scroll-btn scroll-left d-none d-md-flex" onclick="scrollCards('recommendProducts', -1)">
                        &#8249;
                    </button>
                    <div class="product-container" id="recommendProducts">
                        <c:forEach items="${recommendProducts}" var="p">
                            <div class="product-card position-relative">
                                <c:if test="${p.discount > 0}">
                                    <span class="discount-badge">${p.discount}%</span>
                                </c:if>
                                <img src="${p.image}" class="product-img" alt="${p.pro_name}" />
                                <div class="card-body">
                                    <a class="text-decoration-none text-dark" href="detail?id=${p.pro_id}">
                                        <h5 class="card-title">${p.pro_name}</h5>
                                    </a>
                                    <div>
                                        <c:if test="${p.discount > 0}">
                                            <div class="discount-price">${p.formattedDiscountedPrice} VND</div>
                                            <div class="original-price text-decoration-line-through">${p.formattedPrice} VND</div>
                                        </c:if>
                                        <c:if test="${p.discount == 0}">
                                            <div class="discount-price">${p.formattedPrice} VND</div>
                                        </c:if>
                                    </div>
                                    <div>
                                        <form action="cartServlet?id=${p.pro_id}" method="post">
                                            <input type="hidden" name="pro_id" value="${p.pro_id}"/>
                                            <input type="hidden" name="action" value="add" />
                                            <button type="submit">Buy</button>
                                        </form>       
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <button class="scroll-btn scroll-right" onclick="scrollCards('recommendProducts', 1)">
                        &#8250;
                    </button>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">${recommendProductsMessage}</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- footer -->
        <jsp:include page="../common/layout/footer.jsp"></jsp:include>

        <script>
            function scrollCards(containerId, direction) {
                const container = document.getElementById(containerId);
                if (container) {
                    container.scrollBy({left: direction * 250, behavior: "smooth"});
                }
            }
        </script>

        <title>JSP Page</title>
    </head>
</html>
