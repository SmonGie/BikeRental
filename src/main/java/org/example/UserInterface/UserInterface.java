package org.example.UserInterface;

import com.google.gson.Gson;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.example.Model.Rental;
import org.example.Model.bikes.*;
import org.example.Model.clients.*;
import org.example.Repositories.BikeRepository;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.IBikeRepository;
import org.example.Repositories.RentalRepository;

import redis.clients.jedis.JedisPooled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final ClientRepository clientRepository;
    private final IBikeRepository bikeRepository;
    private final RentalRepository rentalRepository;
    private final MongoClient mongoClient;
    private final Scanner scanner;
    private final JedisPooled pooled;

    public UserInterface(ClientRepository clientRepository, IBikeRepository bikeRepository, RentalRepository rentalRepository, MongoClient mongoClient, JedisPooled pooled) {
        this.clientRepository = clientRepository;
        this.bikeRepository = bikeRepository;
        this.rentalRepository = rentalRepository;
        this.mongoClient = mongoClient;
        this.scanner = new Scanner(System.in);
        this.pooled = pooled;
    }



    public void start() {



        Address a = new Address("lodz", "janowa", "3");
        Client c = new Client("Jedrzej", "Wisniewski", "123123123", 54, a);

        ClientAddressMgd startClient = new ClientAddressMgd(c, a);
        clientRepository.save(startClient);

        MountainBike mtb2 = new MountainBike(true,"lolek X-Cal",120);
        ElectricBike ebike = new ElectricBike(true,"Giant E+",500);
        MountainBikeMgd mountainBikeMgd = new MountainBikeMgd(mtb2);
        ElectricBikeMgd electricBikeMgd = new ElectricBikeMgd(ebike);
        bikeRepository.save(mountainBikeMgd);
        bikeRepository.save(electricBikeMgd);


        final Gson gson = new Gson();
        System.out.println("---------------------------------------------");
        String redisKey = "client1:"+startClient.getClientId();
        pooled.set(redisKey, gson.toJson(startClient));

        String receiver = pooled.get(redisKey);
        ClientAddressMgd fromredis = gson.fromJson(receiver, ClientAddressMgd.class);

        System.out.println(fromredis.getClientId());
        System.out.println(fromredis.getInfo());
        System.out.println("---------------------- teraz bedzie bike! -----------------------");
//        pooled.jsonSet("bike:"+mountainBikeMgd.getBikeId(), gson.toJson(mountainBikeMgd));
//        System.out.println(gson.toJson(mountainBikeMgd));
//        System.out.println(pooled.jsonGet("bike:"+mountainBikeMgd.getBikeId()));
//        MountainBikeMgd bikefromJson = gson.fromJson((String) pooled.jsonGet("bike:"+mountainBikeMgd.getBikeId()),MountainBikeMgd.class);
//        System.out.println(bikefromJson.getEntityId());
//        System.out.println(bikefromJson.getInfo());
        System.out.println("---------------------------------------------");
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

        ClientAddressMgd convertedClient = new ClientAddressMgd(client, address);

        clientRepository.save(convertedClient);
        System.out.println("Klient został dodany.");
    }

    private void removeClient() {
        System.out.print("Podaj ID klienta do usunięcia: ");
        String clientId = scanner.nextLine();

        ClientAddressMgd c = clientRepository.findById(clientId);
        if (c == null) {
            System.out.println("Nie znaleziono klienta o podanym ID.");
            return;
        }
        if (c.getRentalCount() > 0) {
            System.out.println("Klient ma aktywne wypożyczenia. Zakończ wypożyczenia przed usunięciem klienta.");
            return;
        }
        List<Rental> rentalHistory = rentalRepository.getRentalHistoryByClientId(clientId);
        if (rentalHistory == null || rentalHistory.isEmpty()) {
            clientRepository.delete(c);
            System.out.println("Klient został usunięty.");
        } else {
            clientRepository.save(c);
            System.out.println("Klient został oznaczony jako nieaktywny.");
        }
    }

    private void listClients() {
        List<ClientAddressMgd> clients = clientRepository.findAll();

        if (clients == null || clients.isEmpty()) {
            System.out.println("Nie znaleziono żadnych klientów w systemie.");
        } else {
            System.out.println("Lista klientów:");
            for (ClientAddressMgd client : clients) {
                System.out.println("\n-----------------------------------------------------------\n");
                System.out.println(client.getInfo());
            }
            System.out.println("\n-----------------------------------------------------------\n");
        }




    }

    private void addBike() {
        System.out.println("Wybierz typ roweru:");
        System.out.println("1. Rower górski");
        System.out.println("2. Rower elektryczny");

        int choice = readIntegerInput();

        boolean available = true;

        switch (choice) {
            case 1:
                //MountainBike
                System.out.print("Model roweru: ");
                String model = scanner.nextLine();
                System.out.print("Szerokość opony (w cm): ");
                int tireWidth = readIntegerInput();
                MountainBike mountainBike = new MountainBike(available, model, tireWidth);
                MountainBikeMgd newMountain = new MountainBikeMgd(mountainBike);
                bikeRepository.save(newMountain);
                System.out.println("Rower górski został dodany.");
                break;
            case 2:
                //ElectricBike
                System.out.print("Model roweru: ");
                String model2 = scanner.nextLine();
                System.out.print("Pojemność baterii (w Wh): ");
                int batteryCapacity = readIntegerInput();
                ElectricBike electricBike = new ElectricBike(available, model2, batteryCapacity);
                ElectricBikeMgd newElectric = new ElectricBikeMgd(electricBike);
                bikeRepository.save(newElectric);
                System.out.println("Rower elektryczny został dodany.");
                break;
            default:
                System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
        }
    }

    private void rentBike() {
        String temp = "";
        System.out.print("Podaj ID klienta: ");
        String clientId = scanner.nextLine();
        System.out.print("Podaj ID roweru do wypożyczenia: ");
        String bikeId = scanner.nextLine();
        ClientAddressMgd client = clientRepository.findById(clientId);
        BikeMgd bike = bikeRepository.findById(bikeId);

        if (client != null && bike != null && bike.getIsAvailable()) {
//            if (client.getRentalCount() >= 2) {
//                System.out.println("Klient może mieć maksymalnie 2 wypożyczenia.");
//                return;
//            }
            ClientSession clientSession = mongoClient.startSession();
            try  {
                clientSession.startTransaction();
                bike.setIsAvailable(false);
                client.setRentalCount(client.getRentalCount() + 1);
                Rental rental = new Rental(client, bike, LocalDateTime.now());
                clientRepository.update(clientSession, client, "rental_count", client.getRentalCount());
                bikeRepository.update(clientSession, bike, "is_available", bike.getIsAvailable());
                rentalRepository.save(rental);
                clientSession.commitTransaction();
            } catch (Exception e) {
                System.out.println("Napotkano problem. Upewnij się, że klient nie ma już 2 trwających wypożyczeń");
                clientSession.abortTransaction();
                e.printStackTrace();
                temp = " NIE";
            } finally {
                clientSession.close();
            }
            System.out.println("Rower " + bike.getModelName() + temp + " został wypożyczony przez " + client.getFirstName());
        } else {
            System.out.println("Nieprawidłowy klient lub rower niedostępny.");
        }
    }


    private void endRental() {
        System.out.print("Podaj ID klienta, którego wypożyczenie chcesz zakończyć: ");
        String clientId = scanner.nextLine();


        List<Rental> currentRentals = rentalRepository.findById(clientId);

        if (currentRentals.isEmpty()) {
            System.out.println("Brak aktywnych wypożyczeń do zakończenia.");
            return;
        }

        System.out.println("Aktywne wypożyczenia dla klienta:");
        for (int i = 0; i < currentRentals.size(); i++) {
            Rental rental = currentRentals.get(i);
            System.out.println((i + 1) + ". Rower: " + rental.getBike().getModelName() + ", ID wypożyczenia: " + rental.getEntityId().getUuid());
        }

        System.out.print("Wybierz numer wypożyczenia do zakończenia: ");
        int rentalIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        if (rentalIndex < 0 || rentalIndex >= currentRentals.size()) {
            System.out.println("Nieprawidłowy wybór.");
            return;
        }

        Rental selectedRental = currentRentals.get(rentalIndex);
        ClientAddressMgd client = selectedRental.getClient();
        BikeMgd bike = selectedRental.getBike();

        ClientType clientType = client.getClientType();
        int discount = clientType.applyDiscount();
        ClientSession clientSession = mongoClient.startSession();
        try {
            clientSession.startTransaction();
            selectedRental.setEndTime(LocalDateTime.now());
            selectedRental.calculateTotalCost();
            bike.setIsAvailable(true);
            rentalRepository.update(clientSession, selectedRental);
            clientRepository.update(clientSession, client, "rental_count", client.getRentalCount() - 1);
            bikeRepository.update(clientSession, bike, "is_available", true);
            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
        } finally {
            clientSession.close();
        }
        double totalCost = selectedRental.getTotalCost();
        System.out.println("Wypożyczenie zakończone");
        System.out.println("Typ klienta: " + clientType + ", Zniżka: " + discount + "%");
        System.out.println("Całkowity koszt po zniżce: " + totalCost + " zł");
    }


    private void listRentals() {
        List<Rental> rentals = rentalRepository.findAll();

        if (rentals.isEmpty()) {
            System.out.println("Aktualnie nie ma wypożyczeń.");
            return;
        }

        System.out.println("Lista wypożyczeń:");
        for (Rental rental : rentals) {
            System.out.println("\n-----------------------------------------------------------\n");
            System.out.println(" ID wypożyczenia: " + rental.getEntityId().getUuid() +
                    ",\n Klient: " + rental.getClient().getFirstName() + " " + rental.getClient().getLastName() +
                    ",\n Rower: " + rental.getBike().getModelName() +
                    ",\n Czas wypożyczenia: " + rental.getStartTime() +
                    (rental.getEndTime() != null ? ",\n Czas zakończenia: " + rental.getEndTime() : ", Wciąż wypożyczony"));
        }
        System.out.println("\n-----------------------------------------------------------\n");
    }

    private void listFinishedRentals() {
        List<Rental> finishedRentals = rentalRepository.findAllFinished();

        if (finishedRentals.isEmpty()) {
            System.out.println("Aktualnie nie ma żadnych zakończonych wypożyczeń.");
            return;
        }

        System.out.println("Lista zakończonych wypożyczeń:");
        for (Rental rental : finishedRentals) {
            System.out.println("\n-----------------------------------------------------------\n");
            System.out.println(" ID wypożyczenia: " + rental.getEntityId().getUuid() +
                    ",\n Klient: " + rental.getClient().getFirstName() + " " + rental.getClient().getLastName() +
                    ",\n Rower: " + rental.getBike().getModelName() +
                    ",\n Czas wypożyczenia: " + rental.getStartTime() +
                    ",\n Czas zakończenia: " + rental.getEndTime() );
        }
        System.out.println("\n-----------------------------------------------------------\n");
    }

    private void removeBike() {
        System.out.print("Podaj ID roweru do usunięcia: ");
        String bikeId = scanner.nextLine();

        List<Rental> currentRentals = rentalRepository.getCurrentRentalsByBikeId(bikeId);
        if (!currentRentals.isEmpty()) {
            System.out.println("Nie można usunąć roweru, ponieważ jest aktualnie wypożyczony.");
            return;
        }
        BikeMgd b = bikeRepository.findById(bikeId);
        if (b == null) {
            System.out.println("Nie znaleziono roweru o podanym ID");
            return;
        }

        bikeRepository.delete(b);
        System.out.println("Rower został usunięty.");

    }

    private void listBikes() {
        List<BikeMgd> bikes = bikeRepository.findAll();


        if (bikes == null || bikes.isEmpty()) {
            System.out.println("Nie znaleziono żadnych rowerów w systemie.");
        } else {
            System.out.println("Lista rowerów:");
            for (BikeMgd bike : bikes) {

                System.out.println("\n-----------------------------------------------------------\n");
                System.out.println(bike.getInfo());
            }
            System.out.println("\n-----------------------------------------------------------\n");
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
