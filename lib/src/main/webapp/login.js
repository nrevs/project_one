if(typeof(loginForm)==undefined){
    let loginform = document.querySelector('#loginform');
}
async function handleLoginForm(event) {
    event.preventDefault();
    const fetchOptions = {
        method: "POST",
        body: new FormData(loginform)}
    let params = {code:"login"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url, fetchOptions);
    let msg = await resp.json();
    mainHandler(msg);
}
if(loginform != null)loginform.addEventListener('submit', handleLoginForm);



async function forgotUsername(event) {
    event.preventDefault();
    let params = {code:"emailUsername"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url);
    let msg = await resp.json();
    mainHandler(msg);
}

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