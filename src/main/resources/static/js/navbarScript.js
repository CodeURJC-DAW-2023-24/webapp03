window.onscroll = function() {
    let navbar = document.querySelector('.bm-navbar');
    if (window.scrollY > 0) {
        navbar.classList.add('bm-navbar-scrolled');
    } else {
        navbar.classList.remove('bm-navbar-scrolled');
    }
};