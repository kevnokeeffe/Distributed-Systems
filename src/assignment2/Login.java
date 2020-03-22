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

    // IO streams

    public static void main(String[] args) {
        Login frameTabel = new Login();
    }
    private JButton bLogin = new JButton("Login");
    private JPanel panel = new JPanel();
    private JPanel panelArea = new JPanel();
    private JTextArea jta = new JTextArea();
    private JTextField newJtf = new JTextField();
    // Text area to display contents
    private JTextArea newJta = new JTextArea();
    private JLabel areaOfCircle = new JLabel("Enter radius of a circle:");
    private JButton exit =new JButton("Exit");
    private JButton send =new JButton("Send");
    private JTextField studentNumber = new JTextField(15);
    private JLabel labelStdNo = new JLabel("Enter Student Number: ");
    private String fullName = "";
    private String area = "";
    private double radius = 0;
    private String areaFound = "nothing";

    Login(){
        // Create the login screen
        super("Client Login");
        setTitle("Client Login");
        setSize(300,200);
        setLocation(500,280);
        panel.setLayout (null);
        labelStdNo.setBounds(70,20,150,20);
        studentNumber.setBounds(70,50,150,20);
        bLogin.setBounds(100,90,80,20);
        panel.add(labelStdNo);
        panel.add(bLogin);
        panel.add(studentNumber);
        add(new JScrollPane(jta), BorderLayout.CENTER);

        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        // Connect to the server
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
        bLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int studentNo = Integer.parseInt(studentNumber.getText().trim());
                try {
                    // Send entered number to Server for stud_no database check
                    toServer.writeInt(studentNo);
                    toServer.flush();
                    // receive answer from server
                    int stdNo = fromServer.readInt();
                    jta.append("Checking Student Number..." + new Date() + '\n');
                    // If the number sevt back equals the number entered create the new page layout
                    if(studentNo == stdNo) {
                        // Remove login layout
                        getContentPane().remove(panel);
                        //Design of new layout
                        setTitle("Area of Circle");
                        panelArea.setLayout (null);
                        setLocation(500,280);
                        setSize(300,400);
                        JScrollPane sp = new JScrollPane(newJta);
                        areaOfCircle.setBounds(30,20,150,20);
                        newJtf.setBounds(30,45,80,20);
                        exit.setBounds(180,315,80,20);
                        send.setBounds(180,45,80,20);
                        sp.setBounds(30,80,230,220);
                        panelArea.add(newJtf);
                        panelArea.add(areaOfCircle);
                        panelArea.add(exit);
                        panelArea.add(send);
                        //panelArea.add(newJta);
                        panelArea.add(sp);

                        // Create new area of circle layout
                        getContentPane().add(panelArea);

                        fullName = fromServer.readUTF();
                        // Print name
                        newJta.append("Welcome "+fullName+'\n');
                        newJta.append("You are now connected to the server."+'\n');
                        newJta.append("Please enter the radius of a circle."+'\n');
                        //Add event listeners for buttons
                        exit.addActionListener(new Login.ExitListener());
                        send.addActionListener(new Login.Listener());

                    } else if(studentNo != stdNo) {
                        // If invalid login details post message and restart the login page
                        JOptionPane.showMessageDialog(null,"Sorry Invalid Student Number");
                        Login reset =new Login();
                        reset.setVisible(true);
                        dispose();
                    }else{JOptionPane.showMessageDialog(null,"Sorry Invalid Student Number");
                        Login reset =new Login();
                        reset.setVisible(true);
                        dispose();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                    // Get the radius from the text field
                    radius = Double.parseDouble(newJtf.getText().trim());
                    // Send the radius to the server
                    toServer.writeDouble(radius);
                    toServer.flush();
                    // Get area from the server
                   areaFound = fromServer.readUTF().trim();

                   // Run error fix loop
                    for(int i = 0; i < 3; ++i){
                        toServer.writeDouble(radius);
                        toServer.flush();
                       areaFound = fromServer.readUTF().trim();
                   }
                        // Display to the text area
                        newJta.append("Radius: " + radius + "\n");
                        newJta.append("Area: " +areaFound + '\n');
            }
            catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

}

