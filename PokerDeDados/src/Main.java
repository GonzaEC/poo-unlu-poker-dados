import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.vista.IVista;
import com.edu.unlu.generala.vista.InterfazConsola;

public class Main {
    public static void main(String[] args) {
        ControladorGenerala controlador = new ControladorGenerala();
        IVista consola = new InterfazConsola( controlador);
        controlador.setVista(consola);
        consola.iniciar();


    }
}