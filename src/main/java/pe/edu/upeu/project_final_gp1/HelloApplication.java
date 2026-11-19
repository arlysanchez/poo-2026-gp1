package pe.edu.upeu.project_final_gp1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader
                (HelloApplication.class.getResource("/pe/edu/upeu/project_final_gp1/views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        // Asignación de ícono a la ventana principal
        stage.getIcons().add(new
                Image(getClass().getResourceAsStream(
                        "/pe/edu/upeu/project_final_gp1/img/logo.png")));
        stage.setTitle("Login - Sistema Hotelero");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
