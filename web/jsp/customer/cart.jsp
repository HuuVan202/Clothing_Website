<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="shop.model.CartItem" %>
<%@ page import="java.math.BigDecimal" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Shopping Cart</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/common/layout/layout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/common/home.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/cart.css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <jsp:include page="../common/layout/header.jsp"></jsp:include>
            <div>
                <h2>My Cart</h2> 
            </div>
        <c:if test="${empty sessionScope.cart or empty sessionScope.cart.items}">
            <div class="empty-cart">
                <img src="${pageContext.request.contextPath}/img/icon/header/empty-cart.png" width="30%" height="30%" alt="Cart-empty"/>
            </div>
        </c:if>
        <c:if test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
            <table>
                <tr>
                    <td class="no_product">No</td>
                    <td class="name_product">Name</td>
                    <td class="img_product">Image</td>
                    <td class="price_product">Price</td>
                    <td class="quantity_product">Quantity</td>
                    <td class="total_product">Total</td>
                    <td class="remove_product">Action</td>
                </tr>
                <c:set var="o" value="${sessionScope.cart}"/>
                <c:forEach items="${o.items}" var="i" varStatus="loop">
                    <tr>
                        <td class="no_product">${loop.index+1}</td>
                        <td class="name_product">${i.product.pro_name}</td>
                        <td class="img_product">
                            <img src="${pageContext.request.contextPath}/${i.product.image}" alt="${i.product.pro_name}" style="height: 100px; width: 100px"/>
                        </td>
                        <td class="price_product"><fmt:formatNumber value="${i.product.salePrice}" /></td>
                        <td class="quantity_product">
                            <form action="${pageContext.request.contextPath}/cartServlet" method="POST">
                                <input type="hidden" name="pro_id" value="${i.product.pro_id}" />
                                <input type="hidden" name="action" value="updateQuantity"/>
                                <input type="number" name="quantity" value="${i.quantity}" min="0" max="${i.product.stock}" onchange="this.form.submit()" />
                            </form>                         
                        </td>
                        <td class="total_product"><fmt:formatNumber value="${i.product.salePrice * i.quantity}" /></td>
                        <td class="remove_product"> 
                            <form action="${pageContext.request.contextPath}/cartServlet" method="POST">
                                <input type="hidden" name="pro_id" value="${i.product.pro_id}" />
                                <input type="hidden" name="action" value="delete"/>
                                <input type="submit" value="Delete" onclick="return confirmDelete();"/>
                            </form>
                        </td>
                    </tr>
                </c:forEach>    
            </table>
            <c:set var="total" value="0"/>
            <c:forEach items="${o.items}" var="i">
                <c:set var="total" value="${total + (i.quantity * i.product.salePrice)}"/>
            </c:forEach>
            <h3 style="font-family: inherit">TOTAL: <fmt:formatNumber value="${total}" /></h3>
            <form action="checkoutServlet" method="post">
                <div class="checkout_btn">
                    <button class="btn btn-success" type="submit"><h3>Check out</h3></button>
                </div>
            </form>
        </c:if>

        <jsp:include page="../common/layout/footer.jsp"></jsp:include>
    </body>
</html>
<script>
    function confirmDelete() {
        return confirm("Do you want to delete product");
    }
</script>