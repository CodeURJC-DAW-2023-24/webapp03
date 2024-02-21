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

    let currentPage = 1;
    let pageSize = 4;

    $("#load-more-btn").click(function () {
        // spinner animation to button while loading
        $("#load-more-label").hide();
        $("#load-more-spinner").show();
        // AJAX request
        $.ajax({
            type: "GET",
            url: "/landingPage/loadMore?page=" + currentPage + "&size=" + pageSize,
            contentType: "application/json",
            success: function (data) {
                if(data.trim() === ""){
                    $("#load-more-btn").hide();
                    $(".load-more-container").append("<p style='text-align: center;'>No hay nada m&aacutes que mostrar.<br>Sigue leyendo para obtener m&aacutes recomendaciones personalizadas.</p>");
                }
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
            }
        });


    })

    // AJAX request to get data to build the most read genres chart
    $.ajax({
        url: "/landingPage/mostReadGenres",
        method: "GET",
        success: function (response){
            console.log(response);
        }
    })

})