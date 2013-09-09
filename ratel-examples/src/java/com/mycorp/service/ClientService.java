package com.mycorp.service;

import com.google.ratel.Context;
import com.google.ratel.core.RatelService;
import com.google.ratel.core.Param;
import com.mycorp.dao.ClientDao;
import com.mycorp.domain.Client;
import com.mycorp.util.EMF;
import com.mycorp.util.EMUtil;
import java.util.List;
import javax.persistence.*;

@RatelService
public class ClientService {

    private int sleepTime = 0;
    
    public List<Client> getClients() {
        if (true) {
            throw new RuntimeException("Mooo");
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ignore) {
        }

        ClientDao dao = new ClientDao();
        return dao.getClients();
    }

    public Client getClient(@Param(name = "id", required = true) Long id) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ignore) {
        }
        Context context = Context.getContext();
        //String idStr = context.getRequest().getParameter("id");
        //Long id = Long.parseLong(idStr);

        ClientDao dao = new ClientDao();
        return dao.getClient(id);
    }

    public Client saveClient(Client client) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignore) {
        }

        EntityManager em = EMF.getEM();
        try {
            em = EMUtil.beginTransaction(em);

            ClientDao dao = new ClientDao();
            client = dao.merge(client);

            EMUtil.commitTransaction(em);

            return client;

        } catch (RuntimeException e) {
            EMUtil.rollbackTransaction(em, e);
            throw e;
        } finally {
            EMUtil.cleanupTransaction(em);
        }
    }
}
