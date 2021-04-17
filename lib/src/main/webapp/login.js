
let loginform = document.querySelector('#loginform');

async function handleForm(event) {
    event.preventDefault();
    const fetchOptions = {
        method: "POST",
        body: new FormData(loginform)}
    console.log("I got called");
    await fetch(api_url, fetchOptions);
    }

loginform.addEventListener('submit', handleForm)