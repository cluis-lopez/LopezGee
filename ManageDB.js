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
					window.alert("1-"+data);
				}
		});
	}
	
	$("#newuser").click(function (){
		newUser();
		$("#id").val("");
		$("#name").val("");
		$("#password").val("");
	});
	
	$("#removeuser").click(function() {
		removeUser();
		$("#removeid").val("");
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
					infoRefresh(data);
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
					window.alert("3-"+data);
		});
	}
	
	function removeUser(){
		temp = $("#removeid").val();
		$.get("/dbservlet", {
			"adminuser" : adminuser,
			"adminpassword" : adminpassword,
			"DatabaseDriver" : "com.lopezgee.drivers.JsonDriver",
			"ParamatersFile" : "JsonDriver.json",
			"removeid" : $("#removeid").val(),
			"type" : "removeUser"}, 
			function(data, status) {
				console.log(data);
				if (data == "OK"){
					window.alert("Removed user with id: "+temp)
					getDBInfo();
				}
				else
					window.alert("4-"+data);
		});
	}
});