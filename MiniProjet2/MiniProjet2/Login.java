import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JLabel userLabel = new JLabel("Username:");
    JLabel passLabel = new JLabel("Password:");
    JTextField userField = new JTextField();
    JPasswordField passField = new JPasswordField();
    JButton loginButton = new JButton("Login");

    public Login() {
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 900);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(30, 20));
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        loginButton.addActionListener(this);

        add(panel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String username = userField.getText();
        String password = new String(passField.getPassword());
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bank", "root", "1511");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT Type FROM compte WHERE Login='" + username + "' AND MDP='" + password + "'");
            if (rs.next()) {
                String type = rs.getString("Type");
                if (type.equals("admin")) {
                    JOptionPane.showMessageDialog(this, "Welcome, Admin!");
                    new AdminPanel();
                } else if (type.equals("client")) {
                    JOptionPane.showMessageDialog(this, "Welcome, Client!");
                    ResultSet sID = stmt.executeQuery(
                            "SELECT idCl FROM compte WHERE Login='" + username + "' AND MDP='" + password + "'");
                    if (sID.next()) {
                        String idCl = sID.getString("idCl");
                        AccountDetails app = new AccountDetails(idCl);
                        app.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error retrieving client ID!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
