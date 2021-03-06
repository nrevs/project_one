console.log('user.js start read');

let user_url;
if(typeof(user_url)==undefined) {
    user_url = new URL("/user", base);
}
let api_url;
if(typeof(api_url)==undefined) {
    api_url = new URL("/api", base);
}

class SeshComponent extends Component {
    update(msgR) {
        console.log('updating component: '+this.componentId);
        console.log('updating tag: '+this.id);
        let t = document.querySelector(this.id);
        if(t != null) {
            while(t.childNodes.length>0) {
                t.removeChild(t.lastChild);
            }

            console.log('sessions data:'+this.data);

            let jsonObj = JSON.parse(this.data);
            let sessions = jsonObj['sessions'];
            console.log(sessions);

            sessions.forEach(addRow);
            function addRow(item, idx) {
                let r = document.createElement('tr');
                let d1 = document.createElement('td');
                d1.innerText = item['sessionId'];
                let d2 = document.createElement('td');
                d2.innerText = new Date(item['expiration']);
                let d3 = document.createElement('td');
                d3.innerText = item['requestCount'];
                let d4 = document.createElement('td');
                let rad = document.createElement('input');
                rad.setAttribute('type','radio');
                rad.setAttribute('name','sessionid');
                rad.setAttribute('id',item['sessionId']);
                rad.setAttribute('value',item['sessionId']);
                d4.appendChild(rad);

                r.appendChild(d1);
                r.appendChild(d2);
                r.appendChild(d3);
                r.appendChild(d4);
                t.appendChild(r);
            }
        }
        mainHandler(msgR);
    }
}


if(!('seshComponent' in document.componentRegistry)) {
    document.componentRegistry.seshComponent = new SeshComponent('seshComponent');
}


async function getNewSession(event) {
    event.preventDefault();
    let params = {code:"newSession"};
    let user_url = new URL("/user", base);
    user_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(user_url);
    let msg = await resp.json();
    mainHandler(msg);
    return false;
}

if(typeof(userLogoutButton)==undefined){
    let userLogoutButton = document.querySelector('#userLogoutButton');
}
async function logoutUser(event) {
    event.preventDefault();
    let params = {code:"logout"};
    login_url.search = new URLSearchParams(params).toString();
    let resp = await fetch(login_url);
    let msg = await resp.json();
    mainHandler(msg);
    return false;
}
if(userLogoutButton != null)userLogoutButton.addEventListener('click', logoutUser);


class TickersComponent extends Component {
    update(msgR) {
        console.log('updating component: '+this.componentId);
        console.log('updating tag: '+this.id);
        let t = document.querySelector(this.id);
        if(t != null) {
            while(t.childNodes.length>0) {
                t.removeChild(t.lastChild);
            }
            console.log('tickers data:'+this.data);
            
            let jsonObj = JSON.parse(this.data);
            let tickers = jsonObj['tickers'];
            console.log(tickers);

            tickers.forEach(addRow);
            function addRow(item, idx) {
                let o = document.createElement('option');
                o.setAttribute('value',item.ticker);
                o.innerText = item.ticker;

                t.appendChild(o);
            }
        }
        mainHandler(msgR);
    }
}


if(!('tickersComponent' in document.componentRegistry)) {
    document.componentRegistry.tickersComponent = new TickersComponent('tickersComponent');
}



if(typeof(getApiDataButton)==undefined){
    let getApiDataButton = document.querySelector('#getApiDataButton');
}
if(typeof(sessionsForm)==undefined){
    let sessionsForm = document.querySelector('#sessionsForm');
}
async function getApiData(event) {
    event.preventDefault();
    let fd = new FormData(sessionsForm);
    if (fd.get('sessionid')!=null){
        console.log(fd);
        let params = {
            method:"GET",
            code:"data",
            sessionid:fd.get('sessionid'),
            username:fd.get('username'),
            ticker:fd.get('ticker')
        };
        let api_url = new URL("/api", base);
        api_url.search = new URLSearchParams(params).toString();
        let resp = await fetch(api_url);
        let msg = await resp.json();
        mainHandler(msg);
    }
    return false;
}
if(getApiDataButton != null)getApiDataButton.addEventListener('click', getApiData);

class HistDataComponent extends Component {
    update(msgR) {
        console.log('updating component: '+this.componentId);
        console.log('updating tag: '+this.id);
        //let t = document.querySelector(this.id);
        //console.log(this.html);
        //t.innerHtml = this.html;
        //console.log(t);
        let ttitle = document.querySelector('#tickertitle');
        let hdata = document.querySelector(this.id);
        if(hdata != null) {
            while(hdata.childNodes.length>0) {
                hdata.removeChild(t.lastChild);
            }
            console.log('hist data:'+this.data);
            
            let jsonObj = JSON.parse(this.data);
            let tSymbol = jsonObj['tickersymbol']
            let eoddata = jsonObj['eoddata'];
            console.log(tSymbol);
            console.log(eoddata);

            ttitle.innerText = "TICKER: "+tSymbol.toUpperCase();

            eoddata.forEach(addListItem);
            function addListItem(item, idx) {
                let li = document.createElement('li');
                li.setAttribute('value',JSON.stringify(item));
                li.innerText = JSON.stringify(item);

                hdata.appendChild(li);
            }
        }
        mainHandler(msgR);
    }
}


if(!('histDataComponent' in document.componentRegistry)) {
    document.componentRegistry.histDataComponent = new HistDataComponent('histDataComponent');
}


console.log('user.js end read');