$(() => {
    $("#load-more-spinner").hide();
    $("#load-more-btn").parent().css({"display": "flex", "justify-content": "center", "text-align": "center"});
    $("#load-more-btn").css({
        "width": "85%",
        "position": "relative",
        "display": "flex",
        "justify-content": "center",
        "text-align": "center",
        "align-items": "center"
    });
    $("#load-more-btn i").css({"margin-right": "10px"});

    let currentPage = 0;

    $("#load-more-btn").click(function () {
        // spinner animation to button while loading
        $("#load-more-label").hide();
        $("#load-more-spinner").show();
        // AJAX request
        $.ajax({
            type: "GET",
            url: "/landingPage/loadMore",
            contentType: "application/json",
            success: function (data) {
                currentPage++;
                $("#load-more-spinner").hide();
                $("#load-more-label").show();
                let posts = $(data).filter(".blog-post");
                let half = Math.ceil(posts.length / 2);
                let leftPosts = posts.slice(0, half);
                let rightPosts = posts.slice(half, posts.length);
                console.log(leftPosts);
                console.log(rightPosts);
                $("#left-post-column").append(leftPosts);
                $("#right-post-column").append(rightPosts);
                if (posts.length < 6) {
                    $("#load-more-btn").css("display", "none");
                }
            }
        });


    })
})