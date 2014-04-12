var http = require("http");

var sql_roster, mongo_roster = null;
var X_Trap_Token = "zCMbeLuh8XHpfbvaGfwnZmBKOvgVrTfL";

// Get Mongo roster
function getMongoRoster(callback) {
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
function initSQLRoster(callback) {
	var data = '';
	http.get("http://trap.euclidsoftware.com/person", function(res) {
		res.on('data', function(chunk) {
			data += chunk;
		}).on('end', function() {
			console.log("SQL roster initialized")
			sql_roster = JSON.parse(data).person;
			//setInterval(getMongoRoster, 5000);
			getMongoRoster();
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




// What is only in mongo and not in SQL? To find out do MONGO - SQL
function compareDBs() {

}

function synchRoster() {
	var diff = compareDBs();
	// write diff
}


// Printing stuff just for fun
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
initSQLRoster();
