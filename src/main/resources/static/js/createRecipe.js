document.querySelector('.creation').addEventListener('submit', function(event) {
    const ingredientList = document.getElementById('ingredient-list');
    if (ingredientList.value.length === 0) {
        event.preventDefault();
        showSnackbar('Please add at least one ingredient');
    }
});