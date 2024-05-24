//Toggle the ingredients and instructions
document.getElementById("ingredients-button").addEventListener("click", ()=>{
    document.getElementById("ingredientContent").classList.toggle("close");
    document.getElementById("ingredients-button").children[0].classList.toggle("rotate")
})
document.getElementById("instruction-button").addEventListener("click", ()=>{
    document.getElementById("instructionContent").classList.toggle("close");
    document.getElementById("instruction-button").children[0].classList.toggle("rotate")
})


//Hover Ratings
const ratingButtons = document.querySelectorAll(' .rating-button')
let touchedUntil = 0 ;
ratingButtons.forEach((button, index) => {
    if(button.querySelector('span').textContent.trim() === 'star'){
        touchedUntil = index + 1;
        return;
    }
    button.addEventListener('mouseover', () => {
        // Add 'active' class to the hovered button and the ones before it
        for(let i = touchedUntil; i <= index; i++) {
            ratingButtons[i].querySelector('span').textContent = 'star';
        }
    });

    button.addEventListener('mouseout', () => {
        // Remove 'active' class from all buttons
        for(let i = touchedUntil; i < ratingButtons.length; i++) {
            ratingButtons[i].querySelector('span').textContent = 'star_border';
        }
    });
});

//Click Ratings
ratingButtons.forEach((button, index) => {
    button.addEventListener('click', (event) => {
        event.preventDefault();
        event.stopPropagation();

        let id = document.querySelector(".recipeDetail").id;
        let rating = index + 1;

        fetch(`/feedback/${id}/${rating}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        })
            .then(response => response.json())
            .then(data => {
                document.querySelector(".feedback_createdBy").setAttribute("data-rating",data.rating);
                renderRating();
            })
            .catch(error => console.error('Error:', error));
    });
});

const renderRating = () =>{
    let rating = +document.querySelector(".feedback_createdBy").getAttribute("data-rating");
    touchedUntil = rating;
    for(let i = 0; i < rating; i++){
        ratingButtons[i].querySelector('span').textContent = 'star';
    }
}

renderRating();
