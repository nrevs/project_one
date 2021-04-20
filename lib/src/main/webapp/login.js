
let loginform = document.querySelector('#loginform');

async function handleForm(event) {
    event.preventDefault();
    const fetchOptions = {
        method: "POST",
        body: new FormData(loginform)}
    let params = {code:"I got called"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url, fetchOptions);
    let msg = await resp.json();
    mainHandler(msg);
}

if(loginform != null)loginform.addEventListener('submit', handleForm);



async function forgotUsername(event) {
    event.preventDefault();
    let params = {code:"emailUsername"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url);
    let msg = await resp.json();
    mainHandler(msg);
    return false;
}

//let funa = document.querySelector('#forgotuname');
//if(funa != null) funa.addEventListener('onclick', forgotUsername);



async function forgotPassword(event) {
    event.preventDefault();
    let params = {code:"emailPasswordReset"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url);
    let msg = await resp.json();
    mainHandler(msg);
}

async function create(event) {
    event.preventDefault();
    let params = {code:"create"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url);
    let msg = await resp.json();
    mainHandler(msg);
}