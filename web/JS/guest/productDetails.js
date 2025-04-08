let isEditMode = false;
let editingFeedbackId = null;

function filterFeedback(filterType) {
    const proId = document.querySelector("input[name='pro_id']")?.value || new URLSearchParams(window.location.search).get("id");

    const container = document.getElementById("feedback-container");
    if (!container)
        return;
    container.innerHTML = "<p class='text-center'><i class='fas fa-spinner fa-spin'></i> Loading feedback...</p>";

    const url = `getFeedback?filter=${encodeURIComponent(filterType)}&pro_id=${encodeURIComponent(proId)}`;

    fetch(url)
            .then(res => {
                if (!res.ok)
                    throw new Error(`HTTP error! status: ${res.status}`);
                return res.text().then(text => {
                    try {
                        return JSON.parse(text);
                    } catch (e) {
                        throw new Error('Invalid JSON response');
                    }
                });
            })
            .then(data => {
                container.innerHTML = "";

                if (!data || data.length === 0) {
                    container.innerHTML = "<p class='text-muted text-center'>No feedback yet</p>";
                    return;
                }

                data.forEach(f => {
                    try {
                        const date = new Date(f.feedback_date);
                        const formattedDate = date.toLocaleDateString('en-GB');
                        const rating = parseInt(f.rating) || 0;
                        const stars = '★'.repeat(rating) + '☆'.repeat(5 - rating);
                        const sizes = f.purchasedSizes || 'Not available';

                        const isCustomer = document.getElementById('sessionCusId')?.value === f.cus_id;
                        const editBtn = isCustomer ? `<button class="btn btn-sm btn-outline-primary edit-feedback-btn" data-feedback-id="${f.feedback_id}" data-rating="${f.rating}" data-comment="${f.comment}">Edit</button>` : '';

                        const html = `
                        <div class="border-bottom pb-2 mb-2">
                            <div class="d-flex justify-content-between">
                                <span class="fw-bold">${f.cus_name || 'Anonymous'}</span>
                                ${editBtn}
                            </div>
                            <div class="text-warning my-1">${stars}</div>
                            <div class="text-muted small mb-1">${formattedDate} | Variation(s): ${sizes}</div>
                            <p class="mt-1 mb-0">${f.comment || ''}</p>
                        </div>
                    `;
                        container.innerHTML += html;
                    } catch (e) {
                    }
                });

                attachEditListeners();
            })
            .catch(error => {
                container.innerHTML = `<p class='text-danger text-center'>Error loading feedback: ${error.message}</p>`;
            });
}

//Change modal Feedback if customer clicked on the edit button on their feedback
function attachEditListeners() {
    document.querySelectorAll('.edit-feedback-btn').forEach(button => {
        button.addEventListener('click', function () {
            isEditMode = true;
            editingFeedbackId = this.dataset.feedbackId;
            const rating = this.dataset.rating;
            const comment = this.dataset.comment;

            const ratingInputs = document.querySelectorAll('input[name="rating"]');
            ratingInputs.forEach(input => {
                input.checked = (input.value === rating);
            });

            const commentTextarea = document.getElementById('comment');
            commentTextarea.value = comment;
            document.getElementById('charCount').textContent = 500 - comment.length;

            document.getElementById('feedbackModalLabel').textContent = 'Edit Feedback';
            document.querySelector('#feedbackForm button[type="submit"]').textContent = 'Update Feedback';

            document.getElementById('feedbackError').classList.add('d-none');
            document.getElementById('feedbackError').style.display = 'none';

            const modalElement = document.getElementById('feedbackModal');
            const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
            modal.show();
        });
    });
}

function validateFeedbackForm(event) {
    if (event)
        event.preventDefault();

    let isValid = true;
    const rating = document.querySelector('input[name="rating"]:checked');
    const commentTextarea = document.getElementById('comment');
    const comment = commentTextarea.value.trim();
    const ratingWarning = document.getElementById('ratingWarning');
    const commentWarning = document.getElementById('commentWarning');
    const maxLength = 500;

    if (!rating) {
        ratingWarning.style.display = 'block';
        ratingWarning.textContent = 'Please select a rating.';
        isValid = false;
    } else {
        ratingWarning.style.display = 'none';
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
    const errorDiv = document.getElementById('feedbackError');
    errorDiv.style.display = 'none';

    if (isEditMode && editingFeedbackId) {
        formData.append("feedback_id", editingFeedbackId);
    }

    const url = isEditMode ? 'editFeedback' : 'giveFeedback';

    try {
        const response = await fetch(url, {
            method: 'POST',
            body: formData
        });

        let data;
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            throw new Error('Received non-JSON response from server');
        }

        if (response.ok && data.status === 'success') {
            successMessage.style.display = 'block';
            successMessage.textContent = data.message;
            feedbackForm.reset();
            document.getElementById('charCount').textContent = '500';
            errorDiv.classList.add('d-none');
            setTimeout(() => window.location.reload(), 1000);
        } else {
            errorDiv.style.display = 'block';
            errorDiv.classList.remove('d-none');
            errorDiv.textContent = data.message || 'An error occurred while submitting feedback.';
        }
    } catch (error) {
        errorDiv.style.display = 'block';
        errorDiv.classList.remove('d-none');
        errorDiv.textContent = 'An error occurred while submitting feedback.';
    }
}

document.getElementById('feedbackModal').addEventListener('hidden.bs.modal', function () {
    document.getElementById('feedbackForm').reset();
    document.getElementById('ratingWarning').style.display = 'none';
    document.getElementById('commentWarning').style.display = 'none';
    document.getElementById('feedbackError').classList.add('d-none');
    document.getElementById('charCount').textContent = '500';
    document.getElementById('feedbackModalLabel').textContent = 'Give Feedback';
    document.querySelector('#feedbackForm button[type="submit"]').textContent = 'Submit Feedback';
    isEditMode = false;
    editingFeedbackId = null;
});

document.addEventListener('DOMContentLoaded', function () {
    const feedbackModal = document.getElementById('feedbackModal');
    const commentTextarea = document.getElementById('comment');
    const maxLength = 500;

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

    document.querySelectorAll('.feedback-btn').forEach(button => {
        button.addEventListener('click', async function (e) {
            e.preventDefault();

            // Only check purchase if not editing
            if (isEditMode) {
                const modal = bootstrap.Modal.getOrCreateInstance(feedbackModal);
                modal.show();
                return;
            }

            const productId = this.dataset.productId;
            const warningDiv = this.parentElement.querySelector('.feedback-warning');

            try {
                const response = await fetch(`checkPurchase?pro_id=${productId}`, {
                    method: 'GET',
                    headers: {'Accept': 'application/json'}
                });

                if (!response.ok)
                    throw new Error('Network response was not ok');

                const data = await response.json();

                this.classList.remove('btn-danger');
                this.classList.add('btn-primary');
                warningDiv.style.display = 'none';

                if (data.status === 'error') {
                    this.classList.remove('btn-primary');
                    this.classList.add('btn-danger');

                    if (data.message === 'not_logged_in') {
                        window.location.href = 'Login';
                        return;
                    }

                    warningDiv.textContent = data.message === 'already_reviewed'
                            ? 'Sorry, you can only feedback once for this product.'
                            : 'Sorry, you must have ordered and received this product to be able to give feedback.';
                    warningDiv.style.display = 'block';
                    return;
                }

                const modal = bootstrap.Modal.getOrCreateInstance(feedbackModal);
                modal.show();
            } catch (error) {
                const errorDiv = document.getElementById('feedbackError');
                errorDiv.style.display = 'block';
                errorDiv.textContent = 'An error occurred. Please try again later.';
            }
        });
    });

    const feedbackForm = document.getElementById('feedbackForm');
    if (feedbackForm) {
        feedbackForm.addEventListener('submit', validateFeedbackForm);
    }

    attachEditListeners();
});
