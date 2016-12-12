package com.ey.hcp.main;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.ey.hcp.dao.DocumentDAO;

public class WSConfig extends ResourceConfig {
	
	public WSConfig() {
		register(Service.class);
		
		EMFFactory emff = new EMFFactory("EIS");
		register(emff);
		
		register(new DAOBinder());
	}	
	
    static class DAOBinder extends AbstractBinder {
        @Override
        protected void configure() {
        	bind(DocumentDAO.class).to(DocumentDAO.class);
        }
    }
}