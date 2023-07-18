document.getElementById("registerForm").addEventListener("submit", function(event) {
    event.preventDefault();

    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    let data = {
        username: username,
        password: password
    };

    fetch('/api/users/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Error: ' + response.status);
            }
        })
        .then(data => {
            console.log("Rejestracja zakończona pomyślnie, przekierowuję do logowania");
            window.location.href = "/login";
        })
        .catch((error) => {
            console.error('Error:', error);
        });
});
