// Select all the star elements
const stars = document.querySelectorAll('.rating input[type="radio"]');

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

$(() => {
    $("#no-more-reviews").hide();
    $("#load-more-spinner").hide();
    // Mustache review template
    let reviewTemplate = `
            <div class="col mb-5">
                <div class="card h-100">
                    <!-- Product details-->
                    <div class="card-body p-4">
                        <div class="text-left">
                            <!-- Product name-->
                            <h5 class="fw-bolder" id="inputReviewAuthor">{{author}}</h5>
                            <h6>{{title}}</h6>
                            <div class="rating">
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                            </div>
                            <!-- Product price-->
                            <div id="inputReviewContent">{{content}}</div>
                        </div>
                    </div>
                </div>
            </div>
    `;

    let currentPage = 6;

    $("#load-more-btn").click(function () {
        $("#load-more-spinner").show();
        let currentBookID = $("#bookID").text();
        $.ajax({
            type: "POST",
            url: "/book/" + currentBookID + "/loadMoreReviews?page=" + currentPage + "&pageSize=6",
            contentType: "application/json",
            data: JSON.stringify({
                page: currentPage,
                size: 6
            }),
            success: function (data) {
                $("#load-more-spinner").hide();
                // if there are no more reviews to load, hide the button
                if (data.length === 0) {
                    $("#load-more-btn").hide();
                    $("#no-more-reviews").show();
                }
                currentPage += 6;
                data.forEach(review => {
                    let moreReviews = Mustache.render(reviewTemplate, review);
                    $("#reviewsArea").append(moreReviews);
                });
            }
        })
    });

})