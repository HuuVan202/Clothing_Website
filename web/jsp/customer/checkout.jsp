<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Checkout Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/common/layout/layout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/customer/checkout.css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <jsp:include page="../../jsp/common/layout/header.jsp"></jsp:include>

            <div class="container-checkout">
                <div class="column">
                    <h3>My Information</h3>
                <c:if test="${not empty sessionScope.customer}">
                    <table>
                        <tbody>
                            <tr class="customer-name">
                                <td>Full name</td>
                                <td>${sessionScope.customer.cus_name}</td>
                            </tr>
                            <tr class="customer-email">
                                <td>Email</td>
                                <td>${sessionScope.customer.email}</td>
                            </tr>
                            <tr class="customer-phone">
                                <td>Phone</td>
                                <td>${sessionScope.customer.phone}</td>
                            </tr>
                            <tr class="customer-address">
                                <td>Address</td>
                                <td>${sessionScope.customer.address}</td>
                            </tr>
                        </tbody>
                    </table>
                    <form action="profile">
                        <button class="update" type="submit">Update Information</button>
                    </form>



                </c:if>
            </div>
            <div class="column">
                <h3>My Cart</h3>
                <form action="Checkout" method="post">

                    <c:if test="${not empty sessionScope.cart}">
                        <table border="1">
                            <thead>
                                <tr>
                                    <td class="name_product">Name</td>
                                    <td class="img_product">Image</td>
                                    <td class="price_product">Price</td>
                                    <td class="price_product">Size</td>
                                    <td class="quantity_product">Quantity</td>
                                    <td class="total_product">TotalPirce</td>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
                                    <c:forEach var="cartItem" items="${sessionScope.cart.items}">
                                        <tr>
                                            <td class="name_product">${cartItem.product.pro_name}</td>
                                            <td class="img_product"><img src="${cartItem.product.image}" width="100px"></td>
                                            <td class="price_product"><fmt:formatNumber value="${cartItem.product.salePrice}" /> VND</td>
                                            <td class="quantity_product">${cartItem.size}</td>
                                            <td class="quantity_product">${cartItem.quantity}</td>
                                            <td class="total_product"><fmt:formatNumber value="${cartItem.product.salePrice * cartItem.quantity}" /> VND</td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </tbody>
                        </table>
                        <c:set var="total" value="0"/>
                        <c:forEach var="cartItem" items="${sessionScope.cart.items}">
                            <c:set var="total" value="${total + (cartItem.quantity * cartItem.product.salePrice)}"/>
                        </c:forEach>
                        <h3 class="total_price">TOTAL: <fmt:formatNumber value="${total}" /> VND</h3>
                    </c:if>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="payment_method"  value="cash" checked>
                        <label class="form-check-label" for="cash">
                            Cash on Delivery
                        </label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="payment_method" value="bank_transfer">
                        <label class="form-check-label" for="bank_transfer">
                            Banking
                        </label>
                    </div>

                    <input type="hidden" name="amount" value="${total}">
                    <input type="hidden" name="vnp_OrderInfo" value="1">
                    <input type="hidden" name="language" value="vn">

                    <c:if test="${sessionScope.admin.role.toLowerCase() != 'admin'}">
                        <button type="submit" class="checkout">Proceed to Payment</button>
                    </c:if>
                </form>


            </div>

        </form>
    </div>
</div>
<jsp:include page="../common/layout/footer.jsp"></jsp:include>

</body>

</html>


