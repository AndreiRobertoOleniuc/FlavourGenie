document.getElementById("ingredients-button").addEventListener("click", ()=>{
    document.getElementById("ingredientContent").classList.toggle("close");
    document.getElementById("ingredients-button").children[0].classList.toggle("rotate")
})


document.getElementById("instruction-button").addEventListener("click", ()=>{
    document.getElementById("instructionContent").classList.toggle("close");
    document.getElementById("instruction-button").children[0].classList.toggle("rotate")
})
