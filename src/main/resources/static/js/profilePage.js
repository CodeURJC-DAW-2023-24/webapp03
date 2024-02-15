$(() => {
    let bookListTemplate = `
        <a href="/book/{{ID}}" class="book-link-disabled-decorations">
        <div class="single_advisor_profile wow fadeInUp" data-wow-delay="0.3s"
            style="visibility: visible; animation-delay: 0.3s; animation-name: fadeInUp;">
            <div class="in-card-book-cover-image"><img
                    src="{{image}}" alt>
            </div>
            <div class="single_advisor_details_info">
                <h6>{{title}}</h6>
                <p class="designation">{{author}}</p>
                <div class="rating">
                    <div class="d-flex justify-content-end small text-warning mb-2">
                        <div class="bi-star-fill"></div>
                        <div class="bi-star-fill"></div>
                        <div class="bi-star-fill"></div>
                        <div class="bi-star-half"></div>
                        <div class="bi-star"></div>
                    </div>
                </div>
            </div>
        </div>
    </a>
    `
    let bookContReaded = 1
    $("#loadMoreBut-1").on("click", () => {
        $.ajax({
            type: "POST",
            url: "/profile/{{username}}",
            contentType: "application/json" ,
            data: JSON.stringify({
                page: bookContReaded,
                size: 3
            })
            , success:function(data){
                bookContReaded += 3 
                let moreBooks = Mustache.render(bookListTemplate, {
                    readedBooks:data
                })
                $("#readed").append(moreBooks)
            }
        })
    })

    let bookContReading = 1
    $("#loadMoreBut-2").on("click", () => {
        $.ajax({
            type: "POST",
            url: "/profile/{{username}}",
            contentType: "application/json" ,
            data: JSON.stringify({
                page: bookContReading,
                size: 3
            })
            , success:function(data){
                bookContReading += 3 
                let moreBooks = Mustache.render(bookListTemplate, {
                    readingBooks:data
                })
                $("#reading").append(moreBooks)
            }
        })
    })

    let bookContWanted = 1
    $("#loadMoreBut-3").on("click", () => {
        $.ajax({
            type: "POST",
            url: "/profile/{{username}}",
            contentType: "application/json" ,
            data: JSON.stringify({
                page: bookContReading,
                size: 3
            })
            , success:function(data){
                bookContReading += 3 
                let moreBooks = Mustache.render(bookListTemplate, {
                    wantedBooks:data
                })
                $("#wanted").append(moreBooks)
            }
        })
    })
})