<%-- 
    Document   : orderdetails
    Created on : Mar 2, 2025, 10:36:12 PM
    Author     : Admin
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order Detail</title>
        <!-- Link to Bootstrap CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/common/layout/layout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/home.css"/>
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
            crossorigin="anonymous"
            />
    </head>
    <body class="bg-light">

        <jsp:include page="../common/layout/header.jsp"></jsp:include>
            <div class="container mt-5">
                <!-- Header -->
                <div class="text-center mb-4">
                    <h2 class="text-primary font-weight-bold">Products Purchased</h2>
                </div>

                <!-- Order Detail Table -->
                <div class="card shadow-sm">
                    <div class="card-body">
                        <table class="table table-bordered text-center">
                            <thead style="background-color: #2d336b; color: white">
                                <tr>
                                    <th>Picture</th>
                                    <th>Product Name</th>
                                    <th>Quantity</th>
                                    <th>Price</th>
                                    <th>Total</th>    
                                </tr>
                            </thead>
                            <tbody>
                            <c:set var="grandTotal" value="0" scope="page"/>
                            <c:set var="formattedGrandTotal" value="" scope="page"/>

                            <c:forEach items="${requestScope.listOrderDetail}" var="o" varStatus="loop">
                                <c:set var="p" value="${requestScope.listProduct[o.pro_id]}"/>
                                <tr>
                                    <td><img src="${p.image}" alt="Product Image" width="100" height="100" class="img-thumbnail"></td>
                                    <td><a style="color: black" href="detail?id=${o.pro_id}">${p.pro_name}</a></td>
                                    <td>${o.quantity}</td>
                                    <td>${o.getFormattedPrice(p.salePrice)} VND</td>
                                    <td>${o.getFormattedPrice(p.salePrice * o.quantity)} VND</td>
                                </tr>
                                <c:set var="grandTotal" value="${grandTotal + (o.quantity * p.salePrice)}" scope="page"/>

                                <!-- Nếu là phần tử cuối cùng, định dạng grandTotal -->
                                <c:if test="${loop.last}">
                                    <c:set var="formattedGrandTotal" value="${o.getFormattedPrice(grandTotal)}" scope="page"/>
                                </c:if>
                            </c:forEach>

                            <tr>
                                <td colspan="4" style="text-align: right;"><strong>Total Price:</strong></td>
                                <td><strong>${formattedGrandTotal} VND</strong></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Back Button -->
            <div class="text-center mt-4">
                <button onclick="history.back()" class="btn btn-primary btn-lg" style="background: #2d336b">Back</button>
            </div>
        </div>

        <!-- footer -->
        <jsp:include page="../common/layout/footer.jsp"></jsp:include>
        <!-- Optional JavaScript and Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.0.7/dist/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
