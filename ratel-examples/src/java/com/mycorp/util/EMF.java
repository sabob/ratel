package com.mycorp.util;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
  
  private final static Logger LOGGER = Logger.getLogger(EMF.class.getName());

  private static final ThreadLocal<EntityManager> THREAD_LOCAL = new ThreadLocal<EntityManager>();

  private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("manager1");

  private EMF() {
  }

  public static EntityManagerFactory getEMF() {
    return entityManagerFactory;
  }

  public static EntityManager getEM() {
    EntityManager em = THREAD_LOCAL.get();
    if (em == null || !em.isOpen()) {
      // If EM isn't open, create a new one
        long start = System.currentTimeMillis();
      em = getEMF().createEntityManager();
      LOGGER.info("createEntityManager time: " + (System.currentTimeMillis() - start));

      setEM(em);
      return em;
    } else {
      return em;
    }
  }

  public static void setEM(EntityManager em) {
    THREAD_LOCAL.set(em);
  }

  public static boolean hasEM() {
    EntityManager em = THREAD_LOCAL.get();
    if (em == null) {
      return false;
    } else {
      return true;
    }
  }
}
