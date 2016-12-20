sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/m/MessageToast",
    "sap/ui/unified/FileUploader"
], function(Controller, MessageToast, FileUploader) {
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
		    
			var oContext = src.getBindingContext();
            var sPath = oContext.getPath();
            var tableModel = this.getView().byId("TreeTableBasic").getModel();
            
            var docId = tableModel.getProperty(sPath + "/id");
			console.log(docId);
			
			var dialog = new sap.m.Dialog({
				title: 'Upload',
				content: new sap.ui.unified.FileUploader({ 
					id:"fileUploader",
					name:"myFileUpload",
					style:"Emphasized",
					fileType:"jpg",
					uploadUrl:"ws/service/document/upload/" + docId,
					placeholder:"Choose a file for Upload..."
				}),
				beginButton: new sap.m.Button({
					text: 'Upload',
					press: function () {
						var oFileUploader = sap.ui.getCore().byId("fileUploader");
						if(!oFileUploader.getValue()) {
							MessageToast.show("Choose a file first");
							return;
						}
						oFileUploader.setSendXHR(true);
						oFileUploader.upload();
						
						getData(); 
						me.getView().byId("TreeTableBasic").collapseAll();
						
						sap.ui.getCore().byId("fileUploader").destroy();
						dialog.close();
					}
				}),
				endButton: new sap.m.Button({
					text: 'Close',
					press: function () {
						sap.ui.getCore().byId("fileUploader").destroy();
						dialog.close();
					}
				})
			});
			dialog.open();
			
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
		
		rowSelect : function(e) {
			var src = e.getParameters();
			var sPath = src.rowContext.sPath;
            var tableModel = this.getView().byId("TreeTableBasic").getModel();
            
            var docId = tableModel.getProperty(sPath + "/id");
            var url = "ws/service/document/download/" + docId;
            var img = new Image();
            
            var height,width;
            img.onload = function(){
               height = img.height+"px";
               width = img.width+"px";
               console.log(height,width);
              // code here to use the dimensions
            }
            img.src = url;
            
            var dialog = new sap.m.Dialog({
				title: 'Preview',
				contentWidth:width,
				contentHeight:height,
				draggable:true,
				resizable: true,
				content: new sap.m.Image({ 
					src: url
				}),
				endButton: new sap.m.Button({
					text: 'Close',
					press: function () {
						dialog.close();
					}
				})
			});
			dialog.open();
            
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
							method: "post",
							success: function(data, status, xhr) {
								console.log(data);
								getData();
								//me.getView().byId("TreeTableBasic").collapseAll;
								sap.ui.getCore().byId("rootFolderInput").destroy();
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
						sap.ui.getCore().byId("rootFolderInput").destroy();
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
							method: "post",
							success: function(data, status, xhr) {
								console.log(data);
								getData();
								me.getView().byId("TreeTableBasic").collapseAll();
								sap.ui.getCore().byId("folderInput").destroy();
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
						sap.ui.getCore().byId("folderInput").destroy();
						dialog.close();
					}
				})
			});
			dialog.open();
		},
		
		
		btnDeleteFolderPressed: function(e) {
			
			var src = e.getSource();
		    
            var oContext = src.getBindingContext();
            var sPath = oContext.getPath();
            
            var tableModel = this.getView().byId("TreeTableBasic").getModel();
            
            var docId = tableModel.getProperty(sPath + "/id");	
			
            $.ajax({
				url: "ws/service/deleteFol/" + docId ,
				method: "get",
				success: function(data, status, xhr) {
					console.log("data: " + data + ", status: " + status);
					var dialog = new sap.m.Dialog({
						title: 'Do you want to Delete this!',
						type: 'Message',
						content: new sap.m.Text({ 
							text:"This folder has " + data["FolderCount: "] + " folders and " + data["DocCount: "] + " docs"
						}),
						beginButton: new sap.m.Button({
							text: 'Ok',
							press: function () {
								//call delete 
								$.ajax({
									url: "ws/service/delete/" + docId ,
									method: "delete",
									success: function(data, status, xhr) {
										console.log("data: " + data + ", status: " + status);
										getData();
										me.getView().byId("TreeTableBasic").collapseAll();
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
				error: function(xhr, status, error) {
					console.log("Error in XHR: " + status + " | " + error);
				}
			});
		},
		
		btnDeletePressed: function(e) {
			
			var src = e.getSource();
		    
            var oContext = src.getBindingContext();
            var sPath = oContext.getPath();
            
            var tableModel = this.getView().byId("TreeTableBasic").getModel();
            
            var docId = tableModel.getProperty(sPath + "/id");	
			
            $.ajax({
				url: "ws/service/delete/" + docId ,
				method: "delete",
				success: function(data, status, xhr) {
					console.log("data: " + data + ", status: " + status);
					getData();
					me.getView().byId("TreeTableBasic").collapseAll();
				},
				error: function(xhr, status, error) {
					console.log("Error in XHR: " + status + " | " + error);
				}
			});
		},
		
		formatterbuttonDown: function(value){
			return value == "document";	
		},
		
		formatterbuttonZipDown: function(value){
			return value == "folder";	
		},
		
		formatterbuttonUp: function(value){
			return value == "folder";	
		},
		
		formatterbuttonAdd: function(value){
			return value == "folder";	
		},
		
		formatterbuttonDeleteFolder: function(value){
			return value == "folder";	
		},
		formatterbuttonDelete: function(value){
			return value == "document";	
		},
		
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