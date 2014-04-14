var http = require("http");

var sql_roster, mongo_roster = null;
var X_Trap_Token = "zCMbeLuh8XHpfbvaGfwnZmBKOvgVrTfL";

// Get Mongo roster
function getMongoRoster() {
	var data = '';
	http.get("http://drowsy.badger.encorelab.org/rollcall/groups", function(res) {
		res.on('data', function(chunk) {
			data += chunk;
		}).on('end', function() {
			mongo_roster = JSON.parse(data);
			synchRoster();
		});
	}).on('error', function(e) {
		console.log("Got error from Mongo: " + e.message);
	});
}

// Get SQL roster
function initSQLRosterAndStart(poll, time) {
	var data = '';
	http.get("http://trap.euclidsoftware.com/person", function(res) {
		res.on('data', function(chunk) {
			data += chunk;
		}).on('end', function() {
			console.log("Roster sync bot started...")
			sql_roster = JSON.parse(data).person;
			if (poll)
				setInterval(getMongoRoster, time);
			else
				getMongoRoster();
		});
	}).on('error', function(e) {
		console.log("Got error from SQL: " + e.message);
	});
}

// Get SQL roster
function getSQLRoster() {
	var data = '';
	http.get("http://trap.euclidsoftware.com/person", function(res) {
		res.on('data', function(chunk) {
			data += chunk;
		}).on('end', function() {
			sql_roster = JSON.parse(data).person;
		});
	}).on('error', function(e) {
		console.log("Got error from SQL: " + e.message);
	});
}


// Associates the class name to the right class id
function getClassId(classname) {
	switch (classname) {
		case "ben":
			return 9;
			break;
		case "amanda":
			return 10;
			break;
		case "7MS":
			return 11;
			break;
		case "7BL":
			return 12;
			break;
		case "7DM":
			return 13;
			break;
		case "test":
			return 15;
			break;
	}
}

// POST person to SQL
function addPersonToSQL(name, classroom) {
	var options = {
		hostname: 'trap.euclidsoftware.com',
		path: '/person/',
		method: 'POST',
		headers: {
			'X-Trap-Token': X_Trap_Token
		}
	};
	var data = '';
	var req = http.request(options, function(res) {
		res.on('data', function(chunk) {
			data += chunk;
		});
		res.on('end', function() {
			var id = JSON.parse(data).id;
			putPerson(id, name, classroom);
			console.log("Added user "+name+" to class "+classroom+" with id "+id)
		});
	});
	req.end();
}

// PUT person into SQL
function putPerson(id, name, classroom) {
	var options = {
		hostname: 'trap.euclidsoftware.com',
		path: '/person/' + id,
		method: 'PUT',
		headers: {
			'X-Trap-Token': X_Trap_Token
		}
	};
	var body = '{"email":"' + name + '","password":"' + name + '","is_admin":0,"class_id":' + getClassId(classroom) + ',"first_name": "' + name + '","last_name": "' + name + '"}'
	var req = http.request(options);
	req.write(body);
	req.end();
}


// What wich users are only in mongo and not in SQL? To find out do MONGO - SQL (mongo always has more elements than sql)
function compareDBs() {
	return mongo_roster.filter(function(el) {
		// if the function returns true, the element is added
		// I need to add stuff that is only in mongo and not in sql
		// So if I find it in sql I should NOT add it
		// If I don't find it in SQL I shoud add it
		var verdict = true;
		var i = 0;
		while (i<sql_roster.length) {
			if (sql_roster[i].first_name==el.groupname &&  sql_roster[i].class_id==getClassId(el.runs[0])) {
				verdict = false;
				break;
			}
			i++;
		}
		return verdict;
	});
}

function synchRoster() {
	var diff = compareDBs();
	diff.forEach(function(group) {
		addPersonToSQL(group.groupname, group.runs[0]);
	});
	getSQLRoster();
}


// Prints both rosters, used for debugging
function printRosters() {
	console.log("SQL:")
	sql_roster.forEach(function(user) {
		console.log(user.first_name);
	});
	console.log("MONGO:")
	mongo_roster.forEach(function(group) {
		console.log(group.groupname);
	})
}


///////////
// Start //
///////////
initSQLRosterAndStart(false);
