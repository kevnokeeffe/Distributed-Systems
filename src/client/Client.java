package client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import javax.swing.*;
import server.Server;

public class Client extends JFrame {
  // Text field for receiving radius
  private JTextField jtf = new JTextField();
  // Text area to display contents
  private JTextArea jta = new JTextArea();
  private JButton exit =new JButton("Exit");
  private JButton send =new JButton("Send");

  // IO streams
  private DataOutputStream toServer;
  private DataInputStream fromServer;

  public static void main(String[] args) {
    new Client();
  }

  public Client() {
    // Panel p to hold the label and text field
    JPanel p = new JPanel();
    // JFrame frame = new JFrame();
    p.setLayout(new BorderLayout());
    p.add(new JLabel("Enter message"), BorderLayout.WEST);
    p.add(jtf, BorderLayout.CENTER);
    jtf.setHorizontalAlignment(JTextField.RIGHT);
    p.add(send, BorderLayout.BEFORE_LINE_BEGINS);
    p.add(exit, BorderLayout.AFTER_LINE_ENDS);
    setLayout(new BorderLayout());
    add(p, BorderLayout.NORTH);
    add(new JScrollPane(jta), BorderLayout.CENTER);

    send.addActionListener(new Listener());
    exit.addActionListener(new ExitListener());
    setTitle("Client");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); // It is necessary to show the frame here!
    try {
      Socket socket = new Socket("localhost", 8000);
      jta.append("Server started at " + new Date() + '\n');
      fromServer = new DataInputStream(socket.getInputStream());
      toServer = new DataOutputStream(socket.getOutputStream());

        while (true) {
          String message = fromServer.readUTF();
          if (!message.isEmpty()) jta.append("[server]: " + message + "\n");
        }

    }
    catch (IOException ex) {
      jta.append(ex.toString() + '\n');
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
      try {
        double radius = Double.parseDouble(jtf.getText().trim());
        toServer.writeDouble(radius);
        toServer.flush();
      }
      catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }
}