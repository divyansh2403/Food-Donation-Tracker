import java.util.Date;

public class Donation {
    private int donationId;
    private String description;
    private int quantity;
    private Date date;
    private Donor donor;
    private boolean claimed;

    // Constructor without donationId (used for creating new donations)
    public Donation(String description, int quantity, Date date, Donor donor) {
        this.description = description;
        this.quantity = quantity;
        this.date = date;
        this.donor = donor;
        this.claimed = false;
    }

    // Constructor with donationId (used for retrieving donations from the database)
    public Donation(int donationId, String description, int quantity, Date date, Donor donor) {
        this.donationId = donationId;
        this.description = description;
        this.quantity = quantity;
        this.date = date;
        this.donor = donor;
        this.claimed = false;
    }

    public int getDonationId() {
        return donationId;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getDate() {
        return date;
    }

    public Donor getDonor() {
        return donor;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "description='" + description + '\'' +
                ", quantity=" + quantity +
                ", date=" + date +
                ", donor=" + donor.getName() +
                ", claimed=" + claimed +
                '}';
    }
}
