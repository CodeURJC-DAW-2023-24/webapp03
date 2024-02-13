document.getElementById('search-bar').addEventListener('input', function () {
    var searchQuery = this.value;
    document.getElementById('magnifier-icon-link').href = '/search?query=' + encodeURIComponent(searchQuery);
});