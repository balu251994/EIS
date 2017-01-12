package com.ey.hcp.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.ey.hcp.jpa.Document2;
import com.ey.hcp.jpa.Folders;

public class DatabaseFetch {
	@Inject
	EntityManager em;
	
	@SuppressWarnings("deprecation")
	public Map<String, ArrayList> dbFetch(){
		TypedQuery<Folders> tq = em.createNamedQuery("getData",Folders.class);
		tq.setParameter("id", "Root");
		//System.out.println(tq.getResultList());
		ArrayList<Map<String, String>> items = new ArrayList<Map<String,String>>();
		
		for(Folders f : tq.getResultList()) {
			Map<String, String> object = new HashMap<String, String>();
			object.put("SNo",f.getSNo());
			object.put("folId", f.getFolId());
			object.put("name", f.getFolName());
			object.put("parentId",f.getParentId());
			object.put("createDate",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(f.getCreateDate()));
			object.put("type", "folder");
			items.add(object);	
					
		}
	
		Map<String, ArrayList> retObj = new HashMap<String, ArrayList>();
		retObj.put("data", items);
		
		return retObj;
	}

	public Map<String, ArrayList> getData(String id) {
	
		TypedQuery<Folders> tq = em.createNamedQuery("getData", Folders.class);
		tq.setParameter("id", id);
		ArrayList<Map<String, String>> items = new ArrayList<Map<String,String>>();
		for(Folders f : tq.getResultList()){
			Map<String, String> object = new HashMap<String, String>();
			object.put("SNo",f.getSNo());
			object.put("folId", f.getFolId());
			object.put("name", f.getFolName());
			object.put("parentId",f.getParentId());
			object.put("createDate",new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(f.getCreateDate()));
			object.put("type", "folder");
			items.add(object);	
		}
		
	//	ArrayList<Map<String, String>> docItems = new ArrayList<Map<String,String>>();
		TypedQuery<Document2> tqD = em.createNamedQuery("getDoc", Document2.class);
		tqD.setParameter("id", id);
		for(Document2 doc : tqD.getResultList()){
			Map<String, String> object = new HashMap<String, String>();
			object.put("SNo", doc.getSNo());
			object.put("name", doc.getDisplayName());
			object.put("docId", doc.getDocId());
			object.put("parentId", doc.getParentId());
			object.put("mimeType", doc.getMimeType());
			object.put("uploadDate", new SimpleDateFormat("dd-MM-yyy hh:mm:ss").format(doc.getUploadDate()));
			object.put("docName",doc.getDocName());
			object.put("type", "document");
			items.add(object);
		}
		
		Map<String, ArrayList> retObj = new HashMap<String, ArrayList>();
		retObj.put("data", items);
		return retObj;
		
	}
	
	

}
