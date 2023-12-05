import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;


public interface MyRemoteInterface extends Remote {
    int calculateSquare(int number) throws RemoteException;
    Map<Character, Integer> countLetters(String textFragment) throws RemoteException;// toma compo argumento el texto ingresado
    //

}

