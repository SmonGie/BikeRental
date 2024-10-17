package org.example.UserInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.*;
import org.example.Model.ElectricBike;
import org.example.Model.MountainBike;
import org.example.Model.Rental;
import org.example.Model.clients.Client;
import org.example.Model.clients.Address;
import org.example.Model.Bike;
import org.example.Model.clients.ClientType;
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
    private final EntityManagerFactory emf;

    public UserInterface(ClientRepository clientRepository, BikeRepository bikeRepository, RentalRepository rentalRepository,EntityManagerFactory emf) {
        this.clientRepository = clientRepository;
        this.bikeRepository = bikeRepository;
        this.rentalRepository = rentalRepository;
        this.scanner = new Scanner(System.in);
        this.emf = emf;
    }


    public void start() {


        Address a = new Address("lodz", "janowa", "3");
        Client c = new Client("Jedrzej", "Wisniewski", "123123123", 54, a);

        clientRepository.save(c);

        MountainBike mtb2 = new MountainBike("lolek X-Cal", true, 120);
        MountainBike mtb = new MountainBike("Trek X-Cal", true, 120);
        ElectricBike ebike = new ElectricBike("Giant E+", true, 500);
        bikeRepository.save(mtb);
        bikeRepository.save(mtb2);
        bikeRepository.save(ebike);




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
            return;
        }
        List<Rental> currentRentals = rentalRepository.getCurrentRentals(clientId);
        if (!currentRentals.isEmpty()) {
            System.out.println("Klient ma aktywne wypożyczenia. Zakończ wypożyczenia przed usunięciem klienta.");
            return;
        }
        c.setActive(false);
        clientRepository.save(c);
        System.out.println("Klient został oznaczony jako nieaktywny.");
    }

    private void listClients() {
        List<Client> clients = clientRepository.findAll();
        System.out.println("Lista klientów:");
        for (Client client : clients) {
            System.out.println(client.getInfo());
            List<Rental> currentRentals = rentalRepository.getCurrentRentals(client.getId());
            if (!currentRentals.isEmpty()) {
                System.out.println("Aktualne wypożyczenia:");
                for (Rental rental : currentRentals) {
                    System.out.println(" - Rower: " + rental.getBike().getModelName() + ", ID: " + rental.getId());
                }
            } else {
                System.out.println("Brak aktywnych wypożyczeń.");
            }
        }
    }

    private void addBike() {
        System.out.println("Wybierz typ roweru:");
        System.out.println("1. MountainBike");
        System.out.println("2. ElectricBike");

        int choice = readIntegerInput();

        System.out.print("Model roweru: ");
        String model = scanner.nextLine();

        boolean available = true; //Domyślnie rower jest dostępny

        switch (choice) {
            case 1:
                //MountainBike
                System.out.print("Szerokość opony (w cm): ");
                int tireWidth = readIntegerInput();
                MountainBike mountainBike = new MountainBike(model, available, tireWidth);
                bikeRepository.save(mountainBike);
                System.out.println("Rower górski został dodany.");
                break;
            case 2:
                //ElectricBike
                System.out.print("Pojemność baterii (w Wh): ");
                int batteryCapacity = readIntegerInput();
                ElectricBike electricBike = new ElectricBike(model, available, batteryCapacity);
                bikeRepository.save(electricBike);
                System.out.println("Rower elektryczny został dodany.");
                break;
            default:
                System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
        }
    }

    public void rentBike() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            System.out.print("Podaj ID klienta: ");
            Long clientId = scanner.nextLong();
            scanner.nextLine();
            System.out.print("Podaj ID roweru do wypożyczenia: ");
            Long bikeId = scanner.nextLong();
            scanner.nextLine();

            Client client = clientRepository.findById(clientId);
            Bike bike = bikeRepository.findById(bikeId);

            if (client != null && bike != null && bike.isIsAvailable()) {
                System.out.print("Podaj kwotę do zapłaty za wypożyczenie: ");

                List<Rental> currentRentals = rentalRepository.getCurrentRentals(clientId);
                if (currentRentals.size() >= 2) {
                    System.out.println("Klient może mieć maksymalnie 2 wypożyczenia.");
                    return;
                }

                double amount = scanner.nextDouble();
                scanner.nextLine();

                System.out.println("Płatność w wysokości " + amount + " została przetworzona dla klienta: " + client.getFirstName());

                Rental rental = new Rental(client, bike, LocalDateTime.now());
                rentalRepository.save(rental);

                bike.setIsAvailable(false);
                bikeRepository.save(bike);

                tx.commit(); // Zatwierdź transakcję
                System.out.println("Rower wypożyczony: " + bike.getModelName() + " przez klienta: " + client.getFirstName());
            } else {
                System.out.println("Nieprawidłowy klient lub rower niedostępny.");
            }

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Wystąpił błąd: " + e.getMessage());
        } finally {
            em.close();
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
        scanner.nextLine();
        if (rentalIndex < 0 || rentalIndex >= currentRentals.size()) {
            System.out.println("Nieprawidłowy wybór.");
            return;
        }

        Rental selectedRental = currentRentals.get(rentalIndex);
        selectedRental.setEndTime(LocalDateTime.now());
        selectedRental.calculateTotalCost();
        rentalRepository.save(selectedRental);

        Client client = selectedRental.getClient();

        ClientType clientType = ClientType.determineClientType(client.getAge());
        int discount = clientType.applyDiscount();

        double totalCost = selectedRental.getTotalCost();
        System.out.println("Wypożyczenie zakończone");
        System.out.println("Typ klienta: " + clientType + ", Zniżka: " + discount + "%");
        System.out.println("Całkowity koszt po zniżce: " + totalCost + " zł");

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
            System.out.println(bike.getInfo());
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
