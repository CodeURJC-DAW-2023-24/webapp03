$(() => {
    $("#editProfileBut").on("click", () => {
        window.location.assign( window.location.href + "/edit");
    });

    $("#saveChangesBut").on("click", () => {
        window.location.assign( window.location.href.slice(0, -5));
    });
})