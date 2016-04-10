// Let's create a DNS-proxy that proxies IPv4 udp-requests to googles IPv6 DNS-server
var proxy = require('./proxy');

var RELIABLE = 0.8;
var OUT_OF_ORDER = 0.8;

var options = {
        address: '127.0.0.1',
        port: 3000,
        ipv6: false,
        localaddress: '0.0.0.0',
        localport: 3001,
        localipv6: false,
        proxyaddress: '0.0.0.0',
        timeOutTime: 60 * 60 * 1000 , // 60 min
        reliable: RELIABLE,
        outOfOrder: OUT_OF_ORDER
    };

// This is the function that creates the server, each connection is handled internally
var server = proxy.createServer(options);

// this should be obvious
server.on('listening', function (details) {
    console.log('udp-proxy-server ready on ' + details.server.family + '  ' + details.server.address + ':' + details.server.port);
    console.log('traffic is forwarded to ' + details.target.family + '  ' + details.target.address + ':' + details.target.port);
    console.log('Reliability is ' + RELIABLE);
    console.log('Out of Order possibility is ' + OUT_OF_ORDER);
});

// 'bound' means the connection to server has been made and the proxying is in action
server.on('bound', function (details) {
    console.log('proxy is bound to ' + details.route.address + ':' + details.route.port);
    console.log('peer is bound to ' + details.peer.address + ':' + details.peer.port);
});

// 'message' is emitted when the server gets a message
server.on('message', function (message, sender) {
    console.log('message from ' + sender.address + ':' + sender.port);
});

server.on('messageDrop', function (message, sender) {
    console.log('drop message from ' + sender.address + ':' + sender.port);
});

// 'proxyMsg' is emitted when the bound socket gets a message and it's send back to the peer the socket was bound to
server.on('proxyMsg', function (message, sender) {
    console.log('answer from ' + sender.address + ':' + sender.port);
});

server.on('proxyMsgDrop', function (message, sender) {
    console.log('drop answer from ' + sender.address + ':' + sender.port);
});

// 'proxyClose' is emitted when the socket closes (from a timeout) without new messages
server.on('proxyClose', function (peer) {
    console.log('disconnecting socket from ' + peer.address);
});

server.on('proxyError', function (err) {
    console.log('ProxyError! ' + err);
});

server.on('error', function (err) {
    console.log('Error! ' + err);
});