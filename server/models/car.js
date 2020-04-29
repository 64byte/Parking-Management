var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var carSchema = new Schema({
	carnum: String,
	entrancetime: Date
});

module.exports = mongoose.model('car', carSchema)