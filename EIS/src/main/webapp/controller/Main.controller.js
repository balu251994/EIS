sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/m/MessageToast"
], function(Controller, MessageToast) {
	"use strict";
	
	return Controller.extend("com.ey.hcp.eis.controller.Main", {
		
		onInit: function() {
			console.log("onInit");
			var oModel = new sap.ui.model.json.JSONModel();
			var me = this;
			$.ajax({
				url: "ws/service/getData",
				method: "get",
				success: function(data, status, xhr) {
					oModel.setData(data);
					console.log("data: " + data + ", status: " + status);
					me.getView().setModel(oModel);
				},
				error: function(xhr, status, error) {
					console.log("Error in XHR: " + status + " | " + error);
				}
			});
	        
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
		
		btnDownloadPressed: function(e) {
			var src = e.getSource();
		    
			console.log(src);
            var oContext = src.getBindingContext();
            var sPath = oContext.getPath();
            
            console.log(oContext,sPath);
            var tableModel = this.getView().byId("TreeTableBasic").getModel();
            
            var docId = tableModel.getProperty(sPath + "/id");
            console.log(docId);
			window.open("ws/service/document/download/" + docId, "_parent");
		},
		
		btnDeletePressed: function() {
			console.log("btnDelete Pressed");
			var documentName = this.getView().byId("documentId").getValue();
			$.ajax({
				url: "ws/service/document/delete/" + documentName ,
				method: "delete",
				success: function(data, status, xhr) {
					console.log("data: " + data + ", status: " + status);
				},
				error: function(xhr, status, error) {
					console.log("Error in XHR: " + status + " | " + error);
				}
			});
		},
		

	
	});
});