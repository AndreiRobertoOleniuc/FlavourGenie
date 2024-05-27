function showSnackbar(message) {
    const snackbar = document.getElementById('notification');
    snackbar.className.replace('show', '');
    snackbar.innerText = message;
    snackbar.className = 'show';

    setTimeout(function() {
        snackbar.className = snackbar.className.replace('show', '');
    }, 8000);
}

const notificationBtn = document.querySelectorAll(".notificationBtn")
notificationBtn.forEach((button) => {
    button.addEventListener('click', (event) => {
        let message = button.getAttribute("data-message");
        showSnackbar(message);
    });
})