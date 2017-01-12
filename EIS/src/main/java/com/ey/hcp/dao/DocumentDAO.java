package com.ey.hcp.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.chemistry.opencmis.client.api.Folder;

import com.ey.hcp.jpa.Document;
import com.ey.hcp.jpa.Document2;
import com.ey.hcp.jpa.Folders;

public class DocumentDAO {

	@Inject
	EntityManager em;
	
/*	public void createDocument(Document document) {
		em.getTransaction().begin();
		em.persist(document);
		em.getTransaction().commit();
		
		System.out.println("docId: " + document.getId());
	}*/
	
	public void createDocument2 (Document2 document)
	{
		em.getTransaction().begin();
		em.persist(document);
		em.getTransaction().commit();
	}
	
	public void createFolder (Folders folder){
		em.getTransaction().begin();
		em.persist(folder);
		em.getTransaction().commit();
	}
	
}