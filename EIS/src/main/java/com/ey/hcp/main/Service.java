package com.ey.hcp.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ey.hcp.RepoAccess.RepoAccess;
import com.ey.hcp.dao.DocumentDAO;
import com.ey.hcp.dto.Test;
import com.ey.hcp.jpa.Document;

@Path("service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Service {
	
	@Inject
	DocumentDAO documentDAO;
	
	@Inject
	Test test;
	
	@GET
	@Path("hello")
	public String getHello(Test test) {
		System.out.println("test a: " + test.getA());
		System.out.println("test b: " + test.isB());
		
		return "Hello World";
	}
	
	@GET
	@Path("date")
	public Map<String, String> getDate() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("date", new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date()));
		
		return map;
	}
	
	@GET
	@Path("document/upload/{name}/{uploadedBy}")
	public Response uploadDocument(@PathParam("name") String name, @PathParam("uploadedBy") String uploadedBy, @Context HttpServletRequest req) {
		
		RepoAccess repo = new RepoAccess(req);
		/*Document document = new Document();
		document.setDocName(name);
		document.setDocUploadedBy(uploadedBy);
		document.setDocUploadedDate(new Date());
		
		documentDAO.createDocument(document);*/
		
		//return Response.ok().entity(document).build();
		
		return null;
	}
}