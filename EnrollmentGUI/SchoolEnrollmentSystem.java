import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SchoolEnrollmentSystem extends JFrame implements ActionListener {
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel lastSchoolLabel;
    private JTextField lastSchoolField;
    private JLabel ageLabel;
    private JTextField ageField;
    private JLabel courseLabel;
    private JComboBox<String> courseComboBox;
    private JButton enrollButton;
    private JButton deleteButton;
    private JButton searchButton; // New button for searching
    private JLabel messageLabel;
    private JPanel mainPanel;

    public SchoolEnrollmentSystem() {
        setTitle("School Enrollment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(137, 209, 254)); // Set background color

        // Create a panel with GridBagLayout for component organization
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(137, 209, 254));
        mainPanel.setPreferredSize(new Dimension(400, 400));
        // Create GridBagConstraints for component placement and spacing
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        lastSchoolLabel = new JLabel("Last School Attended:");
        lastSchoolField = new JTextField(20);
        ageLabel = new JLabel("Age:");
        ageField = new JTextField(20);
        courseLabel = new JLabel("Course:");
        courseComboBox = new JComboBox<>(new String[]{"BSIT", "BSIS", "CS"});
        enrollButton = new JButton("Enroll");
        deleteButton = new JButton("Delete");
        searchButton = new JButton("Search"); // Initialize search button
        messageLabel = new JLabel();

        enrollButton.addActionListener(this);
        deleteButton.addActionListener(this);
        searchButton.addActionListener(this); // Add action listener to search button

        // Set font size for labels and buttons
        Font labelFont = nameLabel.getFont().deriveFont(Font.BOLD, 14f);
        Font buttonFont = enrollButton.getFont().deriveFont(Font.BOLD, 14f);
        nameLabel.setFont(labelFont);
        lastSchoolLabel.setFont(labelFont);
        ageLabel.setFont(labelFont);
        courseLabel.setFont(labelFont);
        enrollButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);
        searchButton.setFont(buttonFont); // Set font for search button

        messageLabel.setFont(labelFont);

        // Set the color of the enroll button
        enrollButton.setBackground(Color.GREEN);

        // Add components to the main panel using GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lastSchoolLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(lastSchoolField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(ageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(courseLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(courseComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Span across two columns
        mainPanel.add(enrollButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(searchButton, gbc); // Add search button

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // Span across two columns
        mainPanel.add(messageLabel, gbc);

        // Add the main panel to the frame's content pane
        getContentPane().add(mainPanel);

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enrollButton) {
            String name = nameField.getText();
            String lastSchool = lastSchoolField.getText();
            String age = ageField.getText();
            String course = (String) courseComboBox.getSelectedItem();

            if (!name.isEmpty() && !lastSchool.isEmpty() && !age.isEmpty()) {
                enrollStudent(name, lastSchool, age, course);
                nameField.setText("");
                lastSchoolField.setText("");
                ageField.setText("");
            }
        } else if (e.getSource() == deleteButton) {
            String name = nameField.getText();
            if (!name.isEmpty()) {
                deleteStudent(name);
                nameField.setText("");
                lastSchoolField.setText("");
                ageField.setText("");
            }
        } else if (e.getSource() == searchButton) { // Search button clicked
            String name = nameField.getText();
            if (!name.isEmpty()) {
                searchStudent(name);
            }
        }
    }

    private void enrollStudent(String name, String lastSchool, String age, String course) {
        try (FileWriter writer = new FileWriter("enrollment.txt", true)) {
            writer.write(name + "," + lastSchool + "," + age + "," + course + "\n");
            writer.flush();
            messageLabel.setText("Welcome to CSU!");
            JOptionPane.showMessageDialog(this, "Enrollment successful!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error enrolling student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent(String name) {
        try {
            File inputFile = new File("enrollment.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            FileWriter writer = new FileWriter(tempFile);

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(",");
                if (studentData[0].trim().equalsIgnoreCase(name.trim())) {
                    found = true;
                    continue;
                }
                writer.write(line + System.lineSeparator());
            }

            writer.close();
            reader.close();

            if (found) {
                inputFile.delete();
                tempFile.renameTo(inputFile);
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStudent(String name) {
        try {
            File inputFile = new File("enrollment.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(",");
                if (studentData[0].trim().equalsIgnoreCase(name.trim())) {
                    found = true;
                    showStudentData(studentData);
                    messageLabel.setText("Student found!");
                    break;
                }
            }

            reader.close();

            if (!found) {
                JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching for student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStudentData(String[] studentData) {
        JFrame searchFrame = new JFrame("Search Result");
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the search window

        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(new Color(137, 209, 254));
        searchPanel.setPreferredSize(new Dimension(400, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        JLabel nameLabel = new JLabel("Name:");
        JLabel nameValueLabel = new JLabel(studentData[0]);
        JLabel lastSchoolLabel = new JLabel("Last School Attended:");
        JLabel lastSchoolValueLabel = new JLabel(studentData[1]);
        JLabel ageLabel = new JLabel("Age:");
        JLabel ageValueLabel = new JLabel(studentData[2]);
        JLabel courseLabel = new JLabel("Course:");
        JLabel courseValueLabel = new JLabel(studentData[3]);

        Font labelFont = nameLabel.getFont().deriveFont(Font.BOLD, 14f);
        nameLabel.setFont(labelFont);
        nameValueLabel.setFont(labelFont);
        lastSchoolLabel.setFont(labelFont);
        lastSchoolValueLabel.setFont(labelFont);
        ageLabel.setFont(labelFont);
        ageValueLabel.setFont(labelFont);
        courseLabel.setFont(labelFont);
        courseValueLabel.setFont(labelFont);

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        searchPanel.add(nameValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPanel.add(lastSchoolLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        searchPanel.add(lastSchoolValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        searchPanel.add(ageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        searchPanel.add(ageValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        searchPanel.add(courseLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        searchPanel.add(courseValueLabel, gbc);

        searchFrame.getContentPane().add(searchPanel);

        searchFrame.pack();
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SchoolEnrollmentSystem();
            }
        });
    }
}
