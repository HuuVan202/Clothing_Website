<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            .message {
                color: green;
                justify-content: center;
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

                            <div class="row">

                                <!-- Profile Section -->
                                <div class="col-lg-4 mb-4">
                                    <div class="card">
                                        <div class="card-header">            
                                            <h6 class="mb-0"><i class="bi bi-person-circle"></i> Profile</h6>
                                        </div>
                                        <div class="card-body">
                                            <div class="text-center">
                                                <img src="${pageContext.request.contextPath}/img/icon/header/shipper.png" 
                                                 class="rounded-circle mb-3" width="120" height="120" alt="Shipper Avatar">
                                            <h5>${sessionScope.customer.cus_name}</h5>
                                        </div>

                                        <hr>
                                        <div class="text-start px-3">
                                            <p ><i class="bi bi-envelope me-2"></i> ${sessionScope.customer.email}</p>
                                            <p><i class="bi bi-phone me-2"></i> ${sessionScope.customer.phone}</p>
                                            <p><i class="bi bi-geo-alt me-2"></i> ${sessionScope.customer.address}</p>
                                        </div>

                                        <div class="text-center mt-3">
                                            <button
                                                type="button"
                                                class="btn btn-outline-primary edit-btn" 
                                                data-bs-toggle="modal" 
                                                data-bs-target="#updateModal">
                                                <i class="bi bi-pencil-square"></i> Edit Profile
                                            </button>
                                        </div>
                                    </div>

                                    <div>
                                        <c:if test="${not empty sessionScope.customer}">
                                            <form action="profile" method="POST">
                                                <input hidden value="dashboardcontroller" name="from"/>
                                                <input hidden id="txtUserName" name="txtUserName" value="${sessionScope.customer.username}"/>
                                                <table>
                                                    <tbody>
                                                        <tr class="customer-name">
                                                            <td><input class="editable" value="${sessionScope.customer.cus_name}" id="txtFullName" name="txtFullName" hidden /></td>
                                                        </tr>
                                                        <tr class="customer-email">
                                                            <td><input value="${sessionScope.customer.email}" type="text" id="txtEmail" name="txtEmail" hidden /></td>
                                                        </tr>
                                                        <tr class="customer-phone">
                                                            <td><input class="editable" value="${sessionScope.customer.phone}" id="txtPhone" name="txtPhone" hidden /></td>
                                                        </tr>
                                                        <tr class="customer-address">
                                                            <td><input class="editable" value="${sessionScope.customer.address}" id="txtAddress" name="txtAddress" hidden /></td>
                                                        </tr>
                                                    </tbody>
                                                </table>

                                            </form>      
                                            <c:if test="${not empty requestScope.message}">
                                                <span class="message container ${requestScope.message == 'Update Successful!' || requestScope.message == 'Password Change Successful' ? 'success' : 'error'}">
                                                    ${requestScope.message}
                                                </span>
                                            </c:if>
                                        </c:if> 
                                    </div>
                                    <!-- Modal -->
                                    <div class="modal fade" id="updateModal" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
                                        <div class="modal-dialog">
                                            <div class="modal-content rounded-4 shadow">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="updateModalLabel">Update Information</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <form action="profile" method="POST">
                                                    <div class="modal-body">
                                                        <input type="hidden" name="from" value="dashboardcontroller">
                                                        <input type="hidden" id="txtUserName" name="txtUserName" value="${sessionScope.customer.username}">

                                                        <div class="mb-3">
                                                            <label for="modalFullName" class="form-label">Full Name</label>
                                                            <input type="text" class="form-control" id="modalFullName" name="txtFullName" value="${sessionScope.customer.cus_name}">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="modalEmail" class="form-label">Email</label>
                                                            <input type="email" class="form-control" id="modalEmail" name="txtEmail" value="${sessionScope.customer.email}" readonly>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="modalPhone" class="form-label">Phone</label>
                                                            <input type="text" class="form-control" id="modalPhone" name="txtPhone" value="${sessionScope.customer.phone}">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="modalAddress" class="form-label">Address</label>
                                                            <input type="text" class="form-control" id="modalAddress" name="txtAddress" value="${sessionScope.customer.address}">
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                        <button type="submit" class="btn btn-success">Save Changes</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>



                                </div>
                            </div>


                            <!-- Performance and Activity Section -->
                            <div class="col-lg-8">
                                <div class="card mb-4">
                                    <div class="card-header">
                                        <h6 class="mb-0"><i class="bi bi-speedometer2 me-2"></i> Performance Metrics</h6>
                                    </div>
                                    <div class="card-body">
                                        <div class="row mt-3">
                                            <div class="map-container" style="height: 300px; border: 1px solid #ccc; border-radius: 8px; overflow: hidden;">
                                                <iframe
                                                    src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.502108155313!2d106.70042461524141!3d10.772305992324817!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f3f8a9bbf3f%3A0x47cbef44769396ae!2zQ2jhu6MgVMOibiBUaMOhaSBIw6AgLSBHSUEgVMOibiBUaMOhaQ!5e0!3m2!1sen!2s!4v1614521350304!5m2!1sen!2s"
                                                    width="100%"
                                                    height="100%"
                                                    style="border:0;"
                                                    allowfullscreen=""
                                                    loading="lazy"
                                                    referrerpolicy="no-referrer-when-downgrade">
                                                </iframe>
                                            </div>

                                        </div>
                                    </div>
                                </div>

                                <!-- Recent Activity -->

                            </div>
                        </div>

                        <!-- Add Chart.js for the weekly chart -->
                        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                        <script>
                            function toggleEdit() {
                                let inputs = document.querySelectorAll(".editable");
                                let button = document.getElementById("btnUpdate");
                                let form = document.querySelector("form");

                                if (button.innerText === "Update Information") {
                                    inputs.forEach(input => {
                                        input.removeAttribute("readonly");
                                        input.classList.add("editing");
                                    });
                                    button.innerText = "Save";
                                } else {
                                    inputs.forEach(input => input.classList.remove("editing"));
                                    form.submit(); // Gửi form
                                }
                            }
                            function toggleEdit() {
                                document.getElementById('updateModal').style.display = 'block';
                            }

                            function closeModal() {
                                document.getElementById('updateModal').style.display = 'none';
                            }

                            // Đóng modal khi click ra ngoài
                            window.onclick = function (event) {
                                const modal = document.getElementById('updateModal');
                                if (event.target === modal) {
                                    modal.style.display = "none";
                                }
                            }
                        </script>            
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>