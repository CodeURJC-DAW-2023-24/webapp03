$(() => {

    function putStars(rating, starElements) {
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
        putStars($(".rating").eq(ind).text(), $(starElements));
    });

    $("#load-more-spinner").hide();
    $("#noResults").css("display", "none");
    $("#loadMoreBut").parent().css({"display": "flex", "justify-content": "center", "text-align": "center"});
    $("#loadMoreBut").css({
        "width": "55%",
        "position": "relative",
        "display": "flex",
        "justify-content": "center",
        "text-align": "center",
        "align-items": "center"
    });
    $("#loadMoreBut i").css({"margin-right": "10px"});

    let currentPage = 1;

    if (currentPage * 4 >= $("#maxBooks").val()) {
        $("#loadMoreBut").hide();
    }

    if (!(($(".book")).length) && !($(".user")).length) {
        $("#noResults").show()
    }

    $("#loadMoreBut").click((event) => {
        $("#loadMoreBut i").addClass("fa-spin");
        // AJAX request
        url = window.location.href;
        let query = url.substring(url.lastIndexOf("&") + 7, url.length);

        let userSearch = localStorage.getItem("userSearch");


        $.ajax({
            type: "GET",
            url: "https://" + window.location.host + "/search/loadMore?userSearch=" + userSearch + "&query=" + query + "&page=" + currentPage,
            success: (data) => {
                $("#loadMoreBut i").removeClass("fa-spin");

                currentPage++;
                if (userSearch === "false") {
                    $("#books").append(data);

                    if (currentPage * 4 >= $("#maxBooks").val()) {
                        $("#loadMoreBut").hide();
                    }

                } else {
                    $("#users").append(data);

                    if (currentPage * 4 >= $("#maxUsers").val()) {
                        $("#loadMoreBut").hide();
                    }
                }
                if (currentPage * 4 >= $("#maxBooks").val()) {
                    $("#loadMoreBut").hide();
                }
            }
        });
        if (userSearch) {
            setTimeout(() => {
                $(".stars").slice((currentPage - 1) * 4, $(".stars").length).each((ind, starElements) => {
                    putStars($(".rating").eq(ind + ((currentPage - 1) * 4)).text(), $(starElements));
                });
            }, 100);
        }

    });
})
;