package service;

import exception.FieldNotCompletedException;
import exception.HaircutNameAlreadyExistsException;
import model.Haircut;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static service.FileSystemService.getPathToFile;

public class HaircutService {
    private static ObjectRepository<Haircut> haircutRepository;
    private static Nitrite database;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("haircut_database.db").toFile())
                .openOrCreate("test2", "test2");

        haircutRepository = database.getRepository(Haircut.class);
    }

    public static void addHaircut(String name, float price) throws FieldNotCompletedException, HaircutNameAlreadyExistsException {
        checkHaircutNameAlreadyExist(name);
        checkAllFieldsAreCompleted(name, price);
        haircutRepository.insert(new Haircut(name, price));
    }

    public static void checkAllFieldsAreCompleted(@NotNull String name, float price) throws FieldNotCompletedException {
        if (name.trim().isEmpty() || String.valueOf(price).trim().isEmpty())
            throw new FieldNotCompletedException();
    }

    public static void checkHaircutNameAlreadyExist(String name) throws HaircutNameAlreadyExistsException {
        for (Haircut haircut : haircutRepository.find()) {
            if (Objects.equals(name, haircut.getName()))
                throw new HaircutNameAlreadyExistsException(name);
        }
    }

    public static boolean checkIfPriceIsAFloat(String price) {
        try {
            Float.parseFloat(price);
            return true;

        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @NotNull
    public static ArrayList<String> getBarberHaircutsNameList() {
        ArrayList<String> barberHaircutList = new ArrayList<>();

        for (Haircut service : haircutRepository.find()) {
            barberHaircutList.add(service.getName());
        }

        return barberHaircutList;
    }

    public static ObjectRepository<Haircut> getDentistRepository() {
        return haircutRepository;
    }

    public static Nitrite getDatabase() {
        return database;
    }

    public static List<Haircut> getServiceList() {
        return haircutRepository.find().toList();
    }
}
