package service;

import exception.FieldNotCompletedException;
import model.Haircut;
import model.User;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static service.FileSystemService.getPathToFile;
import static service.UserService.encodePassword;

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

    public static void addHaircut(String id, String name, float price, @NotNull User user) throws FieldNotCompletedException {
        checkAllFieldsAreCompleted(name, price);

        List<Haircut> haircutList = user.getHaircutList();  //get the barber haircut list, add the new haircut to it and insert it into the DB
        Haircut haircut = new Haircut(id, name, price);
        haircutList.add(haircut);
        user.setHaircutList(haircutList);
        haircutRepository.insert(haircut);

        User newUser = new User(user.getUsername(), encodePassword(user.getUsername(), user.getPassword()), user.getFirstName(), user.getSecondName(), user.getPhoneNumber(), user.getAddress(), user.getRole(), user.getHaircutList());
        UserService.getUsers().remove(user);
        UserService.getUsers().insert(newUser);
    }

    public static void checkAllFieldsAreCompleted(@NotNull String name, float price) throws FieldNotCompletedException {
        if (name.trim().isEmpty() || String.valueOf(price).trim().isEmpty())
            throw new FieldNotCompletedException();
    }

    public static boolean checkIfPriceIsAFloat(String price) {
        try {
            Float.parseFloat(price);
            return true;

        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static ObjectRepository<Haircut> getHaircutRepository() {
        return haircutRepository;
    }

    public static Nitrite getDatabase() {
        return database;
    }

    public static List<Haircut> getServiceList() {
        return haircutRepository.find().toList();
    }
}
