package com.mycorp.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

public class EMUtil {

	private final static Logger LOGGER = Logger.getLogger(EMUtil.class.getName());

	 public static EntityManager getEntityManager() {
		 EntityManager em = EMF.getEM();
		   return em;
		 }

		 public static EntityManager beginTransaction(EntityManager em) {
		   em.getTransaction().begin();
		   return em;
		 }

		 public static void commitTransaction(EntityManager em) {
		   em.getTransaction().commit();
		 }

		 public static void rollbackTransaction(EntityManager em) {
			rollbackTransaction(em, null);
		 }

		 public static void rollbackTransaction(EntityManager em, Throwable t) {
			 if (em == null) {
				 return;
			 }

			 if (em.isOpen()) {
				 if (em.getTransaction().isActive()) {
					 if (t != null) {
						 LOGGER.log(Level.SEVERE, "Rolling back the transaction", t);
					 } else {
						 LOGGER.log(Level.SEVERE, "Rolling back the transaction");
					 }
					 em.getTransaction().rollback();
		         }
			 }
		 }

		 public static void cleanupTransaction(EntityManager em) {
			 try {
			        if (em != null) {
			          if (em.isOpen()) {
			            if (em.getTransaction().isActive()) {
			              LOGGER.severe("Transaction is still active. Rolling transaction back in order to cleanup");
			              rollbackTransaction(em);
			            }
			            em.close();
			          } else {
			            LOGGER.warning("EntityManager is already closed");
			          }
			        }
			      } catch (Throwable t) {
			        LOGGER.log(Level.SEVERE, "Error closing EntityManager", t);
			        throw new RuntimeException(t);
			      } finally {
			        EMF.setEM(null);
			      }
			 }
}
