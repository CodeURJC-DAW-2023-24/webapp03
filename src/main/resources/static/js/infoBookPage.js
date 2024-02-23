// Select all the star elements
const stars = document.querySelectorAll('.rating input[type="radio"]');

// Select all the radio buttons
const radioButtons = document.querySelectorAll('.action-buttons-flex input[type="radio"]');

// Add a click event listener to each radio button
radioButtons.forEach(radioButton => {
    radioButton.addEventListener('click', function () {
        // Get the label for the clicked radio button
        const label = document.querySelector(`label[for="${this.id}"] a`);

        // Add the 'btn-primary-light' class and remove the 'btn-outline-dark' class
        // check if it is the remove button
        if (this.id === "removeButton") {
            label.classList.remove('btn-primary-light');
            label.classList.add('btn-danger');
        } else {
            label.classList.add('btn-primary-light');
            label.classList.remove('btn-outline-dark');
        }

        // Reset all other radio buttons
        radioButtons.forEach(otherRadioButton => {
            if (otherRadioButton !== this) {
                const otherLabel = document.querySelector(`label[for="${otherRadioButton.id}"] a`);
                otherLabel.classList.remove('btn-primary-light');
                otherLabel.classList.add('btn-outline-dark');
            }
        });
    });
});

function fillStars(rating, starElements) {
    for (let i = 0; i < rating; i++) {
        const label = starElements[i];
        label.classList.remove('fa-regular');
        label.classList.add('fa-solid');
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const reviewElements = document.querySelectorAll('.review-post');
    reviewElements.forEach(reviewElement => {
        const rating = reviewElement.querySelector('.rating-number').textContent.trim();
        const starElements = reviewElement.querySelectorAll('.rating i');
        fillStars(rating, starElements);
    });
});

// Add a click event listener to each star
stars.forEach(star => {
    star.addEventListener('click', function () {
        // Get the label for the clicked star
        const label = document.querySelector(`label[for="${this.id}"] i`);

        // Remove the 'fa-regular' class and add the 'fa-solid' class
        label.classList.remove('fa-regular');
        label.classList.add('fa-solid');

        // Reset all other stars
        stars.forEach(otherStar => {
            if (otherStar !== this) {
                const otherLabel = document.querySelector(`label[for="${otherStar.id}"] i`);
                otherLabel.classList.remove('fa-solid');
                otherLabel.classList.add('fa-regular');
            }
        });

        // Fill the previous stars
        let currentStar = this;

        // Get current star value
        let currentStarValue = currentStar.value;

        // Fill the previous stars
        for (let i = currentStarValue - 1; i >= 0; i--) {
            const label = document.querySelector(`label[for="${stars[i].id}"] i`);
            label.classList.remove('fa-regular');
            label.classList.add('fa-solid');
        }


    });
});

// Show a message when trying to post a review without filling all the fields
$("#post-review-btn").click(function () {

    let reviewRating = $("input[name='rating']:checked").val();
    let reviewTitle = $("#reviewTitle").val();
    let reviewText = $("#comment").val();

    if (reviewRating === undefined || reviewTitle === "" || reviewText === "") {
        $("#error-message").show();
    }
});

$(() => {
    $("#no-more-reviews").hide();
    $("#load-more-spinner").hide();
    $("#no-reviews").hide();

    // If there are no reviews to load, hide the button and show the message
    if ($("#reviewsArea").children().length === 0) {
        $("#load-more-btn").hide();
        $("#no-reviews").show();
    }

    let currentPage = 1;

    $("#load-more-btn").click(function () {
        $("#load-more-spinner").show();
        let currentBookID = $("#bookID").text();
        $.ajax({
            type: "GET",
            url: "/book/" + currentBookID + "/loadMoreReviews?page=" + currentPage + "&pageSize=6",
            success: function (data) {
                $("#load-more-spinner").hide();
                // if there are no more reviews to load, hide the button
                if (data.trim() === "") {
                    $("#load-more-btn").hide();
                    $("#no-more-reviews").show();
                }
                else{
                    currentPage ++;
                    // data is raw html (we need to separate the reviews, so we can fill the stars for each one)
                    let reviews = $(data).filter(".col.mb-5");

                    // Append each review to the reviewsArea and fill the stars
                    reviews.each((ind, review) => {
                        $("#reviewsArea").append(review);
                        let rating = $(review).find(".rating-number").text().trim();
                        let starElements = $(review).find(".rating i");
                        fillStars(rating, starElements);
                    });

                }
            }
        })
    });

    // Change lists buttons depending on whether the book is read, reading or to read
    let bookStatus = $("#bookStatus").text().trim();
    console.log(bookStatus);
    if (bookStatus === "read") {
        $("#readButton").addClass("btn-primary-light").removeClass("btn-outline-dark");
    } else if (bookStatus === "reading") {
        $("#readingButton").addClass("btn-primary-light").removeClass("btn-outline-dark");
    } else if (bookStatus === "wanted") {
        $("#toReadButton").addClass("btn-primary-light").removeClass("btn-outline-dark");
    } else {
        $("#removeButton").addClass("btn-danger").removeClass("btn-outline-dark");

    }

})
