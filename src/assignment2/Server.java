package assignment2;

import assignment2.db.db.DBConnection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Server extends JFrame {
        // Text area for displaying contents
        private JTextArea jta = new JTextArea();
        private String tableName = "students";
        private Connection dbConnection;
        private ResultSet rs;
        public int[] students = new int[10];

        public static void main(String[] args) {
            new Server();
        }

        public Server() {
            // Place text area on the frame
            setLayout(new BorderLayout());
            add(new JScrollPane(jta), BorderLayout.CENTER);
            setTitle("Server");
            setSize(500, 300);
            setLocation(500,280);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true); // It is necessary to show the frame here!
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                // Create DB connection
                dbConnection = new DBConnection().getConnection();
                jta.append("Server started at " + new Date() + '\n');
                jta.append("Data Base Connected. " + new Date() + '\n');
                while (true) {
                    // Grab everything from the database
                    Statement myStatement = dbConnection.createStatement();
                    rs = myStatement.executeQuery("select * from "+tableName);
                    int x=0;
                    while (rs.next()){
                        // Put all the student id's into an array
                        students[x] = rs.getInt("STUD_ID");
                        x++;
                    }
                    rs.close();
                    Socket s1=serverSocket.accept();
                    // Launch client
                    myClient c = new myClient(s1);
                    c.start();
                }
            }
            catch(IOException | SQLException ex) {
                System.err.println(ex);
            }
        } // End Server Construct


        private class myClient extends Thread{
            //The input and output streams to the client
            private DataInputStream inputFromClient;
            private DataOutputStream outputToClient;

            //The socket the client is connected through
            private Socket socket;
            private ResultSet rs2;
            private String lName;
            private String fName;
            private double radius = 0;
            private int student = 0;
            private double area = 0;
            //The ip address of the client
            private InetAddress address;
            private Boolean loggedIn = false;
            private int studNo = 0;

            public myClient(Socket socket) throws IOException, SQLException {
                // Declare & Initialise input/output streams
                inputFromClient = new DataInputStream(socket.getInputStream());
                outputToClient = new DataOutputStream(socket.getOutputStream());

            }

            public void checkDB() throws IOException, SQLException {
               studNo = inputFromClient.readInt();
                int x = 0;
                // Check the array to see if the student number exists in it
                    for (int i = 0; i < students.length; ++i) {
                        // If it does do the following:
                        if (studNo == students[i]) {
                            // Send back the number for comp check
                            outputToClient.writeInt(studNo);
                            outputToClient.flush();
                            loggedIn = true;
                            student = studNo;
                            // Get the student name from the database
                            Statement myStatement = dbConnection.createStatement();
                            rs2 = myStatement.executeQuery("select fName, lName from "+tableName+" where STUD_ID = '"+student+"'");
                            rs2.next();
                            fName = rs2.getString("fName");
                            lName = rs2.getString("lName");
                            rs2.close();
                            String fullName = fName+" "+lName;
                            // Send the student name to the client
                            outputToClient.writeUTF(fullName);
                            outputToClient.flush();
                        }
                    }
                    // Send 0 result for comparison fail
                outputToClient.writeInt(x);
                outputToClient.flush();
            }

            public void run() {
                // Send+Receive+Calculations goes here
                // preform results set for array
                    try {
                        while (true) {
                            if (loggedIn != true) {
                                checkDB();
                            } else if (loggedIn = true){
                                // Receive radius from the client
                                radius = inputFromClient.readDouble();
                                System.out.println(radius);
                                // Compute area
                                area = radius * radius * Math.PI;
                                // Convert double to string
                                String areaString = String.valueOf(area);
                                // Send calculated value to client
                                outputToClient.writeUTF(areaString);
                                outputToClient.flush();
                                jta.append("Radius received from client: " + radius + '\n');
                                jta.append("Area found: " + area + '\n');

                            }
                        }
                    } catch(IOException | SQLException ex) {
                        System.err.println(ex);
                    }
                }

        }

}
