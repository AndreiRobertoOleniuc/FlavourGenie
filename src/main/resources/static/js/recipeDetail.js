document.getElementById("ingredients-button").addEventListener("click", ()=>{
    document.getElementById("ingredientContent").classList.toggle("close");
    document.getElementById("ingredients-button").children[0].classList.toggle("rotate")
})


document.getElementById("instruction-button").addEventListener("click", ()=>{
    document.getElementById("instructionContent").classList.toggle("close");
    document.getElementById("instruction-button").children[0].classList.toggle("rotate")
})

const ratingButtons = document.querySelectorAll('.rating-untouched .rating-button')

ratingButtons.forEach((button, index) => {
    button.addEventListener('mouseover', () => {
        // Add 'active' class to the hovered button and the ones before it
        for(let i = 0; i <= index; i++) {
            ratingButtons[i].querySelector('span').textContent = 'star';
        }
    });

    button.addEventListener('mouseout', () => {
        // Remove 'active' class from all buttons
        for(let i = 0; i < ratingButtons.length; i++) {
            ratingButtons[i].querySelector('span').textContent = 'star_border';
        }
    });
});

const ratingButtonsTouched = document.querySelectorAll('.rating-touched .rating-button')

let touchedUntil = 0 ;
ratingButtonsTouched.forEach((button, index) => {

    if(button.querySelector('span').textContent.trim() === 'star'){
        touchedUntil = index + 1;
        return;
    }
    button.addEventListener('mouseover', () => {
        // Add 'active' class to the hovered button and the ones before it
        for(let i = touchedUntil; i <= index; i++) {
            ratingButtonsTouched[i].querySelector('span').textContent = 'star';
        }
    });

    button.addEventListener('mouseout', () => {
        // Remove 'active' class from all buttons
        for(let i = touchedUntil; i < ratingButtonsTouched.length; i++) {
            ratingButtonsTouched[i].querySelector('span').textContent = 'star_border';
        }
    });
});
