const config = {

    connectorEnabled: false,
    connectorHost: '127.0.0.1',
    connectorPort: 58001,
    connectorCheckInterval: 500,

    dataPacketsSend: 0,
    dataPacketsReceived: 0

};

browser.runtime.onMessage.addListener((request, sender, sendResponse) => {
    if (request.receiver != 'config') return;

    if (request.command == 'receiveAll') {
        sendResponse(config);
    }

    else if (request.command == 'enableConnector') {
        if (config.connectorEnabled) return;
        config.connectorEnabled = true;
        connector.checkForUpdates();
    }
    else if (request.command == 'disableConnector') {
        if (!config.connectorEnabled) return;
        config.connectorEnabled = false;
    }
    else if (request.command == 'changeHost') {
        config.connectorHost = request.host;
    }
    else if (request.command == 'changePort') {
        config.connectorPort = parseInt(request.port);
    }
    else if (request.command == 'changeInterval') {
        config.connectorCheckInterval = parseInt(request.interval);
    }
});