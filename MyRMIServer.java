import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class MyRMIServer {
    public static void main(String[] args) {
        try {
           // Establece la propiedad del sistema con la dirección IP real del servidor
System.setProperty("java.rmi.server.hostname", "192.168.1.18");

MyRemoteInterface remoteObject = new MyRemoteObject();

// Iniciar el registro RMI en el puerto 1234
LocateRegistry.createRegistry(1234);

// Registrar el objeto remoto con un nombre
Naming.rebind("rmi://192.168.1.18:1234/MyRemoteObject", remoteObject);

System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

