import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JFileChooser;
import java.util.List;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;







public class MyRemoteObject extends UnicastRemoteObject implements MyRemoteInterface {
    // Necesitas definir la variable frame si la estás utilizando desde otra clase.
    // JFrame frame;
     private final Map<Character, Integer> globalCount = new ConcurrentHashMap<>();
    protected MyRemoteObject() throws RemoteException {
        super();
    }

    public void mostrarTiempoProcesamiento(String tipo, long tiempo) {
        System.out.println("Estado: Procesamiento " + tipo + " completado en " + tiempo + " ms");
    }
    
    @Override
    public int calculateSquare(int number) throws RemoteException {
        return number * number;
    }
    // Este método sería llamado por los clientes para procesar su fragmento.
        // Implementa el método de procesamiento paralelo
//el método countLetters es un método remoto que puede ser invocado por clientes de manera remota. 
//La lógica dentro de este método se ejecutará en la máquina donde se encuentra el objeto remoto
    @Override
    public Map<Character, Integer> countLetters(String textFragment) throws RemoteException {
        Map<Character, Integer> localCount = new HashMap<>();
    
        // Contar las letras del fragmento de texto.
        for (char c : textFragment.toCharArray()) {
            if (Character.isLetter(c)) {
                c = Character.toLowerCase(c);
                localCount.put(c, localCount.getOrDefault(c, 0) + 1);
            }
        }

    
        // Combina el conteo local con el global de forma segura.
        updateGlobalCount(localCount);
        
        return localCount;
    }
    
    // Método sincronizado para actualizar el conteo global.
    //La sincronización en este método asegura que la actualización del conteo global (globalCount) 
    //se realice de manera segura,
     //especialmente si varias llamadas remotas están ocurriendo simultáneamente.
    private synchronized void updateGlobalCount(Map<Character, Integer> localCount) {
        localCount.forEach((key, value) -> globalCount.merge(key, value, Integer::sum));
    }
    
    //est es el metodo original
    
    // Declaración del método contarLetrasConcurrente.
    private Map<Character, Integer> contarLetrasConcurrente(File file) {
        // Implementa la lógica de procesamiento concurrente aquí si es necesario.
        // Puedes utilizar hilos si es necesario.
        // Aquí se muestra una versión secuencial como ejemplo.
        return contarLetrasSecuencial(file);
        
    }
     
   
    
    

    private void procesarArchivoSecuencialmente() {
        JFileChooser fileChooser = new JFileChooser();
        // Necesitas definir la variable frame si la estás utilizando desde otra clase.
        // int result = fileChooser.showOpenDialog(frame);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            long startTime = System.currentTimeMillis();
            Map<Character, Integer> resultado = contarLetrasSecuencial(file);
            long endTime = System.currentTimeMillis();
            // Necesitas definir la variable frame si la estás utilizando desde otra clase.
            // mostrarResultado(resultado);
            // mostrarTiempoProcesamiento("Secuencial", endTime - startTime);
        }
    }

    private void procesarArchivoConcurrentemente() {
        JFileChooser fileChooser = new JFileChooser();
        // Necesitas definir la variable frame si la estás utilizando desde otra clase.
        // int result = fileChooser.showOpenDialog(frame);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            long startTime = System.currentTimeMillis();
            // Llamada al método contarLetrasConcurrente.
            Map<Character, Integer> resultado = contarLetrasConcurrente(file);
            long endTime = System.currentTimeMillis();
            // Necesitas definir la variable frame si la estás utilizando desde otra clase.
            // mostrarResultado(resultado);
            // mostrarTiempoProcesamiento("Concurrente", endTime - startTime);
        }
    }

    private Map<Character, Integer> contarLetrasSecuencial(File file) {
        Map<Character, Integer> contador = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                for (char c : linea.toCharArray()) {
                    if (Character.isLetter(c)) {
                        c = Character.toLowerCase(c);
                        contador.put(c, contador.getOrDefault(c, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contador;
    }
}

    // Otros métodos relacionados con RMI pueden ir aquí.


