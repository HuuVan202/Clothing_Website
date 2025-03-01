<%@page contentType="text/html" pageEncoding="UTF-8"%>

<nav class="navbar navbar-dark px-4 header">
    <div class="container-fluid">
        <span class="navbar-brand"
              ><i class="bi bi-people"></i>  Account Management</span
        >
        <div class="dropdown">
            <button
                class="btn dropdown-toggle"
                type="button"
                id="userMenu"
                data-bs-toggle="dropdown"
                >
                <i class="bi bi-person-circle"></i> Admin User
            </button>
            <ul class="dropdown-menu dropdown-menu-end">
                <li>
                    <a class="dropdown-item" href="#"
                       ><i class="bi bi-box-arrow-right"></i> Logout</a
                    >
                </li>
            </ul>
        </div>
    </div>
</nav>