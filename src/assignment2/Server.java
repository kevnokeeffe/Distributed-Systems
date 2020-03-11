package assignment2;

import assignment2.db.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server extends JFrame {
        // Text area for displaying contents
        private JTextArea jta = new JTextArea();
        private String tableName = "students";
        private Connection dbConnection;
        private ResultSet rs;
        public int[] students = new int[10];
        private int student;
        //The input and output streams to the client
        private DataInputStream inputFromClient;
        private DataOutputStream outputToClient;

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
                    Statement myStatement = dbConnection.createStatement();
                    rs = myStatement.executeQuery("select * from students");
                    int x=0;
                    while (rs.next()){
                        students[x] = rs.getInt("STUD_ID");
                        x++;
                    }
                    rs.close();
                    Socket s1=serverSocket.accept();
                    myClient c = new myClient(s1);
                    c.start();
                }
            }
            catch(IOException | SQLException ex) {
                System.err.println(ex);
            }
        } // End Server Construct
        private class myClient extends Thread {
            //The socket the client is connected through
            private Socket socket;
            private ResultSet rs2;
            private String lName;
            private String fName;
            //The ip address of the client
            private InetAddress address;
            private Boolean loggedin = false;
            private int studNo = 0;
            public myClient(Socket socket) throws IOException, SQLException {
                // Declare & Initialise input/output streams
                inputFromClient = new DataInputStream(socket.getInputStream());
                outputToClient = new DataOutputStream(socket.getOutputStream());
            }

            public void checkDB() throws IOException {
               studNo = inputFromClient.readInt();
                int x = 0;
                    for (int i = 0; i < students.length; ++i) {
                        if (studNo == students[i]) {
                            outputToClient.writeInt(studNo);
                            loggedin = true;
                        }
                    }
                outputToClient.writeInt(x);
            }

            public void run() {
                // Send+Receive+Calculations goes here
                // preform results set for array
                    try {
                        while (true) {
                            if (loggedin != true) {
                                checkDB();
                            } else if (loggedin = true){
                                Statement myStatement = dbConnection.createStatement();
                                rs2 = myStatement.executeQuery("select fName, lName from students where stud_no='"+studNo+"'");
                                fName = rs2.getString("fName");
                                lName = rs2.getString("lName");
                                rs2.close();
                                System.out.println(rs2);
                                // Receive radius from the client
                                double radius = inputFromClient.readDouble();
                                // Compute area
                                double area = radius * radius * Math.PI;
                                // Send area back to the client
                                outputToClient.writeDouble(area);
                                outputToClient.writeUTF(fName);
                                outputToClient.writeUTF(lName);
                                jta.append("Radius received from client: " + radius + '\n');
                                jta.append("Area found: " + area + '\n');
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e + " on " + socket);
                    }
                }

        }

}
