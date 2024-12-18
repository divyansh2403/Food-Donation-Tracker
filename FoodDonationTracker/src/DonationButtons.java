import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DonationButtons extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private JButton button;
    private Donation donation;
    private DonationStorage storage;
    private JTable table;

    public DonationButtons(JTable table, DonationStorage storage) {
        this.storage = storage;
        this.table = table;
        button = new JButton("Claim");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                claimDonation();
            }
        });
    }

    private void claimDonation() {
        if (donation != null) {
            storage.claimDonation(donation);  // Call claimDonation from DonationStorage
            JOptionPane.showMessageDialog(null, "Donation claimed successfully!");
            // Refresh the table data
            ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
        }
    }

    @Override
    public Object getCellEditorValue() {
        return donation;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        donation = (Donation) value;
        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        donation = (Donation) value;
        return button;
    }
}