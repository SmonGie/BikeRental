package org.example.UserInterface;


import org.example.Model.ElectricBike;
import org.example.Model.MountainBike;
import org.example.Model.Rental;
import org.example.Model.clients.Client;
import org.example.Model.clients.Address;
import org.example.Model.Bike;
import org.example.Model.clients.ClientType;
import org.example.Repositories.BikeRepository;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.MongoRepository;
import org.example.Repositories.RentalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final ClientRepository clientRepository;
    private final BikeRepository bikeRepository;
    private final RentalRepository rentalRepository;
    private MongoRepository repo;
    private final Scanner scanner;


    public UserInterface(ClientRepository clientRepository, BikeRepository bikeRepository, RentalRepository rentalRepository, MongoRepository repo) {
        this.clientRepository = clientRepository;
        this.bikeRepository = bikeRepository;
        this.rentalRepository = rentalRepository;
        this.scanner = new Scanner(System.in);
        this.repo = repo;

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
            System.out.println("3. Zarządzanie wypożyczeniami");
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
                    manageRentals();
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
                    return;
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
                    return;
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    private void manageRentals() {
        while (true) {
            System.out.println("\nZarządzanie wypożyczeniami:");
            System.out.println("1. Wypożycz Rower");
            System.out.println("2. Zakończ wypożyczenie");
            System.out.println("3. Przeglądaj wszystkie wypożyczenia");
            System.out.println("4. Przeglądaj zakończone wypożyczenia");
            System.out.println("0. Powrót do menu głównego");

            int choice = readIntegerInput();

            switch (choice) {
                case 1:
//                    rentBike();
                    break;
                case 2:
//                    endRental();
                    break;
                case 3:
                    listRentals();
                    break;
                case 4:
                    listFinishedRentals();
                    break;
                case 0:
                    return;
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
        String city = getValidNameInput();
        System.out.print("Ulica: ");
        String street = getValidNameInput();
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
        scanner.nextLine();

        Client c = clientRepository.findById(clientId);
        if (c == null) {
            System.out.println("Nie znaleziono klienta o podanym ID.");
            return;
        }
        if (c.getRentalCount() > 0) {
            System.out.println("Klient ma aktywne wypożyczenia. Zakończ wypożyczenia przed usunięciem klienta.");
            return;
        }
        List<Rental> rentalHistory = rentalRepository.getRentalHistoryByClientId(clientId);
        if (rentalHistory.isEmpty()) {
            clientRepository.delete(c);
            System.out.println("Klient został usunięty.");
        } else {
            c.setActive(false);
            clientRepository.save(c);
            System.out.println("Klient został oznaczony jako nieaktywny.");
        }
    }

    private void listClients() {
        List<Client> clients = clientRepository.findAll();
        System.out.println("Lista klientów:");
        for (Client client : clients) {
            System.out.println(client.getInfo());
            if (client.getRentalCount() != 0) {
                List<Rental> currentRentals = rentalRepository.getCurrentRentals(client.getId());
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
        System.out.println("1. Rower górski");
        System.out.println("2. Rower elektryczny");

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
//    private void rentBike() {
//
//        System.out.print("Podaj ID klienta: ");
//        Long clientId = scanner.nextLong();
//        scanner.nextLine();
//        System.out.print("Podaj ID roweru do wypożyczenia: ");
//        Long bikeId = scanner.nextLong();
//        scanner.nextLine();
//
//        EntityManager em = emf.createEntityManager();
//        EntityTransaction transaction = em.getTransaction();
//
//        try {
//            transaction.begin();
//
//            Client client = em.find(Client.class, clientId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//            Bike bike = em.find(Bike.class, bikeId);
//
//            if (client != null && bike != null && bike.isIsAvailable()) {
//
//                if (client.getRentalCount() >= 2) {
//                    transaction.rollback();
//                    System.out.println("Klient może mieć maksymalnie 2 wypożyczenia.");
//                    em.close();
//                    return;
//                }
//
//                Rental rental = new Rental(client, bike, LocalDateTime.now());
//                bike.setIsAvailable(false);
//                client.setRentalCount(client.getRentalCount() + 1);
//                client.setActive(true);
//                em.persist(client);
//                em.persist(bike);
//                em.persist(rental);
//
//                transaction.commit();
//
//                System.out.println("Rower wypożyczony: " + bike.getModelName() + " przez klienta: " + client.getFirstName());
//
//            } else {
//                System.out.println("Nieprawidłowy klient lub rower niedostępny.");
//                transaction.rollback();
//                em.close();
//            }
//
//        } catch (OptimisticLockException e) {
//            transaction.rollback();
//            System.out.println("W czasie przetwarzania transakcji doszlo do modyfikacji jednego z obiektow.");
//        } finally {
//            em.close();
//
//        }
//
//    }

//    private void endRental() {
//
//        EntityManager em = emf.createEntityManager();
//        EntityTransaction transaction = em.getTransaction();
//        System.out.print("Podaj ID klienta, którego wypożyczenie chcesz zakończyć: ");
//        Long clientId = scanner.nextLong();
//        scanner.nextLine();
//
//        List<Rental> currentRentals = rentalRepository.getCurrentRentals(clientId);
//
//        if (currentRentals.isEmpty()) {
//            System.out.println("Brak aktywnych wypożyczeń do zakończenia.");
//            return;
//        }
//
//        System.out.println("Aktywne wypożyczenia dla klienta:");
//        for (int i = 0; i < currentRentals.size(); i++) {
//            Rental rental = currentRentals.get(i);
//            System.out.println((i + 1) + ". Rower: " + rental.getBike().getModelName() + ", ID: " + rental.getId());
//        }
//
//        System.out.print("Wybierz numer wypożyczenia do zakończenia: ");
//        int rentalIndex = scanner.nextInt() - 1;
//        scanner.nextLine();
//        if (rentalIndex < 0 || rentalIndex >= currentRentals.size()) {
//            System.out.println("Nieprawidłowy wybór.");
//            return;
//        }
//
//        Rental selectedRental = currentRentals.get(rentalIndex);
//
//        Client client = selectedRental.getClient();
//        ClientType clientType = ClientType.determineClientType(client.getAge());
//        int discount = clientType.applyDiscount();
//
//        Bike bike = selectedRental.getBike();
//
//        try {
//
//            transaction.begin();
//
//            selectedRental.setEndTime(LocalDateTime.now());
//            selectedRental.calculateTotalCost();
//            client.setRentalCount(client.getRentalCount() - 1);
//            bike.setIsAvailable(true);
//
//            em.merge(client);
//            em.merge(bike);
//            em.merge(selectedRental);
//
//            transaction.commit();
//
//        }  catch (OptimisticLockException e) {
//
//            transaction.rollback();
//            System.out.println("Operacja zakończyła się niepowodzeniem");
//
//        } finally {
//            em.close();
//        }
//
//
//        double totalCost = selectedRental.getTotalCost();
//        System.out.println("Wypożyczenie zakończone");
//        System.out.println("Typ klienta: " + clientType + ", Zniżka: " + discount + "%");
//        System.out.println("Całkowity koszt po zniżce: " + totalCost + " zł");
//
//    }

    private void listRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        if(rentals.isEmpty()){
            System.out.println("Aktualnie nie ma wypożyczeń.");
            return;
        }
        System.out.println("Lista wypożyczeń:");
        for (Rental rental : rentals) {
            System.out.println("ID wypożyczenia: " + rental.getId() +
                    ", Klient: " + rental.getClient().getFirstName() + " " + rental.getClient().getLastName() +
                    ", Rower: " + rental.getBike().getModelName() +
                    ", Czas wypożyczenia: " + rental.getStartTime() +
                    (rental.getEndTime() != null ? ", Czas zakończenia: " + rental.getEndTime() : ", Wciąż wypożyczony"));
        }
    }

    private void listFinishedRentals() {
        List<Rental> finishedRentals = rentalRepository.findAll();
        if(finishedRentals.isEmpty()){
            System.out.println("Aktualnie nie ma żadnych zakończonych wypożyczeń.");
            return;
        }
        System.out.println("Lista zakończonych wypożyczeń:");

        for (Rental rental : finishedRentals) {
            if (rental.getEndTime() != null) {
                System.out.println("ID wypożyczenia: " + rental.getId() +
                        ", Klient: " + rental.getClient().getFirstName() + " " + rental.getClient().getLastName() +
                        ", Rower: " + rental.getBike().getModelName() +
                        ", Czas wypożyczenia: " + rental.getStartTime() +
                        ", Czas zakończenia: " + rental.getEndTime());
            }
        }
    }


    private void removeBike() {
        System.out.print("Podaj ID roweru do usunięcia: ");
        Long bikeId = scanner.nextLong();

        List<Rental> currentRentals = rentalRepository.getCurrentRentalsByBikeId(bikeId);
        if (!currentRentals.isEmpty()) {
            System.out.println("Nie można usunąć roweru, ponieważ jest aktualnie wypożyczony.");
            return;
        }
        Bike b = bikeRepository.findById(bikeId);
        if (b == null) {
            System.out.println("Nie znaleziono roweru o podanym ID");
            return;
        }

        bikeRepository.delete(b);
        System.out.println("Rower został usunięty.");

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
                System.out.println("Proszę używać odpowiednich znaków (tylko litery): ");
            }
        }
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ ]+");
    }
}
