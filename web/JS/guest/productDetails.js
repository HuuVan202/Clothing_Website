// Global validation and submission functions
function validateFeedbackForm(event) {
    if (event) {
        event.preventDefault();
    }

    let isValid = true;
    const rating = document.querySelector('input[name="rating"]:checked');
    const commentTextarea = document.getElementById('comment');
    const comment = commentTextarea.value.trim();
    const ratingWarning = document.getElementById('ratingWarning');
    const commentWarning = document.getElementById('commentWarning');
    const maxLength = 500;

    // Validate rating
    if (!rating) {
        ratingWarning.style.display = 'block';
        ratingWarning.textContent = 'Please select a rating.';
        isValid = false;
    } else {
        ratingWarning.style.display = 'none';
    }

    // Validate comment length
    if (!comment || comment.trim().length === 0) {
        commentWarning.style.display = 'block';
        commentWarning.textContent = 'Please enter your comment.';
        isValid = false;
    } else if (comment.length > maxLength) {
        commentWarning.style.display = 'block';
        commentWarning.textContent = 'Comment must be less than 500 characters';
        isValid = false;
    } else {
        commentWarning.style.display = 'none';
    }

    if (isValid && event) {
        submitFeedback();
    }

    return isValid;
}

async function submitFeedback() {
    const feedbackForm = document.getElementById('feedbackForm');
    const formData = new FormData(feedbackForm);
    const successMessage = document.getElementById('successMessage');

    try {
        const response = await fetch('addFeedback', {
            method: 'POST',
            body: formData,
            headers: {
                'Accept': 'application/json'
            }
        });

        let data;
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            throw new Error('Received non-JSON response from server');
        }

        if (response.ok && data.status === 'success') {
            // Show success message
            successMessage.style.display = 'block';
            successMessage.textContent = data.message;

            // Reset form
            feedbackForm.reset();
            document.getElementById('charCount').textContent = '500';

            // Hide any error messages
            const errorDiv = document.getElementById('feedbackError');
            errorDiv.style.display = 'none';

            // Reload page after 1 second
            setTimeout(() => {
                window.location.reload();
            }, 1000);
        } else {
            // Show error in modal
            const errorDiv = document.getElementById('feedbackError');
            errorDiv.style.display = 'block';
            errorDiv.classList.remove('d-none');
            errorDiv.textContent = data.message || 'An error occurred while submitting feedback.';
        }
    } catch (error) {
        console.error('Error:', error);
        const errorDiv = document.getElementById('feedbackError');
        errorDiv.style.display = 'block';
        errorDiv.classList.remove('d-none');
        errorDiv.textContent = 'An error occurred while submitting feedback.';
    }
}

// Reset form when modal is closed
document.getElementById('feedbackModal').addEventListener('hidden.bs.modal', function () {
    document.getElementById('feedbackForm').reset();
    document.getElementById('ratingWarning').style.display = 'none';
    document.getElementById('commentWarning').style.display = 'none';
    document.getElementById('feedbackError').classList.add('d-none');
    document.getElementById('charCount').textContent = '500';
});

document.addEventListener('DOMContentLoaded', function () {
    const feedbackModal = document.getElementById('feedbackModal');
    const commentTextarea = document.getElementById('comment');
    const maxLength = 500;

    // Add character count update handler
    if (commentTextarea) {
        commentTextarea.addEventListener('input', function () {
            const currentLength = this.value.length;
            const remaining = maxLength - currentLength;
            document.getElementById('charCount').textContent = remaining;

            const commentWarning = document.getElementById('commentWarning');
            if (currentLength > maxLength) {
                commentWarning.style.display = 'block';
                commentWarning.textContent = 'Comment must be less than 500 characters';
            } else {
                commentWarning.style.display = 'none';
            }
        });
    }

    // Handle feedback button click
    document.querySelectorAll('.feedback-btn').forEach(button => {
        button.addEventListener('click', async function (e) {
            e.preventDefault();
            const productId = this.dataset.productId;
            const warningDiv = this.parentElement.querySelector('.feedback-warning');

            try {
                const response = await fetch(`checkPurchase?pro_id=${productId}`, {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const data = await response.json();

                // Reset button and warning styles
                this.classList.remove('btn-danger');
                this.classList.add('btn-primary');
                warningDiv.style.display = 'none';

                if (data.status === 'error') {
                    if (data.message === 'not_logged_in') {
                        window.location.href = 'Login';
                        return;
                    }

                    // Show red button and appropriate warning message
                    this.classList.remove('btn-primary');
                    this.classList.add('btn-danger');

                    if (data.message === 'already_reviewed') {
                        warningDiv.textContent = 'Sorry, you can only feedback once for this product.';
                    } else {
                        warningDiv.textContent = 'Sorry, you must have ordered and received this product to be able to give feedback.';
                    }
                    warningDiv.style.display = 'block';
                    return;
                }

                // If we get here, user can give feedback, show modal
                const modal = new bootstrap.Modal(feedbackModal);
                modal.show();

            } catch (error) {
                console.error('Error checking feedback eligibility:', error);
                const errorDiv = document.getElementById('feedbackError');
                errorDiv.style.display = 'block';
                errorDiv.textContent = 'An error occurred. Please try again later.';
            }
        });
    });

    // Event Listeners
    const feedbackForm = document.getElementById('feedbackForm');
    if (feedbackForm) {
        feedbackForm.addEventListener('submit', validateFeedbackForm);
    }
});


