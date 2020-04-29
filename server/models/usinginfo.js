var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var usinginfoSchema = new Schema({
	entrancetime: Date,
	exittime: Date,
	totaltime: Date,
	usingtime: Date,
	totalamount: Number,
	userlevel: Number
});

module.exports = mongoose.model('usinginfo', usinginfoSchema)