'use strict';

let base = 'https://localhost:8443';
let api_url = new URL("/api",base);
let params = {code:"login"};
api_url.search = new URLSearchParams(params).toString();


class Component {
    html = '';
    src = '';
    componentId = '';
    id = '';

    constructor(componentId) {
        this.componentId = componentId;
        this._id = ++document.nextId;
        document.componentRegistry[this._id] = this;
    }

    handlePayload(payload) {
        if ('id' in payload){ this.setId(payload.id); }
        if ('html' in payload){ this.setHtml(payload.html); }
        if ('src' in payload){ this.setSrc(payload.src); }
        this.update();
    }

    setId(id) {
        this.id = id;
    }

    setHtml(html) {
        this.html = html;
    }

    setSrc(src) {
        this.src = src;
    }

    update() {
        document.querySelector(this.id).innerHTML = this.html;
        document.querySelector("#dynamicscript").setAttribute('src',this.src);
    }
    
}



document.componentRegistry = {};
document.nextId = 0;

document.componentRegistry.mainComponent = new Component('mainComponent');


let mainHandler = function(msg) {

    console.log(msg);
    let handle = function(cmp, idx) {
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


async function startLogin() {
    let resp = await(fetch(api_url));
    let msg = await resp.json();
    mainHandler(msg);
};
startLogin();




