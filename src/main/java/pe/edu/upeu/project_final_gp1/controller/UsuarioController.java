package pe.edu.upeu.project_final_gp1.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pe.edu.upeu.project_final_gp1.implementacion.UsuarioDaoImpl;
import pe.edu.upeu.project_final_gp1.interfaces.IUsuarioDao;
import pe.edu.upeu.project_final_gp1.model.UserSession;
import pe.edu.upeu.project_final_gp1.model.Usuario;
import pe.edu.upeu.project_final_gp1.utils.AlertsUtils;

import java.io.IOException;

public class UsuarioController {
    private static AlertsUtils alertsUtils = new AlertsUtils();
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPassword;

    private final IUsuarioDao usuarioDao = new UsuarioDaoImpl();

    @FXML
    private void handleLogin(){
        String user = txtUser.getText();
        String pass = txtPassword.getText();
        if (user.isEmpty() || pass.isEmpty()){
            Alert alert = alertsUtils.viewAlerts("Campos Vacios",
                    "ingrese user y password","WARNING");
            alert.showAndWait();
            return;
        }
        Usuario u = usuarioDao.login(user,pass);
        if (u !=null){
            Alert alert = alertsUtils.viewAlerts("Acceso concedido",
                    "Bienvenido: "+u.getNombres(),"INFORMATION");
            alert.showAndWait();
            UserSession.getInstance().setUserLogger(u);
            navigateToHome();
        }else{
            Alert alert = alertsUtils.viewAlerts("Acceso denegado",
                    "Usuario y password incorrectos","ERROR");
            alert.showAndWait();
        }

    }

    private void navigateToHome(){
        try {
            FXMLLoader loader = new FXMLLoader
                    (getClass().getResource("/pe/edu/upeu/project_final_gp1/views/main-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtUser.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Sistema Hotelero");
            stage.show();
        }catch (IOException e){
            System.err.println("Error al cargar el home"+e.getMessage());
            e.printStackTrace();
        }

    }


}
