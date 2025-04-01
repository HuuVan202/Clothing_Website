<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            <jsp:include page="../common/layout/manageSibarShiper.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="flex-grow-1 d-flex flex-column" style="overflow-y: auto;">
                    <!-- Shipper Header -->
                    <header class="bg-white shadow-sm p-3 d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-0"><i class="bi bi-truck me-2"></i> Shipper Dashboard</h5>
                        </div>

                    </header>

                    <!-- Content Area -->   
                    <div class="content-area p-4">
                        <!-- Stats Cards -->
                        <div class="row mb-4">
                            <div class="col-md-3 mb-3">
                                <div class="card shipper-card bg-primary text-white">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div>
                                                <h6 class="card-subtitle mb-2"> Shipping</h6>
                                                <h3 class="card-title mb-0">${shippingCount}</h3>
                                        </div>
                                        <i class="bi bi-box-seam fs-1 opacity-50"></i>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 mb-3">
                            <div class="card shipper-card bg-success text-white">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <h6 class="card-subtitle mb-2">Delivered</h6>
                                            <h3 class="card-title mb-0">${deleveriedCount}</h3>
                                        </div>
                                        <i class="bi bi-check-circle fs-1 opacity-50"></i>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 mb-3">
                            <div class="card shipper-card bg-warning text-dark">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <h6 class="card-subtitle mb-2">Pending Delivery</h6>
                                            <h3 class="card-title mb-0">${pendingCount}</h3>
                                        </div>
                                        <i class="bi bi-arrow-repeat fs-1 opacity-50"></i>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 mb-3">
                            <div class="card shipper-card bg-danger text-white">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <h6 class="card-subtitle mb-2">Canceled</h6>
                                            <h3 class="card-title mb-0">${canceledCount}</h3>
                                        </div>
                                        <i class="bi bi-exclamation-triangle fs-1 opacity-50"></i>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>