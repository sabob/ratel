package com.mycorp.dao;

import com.mycorp.util.*;
import javax.persistence.EntityManager;

public class BaseDao {
	
	protected static final int MAX_NUMBER_OF_RECORDS = 1000;

	protected EntityManager getEntityManager() {
		EntityManager em = EMF.getEM();
		return em;
	}
  
  public void save(Object entity) {
      EntityManager em = getEntityManager();
      em.persist(entity);
  }
  
  public Object merge(Object entity) {
      EntityManager em = getEntityManager();
      return em.merge(entity);
  }
}
