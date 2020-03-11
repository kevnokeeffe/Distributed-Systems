package server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.DateFormat;
public class SimpleServer {
    public static void main(String args[]) {
        ServerSocket s = null;

//Input From Keyboard
        String str;
        DataInputStream indata= new  DataInputStream (System.in);
        System.out.println("Type in Something & Press Enter to Send it To The >>C L I E N T<<: ");
// Register your service on port 5432
        try {
            s = new ServerSocket(5432);
        } catch (IOException e) {
// ignore
        }
// Run the listen/accept loop forever
        while (true) {
            try {
// Wait here and listen for a connection
                Socket s1 = s.accept();
// Get output stream associated with the socket
                OutputStream s1out = s1.getOutputStream();
                DataOutputStream dos = new DataOutputStream(s1out);

                System.out.println();
                System.out.println("Write Something: ");
                str=indata.readLine();
                dos.writeUTF(str);

// Get an input stream from the socket
                InputStream is = s1.getInputStream();

// Decorate it with a �data� input stream
                DataInputStream dis = new DataInputStream(is);
// Read the input and print it to the screen
                System.out.println("Incoming From Client>>>:" +dis.readUTF());
//Display System Date
                DateFormat defaultDate = DateFormat.getDateInstance();
                System.out.println(defaultDate.format(new Date()));
//Display System Time
                DateFormat shortTime = DateFormat.getTimeInstance(DateFormat.SHORT);
                System.out.println(shortTime.format(new Date()));

// Close the connection, but not the server socket
                dos.close();
                s1.close();
                dis.close();
            } catch (IOException e) {
// ignore
            }
        }
    }
}
