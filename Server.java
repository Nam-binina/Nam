import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Server {
    public static int localport;// = 1902;
    public static ServerSocket serveur;
    public static List<Socket> clients = new ArrayList<>();
   

    public static void askToDel(Object demande) throws Exception {
        List<String> sbDispo = new ArrayList<>(Fonction.getSbDispo());
        if (sbDispo.size() != 3) {
            System.out.println("Erreur-delete:Tout les slaves ne sont pas prets...");
            throw new Exception("Erreur:le serveur ne peut pas demander la suppression du fichier");
        }
        Delete del = (Delete) demande;
        for (int i = 0; i < sbDispo.size(); i++) {
            Map<String, Object> sbMap = Reader.getValue(sbDispo.get(i));

            Socket tempSb = new Socket(sbMap.get("ip").toString(), Integer.parseInt(sbMap.get("localport").toString()));
            Fonction.sendObjectToServer(tempSb, del);
            tempSb.close();
        }
    }

    public static void getFromClientAndShare(Socket client, Object demande) throws Exception {
        System.out.println("Client connecté: " + client.getInetAddress());
        Fichier fichier = (Fichier) demande;

        // list subserveur disopo->
        List<String> sbDispo = new ArrayList<>(Fonction.getSbDispo());
        if (sbDispo.size() != 3) {
            System.out.println("Erreur-upload:Tout les slaves ne sont pas prets...");
            throw new Exception("Erreur:le serveur ne peut pas telecharger le fichier");
        }
        List<Fichier> segFichier = Main.partition(fichier, sbDispo.size());
        for (int i = 0; i < segFichier.size(); i++) {
            Map<String, Object> sbMap = Reader.getValue(sbDispo.get(i));
            Socket tempSb = new Socket(sbMap.get("ip").toString(), Integer.parseInt(sbMap.get("localport").toString()));
            Fonction.sendObjectToServer(tempSb, segFichier.get(i));
            tempSb.close();
        }
    }

    public static void askServerAndDownload(Socket client, Object demande  , ServerSocket receivePartitionBySb)
            throws IOException, ClassNotFoundException, InterruptedException {

        Partition partition = null;
        
        Object fromClient = demande;

        List<String> sbDispo = new ArrayList<>(Fonction.getSbDispo());
        if (sbDispo.size() != 3) {
            System.out.println("Erreur-download:Tout les slaves ne sont pas prets...");
            Fichier fichiervide = new Fichier("null", null, null);
            Socket download = new Socket(client.getInetAddress().getHostAddress(), 2025);
            Fonction.sendObjectToServer(download, fichiervide);
            download.close();
        } else {
            if (fromClient.getClass().equals(String.class)) {
                String filedoDown = (String) fromClient;
                partition = new Partition(filedoDown);
                Socket Demandetelechargement = new Socket(Reader.getValue("Sb1").get("ip").toString(),
                        Integer.parseInt(Reader.getValue("Sb1").get("localport").toString()));
                Fonction.sendObjectToServer(Demandetelechargement, partition);
                Socket sb = receivePartitionBySb.accept();
                new Thread(()->{
                    try {
                        Object   fromSb =  Fonction.getObjectFromClient(sb);
                      Partition part = (Partition) fromSb;
                        Fichier fichierAssembler = Main.assembler(part.getFichiers());
                        if (fichierAssembler == null) {
                            fichierAssembler = new Fichier("null", null, null);
                        } else {
                            fichierAssembler.setNom(part.getNom());
                        }
                        try {
                            Socket download = new Socket(client.getInetAddress().getHostAddress(), 2025);
                            Fonction.sendObjectToServer(download, fichierAssembler);
                            download.close();
                        } catch (Exception e) {
                            System.out.println(e.getMessage() + " "  + client.getInetAddress().getHostAddress()); 
                        }
                    } catch (Exception e) {
                        
                    }
                    
                }).start();
                Demandetelechargement.close();
            }
        }

    }

    public static void main(String[] args) throws Exception {

        Map<String, Object> Map = Reader.getValue(Server.class.getName());
        localport = Integer.parseInt(Map.get("localport").toString());
        
        try (ServerSocket server = new ServerSocket(localport)) {
            ServerSocket receivePartitionBySb = new ServerSocket(1999);

            System.out.println("Server actif sur port: " + localport);
            while (true) {
                Socket client = server.accept();
                // clients = new ArrayList<>();
                clients.add(client);
                // System.out.println(clients);
                // System.out.println("Client connecté: " +
                // client.getInetAddress().getHostAddress());
                // Object demande = Fonction.getObjectFromClient(client);
                // if (demande != null) {
                // if (demande.getClass().equals(Fichier.class)) {
                // try {
                // getFromClientAndShare(client, demande);
                // } catch (Exception e) {
                // Ecouteur.afficherAlerte(e.getMessage());
                // }
                // } else if (demande.getClass().equals(String.class) ||
                // demande.getClass().equals(Partition.class)) {
                // askServerAndDownload(client, demande);
                // }
                // else if (demande.getClass().equals(Delete.class) ) {
                // // askServerAndDownload(client, demande);
                // askToDel(demande);
                // // System.out.println("Delete");
                // }

                // }
                new ClientHandler(client , receivePartitionBySb).start();

                // client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private ServerSocket receivePartitionBySb;


    public ClientHandler(Socket socket  , ServerSocket receivePartitionBySb) {
        this.clientSocket = socket;
        this.receivePartitionBySb = receivePartitionBySb;
    }

    @Override
    public void run() {
        try {
            Object demande = Fonction.getObjectFromClient(clientSocket);
            System.out.println(demande);

            if (demande != null) {
                if (demande.getClass().equals(Fichier.class)) {
                    try {
                        Server.getFromClientAndShare(clientSocket, demande);
                    } catch (Exception e) {
                        Ecouteur.afficherAlerte(e.getMessage());
                    }
                } else if (demande.getClass().equals(String.class)) {
                    Server.askServerAndDownload(clientSocket, demande , receivePartitionBySb);
                } 
                
                else if (demande.getClass().equals(Delete.class)) {
                    // askServerAndDownload(client, demande);
                    Server.askToDel(demande);
                    // System.out.println("Delete");
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
