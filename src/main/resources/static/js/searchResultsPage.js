$(() => {
    $("#load-more-spinner").hide();
    $("#noResults").css("display", "none");
    $("#loadMoreBut").parent().css({"display": "flex", "justify-content": "center", "text-align": "center"});
    $("#loadMoreBut").css({
        "width": "85%",
        "position": "relative",
        "display": "flex",
        "justify-content": "center",
        "text-align": "center",
        "align-items": "center"
    });
    $("#loadMoreBut i").css({"margin-right": "10px"});

    // Mustache book template
    let bookTemplate = `
    {{#bookQueries}}
            <!-- Book card-->
            <div class="col mb-5">
                <div class="card h-100">
                    <!-- Read badge-->
                    <div class="badge bg-dark text-white position-absolute" style="top: 0.5rem; right: 0.5rem">Le&iacutedo
                    </div>
                    <!-- Book cover image-->
                    <img class="card-img-top" src="{{image}}" alt="..."/>
                    <!-- Product details-->
                    <div class="card-body p-4">
                        <div class="text-center">
                            <!-- Book name-->
                            <h5 class="fw-bolder">{{title}}</h5>
                            <!-- Book Author-->
                            <h6 class="fw-normal">{{author}}</h6>
                            <!-- Product reviews-->
                            <div class="d-flex justify-content-center small text-warning mb-2">
                                <div class="bi-star-fill"></div>
                                <div class="bi-star-fill"></div>
                                <div class="bi-star-fill"></div>
                                <div class="bi-star-fill"></div>
                                <div class="bi-star"></div>
                            </div>
                        </div>
                    </div>
                    <!-- Product actions-->
                    <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                        <div class="text-center"><a class="btn btn-outline-dark mt-auto" href="/book/{{id}}">Ver
                            libro</a>
                        </div>
                    </div>
                </div>
            </div> <!-- End of book card-->
            {{/bookQueries}}
    `;

    let currentPage = 4;

    $("#loadMoreBut").click((event) => {
        $("#load-more-spinner").show();
        // AJAX request
        url = window.location.href;
        let query = ((url).substring(url.indexOf("=") + 1, url.length));
        $.ajax({
            type: "POST",
            url: "/search?query=" + query,
            contentType: "application/json",
            success: function (data) {
                $("#load-more-spinner").hide();
                if (data.length > 4 && event.which) {
                    let newBooks = data.slice(currentPage, currentPage + 4);
                    newBooks = Mustache.render(bookTemplate, {bookQueries: newBooks});
                    $(".row-cols-2").append(newBooks);
                    currentPage += 4;
                    if (currentPage >= data.length) {
                        $("#loadMoreBut").css("display", "none");
                    }
                } else if ((data.length > 0) && (data.length <= 4)) {
                    $("#loadMoreBut").css("display", "none");
                } else if (data.length == 0) {
                    $("#loadMoreBut").css("display", "none");
                    $("#noResults").css("display", "block");
                }
            }
        });
    });

    $("#loadMoreBut").click();
})