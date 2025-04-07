<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Chat</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css" rel="stylesheet" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"/>
        <link rel="stylesheet" href="CSS/admin/chat.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/admin/managerLayout.css"/>


    </head>
    <body class="">
        <div class="d-flex h-100">
            <!-- Sidebar -->
            <jsp:include page="../common/layout/managerSidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="flex-grow-1">
                    <!-- Header -->
                <jsp:include page="../common/layout/managerHeader.jsp"></jsp:include>

                    <!-- Content Area -->
                    <div class="content-area">

                        <!-- Khu vực chat -->
                        <div class="chat-container">

                            <!-- Danh sách khách hàng -->
                            <div class="customer-list">
                                <h2>Customers</h2>
                            <c:choose>
                                <c:when test="${empty customerIDs}">
                                    <p class="text-center text-gray-500 p-4">No customers have chatted yet.</p>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="customerID" items="${customerIDs}">
                                        <div class="customer-item ${customerID == selectedCustomerID ? 'active' : ''}">
                                            <a href="${pageContext.request.contextPath}/Chat?customerID=${customerID}">
                                                ${customerNames[customerID]}
                                            </a>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Khu vực chat -->
                        <div class="chat-area">
                            <div class="chat-header">
                                <c:choose>
                                    <c:when test="${selectedCustomerID != null}">
                                        Chat with ${customerNames[selectedCustomerID]}
                                    </c:when>
                                    <c:otherwise>
                                        Select a customer to start chatting
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div id="chatMessages" class="chat-messages">
                                <c:choose>
                                    <c:when test="${empty chats}">
                                        <p class="text-center text-gray-500">No messages yet.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="today" value="<%= new java.util.Date()%>" />
                                        <fmt:formatDate value="${today}" pattern="yyyy-MM-dd" var="todayDate" />
                                        <c:set var="previousDate" value="" />
                                        <c:forEach var="chat" items="${chats}">
                                            <fmt:formatDate value="${chat.sentDate}" pattern="yyyy-MM-dd" var="currentDate" />
                                            <c:if test="${currentDate != todayDate && currentDate != previousDate}">
                                                <div class="text-center text-gray-500 my-2">
                                                    <fmt:formatDate value="${chat.sentDate}" pattern="dd/MM/yyyy" />
                                                </div>
                                                <c:set var="previousDate" value="${currentDate}" />
                                            </c:if>
                                            <c:if test="${currentDate == todayDate && currentDate != previousDate}">
                                                <div class="text-center text-gray-500 my-2">
                                                    Today
                                                </div>
                                                <c:set var="previousDate" value="${currentDate}" />
                                            </c:if>
                                            <div class="mb-4 ${chat.senderID == 1 ? 'text-right' : 'text-left'}">
                                                <c:choose>
                                                    <c:when test="${chat.senderID == sessionScope.admin.accountID}">
                                                        <div class="inline-block">
                                                            <p>${chat.messageContent}</p>
                                                            <span class="text-xs">
                                                                <fmt:formatDate value="${chat.sentDate}" pattern="HH:mm"/>
                                                                <c:choose>
                                                                    <c:when test="${chat.isSeen}">
                                                                        <span class="ml-2">Viewed</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="ml-2">Received</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </span>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="inline-block">
                                                            <p>${chat.messageContent}</p>
                                                            <span class="text-xs">
                                                                <fmt:formatDate value="${chat.sentDate}" pattern="HH:mm"/>
                                                            </span>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="chat-input">
                                <form id="chatForm" action="${pageContext.request.contextPath}/Chat" method="POST">
                                    <!-- Add hidden field for customerID if needed (for admin/staff users) -->
                                    <c:if test="${sessionScope.admin.role == 'admin' || sessionScope.admin.role == 'staff'}">
                                        <c:if test="${not empty param.customerID}">
                                            <input type="hidden" name="customerID" value="${param.customerID}" />
                                        </c:if>
                                    </c:if>
                                    <div class="flex">
                                        <input type="text" name="messageContent" id="messageContent" class="flex-1 p-2 border rounded-l-lg" 
                                               placeholder="Enter message..." required autocomplete="off">
                                        <button type="submit" class="bg-blue-500 text-white p-2 rounded-r-lg">
                                            <i class="fas fa-paper-plane"></i>
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
            const contextPath = '${pageContext.request.contextPath}';
            const chatUrl = contextPath + '/Chat';
            let selectedCustomerID = '${selectedCustomerID}' || null;

            // Open/close chat when clicking the chat button
            document.getElementById('chatButton')?.addEventListener('click', (e) => {
                e.stopPropagation();
                document.getElementById('chatContainer').classList.toggle('show');
                loadMessages();
            });

            // Close chat when clicking the close button
            document.getElementById('closeChat')?.addEventListener('click', (e) => {
                e.stopPropagation();
                document.getElementById('chatContainer').classList.remove('show');
            });

            // Close chat when clicking outside
            document.addEventListener('click', (e) => {
                const chatContainer = document.getElementById('chatContainer');
                const chatButton = document.getElementById('chatButton');
                if (!chatContainer.contains(e.target) && e.target !== chatButton && chatContainer.classList.contains('show')) {
                    chatContainer.classList.remove('show');
                }
            });

            // Function to scroll to the last message
            function scrollToBottom() {
                const chatMessages = document.getElementById('chatMessages');
                if (chatMessages) {
                    chatMessages.scrollTop = chatMessages.scrollHeight;
                }
            }

            // Function to load messages and scroll to the bottom
            function loadMessages() {
                fetch(`${chatUrl}?customerID=${selectedCustomerID}`, {
                            method: 'GET',
                            credentials: 'same-origin'
                        })
                                .then(response => {
                                    if (!response.ok)
                                        throw new Error('Error loading messages');
                                    return response.text();
                                })
                                .then(data => {
                                    const parser = new DOMParser();
                                    const doc = parser.parseFromString(data, 'text/html');
                                    const newMessages = doc.querySelector('#chatMessages');
                                    if (newMessages) {
                                        document.getElementById('chatMessages').innerHTML = newMessages.innerHTML;
                                        scrollToBottom();
                                    }
                                })
                                .catch(error => console.error('Error loading messages:', error));
                    }

// Function to send messages
                    document.getElementById('chatForm')?.addEventListener('submit', function (e) {
                        e.preventDefault();

                        const messageContent = document.getElementById('messageContent').value.trim();
                        if (!messageContent) {
                            Swal.fire({icon: 'warning', title: 'Oops...', text: 'Please enter a message!'});
                            return;
                        }

                        const formData = new URLSearchParams();
                        formData.append('messageContent', messageContent);
                        formData.append('customerID', selectedCustomerID);

                        fetch(chatUrl, {
                            method: 'POST',
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                            body: formData,
                            credentials: 'same-origin'
                        })
                                .then(response => response.json())
                                .then(data => {
                                    if (data.success) {
                                        document.getElementById('messageContent').value = '';
                                        loadMessages();
                                    } else {
                                        Swal.fire({icon: 'error', title: 'Error', text: data.message || 'Failed to send message!'});
                                    }
                                })
                                .catch(error => {
                                    console.error('Error sending message:', error);
                                    Swal.fire({icon: 'error', title: 'Error', text: 'An error occurred while sending the message!'});
                                });
                    });

// Auto-scroll on page load
                    document.addEventListener('DOMContentLoaded', function () {
                        if (document.getElementById('chatContainer').classList.contains('show')) {
                            loadMessages();
                        }
                        scrollToBottom();
                    });
        </script>
    </body>
</html>