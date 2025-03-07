/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let originalImagePath = '';

            function previewImage(input) {
                if (input.files && input.files[0]) {
                    const image = document.getElementById('currentProductImage');
                    image.src = URL.createObjectURL(input.files[0]);
                }
            }

            function resetImage() {
                const image = document.getElementById('currentProductImage');
                const imageInput = document.querySelector('input[type="file"][name="image"]');
                const currentImagePathInput = document.getElementById('currentImagePath');

                // Reset the file input
                imageInput.value = '';

                // Restore original image path
                if (originalImagePath) {
                    image.src = originalImagePath;
                    currentImagePathInput.value = originalImagePath;
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

            function validateStock(input) {
                const value = parseInt(input.value);
                if (value < 1 || value > 99999999) {
                    input.classList.add('is-invalid');
                    return false;
                } else {
                    input.classList.remove('is-invalid');
                    return true;
                }
            }

            function handleTypeChange(select) {
                const typeId = parseInt(select.value);
                const form = select.closest('form');
                const sizeOptions = form.querySelector('.size-options');
                const oneSize = form.querySelector('.one-size');

                if (typeId >= 1 && typeId <= 5) {
                    // Show size options for clothing items
                    sizeOptions.style.display = 'block';
                    oneSize.style.display = 'none';

                    // Clear and update hidden size input
                    form.querySelector('input[name="size"]').value = '';
                } else {
                    // Show one size for accessories
                    sizeOptions.style.display = 'none';
                    oneSize.style.display = 'block';

                    // Set one size
                    form.querySelector('input[name="size"]').value = 'One Size';
                }
            }

            function openUpdateModal(productId) {
                const product = products.find(p => p.pro_id === productId);

                if (!product) {
                    console.error('Product not found:', productId);
                    return;
                }

                const form = document.getElementById('updateForm');

                // Reset form
                form.reset();

                // Store original image path
                originalImagePath = product.pro_image;

                // Set basic fields
                form.querySelector('input[name="pro_id"]').value = product.pro_id;
                document.getElementById('displayProductId').textContent = product.pro_id;
                form.querySelector('input[name="name"]').value = product.pro_name;

                // Set image
                document.getElementById('currentProductImage').src = product.pro_image;
                document.getElementById('currentImagePath').value = product.pro_image;
                form.querySelector('input[name="currentImage"]').value = product.pro_image;

                // Set other fields
                form.querySelector('select[name="type_id"]').value = product.type.type_id;
                form.querySelector('select[name="gender"]').value = product.pro_gender;
                form.querySelector('select[name="brand"]').value = product.pro_brand;
                form.querySelector('input[name="price"]').value = product.pro_price;
                form.querySelector('input[name="discount"]').value = product.pro_discount;
                form.querySelector('input[name="stock"]').value = product.pro_stock;

                // Handle size display and values
                const typeId = parseInt(product.type.type_id);
                const sizeOptions = form.querySelector('.size-options');
                const oneSize = form.querySelector('.one-size');

                // Reset size displays
                sizeOptions.style.display = 'none';
                oneSize.style.display = 'none';

                // Reset all checkboxes
                form.querySelectorAll('input[type="checkbox"][name="size"]').forEach(cb => {
                    cb.checked = false;
                });

                if (typeId >= 1 && typeId <= 5) {
                    // Show size options for clothing items
                    sizeOptions.style.display = 'block';

                    // Parse the size string and check the appropriate checkboxes
                    if (product.size && product.size !== 'One Size') {
                        const sizes = product.size.split(',').map(s => s.trim());
                        form.querySelectorAll('input[type="checkbox"][name="size"]').forEach(cb => {
                            cb.checked = sizes.includes(cb.value);
                        });
                        form.querySelector('input[name="size"]').value = product.size;
                    }
                } else {
                    // Show one size for accessories
                    oneSize.style.display = 'block';
                    form.querySelector('input[name="size"]').value = 'One Size';
                }

                // Set status
                form.querySelectorAll('input[name="updateProductStatus"]').forEach(radio => {
                    if (radio.value === product.status) {
                        radio.checked = true;
                    }
                });

                // Show the modal
                const updateProductModal = new bootstrap.Modal(document.getElementById('updateProductModal'));
                updateProductModal.show();
            }

            function validateForm(form) {
                const price = form.querySelector('input[name="price"]');
                const discount = form.querySelector('input[name="discount"]');
                const stock = form.querySelector('input[name="stock"]');

                const isPriceValid = validatePrice(price);
                const isDiscountValid = validateDiscount(discount);
                const isStockValid = validateStock(stock);

                if (!isPriceValid || !isDiscountValid || !isStockValid) {
                    return false;
                }

                const typeId = parseInt(form.querySelector('select[name="type_id"]').value);

                if (typeId >= 1 && typeId <= 5) {
                    const checkedSizes = form.querySelectorAll('input[type="checkbox"][name="size"]:checked');
                    if (checkedSizes.length === 0) {
                        alert('Please select at least one size for clothing items');
                        return false;
                    }

                    const selectedSizes = Array.from(checkedSizes).map(cb => cb.value);
                    form.querySelector('input[name="size"]').value = selectedSizes.join(', ');
                } else {
                    form.querySelector('input[name="size"]').value = 'One Size';
                }

                return true;
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

                // Initialize type change handlers for both forms on page load
                document.querySelectorAll('select[name="type_id"]').forEach(select => {
                    handleTypeChange(select); // Initialize size options on page load
                });

                // Initialize tooltips
                var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
                tooltipTriggerList.map(function (tooltipTriggerEl) {
                    return new bootstrap.Tooltip(tooltipTriggerEl);
                });
            });

            function goToPage(page) {
                const params = new URLSearchParams(window.location.search);
                params.set('page', page);
                window.location.href = window.location.pathname + '?' + params.toString();
            }

            function previewImage(input) {
                const image = document.getElementById('currentProductImage');
                image.src = URL.createObjectURL(input.files[0]);
            }

            function resetImage() {
                const image = document.getElementById('currentProductImage');
                const imageInput = document.querySelector('input[type="file"][name="image"]');
                const currentImagePathInput = document.getElementById('currentImagePath');

                // Reset the file input
                imageInput.value = '';

                // Restore original image path
                if (originalImagePath) {
                    image.src = originalImagePath;
                    currentImagePathInput.value = originalImagePath;
                }
            }
