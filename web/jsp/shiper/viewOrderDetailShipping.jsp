<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shipper Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css" rel="stylesheet"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/admin/managerLayout.css"/>
        <style>
            .shipper-card {
                transition: transform 0.3s;
                cursor: pointer;
            }
            .shipper-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 10px 20px rgba(0,0,0,0.1);
            }
            .status-badge {
                font-size: 0.75rem;
                padding: 0.35em 0.65em;
            }
            .map-container {
                height: 300px;
                background-color: #f8f9fa;
                border-radius: 0.375rem;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #6c757d;
            }
            .task-list-item {
                border-left: 3px solid transparent;
                transition: all 0.3s;
            }
            .task-list-item:hover {
                background-color: #f8f9fa;
                border-left-color: #0d6efd;
            }
            .progress-thin {
                height: 6px;
            }
        </style>
    </head>
    <body>

        <div class="d-flex h-100">
            <div class="flex-grow-1 d-flex flex-column" style="overflow-y: auto;">
                <div class="">
                    <div class="card h-100">
                        <div class="card-header bg-white d-flex justify-content-between align-items-center">
                            <h5 class="mb-0"><i class="bi bi-list-task me-2"></i> Order Details</h5>
                        </div>
                        <div class="card-body">

                            <c:choose>
                                <c:when test="${empty orderDetails}">
                                    <div class="alert alert-info">No shipping orders currently</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Name</th>
                                                    <th>Image</th>
                                                    <th>Size</th>
                                                    <th>Quantity</th>
                                                    <th>Unit Price</th>
                                                    <th>Total</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="detail" items="${orderDetails}">
                                                    <tr>
                                                        <td>${detail.order_detail_id}</td>
                                                        <td>${detail.product.pro_name}</td>
                                                        <td>
                                                            <img src="${pageContext.request.contextPath}/${detail.product.image}" 
                                                                 width="80" 
                                                                 alt="${detail.product.pro_name}" 
                                                                 class="img-thumbnail">
                                                        </td>
                                                        <td>${detail.size}</td>
                                                        <td>${detail.quantity}</td>
                                                        <td>
                                                            <fmt:formatNumber value="${detail.price_per_unit}" /> VND
                                                        </td>
                                                        <td>
                                                            <fmt:formatNumber value="${detail.price_per_unit * detail.quantity}" /> VND
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <form action="shippingOrderServlet" method="post"
                                          onsubmit="return confirm('Confirm status change for order #${order.order_id}?')" class="text-end mt-3">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="order_id" value="${order.order_id}">
                                        <button type="submit" name="tracking" value="delivered" class="btn btn-success btn-sm">
                                            <i class="bi bi-check2"></i> Completed
                                        </button>
                                        <button type="submit" name="tracking" value="canceled" class="btn btn-danger btn-sm ms-1">
                                            <i class="bi bi-x-circle"></i> Return
                                        </button>
                                    </form>
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>