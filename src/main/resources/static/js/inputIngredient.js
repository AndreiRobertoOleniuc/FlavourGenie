function updateIngredientList() {
    const chips = document.querySelectorAll('.chip');
    const ingredientList = Array.from(chips).map(chip => chip.textContent.slice(0, -1)); // slice to remove the '×' character
    document.getElementById('ingredient-list').value = ingredientList.join(',');
}

function addIngredient() {
    const input = document.getElementById('ingredient-input');
    if (input.value.trim() !== "") {
        const chipsContainer = document.getElementById('chips-container');
        const newChip = document.createElement('div');
        newChip.className = 'chip';
        const value = input.value.trim();
        newChip.textContent = value;

        const closeSpan = document.createElement('span');
        closeSpan.className = 'close';
        closeSpan.innerHTML = '&times;';
        closeSpan.onclick = function() {
            chipsContainer.removeChild(newChip);
            updateIngredientList();  // Update the list when a chip is removed
        };
        newChip.appendChild(closeSpan);
        chipsContainer.appendChild(newChip);
        input.value = ''; // Clear the input field after adding a chip
        updateIngredientList();  // Update the list when a chip is removed
    }
}

function handleKeyPress(event) {
    if (event.key === "Enter") {
        addIngredient();
        event.preventDefault(); // Prevent form submission or any default action
    }
}

document.getElementById('ingredient-input').addEventListener('keypress', handleKeyPress);
document.getElementById('add-icon').addEventListener('click', addIngredient);