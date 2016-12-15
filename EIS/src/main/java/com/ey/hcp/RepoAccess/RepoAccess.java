package com.ey.hcp.RepoAccess;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.chemistry.opencmis.client.api.Document;

import com.ey.hcp.dao.DocumentDAO;
import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;

public class RepoAccess {

@Inject
DocumentDAO documentDAO;

	private Session openCMISSession = null;

	private void sessionLogin(){
	
		if(openCMISSession !=null){
			return;
		}
		
		
		String uniqueName = "com.ey.hcp.ABC";
		String secretKey = "!Asdfg123$";
		
		try {
			InitialContext ctx = new InitialContext();
			String lookupName = "java:comp/env/EcmService";
			EcmService ecmSvc = (EcmService) ctx.lookup(lookupName);
			
			try {
				openCMISSession = ecmSvc.connect(uniqueName, secretKey);
				} catch (CmisObjectNotFoundException e) {
				
				RepositoryOptions options = new RepositoryOptions();
				options.setUniqueName(uniqueName);
				options.setRepositoryKey(secretKey);
				options.setVisibility(Visibility.PROTECTED);
				ecmSvc.createRepository(options);
				openCMISSession = ecmSvc.connect(uniqueName, secretKey);
				openCMISSession.getDefaultContext().setCacheEnabled(false);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	

	public com.ey.hcp.jpa.Document repoAcc(HttpServletRequest req, String parentId) {
		
		sessionLogin();
		
		com.ey.hcp.jpa.Document doc=null;
		String docName = null;
		String docMime = null;
		InputStream docIs = null;
		long docSize = 0;
		DiskFileItemFactory dif = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(dif);
		
		try {
			List<FileItem> items = sfu.parseRequest(req);
			Iterator<FileItem> it = items.iterator();
			
			while(it.hasNext())
			{
				FileItem f = it.next();
				if(!f.isFormField())
				{
					docName = f.getName();
					docMime = f.getContentType();
					docSize = f.getSize();
					docIs = f.getInputStream();
				}
				
			}
			
		} catch (FileUploadException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
/*			Folder root = openCMISSession.getRootFolder();	
			//Folder Create
			
			Map<String, String> folderProp = new HashMap<String, String>();
			folderProp.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			folderProp.put(PropertyIds.NAME, "Images");
			
			Folder folder = null;
			
			try {
				folder = root.createFolder(folderProp);
			} catch (CmisNameConstraintViolationException e) {
				
				
				Iterator<CmisObject> itFol = root.getChildren().iterator();
				
				while (itFol.hasNext()) {
					CmisObject obj = (CmisObject) itFol.next();
					if (obj instanceof Folder)
					{
						folder = (Folder) obj;
					}
					
				}
				
			}*/
			
			Folder folder = (Folder) openCMISSession.getObject(parentId);
			
			//Document Create
			
			Map<String, String> properties = new HashMap<String, String>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
			properties.put(PropertyIds.NAME, docName);
			
			Document docIm =null;
			
			ContentStream cstream = openCMISSession.getObjectFactory().createContentStream(docName, docSize, docMime, docIs);
			
			try {
				doc = new com.ey.hcp.jpa.Document();
				
				docIm = folder.createDocument(properties, cstream, VersioningState.NONE);
				System.out.println("Document Id:" + docIm.getId());
				doc.setDocId(docIm.getId());
				doc.setDocName(docIm.getName());
				doc.setDocType("document");
				doc.setParentId(parentId);
				documentDAO.createDocument(doc);
				System.out.println("Document Uploaded Successfully");
				
			} catch (CmisNameConstraintViolationException e) {
				System.out.println("Document alread exists");
			}
		
		return doc;
	}

	public Document download(String id) {
		sessionLogin();
		Document obj = null;
		obj = (Document) openCMISSession.getObject(id);
		
		//System.out.println(obj.toString());
		return obj;
		
	}



	public Map<String, ArrayList> getData() {
		sessionLogin();
		
		Folder root = openCMISSession.getRootFolder();
		
		ItemIterable<CmisObject> folder = root.getChildren();
		
		Iterator<CmisObject> it = folder.iterator();
		
		ArrayList<Map<String, String>> childrenArrayList = new ArrayList<Map<String,String>>();
		
		Map<String, String> object;
		ArrayList<Map<String, String>> objectArray = new ArrayList<Map<String,String>>();
		
		while (it.hasNext()) {
			CmisObject obj = it.next();
			
			object = new HashMap<String, String>();
			
			if(obj instanceof Folder)
			{	
				object.put("type", "folder");
			} else
			{
				object.put("type","document");
				
			}
			
			object.put("id", obj.getId());
			object.put("name", obj.getName());
			
			objectArray.add(object);
			childrenArrayList.add(object);
		}
		
		Map<String, ArrayList> retObj = new HashMap<String, ArrayList>();
		retObj.put("data", childrenArrayList);
		return retObj;
	}



	public Map<String, ArrayList> getData(String id) {
		sessionLogin();
		
		CmisObject objCmis =  openCMISSession.getObject(id);
		
		Iterator<CmisObject> it = ((Folder)objCmis).getChildren().iterator();
	
		ArrayList<Map<String, String>> childrenArrayList = new ArrayList<Map<String,String>>();
		
		Map<String, String> object;
		ArrayList<Map<String, String>> objectArray = new ArrayList<Map<String,String>>();
		
		while (it.hasNext()) {
			CmisObject obj = it.next();
			
			object = new HashMap<String, String>();
			
			if(obj instanceof Folder)
			{	
				object.put("type", "folder");
			} else
			{
				object.put("type","document");
				
			}
			
			object.put("id", obj.getId());
			object.put("name", obj.getName());
			
			objectArray.add(object);
			childrenArrayList.add(object);
		}
		
		Map<String,ArrayList> obj = new HashMap<String, ArrayList>();
		obj.put("data", childrenArrayList);
		return obj;
	}



	public String createFolder(String folName, String folId) {
		sessionLogin();
		
		if(folId.equals("6dda0564d9a887d1aced0585"))
		{
			Folder root = openCMISSession.getRootFolder();
			
			Map<String, String> newfolder = new HashMap<String, String>();
			newfolder.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			newfolder.put(PropertyIds.NAME, folName);
			
			Folder newfol = root.createFolder(newfolder);
			return newfol.getId();
		}
		else {
			
			Folder fol = (Folder) openCMISSession.getObject(folId);
			
			Map<String, String> newfolder = new HashMap<String, String>();
			newfolder.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			newfolder.put(PropertyIds.NAME, folName);
			
			Folder newfol = fol.createFolder(newfolder);
			return newfol.getId();
			
		}
	}
}