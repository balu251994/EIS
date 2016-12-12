package com.ey.hcp.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Service {
	
	@GET
	@Path("hello")
	public String getHello() {
		return "Hello World";
	}
	
	@GET
	@Path("date")
	public Map<String, String> getDate() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("date", new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date()));
		
		return map;
	}
}