import java.awt.Color;
import java.awt.Graphics;
import java.net.Socket;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Ui extends JFrame {

    static JPanel panneauStatus ;
    static int i = 0;

    static Ecouteur ecouteur;

    static JButton refreshB = new JButton("refresh");

    static JTextField ipAddress = new JTextField(    Reader.getValue("Server").get("ip").toString()   );
    static JTextField port = new JTextField( Reader.getValue("Server").get("localport").toString())   ;

    static JButton uploadChoose = new JButton("File");

    static JComboBox<String> fileToDonw = new JComboBox<>();

    static JLabel statserver = new JLabel();

    static JPanel upload = new JPanel();
    static JPanel download = new JPanel();

    static JButton uploadB = new JButton("Upload");
    static JButton downloadB = new JButton("Download");

    static JProgressBar progressBar = new JProgressBar(0, 100);

    public Ui(int width, int height) throws Exception {
        ecouteur = new Ecouteur(this);
        setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configureButton();
      
        repaint();
        // statServers();

    }
    

   

    public  static void configureProgresBar ()
    {
        progressBar.setLayout(null);
        progressBar.setBounds(50, 50, 100, 10);
        upload.add(progressBar);
        upload.repaint();
    }

    public void configurationUploadPanel() {
        this.setLayout(null);
        download.removeAll();
        upload.removeAll();
        remove(download);
        JLabel title = new JLabel("Upload");
        title.setLayout(null);
        JButton up = new JButton("up");
        up.setLayout(null);
        up.setBounds(185, 100, 60, 20);

        uploadChoose.setLayout(null);
        uploadChoose.setBounds(50, 100, 130,  20);
        uploadChoose.setVisible(true);

        upload.setLayout(null);
        upload.setBackground(Color.LIGHT_GRAY);

        upload.setBounds(25, 35, 250, 180);
        upload.setVisible(true);
        title.setBounds((upload.getWidth() / 2) - 20, 0, 100, 30);

        upload.add(title);
        upload.add(uploadChoose);
        upload.add(up);
        up.addActionListener(ecouteur);
        add(upload);
        revalidate();
        repaint();
    }

    public void configurationDownloadPanel() {

        upload.removeAll();
        download.removeAll();
        upload.remove(uploadChoose);
        remove(upload);
        JLabel title = new JLabel("Download");
        JButton down = new JButton("do");
        JButton del = new JButton("del");


        down.setLayout(null);
        down.setBounds(115, 100, 60, 20);
        del.setLayout(null);
        del.setBounds(175, 100, 60, 20);


        fileToDonw.setLayout(null);
        fileToDonw.setBounds(10, 100, 100, 20);
        fileToDonw.setVisible(true);

        title.setLayout(null);
        upload.setLayout(null);
        upload.setBackground(Color.LIGHT_GRAY);
        upload.setBounds(25, 35, 250, 180);
        upload.setVisible(true);
        title.setBounds((upload.getWidth() / 2) - 20, 0, 100, 30);
        upload.add(title);
        upload.add(down);
        upload.add(del);
        upload.add(fileToDonw);
        down.addActionListener(ecouteur);
        del.addActionListener(ecouteur);
        add(upload);
        repaint();
    }

    public void configureButton() {
        this.setLayout(null);
        uploadB.setLayout(null);

        downloadB.setLayout(null);
        refreshB.setLayout(null);

        ipAddress.setLayout(null);
        port.setLayout(null);

        JLabel ip = new JLabel("ip");
        JLabel portT = new JLabel("port");

        ip.setLayout(null);
        port.setLayout(null);
        portT.setLayout(null);
        portT.setBounds(140, 0, 50, 20);
        ip.setBounds(15, 0, 50, 20);

        ipAddress.setBounds(30, 5, 100, 15);
        port.setBounds(170, 5, 100, 15);

        uploadB.setBounds(0, 220, 100, 20);
        downloadB.setBounds(100, 220, 100, 20);
        refreshB.setBounds(200, 220, 100, 20);

        uploadB.addActionListener(ecouteur);
        downloadB.addActionListener(ecouteur);
        refreshB.addActionListener(ecouteur);
        uploadChoose.addActionListener(ecouteur);

        this.add(uploadB);
        this.add(refreshB);
        this.add(downloadB);
        this.add(ipAddress);
        this.add(port);
        this.add(ip);
        this.add(portT);
    }

}
