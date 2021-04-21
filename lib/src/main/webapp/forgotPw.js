let pwForm = document.querySelector('#pwForm');
async function handleForgotPwForm(event) {
    event.preventDefault();
    const fetchOptions = {
        method: "POST",
        body: new FormData(pwForm)}
    let params = {code:"email"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url, fetchOptions);
    let msg = await resp.json();
    mainHandler(msg);
}
if(pwForm != null)pwForm.addEventListener('submit', handleForgotPwForm);



async function forgotUsername(event) {
    event.preventDefault();
    let params = {code:"emailUsername"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url);
    let msg = await resp.json();
    mainHandler(msg);
    return false;
}


async function create(event) {
    event.preventDefault();
    let params = {code:"create"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url);
    let msg = await resp.json();
    mainHandler(msg);
}