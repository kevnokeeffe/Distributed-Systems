package SimpleServerClient;
    /*
 Multithreaded version of Area of Circle Client/Server programme
*/
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.util.Date;

    public class STServer extends JFrame {
        // Text area for displaying contents
        private JTextArea jta = new JTextArea();
        public static void main(String[] args) {
            new STServer();
        }
        public STServer() {
            // Place text area on the frame
            setLayout(new BorderLayout());
            add(new JScrollPane(jta), BorderLayout.CENTER);
            setTitle("Server");
            setSize(500, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true); // It is necessary to show the frame here!
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                jta.append("Server started at " + new Date() + '\n');
                while (true) {
                    Socket s1=serverSocket.accept();
                    myClient c = new myClient(s1);
                    c.start();
                }
            }
            catch(IOException ex) {
                System.err.println(ex);
            }
        } // End Server Construct
        private class myClient extends Thread {
            //The socket the client is connected through
            private Socket socket;
            //The ip address of the client
            private InetAddress address;
            //The input and output streams to the client
            private DataInputStream inputFromClient;
            private DataOutputStream outputToClient;
            // The Constructor for the client
            public myClient(Socket socket) throws IOException {
            // Declare & Initialise input/output streams
                inputFromClient = new DataInputStream(socket.getInputStream());
                outputToClient = new DataOutputStream(socket.getOutputStream());
            }
            /*
             * The method that runs when the thread starts
             */
            public void run() {
            // Send+Receive+Calculations goes here
                try {
                    while (true) {
                        // Receive radius from the client
                        double radius = inputFromClient.readDouble();
                        // Compute area
                        double area = radius * radius * Math.PI;
                        // Send area back to the client
                        outputToClient.writeDouble(area);
                        jta.append("Radius received from client: " + radius + '\n');
                        jta.append("Area found: " + area + '\n');
                    }
                } catch (Exception e) {
                    System.err.println(e + " on " + socket);
                }
            }
        }
    }
