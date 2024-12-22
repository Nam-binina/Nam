import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

public class Fonction {

    public static List<String> afficheListe(String repository) {
        File repositor = new File(repository);
        List<String> resp = new ArrayList<>();
        for (String string : repositor.list()) {
            string = string.replace("part1_", "");
            resp.add(string);
        }
        return resp;
    }

    public static void deleteFile(String filename, String repository) {
        File repositor = new File(repository);
        if (repositor.exists()) {
            for (String string : repositor.list()) {
                if (string.contains(filename)) {
                    System.out.println(string);
                    File theFile = new File(repositor + "/" + string);
                    theFile.delete();
                }
            }
        }
    }

    public static List<String> getSbDispo() {
        Reader reader = new Reader();
        String sub = "Sb";
        String[] conf = Reader.StringBuilder.toString().split("\n");
        List<String> sbDispo = new ArrayList<>();
        for (int i = 0; i < conf.length; i++) {
            if (conf[i].contains(sub)) {

                conf[i] = conf[i].replace("]", "");
                conf[i] = conf[i].replace("[", "");
                Map<String, Object> sbmap = Reader.getValue(conf[i]);

                String sbip = sbmap.get("ip").toString();
                int sbport = Integer.parseInt(sbmap.get("localport").toString());
                try  {
                    Socket clienttest = new Socket();
                    InetSocketAddress address = new InetSocketAddress(sbip, sbport);
                    clienttest.connect(  address , 1000);
                    clienttest.close();
                    sbDispo.add(conf[i]);
                } catch (Exception e) {
                }
            }
        }
        return sbDispo;
    }
    public static byte[] getData(File theFile) throws IOException {
        FileInputStream f = new FileInputStream(theFile);
        long tranche = Files.size(theFile.toPath());
        byte[] buffer = new byte[(int) (tranche)];
        byte[] resp = new byte[buffer.length];
        int byteData;
        while ((byteData = f.read(buffer)) != -1) {
            resp = Arrays.copyOf(buffer, byteData);
        }
        return resp;
    }

    public static Fichier searchFile(String filename, String directory) {
        try {
            for (int i = 0; i < 3; i++) {
                String part = "part" + (i + 1) + "_" + filename;
                File file = new File(directory + File.separator + part);

                if (file.exists()) {
                    byte[] data = Fonction.getData(file);
                    return new Fichier(file.getName(), file.getName(), data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("File not found: " + filename + " in directory: " + directory);
        return null;
    }

    public static void ecrireFichier(Fichier fichier, String path) throws IOException {
        File resp = new File(path + fichier.getNom());
        resp.createNewFile();
        FileOutputStream ecrire = new FileOutputStream(resp);
        ecrire.write(fichier.getData());
        ecrire.close();
    }

    public static void sendObjectToServer(Socket client, Object object) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        objectOutputStream.writeObject(object);
        System.out.println("Fichier envoyer " + object.toString());
    }

    public static void sendObjectToClient(Socket client, Object object) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        objectOutputStream.writeObject(object);
        System.out.println("Object sent to client: " + object.toString());
    }

    public static void sendFileToServer(Socket client, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Le fichier n'existe pas.");
            return;
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        byte[] data = getData(file);
        Fichier fichier = new Fichier(file.getName(), file.getName(), data);
        objectOutputStream.writeObject(fichier);
        System.out.println("Fichier envoyer " + fichier.getNom());
    }

    public static Object getObjectFromClient(Socket inclient) throws IOException, ClassNotFoundException {
        try (ObjectInputStream lecture = new ObjectInputStream(inclient.getInputStream())) {
            return lecture.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getObjectFromServer(Socket inserver) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inserver.getInputStream());
        Object receivedObject = objectInputStream.readObject();
        System.out.println("Object received from server: " + receivedObject.toString());
        return receivedObject;
    }

    

   
}
