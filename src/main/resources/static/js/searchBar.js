$(() => {
    function loadQuery(query) {
        let searchQuery = $(query).val();
        if (searchQuery.trim() !== "") {
            let url = '/search?query=' + encodeURIComponent(searchQuery);
            window.location.assign(url);
        }
    }


    $("#magnifier-icon").on("click", (e) => {
        loadQuery("#search-bar");
    });


    $("#search-bar").on("keydown", (e) => {
        if (e.keyCode === 13) {
            $("#magnifier-icon").click();
        }
    });
});


