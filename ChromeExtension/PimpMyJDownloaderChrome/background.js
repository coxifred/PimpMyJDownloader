

let server;
let downloaded = new Map();


chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.create({
    id: "pimp-this",
    title: "Pimp this !",
    contexts: ["page", "selection", "link"]
  });
});

chrome.contextMenus.onClicked.addListener((info, tab) => {
  if (info.menuItemId === "pimp-this") {
    console.log("Lien cliqué :", info.linkUrl);
    graburl(info); 
  }
});


function graburl(info) {
    let grabbed = info.selectionText;
    let url = info.linkUrl;
    let selected = url;
    if (typeof grabbed !== "undefined" && grabbed && grabbed.startsWith("http")) {
        selected = grabbed;
    }

    if (selected && selected.startsWith("http")) {
        notification("Grabbing url by a monkey", selected);
        getValueFromUrl("http://" + server + "/admin?link=" + selected, true);
    } else {
        notification("Error", "[" + selected + "] not starting with http, bypass");
    }
}

// Poll server every 5 seconds
setInterval(reload, 5000);
reload();

async function reload() {
    server = await getLocalStorage('pimpMyJDownloaderServer');
    log(server);
    if (server !== undefined && server !== null && server !== "null" ) {
        log("server is " + server);
        getValueFromUrl("http://" + server + "/admin?state=yes", true, parseDownload);
    } else {
        log("server is undefined");
    }
}

function parseDownload(jsonMessage) {
    if (jsonMessage === undefined) {
        // No DOM, so use notification or log
        notification("Error in query server", server);
    } else {
        let jsonMonkeys = JSON.parse(jsonMessage);
        let globalPercent = 0;
        let count = 0;
        for (let i = 0; i < jsonMonkeys.length; i++) {
            if (jsonMonkeys[i].progression === 100 && downloaded.get(jsonMonkeys[i].url) !== "100") {
                notification("Finished", "[" + jsonMonkeys[i].fileName + "] is downloaded");
                downloaded.set(jsonMonkeys[i].url, "100");
            }
            if (jsonMonkeys[i].progression > 0) {
                globalPercent += jsonMonkeys[i].progression;
                count++;
            }
        }
        // Compute %
        globalPercent = count > 0 ? parseInt(Math.floor((globalPercent / count % 10)) * 10) : 0;

        log("Global percent: " + globalPercent); 
        //notification("Info",  + globalPercent + "% of downloads are in progress");
        
        // In MV3, use chrome.action instead of chrome.browserAction
        chrome.action.setIcon({ path: "/img/" + globalPercent + ".png" });
    }
}

function log(message) {
    console.log(message);
}

async function getLocalStorage(key) {
    return new Promise((resolve) => {
        chrome.storage.local.get([key], (result) => {
            return resolve(result[key]);
        });
    });
}

// Replace getValueFromUrl and notification with fetch and chrome.notifications
function getValueFromUrl(url, async, callback) {
    log("Fetching URL: " + url);
    fetch(url)
        .then(response => response.text())
        .then(data => {
            if (callback) callback(data);
        })
        .catch(error => {
            log("Fetch error: " + error);
            if (callback) callback(undefined);
        });
}

function notification(title, message) {
   
   chrome.notifications.create({
        type: "basic",
        iconUrl: chrome.runtime.getURL("/img/monkey.png"),
        title: title,
        message: message
    });
}

    