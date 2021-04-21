'use strict';

let base = 'https://localhost:8443';
let login_url = new URL("/login",base);

class Component {
    html = '';
    src = '';
    componentId = '';
    id = '';

    constructor(componentId) {
        console.log('component constructor');
        this.componentId = componentId;
        this._id = ++document.nextId;
        document.componentRegistry[this._id] = this;
    }

    handlePayload(payload) {
        console.log('handlePayload');
        if ('id' in payload){ this.setId(payload.id); }
        if ('html' in payload){ this.setHtml(payload.html); }
        if ('src' in payload){ this.setSrc(payload.src); }
        this.update();
    }

    setId(id) {
        console.log('setId');
        this.id = id;
    }

    setHtml(html) {
        console.log('setHtml');
        this.html = html;
    }

    setSrc(src) {
        console.log('setSrc');
        this.src = src;
    }

    update() {
        let t = document.querySelector(this.id);
        let g = document.getElementById("maincontent");
        
        if(t != null) {
            t.innerHTML = this.html;
        }
        let scrpt = document.querySelector("#dynamicscript");
        scrpt.remove();
        scrpt = document.createElement("script");
        scrpt.id="dynamicscript";
        scrpt.src=this.src;
        document.head.appendChild(scrpt);
    }
    
}


document.componentRegistry = {};
document.nextId = 0;

document.componentRegistry.mainComponent = new Component('mainComponent');


let mainHandler = function(msg) {
    console.log("mainHandler called");
    console.log(msg);

    let handle = function(cmp, idx) {
        console.log('handle');
        let componentId = 'id' in cmp && cmp.id;
        let payload = 'payload' in cmp && cmp.payload;
        if (componentId && payload) {
            let lclCmp = componentId in document.componentRegistry && document.componentRegistry[componentId];
            lclCmp.handlePayload(payload);
        }
    }

    if (!('components' in msg)) {
        throw 'msg should contain array of component objs'
    }
    msg.components.forEach(handle);
}


let msgHint = {
    components: [
        {
            id:"mainComponent",
            payload:{
                id: '#maincontent',
                html:'<span>example content</span>',
                src: 'login.js'
            }
        }
    ]
}


async function startLogin(event) {
    if (event!=null) {
        event.preventDefault();
    }
    console.log('startLogin called');
    console.log(document.readyState);
    let params = {code:"login"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url)
    let msg = await resp.json();
    mainHandler(msg);
};


let sf = function() {
    startLogin();
};


document.addEventListener('DOMContentLoaded', sf());
