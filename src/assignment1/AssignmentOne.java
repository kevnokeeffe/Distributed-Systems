package assignment1;

import java.security.SecureRandom;
import java.awt.*;
import java.sql.*;
import java.util.Properties;
import java.util.UUID;
import java.util.Scanner;
import javax.swing.*;
import java.awt.event.*;

public class AssignmentOne {

    static // Scanner
            Scanner sca = new Scanner(System.in);
    // The name of the MySQL account.
    private static String userName = "kev";
    // The password for the MySQL account.
    private static String password = "akakok1984";
    // The name of the computer running MySQL.
    static String serverName = "localhost";
    // The port of the MySQL server.
    static int portNumber = 3308;
    // The name of the database we are testing with.
    static String dbName = "databasetest";
    // The name of the table we are testing with.
    public static String tableName = "database_test_table";
    // Global connection object
    public static Connection conn = null;
    // Declare ResultSet object
    public static ResultSet rs;
    // Declare variables
    public static String PPSn = "", DOB = "", Name = "", Address = "", Salary = "", Gender = "", UUID = "";
    // Declare JFrame object
    public static JFrame f;
    // Dropdown
    public static String gender[]={"Male","Female","Non-Binary"};

    // The Connection Method
    public Connection getConnection() throws SQLException {
        // Create connection properties for the login and password for the db connection
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        // Create the connection with the database
        conn = DriverManager.getConnection("jdbc:mysql://"
                        + this.serverName + ":" + this.portNumber + "/" + this.dbName,
                connectionProps);
        // Return connection
        return conn;
    }

    // Create the view elements and define them
    public static void main(String[] args) {

        //String uniqueID = UUID.randomUUID().toString().replace("-", "");
        AssignmentOne app = new AssignmentOne();
        app.run();
        f = new JFrame();
        JLabel labelHeader = new JLabel("Employee Database");
        JLabel labelBlank = new JLabel("");
        // JLabel labelUUID = new JLabel("UUID: ");
        JLabel labelPPSn = new JLabel("PPSn: ");
        JLabel labelDOB = new JLabel("DOB: ");
        JLabel labelName = new JLabel("Name: ");
        JLabel labelAddress = new JLabel("Address: ");
        JLabel labe1Salary = new JLabel("Salary: ");
        JLabel labe1Gender = new JLabel("Gender: ");
        // JTextField textUUID=new JTextField(20);
        JTextField textPPSn=new JTextField(20);
        JTextField textDOB=new JTextField(20);
        JTextField textName=new JTextField(20);
        JTextField textAddress=new JTextField(20);
        JTextField textSalary=new JTextField(20);
        JTextField textGender=new JTextField(20);
        JButton previous = new JButton("Previous");
        JButton next = new JButton("Next");
        JButton clear = new JButton("clear");
        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton update = new JButton("Update");

        try {
            // Grab the data from the database
            Statement st =conn.createStatement();
            rs=st.executeQuery("select * from "+ tableName);
            // fill the text field with the grabbed data
            if(rs.next()){
                // UUID=rs.getString("UUID");
                PPSn=rs.getString("PPSn");
                DOB=rs.getString("DOB");
                Name=rs.getString("Name");
                Address=rs.getString("Address");
                Salary=rs.getString("Salary");
                Gender=rs.getString("Gender");
                // textUUID.setText(UUID);
                textPPSn.setText(PPSn);
                textDOB.setText(DOB);
                textName.setText(Name);
                textAddress.setText(Address);
                textSalary.setText(Salary);
                textGender.setText(Gender);
            }

            // Clears the text fields
            clear.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    // textUUID.setText("");
                    textPPSn.setText("");
                    textDOB.setText("");
                    textName.setText("");
                    textAddress.setText("");
                    textSalary.setText("");
                    textGender.setText("");

                }
            });

            // Updates the an entry in the data base
            update.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // String uuid = textUUID.getText();
                        String a = textPPSn.getText();
                        String b = textDOB.getText();
                        String c = textName.getText();
                        String d = textAddress.getText();
                        String g = textSalary.getText();
                        String f = textGender.getText();
                        Properties connectionProps = new Properties();

                        if(a == null || a.trim().equals( "" ) ||
                                b == null || b.trim().equals( "" ) ||
                                c == null || c.trim().equals( "" ) ||
                                d == null || d.trim().equals( "" ) ||
                                g == null || g.trim().equals( "" ) ||
                                f == null || f.trim().equals( "" ))
                        { JOptionPane.showMessageDialog( null, "Must fill in all fields!!");
                        } else {
                            if (b.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                                if (g.matches("-?(0|[1-9]\\d*)")) {
                                    connectionProps.put("user", userName);
                                    connectionProps.put("password", password);
                                    Connection myConn = DriverManager.getConnection("jdbc:mysql://"
                                                    + serverName + ":" + portNumber + "/" + dbName,
                                            connectionProps);
                                    //Statement st = myConn.createStatement();
                                    PreparedStatement st = myConn.prepareStatement("UPDATE "
                                            +tableName
                                            + " SET "
                                            +"PPSn = " + "'"+ a + "',"
                                            +"DOB = " + "'"+ b + "',"
                                            +"Name = " + "'"+ c + "',"
                                            +"Address = " + "'"+ d + "',"
                                            +"Salary = " + "'"+ g + "',"
                                            +"Gender = " + "'"+ f + "'"
                                            + " WHERE PPSn=?");
                                    st.setString( 1, PPSn );
                                    st.execute();
                                    myConn.close();
                                    Statement st2=conn.createStatement();
                                    rs=st2.executeQuery("select * from "+ tableName);
                                    JOptionPane.showMessageDialog( null, "Updated" );
                                }else {JOptionPane.showMessageDialog( null, "Im on to you! Salary has to be a number!");}
                            }else {JOptionPane.showMessageDialog( null, "Date of birth has to be in the correct format YYYY-MM-DD.");}
                        }
                    }catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });

            // Deletes the selected data file
            delete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        Properties connectionProps = new Properties();
                        connectionProps.put("user", userName);
                        connectionProps.put("password", password);
                        Connection myConn = DriverManager.getConnection("jdbc:mysql://"
                                        + serverName + ":" + portNumber + "/" + dbName,
                                connectionProps);
                        PreparedStatement myStat = myConn.prepareStatement( "DELETE FROM "+tableName+" WHERE PPSn=?" );
                        myStat.setString( 1, PPSn );
                        myStat.execute();
                        myConn.close();
                        // Clears the text fields on delete
                        textPPSn.setText("");
                        textDOB.setText("");
                        textName.setText("");
                        textAddress.setText("");
                        textSalary.setText("");
                        textGender.setText("");
                        // Delete popup
                        JOptionPane.showMessageDialog( null, "Deleted" );
                        Statement st2=conn.createStatement();
                        rs=st2.executeQuery("select * from "+ tableName);
                    }catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });

            // Adds an entry to the database
            add.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // Gets everything converts it to a string

                        String a = textPPSn.getText();
                        String b = textDOB.getText();
                        String c = textName.getText();
                        String d = textAddress.getText();
                        String g = textSalary.getText();
                        String f = textGender.getText();

                        // Checks to see if the valid fields are filled in
                        // & handles empty String case
                        if(a == null || a.trim().equals( "" ) ||
                                b == null || b.trim().equals( "" ) ||
                                c == null || c.trim().equals( "" ) ||
                                d == null || d.trim().equals( "" ) ||
                                g == null || g.trim().equals( "" ) ||
                                f == null || f.trim().equals( "" ))
                        { JOptionPane.showMessageDialog( null, "Must fill in all fields!!");
                        } else {
                            if (b.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                                if (g.matches("-?(0|[1-9]\\d*)")) {
                                    // Handle non-empty String case and add the data entry
                                    Properties connectionProp = new Properties();
                                    connectionProp.put("user", userName);
                                    connectionProp.put("password", password);
                                    Connection con = DriverManager.getConnection("jdbc:mysql://"
                                                    + serverName + ":" + portNumber + "/" + dbName,
                                            connectionProp);

                                    Statement st = con.createStatement();
                                    st.executeUpdate(
                                            "INSERT INTO "+ tableName +"(PPSn, DOB, Name, Address, Salary, Gender)"
                                                    + "VALUES ("
                                                    +"'"+ a +"',"
                                                    +"'"+ b +"',"
                                                    +"'"+ c +"',"
                                                    +"'"+ d +"',"
                                                    +"'"+ g +"',"
                                                    +"'"+ f +"');");
                                    JOptionPane.showMessageDialog( null, "Added" );
                                    textPPSn.setText("");
                                    textDOB.setText("");
                                    textName.setText("");
                                    textAddress.setText("");
                                    textSalary.setText("");
                                    textGender.setText("");
                                    st.close ();
                                    Statement st2=conn.createStatement();
                                    rs=st2.executeQuery("select * from "+ tableName);
                                }else {JOptionPane.showMessageDialog( null, "Im on to you! Salary has to be a number!");}
                            }else {JOptionPane.showMessageDialog( null, "Date of birth has to be in the correct format YYYY-MM-DD.");}
                        }
                    }catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });

            // Previous button listener, moves back one data entry
            previous.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try {
                        next.setEnabled(true);
                        if(rs.previous()){
                            PPSn=rs.getString("PPSn");
                            DOB=rs.getString("DOB");
                            Name=rs.getString("Name");
                            Address=rs.getString("Address");
                            Salary=rs.getString("Salary");
                            Gender=rs.getString("Gender");
                            textPPSn.setText(PPSn);
                            textDOB.setText(DOB);
                            textName.setText(Name);
                            textAddress.setText(Address);
                            textSalary.setText(Salary);
                            textGender.setText(Gender);
                        }else {previous.setEnabled(false);
                            textPPSn.setText("");
                            textDOB.setText("");
                            textName.setText("");
                            textAddress.setText("");
                            textSalary.setText("");
                            textGender.setText("");}
                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });

            // Next button listener, moves forward one data entry
            next.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try {
                        previous.setEnabled(true);
                        if(rs.next()){
                            PPSn=rs.getString("PPSn");
                            DOB=rs.getString("DOB");
                            Name=rs.getString("Name");
                            Address=rs.getString("Address");
                            Salary=rs.getString("Salary");
                            Gender=rs.getString("Gender");
                            textPPSn.setText(PPSn);
                            textDOB.setText(DOB);
                            textName.setText(Name);
                            textAddress.setText(Address);
                            textSalary.setText(Salary);
                            textGender.setText(Gender);
                        }else {next.setEnabled(false);
                            textPPSn.setText("");
                            textDOB.setText("");
                            textName.setText("");
                            textAddress.setText("");
                            textSalary.setText("");
                            textGender.setText("");}
                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });

            System.out.println("Data loaded from database");
        }catch (SQLException e) {
            System.out.println("ERROR: Could not load the data!");
            e.printStackTrace();
            return;
        }

        // Set up grid with elements
        JPanel assignmentPanel=new JPanel(new GridLayout(10,2,5,5));
        assignmentPanel.add(labelHeader);
        assignmentPanel.add(labelBlank);
        assignmentPanel.add(previous);
        assignmentPanel.add(next);
        assignmentPanel.add(labelPPSn);
        assignmentPanel.add(textPPSn);
        assignmentPanel.add(labelDOB);
        assignmentPanel.add(textDOB);
        assignmentPanel.add(labelName);
        assignmentPanel.add(textName);
        assignmentPanel.add(labelAddress);
        assignmentPanel.add(textAddress);
        assignmentPanel.add(labe1Salary);
        assignmentPanel.add(textSalary);
        assignmentPanel.add(labe1Gender);
        assignmentPanel.add(textGender);
        assignmentPanel.add(clear);
        assignmentPanel.add(add);
        assignmentPanel.add(delete);
        assignmentPanel.add(update);

        // On window close it shuts down the whole program.
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800,800);
        f.add(assignmentPanel);
        f.setVisible(true);
        f.pack();
    }

    public void run() {
        // Run the connection to MySQL
        try {
            conn = this.getConnection();
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println("ERROR: Could not connect to the database");
            e.printStackTrace();
            return;
        }
    }
}

