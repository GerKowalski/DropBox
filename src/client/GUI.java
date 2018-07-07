package client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import static commonThings.API.DELETE_FILE;
import static commonThings.API.DOWNLOAD_FILE;

public class GUI extends JFrame {
    private JTree fileList;
    private JLabel corgi;
    private JPanel center;
    private JButton upload;
    private JButton delete;
    private JButton download;
    private JPanel down;
    private JPanel logPassField;
    private JPanel loginPanel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton signIn;
    private Connection connection;
    private String login;
    private JPanel mainPanel;

    public GUI() {
        super("CorgiCloudService");

        connection = new Connection();
        connection.init(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(300,370);

        logPassField = new JPanel(new GridLayout(1,3));
        loginPanel = new JPanel();
        loginField = new JTextField();
        passwordField = new JPasswordField();
        loginPanel.setBackground(Color.WHITE);

        try {
            corgi = new JLabel(new ImageIcon(ImageIO.read(new File(
                    "/Users/fima/Desktop/GeekBrains/DropBox/src/client/Resource/corgi.jpg"
            )).getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        signIn = new JButton("Sign in");
        loginPanel.add(corgi, BorderLayout.NORTH);
        logPassField.add(loginField);
        logPassField.add(passwordField);
        logPassField.add(signIn);
        loginPanel.add(logPassField, BorderLayout.SOUTH);
        setContentPane(loginPanel);
        setVisible(true);


        down = new JPanel(new GridLayout(3,1));
        delete = new JButton("Delete");
        upload = new JButton("Upload");
        download = new JButton("Download");
        down.add(delete);
        down.add(upload);
        down.add(download);

        center = new JPanel(new GridLayout(1,1));
        center.setSize(new Dimension(300,300));

        mainPanel = new JPanel();
        mainPanel.add(center, BorderLayout.WEST);
        mainPanel.add(down, BorderLayout.EAST);
        mainPanel.setVisible(true);

        delete.addActionListener(e -> sendMessage(DELETE_FILE + " "
                + login + " " + fileList.getSelectionPath().getLastPathComponent().toString()));

        upload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Выберите файл");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                sendFile(fileChooser.getSelectedFile());
            }
        });

        download.addActionListener(e -> {
            sendMessage(DOWNLOAD_FILE + " " + login + " " + fileList.getSelectionPath().getLastPathComponent().toString());
        });
        signIn.addActionListener(e -> sendAuthData());
        }
    public void sendAuthData() {
        login = loginField.getText();
        connection.auth(login, String.valueOf(passwordField.getPassword()));
        loginField.setText("");
        passwordField.setText("");
    }

    public void showFileList(Vector<String> files) {
        center.setVisible(false);
        center.removeAll();
        fileList = new JTree(files);
        center.add(new JScrollPane(fileList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        center.setVisible(true);
    }

    public void changePaneToMain() {
        setSize(300, 450);
        setContentPane(mainPanel);
    }

    public void sendMessage(String msg) {
        connection.sendMessage(msg);
    }

    public void sendFile(File file) {
        connection.sendFile(file);
    }
}
