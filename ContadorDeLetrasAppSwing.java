import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ContadorDeLetrasAppSwing {
    private JFrame frame;
    private JLabel statusLabel;
    private JTextArea resultTextArea;
    private JButton sequentialButton;
    private JButton parallelButton;
    private JButton concurrentButton; // Botón para procesamiento concurrente

    public void mostrarResultado(Map<Character, Integer> resultado, long tiempoEjecucion) {
        StringBuilder sb = new StringBuilder();
        resultado.forEach((character, integer) -> sb.append(character).append(": ").append(integer).append("\n"));
        resultTextArea.setText(sb.toString());
        statusLabel.setText("Procesamiento completado en " + tiempoEjecucion + " ms");
    }

    public ContadorDeLetrasAppSwing() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Contador de Letras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        statusLabel = new JLabel("Estado:");
        resultTextArea = new JTextArea(10, 40);
        resultTextArea.setEditable(false);

        sequentialButton = new JButton("Procesamiento Secuencial");
        parallelButton = new JButton("Procesamiento Paralelo");
        concurrentButton = new JButton("Procesamiento Concurrente");

        sequentialButton.addActionListener(this::accionProcesamientoSecuencial);
        parallelButton.addActionListener(this::accionProcesamientoParalelo);
        concurrentButton.addActionListener(this::accionProcesamientoConcurrente);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sequentialButton);
        buttonPanel.add(parallelButton);
        buttonPanel.add(concurrentButton);

        frame.add(statusLabel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultTextArea), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private void accionProcesamientoSecuencial(ActionEvent e) {
        procesarArchivo(false);
    }

    private void accionProcesamientoParalelo(ActionEvent e) {
        procesarArchivo(true);
    }

    private void accionProcesamientoConcurrente(ActionEvent e) {
        procesarArchivoConcurrentemente();
    }
    private void procesarArchivo(boolean paralelo) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            statusLabel.setText("Procesando...");
            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                Map<Character, Integer> resultado;
                if (paralelo) {
                    resultado = contarLetrasParalelo(file); // Implementa este método para el procesamiento paralelo
                } else {
                    resultado = contarLetrasSecuencial(file); // Este ya debería estar implementado
                    
                }
                long endTime = System.currentTimeMillis();
                long tiempoEjecucion = endTime - startTime; // Declaración de la variable tiempoEjecucion
                SwingUtilities.invokeLater(() -> mostrarResultado(resultado, tiempoEjecucion)); // Pasamos tiempoEjecucion aquí
            }).start();
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

    private Map<Character, Integer> contarLetrasParalelo(File file) {
        ForkJoinPool pool = new ForkJoinPool();
        // Aquí deberías implementar tu propia tarea ForkJoin que procese el archivo
        CountTask task = new CountTask(file.getPath(), 0, getTotalNumberOfLines(file)); // Debes implementar getTotalNumberOfLines
        return contarLetrasSecuencial(file); // Reemplaza esto con la implementación real
            }

    private int getTotalNumberOfLines(File file) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) lines++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

   // private void procesarArchivoConcurrentemente() {
        // Aquí implementarías la lógica para procesar el archivo concurrentemente
   // }
   private Map<Character, Integer> contarLetrasConcurrentemente(File file) {
    // Dividir el archivo y procesar las partes concurrentemente
    // ...
    return new HashMap<>(); // Devuelve el resultado combinado
}
/* */
//este metodo se encaraga de procesar  el archivo de manera concurrente 
   private void procesarArchivoConcurrentemente() {
    //se encarag de abriel el cuadro de dialogo para subir el archivo
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showOpenDialog(frame);
    if (result == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        statusLabel.setText("Procesando concurrentemente...");

        // Crea un pool de hilos para ejecutar tareas concurrentemente
        ExecutorService executor = Executors.newCachedThreadPool();

        // Obtiene el tiempo de inicio de la ejecución
        long startTime = System.currentTimeMillis();

        // Suponiendo que tienes una forma de dividir el trabajo y contar las letras de forma concurrente
        // Puede que necesites implementar esta parte
        //el signo ()-> indica que es para la interfaz de dar prioridad pero no manda ningún argumento 
        Runnable task = () -> {
            Map<Character, Integer> resultado = contarLetrasConcurrentemente(file); // Implementa este método
            long tiempoEjecucion = System.currentTimeMillis() - startTime;
            
            // Actualizamos la GUI en el EDT
            SwingUtilities.invokeLater(() -> {
                mostrarResultado(resultado, tiempoEjecucion);
                statusLabel.setText("Procesamiento concurrente completado en " + tiempoEjecucion + " ms.");
            });
        };

        //el pool se encarga de gestionar los hilos y reutilizarlos todo esto en segundo plano 
        //Después de que una tarea se completa, el hilo que la ejecutó no se cierra inmediatamente.
        // Envía la tarea al pool de hilos para su ejecución
        executor.submit(task);// El pool decidirá cuándo y en qué hilo se ejecutará la tarea
     // Apaga el pool de hilos después de que se complete la tarea
        executor.shutdown();//Esto significa que el pool de hilos dejará de aceptar nuevas tareas 
        //y esperará a que todas las tareas en curso se completen. Luego, los hilos se apagarán de manera ordenada.
    }
}
/* */
   

   

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContadorDeLetrasAppSwing::new);
    }
}
