function signUp() {
    const firstName = document.querySelector('#firstName').value;
    const lastName = document.querySelector('#lastName').value;
    const email = document.querySelector('#email').value;
    const phone = document.querySelector('#phone').value;
    const password = document.querySelector('#password').value;
    const confirmPassword = document.querySelector('#confirm-password').value;

    const nodeFirstName = document.querySelector('#invalid-first-name');
    const nodeLastName = document.querySelector('#invalid-last-name');
    const nodeEmail = document.querySelector('#invalid-email');
    const nodePhone = document.querySelector('#invalid-phone');
    const nodePassword = document.querySelector('#invalid-password');
    const nodeConfirmPassword = document.querySelector('#invalid-confirm-password');

    let message;

    nodeFirstName.textContent = '';
    nodeLastName.textContent = '';
    nodeEmail.textContent = '';
    nodePhone.textContent = '';
    nodePassword.textContent = '';
    nodeConfirmPassword.textContent = '';

    if (!firstName || firstName.trim() === '') {
        message = document.createTextNode('Invalid First Name');
        nodeFirstName.appendChild(message);
    } else if (!lastName || lastName.trim() === '') {
        message = document.createTextNode('Invalid Last Name');
        nodeLastName.appendChild(message);
    } else if (!email || email.trim() === '') {
        message = document.createTextNode('Invalid Email');
        nodeEmail.appendChild(message);
    } else if (!phone || phone.trim() === '') {
        message = document.createTextNode('Invalid Phone');
        nodePhone.appendChild(message);
    } else if (!password || password.trim() === '') {
        message = document.createTextNode('Invalid Password');
        nodePassword.appendChild(message);
    } else if (!confirmPassword || confirmPassword.trim() === '') {
        message = document.createTextNode('Invalid Confirm Password');
        nodeConfirmPassword.appendChild(message);
    } else if (password !== confirmPassword) {
        message = document.createTextNode('Confirm Password Don\'t Equals Password');
        nodeConfirmPassword.appendChild(message);
    } else {
        const bodySignUp = {
            'firstName': firstName,
            'lastName': lastName,
            'email': email,
            'phone': phone,
            'password': password
        };

        const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

        fetch(`http://localhost:8080/web-api/sign-up`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'X-XSRF-TOKEN': csrfToken
            },
            body: JSON.stringify(bodySignUp)
        })
            .then(response => response.text())
            .then(data => {
                if (data.includes('message')) {
                    const indexStart = data.indexOf(':') + 2;
                    const indexEnd = data.length - 2;
                    const message = data.substring(indexStart, indexEnd);

                    if (message.includes('Phone')) {
                        nodePhone.appendChild(document.createTextNode(message));
                    } else if (message.includes('E-mail')) {
                        nodeEmail.appendChild(document.createTextNode(message));
                    }
                } else {
                    window.location.href = '/login';
                }
            })
            .catch(error => console.log('Request failed', error));
    }
}