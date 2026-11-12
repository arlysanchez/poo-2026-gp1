package pe.edu.upeu.project_final_gp1.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pe.edu.upeu.project_final_gp1.implementacion.ReservaDaoImpl;
import pe.edu.upeu.project_final_gp1.interfaces.IReservaDao;
import pe.edu.upeu.project_final_gp1.model.Reserva;
import pe.edu.upeu.project_final_gp1.utils.AlertsUtils;

import java.time.LocalDate;
import java.util.Optional;

public class ReservaController {
    private static AlertsUtils alertsUtils = new AlertsUtils();
    @FXML
    private TableView<Reserva> tableReserva;
    @FXML
    private TableColumn<Reserva,Number> ColNumero;
    @FXML
    private TableColumn<Reserva,Integer> ColId;
    @FXML
    private TableColumn<Reserva,String> ColCliente;
    @FXML
    private TableColumn<Reserva,String> ColHabitacion;
    @FXML
    private TableColumn<Reserva, LocalDate> ColIngreso;
    @FXML
    private TableColumn<Reserva,LocalDate> ColSalida;
    @FXML
    private TableColumn<Reserva,Double> ColTotal;
    @FXML
    private TableColumn<Reserva,String> ColEstado;
    private final IReservaDao reservaDao = new ReservaDaoImpl();
    private ObservableList<Reserva> listaReserva;

    @FXML
    private void initialize(){
        ColNumero.setCellFactory(column-> new TableCell<>(){
            @Override
            protected void updateItem(Number item,boolean empty){
                super.updateItem(item,empty);
                if (empty){
                    setText(null);
                }else{
                    setText(String.valueOf(getIndex()+1));
                }
            }
        });
        ColId.setCellValueFactory(
                cd-> cd.getValue().idreservaProperty().asObject());
        ColCliente.setCellValueFactory(
                cd-> cd.getValue().getCliente().nombresProperty());
        ColHabitacion.setCellValueFactory(
                cd-> cd.getValue().getHabitacion().numeroProperty());
        ColIngreso.setCellValueFactory(
                cd-> cd.getValue().fechaIngresoProperty());
        ColSalida.setCellValueFactory(
                cd-> cd.getValue().fechaSalidaProperty());
        ColTotal.setCellValueFactory(
                cd-> cd.getValue().totalProperty().asObject());
        ColEstado.setCellValueFactory(
                cd-> {
                    String estadoBD = cd.getValue().getEstado();
                    String estadoText = switch (estadoBD){
                        case "R" -> "Reservada";
                        case "O" -> "Ocupada";
                        case "T" -> "Terminada";
                        case "C" -> "Cancelada";
                        default -> "Desconocido";
                    };
                    return new SimpleStringProperty(estadoText);
                });
        ColEstado.setCellFactory(
                cl-> new TableCell<Reserva,String>(){
                    @Override
                    protected void updateItem(String estado, boolean empty){
                        super.updateItem(estado,empty);
                        if (empty || estado == null){
                            setText(null);
                            setStyle("");
                        }else{
                            setText(estado);
                            switch (estado){
                                case "Reservada" ->setStyle("-fx-text-fill: #1B5E20; -fx-font-weight: bold; ");
                                case "Ocupada" ->setStyle("-fx-text-fill: #B71C1C; -fx-font-weight: bold; ");
                                case "Terminada" ->setStyle("-fx-text-fill: #0D47A1; -fx-font-weight: bold; ");
                                case "Cancelada" ->setStyle("-fx-text-fill: #d2691e; -fx-font-weight: bold; ");
                                default -> setStyle("");
                            }
                        }
                    }
                }
        );

      cargarDatos();
    }
    private void cargarDatos(){
        listaReserva = FXCollections.observableArrayList(reservaDao.listAll());
        tableReserva.setItems(listaReserva);
    }

    @FXML
    private void handleCheckIn(){
        updateStatus("O","Confirmar CheckIn",
                "Desea marcar la habitacion como ocupada?");
    }
    @FXML
    private void handleCheckOut(){
        updateStatus("T","Confirmar CheckOut",
                "Desea finalizar la reserva?");
    }
    @FXML
    private void handleCancel(){
        updateStatus("C","Confirmar Cancelacion",
                "Esta seguro de CANCELAR la reserva?");
    }

    private void updateStatus(String nuevoEstado, String titulo,String mensaje){
        Reserva select = tableReserva.getSelectionModel().getSelectedItem();
        if (select==null){
            Alert alert = alertsUtils.viewAlerts("Seleccion requerida",
                    "Seleccione una reserva","WARNING");
            alert.showAndWait();
            return;
        }
        Alert confirm = alertsUtils.viewAlerts(titulo,mensaje,"CONFIRMATION");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            select.setEstado(nuevoEstado);
            if (reservaDao.updateStatus(select)){
                cargarDatos();
            }
        }
    }
}
