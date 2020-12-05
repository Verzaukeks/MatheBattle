const connector = {

    checkForUpdates: () => {
        fetch('http://' + config.connectorHost + ':' + config.connectorPort)
            .then(response => response.json())
            .then(json => {
                if (json.queue) json.queue.forEach(connector.onUpdate);
                setTimeout(connector.checkForUpdates, config.connectorCheckInterval);
            })
            .catch(error => {
                setTimeout(connector.checkForUpdates, config.connectorCheckInterval);
            });
    },

    onUpdate: (packet) => {
        if (!packet.type) return
        switch (packet.type) {
        case "newTab":
            handler.newTab(packet);
            break;
        case "getCurrentTab":
            handler.getCurrentTab(packet);
            break;
        case "getTabs":
            handler.getTabs(packet);
            break;

        case "getTabFromId":
            handler.getTabFromId(packet);
            break;
        case "reloadTab":
            handler.reloadTab(packet);
            break;
        case "removeTab":
            handler.removeTab(packet);
            break;
        case "executeScript":
            handler.executeScript(packet);
            break;
        case "insertCSS":
            handler.insertCSS(packet);
            break;
        default:
        }
    },

    sendUpdate: (packet, onResponse) => {
        fetch('http://' + config.connectorHost + ':' + config.connectorPort, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(packet)
        })
            .then(response => response.json())
            .then(json => {
                if (onResponse) onResponse(json);
            })
    },

    sendAnswer: (packet, response) => {
        if (!packet.expectAnswer) return;
        connector.sendUpdate({
            id: packet.id,
            type: packet.type,
            response: response
        });
    }

};

connector.checkForUpdates();