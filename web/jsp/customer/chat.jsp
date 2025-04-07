<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chat</title>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css" rel="stylesheet" />
        <style>
            .chat-container {
                width: 45%;
                height: 80%;
                position: fixed;
                bottom: 10px;
                right: 10px;
                display: none;
                background: white;
                box-shadow: -2px -2px 15px rgba(0,0,0,0.1);
                border-radius: 10px 10px 0 0;
                z-index: 1000;
            }
            .chat-container.show {
                display: flex;
            }
            .chat-container h2 {
                text-align: center;
                display: block;
                margin: 0 auto;
                width: 100%;
            }

            .chat-input {
                width: 100%;
                padding: 10px;
                border-top: 1px solid #eee;
            }
            .chat-container {
                border-radius: 12px; /* Bo góc 12px */
                overflow: hidden; /* Đảm bảo nội dung bên trong không bị tràn */
            }

            @media (max-width: 768px) {
                .chat-container {
                    width: 100%;
                    height: 90%;
                }
            }
        </style>
    </head>
    <body>
        <div class="fixed bottom-4 right-4 z-50">
            <button id="chatButton" class="bg-blue-500 text-white p-2 rounded-full shadow-lg">
                <i class="bi bi-chat-dots-fill"></i>
                <span class="ml-1">Chat</span>
            </button>
        </div>

        <div id="chatContainer" class="chat-container flex flex-col">
            <div class="bg-blue-500 p-1 border-b flex justify-between items-center">
                <h2 class="text-white font-bold">Clothing Shop</h2>
                <button id="closeChat" class="text-white text-xl font-bold mr-2">x</button>
            </div>

            <div id="chatMessages" class="flex-1 p-4 overflow-y-auto bg-gray-100">
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
                            <div class="mb-4 ${chat.senderID == sessionScope.admin.accountID ? 'text-right' : 'text-left'}">
                                <c:choose>
                                    <c:when test="${chat.senderID == sessionScope.admin.accountID}">
                                        <div class="inline-block p-3 rounded-lg ${chat.senderID == sessionScope.admin.accountID ? 'bg-blue-400 text-dark text-left' : 'bg-white'}">
                                            <p>${chat.messageContent}</p>
                                            <span class="text-xs">
                                                <fmt:formatDate value="${chat.sentDate}" pattern="HH:mm"/>
                                                <c:choose>
                                                    <c:when test="${chat.isSeen}">
                                                        <span class="ml-2">Seen</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="ml-2">Sent</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="inline-block p-3 rounded-lg ${chat.senderID == sessionScope.admin.accountID ? 'bg-orange-500 text-white text-left' : 'bg-white'}">
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
                <form id="chatForm" action="Chat" method="POST">
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

        <script>
            // Lấy context path từ request thay vì hardcode
            const contextPath = '${pageContext.request.contextPath}';

            // Tạo base URL cho các request
            const chatUrl = contextPath + '/Chat';

            // Open/close chat when clicking the chat button
            document.getElementById('chatButton').addEventListener('click', (e) => {
                e.stopPropagation();
                document.getElementById('chatContainer').classList.toggle('show');
                loadMessages();
            });

            // Close chat when clicking the close button
            document.getElementById('closeChat').addEventListener('click', (e) => {
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

            // Handle form submission with fetch to avoid page reload
            document.getElementById('chatForm').addEventListener('submit', function (e) {
                e.preventDefault();

                const messageContent = document.getElementById('messageContent').value.trim();
                if (!messageContent) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Oops...',
                        text: 'Please enter a message!'
                    });
                    return;
                }

                // Create FormData from the form
                const formData = new FormData(this);

                // Convert FormData to URLSearchParams for application/x-www-form-urlencoded
                const data = new URLSearchParams();
                for (const pair of formData.entries()) {
                    data.append(pair[0], pair[1]);
                }

                // Send the request
                fetch(this.action, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: data,
                    credentials: 'same-origin'
                })
                        .then(response => {
                            if (!response.ok) {
                                if (response.status === 401) {
                                    Swal.fire({
                                        icon: 'info',
                                        title: 'Login Required',
                                        text: 'Please login to send messages!',
                                        confirmButtonText: 'Go to Login'
                                    }).then(() => {
                                        window.location.href = contextPath + '/Login';
                                    });
                                    throw new Error('Not logged in');
                                } else {
                                    return response.json().then(data => {
                                        throw new Error(data.message || 'Server error');
                                    });
                                }
                            }
                            return response.json();
                        })
                        .then(data => {
                            if (data.success) {
                                // Clear input and reload messages
                                document.getElementById('messageContent').value = '';
                                loadMessages();
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Message Failed',
                                    text: data.message || 'Unable to send message!'
                                });
                            }
                        })
                        .catch(error => {
                            if (error.message !== 'Not logged in') {
                                console.error('Error:', error);
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: 'An error occurred while sending the message: ' + error.message
                                });
                            }
                        });
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
                fetch(chatUrl, {
                    method: 'GET',
                    credentials: 'same-origin' // Đảm bảo gửi kèm cookies/session
                })
                        .then(response => {
                            if (!response.ok) {
                                if (response.status === 401) {
                                    throw new Error('Not logged in');
                                }
                                throw new Error('Error loading messages');
                            }
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
                        .catch(error => {
                            if (error.message === 'Not logged in') {
                                Swal.fire({
                                    icon: 'info',
                                    title: 'Login Required',
                                    text: 'Please login to view messages!',
                                    confirmButtonText: 'Go to Login'
                                }).then(() => {
                                    window.location.href = contextPath + '/Login';
                                });
                            } else {
                                console.error('Error loading messages:', error);
                            }
                        });
            }

            // Tự động load tin nhắn khi trang được tải
            document.addEventListener('DOMContentLoaded', function () {
                // Nếu chat container đang hiển thị, load tin nhắn
                if (document.getElementById('chatContainer').classList.contains('show')) {
                    loadMessages();
                }

                // Auto scroll to bottom when page loads
                scrollToBottom();
            });
        </script> 
    </body>
</html>