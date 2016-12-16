sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/m/MessageToast"
], function(Controller, MessageToast) {
	"use strict";
	var oModel = new sap.ui.model.json.JSONModel();
	var me;
	
	function getData(){
		$.ajax({
			url: "ws/service/getData",
			method: "get",
			success: function(data, status, xhr) {
				for(var i=0; i<data.data.length;i++){
					if(data.data[i].type === "folder"){
						data.data[i].data = [{"id":""}];
					}
				}
				oModel.setData(data);
				me.getView().setModel(oModel);
			},
			error: function(xhr, status, error) {
				console.log("Error in XHR: " + status + " | " + error);
			}
		});
	}
	
	return Controller.extend("com.ey.hcp.eis.controller.Main", {
		
		onInit: function() {
			console.log("onInit");

			me = this
	        getData();
	
		},
		
		onBeforeRendering: function() {
			
		},
		
		onAfterRendering: function() {
			
		},
		
		
		btnSubmitPressed: function(e) {
			
			var src = e.getSource();
		    
			console.log(src);
            var oContext = src.getBindingContext();
            var sPath = oContext.getPath();
            
            console.log(oContext,sPath);
            var tableModel = this.getView().byId("TreeTableBasic").getModel();
            
            var docId = tableModel.getProperty(sPath + "/id");
			console.log("btnSubmit Pressed");
			
			var oFileUploader = this.getView().byId("fileUploader");
			if(!oFileUploader.getValue()) {
				MessageToast.show("Choose a file first");
				return;
			}
			oFileUploader.setUploadUrl("ws/service/document/upload/" + docId);
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
		    
            var oContext = src.getBindingContext();
            var sPath = oContext.getPath();
            
            var tableModel = this.getView().byId("TreeTableBasic").getModel();
            
            var docId = tableModel.getProperty(sPath + "/id");
            console.log(docId);
			window.open("ws/service/document/download/" + docId, "_parent");
		},
		
		btnGetPressed : function(e) {
			var src = e.getParameters();
		    
			console.log(src.expanded,src.rowContext.sPath);
            if(src.expanded){
				var sPath = src.rowContext.sPath;
	            var tableModel = this.getView().byId("TreeTableBasic").getModel();
	            
	            var docId = tableModel.getProperty(sPath + "/id");
	         
	            $.ajax({
					url: "ws/service/getData/" + docId,
					method: "get",
					success: function(data, status, xhr) {
						for(var i=0; i<data.data.length;i++){
							if(data.data[i].type === "folder"){
								data.data[i].data = [{"id":""}];
							}
						}
						tableModel.setProperty(sPath+"/data", data.data);
					},
					error: function(xhr, status, error) {
						console.log("Error in XHR: " + status + " | " + error);
					}
				});
			}
		},
		
		addFolderRoot: function(){
			var dialog = new sap.m.Dialog({
				title: 'Create Folder',
				type: 'Message',
				content: new sap.m.Input("rootFolderInput",{ 
					placeholder: 'Enter Folder Name' 
				}),
				beginButton: new sap.m.Button({
					text: 'Submit',
					press: function () {
						var folName = sap.ui.getCore().byId("rootFolderInput").getValue();
						$.ajax({
							url: "ws/service/folderAtRoot/" + folName,
							method: "get",
							success: function(data, status, xhr) {
								console.log(data);
								getData();
							},
							error: function(xhr, status, error) {
								console.log("Error in XHR: " + status + " | " + error);
							}
						});
						dialog.close();
					}
				}),
				endButton: new sap.m.Button({
					text: 'Cancel',
					press: function () {
						dialog.close();
					}
				})
			});
			dialog.open();
		},
		
		btnAddFolderPressed: function(e){
			
			var src = e.getSource();
		    
            var oContext = src.getBindingContext();
            var sPath = oContext.getPath();
            
            var tableModel = this.getView().byId("TreeTableBasic").getModel();
            
            var docId = tableModel.getProperty(sPath + "/id");
			
            var dialog = new sap.m.Dialog({
				title: 'Create Folder',
				type: 'Message',
				content: new sap.m.Input("folderInput",{ 
					placeholder: 'Enter Folder Name' 
				}),
				beginButton: new sap.m.Button({
					text: 'Submit',
					press: function () {
						var folName = sap.ui.getCore().byId("folderInput").getValue();
						$.ajax({
							url: "ws/service/folderCreate/"+folName+"/"+docId,
							method: "get",
							success: function(data, status, xhr) {
								console.log(data);
								getData();
							},
							error: function(xhr, status, error) {
								console.log("Error in XHR: " + status + " | " + error);
							}
						});
						dialog.close();
					}
				}),
				endButton: new sap.m.Button({
					text: 'Cancel',
					press: function () {
						dialog.close();
					}
				})
			});
			dialog.open();
		},
		
		
		btnDeletePressed: function(e) {
			
			var src = e.getSource();
		    
            var oContext = src.getBindingContext();
            var sPath = oContext.getPath();
            
            var tableModel = this.getView().byId("TreeTableBasic").getModel();
            
            var docId = tableModel.getProperty(sPath + "/id");	
			
            $.ajax({
				url: "ws/service/delete/" + docId ,
				method: "get",
				success: function(data, status, xhr) {
					console.log("data: " + data + ", status: " + status);
					//location.reload();
				},
				error: function(xhr, status, error) {
					console.log("Error in XHR: " + status + " | " + error);
				}
			});
		},
		
		/*formatterT: function(value){
			//debugger;
			for(var i=0;i<value.length;i++){
				if(value[i].type === "folder"){
					this.getView().byId("buttonGet").getVisible(true);
					return value[i].name ;
				}else{
					this.getView().byId("buttonGet").getVisible(false);
					return value[i].name ;
				}
				
			}
			
		}*/
		
		/*btnMovePressed : function(e) {
			var src = e.getSource();
            
			var oContext = src.getBindingContext();
            var sPath = oContext.getPath();
            
            var tableModel = this.getView().byId("TreeTableBasic").getModel();
            
            var docId = tableModel.getProperty(sPath + "/id");
            console.log();
            
		},*/
	
	});
});