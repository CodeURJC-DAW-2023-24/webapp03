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

// Mustache post template
    let postTemplateLeft = `
    <!-- Blog post-->
                    {{#postL}}
                    <div class="card mb-4 blog-post">
                        <div class="img-container">
                            <!-- Image goes here -->
                            <a href="#!"><img class="card-img-top"
                                              src="{{image}}"
                                              alt="..."/></a>
                        </div>
                        <div class="card-body">
                            <!-- Text and buttons go here -->
                            <div class="small text-muted">{{postDate}}</div>
                            <h2 class="card-title h4">{{title}}</h2>
                            <p class="card-text">{{description}}</p>
                            <a class="btn btn-primary" href="infoBookPage.html">Leer m&aacutes</a>
                        </div>
                    </div>
                    {{/postL}}
    `;

    let postTemplateRight = `
    <!-- Blog post-->
                    {{#postR}}
                    <div class="card mb-4 blog-post">
                        <div class="img-container">
                            <!-- Image goes here -->
                            <a href="#!"><img class="card-img-top"
                                              src="{{image}}"
                                              alt="..."/></a>
                        </div>
                        <div class="card-body">
                            <!-- Text and buttons go here -->
                            <div class="small text-muted">{{postDate}}</div>
                            <h2 class="card-title h4">{{title}}</h2>
                            <p class="card-text">{{description}}</p>
                            <a class="btn btn-primary" href="infoBookPage.html">Leer m&aacutes</a>
                        </div>
                    </div>
                    {{/postR}}
    `;

    let currentPage = 1;

    $("#load-more-btn").click(function () {
        // spinner animation to button while loading
        $("#load-more-label").hide();
        $("#load-more-spinner").show();
        // AJAX request
        $.ajax({
            type: "POST",
            url: "/landingPage/loadMore",
            contentType: "application/json",
            data: JSON.stringify({
                page: currentPage
            }),
            success: function (data) {
                // removes spinner animation from button after loading
                $("#load-more-spinner").hide();
                $("#load-more-label").show();
                if (currentPage < data.length) {
                    currentPage += 4;
                    let postL = data.slice(0, 2);
                    let postR = data.slice(2, 4);
                    let renderedPostL = Mustache.render(postTemplateLeft, {postL});
                    let renderedPostR = Mustache.render(postTemplateRight, {postR});
                    $("#left-post-column").append(renderedPostL);
                    $("#right-post-column").append(renderedPostR);
                    if (currentPage >= data.length) {
                        $("#load-more-btn").css("display", "none");
                    }
                } else {

                    $("#load-more-btn").css("display", "none");
                }
            }
        });


    })
})