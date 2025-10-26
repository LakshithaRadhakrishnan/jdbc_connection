import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentManager extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;

    public StudentManager() {
        setTitle("Student Manager");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Course"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel panel = new JPanel();
        JButton addButton = new JButton("Add Student");
        JButton refreshButton = new JButton("Refresh List");
        panel.add(addButton);
        panel.add(refreshButton);
        add(panel, BorderLayout.SOUTH);

        // Actions
        addButton.addActionListener(e -> addStudent());
        refreshButton.addActionListener(e -> loadStudents());

        loadStudents();
    }

    private void addStudent() {
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField courseField = new JTextField();
        Object[] message = {
                "Name:", nameField,
                "Age:", ageField,
                "Course:", courseField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Add New Student", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String course = courseField.getText();
            try {
                Connection conn = DBConnection.getConnection();
                String sql = "CREATE TABLE IF NOT EXISTS students (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                             "name TEXT NOT NULL," +
                             "age INTEGER NOT NULL," +
                             "course TEXT)";
                conn.createStatement().execute(sql);

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO students(name, age, course) VALUES(?,?,?)");
                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setString(3, course);
                ps.executeUpdate();
                ps.close();
                conn.close();
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                loadStudents();
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM students");
            while(rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("course")
                });
            }
            rs.close();
            conn.close();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManager().setVisible(true));
    }
}
