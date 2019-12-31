/*
 * Will this be useful?
 */

$(document).ready(function() {

	getDBInfo();
	
	function infoRefresh(obj) {
		$("#infobox").html("<table>");
		for (let key in obj){
			   if(obj.hasOwnProperty(key)){
				   $("#infobox").append("<tr><td>" + key + "</td><td>" + obj[key] + "</td></tr>");
				   console.log(`${key} : ${obj[key]}`)
			   }
		}
		$("#infobox").append("</table>");
	}

	function getDBInfo() {
		return $.get("/dbservlet", {
			"DatabaseDriver" : "com.lopezgee.drivers.JsonDriver",
			"ParamatersFile" : "JsonDriver.json",
			"type" : "info"}, 
			function(data, status) {
				console.log(data);
				infoRefresh(data);
		});
	}
});