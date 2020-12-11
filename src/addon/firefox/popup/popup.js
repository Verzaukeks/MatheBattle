const binding = {
    status: document.getElementById('status'),
    host: document.getElementById('host'),
    port: document.getElementById('port'),
    interval: document.getElementById('interval'),
    stop: document.getElementById('stop'),
    start: document.getElementById('start'),
    send: document.getElementById('send'),
    received: document.getElementById('received')
};

function updateInformation() {
    browser.runtime.sendMessage({
        'receiver': 'config',
        'command': 'receiveAll'
    }).then(config => {
        binding.status.textContent = (config.connectorEnabled ? 'Running' : 'Halted');
        binding.host.value = config.connectorHost;
        binding.port.value = config.connectorPort;
        binding.interval.value = config.connectorCheckInterval;
        binding.send.textContent = config.dataPacketsSend;
        binding.received.textContent = config.dataPacketsReceived;
    });
}

binding.host.oninput = () => {
    browser.runtime.sendMessage({
        'receiver': 'config',
        'command': 'changeHost',
        'host': binding.host.value
    });
};

binding.port.oninput = () => {
    browser.runtime.sendMessage({
        'receiver': 'config',
        'command': 'changePort',
        'port': binding.port.value
    });
};

binding.interval.oninput = () => {
    browser.runtime.sendMessage({
        'receiver': 'config',
        'command': 'changeInterval',
        'interval': binding.interval.value
    });
};

binding.stop.onclick = () => {
    browser.runtime.sendMessage({
        'receiver': 'config',
        'command': 'disableConnector'
    }).then(response => {
        updateInformation();
    });
};

binding.start.onclick = () => {
    browser.runtime.sendMessage({
        'receiver': 'config',
        'command': 'enableConnector'
    }).then(response => {
        updateInformation();
    });
};

binding.host.placeholder = '127.0.0.1';
binding.port.placeholder = '58001';
binding.interval.placeholder = '500';

updateInformation();