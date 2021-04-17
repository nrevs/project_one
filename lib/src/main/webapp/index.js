'use strict';

let base = 'https://localhost:8443';
let api_url = new URL("/api",base);
let params = {code:"login"};
api_url.search = new URLSearchParams(params).toString();

(async() => {
    let resp = await(fetch(api_url))
    let api = await resp.json();
    console.log(api)
    let mainCmpnt = new Component();
    mainCmpnt.setName('#maincontent');
    mainCmpnt.setRenderStr(api.maincontent);
    mainCmpnt.setScriptStr(api.script);
    mainCmpnt.updateComponent();
})()


document.componentRegistry = {};
document.nextId = 0;


class Component {
    renderStr = '';
    scriptStr = '';
    name = '';

    constructor() {
        this._id = ++document.nextId;
        document.componentRegistry[this._id] = this;
    }

    setName(nm) {
        this.name = nm;
    }

    setRenderStr(rdrStr) {
        this.renderStr = rdrStr;
    }

    setScriptStr(scrStr) {
        this.scriptStr = scrStr;
    }

    updateComponent() {
        console.log(document);
        console.log(this.name);
        document.querySelector(this.name).innerHTML = this.renderStr;
        document.querySelector("#dynamicscript").setAttribute('src',this.scriptStr);
    }
    
}


