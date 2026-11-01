
document.addEventListener("DOMContentLoaded", () => {

    const slider =
        document.querySelector(".destination-slider");

    const grid =
        document.querySelector(".destination-grid");

    const cards =
        document.querySelectorAll(".destination-card");

    const previousButton =
        document.querySelector(".destination-arrow-left");

    const nextButton =
        document.querySelector(".destination-arrow-right");


    if (
        !slider ||
        !grid ||
        cards.length === 0
    ) {
        return;
    }


    let currentIndex = 0;


    function getVisibleCards() {

        if (window.innerWidth <= 600) {
            return 1;
        }

        if (window.innerWidth <= 900) {
            return 2;
        }

        return 3;
    }


    function updateCarousel() {

        const visibleCards =
            getVisibleCards();

        const maxIndex =
            Math.max(
                0,
                cards.length - visibleCards
            );


        if (currentIndex > maxIndex) {
            currentIndex = maxIndex;
        }


        const cardWidth =
            cards[0].offsetWidth;

        const gap = 25;


        grid.style.transform =
            `translateX(-${currentIndex * (cardWidth + gap)}px)`;


        previousButton.disabled =
            currentIndex === 0;

        nextButton.disabled =
            currentIndex >= maxIndex;

    }


    nextButton.addEventListener(
        "click",
        () => {

            const visibleCards =
                getVisibleCards();

            const maxIndex =
                Math.max(
                    0,
                    cards.length - visibleCards
                );


            if (currentIndex < maxIndex) {
                currentIndex++;
                updateCarousel();
            }

        }
    );


    previousButton.addEventListener(
        "click",
        () => {

            if (currentIndex > 0) {
                currentIndex--;
                updateCarousel();
            }

        }
    );


    window.addEventListener(
        "resize",
        updateCarousel
    );


    updateCarousel();

});
