/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycorp.util;

import com.mycorp.dao.ClientDao;
import com.mycorp.domain.Client;
import javax.persistence.EntityManager;
import javax.servlet.*;

/**
 *
 */
public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        ClientDao dao = new ClientDao();

        EntityManager em = EMF.getEM();
		try {
			EMUtil.beginTransaction(em);
        Client person = new Client();
        person.setFirstname("John");
        person.setLastname("Smit");
        dao.save(person);
        
        person = new Client();
        person.setFirstname("Steve");
        person.setLastname("Anderson");
        dao.save(person);
        
        person = new Client();
        person.setFirstname("Alice");
        person.setLastname("Wonder");
        dao.save(person);

        EMUtil.commitTransaction(em);

		} catch (RuntimeException e) {
			EMUtil.rollbackTransaction(em, e);
			throw e;
		} finally {
			EMUtil.cleanupTransaction(em);
		}
   }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
