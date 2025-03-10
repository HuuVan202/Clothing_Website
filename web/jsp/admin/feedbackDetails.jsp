<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Feedback Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <style>
        .star {
            color: gold;
            font-size: 24px;
        }
        .comment-box {
            border: 2px solid #ccc;
            padding: 15px;
            border-radius: 5px;
            min-height: 100px;
            font-size: 18px;
            text-align: center;
            background: #f8f8f8;
            font-style: italic;
        }
        .product-img {
            width: 50px;
            height: 50px;
            object-fit: cover;
        }
    </style>
</head>
<body class="container mt-4">
    <h2 class="text-center">Feedback Details</h2>

    <c:forEach var="feedback" items="${feedbackList}">
        <!-- Feedback ID -->
        <h4>Feedback :</h4>

        <!-- Product Details Table -->
        <table class="table table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Product</th>
                    <th>Size</th>
                    <th>Gender</th>
                    <th>Brand</th>
                    <th>Price</th>
                    <th>Discount</th>
                    <th>Stock</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>${feedback[2].pro_id}</td>
                    <td>
                        <a style="text-decoration: none ;color: black" href="viewFeedbackDetails?product_name=${feedback[2].pro_name} ">
                            <img src="${feedback[2].image}" alt="Product Image" class="product-img">
                            ${feedback[2].pro_name}
                        </a>
                    </td>
                    <td>${feedback[2].size}</td>
                    <td>${feedback[2].gender}</td>
                    <td>${feedback[2].brand}</td>
                    <td>${feedback[2].price} $</td>
                    <td>${feedback[2].discount}%</td>
                    <td>${feedback[2].stock}</td>
                    <td>${feedback[2].status}</td>
                </tr>
            </tbody>
        </table>

        <!-- Customer & Rating -->
        <p><strong>Customer:</strong> ${feedback[1].cus_name}</p>

        <p><strong>Rating:</strong> 
            <c:forEach begin="1" end="${feedback[0].rating}">
                <span class="star">★</span>
            </c:forEach>
            <c:forEach begin="1" end="${5 - feedback[0].rating}">
                <span class="star" style="color: gray;">★</span>
            </c:forEach>
        </p>

        <!-- Customer's Comment -->
        <p> <strong> Comment:</strong>
        <div class="comment-box">
            ${feedback[0].comment}
        </div></p>
       
    </c:forEach>
        <div class="text-end " >
                            <a href="feedbackHome" class="btn btn-secondary px-4"><i class="bi bi-arrow-left" ></i> Back</a>

        </div>
            

</body>
</html>  
