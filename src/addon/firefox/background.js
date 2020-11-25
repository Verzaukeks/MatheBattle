async function checkForUpdates() {
    fetch('http://127.0.0.1:58001')
        .then(response => response.json())
        .then(json => {
            if (json.queue) json.queue.forEach(onUpdate);
            setTimeout(checkForUpdates, 500);
        })
        .catch(error => {
            setTimeout(checkForUpdates, 500);
        });
}
checkForUpdates();

function onUpdate(json) {
    if (json.type)
        switch (json.type) {
            case "getTab":
                getTab(json);
                break;
            case "reloadTab":
                reloadTab(json);
                break;
            case "removeTab":
                removeTab(json);
                break;
            case "executeScript":
                executeScript(json);
                break;
            case "insertCSS":
                insertCSS(json);
                break;
            case "newTab":
                newTab(json);
                break;
            case "getCurrentTab":
                getCurrentTab(json);
                break;
            case "getTabs":
                getTabs(json);
                break;
            default:
        }
}

function sendUpdate(json, onResponse) {
    fetch('http://127.0.0.1:58001', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(json)
    })
        .then(response => response.json())
        .then(json => { if (onResponse) onResponse(json) });
}

function getTab(json) {
    browser.tabs.get(json.id)
        .then(tab => {

            json.tab = {};
            json.tab.url = tab.url;
            json.tab.title = tab.title;
            json.tab.status = tab.status;

            sendUpdate(json);
        });
}

function reloadTab(json) {
    browser.tabs.reload(json.id, { bypassCache: json.bypassCache })
        .then(() => sendUpdate(json));
}

function removeTab(json) {
    browser.tabs.remove(json.id)
        .then(() => sendUpdate(json));
}

function executeScript(json) {
    browser.tabs.executeScript(json.id, { code: json.script })
        .then(results => {
            json.result = results[0];
            sendUpdate(json);
        });
}

function insertCSS(json) {
    browser.tabs.insertCSS(json.id, { code: json.css })
        .then(() => sendUpdate(json));
}

function newTab(json) {
    browser.tabs.create({
        active: json.active,
        url: json.url
    })
        .then(tab => {

            json.tab = {};
            json.tab.id = tab.id;
            json.tab.url = tab.url;
            json.tab.title = tab.title;
            json.tab.status = tab.status;

            sendUpdate(json);
        });
}

function getCurrentTab(json) {
    browser.tabs.query({ currentWindow: true, active: true })
        .then(tabs => {
            let tab = tabs[tabs.length-1];

            json.tab = {};
            json.tab.id = tab.id;
            json.tab.url = tab.url;
            json.tab.title = tab.title;
            json.tab.status = tab.status;

            sendUpdate(json);
        });
}

function getTabs(json) {
    browser.tabs.query({})
            .then(tabs => {

                json.tabs = [];
                for (let tab of tabs) {

                    let t = {};
                    t.id = tab.id;
                    t.url = tab.url;
                    t.title = tab.title;
                    t.status = tab.status;
                    json.tabs.push(t);
                }
                sendUpdate(json);
            });
}

browser.tabs.onCreated.addListener(tab => {
    let json = {};
    json.type = "onTabCreated";

    json.tab = {};
    json.tab.id = tab.id;
    json.tab.url = tab.url;
    json.tab.title = tab.title;
    json.tab.status = tab.status;

    sendUpdate(json);
});

browser.tabs.onUpdated.addListener((_1, _2, tab) => {
    let json = {};
    json.type = "onTabUpdated";

    json.tab = {};
    json.tab.id = tab.id;
    json.tab.url = tab.url;
    json.tab.title = tab.title;
    json.tab.status = tab.status;

    sendUpdate(json);
});