import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Reader {
    FileReader FileReader;
    static String str;
    static StringBuilder StringBuilder;

    public Reader() {
        try (FileReader FileReader = new FileReader("conf.txt")) {
            str = FileReader.toString();
            int i = FileReader.read();
            StringBuilder = new StringBuilder();
            while (i != -1) {
                StringBuilder.append((char) i);
                i = FileReader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> getValue(String val) {
        Reader Reader = new Reader();
        String[] str = StringBuilder.toString().split("\n");
        int i = 0;
        Map<String, Object> Map = new HashMap<>();
        while (i < str.length) {
            if (str[i].equals("[" + val + "]")) {
                int j = i + 1;
                while (j < str.length) {
                    if (str[j].contains("[") && !str[j].equals("[" + val + "]")) {
                        break;
                    }
                    String val1 = str[j].split("=")[0];
                    String val2 = str[j].split("=")[1];
                    if (val1.equals("localport")) {
                        Map.put(val1, Integer.parseInt(val2));
                    } else {
                        Map.put(val1, val2);
                    }
                    j++;
                }
            }
            i++;
        }
        return Map;
    }

    public static void main(String[] args) {
        Map<String, Object> Map = getValue("Server");
        for (Object Object : Map.values()) {
            System.out.println(Object);
        }
    }
}
