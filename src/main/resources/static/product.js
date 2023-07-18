document.querySelectorAll('button').forEach((button) => {
    button.addEventListener('click', (event) => {
        let productId = event.target.dataset.productId;

        fetch(`/add-to-cart/${productId}`, {
            method: 'POST'
        }).then(response => response.json())
            .then(data => console.log(data))
            .catch((error) => {
                console.error('Error:', error);
            });
    });
});
