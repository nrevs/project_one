if(typeof(createForm)==undefined) {
    let createForm = document.querySelector('#create');
}
async function handleCreateForm(event) {
    event.preventDefault();
    const fetchOptions = {
        method: "POST",
        body: new FormData(createForm)}
    let params = {code:"create"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url, fetchOptions);
    let msg = await resp.json();
    mainHandler(msg);
}
if(createForm != null)createForm.addEventListener('submit', handleCreateForm);




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