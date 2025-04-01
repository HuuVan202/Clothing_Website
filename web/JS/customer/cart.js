
$(document).ready(function () {
    // Xử lý sự kiện thay đổi số lượng
    $('.quantity-input').on('change', function () {
        var form = $(this).closest('.ajax-form');
        var formData = form.serialize();

        $.ajax({
            url: '/ClothingShop/Cart',
            type: 'POST',
            data: formData,
            success: function (response) {
                // Lấy thông tin hàng hiện tại
                var row = form.closest('tr');

                // Lấy giá từ data attribute để đảm bảo chính xác
                var price = parseFloat(row.find('.price_product').data('price'));
                var quantity = parseInt(form.find('.quantity-input').val());

                // Tính toán tổng tiền cho sản phẩm
                var total = price * quantity;

                // Cập nhật hiển thị và data attribute
                row.find('.total_product')
                        .text(total.toLocaleString('vi-VN') + ' VND')
                        .data('value', total);

                // Cập nhật tổng tiền giỏ hàng
                updateCartTotal();
            },
            error: function (xhr, status, error) {
                console.error("Lỗi: " + error);
                console.error("Phản hồi: " + xhr.responseText);
                alert('Có lỗi xảy ra khi cập nhật số lượng');
            }
        });
    });

    // Xử lý sự kiện xóa sản phẩm
    $('.delete-btn').on('click', function () {
        if (confirm("Bạn có muốn xóa sản phẩm khỏi giỏ hàng không?")) {
            var form = $(this).closest('.ajax-form');
            var formData = form.serialize();

            $.ajax({
                url: '/ClothingShop/Cart',
                type: 'POST',
                data: formData,
                success: function (response) {
                    // Xóa hàng khỏi bảng
                    form.closest('tr').remove();

                    // Cập nhật số thứ tự
                    updateRowNumbers();

                    // Cập nhật tổng tiền
                    updateCartTotal();

                    // Kiểm tra nếu giỏ hàng trống thì reload trang
                    if ($('table tbody tr').length === 0) {
                        location.reload();
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Trạng thái: " + status);
                    console.error("Lỗi: " + error);
                    console.error("Phản hồi: " + xhr.responseText);

                    let errorMsg = "Có lỗi xảy ra khi xóa sản phẩm\n";
                    errorMsg += "Chi tiết: " + (xhr.responseText || error);
                    alert(errorMsg);
                }
            });
        }
    });

    // Khởi tạo giá trị data-value cho tất cả .total_product khi trang tải
    initializeProductTotals();
});

// Hàm khởi tạo data-value cho tất cả total_product
function initializeProductTotals() {
    $('.total_product').each(function () {
        var text = $(this).text();
        var value = parseFloat(text.replace(/[^\d]/g, ''));
        $(this).data('value', value);
    });

    // Đảm bảo mỗi price_product cũng có data-price
    $('.price_product').each(function () {
        var text = $(this).text();
        var value = parseFloat(text.replace(/[^\d]/g, ''));
        $(this).data('price', value);
    });
}

// Hàm cập nhật tổng tiền toàn bộ giỏ hàng
function updateCartTotal() {
    var grandTotal = 0;

    $('.total_product').each(function () {
        var value = $(this).data('value');
        grandTotal += parseFloat(value) || 0;
    });

    $('h3:contains("TOTAL:")').text('TOTAL: ' + grandTotal.toLocaleString('vi-VN') + ' VND');
}

// Hàm cập nhật số thứ tự sau khi xóa
function updateRowNumbers() {
    $('table tbody tr').each(function (index) {
        $(this).find('.no_product').text(index + 1);
    });
}