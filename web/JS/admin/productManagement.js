/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let originalImagePath = '';

function previewImage(input) {
    if (input.files && input.files[0]) {
        const image = document.getElementById('currentProductImage');
        if (image) {
            image.src = URL.createObjectURL(input.files[0]);
        }
    }
}

function resetImage() {
    const image = document.getElementById('currentProductImage');
    const imageInput = document.querySelector('input[type="file"][name="image"]');
    const currentImagePathInput = document.getElementById('currentImagePath');

    if (image && imageInput && currentImagePathInput) {
        // Reset the file input
        imageInput.value = '';

        // Restore original image path
        if (originalImagePath) {
            image.src = originalImagePath;
            currentImagePathInput.value = originalImagePath;
        }
    }
}

function validatePrice(input) {
    const value = parseFloat(input.value);
    if (value < 1 || value > 99999999) {
        input.classList.add('is-invalid');
        return false;
    } else {
        input.classList.remove('is-invalid');
        return true;
    }
}

function validateDiscount(input) {
    const value = parseInt(input.value);
    if (value < 0 || value >= 100) {
        input.classList.add('is-invalid');
        return false;
    } else {
        input.classList.remove('is-invalid');
        return true;
    }
}

function validateForm(form) {
    const name = form.querySelector('input[name="name"]').value.trim();
    const price = parseFloat(form.querySelector('input[name="price"]').value);
    const discount = parseInt(form.querySelector('input[name="discount"]').value);
    const typeId = parseInt(form.querySelector('select[name="type_id"]').value);

    // Basic validation
    if (!name) {
        alert('Please enter a product name');
        return false;
    }

    if (!typeId) {
        alert('Please select a product type');
        return false;
    }

    if (isNaN(price) || price < 1 || price > 99999999) {
        alert('Price must be between 1 and 99,999,999 VND');
        return false;
    }

    if (isNaN(discount) || discount < 0 || discount > 99) {
        alert('Discount must be between 0 and 99%');
        return false;
    }

    // Image validation for new products
    const imageInput = form.querySelector('input[name="image"]');
    if (form.id === 'addForm' && (!imageInput.files || !imageInput.files[0])) {
        alert('Please select an image');
        return false;
    }

    // Stock validation
    let totalStock = 0;
    if (typeId >= 1 && typeId <= 5) {
        // For clothing items
        const checkedSizes = form.querySelectorAll('input[type="checkbox"][name^="size_"]:checked');
        if (checkedSizes.length === 0) {
            alert('Please select at least one size');
            return false;
        }

        for (const sizeCheckbox of checkedSizes) {
            const size = sizeCheckbox.value;
            const stockInput = form.querySelector(`input[name="stock_${size}"]`);
            const stock = parseInt(stockInput.value) || 0;

            if (stock < 0) {
                alert(`Please enter a valid stock amount for size ${size}`);
                stockInput.focus();
                return false;
            }

            totalStock += stock;
        }
    } else {
        // For accessories
        const oneSizeStock = parseInt(form.querySelector('input[name="stock_one_size"]').value) || 0;
        if (oneSizeStock < 0) {
            alert('Please enter a valid stock amount');
            return false;
        }
        totalStock = oneSizeStock;
    }

    return true;
}

function handleTypeChange(select) {
    console.log('handleTypeChange called with:', select);
    console.log('select value:', select.value);
    
    const typeId = parseInt(select.value);
    console.log('parsed typeId:', typeId);
    
    const form = select.closest('form');
    console.log('form found:', form);
    
    const sizeOptions = form.querySelector('.size-options');
    const oneSizeOption = form.querySelector('.one-size-option');
    console.log('size options elements:', { sizeOptions, oneSizeOption });

    if (!sizeOptions || !oneSizeOption) {
        console.error('Size options elements not found');
        return;
    }

    if (typeId >= 1 && typeId <= 5) {
        // For clothing items
        console.log('Showing size options for clothing item');
        sizeOptions.style.display = 'block';
        oneSizeOption.style.display = 'none';
        
        // Enable all size checkboxes
        form.querySelectorAll('input[type="checkbox"][name^="size_"]').forEach(checkbox => {
            checkbox.disabled = false;
        });
    } else {
        // For accessories
        console.log('Showing one-size option for accessory');
        sizeOptions.style.display = 'none';
        oneSizeOption.style.display = 'block';
        
        // Reset and disable all size checkboxes
        form.querySelectorAll('input[type="checkbox"][name^="size_"]').forEach(checkbox => {
            checkbox.checked = false;
            checkbox.disabled = true;
        });
    }
    
    // Update total stock
    updateTotalStock(form);
}

function openUpdateModal(productId) {
    console.log('Opening update modal for product ID:', productId);
    
    // Find product by ID
    const product = products.find(p => p.pro_id === productId);
    if (!product) {
        console.error('Product not found:', productId);
        return;
    }
    console.log('Found product:', product);
    console.log('Product type:', product.type);

    const modal = document.getElementById('updateProductModal');
    if (!modal) {
        console.error('Update modal not found');
        return;
    }

    const form = modal.querySelector('form');
    if (!form) {
        console.error('Update form not found');
        return;
    }

    try {
        // Reset form
        form.reset();

        // Set basic fields
        form.querySelector('input[name="pro_id"]').value = product.pro_id;
        document.getElementById('displayProductId').textContent = product.pro_id;
        form.querySelector('input[name="name"]').value = product.pro_name;
        form.querySelector('input[name="currentImage"]').value = product.image;
        
        // Set type and log for debugging
        const typeSelect = form.querySelector('select[name="type_id"]');
        console.log('Type select element:', typeSelect);
        if (product.type && product.type.type_id) {
            console.log('Setting type_id to:', product.type.type_id);
            typeSelect.value = product.type.type_id;
            console.log('Type select value after setting:', typeSelect.value);
            
            // Trigger type change to update size options
            handleTypeChange(typeSelect);
        } else {
            console.error('Product type information is missing:', product);
        }

        form.querySelector('select[name="gender"]').value = product.gender.toLowerCase();
        form.querySelector('select[name="brand"]').value = product.brand;
        form.querySelector('input[name="price"]').value = product.price;
        form.querySelector('input[name="discount"]').value = product.discount || 0;
        
        // Set status
        const status = product.status || 'active';
        form.querySelector(`input[name="status"][value="${status}"]`).checked = true;

        // Show current image
        const previewImage = form.querySelector('#currentProductImage');
        if (previewImage && product.image) {
            previewImage.src = product.image;
            previewImage.style.display = 'block';
        }

        // Reset all size inputs
        form.querySelectorAll('input[type="checkbox"][name^="size_"]').forEach(checkbox => {
            checkbox.checked = false;
        });
        form.querySelectorAll('input[name^="stock_"]').forEach(input => {
            input.value = 0;
        });

        // Set sizes and stocks
        if (product.productSizes && Array.isArray(product.productSizes)) {
            product.productSizes.forEach(size => {
                const checkbox = form.querySelector(`input[name="size_${size.size}"]`);
                const stockInput = form.querySelector(`input[name="stock_${size.size}"]`);
                if (checkbox && stockInput) {
                    checkbox.checked = true;
                    stockInput.value = size.stock;
                }
            });
        }

        // Show modal
        const bsModal = new bootstrap.Modal(modal);
        bsModal.show();

    } catch (error) {
        console.error('Error setting up update modal:', error);
    }
}

function applyFilters() {
    // Add all current parameters
    const params = new URLSearchParams(window.location.search);

    // Update or add new filter values
    const filters = document.querySelectorAll('select[id$="Filter"]');
    filters.forEach(filter => {
        // Special handling for sort parameter
        if (filter.id === 'sortFilter') {
            if (filter.value && filter.value !== '') {
                params.set('sortBy', filter.value);
                // Update the dropdown text
                const selectedOption = filter.options[filter.selectedIndex];
                filter.options[0].text = selectedOption.text;
            } else {
                params.delete('sortBy');
                filter.options[0].text = 'Sort By';
            }
        } else {
            const paramName = filter.id.replace('Filter', '');
            if (filter.value && filter.value !== '') {
                params.set(paramName, filter.value);
            } else {
                params.delete(paramName);
            }
        }
    });

    // Update search value
    const searchInput = document.getElementById('search');
    if (searchInput && searchInput.value.trim()) {
        params.set('search', searchInput.value.trim());
    } else {
        params.delete('search');
    }

    // Reset to page 1 when applying new filters
    params.set('page', '1');

    // Redirect with updated parameters
    window.location.href = window.location.pathname + '?' + params.toString();
}

// Add event listeners for filters
document.addEventListener('DOMContentLoaded', function () {
    const filters = document.querySelectorAll('select[id$="Filter"]');
    filters.forEach(filter => {
        filter.addEventListener('change', applyFilters);

        // Set initial values from URL params
        const paramName = filter.id === 'sortFilter' ? 'sortBy' : filter.id.replace('Filter', '');
        const value = new URLSearchParams(window.location.search).get(paramName);
        if (value) {
            filter.value = value;
            // Update sort dropdown text if it's the sort filter
            if (filter.id === 'sortFilter') {
                const selectedOption = filter.options[filter.selectedIndex];
                filter.options[0].text = selectedOption.text;
            }
        }
    });

    // Handle search with debounce
    const searchInput = document.getElementById('search');
    let searchTimeout;
    searchInput.addEventListener('input', function () {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(applyFilters, 500);
    });

    // Set initial search value
    const searchValue = new URLSearchParams(window.location.search).get('search');
    if (searchValue) {
        searchInput.value = searchValue;
    }

    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Add event listeners for forms
    const addForm = document.getElementById('addForm');
    const updateForm = document.getElementById('updateForm');

    if (addForm) {
        addForm.addEventListener('submit', function(e) {
            e.preventDefault();
            if (validateForm(this)) {
                // Remove any existing hidden size-stock inputs
                this.querySelectorAll('input[name="sizes"], input[name="stocks"]').forEach(input => input.remove());
                
                const typeId = parseInt(this.querySelector('select[name="type_id"]').value);
                if (typeId >= 1 && typeId <= 5) {
                    // For clothing items
                    const checkedSizes = this.querySelectorAll('input[type="checkbox"][name^="size_"]:checked');
                    checkedSizes.forEach(checkbox => {
                        const size = checkbox.value;
                        const stock = this.querySelector(`input[name="stock_${size}"]`).value;
                        
                        const sizeInput = document.createElement('input');
                        sizeInput.type = 'hidden';
                        sizeInput.name = 'sizes';
                        sizeInput.value = size;
                        this.appendChild(sizeInput);

                        const stockInput = document.createElement('input');
                        stockInput.type = 'hidden';
                        stockInput.name = 'stocks';
                        stockInput.value = stock;
                        this.appendChild(stockInput);
                    });
                } else {
                    // For accessories
                    const stock = this.querySelector('input[name="stock_one_size"]').value;
                    
                    const sizeInput = document.createElement('input');
                    sizeInput.type = 'hidden';
                    sizeInput.name = 'sizes';
                    sizeInput.value = 'One Size';
                    this.appendChild(sizeInput);

                    const stockInput = document.createElement('input');
                    stockInput.type = 'hidden';
                    stockInput.name = 'stocks';
                    stockInput.value = stock;
                    this.appendChild(stockInput);
                }

                this.submit();
            }
        });
    }

    if (updateForm) {
        updateForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            if (validateForm(this)) {
                const formData = new FormData(this);
                
                // Add action parameter
                formData.append('action', 'update');
                
                // Get all form data
                const typeId = parseInt(this.querySelector('select[name="type_id"]').value);
                
                // Handle sizes and stocks
                if (typeId >= 1 && typeId <= 5) {
                    // For clothing items
                    const checkedSizes = this.querySelectorAll('input[type="checkbox"][name^="size_"]:checked');
                    if (checkedSizes.length === 0) {
                        alert('Please select at least one size');
                        return;
                    }
                    
                    const sizes = [];
                    const stocks = [];
                    
                    checkedSizes.forEach(checkbox => {
                        const size = checkbox.value;
                        const stockInput = this.querySelector(`input[name="stock_${size}"]`);
                        if (stockInput && !isNaN(stockInput.value)) {
                            sizes.push(size);
                            stocks.push(stockInput.value);
                        }
                    });
                    
                    // Add sizes and stocks as arrays
                    formData.append('sizes', sizes.join(','));
                    formData.append('stocks', stocks.join(','));
                } else {
                    // For accessories (one size)
                    const oneSizeStock = this.querySelector('input[name="stock_one_size"]').value;
                    formData.append('sizes', 'One Size');
                    formData.append('stocks', oneSizeStock);
                }
                
                // Debug log
                console.log('Submitting form data:');
                for (let pair of formData.entries()) {
                    console.log(pair[0] + ': ' + pair[1]);
                }
                
                // Send AJAX request
                fetch('updateProduct', {
                    method: 'POST',
                    body: formData
                })
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(text => {
                            throw new Error('Update failed: ' + text);
                        });
                    }
                    return response.text();
                })
                .then(text => {
                    console.log('Server response:', text);
                    if (text.includes('success')) {
                        window.location.href = 'productM?success=true';
                    } else {
                        throw new Error('Update failed: ' + text);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert(error.message);
                });
            }
        });
    }

    // Add input event listeners to all stock inputs in both forms
    ['addForm', 'updateForm'].forEach(formId => {
        const form = document.getElementById(formId);
        if (form) {
            // Listen to all stock inputs
            form.querySelectorAll('input[type="number"][name^="stock_"]').forEach(input => {
                input.addEventListener('input', () => updateTotalStock(form));
            });

            // Listen to size checkboxes
            form.querySelectorAll('input[type="checkbox"][name^="size_"]').forEach(checkbox => {
                checkbox.addEventListener('change', function() {
                    const size = this.value;
                    const stockInput = form.querySelector(`input[name="stock_${size}"]`);
                    if (!this.checked && stockInput) {
                        stockInput.value = '0';
                    }
                    updateTotalStock(form);
                });
            });
        }
    });
});

function updateTotalStock(form) {
    let total = 0;
    const typeId = parseInt(form.querySelector('select[name="type_id"]').value);
    
    if (typeId >= 1 && typeId <= 5) {
        // For clothing items
        const sizes = ['S', 'M', 'L', 'XL', 'XXL'];
        sizes.forEach(size => {
            const checkbox = form.querySelector(`input[type="checkbox"][name="size_${size}"]`);
            const stockInput = form.querySelector(`input[name="stock_${size}"]`);
            if (checkbox && checkbox.checked && stockInput && stockInput.value) {
                total += parseInt(stockInput.value) || 0;
            }
        });
    } else {
        // For accessories
        const oneSizeStock = form.querySelector('input[name="stock_one_size"]');
        if (oneSizeStock && oneSizeStock.value) {
            total += parseInt(oneSizeStock.value) || 0;
        }
    }
    
    // Update display
    const totalStockElement = form.querySelector('.total-stock');
    if (totalStockElement) {
        totalStockElement.textContent = total;
    }
    
    // Update hidden input for total stock
    let stockInput = form.querySelector('input[name="stock"]');
    if (!stockInput) {
        stockInput = document.createElement('input');
        stockInput.type = 'hidden';
        stockInput.name = 'stock';
        form.appendChild(stockInput);
    }
    stockInput.value = total;
}

function goToPage(page) {
    const params = new URLSearchParams(window.location.search);
    params.set('page', page);
    window.location.href = window.location.pathname + '?' + params.toString();
}

// Add event listeners for stock inputs
document.addEventListener('input', function(e) {
    if (e.target.matches('input[name^="stock_"]')) {
        const form = e.target.closest('form');
        if (form) {
            updateTotalStock(form);
        }
    }
});

// Add event listeners for size checkboxes
document.addEventListener('change', function(e) {
    if (e.target.matches('input[type="checkbox"][name^="size_"]')) {
        const form = e.target.closest('form');
        if (form) {
            updateTotalStock(form);
        }
    }
});


