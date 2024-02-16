// Select all the star elements
const stars = document.querySelectorAll('.rating input[type="radio"]');

// Add a click event listener to each star
stars.forEach(star => {
    star.addEventListener('click', function () {
        // Get the label for the clicked star
        const label = document.querySelector(`label[for="${this.id}"] i`);

        // Remove the 'fa-regular' class and add the 'fa-solid' class
        label.classList.remove('fa-regular');
        label.classList.add('fa-solid');

        // Reset all other stars
        stars.forEach(otherStar => {
            if (otherStar !== this) {
                const otherLabel = document.querySelector(`label[for="${otherStar.id}"] i`);
                otherLabel.classList.remove('fa-solid');
                otherLabel.classList.add('fa-regular');
            }
        });

        // Fill the previous stars
        let currentStar = this;

        // Get current star value
        let currentStarValue = currentStar.value;

        // Fill the previous stars
        for (let i = currentStarValue - 1; i >= 0; i--) {
            const label = document.querySelector(`label[for="${stars[i].id}"] i`);
            label.classList.remove('fa-regular');
            label.classList.add('fa-solid');
        }


    });
});