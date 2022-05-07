package controller;

import exception.HaircutWasNotSelectedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Haircut;
import model.User;
import org.jetbrains.annotations.NotNull;
import service.HaircutService;
import service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class BarberViewHaircutsController implements BarberPageInterface, Initializable {

    @FXML
    private TableView<Haircut> haircutsTable;

    @FXML
    private TableColumn<Haircut, String> haircutColumnName;

    @FXML
    private TableColumn<Haircut, String> haircutColumnPrice;

    @FXML
    private Button backButton;

    @FXML
    private Button deleteHaircutButton;

    @FXML
    private Text deleteHaircutMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        haircutColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        haircutColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        haircutsTable.setItems(getHaircutsObservableArrayList());
    }

    @NotNull
    private ObservableList<Haircut> getHaircutsObservableArrayList() {
        ObservableList<Haircut> haircutObservableList = FXCollections.observableArrayList();

        List<Haircut> haircutList = UserService.getUserHaircutList(LoginController.getLoggedUser().getUsername());

        System.out.println(haircutList);
        haircutObservableList.addAll(haircutList);
        return haircutObservableList;
    }

    @FXML
    private void deleteHaircut(@NotNull javafx.event.ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete haircut");
        alert.setHeaderText("Are you sure you want to delete the haircut?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    removeSelectedService();
                    refreshPage(event);
                } catch (HaircutWasNotSelectedException e) {
                    deleteHaircutMessage.setVisible(true);
                    deleteHaircutMessage.setText(e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else
                deleteHaircutMessage.setVisible(false);
            alert.close();
        });
    }

    private void removeSelectedService() throws HaircutWasNotSelectedException {
        Haircut deletedHaircut = haircutsTable.getSelectionModel().getSelectedItem();   //select the wanted haircut to delete

        if (Objects.equals(deletedHaircut, null))       //if no haircut is selected, throw an error
            throw new HaircutWasNotSelectedException();
        else {
            User currentBarber = LoginController.getLoggedUser();   //get the current and persisted barber from the DB
            User persistedBarber = LoginController.getLoggedUser();

            List<Haircut> barberHaircuts =  LoginController.getLoggedUser().getHaircutList();  //assign the current barber haircut list to barberHaircuts

            HaircutService.getHaircutRepository().remove(deletedHaircut);   //delete the selected haircut from the DB

            barberHaircuts.remove(deletedHaircut);  //delete the selected haircut from the current barber list
            persistedBarber.setHaircutList(barberHaircuts); //set the current haircut list to the persisted barber list

            String encryptedPassword = UserService.encodePassword(currentBarber.getUsername(), currentBarber.getPassword());    //encrypt the current barber password

            persistedBarber.setPassword(encryptedPassword);
            UserService.getUsers().remove(currentBarber);
            UserService.getUsers().insert(persistedBarber);
        }
    }

    @FXML
    private void refreshPage(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("fxml/barber_view_haircuts.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 661, 550);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @Override
    public void goBackToBarberPage(@NotNull ActionEvent event) throws IOException {
        BarberPageInterface.super.goBackToBarberPage(event);
    }
}
