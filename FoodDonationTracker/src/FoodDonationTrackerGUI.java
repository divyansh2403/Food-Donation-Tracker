import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FoodDonationTrackerGUI extends JFrame {
    private DonationStorage storage;
    private JTextArea displayArea;

    public FoodDonationTrackerGUI() {
        storage = new DonationStorage();
        setTitle("Food Donation Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton donateButton = new JButton("Donate Food");
        donateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                donateFood();
            }
        });

        JButton checkButton = new JButton("Check Available Donations by City");
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAvailableDonations();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel.add(donateButton);
        panel.add(checkButton);
        panel.add(exitButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void donateFood() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JTextField nameField = new JTextField();
        JTextField cityField = new JTextField();
        JTextArea descriptionArea = new JTextArea(3, 20);
        JTextField quantityField = new JTextField();

        panel.add(new JLabel("Enter donor name:"));
        panel.add(nameField);
        panel.add(new JLabel("Enter donor city:"));
        panel.add(cityField);
        panel.add(new JLabel("Enter food description:"));
        panel.add(new JScrollPane(descriptionArea));
        panel.add(new JLabel("Enter quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Donate Food", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String city = cityField.getText();
            String foodDescription = descriptionArea.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            Donor donor = new Donor(name, "Unknown", city);
            Donation donation = new Donation(foodDescription, quantity, new Date(), donor);

            storage.saveDonation(donation);
            displayArea.append("Donation saved successfully!\n");
        }
    }

    private void checkAvailableDonations() {
        String city = JOptionPane.showInputDialog(this, "Enter city to search for donations:");

        List<Donation> donations = storage.getDonations().stream()
                .filter(d -> d.getDonor().getCity().equalsIgnoreCase(city) && !d.isClaimed())
                .collect(Collectors.toList());

        if (donations.isEmpty()) {
            displayArea.append("No available donations in " + city + "\n");
        } else {
            String[] columnNames = {"Description", "Quantity", "Date", "Donor", "Claim"};
            Object[][] data = new Object[donations.size()][5];

            for (int i = 0; i < donations.size(); i++) {
                Donation donation = donations.get(i);
                data[i][0] = donation.getDescription();
                data[i][1] = donation.getQuantity();
                data[i][2] = donation.getDate();
                data[i][3] = donation.getDonor().getName();
                data[i][4] = "Claim";
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 4; // Only the "Claim" button is editable
                }
            };

            JTable table = new JTable(model);
            table.getColumn("Claim").setCellRenderer(new ButtonRenderer());
            table.getColumn("Claim").setCellEditor(new ButtonEditor(new JCheckBox(), storage, displayArea, table));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(500, 200));

            JOptionPane.showMessageDialog(this, scrollPane, "Available Donations in " + city, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FoodDonationTrackerGUI().setVisible(true);
            }
        });
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private DonationStorage storage;
    private JTextArea displayArea;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox, DonationStorage storage, JTextArea displayArea, JTable table) {
        super(checkBox);
        this.storage = storage;
        this.displayArea = displayArea;
        this.table = table;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int row = table.getSelectedRow();
            Donation donation = storage.getDonations().get(row);
            storage.claimDonation(donation);
            displayArea.append("Donation claimed successfully!\n");
            ((DefaultTableModel) table.getModel()).removeRow(row);
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}