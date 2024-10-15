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


        Address a = new Address("lodz", "janowa", "3");
        Client c = new Client("Jedrzej", "Wisniewski", "123123123", 54, a);
        clientRepository.save(c);
//        List<Client> clients = clientRepository.findAll();
//        for (Client client : clients) {
//
//            System.out.println(client.getInfo());
//
//        }


        while (true) {
            System.out.println("Wybierz opcję:");
            System.out.println("1. Zarządzanie klientami");
            System.out.println("2. Zarządzanie rowerami");
            System.out.println("3. Wypożyczenie roweru");
            System.out.println("4. Zakończenie wypożyczenia");
            System.out.println("0. Wyjdź");

            int choice = readIntegerInput();

            switch (choice) {
                case 1:
                    manageClients();
                    break;
                case 2:
                    manageBikes();
                    break;
                case 3:
                    rentBike();
                    break;
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

    private int readIntegerInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Proszę wpisać poprawną liczbę.");
            }
        }
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }


    private void manageClients() {
        while (true) {
            System.out.println("\nZarządzanie klientami:");
            System.out.println("1. Dodaj klienta");
            System.out.println("2. Usuń klienta");
            System.out.println("3. Przeglądaj klientów");
            System.out.println("0. Powrót do menu głównego");

            int choice = readIntegerInput();

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
            System.out.println("3. Przeglądaj rowery");
            System.out.println("0. Powrót do menu głównego");

            int choice = readIntegerInput();

            switch (choice) {
                case 1:
                    addBike();
                    break;
                case 2:
                    removeBike();
                    break;
                case 3:
                    listBikes();
                    break;
                case 0:
                    return; // Powrót do menu głównego
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    private void addClient() {
        System.out.print("Imię: ");
        String firstName = getValidNameInput();
        System.out.print("Nazwisko: ");
        String lastName = getValidNameInput();
        System.out.print("Numer telefonu: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Wiek: ");
        int age = readIntegerInput();
        scanner.nextLine();
        System.out.print("Miasto: ");
        String city = scanner.nextLine();
        System.out.print("Ulica: ");
        String street = scanner.nextLine();
        System.out.print("Numer: ");
        String number = scanner.nextLine();

        Address address = new Address(city, street, number);
        Client client = new Client(firstName, lastName, phoneNumber, age, address);
        clientRepository.save(client);
        System.out.println("Klient został dodany.");
    }

    private void removeClient() {
        System.out.print("Podaj ID klienta do usunięcia: ");
        Long clientId = scanner.nextLong();

        Client c = clientRepository.findById(clientId);
        if (c == null) {
            System.out.println("Nie znaleziono klienta o podanym ID.");
        } else {
            clientRepository.delete(c);
            System.out.println("Klient został usunięty.");
        }

    }

    private void listClients() {
        List<Client> clients = clientRepository.findAll();
        System.out.println("Lista klientów:");
        for (Client client : clients) {
            System.out.println(client.getInfo());
        }
    }

    private void addBike() {
        System.out.print("Model roweru: ");
        String model = scanner.nextLine();

        boolean available = true; // Domyślnie rower jest dostępny

        Bike bike = new Bike(model, available);
        bikeRepository.save(bike);
        System.out.println("Rower został dodany.");
    }

    private void rentBike() {
        System.out.print("Podaj ID klienta: ");
        Long clientId = scanner.nextLong();
        System.out.print("Podaj ID roweru do wypożyczenia: ");
        Long bikeId = scanner.nextLong();

        Client client = clientRepository.findById(clientId);
        Bike bike = bikeRepository.findById(bikeId);

        if (client != null && bike != null && bike.isIsAvailable()) {
            System.out.print("Podaj kwotę do zapłaty za wypożyczenie: ");
            double amount = scanner.nextDouble();

            System.out.println("Płatność w wysokości " + amount + " została przetworzona dla klienta: " + client.getFirstName());

            Rental rental = new Rental(client, bike, LocalDateTime.now());

            rentalRepository.save(rental);

            bike.setIsAvailable(false);
            bikeRepository.save(bike);

            System.out.println("Rower wypożyczony: " + bike.getModelName() + " przez klienta: " + client.getFirstName());
        } else {
            System.out.println("Nieprawidłowy klient lub rower niedostępny.");
        }
    }

    private void endRental() {
        System.out.print("Podaj ID klienta, którego wypożyczenie chcesz zakończyć: ");
        Long clientId = scanner.nextLong();
        scanner.nextLine();

        List<Rental> currentRentals = rentalRepository.getCurrentRentals(clientId);

        if (currentRentals.isEmpty()) {
            System.out.println("Brak aktywnych wypożyczeń do zakończenia.");
            return;
        }

        System.out.println("Aktywne wypożyczenia dla klienta:");
        for (int i = 0; i < currentRentals.size(); i++) {
            Rental rental = currentRentals.get(i);
            System.out.println((i + 1) + ". Rower: " + rental.getBike().getModelName() + ", ID: " + rental.getId());
        }

        System.out.print("Wybierz numer wypożyczenia do zakończenia: ");
        int rentalIndex = scanner.nextInt() - 1;
        if (rentalIndex < 0 || rentalIndex >= currentRentals.size()) {
            System.out.println("Nieprawidłowy wybór.");
            return;
        }

        Rental selectedRental = currentRentals.get(rentalIndex);

        selectedRental.setEndTime(LocalDateTime.now());
        rentalRepository.save(selectedRental);

        double totalCost = selectedRental.getTotalCost();

        System.out.println("Wypożyczenie zakończone. Całkowity koszt: " + totalCost + " zł");

        Bike bike = selectedRental.getBike();
        bike.setIsAvailable(true);
        bikeRepository.save(bike);
    }



    private void removeBike() {
        System.out.print("Podaj ID roweru do usunięcia: ");
        Long bikeId = scanner.nextLong();
        Bike b = bikeRepository.findById(bikeId);
        if (b == null) {
            System.out.println("Nie znaleziono roweru o podanym ID");
        } else {
            bikeRepository.delete(bikeRepository.findById(bikeId));
            System.out.println("Rower został usunięty.");
        }
    }

    private void listBikes() {
        List<Bike> bikes = bikeRepository.findAll();
        System.out.println("Lista rowerów:");
        for (Bike bike : bikes) {
            System.out.println("Model: " + bike.getModelName() + ", ID: " + bike.getId() + ", Dostępny: " + bike.isIsAvailable());
        }
    }

    private String getValidNameInput() {
        while (true) {
            String input = scanner.nextLine();
            if (isValidName(input)) {
                return capitalizeFirstLetter(input);
            } else {
                System.out.println("Proszę wpisać poprawne imię/nazwisko (tylko litery): ");
            }
        }
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ ]+");
    }
}
