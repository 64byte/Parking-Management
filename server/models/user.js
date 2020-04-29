var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var userSchema = new Schema({
	email: String,
	password: String,
	name: String,
	phonenum: String,
	carnum: String,
	usinginfo: {
		entrancetime: Number,
		exittime: Number,
		totaltime: Number,
		usingtime: Number,
		totalamount: Number,
		userlevel: Number
	}
});

module.exports = mongoose.model('user', userSchema);