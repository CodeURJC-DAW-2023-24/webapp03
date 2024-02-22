$(() => {

    function putStars(rating, starElements) {
        console.log(rating);
        console.log(rating < 4.0);
        if (rating >= 5.0) {
            $(starElements).children().eq(0).addClass("bi-star-fill");
            $(starElements).children().eq(1).addClass("bi-star-fill");
            $(starElements).children().eq(2).addClass("bi-star-fill");
            $(starElements).children().eq(3).addClass("bi-star-fill");
            $(starElements).children().eq(4).addClass("bi-star-fill");
        } else if (rating >= 4.5) {
            $(starElements).children().eq(0).addClass("bi-star-fill");
            $(starElements).children().eq(1).addClass("bi-star-fill");
            $(starElements).children().eq(2).addClass("bi-star-fill");
            $(starElements).children().eq(3).addClass("bi-star-fill");
            $(starElements).children().eq(4).addClass("bi-star-half");
        } else if (rating >= 4.0) {
            $(starElements).children().eq(0).addClass("bi-star-fill");
            $(starElements).children().eq(1).addClass("bi-star-fill");
            $(starElements).children().eq(2).addClass("bi-star-fill");
            $(starElements).children().eq(3).addClass("bi-star-fill");
            $(starElements).children().eq(4).addClass("bi-star");
        } else if (rating >= 3.5) {
            $(starElements).children().eq(0).addClass("bi-star-fill");
            $(starElements).children().eq(1).addClass("bi-star-fill");
            $(starElements).children().eq(2).addClass("bi-star-fill");
            $(starElements).children().eq(3).addClass("bi-star-half");
            $(starElements).children().eq(4).addClass("bi-star");
        } else if (rating >= 3.0) {
            $(starElements).children().eq(0).addClass("bi-star-fill");
            $(starElements).children().eq(1).addClass("bi-star-fill");
            $(starElements).children().eq(2).addClass("bi-star-fill");
            $(starElements).children().eq(3).addClass("bi-star");
            $(starElements).children().eq(4).addClass("bi-star");
        } else if (rating >= 2.5) {
            $(starElements).children().eq(0).addClass("bi-star-fill");
            $(starElements).children().eq(1).addClass("bi-star-fill");
            $(starElements).children().eq(2).addClass("bi-star-half");
            $(starElements).children().eq(3).addClass("bi-star");
            $(starElements).children().eq(4).addClass("bi-star");
        } else if (rating >= 2.0) {
            $(starElements).children().eq(0).addClass("bi-star-fill");
            $(starElements).children().eq(1).addClass("bi-star-fill");
            $(starElements).children().eq(2).addClass("bi-star");
            $(starElements).children().eq(3).addClass("bi-star");
            $(starElements).children().eq(4).addClass("bi-star");
        } else if (rating >= 1.5) {
            $(starElements).children().eq(0).addClass("bi-star-fill");
            $(starElements).children().eq(1).addClass("bi-star-half");
            $(starElements).children().eq(2).addClass("bi-star");
            $(starElements).children().eq(3).addClass("bi-star");
            $(starElements).children().eq(4).addClass("bi-star");
        } else if (rating >= 1.0) {
            $(starElements).children().eq(0).addClass("bi-star-fill");
            $(starElements).children().eq(1).addClass("bi-star");
            $(starElements).children().eq(2).addClass("bi-star");
            $(starElements).children().eq(3).addClass("bi-star");
            $(starElements).children().eq(4).addClass("bi-star");
        } else {
            $(starElements).children().eq(0).addClass("bi-star-half");
            $(starElements).children().eq(1).addClass("bi-star");
            $(starElements).children().eq(2).addClass("bi-star");
            $(starElements).children().eq(3).addClass("bi-star");
            $(starElements).children().eq(4).addClass("bi-star");
        }

    }

    $(".stars").each((ind, starElements) => {
        console.log($(starElements))
        putStars($(".rating").eq(ind).text(), $(starElements));
    });

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


    let currentPage = 1;

    if (currentPage * 4 >= $("#maxBooks").val()) {
        $("#loadMoreBut").hide();
    }

    if (!(($(".book")).length)) {
        $("#noResults").show()
    }

    $("#loadMoreBut").click((event) => {
        $("#loadMoreBut i").addClass("fa-spin");
        // AJAX request
        url = window.location.href;
        let query = url.substring(url.lastIndexOf("?") + 7, url.length);

        $.ajax({
            type: "GET",
            url: "/search/loadMore?query=" + query + "&page=" + currentPage,
            success: function (data) {
                $("#loadMoreBut i").removeClass("fa-spin");
                $("#books").append(data)
                currentPage++;
                if (currentPage * 4 >= $("#maxBooks").val()) {
                    $("#loadMoreBut").hide();
                }
            }
        });
        setTimeout(() => {
            console.log($(".stars").slice((currentPage - 1) * 4, $(".stars").length));
            $(".stars").slice((currentPage - 1) * 4, $(".stars").length).each((ind, starElements) => {
                console.log(ind + 4);
                putStars($(".rating").eq(ind + ((currentPage - 1) * 4)).text(), $(starElements));
            });
        }, 100);

    });
});