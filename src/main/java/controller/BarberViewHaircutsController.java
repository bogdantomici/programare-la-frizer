package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Haircut;
import org.jetbrains.annotations.NotNull;
import service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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

        haircutObservableList.addAll(haircutList);
        return haircutObservableList;
    }

    @Override
    public void goBackToBarberPage(@NotNull ActionEvent event) throws IOException {
        BarberPageInterface.super.goBackToBarberPage(event);
    }
}
