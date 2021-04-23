
if(typeof(adminLogoutButton)==undefined){
    adminLogoutButton = document.querySelector('#adminLogoutButton');
}
async function what(event) {
    event.preventDefault();
    let params = {code:"logout"};
    login_url.search = new URLSearchParams(params).toString();
    console.log(login_url);
    let resp = await fetch(login_url);
    let msg = await resp.json();
    mainHandler(msg);
}
if(adminLogoutButton != null)adminLogoutButton.addEventListener('click',what);



if(typeof(admin_url)==undefined){
    admin_url = new URL("/admin",base);
}

if(typeof(fileUploadForm)==undefined) {
    let fileUploadForm = document.querySelector('#fileUploadForm')
}
async function handleFileUploadForm(event) {
    event.preventDefault();
    const fetchOptions = {
        method: "POST",
        body: new FormData(fileUploadForm)}
    let params = {code:"upload"};
    
    admin_url = new URL("/admin",base);
    
    admin_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(admin_url, fetchOptions);
    let msg = await resp.json();
    mainHandler(msg);
}



