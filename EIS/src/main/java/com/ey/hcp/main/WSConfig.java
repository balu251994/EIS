package com.ey.hcp.main;

import org.glassfish.jersey.server.ResourceConfig;

public class WSConfig extends ResourceConfig {
	
	public WSConfig() {
		register(Service.class);
	}	
}