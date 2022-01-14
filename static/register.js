const usernameDOM = document.getElementById('username');
const passwordDOM = document.getElementById('password');
const confirmPasswordDOM = document.getElementById('confirmPassword');
const emailDOM = document.getElementById('email');

function validatePassword(password, confirmPassword) {
    if (password != confirmPassword) {
        confirmPasswordDOM.setCustomValidity('Passwords do not match.');
        return false;
    } else {
        confirmPasswordDOM.setCustomValidity('');
        return true;
    }
}

function register() {
    const username = usernameDOM.value;
    const password = passwordDOM.value;
    const confirmPassword = confirmPasswordDOM.value;
    const email = emailDOM.value;
    const passwordCheck = validatePassword(password, confirmPassword);
    if(!passwordCheck){return;}
    registerUser(username, password, email).then(response => {
        if (response.success) {
            window.location.href = '/';
        } else {
            window.location.href = '/something-went-wrong.html';
        }
    });
}

async function registerUser(username, password, email) {
    const response = await fetch('/api/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password,
            email: email
        })
    });
    return response.json();
}