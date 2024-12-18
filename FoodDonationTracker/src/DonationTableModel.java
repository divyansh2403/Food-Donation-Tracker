import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DonationTableModel extends AbstractTableModel {
    private List<Donation> donations;
    private final String[] columnNames = {"Description", "Quantity", "City", "Claim"};

    public DonationTableModel(List<Donation> donations) {
        this.donations = donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }

    @Override
    public int getRowCount() {
        return donations.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Donation donation = donations.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return donation.getDescription();
            case 1:
                return donation.getQuantity();
            case 2:
                return donation.getDonor().getCity();
            case 3:
                return "Claim";
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3;
    }
}
