$(() => {
    // Get username from userName-header element
    let username = $("#userName-header").text() // WE WILL HAVE TO CHANGE THIS WHEN WE HAVE THE SESSION CONTROL AND DDBB
    let token = $("#_csrf").attr("value");

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

    //show the empty lists message for each list if it's empty
    if ($("#read").children().length === 0) {
        $("#load-more-btn-read").hide();
        $("#read").append("<p style='text-align: center;'>Los libros que marques como le&iacutedos aparecer&aacuten aqu&iacute</p>");
    }

    if ($("#reading").children().length === 0) {
        $("#load-more-btn-reading").hide();
        $("#reading").append("<p style='text-align: center;'>Los libros que marques como leyendo aparecer&aacuten aqu&iacute</p>");
    }

    if ($("#wanted").children().length === 0) {
        $("#load-more-btn-wanted").hide();
        $("#wanted").append("<p style='text-align: center;'>Los libros que marques como deseados aparecer&aacuten aqu&iacute</p>");
    }

    $(".stars").each((ind, starElements) => {
        putStars($(".rating").eq(ind).text(), $(starElements));
    });

    let pageSize = 4
    let bookContRead = 1
    $("#load-more-btn-read").on("click", () => {
        // Rotate the icon of the button while loading
        $("#load-more-btn-read i").addClass("fa-spin");
        $.ajax({
            type: "GET",
            url: "/profile/" + username + "/loadMore?listType=read&page=" + bookContRead + "&size=" + pageSize,
            success: function (data) {
                // Stop the rotation of the icon
                $("#load-more-btn-read i").removeClass("fa-spin");
                if (data.trim() === "") {
                    $("#load-more-btn-read").hide()
                    $("#read").append("<p style='text-align: center;'>No hay m&aacutes libros</p>");
                } else {
                    bookContRead++
                    // data is raw html
                    $("#read").append(data)
                    $(".stars").slice((bookContRead - 1) * 4, $(".stars").length).each((ind, starElements) => {
                        putStars($(".rating").eq(ind + ((bookContRead - 1) * 4)).text(), $(starElements));
                    });
                }

            }
        })
    })

    let bookContReading = 1
    $("#load-more-btn-reading").on("click", () => {
        // Rotate the icon of the button while loading
        $("#load-more-btn-reading i").addClass("fa-spin");
        $.ajax({
            type: "GET",
            url: "/profile/" + username + "/loadMore?listType=reading&page=" + bookContReading + "&size=" + pageSize,
            success: function (data) {
                // Stop the rotation of the icon
                $("#load-more-btn-reading i").removeClass("fa-spin");
                if (data.trim() === "") {
                    $("#load-more-btn-reading").hide()
                    $("#reading").append("<p style='text-align: center;'>No hay m&aacutes libros</p>");
                } else {
                    bookContReading++
                    // data is raw html
                    $("#reading").append(data)
                    $(".stars").slice((bookContReading - 1) * 4, $(".stars").length).each((ind, starElements) => {
                        putStars($(".rating").eq(ind + ((bookContReading - 1) * 4)).text(), $(starElements));
                    });
                }
            }
        })
    })

    let bookContWanted = 1
    $("#load-more-btn-wanted").on("click", () => {
        // Rotate the icon of the button while loading
        $("#load-more-btn-wanted i").addClass("fa-spin");
        $.ajax({
            type: "GET",
            url: "/profile/" + username + "/loadMore?listType=wanted&page=" + bookContWanted + "&size=" + pageSize,
            success: function (data) {
                // Stop the rotation of the icon
                $("#load-more-btn-wanted i").removeClass("fa-spin");
                if (data.trim() === "") {
                    $("#load-more-btn-wanted").hide()
                    $("#wanted").append("<p style='text-align: center;'>No hay m&aacutes libros</p>");
                } else {
                    bookContWanted++
                    // data is raw html
                    $("#wanted").append(data)
                    $(".stars").slice((bookContWanted - 1) * 4, $(".stars").length).each((ind, starElements) => {
                        putStars($(".rating").eq(ind + ((bookContWanted - 1) * 4)).text(), $(starElements));
                    });
                }
            }
        })
    })

    $("#csvExportButton").on("click", () => {
        $.ajax({
            type: "GET",
            url: "https://" + window.location.host + "/profile/" + username + "/exportLists",
            success: function (data) {
                let link = document.createElement('a');
                link.href = "/profile/" + username + "/exportLists";
                link.download = "books.csv";
                link.style.display = 'none';

                document.body.appendChild(link);

                link.click();

                document.body.removeChild(link);
            }
        })
    });

    $("#csvImportBtn").on("click", () => {
        $("#importIn").click();
    });

    $("#importIn").on("change", () => {
        let decision = confirm("Esto sobreescribirá tus listas actuales. ¿Estás seguro?");
        if (decision) {
            let file = $("#importIn")[0].files[0];
            $.ajax({
                type: "POST",
                url: "https://" + window.location.host + "/profile/" + username + "/importLists",
                data: file,
                datatype: "json",
                contentType: "text/csv",
                processData: false,
                beforeSend: (xhr) => {
                    xhr.setRequestHeader("X-CSRF-TOKEN", token);
                },
                success: () => {
                    location.reload();
                },
                error: (err) => {
                    console.log(err);
                }
            });
        }
        //location.reload();
    });
})