var exec = null;
if(Meteor.isCordova) {
	exec = require("cordova/exec");
	NativeUtils.getGPSState = function(success, failure, config) {

		var dialog = (config && config.dialog) || false;

		console.log("CONFIG:" + JSON.stringify(config));

		exec(
			success || function() {},
			failure || function() {},
			'NativeUtils',
			'getGPSState',
			[dialog]
			);
	}
}
var NativeUtils = {
}

module.exports = NativeUtils;