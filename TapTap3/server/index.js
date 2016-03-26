var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];

//server listen on port 8080
server.listen(8080, function() {
	console.log("Server is now running...");
});

//socket.io do this when connection happens
io.on('connection', function(socket){
	console.log("Player Connected!");
	//server emits a 'socketID' event to the client with the data socket.id
	socket.emit('socketID', { id: socket.id});
	//socket sends the players array to the current client 
	socket.emit('getPlayers', players)
	//server sends to everyone else except this socket
	socket.broadcast.emit('newPlayer', { id: socket.id});
	socket.on('playerMoved', function(data){
		data.id=socket.id;
		socket.broadcast.emit('playerMoved', data);

		console.log("playerMoved: " +
					"ID: " + data.id + "\n" +
					"X: " + data.x + "\n" +
					"Y: " + data.y);

		for (var i=0;i<players.length;i++){
			if(players[i].id == data.id){
					players[i].x = data.x;
					players[i].y = data.y;
			}
		}
	})
	//disconnection?
	socket.on('disconnect', function(){
		console.log("Player Disconnected");
		//server sends the disconnected player's id to all players 
		socket.broadcast.emit('playerDisconnected', {id: socket.id});
		//server deletes the disconnected player from the player array 
		for (var i = 0;i < players.length;i ++) {
			if (players[i].id = socket.id){
				players.splice(i, 1);
			}
		}

	});
	//server includes the connected player to the player array 
	players.push(new player(socket.id, 0, 0));
});

function player(id, x, y) {
	this.id = id;
	this.x = x;
	this.y = y;
}