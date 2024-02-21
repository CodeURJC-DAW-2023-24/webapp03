// Select all the star elements
const stars = document.querySelectorAll('.rating input[type="radio"]');

function fillStars(rating, starElements) {
    for (let i = 0; i < rating; i++) {
        const label = starElements[i];
        label.classList.remove('fa-regular');
        label.classList.add('fa-solid');
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const reviewElements = document.querySelectorAll('.col.mb-5');
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

// Function to fill stars based on rating

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

})