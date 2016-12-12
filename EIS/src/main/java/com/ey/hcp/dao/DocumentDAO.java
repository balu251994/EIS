package com.ey.hcp.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

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
	
}