import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static Ui ui;

    // filoutputstream: ecrire dans un fichier
    // fileinputstream: lire un fichier
    public static Fichier assembler(List<Fichier> fichiers) throws IOException {
        if (fichiers.get(0) == null) {
            return null;
        }

        byte[] data = new byte[fichiers.size() * fichiers.get(0).getData().length];
        int i = 0;
        for (Fichier fichier : fichiers) {
            for (int j = 0; j < fichier.getData().length; j++) {
                data[i] = fichier.getData()[j];
                i++;
            }
        }
        return new Fichier(fichiers.get(0).getParentname(), fichiers.get(0).getParentname(), data);

    }

    public static List<Fichier> partition(Fichier theFile, int nombredetranche) throws IOException {
        List<Fichier> filesTrancher = new ArrayList<>();
        int totalSize = theFile.getData().length;
        int partitionSize = totalSize / nombredetranche;

        int remainingBytes = totalSize % nombredetranche;
        System.out.println("Total size " + totalSize);
        System.out.println("Partition size " + partitionSize);
        System.out.println("Reste " + partitionSize);
        int offset = 0;
        for (int i = 0; i < nombredetranche; i++) {
            int currentPartitionSize = partitionSize + (i < remainingBytes ? 1 : 0);
            byte[] buffer = Arrays.copyOfRange(theFile.getData(), offset, offset + currentPartitionSize);
            filesTrancher.add(new Fichier("part" + (i + 1) + "_" + theFile.getNom(), theFile.getNom(), buffer));
            offset += currentPartitionSize;
        }

        return filesTrancher;
    }

    public static void inputGestion(String ip, String port, String input) {
        Ecouteur.actionHandler(ip, port, input);
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> Map = Reader.getValue(Server.class.getName());
        String port = Map.get("localport").toString();
        Scanner Scanner = new Scanner(System.in);
        String ip = Map.get("ip").toString();

        String action;
        while (true) {
            try {
                System.out.println("Quelle est l'action a performer");
                System.out.println("do/up/del/list/exit");
                action = Scanner.nextLine();
                if (action.equals("exit")) {
                    Scanner.close();
                    break;
                }
                inputGestion(ip, port, action);
            } catch (java.util.InputMismatchException e) {
                Ecouteur.afficherAlerte(e.getMessage());
            }
        }
    }
}
