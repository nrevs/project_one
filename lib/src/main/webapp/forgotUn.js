if (typeof(unForm)==undefined) {
    let unForm = document.querySelector('#unForm');
}
async function handleForgotUnForm(event) {
    event.preventDefault();
    const fetchOptions = {
        method: "POST",
        body: new FormData(unForm)}
    let params = {code:"emailUsername"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url, fetchOptions);
    let msg = await resp.json();
    mainHandler(msg);
}
if(unForm != null)unForm.addEventListener('submit', handleForgotUnForm);




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