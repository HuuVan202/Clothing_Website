$(document).ready(function () {
    // Load orders ban đầu khi trang được tải
    loadOrders();
    // Gắn sự kiện cho các input filter
    $("#searchInput").on('input', debounce(loadOrders, 300));
    $("#timeRange").on('change', loadOrders);
    $("#statusFilter").on('change', loadOrders);
    $("#sortSelect").on('change', loadOrders);

    // Hàm debounce để hạn chế số lần gọi API khi người dùng nhập search
    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    // Hàm chính để load orders
    function loadOrders() {
        let search = $("#searchInput").val().trim().toLowerCase();
        let time = $("#timeRange").val() || "all";
        let status = $("#statusFilter").val() || "";
        let sort = $("#sortSelect").val() || "newest";

        console.log("Loading orders with params - Search: " + search + ", Time: " + time + ", Status: " + status + ", Sort: " + sort);

        $.ajax({
            url: "/ClothingShop/orderM",
            type: 'GET',
            data: {
                search: search,
                time: time,
                status: status,
                sort: sort
            },
            beforeSend: function () {
                $('tbody').html('<tr><td colspan="7">Loading...</td></tr>');
            },
            success: function (data) {
                let htmlContent = data.split("<!--JSON_START-->")[0];
                updateOrderTable(htmlContent);
            },
            error: function (xhr, status, error) {
                console.error("Error loading orders:", error);
                $('tbody').html('<tr><td colspan="7">Error loading orders</td></tr>');
            }
        });
    }

    // Hàm cập nhật bảng orders
    function updateOrderTable(htmlContent) {
        let tbody = $('tbody');
        tbody.html(htmlContent || '<tr><td colspan="7">No orders found</td></tr>');

        // Xử lý lại các nút sau khi cập nhật bảng
        $('tbody .btn-details').off('click').on('click', function () {
            let orderId = $(this).closest('tr').find('td:first').text();
            loadOrderDetails(orderId);
        });

        $('tbody .btn-update').off('click').on('click', function () {
            let orderId = $(this).closest('tr').find('td:first').text();
            console.log("Update button clicked for orderId: " + orderId); // Debug log
            loadUpdateDetails(orderId);
        });
    }

    // Hàm load chi tiết order và hiển thị trong modal
    function loadOrderDetails(orderId) {
        $.ajax({
            url: "/ClothingShop/orderM/details",
            type: 'GET',
            data: {orderId: orderId},
            success: function (data) {
                $('#orderDetailsContent').html(data);
                $('#orderDetailsModal').css('display', 'block');
            },
            error: function (xhr, status, error) {
                console.error("Error loading order details:", error);
                $('#orderDetailsContent').html('<p class="text-danger">Error loading order details</p>');
                $('#orderDetailsModal').css('display', 'block');
            }
        });
    }

    // Hàm load chi tiết update và hiển thị trong modal
    function loadUpdateDetails(orderId) {
        console.log("Loading update details for orderId: " + orderId); // Debug log
        $.ajax({
            url: "/ClothingShop/orderM/details",
            type: 'GET',
            data: {orderId: orderId},
            success: function (data) {
                console.log("AJAX success, data received: ", data); // Debug log
                let parser = new DOMParser();
                let doc = parser.parseFromString(data, 'text/html');
                let elements = doc.getElementsByTagName('p');

                let orderIdValue = '';
                let customerValue = '';
                let phoneValue = '';
                let emailValue = '';
                let addressValue = '';
                let paymentMethodValue = '';
                let trackingStatus = '';
                let orderDateValue = '';
                let orderItemsTable = '';
                let totalPriceValue = '';

                // Trích xuất dữ liệu từ các thẻ <p>
                for (let element of elements) {
                    let text = element.textContent.trim();
                    if (text.startsWith('Order ID:'))
                        orderIdValue = text.split(': ')[1];
                    else if (text.startsWith('Customer:'))
                        customerValue = text.split(': ')[1];
                    else if (text.startsWith('Phone:'))
                        phoneValue = text.split(': ')[1];
                    else if (text.startsWith('Email:'))
                        emailValue = text.split(': ')[1];
                    else if (text.startsWith('Address:'))
                        addressValue = text.split(': ')[1];
                    else if (text.startsWith('Payment method:'))
                        paymentMethodValue = text.split(': ')[1];
                    else if (text.startsWith('Tracking status:'))
                        trackingStatus = text.split(': ')[1];
                    else if (text.startsWith('Order date:'))
                        orderDateValue = text.split(': ')[1];
                }

                // Lấy bảng Order Items
                let table = doc.querySelector('table');
                if (table)
                    orderItemsTable = table.outerHTML;

                // Lấy Total Price
                let totalPriceElement = doc.querySelector('p.text-end');
                if (totalPriceElement)
                    totalPriceValue = totalPriceElement.outerHTML;

                // Tạo nội dung cho modal Update
                let content = `
                    <h3>Update Order</h3>
                    <p><strong>Order ID:</strong> ${orderIdValue || 'N/A'}</p>
                    <p><strong>Customer:</strong> ${customerValue || 'N/A'}</p>
                    <p><strong>Phone:</strong> ${phoneValue || 'N/A'}</p>
                    <p><strong>Email:</strong> ${emailValue || 'N/A'}</p>
                    <p><strong>Address:</strong> ${addressValue || 'N/A'}</p>
                    <p><strong>Payment method:</strong> ${paymentMethodValue || 'N/A'}</p>
                    <p><strong>Tracking status:</strong>
                        <select id="updateTrackingStatus" class="form-select w-25">
                            ${getTrackingOptions(trackingStatus)}
                        </select>
                    </p>
                    <p><strong>Order date:</strong> ${orderDateValue || 'N/A'}</p>
                    <h4>Order Items:</h4>
                    ${orderItemsTable || '<p>No items found</p>'}
                    ${totalPriceValue || '<p class="text-end m-3"><strong>Total Price:</strong> N/A</p>'}
                `;
                $('#updateContent').html(content);
                $('#updateModal').css('display', 'block'); // Đảm bảo modal hiển thị
                console.log("Update modal displayed with content: ", content); // Debug log

                // Gắn sự kiện cho nút Update trong modal
                $('#confirmUpdate').off('click').on('click', function () {
                    let newStatus = $('#updateTrackingStatus').val();
                    updateOrderStatus(orderId, newStatus);
                });
            },
            error: function (xhr, status, error) {
                console.error("Error loading update details:", error, "Status:", xhr.status, "Response:", xhr.responseText);
                $('#updateContent').html('<p class="text-danger">Error loading update details</p>');
                $('#updateModal').css('display', 'block');
            }
        });
    }

    // Hàm tạo các tùy chọn cho select dựa trên trạng thái hiện tại
    function getTrackingOptions(currentStatus) {
        let options = '';
        switch (currentStatus.toLowerCase()) {
            case 'processing':
                options += '<option value="processing">Processing</option>';
                options += '<option value="shipping">Shipping</option>';
                options += '<option value="canceled">Canceled</option>';
                break;
            case 'shipping':
                options += '<option value="shipping">Shipping</option>';
                options += '<option value="delivered">Delivered</option>';
                options += '<option value="canceled">Canceled</option>';
                break;
            default:
                options += '<option value="">No update available</option>';
                break;
        }
        return options;
    }

    // Hàm đóng modal
    $('.close').on('click', function () {
        $('#orderDetailsModal').hide();
    });

    // Đóng modal Update
    $('.close-update').on('click', function () {
        $('#updateModal').hide();
    });

    // Đóng modal khi click ngoài
    $(window).on('click', function (event) {
        if (event.target === $('#orderDetailsModal')[0]) {
            $('#orderDetailsModal').hide();
        }
        if (event.target === $('#updateModal')[0]) {
            $('#updateModal').hide();
        }
    });

    // Hàm cập nhật status (sử dụng hàm có sẵn trong DAO)
    function updateOrderStatus(orderId, newStatus) {
        $.ajax({
            url: `/ClothingShop/orderM/updateStatus`,
            type: 'POST',
            data: {orderId: orderId, newStatus: newStatus},
            success: function (response) {
                console.log("Update status response: ", response);
                alert(response); // Hiển thị thông báo từ DAO
                $('#updateModal').hide();
                loadOrders(); // Tải lại danh sách sau khi cập nhật
            },
            error: function (xhr, status, error) {
                console.error("Error updating order status:", error, "Status:", xhr.status, "Response:", xhr.responseText);
                $('#updateContent').html('<p class="text-danger">Error updating order status</p>');
            }
        });
    }
});