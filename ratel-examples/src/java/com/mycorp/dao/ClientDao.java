package com.mycorp.dao;

import com.mycorp.domain.Client;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 */
public class ClientDao extends BaseDao {
    
    public Client getClient(Long id) {
        EntityManager em = getEntityManager();
        Client client = em.find(Client.class, id);
        return client;
    }

    public List<Client> getClients() {
        EntityManager em = getEntityManager();
        TypedQuery<Client> query = em.createQuery("Select c from Client c", Client.class);
        List<Client> results = query.getResultList();
        return results;
    }
    
    public Client merge(Client client) {
        return (Client) super.merge(client);
    }
}
