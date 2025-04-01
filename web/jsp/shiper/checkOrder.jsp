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
            .table-responsive {
                overflow-x: auto;
            }
            .action-buttons {
                white-space: nowrap;
            }
        </style>
    </head>
    <body>
        <div class="d-flex h-100">
            <jsp:include page="../common/layout/manageSibarShiper.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="flex-grow-1 d-flex flex-column" style="overflow-y: auto;">
                    <!-- Shipper Header -->
                    <header class="bg-white shadow-sm p-3 d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-0"><i class="bi bi-truck me-2"></i> Shipper Dashboard</h5>
                        </div>
                    </header>
                    <hr>
                    <div class="container-fluid p-3">
                        <div class="card">
                            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                                <h5 class="mb-0"><i class="bi bi-list-task me-2"></i> Current Deliveries</h5>
                            </div>
                            <div class="card-body">
                            <c:choose>
                                <c:when test="${empty pendingOrders}">
                                    <div class="alert alert-info">No shipping orders currently</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-hover align-middle">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>Order ID</th>
                                                    <th>Name</th>
                                                    <th>Phone</th>
                                                    <th>Email</th>
                                                    <th>Address</th>
                                                    <th>Order Date</th>
                                                    <th>Amount</th>
                                                    <th>Status</th>
                                                    <th colspan="3" class="text-center">Actions</th> 

                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="order" items="${pendingOrders}">
                                                    <tr>
                                                        <td>#${order.order_id}</td>
                                                        <td>${order.customer.cus_name}</td>
                                                        <td>${order.customer.phone}</td>
                                                        <td>${order.customer.email}</td>
                                                        <td>${order.customer.address}</td>
                                                        <td><fmt:formatDate value="${order.order_date}" pattern="dd/MM/yyyy"/></td>
                                                        <td><fmt:formatNumber value="${order.total_price}" /> VND</td>
                                                        <td>
                                                            <span class="badge bg-warning text-dark">
                                                                <i class="bi bi-truck me-1"></i> Pending Delivery
                                                            </span>
                                                        </td>
                                                        <td class="action-buttons">
                                                            <div class="d-flex gap-2">
                                                                <a href="orderDetailServlet?id=${order.order_id}" class="btn btn-sm btn-outline-primary">
                                                                    <i class="bi bi-eye"></i> View
                                                                </a>
                                                                <form action="${pageContext.request.contextPath}/orderServlet" method="post"
                                                                      onsubmit="return confirm('Confirm status change for order #${order.order_id}?')">
                                                                    <input type="hidden" name="action" value="updateStatus">
                                                                    <input type="hidden" name="order_id" value="${order.order_id}">
                                                                    <button type="submit" name="tracking" value="shipping" 
                                                                            class="btn btn-success btn-sm">
                                                                        <i class="bi bi-check-lg"></i> Shipping
                                                                    </button>
                                                                    <button type="submit" name="tracking" value="canceled" 
                                                                            class="btn btn-danger btn-sm ms-1">
                                                                        <i class="bi bi-x-lg"></i> Cancel
                                                                    </button>
                                                                </form>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <hr>
            </div>
        </div>
    </body>
</html>