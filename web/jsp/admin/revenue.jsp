<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Analyze Revenue</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css" rel="stylesheet" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/admin/managerLayout.css"/>
        
    </head>
    <body>
        <div class="d-flex h-100">
            <!-- Sidebar -->
            <jsp:include page="../common/layout/managerSidebar.jsp"></jsp:include>

            <!-- Main Content -->
            <div class="flex-grow-1">
                <!-- Header -->
                <jsp:include page="../common/layout/managerHeader.jsp"></jsp:include>

                <!-- Content Area -->
                <div class="content-area">
                    <h2 class="order-title"><i class="bi bi-graph-up"></i> Revenue</h2>

                    <!-- Summary content -->
                    <div class="filter-row flex-column flex-md-row align-items-center gap-2 gap-md-1">
                        <h2 class="text-center">Summary</h2>

                        <div class="d-flex flex-column align-items-center w-100">
                            <div class="d-flex justify-content-end w-75 mb-3">
                                <select class="form-select" id="summaryTimeRange" style="width: 180px">
                                    <option selected disabled value="">Select Time Range</option>
                                    <option value="today">Today</option>
                                    <option value="last7days">Last 7 days</option>
                                    <option value="last30days">Last 30 days</option>
                                    <option value="last3months">Last 3 months</option>
                                    <option value="last6months">Last 6 months</option>
                                    <option value="lastyear">Last year</option>
                                </select>
                            </div>

                            <!-- Table content -->
                            <div class="row mt-4 w-75">
                                <div class="col-md-4">
                                    <div class="card text-white bg-primary">
                                        <div class="card-header">Orders</div>
                                        <div class="card-body" id="orders">
                                            <h5 class="card-title">Loading...</h5>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="card text-white bg-success">
                                        <div class="card-header">Sold Products</div>
                                        <div class="card-body" id="soldProducts">
                                            <h5 class="card-title">Loading...</h5>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="card text-white bg-warning">
                                        <div class="card-header">Revenue Summary</div>
                                        <div class="card-body" id="revenueSummary">
                                            <h5 class="card-title">Loading...</h5>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <hr />
                    <!-- Chart Table -->
                    <div class="chart-container align-items-center text-center">
                        <h2 class="">Revenue Chart</h2>

                        <div class="d-flex flex-column align-items-center w-100">
                            <div class="d-flex justify-content-end w-100 mb-3">
                                <select class="form-select" id="chartTimeRange" style="width: 180px">
                                    <option selected disabled value="">Select Time Range</option>
                                    <option value="last3months">Last 3 months</option>
                                    <option value="last6months">Last 6 months</option>
                                    <option value="lastyear">Last year</option>
                                </select>
                            </div>
                            <canvas id="revenueChart" width="400" height="200"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="${pageContext.request.contextPath}/JS/admin/revenue.js"></script>
    </body>
</html>