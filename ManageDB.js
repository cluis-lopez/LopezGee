/*
 * Will this be useful?
 */

$(document).ready(function() {
	var adminuser = "";
	var adminpassword = "";
	
	$("#modal").css("display", "block");
	
	$("#login").click(function (){
		loginuser($("#adminuser").val(), $("#adminpassword").val());
	});
	
	getDBInfo();
	
	function loginuser(user, password){
		$.get("/dbservlet", {
			"adminuser" : user,
			"adminpassword" : password,
			"DatabaseDriver" : "com.lopezgee.drivers.JsonDriver",
			"ParamatersFile" : "JsonDriver.json",
			"type" : "login"}, 
			function(data, status) {
				console.log(data);
				if (data == "OK"){
					adminuser = $("#adminuser").val();
					adminpassword = $("#adminpassword").val();
					$("#modal").css("display", "none");
					getDBInfo();
				} else {
					window.alert(data);
				}
		});
	}
	
	$("#newuser").click(function (){
		newUser();
		$("#id").val("");
		$("#name").val("");
		$("#password").val("");
	});
	
	function infoRefresh(obj) {
		$("#infobox").html("<table>");
		for (let key in obj){
			   if(obj.hasOwnProperty(key)){
				   $("#infobox").append("<tr><td class='firstcol'>" + key + "</td><td>" + obj[key] + "</td></tr>");
				   console.log(`${key} : ${obj[key]}`)
			   }
		}
		$("#infobox").append("</table>");
	}

	function getDBInfo() {
		$.get("/dbservlet", {
			"adminuser" : adminuser,
			"adminpassword" : adminpassword,
			"DatabaseDriver" : "com.lopezgee.drivers.JsonDriver",
			"ParamatersFile" : "JsonDriver.json",
			"type" : "info"}, 
			function(data, status) {
				console.log(data);
				if (data == "OK")
					infoRefresh(data);
				else
					window.alert(data);
		});
	};
	
	function newUser(){
		$.get("/dbservlet", {
			"adminuser" : adminuser,
			"adminpassword" : adminpassword,
			"DatabaseDriver" : "com.lopezgee.drivers.JsonDriver",
			"ParamatersFile" : "JsonDriver.json",
			"id" : $("#id").val(),
			"name" : $("#name").val(),
			"password" : $("#password").val(),
			"type" : "newUser"}, 
			function(data, status) {
				console.log(data);
				if (data == "OK")
					getDBInfo();
				else
					window.alert(data);
		});
	}
});