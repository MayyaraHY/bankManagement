import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AccountDetails extends JFrame {
    private final JTextField nameField;
    private final JTextField balanceField;

    public AccountDetails(String Id) {
        setTitle("User Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 900);
        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Name:");
        JLabel balanceLabel = new JLabel("Balance:");
        nameField = new JTextField();
        balanceField = new JTextField();
        JButton loadButton = new JButton("Load");

        JPanel panel = new JPanel(new GridLayout(30, 20));
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(balanceLabel);
        panel.add(balanceField);
        panel.add(loadButton);

        add(panel);
        setLocationRelativeTo(null);
        this.loadUserDetails(Id);
    }

    private void loadUserDetails(String userId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bank", "root", "1511")) {
            String sql = "SELECT * FROM client WHERE idCl = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(userId));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("NomCl");
                String balance = rs.getString("SoldeCl");
                nameField.setText(name);
                balanceField.setText(balance);
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred.");
        }
    }
}
