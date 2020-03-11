package assignment2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;


public class Login extends JFrame {
    private DataOutputStream toServer;
    private DataInputStream fromServer;

    public static void main(String[] args) {
        Login frameTabel = new Login();
    }
    private JButton blogin = new JButton("Login");
    private JPanel panel = new JPanel();
    private JTextArea jta = new JTextArea();
    private JTextField studentNumber = new JTextField(15);
    private JLabel labelStdNo = new JLabel("Enter Student Number: ");

    Login(){
        super("Client Login");
        setTitle("Client Login");
        setSize(300,200);
        setLocation(500,280);
        panel.setLayout (null);
        labelStdNo.setBounds(70,20,150,20);
        studentNumber.setBounds(70,50,150,20);
        blogin.setBounds(100,90,80,20);
        panel.add(labelStdNo);
        panel.add(blogin);
        panel.add(studentNumber);
        add(new JScrollPane(jta), BorderLayout.CENTER);

        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);
            jta.append("Server started at " + new Date() + '\n');
            // Create an input & output streams to send & receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            jta.append(ex.toString() + '\n');
            System.out.println("bad command");
        }
        actionlogin();
    }

    public void actionlogin(){
        blogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int studentNo = Integer.parseInt(studentNumber.getText().trim());
                try {
                    toServer.writeInt(studentNo);
                    toServer.flush();

                    int stdNo = fromServer.readInt();
                    jta.append("Checking Student Number..." + new Date() + '\n');
                    if(studentNo == stdNo) {
                        AreaOfCircle regFace =new AreaOfCircle();
                        regFace.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null,"Sorry Invalid Student Number");
                        Login reset =new Login();
                        reset.setVisible(true);
                        dispose();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

