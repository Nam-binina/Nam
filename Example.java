import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.IOException;

public class Example {
    public static void main(String[] args) {
        try {
            // Création d'un ServerSocket qui écoute sur le port 8080
            ServerSocket serverSocket = new ServerSocket(8080);

            // Récupération de l'adresse IP et du port du serveur
            InetAddress inetAddress = serverSocket.getInetAddress();
            int port = serverSocket.getLocalPort();

            // Affichage de l'adresse IP et du port
            System.out.println("Adresse IP du serveur : " + inetAddress.getHostAddress());
            System.out.println("Port du serveur : " + port);

            // Fermer le ServerSocket
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
