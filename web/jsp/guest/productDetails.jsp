<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="shop.model.Feedback"%>
<%@page import="shop.model.Product"%>
<%@page import="java.util.List"%>



<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Home</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/common/layout/layout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/guest/home.css"/>
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
            crossorigin="anonymous"
            />
        <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"
        ></script>        
        <style>
            .product-container {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 30px;
            }
            .give-feedback {
                background-color: #007bff;
                color: white;
                font-weight: bold;
                border: none;
                padding: 5px 10px;
                cursor: pointer;
                border-radius: 5px;
                margin-top: 5px;
                width: 200px;
                height: 38px;
            }
            .add-to-cart {
                background-color: #28a745;
                color: white;
                border: none;
                padding: 5px 10px;
                cursor: pointer;
                border-radius: 5px;
                margin-top: 5px;
                width: 200px;
                height: 38px;
            }

            .feedback-container {
                width: 50%;
                margin: 20px auto;
                border: 1px solid #ccc;
                padding: 10px;
                border-radius: 10px;
                background-color: #f9f9f9;
                max-height: 200px;
                overflow-y: auto;
            }
            .feedback-item {
                padding: 10px;
                border-bottom: 1px solid #ddd;
                text-align: center;
                display: flex;
                justify-content: space-between;
                align-items: center;
                background: white;
                border-radius: 5px;
                margin-bottom: 5px;
            }
            .feedback-item:last-child {
                border-bottom: none;
            }
            .feedback-details {
                flex: 1;
                text-align: left;
                padding: 5px;
            }
            .feedback-rating {
                font-size: 16px;
                color: gold;
                flex: 0 0 auto;
                padding-right: 10px;
            }
            .feedback-date {
                font-size: 12px;
                color: gray;
                flex: 0 0 auto;
                padding-right: 10px;
            }

            .suggestion-container {
                display: flex;
                justify-content: space-around;
                gap: 15px;
                padding: 10px;
            }
            .suggestion-item {
                flex: 1;
                border: 1px solid #ddd;
                border-radius: 10px;
                padding: 10px;
                text-align: center;
                background-color: #f9f9f9;
                display: flex;
                flex-direction: column;
                align-items: center;
            }
            .suggestion-item img {
                width: 256px;
                height: auto;
                margin-bottom: 10px;
            }
            .suggestion-item .product-name strong {
                font-weight: bold;
            }
            .suggestion-item .product-name {
                flex-grow: 1;
            }
            .suggestion-item .product-price strong {
                font-weight: bold;
            }
            .suggestion-item .product-price {
                margin-top: auto;
                font-size: 16px;
            }

        </style>
    </head>
    <body>
        <!-- header -->
        <jsp:include page="../common/layout/header.jsp"></jsp:include>

            <h1>Product Details</h1>

        <% Product product = (Product) request.getAttribute("product"); %>

        <% if (product != null) { %>
        <div class="product-container">
            <div class="product-image">
                <img src="<%= product.getImage() %>" alt="Product Image" style="width:300px;
                     height:auto;">
            </div>
            <div class="product-info">
                <p><strong>Product ID:</strong> <%= product.getPro_id() %></p>
                <p><strong>Name:</strong> <%= product.getPro_name() %></p>
                <p><strong>Size:</strong> <%= product.getSize() %></p>
                <p><strong>Type:</strong> <%= product.getType_id() %></p>
                <p><strong>Stocks:</strong> <%= product.getStock() %></p>
                <p><strong>Price:</strong> <%= product.getPrice() %> VND</p>
                <p><strong>Feedback:</strong> <%= request.getAttribute("averageRating") %> ⭐ (<%= request.getAttribute("feedbackCount") %>)</p>
                <button class="add-to-cart">Add to Cart</button>
                <button class="give-feedback">Give Feedback</button>
            </div>
        </div>

        <h2>Feedbacks:</h2>
        <div class="feedback-container">
            <%
                List<Feedback> feedbacks = (List<Feedback>) request.getAttribute("feedbacks");
                if (feedbacks != null && !feedbacks.isEmpty()) {
                    for (Feedback f : feedbacks) {
            %>
            <div class="feedback-item">
                <div class="feedback-details">
                    <p><strong><%= f.getCus_name() %></strong></p>
                    <p><%= f.getComment() %></p>
                </div>
                <p class="feedback-rating"><%= f.getRating() %> ⭐</p>
                <p class="feedback-date"><%= f.getFeedback_date() %></p>
            </div>
            <%
                    }
                } else {
            %>
            <p style="text-align: center;">No feedback available.</p>
            <%
                }
            %>
        </div>

        <h2>Suggestion:</h2>
        <div class="suggestion-container">
            <%
                List<Product> suggestions = (List<Product>) request.getAttribute("suggestions");
                if (suggestions != null && !suggestions.isEmpty()) {
                    for (Product p : suggestions) {
            %>
            <div class="suggestion-item">
                <img src="<%= p.getImage() %>" alt="Product Image">
                <p class="product-name"><strong>Product Name:</strong> <%= p.getPro_name() %></p>
                <p class="product-price"><strong>Price:</strong> <%= p.getPrice() %> VND</p>
                <button class="add-to-cart">Add to Cart</button>
            </div>
            <%
                    }
                } else {
            %>
            <p>No suggestion available.</p>
            <%
                }
            %>
        </div>
        <% } else { %>
        <p>Product not found.</p>
        <% } %>

        <!-- footer -->
        <jsp:include page="../common/layout/footer.jsp"></jsp:include>
    </body>
</html>
