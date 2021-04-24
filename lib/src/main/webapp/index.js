'use strict';

let base = 'https://localhost:8443';
let login_url = new URL("/login",base);

class Component {
    html = '';
    src = '';
    componentId = '';
    id = '';
    data = '';

    constructor(componentId) {
        console.log('component constructor');
        this.componentId = componentId;
        this._id = ++document.nextId;
        document.componentRegistry[this._id] = this;
    }

    handlePayload(payload, msgR) {
        console.log(this.componentId+' -> handlePayload');
        if ('id' in payload){ this.setId(payload.id); }
        if ('html' in payload){ this.setHtml(payload.html); }
        if ('src' in payload){ this.setSrc(payload.src); }
        if ('data' in payload){ this.setData(payload.data); }
        this.update(msgR);
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

    setData(data) {
        console.log('setData');
        this.data = data;
    }

    update(msgR) {
        let t = document.querySelector(this.id);
        
        if(t != null) {
            t.innerHTML = this.html;
        }
        let scrpt = document.querySelector("#dynamicscript");
        scrpt.remove();
        scrpt = document.createElement("script");
        scrpt.id="dynamicscript";
        scrpt.src=this.src;
        if(msgR.components.length > 0) {
            scrpt.onload = () => {
                mainHandler(msgR);
            }
        }
        document.head.appendChild(scrpt);
    }
    
}


document.componentRegistry = {};
document.nextId = 0;

document.componentRegistry.mainComponent = new Component('mainComponent');


let mainHandler = function(msg) {
    console.log("mainHandler called");
    console.log(msg);

    let handle = function(cmp, msgR) {
        console.log('handle:');
        let componentId = 'id' in cmp && cmp.id;
        console.log('handling '+componentId);
        let payload = 'payload' in cmp && cmp.payload;

        if (componentId && payload) {
            if (componentId in document.componentRegistry) {
                console.log(componentId + ' found in componentRegistry');
                let lclCmp = document.componentRegistry[componentId];
                lclCmp.handlePayload(payload, msgR);
            } else {
                console.log(componentId + ' NOT found in componentRegistry');
            }
        }
    }

    if (!('components' in msg)) {
        throw 'msg should contain array of component objs'
    }
    let cmp0 = msg.components.shift();
    handle(cmp0, msg);
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
