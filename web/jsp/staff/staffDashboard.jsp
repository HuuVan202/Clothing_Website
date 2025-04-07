
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
            />
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css"
            rel="stylesheet"
            />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/staff/assistantLayout.css"/>
    </head>
    <body>
        <div class="d-flex h-100">
            <!-- Sidebar -->
             <jsp:include page="../common/layout/staffSidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="flex-grow-1">
                    <!-- Header -->
                <jsp:include page="../common/layout/staffHeader.jsp"></jsp:include>

                <!-- Content Area -->
                <div class="content-area">
                    <h2 class="mb-4">
                        <i class="bi bi-display"></i> Welcome to Dashboard
                    </h2>
                    <!-- Add your dashboard content here -->
                    <p>This is the main content area of your dashboard.</p>
                    <p>
                        You can add your dashboard widgets, charts, and other content here.
                    </p>
                    <p>Feel free to customize this dashboard to suit your needs.</p>
                </div>
            </div>
        </div>
    </body>
</html>
