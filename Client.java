import java.io.IOException;
import java.net.*;

public class Client extends Socket {
    public static Socket client ;
    public  static String ip;
    public  static int port;

    public static void upload(Socket client, String path) throws IOException {
        Fonction.sendFileToServer(client, path);

    }

    public static void download(Socket client, String fileToDown) throws IOException, ClassNotFoundException {
        Fonction.sendObjectToServer(client, fileToDown);
        Thread thread = new Thread(()->{
            try {
                ServerSocket socket = new ServerSocket(2025);
                Socket c1 = socket.accept();
                Fichier file = (Fichier) Fonction.getObjectFromClient(c1);
                Fonction.ecrireFichier(file, "download/");
                c1.close();
                socket.close();
            } catch (Exception e) {
            }
           
        });
        thread.start();
    }

    public static void main(String[] args) {
        try (Socket client = new Socket(ip, port)) {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
