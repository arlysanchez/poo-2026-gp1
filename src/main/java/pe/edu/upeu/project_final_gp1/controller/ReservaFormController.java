package pe.edu.upeu.project_final_gp1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pe.edu.upeu.project_final_gp1.implementacion.ClienteDaoImpl;
import pe.edu.upeu.project_final_gp1.implementacion.HabitacionDaoImpl;
import pe.edu.upeu.project_final_gp1.implementacion.ReservaDaoImpl;
import pe.edu.upeu.project_final_gp1.interfaces.IClienteDao;
import pe.edu.upeu.project_final_gp1.interfaces.IHabitacionDao;
import pe.edu.upeu.project_final_gp1.interfaces.IReservaDao;
import pe.edu.upeu.project_final_gp1.model.Cliente;
import pe.edu.upeu.project_final_gp1.model.Habitacion;
import pe.edu.upeu.project_final_gp1.model.Reserva;
import pe.edu.upeu.project_final_gp1.model.UserSession;
import pe.edu.upeu.project_final_gp1.utils.AlertsUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservaFormController {
    private static AlertsUtils alertsUtils = new AlertsUtils();

    @FXML
    private TextField txtDniBusqueda, txtNumeroHab, txtTotal;
    @FXML
    private Label lblNombreCliente,lblDetalleHab;
    @FXML
    private DatePicker dpIngreso,dpSalida;

    private Cliente clienteEncontrado;
    private Habitacion habitacionEncontrada;
    private boolean guardado = false;
    private final IClienteDao clienteDao= new ClienteDaoImpl();
    private final IHabitacionDao habitacionDao = new HabitacionDaoImpl();
    private final IReservaDao reservaDao =new ReservaDaoImpl();
    @FXML
    private void initialize(){
        dpIngreso.valueProperty().addListener((
                ov,oldValue, newValue)->{
            calcularTotal();
        });
        dpSalida.valueProperty().addListener((
                ov,oldValue, newValue)->{
            calcularTotal();
        });

    }
    @FXML
    private void buscarCliente(){
        String dni = txtDniBusqueda.getText();
        if (dni.length()==8){
            clienteEncontrado = clienteDao.searchByDni(dni);
            if (clienteEncontrado!=null){
                lblNombreCliente.setText(clienteEncontrado.getNombres()+" "+
                        clienteEncontrado.getApellidos());
                lblNombreCliente.setStyle("-fx-text-fill: green");
            }else{
                lblNombreCliente.setText("No encontrado");
                lblNombreCliente.setStyle("-fx-text-fill: red");
            }
        }
    }
    @FXML
    private void buscarHabitacion(){
        String num = txtNumeroHab.getText();
        if (!num.isEmpty()){
            habitacionEncontrada = habitacionDao.searchByNumero(num);
            if (habitacionEncontrada!=null){
                lblDetalleHab.setText(habitacionEncontrada.getTipo()+" "+
                        habitacionEncontrada.getPrecio());
                lblDetalleHab.setStyle("-fx-text-fill: green");
                txtTotal.setText("0.00");
                calcularTotal();
            }else{
                lblDetalleHab.setText("No encontrado");
                lblDetalleHab.setStyle("-fx-text-fill: red");
            }
        }
      }
      private void calcularTotal(){
        if (habitacionEncontrada !=null && dpIngreso.getValue()!=null
         && dpSalida.getValue() !=null){
            LocalDate ingreso = dpIngreso.getValue();
            LocalDate salida = dpSalida.getValue();
            long noches = ChronoUnit.DAYS.between(ingreso,salida);
            if (noches>0){
                double precioNoche = habitacionEncontrada.getPrecio();
                double totalPrecio = noches * precioNoche;
                txtTotal.setText(String.format("%.2f",totalPrecio));
            }else{
                txtTotal.setText("0.00");
            }
        }
      }
      private void cerrar(){
          ((Stage) txtDniBusqueda.getScene().getWindow()).close();
      }
      @FXML
    private void handleCancelar(){
        cerrar();
      }
      public boolean isGuardado(){
        return guardado;
      }
      @FXML
     private void handleGuardar(){
        if (clienteEncontrado == null || habitacionEncontrada == null
        || dpIngreso.getValue() == null || dpSalida.getValue() == null
        ){
            Alert alert = alertsUtils.viewAlerts("Error",
                    "completar los campos","ERROR");
            alert.showAndWait();
            return;
        }
          Reserva r = new Reserva();
        r.setCliente(clienteEncontrado);
        r.setHabitacion(habitacionEncontrada);
        r.setFechaReserva(LocalDate.now());
        r.setFechaIngreso(dpIngreso.getValue());
        r.setFechaSalida(dpSalida.getValue());
        r.setEstado("R");
        r.setTotal(Double.parseDouble(txtTotal.getText()));
        r.setUsuario(UserSession.getInstance().getUserLogger());

        if (reservaDao.validatedRoom(r)){
            Alert alert = alertsUtils.viewAlerts("Habitacion ocupada",
                    "habitacion no esta disponible","WARNING");
            alert.showAndWait();
        }else{
            if (reservaDao.insert(r)){
                guardado= true;
                cerrar();
            }
        }
      }




}
