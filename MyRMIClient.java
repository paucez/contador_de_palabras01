
import java.io.File;
import java.rmi.Naming;
import java.util.Map;
import java.util.HashMap;
import javax.swing.SwingUtilities;



import javax.swing.JFileChooser;
/* 
public class MyRMIClient {
    // Contador estático para rastrear el número de llamadas al método remoto
    private static int counter = 0;
    //delacra el metodo aqui 
     
    public void mostrarTiempoProcesamiento(String tipo, long tiempo) {
        System.out.println("Estado: Procesamiento " + tipo + " completado en " + tiempo + " ms");
    }
   */ 
/* 
   public static void main(String[] args) {
    try {
        MyRemoteInterface remoteObject = (MyRemoteInterface) Naming.lookup("rmi://localhost/MyRemoteObject");

        int result = remoteObject.calculateSquare(5);
        counter++;

        // Crear una instancia de ContadorDeLetrasAppSwing
        ContadorDeLetrasAppSwing app = new ContadorDeLetrasAppSwing();

        // Crear un resultado ficticio para actualizar la GUI
        Map<Character, Integer> resultado = new HashMap<>();
        resultado.put('A', result); // Supongamos que 'A' es un ejemplo de resultado

        // Llamar al método actualizarResultado de ContadorDeLetrasAppSwing
        app.actualizarResultado(resultado);

        System.out.println("Result from server: " + result);
        System.out.println("The method calculateSquare was called " + counter + " times.");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
*/

  
/* 
public class MyRMIClient {

    public static void main(String[] args) {
        try {
            // Supongamos que esta es la llamada al método remoto que devuelve un resultado
            MyRemoteInterface remoteObject = (MyRemoteInterface) Naming.lookup("rmi://localhost/MyRemoteObject");
            Map<Character, Integer> resultado = remoteObject.countLetters("Some text to process");

            // Asegúrate de que la actualización de la GUI se ejecute en el EDT
            SwingUtilities.invokeLater(() -> {
                // Crear una instancia de ContadorDeLetrasAppSwing
                ContadorDeLetrasAppSwing app = new ContadorDeLetrasAppSwing();

                // Actualizar la GUI con el resultado obtenido del servidor RMI
                app.mostrarResultado(resultado);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
public class MyRMIClient {

    public static void main(String[] args) {
        try {
            MyRemoteInterface remoteObject = (MyRemoteInterface) Naming.lookup("rmi://192.168.1.18:1234/MyRemoteObject");

            


            // Iniciar la medición del tiempo antes de llamar al método remoto
            long startTime = System.currentTimeMillis();

            // Supongamos que esta es la llamada al método remoto que devuelve un resultado
            Map<Character, Integer> resultado = remoteObject.countLetters("Some text to process");

            // Finalizar la medición del tiempo después de recibir el resultado
            long tiempoEjecucion = System.currentTimeMillis() - startTime;

            // Asegúrate de que la actualización de la GUI se ejecute en el EDT
            SwingUtilities.invokeLater(() -> {
                // Crear una instancia de ContadorDeLetrasAppSwing
                ContadorDeLetrasAppSwing app = new ContadorDeLetrasAppSwing();

                // Actualizar la GUI con el resultado y el tiempo de ejecución obtenidos del servidor RMI
                app.mostrarResultado(resultado, tiempoEjecucion);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



