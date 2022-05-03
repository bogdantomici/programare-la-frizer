import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Haircut;
import service.AppointmentService;
import service.HaircutService;
import service.UserService;

import java.util.Objects;

public class Main extends Application {

    private double xoffset, yoffset;
    private boolean existsInDatabse = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserService.initDatabase();
        HaircutService.initDatabase();
        AppointmentService.initDatabase();

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/user_login.fxml"));

        Haircut haircut = new Haircut("1", "Short Cut", 5);

        for (Haircut haircut1 : HaircutService.getHaircutRepository().find())
            if (Objects.equals(haircut1.getId(), "1")) {
                existsInDatabse = true;
                break;
            }
        if (!existsInDatabse) {
            HaircutService.getHaircutRepository().insert(haircut);
        }

        primaryStage.setTitle("ProgramareFrizer");
        primaryStage.setScene(new Scene(root, 655, 500));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        root.setOnMousePressed(event -> {
            xoffset = event.getScreenX();
            yoffset = event.getScreenY();
        });

        root.setOnMouseDragged(e -> {
            primaryStage.setX(e.getScreenX());
            primaryStage.setY(e.getScreenY());

        });
    }
}