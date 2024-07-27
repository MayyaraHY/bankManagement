import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends JFrame implements ActionListener {

    private JButton saveButton;
    private JButton closeButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public AdminPanel() {
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 900);
        setLocationRelativeTo(null);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);

        // Create table
        String[] columns = {"ID", "Username", "Email", "Password", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Populate table
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "username", "password");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM compte");
            while (rs.next()) {
                Object[] row = {rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getString("role")};
                tableModel.addRow(row);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add components to frame
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            int rowCount = tableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                int id = (int) tableModel.getValueAt(i, 0);
                String username = (String) tableModel.getValueAt(i, 1);
                String email = (String) tableModel.getValueAt(i, 2);
                String password = (String) tableModel.getValueAt(i, 3);
                String role = (String) tableModel.getValueAt(i, 4);

                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bank", "root", "1511");
                    PreparedStatement stmt = conn.prepareStatement("UPDATE compte SET Login=?, MDP=?, Type=?, WHERE idCl=?");
                    stmt.setString(1, username);
                    stmt.setString(2, email);
                    stmt.setString(3, password);
                    stmt.setString(4, role);
                    stmt.setInt(5, id);
                    stmt.executeUpdate();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(this, "Changes saved successfully.");
        } else if (e.getSource() == closeButton) {
            dispose();
        }
    }
}
