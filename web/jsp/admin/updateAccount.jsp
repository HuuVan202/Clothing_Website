<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Account</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <div class="card shadow-lg p-4">
                <h2 class="text-center mb-4"><i class="bi bi-person-circle"></i> Update Account


                </h2>


                <form action="updateAccountManagement" method="post">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label">Customer ID:</label>
                            <input type="text" name="cus_id" class="form-control" value="${account[0].cus_id}" readonly>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Name:</label>
                            <input type="text" name="cus_name" class="form-control" value="${account[0].cus_name}" readonly>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Username:</label>
                            <input type="text" name="username" class="form-control" value="${account[1].username}" readonly>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Email:</label>
                            <input type="email" name="email" class="form-control" value="${account[0].email}" required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Phone:</label>
                            <input type="text" name="phone" class="form-control" value="${account[0].phone}" required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Address:</label>
                            <input type="text" name="address" class="form-control" value="${account[0].address}" required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Password:</label>
                            <input type="password" name="password" class="form-control" placeholder="••••••••" readonly>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Role:</label>
                            <input type="text" name="role" class="form-control" value="${account[1].role}" readonly>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Status:</label>
                            <select name="acc_status" class="form-select">
                                <option value="ACTIVE" ${account[1].acc_status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                                <option value="INACTIVE" ${account[1].acc_status == 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>

                        <div class="col-12 text-center mt-4">
                            <button type="submit" class="btn btn-primary px-4"><i class="bi bi-save"></i> Update</button>
                            <a href="accountHome" class="btn btn-secondary px-4"><i class="bi bi-arrow-left"></i> Cancel</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
