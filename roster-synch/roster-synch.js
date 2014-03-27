var http = require("http");

var sql_roster, mongo_roster, mongo_roster_old = null;


function pollAndSync () {
	var data = '';
	http.get("http://drowsy.badger.encorelab.org/rollcall/groups", function (res) {
		res.on('data', function (chunk) {
			data += chunk;
		}).on('end', function () {
			mongo_roster = JSON.parse(data);
			synchRoster();
			printRosters();
		});
	}).on('error', function (e) {
		console.log("Got error from Mongo: " + e.message);
	});
}

function synchRoster () {
	var diff;
	if (mongo_roster_old == null) {
		// first synch
	} else {
		// Compare old and new
	}
	// write diff
}

// Printing stuff just for fun
function printRosters () {
	sql_roster.forEach(function (user) {
		console.log(user.first_name);
	});
	mongo_roster.forEach(function (group) {
		console.log(group.groupname);
	})
}


///////////////////////////////////////
// Init the SQL DB and start the script
///////////////////////////////////////
var data = '';
http.get("http://trap.euclidsoftware.com/person", function (res) {
	res.on('data', function (chunk) {
		data += chunk;
	}).on('end', function () {
		sql_roster = JSON.parse(data).person;
		//setInterval( pollAndSync, 1000)
		pollAndSync();
	});
}).on('error', function (e) {
	console.log("Got error from SQL: " + e.message);
});
