import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Sb2 {
    public static int localport;// = 1724;
    public static String directory;// = "sb2_server/";
    public static void delFile (Object demande) throws Exception
    {
        Delete del = (Delete) demande;
        String filename = del.getFilename();
        Fonction.deleteFile(filename, directory);
    }
    public static void receiveAndSave(Socket client, Object demande) throws IOException, ClassNotFoundException {
        Fichier fichier = (Fichier) demande;
        System.out.println(fichier.getNom());
        Fonction.ecrireFichier(fichier, directory);
    }

    public static void AddAndgivePartition(Socket client, int portToGive,  String ip  ,  Object demande)
            throws ClassNotFoundException, IOException {
        Partition partition = (Partition) demande;
        Fichier fichier = Fonction.searchFile(partition.getNom(), directory);
        System.out.println(fichier);
        System.out.println(partition.getNom());
        partition.addFichiers(fichier);

        Socket Demandetelechargement = new Socket(ip, portToGive);
        Fonction.sendObjectToServer(Demandetelechargement, partition);
        Demandetelechargement.close();
    }
    public static void main(String[] args) throws Exception {

        Map<String, Object> Map = Reader.getValue(Sb2.class.getName());
        localport = Integer.parseInt(Map.get("localport").toString());
        directory = Map.get("directory").toString();

        try (ServerSocket server = new ServerSocket(localport)) {
            System.out.println("Server actif sur port: " + localport);
            while (true) {
                Socket client = server.accept();
                System.out.println("Client connecté: " + client.getInetAddress());
                Object demande = Fonction.getObjectFromClient(client);

                // sauvegarder la partition
                if (demande != null) {
                    if (demande.getClass().equals(Fichier.class)) {
                        receiveAndSave(client, demande);
                    }
                      // ajouter et donner la partition a Sb3
                      else if (demande.getClass().equals(Partition.class)) {
                        AddAndgivePartition(client,  Integer.parseInt( Reader.getValue("Sb3").get("localport").toString() )  , Reader.getValue("Sb3").get("ip").toString()  , demande);
                    }
                    else if (demande.getClass().equals(Delete.class)) {
                        System.out.println(demande.getClass().getSimpleName());
                        delFile(demande);
                    } else if (demande.getClass().equals(Liste.class)) {
                        Liste liste = new Liste(Fonction.afficheListe(directory));

                        Thread.sleep(5000);
                        try (Socket socket = new Socket(client.getInetAddress().getHostAddress(), 4456)) {
                            Fonction.sendObjectToServer(socket, liste);
                            socket.close();
                        } catch (Exception e) {
                            System.out.println(e.getMessage() + " " + client.getInetAddress().getHostAddress());
                        }

                }
                }
               
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
