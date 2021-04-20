
let loginform = document.querySelector('#loginform');

async function handleForm(event) {
    event.preventDefault();
    const fetchOptions = {
        method: "POST",
        body: new FormData(loginform)}
    console.log("I got called");
    let resp = await fetch(login_url, fetchOptions);
    let msg = await resp.json();
    mainHandler(msg);
}

if(loginform != null)loginform.addEventListener('submit', handleForm);