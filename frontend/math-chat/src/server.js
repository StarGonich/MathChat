const WebSocket = require('ws');

const wss = new WebSocket.Server({port: 9000});

wss.on('connection', (ws) =>{
    console.log('New client connected');

    ws.on('message', (message) => {
        console.log('Received: ${message}');
        wss.clients.forEach(client => {
            if(client.readyState === WebSocket.OPEN){
                client.send(message);
            }
        });
    });

    ws.on('close', () => {
        console.log('Client disconnected');
    });
});

console.log('WebSocket server is running');