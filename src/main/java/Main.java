import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.*;

public class Main extends Application {

    private double xoffset, yoffset;

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserService.initDatabase();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/user_registration.fxml"));

        primaryStage.setTitle("ProgramareFrizer");
        primaryStage.setScene(new Scene(root, 655, 500));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        root.setOnMousePressed(event->{
            xoffset = event.getScreenX();
            yoffset = event.getScreenY();
        });

        root.setOnMouseDragged(e ->{
            primaryStage.setX(e.getScreenX());
            primaryStage.setY(e.getScreenY());

        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}