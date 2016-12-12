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
			
			var docName = this.getView().byId("inpDocName").getValue();
			var docUploadedBy = this.getView().byId("inpDocUploadedBy").getValue();
			
			console.log("docName: " + docName + " | docUploadedBy: " + docUploadedBy);
			
			$.ajax({
				url: "ws/service/document/upload/" + docName + "/" + docUploadedBy,
				method: "get",
				success: function(data, status, xhr) {
					console.log("data: " + data + ", status: " + status);
				},
				error: function(xhr, status, error) {
					console.log("Error in XHR: " + status + " | " + error);
				}
			});
		}
	
	});
});