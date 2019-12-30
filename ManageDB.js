/**
 * 
 */

$(document).ready(function() {

	infoRefresh();

	function infoRefresh() {
		$("#infobox").html("Text");
	}

	function getDBInfo() {
		var ret;
		$.get("dbservlet", {
			"ParamatersFile" : "JsonDriver.json",
			"type" : "login"}, 
			function(data, status) {
			alert("Data: " + data + "\nStatus: " + status);
		});
	}
});