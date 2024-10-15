package org.example.Repositories;

import jakarta.persistence.*;
import org.example.Model.clients.Client;

import java.util.List;


public class ClientRepository implements IClientRepository {

    private EntityManagerFactory emf;

    public ClientRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }


    // Znajdowanie klienta po numerze telefonu CZY TO SIE GDZIEKOLWIEK PRZYDA?


    // Pobieranie klientów w zależności od wieku (np. dzieci lub dorośli)
//    public List<Client> findClientsByAge(int age) {
//        return clients.stream()
//                .filter(client -> client.getAge() == age)
//                .collect(Collectors.toList());
//    }// tego nie zrobilem

    @Override
    public Client findById(Long id) {

        EntityManager em = emf.createEntityManager();
        Client client = null;

        try {
            client = em.find(Client.class, id);
        } finally {
            em.close();
        }

        return client;
    }

    @Override
    public List<Client> findAll() {
        EntityManager em = emf.createEntityManager();

        List<Client> clients = null;

        try {
            clients = em.createQuery("SELECT c FROM Client c", Client.class).getResultList();

        } finally {
            em.close();
        }

        return clients;
    }

    @Override

    public void save(Client client) {
        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (client.getId() == null) {
                em.persist(client);
            } else em.merge(client);

            transaction.commit();
        } catch (OptimisticLockException e) {
            transaction.rollback();
            System.out.println("Inny użytkownik zmodyfikował ten obiekt. Spróbuj ponownie.");
        }finally {
            em.close();
        }

    }

    @Override

    public void delete(Client client) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (em.contains(client)) {
                em.remove(client);
            } else {
                em.remove((em.merge(client)));
            }
            transaction.commit();
        }catch (OptimisticLockException e) {
            transaction.rollback();
            System.out.println("Inny użytkownik zmodyfikował ten obiekt. Spróbuj ponownie.");
        } finally {
            em.close();
        }


    }


}
