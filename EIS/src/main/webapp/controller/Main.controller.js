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
			
			var oFileUploader = this.getView().byId("fileUploader");
			if(!oFileUploader.getValue()) {
				MessageToast.show("Choose a file first");
				return;
			}
			oFileUploader.upload();
			
			/*$.ajax({
				url: "ws/service/document/upload/" + docName + "/" + docUploadedBy,
				method: "get",
				success: function(data, status, xhr) {
					console.log("data: " + data + ", status: " + status);
				},
				error: function(xhr, status, error) {
					console.log("Error in XHR: " + status + " | " + error);
				}
			});*/
		},
		
		btnDownloadPressed: function() {
			console.log("btnDownload Pressed");
			var documentName = this.getView().byId("documentId").getValue();
			/*$.ajax({
				url: "ws/service/document/upload/" + docName + "/" + docUploadedBy,
				method: "get",
				success: function(data, status, xhr) {
					console.log("data: " + data + ", status: " + status);
				},
				error: function(xhr, status, error) {
					console.log("Error in XHR: " + status + " | " + error);
				}
			});*/
		},
		
		btnDeletePressed: function() {
			console.log("btnDelete Pressed");
			var documentName = this.getView().byId("documentId").getValue();
			/*$.ajax({
				url: "ws/service/document/upload/" + docName + "/" + docUploadedBy,
				method: "get",
				success: function(data, status, xhr) {
					console.log("data: " + data + ", status: " + status);
				},
				error: function(xhr, status, error) {
					console.log("Error in XHR: " + status + " | " + error);
				}
			});*/
		},
		

	
	});
});