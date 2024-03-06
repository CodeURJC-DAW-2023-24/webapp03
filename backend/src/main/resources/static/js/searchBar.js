$(() => {
    function loadQuery(query) {
        let searchQuery = $(query).val();
        if (searchQuery.trim() !== "") {
            let userSearch = $("#search-select").is(":checked");
            localStorage.setItem("userSearch", userSearch);
            let url = "https://" + window.location.host + "/search?users=" + userSearch + "&query=" + encodeURIComponent(searchQuery);
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


