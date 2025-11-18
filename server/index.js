var app = require("express")();
var server = require("http").Server(app);
var io = require("socket.io")(server);
var players = []

server.listen(30003, function () {
    console.log("[WELL] Server is now running.")
})

io.on('connection', function (socket) {
    console.log("Player has connected to the server.")

    socket.emit('socketID', {id: socket.id});
    socket.emit('getPlayers', players)
    socket.broadcast.emit('newPlayer', {id: socket.id});
    socket.on('playerMoved', function (data) {
        data.id = socket.id;
        socket.broadcast.emit('playerMoved', data);

        for (let i = 0; i < players.length; i++) {
            if (players[i].id === data.id) {
                players[i].x = data.x;
                players[i].y = data.y;
            }
        }
    });

    socket.on('disconnect', function () {
        console.log("Player has disconnected.")
        socket.broadcast.emit('playerDisconnected', {id: socket.id})
        for (let i = 0; i < players.length; i++) {
            if (players[i].id === socket.id) {
                players.splice(i, 1);
            }
        }
    });
    players.push(new Player(socket.id, 100, 100))
});

function Player(id, x, y) {
    this.x = x;
    this.y = y;
    this.id = id;
}
