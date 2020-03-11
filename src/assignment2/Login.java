package assignment2;

import com.mysql.cj.log.Log;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;

public class Login extends JFrame {
    private DataOutputStream toServer;
    private DataInputStream fromServer;

    public static void main(String[] args) {
        Login frameTabel = new Login();
    }
    JButton blogin = new JButton("Login");
    JPanel panel = new JPanel();
    JTextField studentNumber = new JTextField(15);
    JLabel labelStdNo = new JLabel("Enter Student Number: ");

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

        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);
            // Create an input & output streams to send & receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            //jta.append(ex.toString() + '\n');
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
                    if(studentNo == stdNo) {
                        Client regFace =new Client();
                        regFace.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null,"Sorry Invalid Student Number");
                        studentNumber.setText("");
                        studentNumber.requestFocus();
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

