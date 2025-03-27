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
        <style>
            td input {
                border: none;
                outline: none;
                width: 100%;
                background: none;
                padding: 0;
            }

            /* Khi ở chế độ chỉnh sửa */
            td input.editing {
                border: 1px solid #007bff;  /* Viền xanh khi chỉnh sửa */
                background-color: #f0f8ff; /* Màu nền nhạt khi chỉnh sửa */
                padding: 5px; /* Thêm padding để dễ nhìn hơn */
                border-radius: 4px;
            }
            .success {
                color: green; /* Màu chữ xanh */
                background-color: rgba(0, 255, 0, 0.1); /* Nền xanh nhạt */
                border: 2px solid green; /* Viền xanh đậm hơn */
                padding: 10px 15px; /* Khoảng cách bên trong */
                border-radius: 5px; /* Bo tròn góc */
                font-weight: bold; /* Chữ đậm */
                display: inline-block; /* Để nó không bị kéo dài toàn bộ hàng */
                margin-top: 10px
            }

            .error {
                color: red;
                background-color: rgba(255, 0, 0, 0.1);
                border: 1px solid red;
                padding: 10px 15px; /* Khoảng cách bên trong */
                border-radius: 5px; /* Bo tròn góc */
                font-weight: bold; /* Chữ đậm */
                display: inline-block; /* Để nó không bị kéo dài toàn bộ hàng */
                margin-top: 10px
            }
        </style>
    </head>
    <body>
        <jsp:include page="../../jsp/common/layout/header.jsp"></jsp:include>

            <div class="container-checkout">
                <div class="column">
                    <h3>My Information</h3>
                <c:if test="${not empty sessionScope.customer}">
                    <form action="profile" method="POST">
                        <input hidden value="checkout" name="from"/>
                        <input hidden id="txtUserName" name="txtUserName" value="${sessionScope.customer.username}"/>
                        <table>
                            <tbody>
                                <tr class="customer-name">
                                    <td>Full name</td>
                                    <td><input class="editable" value="${sessionScope.customer.cus_name}" id="txtFullName" name="txtFullName" readonly /></td>
                                </tr>
                                <tr class="customer-email">
                                    <td>Email</td>
                                    <td><input value="${sessionScope.customer.email}" type="text" id="txtEmail" name="txtEmail" readonly /></td>
                                </tr>
                                <tr class="customer-phone">
                                    <td>Phone</td>
                                    <td><input class="editable" value="${sessionScope.customer.phone}" id="txtPhone" name="txtPhone" readonly /></td>
                                </tr>
                                <tr class="customer-address">
                                    <td>Address</td>
                                    <td><input class="editable" value="${sessionScope.customer.address}" id="txtAddress" name="txtAddress" readonly /></td>
                                </tr>
                            </tbody>
                        </table>
                        <button class="update" type="button" id="btnUpdate" onclick="toggleEdit()">Update Information</button>
                    </form>      
                    <c:if test="${not empty requestScope.message}">
                        <span class="message ${requestScope.message == 'Update Successful!' || requestScope.message == 'Password Change Successful' ? 'success' : 'error'}">
                            ${requestScope.message}
                        </span>
                    </c:if>
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
                                    <td class="total_product">Total Price</td>
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
<script>
    function toggleEdit() {
        let inputs = document.querySelectorAll(".editable");
        let button = document.getElementById("btnUpdate");
        let form = document.querySelector("form");

        if (button.innerText === "Update Information") {
            // Bỏ readonly và thêm class "editing" để áp dụng CSS
            inputs.forEach(input => {
                input.removeAttribute("readonly");
                input.classList.add("editing");
            });
            button.innerText = "Save";
        } else {
            // Xóa class "editing" để quay về trạng thái mặc định
            inputs.forEach(input => input.classList.remove("editing"));
            form.submit(); // Gửi form
        }
    }
</script>
</body>

</html>


