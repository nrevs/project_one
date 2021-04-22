if(typeof(user_url)==undefined){
    let user_url = new URL("/user", base);
}

class SeshComponent extends Component {
    update() {
        let t = document.querySelector(this.id);
        if(t != null) {
            while(t.childNodes.length>1) {
                t.removeChild(t.lastChild);
            }
            jsonObj = JSON.parse(this.data);
            for (var s in jsonObj.sessions) {
                let r = document.createElement('tr');
                let d1 = document.createElement('td');
                d1.innerText = s.sessionid;
                let d2 = document.createElement('td');
                d2.innerText = s.expir;
                let d3 = document.createElement('td');
                d3.innerText = s.reqcount;
                r.appendChild(d1);
                r.appendChild(d2);
                r.appendChild(d3);
                t.appendChild(r);
            }
        }
    }
}


if(!('seshComponent' in document.componentRegistry)) {
    document.componentRegistry.seshComponent = new SeshComponent('seshComponent');
}




alert("user.js loaded");








async function getNewSession(event) {
    event.preventDefault();
    let params = {code:"newSession"};
    user_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(user_url);
    let msg = await resp.json();
    mainHandler(msg);
    return false;
}


async function logut(event) {
    event.preventDefault();
    let params = {code:"logout"};
    user_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(base_url);
    let msg = await resp.json();
    mainHandler(msg);
    return false;
}