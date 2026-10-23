package pe.edu.upeu.project_final_gp1.test;

import pe.edu.upeu.project_final_gp1.implementacion.ClienteDaoImpl;
import pe.edu.upeu.project_final_gp1.interfaces.IClienteDao;
import pe.edu.upeu.project_final_gp1.model.Cliente;

import java.util.List;

public class TestCliente {
    static IClienteDao cl = new ClienteDaoImpl();

    public void insertar(){
        Cliente c = new Cliente();
        c.setNombres("Juan1");
        c.setApellidos("Perez1");
        c.setDni("987654321");
        c.setEmail("juan1@gmail.com");
        c.setGenero("M");
        boolean result = cl.insert(c);
        if (result){
            System.out.println("Success");
        }else{
            System.out.println("Error");
        }
    }
    public void update(){
        Cliente c = new Cliente();
        c.setNombres("jhan Arly act");
        c.setApellidos("Sanchez act");
        c.setDni("73472336");
        c.setEmail("jarly_act@gmail.com");
        c.setGenero("M");
        c.setIdcliente(1);
        boolean result = cl.update(c);
        if (result){
            System.out.println("Success update");
        }else{
            System.out.println("Error");
        }
    }
    public void delete(){
        Cliente c = new Cliente();
        c.setIdcliente(3);
        boolean result = cl.delete(c);
        if (result){
            System.out.println("Success delete");
        }else{
            System.out.println("Error delete");
        }
    }

    public void listarCliente() {
        List<Cliente> list = cl.listAll();
        if (list !=null && !list.isEmpty()){
            for (Cliente c : list){
                System.out.println("ID_CLIENTE:"+c.getIdcliente());
                System.out.println("NOMBRE:"+c.getNombres());
                System.out.println("APELLIDOS:"+c.getApellidos());
                System.out.println("DNI:"+c.getDni());
                System.out.println("EMAIL:"+c.getEmail());
                System.out.println("GENERO:"+c.getGenero());
            }
        }else{
            System.out.println("no hay registros");
        }
    }

    public void searchById(){
        Cliente c = cl.searchById(1);
        if (c !=null){
            System.out.println("ID_CLIENTE:"+c.getIdcliente());
            System.out.println("NOMBRE:"+c.getNombres());
            System.out.println("APELLIDOS:"+c.getApellidos());
            System.out.println("DNI:"+c.getDni());
            System.out.println("EMAIL:"+c.getEmail());
            System.out.println("GENERO:"+c.getGenero());
        }else{
            System.out.println("no hay registros");
        }
    }
    public void searchByDni(){
        Cliente c = cl.searchByDni("73472336");
        if (c !=null){
            System.out.println("ID_CLIENTE:"+c.getIdcliente());
            System.out.println("NOMBRE:"+c.getNombres());
            System.out.println("APELLIDOS:"+c.getApellidos());
            System.out.println("DNI:"+c.getDni());
            System.out.println("EMAIL:"+c.getEmail());
            System.out.println("GENERO:"+c.getGenero());
        }else{
            System.out.println("no hay registros");
        }
    }


    public static void main(String[] args) {
        TestCliente t = new TestCliente();
        System.out.println("*********");
       // t.listarCliente();
        //t.searchById();
        t.searchByDni();
       // t.update();
      //  t.listarCliente();
    }

}
