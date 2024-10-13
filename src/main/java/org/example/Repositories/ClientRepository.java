package org.example.Repositories;

import org.example.Model.clients.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientRepository {
    // Lista przechowująca klientów
    private List<Client> clients = new ArrayList<>();

    // Dodawanie nowego klienta do repozytorium
    public void addClient(Client client) {
        clients.add(client);
    }

    // Usuwanie klienta po ID
    public void removeClientById(Long clientId) {
        clients.removeIf(client -> client.getId().equals(clientId));
    }

    // Znajdowanie klienta po ID
    public Optional<Client> findClientById(Long clientId) {
        return clients.stream()
                .filter(client -> client.getId().equals(clientId))
                .findFirst();
    }

    // Znajdowanie klienta po numerze telefonu
    public Optional<Client> findClientByPhoneNumber(String phoneNumber) {
        return clients.stream()
                .filter(client -> client.getPhoneNumber().equals(phoneNumber))
                .findFirst();
    }

    // Pobieranie wszystkich klientów
    public List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }

    // Pobieranie klientów w zależności od wieku (np. dzieci lub dorośli)
    public List<Client> findClientsByAge(int age) {
        return clients.stream()
                .filter(client -> client.getAge() == age)
                .collect(Collectors.toList());
    }

    // Aktualizacja danych klienta
    public void updateClient(Client updatedClient) {
        findClientById(updatedClient.getId()).ifPresent(client -> {
            client.setFirstName(updatedClient.getFirstName());
            client.setLastName(updatedClient.getLastName());
            client.setPhoneNumber(updatedClient.getPhoneNumber());
            client.setAge(updatedClient.getAge());
            client.setId(updatedClient.getId());
        });
    }
}
