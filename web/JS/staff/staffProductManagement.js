let originalImagePath = '';

function previewImage(input) {
    if (input.files && input.files[0]) {
        const image = document.getElementById('currentProductImage');
        if (image) {
            image.src = URL.createObjectURL(input.files[0]);
        }
    }
}

//Update Total Stock for Clothing items
function updateTotalStock(form) {
    let total = 0;
    const typeId = parseInt(form.querySelector('select[name="type_id"]').value);

    //For clothing items
    const sizes = ['S', 'M', 'L', 'XL', 'XXL'];
    sizes.forEach(size => {
        const checkbox = form.querySelector(`input[type="checkbox"][name="size_${size}"]`);
        const stockInput = form.querySelector(`input[name="stock_${size}"]`);
        if (checkbox && checkbox.checked && stockInput && stockInput.value) {
            total += parseInt(stockInput.value) || 0;
        }
    });

    //Update display
    const totalStockElement = form.querySelector('.total-stock');
    if (totalStockElement) {
        totalStockElement.textContent = total;
    }

    //Update hidden input for total stock
    let stockInput = form.querySelector('input[name="stock"]');
    if (!stockInput) {
        stockInput = document.createElement('input');
        stockInput.type = 'hidden';
        stockInput.name = 'stock';
        form.appendChild(stockInput);
    }
    stockInput.value = total;
}

//Add event listeners for stock inputs
document.addEventListener('input', function (e) {
    if (e.target.matches('input[name^="stock_"]')) {
        const form = e.target.closest('form');
        if (form) {
            updateTotalStock(form);
        }
    }
});

//Add event listeners for size checkboxes
document.addEventListener('change', function (e) {
    if (e.target.matches('input[type="checkbox"][name^="size_"]')) {
        const form = e.target.closest('form');
        if (form) {
            updateTotalStock(form);
        }
    }
});


function handleTypeChange(select) {
    const typeId = parseInt(select.value);
    const form = select.closest('form');

    const sizeOptions = form.querySelector('.size-options');
    const oneSizeOption = form.querySelector('.one-size-option');

    if (typeId >= 1 && typeId <= 5) {
        //For clothing items
        sizeOptions.style.display = 'block';
        oneSizeOption.style.display = 'none';

        // Enable all size checkboxes
        form.querySelectorAll('input[type="checkbox"][name^="size_"]').forEach(checkbox => {
            checkbox.disabled = false;
        });
    } else {
        //For accessories
        sizeOptions.style.display = 'none';
        oneSizeOption.style.display = 'block';

        //Reset and disable all size checkboxes
        form.querySelectorAll('input[type="checkbox"][name^="size_"]').forEach(checkbox => {
            checkbox.checked = false;
            checkbox.disabled = true;
        });
    }

    // Update total stock
    updateTotalStock(form);
}

//Handle Update Modal
function openUpdateModal(productId) {
    // Find product by ID
    const product = products.find(p => p.pro_id === productId);
    const modal = document.getElementById('updateProductModal');
    const form = modal.querySelector('form');

    try {
        //Reset form
        form.reset();

        //Set basic fields
        form.querySelector('input[name="pro_id"]').value = product.pro_id;
        document.getElementById('displayProductId').textContent = product.pro_id;


        //Set type and log for debugging
        const typeSelect = form.querySelector('select[name="type_id"]');
        if (product.type && product.type.type_id) {
            typeSelect.value = product.type.type_id;

            //Trigger type change to update size options
            handleTypeChange(typeSelect);
        } else {
            console.error('Product type information is missing:', product);
        }

        form.querySelector('input[name="price"]').value = product.price;
        form.querySelector('input[name="discount"]').value = product.discount || 0;

        //Reset all size inputs
        form.querySelectorAll('input[type="checkbox"][name^="size_"]').forEach(checkbox => {
            checkbox.checked = false;
        });
        form.querySelectorAll('input[name^="stock_"]').forEach(input => {
            input.value = 0;
        });

        if (product.productSizes && Array.isArray(product.productSizes)) {
            product.productSizes.forEach(size => {
                if (size.size === "One Size") {
                    // For accessories
                    const oneSizeInput = form.querySelector('input[name="stock_one_size"]');
                    if (oneSizeInput) {
                        oneSizeInput.value = size.stock;
                    }
                } else {
                    // For clothing
                    const checkbox = form.querySelector(`input[name="size_${size.size}"]`);
                    const stockInput = form.querySelector(`input[name="stock_${size.size}"]`);
                    if (checkbox && stockInput) {
                        checkbox.checked = true;
                        stockInput.value = size.stock;
                    }
                }
            });
        }

        //Show/hide delete button based on status
        const deleteButtonContainer = document.getElementById('deleteButtonContainer');
        if (product.status && product.status.toLowerCase() === 'pending') {
            deleteButtonContainer.style.display = 'block';
        } else {
            deleteButtonContainer.style.display = 'none';
        }

        //Show modal
        const bsModal = new bootstrap.Modal(modal);
        bsModal.show();

    } catch (error) {
        console.error('Error setting up update modal:', error);
    }
}


//Handle Delete pendind product
function deletePendingProduct() {
    const pro_id = document.querySelector('#updateForm input[name="pro_id"]').value;
    if (confirm('Are you sure you want to delete this product?')) {
        fetch(`updateProductByStaff?action=delete&pro_id=${pro_id}`)
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'success') {
                        // Close update modal first
                        const updateModal = bootstrap.Modal.getInstance(document.getElementById('updateProductModal'));
                        updateModal.hide();

                        // Show success message
                        showMessage('success', data.message);

                        // Reload page after a short delay
                        setTimeout(() => {
                            window.location.reload();
                        }, 1500);
                    } else {
                        showMessage('error', data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showMessage('error', 'An error occurred while deleting the product.');
                });
    }
}


function applyFilters() {
    //Add all current parameters
    const params = new URLSearchParams(window.location.search);

    //Update or add new filter values
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

    //Update search value
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

    //Handle search with debounce
    const searchInput = document.getElementById('search');
    let searchTimeout;
    //Auto enter user search after 1s
    searchInput.addEventListener('input', function () {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(applyFilters, 1000);
    });

    //Set initial search value
    const searchValue = new URLSearchParams(window.location.search).get('search');
    if (searchValue) {
        searchInput.value = searchValue;
    }

    //Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    //Add event listeners for forms
    const addForm = document.getElementById('addForm');
    const updateForm = document.getElementById('updateForm');

    if (addForm) {
        addForm.addEventListener('submit', function (e) {
            e.preventDefault();
                //Remove any existing hidden size-stock inputs
                this.querySelectorAll('input[name="sizes"], input[name="stocks"]').forEach(input => input.remove());

                const typeId = parseInt(this.querySelector('select[name="type_id"]').value);
                if (typeId >= 1 && typeId <= 5) {
                    //For clothing items
                    const checkedSizes = this.querySelectorAll('input[type="checkbox"][name^="size_"]:checked');
                    checkedSizes.forEach(sizeCheckbox => {
                        const size = sizeCheckbox.value;
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
                    //For accessories
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

                fetch('addProductByStaff', {
                    method: 'POST',
                    body: new FormData(this)
                })
                        .then(response => response.json())
                        .then(data => {
                            if (data.status === 'success') {
                                //Close add modal first
                                const addModal = bootstrap.Modal.getInstance(document.getElementById('requestAddProductModal'));
                                addModal.hide();

                                //Show success message
                                showMessage('success', data.message);

                                //Reload page after a short delay
                                setTimeout(() => {
                                    window.location.reload();
                                }, 1500);
                            } else {
                                showMessage('error', data.message);
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            showMessage('error', 'An error occurred while adding the product.');
                        });
        });
    }

    if (updateForm) {
        updateForm.addEventListener('submit', function (e) {
            e.preventDefault();
                const formData = new FormData(this);

                //Add action parameter
                formData.append('action', 'update');

                //Get all form data
                const typeId = parseInt(this.querySelector('select[name="type_id"]').value);
                const sizes = [];
                const stocks = [];

                if (typeId >= 1 && typeId <= 5) {
                    //For clothing items
                    const checkedSizes = this.querySelectorAll('input[type="checkbox"][name^="size_"]:checked');
                    checkedSizes.forEach(sizeCheckbox => {
                        const size = sizeCheckbox.value;
                        const stock = this.querySelector(`input[name="stock_${size}"]`).value;
                        if (stock && parseInt(stock) > 0) {
                            sizes.push(size);
                            stocks.push(stock);
                        }
                    });

                    //Add sizes and stocks as arrays
                    formData.append('sizes', sizes.join(','));
                    formData.append('stocks', stocks.join(','));
                } else {
                    //For accessories
                    const oneSizeStock = this.querySelector('input[name="stock_one_size"]').value;
                    formData.append('sizes', 'One Size');
                    formData.append('stocks', oneSizeStock);
                }

                // Send AJAX request
                fetch('updateProductByStaff', {
                    method: 'POST',
                    body: formData
                })
                        .then(response => response.json())
                        .then(data => {
                            if (data.status === 'success') {
                                //Close update modal first
                                const updateModal = bootstrap.Modal.getInstance(document.getElementById('updateProductModal'));
                                updateModal.hide();

                                //Show success message
                                showMessage('success', data.message);

                                //Reload page after a short delay
                                setTimeout(() => {
                                    window.location.reload();
                                }, 1500);
                            } else {
                                showMessage('error', data.message);
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            showMessage('error', 'An error occurred while updating the product.');
                        });
        });
    }

    //Add input event listeners to all stock inputs in both forms
    ['addForm', 'updateForm'].forEach(formId => {
        const form = document.getElementById(formId);
        if (form) {
            //Listen to all stock inputs
            form.querySelectorAll('input[type="number"][name^="stock_"]').forEach(input => {
                input.addEventListener('input', () => updateTotalStock(form));
            });

            //Listen to size checkboxes
            form.querySelectorAll('input[type="checkbox"][name^="size_"]').forEach(checkbox => {
                checkbox.addEventListener('change', function () {
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

//Pagination
function goToPage(page) {
    const params = new URLSearchParams(window.location.search);
    params.set('page', page);
    window.location.href = window.location.pathname + '?' + params.toString();
}

//Product notify message when add/update/delete
function showMessage(status, message) {
    const alertClass = status === 'success' ? 'alert-success' : 'alert-danger';
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert ${alertClass} alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3`;
    alertDiv.role = 'alert';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    document.body.appendChild(alertDiv);

    //Hide after 3 seconds
    setTimeout(() => {
        alertDiv.remove();
    }, 3000);
}
