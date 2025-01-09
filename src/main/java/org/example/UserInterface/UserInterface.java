package org.example.UserInterface;


import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.example.Dao.BikeDao;
import org.example.Dao.ClientDao;
import org.example.Mappers.BikeMapper;
import org.example.Mappers.BikeMapperBuilder;
import org.example.Mappers.ClientMapper;
import org.example.Mappers.ClientMapperBuilder;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class UserInterface {
    private final ClientRepository clientRepository;
    private final BikeRepository bikeRepository;
    private final RentalRepository rentalRepository;
    private final Scanner scanner;
    private final BikeDao bikeDao;
    private final ClientDao clientDao;


    public UserInterface(ClientRepository clientRepository, BikeRepository bikeRepository, RentalRepository rentalRepository) {
        this.clientRepository = clientRepository;
        this.bikeRepository = bikeRepository;
        this.rentalRepository = rentalRepository;
        this.scanner = new Scanner(System.in);
        BikeMapper bikeMapper = new BikeMapperBuilder(bikeRepository.getSession()).build();
        ClientMapper clientMapper = new ClientMapperBuilder(clientRepository.getSession()).build();
        bikeDao = bikeMapper.bikeDao("bikeRental", "bikes");
        clientDao = clientMapper.clientDao("bikeRental", "clients");


    }


    public void start() {

        Address a = new Address("lodz", "janowa", "3");
        Client c = new Client("Jedrzej", "Wisniewski", "123123123", 54, a);

        clientDao.create(c);

        MountainBike mtb2 = new MountainBike("lolek X-Cal", true, 120);
        MountainBike mtb = new MountainBike("Trek X-Cal", true, 120);
        ElectricBike ebike = new ElectricBike("Giant E+", true, 500);

        bikeDao.create(mtb2);
        bikeDao.create(mtb);
        bikeDao.create(ebike);
        LocalDateTime date = LocalDateTime.now();
        c.setRentalCount(c.getRentalCount() + 1);
        clientDao.update(c);
        Rental rental = new Rental(c, mtb, date);
        rentalRepository.insert(rental);

        while (true) {
            System.out.println("Wybierz opcję:");
            System.out.println("1. Zarządzanie klientami");
            System.out.println("2. Zarządzanie rowerami");
            System.out.println("3. Zarządzanie wypożyczeniami");
            System.out.println("4. WYCZYSC BAZE DANYCH");
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
                case 4:
                    bikeRepository.deleteData();
                    clientRepository.deleteData();
                    rentalRepository.deleteDataByBikes();
                    rentalRepository.deleteDataByClients();
                    return;
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
                    rentBike();
                    break;
                case 2:
                    endRental();
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
        clientDao.create(client);
        System.out.println("Klient został dodany.");
    }

    private void removeClient() {
        System.out.print("Podaj ID klienta do usunięcia: ");
        long clientId = scanner.nextLong();
        scanner.nextLine();
        UUID uuid = UUID.fromString(Long.toString(clientId));
        Client c = clientDao.findById(uuid);
        if (c == null) {
            System.out.println("Nie znaleziono klienta o podanym ID.");
            return;
        }
        if (c.getRentalCount() > 0) {
            System.out.println("Klient ma aktywne wypożyczenia. Zakończ wypożyczenia przed usunięciem klienta.");
            return;
        }

        clientDao.remove(c);
        System.out.println("Klient został usunięty.");

    }

    private void listClients() {
        ResultSet resultSet = clientDao.findAll();
        List<Client> clients = new ArrayList<>();

        for (Row row : resultSet) {
            Client client = mapRowToClient(row);
            clients.add(client);
        }

        System.out.println("Lista klientów:");
        for (Client client : clients) {
            System.out.println(client.getInfo());
            System.out.println(client.getRentalCount());
            if (client.getRentalCount() != 0) {
                List<Rental> currentRentals = rentalRepository.findByClientId(client.getId());
                System.out.println("Aktualne wypożyczenia:");
                for (Rental rental : currentRentals) {
                    Bike bike = bikeDao.findById(rental.getBikeId());
                    System.out.println(" - Rower: " + bike.getInfo() + ", ID: " + rental.getId());
                }
            } else {
                System.out.println("Brak aktywnych wypożyczeń.");
            }
        }
    }

    private Client mapRowToClient(Row row) {
        Client client = new Client();
        client.setId(row.getUuid("uuid"));
        client.setFirstName(row.getString("first_name"));
        client.setLastName(row.getString("last_name"));
        client.setPhoneNumber(row.getString("phone_number"));
        client.setAge(row.getInt("age"));
        client.setRentalCount(row.getInt("rental_count"));
        client.setAddress(row.getString("client_address"));

        return client;
    }


    private void addBike() {
        System.out.println("Wybierz typ roweru:");
        System.out.println("1. Rower górski");
        System.out.println("2. Rower elektryczny");

        int choice = readIntegerInput();

        System.out.print("Model roweru: ");
        String model = scanner.nextLine();

        boolean available = true;

        switch (choice) {
            case 1:
                //MountainBike
                System.out.print("Szerokość opony (w cm): ");
                int tireWidth = readIntegerInput();
                MountainBike mountainBike = new MountainBike(model, available, tireWidth);
                bikeDao.create(mountainBike);
                System.out.println("Rower górski został dodany.");
                break;
            case 2:
                //ElectricBike
                System.out.print("Pojemność baterii (w Wh): ");
                int batteryCapacity = readIntegerInput();
                ElectricBike electricBike = new ElectricBike(model, available, batteryCapacity);
                bikeDao.create(electricBike);
                System.out.println("Rower elektryczny został dodany.");
                break;
            default:
                System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
        }
    }

    private void rentBike() {
        System.out.print("Podaj ID klienta (UUID): ");
        String clientIdStr = scanner.nextLine();
        System.out.print("Podaj ID roweru do wypożyczenia (UUID): ");
        String bikeIdStr = scanner.nextLine();

        try {
            UUID clientId = UUID.fromString(clientIdStr);
            Client client = clientDao.findById(clientId);

            UUID bikeId = UUID.fromString(bikeIdStr);
            Bike bike = bikeDao.findById(bikeId);

            if (client != null && bike != null && bike.isIsAvailable()) {

                if (client.getRentalCount() >= 2) {
                    System.out.println("Klient może mieć maksymalnie 2 wypożyczenia.");
                    return;
                }

                Rental rental = new Rental(client, bike, LocalDateTime.now());

                rentalRepository.insert(rental);
                client.setRentalCount(client.getRentalCount() + 1);
                clientDao.update(client);
                bike.setIsAvailable(false);
                bikeDao.update(bike);

                System.out.println("Rower wypożyczony: " + bike.getModelName() + " przez klienta: " + client.getFirstName());

            } else {
                System.out.println("Nieprawidłowy klient lub rower niedostępny.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Podano nieprawidłowy format UUID.");
        } catch (Exception e) {
            System.out.println("W czasie przetwarzania transakcji doszlo do modyfikacji jednego z obiektow.");
        }
    }


    private void endRental() {
        System.out.print("Podaj ID klienta, którego wypożyczenie chcesz zakończyć: ");
        String clientIdStr = scanner.nextLine();

        UUID clientUUID = UUID.fromString(clientIdStr);

        List<Rental> currentRentals = rentalRepository.findByClientId(clientUUID);

        if (currentRentals.isEmpty()) {
            System.out.println("Brak aktywnych wypożyczeń do zakończenia.");
            return;
        }

        System.out.println("Aktywne wypożyczenia dla klienta:");
        for (Rental rental : currentRentals) {
            Bike bike = bikeDao.findById(rental.getBikeId());
            int index = currentRentals.indexOf(rental);
            System.out.println(index - 1+ ". Rower: " + bike.getInfo() + ", ID: " + rental.getId());
        }

        System.out.print("Wybierz numer wypożyczenia do zakończenia: ");
        int rentalIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        if (rentalIndex < 0 || rentalIndex >= currentRentals.size()) {
            System.out.println("Nieprawidłowy wybór.");
            return;
        }

        Rental selectedRental = currentRentals.get(rentalIndex);
        Client client = clientDao.findById(selectedRental.getClientId());
        if (client.getClientType() == null) {
            client.setClientType(ClientType.determineClientType(client.getAge()));
        }
        ClientType clientType = client.getClientType();
        int discount = clientType.applyDiscount();

        Bike bike = bikeDao.findById(selectedRental.getBikeId());
        selectedRental.setClient(client);
        selectedRental.setBike(bike);
        try {
            selectedRental.setEndTime(LocalDateTime.now());
            selectedRental.calculateTotalCost(client);

            client.setRentalCount(client.getRentalCount() - 1);
            bike.setIsAvailable(true);

            rentalRepository.endRent(selectedRental);
            clientDao.update(client);
            bikeDao.update(bike);
            System.out.println("Wypożyczenie zakończone");
            System.out.println("Typ klienta: " + clientType + ", Zniżka: " + discount + "%");
            System.out.println("Całkowity koszt po zniżce: " + selectedRental.getTotalCost() + " zł");

        } catch (Exception e) {
            System.out.println("Operacja zakończyła się niepowodzeniem");
            e.printStackTrace();
        }
    }

    private void listRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        if (rentals.isEmpty()) {
            System.out.println("Aktualnie nie ma wypożyczeń.");
            return;
        }
        System.out.println("Lista wypożyczeń:");
        for (Rental rental : rentals) {
            Bike bike = bikeDao.findById(rental.getBikeId());
            Client client = clientDao.findById(rental.getClientId());

            System.out.println("ID wypożyczenia: " + rental.getId() +
                    ", Klient: " + client.getFirstName() + " " + client.getLastName() +
                    ", Rower: " + bike.getModelName() +
                    ", Czas wypożyczenia: " + rental.getStartTime() +
                    (rental.getEndTime() != null ? ", Czas zakończenia: " + rental.getEndTime() : ", Wciąż wypożyczony"));
        }
    }

    private void listFinishedRentals() {
        List<Rental> finishedRentals = rentalRepository.findAll();
        if (finishedRentals.isEmpty()) {
            System.out.println("Aktualnie nie ma żadnych zakończonych wypożyczeń.");
            return;
        }
        System.out.println("Lista zakończonych wypożyczeń:");

        for (Rental rental : finishedRentals) {
            if (rental.getEndTime() != null) {
                Bike bike = bikeDao.findById(rental.getBikeId());
                Client client = clientDao.findById(rental.getClientId());

                System.out.println("ID wypożyczenia: " + rental.getId() +
                        ", Klient: " + client.getFirstName() + " " + client.getLastName() +
                        ", Rower: " + bike.getModelName() +
                        ", Czas wypożyczenia: " + rental.getStartTime() +
                        ", Czas zakończenia: " + rental.getEndTime());
            }
        }
    }

    private void removeBike() {
        System.out.print("Podaj ID roweru do usunięcia: ");
        long bikeId = scanner.nextLong();
        UUID uuid = UUID.fromString(Long.toString(bikeId));

        List<Rental> currentRentals = rentalRepository.findByBikeId(uuid);
        if (!currentRentals.isEmpty()) {
            for (Rental rental : currentRentals) {
                if (rental.getEndTime() != null) {
                    System.out.println("Nie można usunąć roweru, ponieważ jest aktualnie wypożyczony.");
                    return;
                }
            }
        }
        Bike b = bikeDao.findById(uuid);
        if (b == null) {
            System.out.println("Nie znaleziono roweru o podanym ID");
            return;
        }

        bikeDao.remove(b);
        System.out.println("Rower został usunięty.");

    }

    private void listBikes() {
        List<Bike> bikes = bikeDao.findAll();

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
