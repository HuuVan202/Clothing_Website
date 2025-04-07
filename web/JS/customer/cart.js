$(document).ready(function () {
    // Handle quantity change event
    $('.quantity-input').on('change', function () {
        var form = $(this).closest('.ajax-form');
        var stock = parseInt($(this).attr('max'));

        // Validate quantity before sending AJAX request
        if (!validateQuantity(form[0], stock)) {
            return;
        }

        var formData = form.serialize();
        var currentRow = form.closest('tr');
        var quantityInput = $(this);
        var newQuantity = parseInt(quantityInput.val());

        // Get price from price cell text (strip "VND" and formatting)
        var priceCell = currentRow.find('.price_product').eq(0);
        var priceText = priceCell.text().replace('VND', '').trim();
        var price = parseFloat(priceText.replace(/\./g, ''));

        // Prevent default form submission behavior
        form.on('submit', function (e) {
            e.preventDefault();
            return false;
        });

        $.ajax({
            url: '/ClothingShop/Cart',
            type: 'POST',
            data: formData,
            success: function (response) {
                // Calculate the new total
                var newTotal = price * newQuantity;

                // Update the total cell with the exact same format as JSP
                var totalCell = currentRow.find('.total_product');
                totalCell.attr('data-value', newTotal);

                // Format exactly like fmt:formatNumber in JSP
                var formattedTotal = formatNumberExactly(newTotal) + " VND";
                totalCell.text(formattedTotal);

                // Update the cart total
                updateCartTotal();
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
                alert('An error occurred while updating the quantity');
                // Reset to original value on error
                quantityInput.val(quantityInput.attr('data-original-value'));
            }
        });
    });

    // Handle delete button click


    // Stop default form submission for all ajax forms
    $('.ajax-form').on('submit', function (e) {
        e.preventDefault();
        return false;
    });
});

// Format number EXACTLY like fmt:formatNumber in JSP
function formatNumberExactly(number) {
    // Create a string version of the number
    var str = number.toString();

    // Add thousand separators - JSP uses dot (.) as the thousand separator
    // This matches exactly the fmt:formatNumber output in Vietnamese locale
    var parts = [];

    // Process from the end of the string
    for (var i = str.length; i > 0; i -= 3) {
        var start = Math.max(0, i - 3);
        parts.unshift(str.substring(start, i));
    }

    // Join all parts with dot as separator
    return parts.join('.');
}

// Update the cart total with exact same format
function updateCartTotal() {
    var grandTotal = 0;

    // Sum all product totals
    $('.total_product').each(function () {
        // Skip the header row
        if ($(this).closest('tr').find('.no_product').text().includes('No')) {
            return;
        }

        var value = parseInt($(this).attr('data-value')) || 0;
        grandTotal += value;
    });

    // Update the total display with the exact same format as JSP
    var formattedGrandTotal = formatNumberExactly(grandTotal) + " VND";
    $('h3:contains("TOTAL:")').text('TOTAL: ' + formattedGrandTotal);
}

// Update row numbers after deletion
function updateRowNumbers() {
    var index = 1;
    $('table tr').each(function () {
        // Skip the header row
        if ($(this).find('.no_product').length && !$(this).find('.no_product').text().includes('No')) {
            $(this).find('.no_product').text(index);
            index++;
        }
    });
}

// Function to ensure quantity validation
function validateQuantity(form, stock) {
    const quantity = parseInt(form.quantity.value) || 0;
    if (quantity > stock) {
        form.quantity.value = stock;
        form.quantity.classList.add('is-invalid');
        alert(`Maximum product in stock is ${stock}`);
        return false;
    }
    if (quantity <= 0) {
        form.quantity.value = 1;
        form.quantity.classList.add('is-invalid');
        alert(`Minimum quantity is 1`);
        return false;
    }
    form.quantity.classList.remove('is-invalid');
    return true;
}