<%@page contentType="text/html" pageEncoding="UTF-8"%>

<nav class="navbar navbar-dark px-4 header">
    <div class="container-fluid">
        <span class="navbar-brand">
            <a
                class="nav-link px-2 text-light main-link rounded-3"
                href="${pageContext.request.contextPath}/home"
                >
                Back to Home
            </a>
        </span>

        <div class="dropdown">
            <button
                class="btn dropdown-toggle"
                type="button"
                id="userMenu"
                data-bs-toggle="dropdown"
                >
                <i class="bi bi-person-circle"></i> Staff
            </button>
            <ul class="dropdown-menu dropdown-menu-end">
                <li>
                    <a class="dropdown-item" href="profile"
                       ><i class="bi bi-person"></i> Profile</a
                    >
                </li>
                <li>
                    <a class="dropdown-item" href="Logout"
                       ><i class="bi bi-box-arrow-right"></i> Logout</a
                    >
                </li>
            </ul>
        </div>
    </div>
</nav>