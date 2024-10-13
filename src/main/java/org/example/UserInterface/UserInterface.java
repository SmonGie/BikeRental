package org.example.UserInterface;

import org.example.Model.Rental;
import org.example.Model.clients.Client;
import org.example.Model.clients.Address;
import org.example.Model.Bike;
import org.example.Repositories.BikeRepository;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.RentalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final ClientRepository clientRepository;
    private final BikeRepository bikeRepository;
    private final RentalRepository rentalRepository;
    private final Scanner scanner;

    public UserInterface(ClientRepository clientRepository, BikeRepository bikeRepository, RentalRepository rentalRepository) {
        this.clientRepository = clientRepository;
        this.bikeRepository = bikeRepository;
        this.rentalRepository = rentalRepository;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("Wybierz opcję:");
            System.out.println("1. Zarządzanie klientami");
            System.out.println("2. Zarządzanie rowerami");
            System.out.println("3. Wypożyczenie roweru TYMCZASOWO NIE DZIALA");
            System.out.println("0. Wyjdź");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manageClients();
                    break;
                case 2:
                    manageBikes();
                    break;
//                case 3:
//                   // rentBike();
//                    break;
                case 4:
                    endRental();
                    break;
                case 0:
                    System.out.println("Do widzenia!");
                    return;
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    private void manageClients() {
        while (true) {
            System.out.println("\nZarządzanie klientami:");
            System.out.println("1. Dodaj klienta");
            System.out.println("2. Usuń klienta");
            System.out.println("3. Przeglądaj klientów");
            System.out.println("0. Powrót do menu głównego");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addClient();
                    break;
                case 2:
                    removeClient();
                    break;
                case 3:
                    listClients();
                    break;
                case 0:
                    return; // Powrót do menu głównego
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    private void manageBikes() {
        while (true) {
            System.out.println("\nZarządzanie rowerami:");
            System.out.println("1. Dodaj rower");
            System.out.println("2. Usuń rower");
            System.out.println("3. Przeglądaj rowery - NIECZYNNE");
            System.out.println("0. Powrót do menu głównego");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Odczytanie nowej linii

            switch (choice) {
                case 1:
                    addBike();
                    break;
                case 2:
                    removeBike();
                    break;
//                case 3:
//                    listBikes();
//                    break;
                case 0:
                    return; // Powrót do menu głównego
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    private void addClient() {
        System.out.print("Imię: ");
        String firstName = scanner.nextLine();
        System.out.print("Nazwisko: ");
        String lastName = scanner.nextLine();
        System.out.print("Numer telefonu: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Wiek: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Odczytanie nowej linii
        System.out.print("Miasto: ");
        String city = scanner.nextLine();
        System.out.print("Ulica: ");
        String street = scanner.nextLine();
        System.out.print("Numer: ");
        String number = scanner.nextLine();
        long id = 1; // Możesz wprowadzić logikę do generowania unikalnego ID

        Address address = new Address(city, street, number);
        Client client = new Client(firstName, lastName, phoneNumber, age, address);
      //  client.setId(id);
       // clientRepository.addClient(client);
        System.out.println("Klient został dodany.");
    }

    private void removeClient() {
        System.out.print("Podaj ID klienta do usunięcia: ");
        Long clientId = scanner.nextLong();
       // clientRepository.removeClientById(clientId);
        System.out.println("Klient został usunięty.");
    }

    private void listClients() {
        //List<Client> clients = clientRepository.getAllClients();
        System.out.println("Lista klientów:");
//        for (Client client : clients) {
//            System.out.println(client.getInfo());
//        }
    }

    private void addBike() {
        System.out.print("Model roweru: ");
        String model = scanner.nextLine();
        System.out.print("ID roweru: ");
        Long id = scanner.nextLong();
        boolean available = true; // Domyślnie, rower jest dostępny

        Bike bike = new Bike(id, model, available);
       // bikeRepository.addBike(bike);
        System.out.println("Rower został dodany.");
    }

//    private void rentBike() {
//        System.out.print("Podaj ID klienta: ");
//        Long clientId = scanner.nextLong();
//        System.out.print("Podaj ID roweru do wypożyczenia: ");
//        Long bikeId = scanner.nextLong();
//
//        Client client = clientRepository.findClientById(clientId).orElse(null);
//        Bike bike = bikeRepository.findBikeById(bikeId).orElse(null);
//
//        if (client != null && bike != null && bike.isIsAvailable()) {
//            System.out.print("Podaj kwotę do zapłaty za wypożyczenie: ");
//            double amount = scanner.nextDouble();
//
//            // Proces płatności (można tu dodać więcej logiki)
//            System.out.println("Płatność w wysokości " + amount + " została przetworzona dla klienta: " + client.getFirstName());
//
//            // Tworzenie wypożyczenia z unikalnym ID
//           // int rentalID = generateRentalID();
//            Rental rental = new Rental( client, bike, LocalDateTime.now());
//
//            System.out.println("Rower wypożyczony: " + bike.getModelName() + " przez klienta: " + client.getFirstName());
//
//            // Oznacz rower jako niedostępny
//            bike.setIsAvailable(false);
//        } else {
//            System.out.println("Nieprawidłowy klient lub rower niedostępny.");
//        }
//    }
    private void endRental() {
        System.out.print("Podaj ID klienta, którego wypożyczenie chcesz zakończyć: ");
//        Long clientId = scanner.nextLong();
//        Client client = clientRepository.findClientById(clientId).orElse(null);
//
//        if (client != null) {
////            Rental currentRental = RentalRepository.getCurrentRental(client); // Pobierz aktualne wypożyczenie
//            if (currentRental != null) {
//                RentalRepository.endCurrentRental(client); // Zakończ wypożyczenie
//                double totalCost = currentRental.getTotalCost(); // Oblicz całkowity koszt
//                System.out.println("Wypożyczenie zakończone. Całkowity koszt: " + totalCost + " zł");
//                Bike bike = currentRental.getBike();
//                bike.setIsAvailable(true); // Oznacz rower jako dostępny
//            } else {
//                System.out.println("Brak aktywnych wypożyczeń do zakończenia.");
//            }
//        } else {
//            System.out.println("Nie znaleziono klienta o podanym ID.");
//        }
    }


    private int generateRentalID() {
        // Logika do generowania unikalnego ID dla wypożyczenia
        // Na przykład można wykorzystać licznik lub UUID
        return (int) (Math.random() * 10000); // Przykład prostej losowej wartości
    }

    private void removeBike() {
        System.out.print("Podaj ID roweru do usunięcia: ");
        Long bikeId = scanner.nextLong();
        bikeRepository.removeBikeById(bikeId);
        System.out.println("Rower został usunięty.");
    }

//    private void listBikes() {
//        List<Bike> bikes = bikeRepository.getAllBikes();
//        System.out.println("Lista rowerów:");
//        for (Bike bike : bikes) {
//            System.out.println("Model: " + bike.getModelName() + ", ID: " + bike.getId() + ", Dostępny: " + bike.isIsAvailable());
//        }
//    }
}
