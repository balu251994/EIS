package com.ey.hcp.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.chemistry.opencmis.client.api.Folder;

import com.ey.hcp.jpa.Document;

public class DocumentDAO {

	@Inject
	EntityManager em;
	
	public void createDocument(Document document) {
		em.getTransaction().begin();
		em.persist(document);
		em.getTransaction().commit();
		
		System.out.println("docId: " + document.getId());
	}

	public void updateDocument(String docId, String dest) {
		
		em.getTransaction().begin();
		Document doc = em.find(Document.class, dest);
		System.out.println("After Query"+ dest);
		doc.setParentId(dest);
		em.getTransaction().commit();
		System.out.println("Parent Id" + doc.getParentId());
	}

}