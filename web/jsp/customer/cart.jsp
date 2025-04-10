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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/customer/cart.css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="${pageContext.request.contextPath}/JS/customer/cart.js"></script>
    </head>
    <body>
        <jsp:include page="../common/layout/header.jsp"></jsp:include>
            <div>
                <h2 class="container text-start">My Cart</h2> 
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
                    <td class="price_product">Size</td>
                    <td class="quantity_product">Quantity</td>
                    <td class="total_product">Total</td>
                    <td class="remove_product">Action</td>
                </tr>
                <c:set var="o" value="${sessionScope.cart}"/>
                <c:forEach items="${o.items}" var="i" varStatus="loop">
                    <tr>
                        <td class="no_product">${loop.index+1}</td>
                        <td class="name_product"><a href="detail?id=${i.product.pro_id}" style="text-decoration: none;color: black">${i.product.pro_name}</a></td>
                        <td class="img_product">
                            <a href="detail?id=${i.product.pro_id}">
                                <img src="${pageContext.request.contextPath}/${i.product.image}" alt="${i.product.pro_name}" style="height: 100px; width: 100px"/>
                            </a>
                        </td>
                        <td class="price_product"><fmt:formatNumber value="${i.product.salePrice}" /> VND</td>
                        <td>${i.size != null ? i.size : 'Default'}</td>
                        <td class="quantity_product">
                            <form class="ajax-form" onsubmit="return validateQuantity(this, ${i.stock})">
                                <input type="hidden" name="pro_id" value="${i.product.pro_id}" />
                                <input type="hidden" name="size" value="${i.size}" />
                                <input type="hidden" name="action" value="updateQuantity"/>
                                <input type="number" class="quantity-input" 
                                       name="quantity" 
                                       value="${i.quantity}"
                                       min="1" 
                                       max="${i.stock}"
                                       data-original-value="${i.quantity}"
                                       onchange="showError(this, ${i.stock})"
                                       onfocus="this.dataset.originalValue = this.value"/>
                                <div class="error-message"></div>
                            </form>
                        </td>
                        <td class="total_product" data-value="${i.product.salePrice * i.quantity}">
                            <fmt:formatNumber value="${i.product.salePrice * i.quantity}" /> VND
                        </td>
                        <td class="remove_product">
                            <form method="post" action="Cart">
                                <input type="hidden" name="pro_id" value="${i.product.pro_id}" />
                                <input type="hidden" name="size" value="${i.size}" />
                                <input type="hidden" name="action" value="delete"/>
                                <button type="submit" class="delete-btn btn btn-danger">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>    
            </table>
            <c:set var="total" value="0"/>
            <c:forEach items="${o.items}" var="i">
                <c:set var="total" value="${total + (i.quantity * i.product.salePrice)}"/>
            </c:forEach>
            <h3 style="font-family: inherit">TOTAL: <fmt:formatNumber value="${total}" /> VND</h3>
            <div class="checkout_btn">
                <form action="Checkout">
                    <button style="border-radius: 20px" class="btn btn-success" type="submit">
                        <h3>Check Out</h3>
                    </button>
                </form>
            </div>
        </c:if>

        <jsp:include page="../common/layout/footer.jsp"></jsp:include>
            <script>
                function checkStock(input, stock) {
                    var quantity = parseInt(input.value);
                    if (quantity === stock) {
                        alert("You have selected the maximum quantity in stock!");
                    }
                }

                function showError(input, stock) {
                    const errorDiv = input.parentElement.querySelector('.error-message');
                    const quantity = parseInt(input.value) || 0;
                    if (quantity > stock) {
                        input.value = stock;
                        input.classList.add('is-invalid');
                        alert(`Maximum product in stock`);
                        return false;
                    }
                    input.classList.remove('is-invalid');
                    return true;
                }

                function validateQuantity(form, stock) {
                    const quantity = parseInt(form.quantity.value) || 0;
                    if (quantity > stock) {
                        form.quantity.value = stock;
                        form.quantity.classList.add('is-invalid');
                        alert(`Maximum product in stock.`);
                        return false;
                    }
                    form.quantity.classList.remove('is-invalid');
                    return true;
                }

                document.addEventListener('DOMContentLoaded', function () {
                    // Set up quantity inputs
                    document.querySelectorAll('.quantity-input').forEach(input => {
                        input.dataset.originalValue = input.value;
                        input.addEventListener('change', function () {
                            const form = this.closest('form');
                            if (validateQuantity(form, parseInt(this.max))) {
                                form.submit();
                            }
                        });
                    });
                });
        </script>
    </body>
</html>