if(typeof(resetForm)==undefined){
    let resetForm = document.querySelector('#reset');
}
async function handleResetForm(event) {
    event.preventDefault();
    const fetchOptions = {
        method: "POST",
        body: new FormData(resetForm)}
    let params = {code:"reset"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url, fetchOptions);
    let msg = await resp.json();
    mainHandler(msg);
}
if(resetForm != null)createForm.addEventListener('submit', handleResetForm);




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