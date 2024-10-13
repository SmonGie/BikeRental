package org.example.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.example.Model.clients.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientRepository implements IClientRepository {
    // Lista przechowująca klientów
    private List<Client> clients = new ArrayList<>();

    // Dodawanie nowego klienta do repozytorium
//    public void addClient(Client client) {
//        clients.add(client);
//    }
    // SAVE SPELNIA TA FUNKCJE!


    // Usuwanie klienta po ID
//    public void removeClientById(Long clientId) {
//        clients.removeIf(client -> client.getId().equals(clientId));
//    }
//MYSLE ZE DELETE TEZ SPELNIA TA FUNKCJE


    // Znajdowanie klienta po ID
//    public Optional<Client> findClientById(Long clientId) {
//        return clients.stream()
//                .filter(client -> client.getId().equals(clientId))
//                .findFirst();
//    }
// PRZEJETE PRZEZ FINDBYID

    // Znajdowanie klienta po numerze telefonu CZY TO SIE GDZIEKOLWIEK PRZYDA?
    public Optional<Client> findClientByPhoneNumber(String phoneNumber) {
        return clients.stream()
                .filter(client -> client.getPhoneNumber().equals(phoneNumber))
                .findFirst();
    }

    // Pobieranie wszystkich klientów
//    public List<Client> getAllClients() {
//        return new ArrayList<>(clients);
//    }
// MYSLE ZE FINDALL ODPOWIADA TEJ FUNKCJI


    // Pobieranie klientów w zależności od wieku (np. dzieci lub dorośli)
    public List<Client> findClientsByAge(int age) {
        return clients.stream()
                .filter(client -> client.getAge() == age)
                .collect(Collectors.toList());
    }// tego nie zrobilem

    // Aktualizacja danych klienta
    // teraz save to robi

    private EntityManager em;


    @Override
    public Client findById(Long id) {
        return em.find(Client.class, id);
    }

    @Override
    public List<Client> findAll() {
        return em.createQuery("SELECT c FROM Client c", Client.class).getResultList();
    }

    @Override
    @Transactional
    public void save(Client client) {
        if (client.getId() == null) {
            em.persist(client);
        } else em.merge(client);

    }

    @Override
    @Transactional
    public void delete(Client client) {

        if (em.contains(client)) {

            em.remove(client);
        } else {
            em.remove((em.merge(client)));
        }
    }


}
