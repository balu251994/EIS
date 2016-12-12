sap.ui.define([
    "sap/ui/core/UIComponent"               
], function(UIComponent) {
	"use strict";
	
	return UIComponent.extend("com.ey.hcp.eis.Component", {
		metadata: {
			manifest: "json"
		},
		
		init: function() {
			UIComponent.prototype.init.apply(this, arguments);
		}
	});
});