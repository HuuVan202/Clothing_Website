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
                    <div class="container row">
                        <form action="orderHistory" method="post" class="d-flex">
                            <input class="form-control me-2 w-50"type="text" name="cus_name" placeholder="Enter name" value="${name}">
                        <button class="btn btn-primary "type="submit">Search</button>
                    </form> 
                </div>
                <hr>
                <div class="container-fluid p-3">
                    <div class="card">
                        <div class="card-header bg-white d-flex justify-content-between align-items-center">
                            <h5 class="mb-0"><i class="bi bi-list-task me-2"></i> History Deliveries</h5>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${empty historyDeli}">
                                    <div class="alert alert-info">No shipping orders history</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-hover align-middle">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>No</th>
                                                    <th>Order ID</th>
                                                    <th>Name</th>
                                                    <th>Phone</th>
                                                    <th>Email</th>
                                                    <th>Address</th>
                                                    <th>Order Date</th>
                                                    <th>Amount</th>
                                                    <th colspan="3" class="text-center">Status</th> 


                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="order" items="${historyDeli}" varStatus="loop">
                                                    <tr>
                                                        <td>${loop.index+1}</td>
                                                        <td>#${order.order_id}</td>
                                                        <td>${order.customer.cus_name}</td>
                                                        <td>${order.customer.phone}</td>
                                                        <td>${order.customer.email}</td>
                                                        <td>${order.customer.address}</td>
                                                        <td><fmt:formatDate value="${order.order_date}" pattern="dd/MM/yyyy"/></td>
                                                        <td><fmt:formatNumber value="${order.total_price}" /> VND</td>
                                                        <td>
                                                            <c:if test="${order.tracking=='delivered'}">
                                                                <span class="badge bg-success text-white">
                                                                    <i class="bi bi-truck me-1"></i> ${order.tracking} 
                                                                </span>
                                                            </c:if>
                                                            <c:if test="${order.tracking=='canceled'}">
                                                                <span class="badge bg-danger text-white">
                                                                    <i class="bi bi-truck me-1"></i> ${order.tracking} 
                                                                </span>
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <button type="button" class="btn btn-sm btn-outline-info"
                                                                    data-bs-toggle="modal"
                                                                    data-bs-target="#orderDetailModal"
                                                                    data-order-id="${order.order_id}">
                                                                <i class="bi bi-eye"></i> View
                                                            </button>
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
        <div class="modal fade" id="orderDetailModal" tabindex="-1" aria-labelledby="orderDetailLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Nội dung AJAX load ở đây -->
                        <div id="orderDetailContent" class="text-center text-muted">
                            <div class="spinner-border" role="status">
                                <span class="visually-hidden">Loading...</span>
                            </div>
                            <p class="mt-2">Loading order detail...</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            var orderDetailModal = document.getElementById('orderDetailModal');
            orderDetailModal.addEventListener('show.bs.modal', function (event) {
                var button = event.relatedTarget;
                var orderId = button.getAttribute('data-order-id');
                var contentDiv = document.getElementById('orderDetailContent');

// Show loading
                contentDiv.innerHTML = `
   <div class="spinner-border" role="status">
       <span class="visually-hidden">Loading...</span>
   </div>
   <p class="mt-2">Loading order detail...</p>
`;

// Gửi AJAX
                fetch('${pageContext.request.contextPath}/orderDetailServlet?id=' + orderId)
                        .then(response => response.text())
                        .then(data => {
                            contentDiv.innerHTML = data;
                        })
                        .catch(error => {
                            contentDiv.innerHTML = '<div class="alert alert-danger">Failed to load order detail.</div>';
                            console.error('Error loading detail:', error);
                        });
            });
        </script>
    </body>
</html>