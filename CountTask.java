import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.HashMap;

public class CountTask extends RecursiveTask<Map<Character, Integer>> {
    private static final int THRESHOLD = 100; // Puedes ajustar este valor según tus necesidades y pruebas

    private final String filePath;
    private final int startLine;
    private final int endLine;

    //se crea un cosntrusctor  de la clse countask
    public CountTask(String filePath, int startLine, int endLine) {
        //se encarga de asignar los valores iniciales 
        this.filePath = filePath;//Esto indica que el archivo que se va a procesar está especificado por el parámetro
        this.startLine = startLine;//Esto indica la línea de inicio en la que se iniciará el procesamiento del archivo.
        this.endLine = endLine;//Esto indica la línea final hasta la cual se realizará el procesamiento del archivo.
    }

    @Override
    protected Map<Character, Integer> compute() {
        if (endLine - startLine <= THRESHOLD) { // Define una constante THRESHOLD para tareas pequeñas
            return countLettersInLines(filePath, startLine, endLine);
        } else {
            int mid = startLine + (endLine - startLine) / 2;
            CountTask leftTask = new CountTask(filePath, startLine, mid);
            CountTask rightTask = new CountTask(filePath, mid, endLine);

            leftTask.fork(); // Ejecuta la tarea izquierda de manera asíncrona
            Map<Character, Integer> rightResult = rightTask.compute(); // Ejecuta la tarea derecha y espera su resultado
            Map<Character, Integer> leftResult = leftTask.join(); // Espera a que termine la tarea izquierda

            return mergeResults(leftResult, rightResult); // Combina los resultados de ambas tareas
        }
    }

    private Map<Character, Integer> countLettersInLines(String filePath, int startLine, int endLine) {
        Map<Character, Integer> letterCounts = new HashMap<>();
        // Implementa la lógica de conteo aquí, leyendo las líneas del archivo desde startLine hasta endLine
        // ...
        return letterCounts;
    }

    private Map<Character, Integer> mergeResults(Map<Character, Integer> a, Map<Character, Integer> b) {
        // Implementa la lógica para combinar dos mapas de conteo aquí
        // ...
        return a; // Retorna el mapa combinado
    }
}
