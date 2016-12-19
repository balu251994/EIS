package com.ey.hcp.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;

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
	RepoAccess repo;
	
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
	
	@POST
	@Path("document/upload/{parentId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadDocument(@PathParam("parentId") String parentId,@Context HttpServletRequest req) {
		
		Document doc = repo.repoAcc(req, parentId);
		if(doc==null)
		{
			return Response.status(Status.CONFLICT).build();
		}
		return Response.ok().entity(doc).build();
		
		
	}
	
	@GET
	@Path("document/download/{id}")
	 public Response downloadDocument (@PathParam("id") String id)
	 {
        String docName = "";
        StreamingOutput so = null;
        
        try {
              org.apache.chemistry.opencmis.client.api.Document doc = repo.download(id);
               
               docName = doc.getName();
               final InputStream is = doc.getContentStream().getStream();
               
               so = new StreamingOutput() {
				
				public void write(OutputStream os) throws IOException, WebApplicationException {
					int num;
                    byte b[] = new byte[8 * 1024];
                    
                    while((num = is.read(b)) != -1) {
                    	os.write(b, 0, num);
                    }
                    
                    is.close();
                    os.close();
				}
			};
        } catch(Exception e) {
               e.printStackTrace();
        }
        
        return Response.ok(so, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename=" + docName).build();

	 }
	
	@GET
	@Path("getData")
	public Map<String, ArrayList> getData(){
	return repo.getData();
		
		
	}
	
	@GET
	@Path("getData/{id}")
	public Map<String, ArrayList> getData(@PathParam("id") String id){	
	return repo.getData(id);
			
	}
	
	//CREATE FOLDER SERVICE
	
	@POST
	@Path("folderAtRoot/{folName}")
	public Response createAtRoot(@PathParam("folName") String folName){
		
		String folId = repo.createAtRoot(folName);
		Document doc = new Document();
		doc.setDocId(folId);
		doc.setDocType("folder");
		documentDAO.createDocument(doc);
		return Response.ok().entity(doc).build();
		
	}
	
	@POST
	@Path("folderCreate/{folName}/{parentId}")
	public Response createFolder(@PathParam("folName") String folName, @PathParam("parentId") String parentId){
		
		String folId =repo.createFolder(folName, parentId);
		Document doc = new Document();
		doc.setDocId(folId);
		doc.setDocName(folName);
		doc.setDocType("folder");
		doc.setParentId(parentId);
		documentDAO.createDocument(doc);
		return Response.ok().entity(doc).build();
		
	}
	
	@GET
	@Path("downZip/{id}")
	public StreamingOutput getZip(@PathParam("id") String id){
		
		StreamingOutput so =null;
		Folder fol = repo.getZipper(id);
		
		Iterator<CmisObject> it = fol.getChildren().iterator();
		ArrayList<CmisObject> listArray = new ArrayList<CmisObject>();
		
		while (it.hasNext()) {
			CmisObject obj =it.next();	
		}
		
		
		return null;
		
	}
	
	@POST
	@Path("move/{docId}/{sId}/{dId}")
	public Response move(@PathParam("docId") String docId,@PathParam("sId") String source, @PathParam("dId") String dest){
		repo.move(docId,source,dest);
		documentDAO.updateDocument(docId,dest);
		
		return Response.ok().build();
			
	}
	
	//Delete Document
	
	@DELETE
	@Path("delete/{docId}")
	public Response delete(@PathParam("docId") String docId){
		repo.delete(docId);
		//Database Update 
		return Response.ok().build();
		
	}
	
	//Delete Folder
	
	@GET
	@Path("deleteFol/{folId}")
	public Map<String, Integer> deleteFol(@PathParam("folId") String folId){
		
		Map<String, Integer> count =	repo.deleteFol(folId);
		return count;	
	}
	
}