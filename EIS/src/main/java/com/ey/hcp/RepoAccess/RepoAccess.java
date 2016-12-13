package com.ey.hcp.RepoAccess;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
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
		
		if(openCMISSession != null) {
			return;
		}
		
		String uniqueName = "com.ey.hcp.Repo1";
		String secretKey = "!Asdfg123$";
		
		try {
			InitialContext ctx = new InitialContext();
			String lookupName = "java:comp/env/EcmService";
			EcmService ecmSvc = (EcmService) ctx.lookup(lookupName);
			try {
				openCMISSession = ecmSvc.connect(uniqueName, lookupName);				
			} catch (CmisObjectNotFoundException e) {
				
				RepositoryOptions options = new RepositoryOptions();
				options.setUniqueName(uniqueName);
				options.setRepositoryKey(secretKey);
				options.setVisibility(Visibility.PROTECTED);
				ecmSvc.createRepository(options);
				openCMISSession = ecmSvc.connect(uniqueName, secretKey);				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public com.ey.hcp.jpa.Document repoAcc(HttpServletRequest req) {
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
		
		
			
			Folder root = openCMISSession.getRootFolder();
			
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
				
			}
			
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
}