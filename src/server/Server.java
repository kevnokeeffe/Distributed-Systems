package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Server extends JFrame {
  // Text area for displaying contents
  private JTextArea jta = new JTextArea();
  private JTextField jtf = new JTextField();
  private JButton exit =new JButton("Exit");
  private JButton send =new JButton("Send");
  // IO streams
  private DataOutputStream toClient;
  private DataInputStream fromClient;

  public static void main(String[] args) {
    new Server();
  }

  public Server() {
    // Place text area on the frame
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(new JLabel("Enter message"), BorderLayout.WEST);
    p.add(jtf, BorderLayout.CENTER);
    jtf.setHorizontalAlignment(JTextField.RIGHT);
    p.add(send, BorderLayout.BEFORE_LINE_BEGINS);
    p.add(exit, BorderLayout.AFTER_LINE_ENDS);
    add(p, BorderLayout.NORTH);
    add(new JScrollPane(jta), BorderLayout.CENTER);
    send.addActionListener(new Server.Listener()); // Register listener
    exit.addActionListener(new Server.ExitListener());
    setTitle("Server");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); // It is necessary to show the frame here!

    try {
      // Create a server socket
      ServerSocket serverSocket = new ServerSocket(8001);
      jta.append("Server started at " + new Date() + '\n');
      // Listen for a connection request

        Socket socket = serverSocket.accept();
        fromClient = new DataInputStream(socket.getInputStream());
        toClient = new DataOutputStream(socket.getOutputStream());
        while (true) {
          String message = fromClient.readUTF();
          if (!message.isEmpty()) jta.append("[client]: " + message + "\n");
        }
    }
    catch(IOException ex) {
      System.err.println(ex);
    }
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
      while (true) {
      try {
        String area = jtf.getText().trim();
        toClient.writeUTF(area);
        toClient.flush();
      }
      catch (IOException ex) {
        System.err.println(ex);
      }
    }
    }
  }
}