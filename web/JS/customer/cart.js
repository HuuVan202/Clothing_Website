
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
                var row = form.closest('tr');
                var price = parseFloat(row.find('.price_product').text().replace(/[^\d.]/g, ''));
                var quantity = parseInt(form.find('.quantity-input').val());
                var total = price * quantity;

                row.find('.total_product')
                        .text(total.toLocaleString('vi-VN') + ' VND')
                        .data('value', total);

                updateCartTotal();
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert('Có lỗi xảy ra khi cập nhật số lượng');
            }
        });
    });

    $('.delete-btn').on('click', function () {
        if (confirm("Do you want to remove the product from your shopping cart?")) {
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

                    // Kiểm tra nếu giỏ hàng trống thì hiển thị thông báo
                    if ($('table tr').length === 1) { // Chỉ còn header
                        location.reload(); // Hoặc hiển thị giỏ hàng trống
                    }
                },
                error: function (xhr, status, error) {
                    console.log("Status: " + status);
                    console.log("Error: " + error);
                    console.log("Response: " + xhr.responseText);

                    let errorMsg = "Có lỗi xảy ra khi xóa sản phẩm\n";
                    errorMsg += "Chi tiết: " + (xhr.responseText || error);

                    alert(errorMsg);
                }
            });
        }
    });
});

// Hàm cập nhật tổng tiền toàn bộ giỏ hàng
function updateCartTotal() {
    var grandTotal = 0;

    $('.total_product').each(function () {
        grandTotal += parseFloat($(this).data('value')) || 0;
    });

    $('h3:contains("TOTAL:")').text('TOTAL: ' + grandTotal.toLocaleString('vi-VN') + ' VND');
}

// Hàm cập nhật số thứ tự sau khi xóa
function updateRowNumbers() {
    $('table tr:gt(0)').each(function (index) {
        $(this).find('.no_product').text(index + 1);
    });
}

