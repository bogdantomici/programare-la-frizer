package controller;

import exception.FieldNotCompletedException;
import exception.HaircutIncorrectPriceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.dizitart.no2.NitriteId;
import org.jetbrains.annotations.NotNull;
import service.HaircutService;

import java.io.IOException;

public class BarberAddHaircutController implements BarberPageInterface {
    @FXML
    private Button backButton;

    @FXML
    private TextField haircutNameField;

    @FXML
    private TextField priceField;

    @FXML
    private Button createHaircutButton;

    @FXML
    private Text haircutMessage;

    @FXML
    private void createHaircut() {
        try {
            insertHaircutIntoDBIfCorrectPriceFieldFormat();
            haircutMessage.setText("Haircut successfully added !");
            clearFields();
        } catch (FieldNotCompletedException e) {
            haircutMessage.setText(e.getMessage());
        } catch (HaircutIncorrectPriceException e) {
            haircutMessage.setText(e.getMessage());
            priceField.clear();
        }
    }

    private void insertHaircutIntoDBIfCorrectPriceFieldFormat() throws FieldNotCompletedException, HaircutIncorrectPriceException {
        if (HaircutService.checkIfPriceIsAFloat(priceField.getText())) {
            String id = NitriteId.newId().toString();
            HaircutService.addHaircut(id, haircutNameField.getText(), Float.parseFloat(priceField.getText()), LoginController.getLoggedUser());
        } else
            throw new HaircutIncorrectPriceException();
    }

    private void clearFields() {
        haircutNameField.clear();
        priceField.clear();
    }

    @Override
    public void goBackToBarberPage(@NotNull ActionEvent event) throws IOException {
        BarberPageInterface.super.goBackToBarberPage(event);
    }
}
