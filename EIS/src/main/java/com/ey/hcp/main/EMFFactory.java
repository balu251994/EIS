package com.ey.hcp.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class EMFFactory extends AbstractBinder implements Factory<EntityManager> {

    private final EntityManagerFactory emf;
    private final String persistenceUnitName;

    public EMFFactory(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;

        emf = createEMF();
    }
	
	@Override
	protected void configure() {
		bindFactory(this).to(EntityManager.class);
	}
	
	public EntityManager provide() {
		return emf.createEntityManager();
	}
	
	public void dispose(EntityManager arg0) {
		// TODO Auto-generated method stub
	}
	
    private EntityManagerFactory createEMF() {
        return Persistence.createEntityManagerFactory(persistenceUnitName);
    }
}