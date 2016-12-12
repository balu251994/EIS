sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/m/MessageToast"
], function(Controller, MessageToast) {
	"use strict";
	
	return Controller.extend("com.ey.hcp.eis.controller.Main", {
		
		onInit: function() {
			console.log("onInit");
		},
		
		onBeforeRendering: function() {
			
		},
		
		onAfterRendering: function() {
			
		},
		
		btnSubmitPressed: function() {
			console.log("btnSubmit Pressed");
			
			$.ajax({
				url: "ws/service/date",
				method: "get",
				success: function(data, status, xhr) {
					console.log("data: " + data.date);
				},
				error: function(xhr, status, error) {
					console.log("Error in XHR: " + status + " | " + error);
				}
			});
		}
	
	});
});