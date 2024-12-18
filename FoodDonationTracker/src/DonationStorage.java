import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationStorage {
    private Connection connection;

    public DonationStorage() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_donations", "root", "aayush");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Donation> getDonations() {
        List<Donation> donations = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM donations");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                int quantity = resultSet.getInt("quantity");
                Date date = resultSet.getDate("date");
                boolean claimed = resultSet.getBoolean("claimed");

                Donor donor = new Donor(resultSet.getString("donor_name"), resultSet.getString("donor_contact"), resultSet.getString("donor_city"));
                Donation donation = new Donation(id, description, quantity, date, donor);
                donation.setClaimed(claimed);
                donations.add(donation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donations;
    }

    // Add the claimDonation method to update the claimed status
    public void claimDonation(Donation donation) {
        try {
            String updateQuery = "UPDATE donations SET claimed = true WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, donation.getDonationId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveDonation(Donation donation) {
        try {
            String insertQuery = "INSERT INTO donations (description, quantity, date, donor_name, donor_contact, donor_city, claimed) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, donation.getDescription());
            preparedStatement.setInt(2, donation.getQuantity());
            preparedStatement.setDate(3, new java.sql.Date(donation.getDate().getTime()));
            preparedStatement.setString(4, donation.getDonor().getName());
            preparedStatement.setString(5, donation.getDonor().getContact());
            preparedStatement.setString(6, donation.getDonor().getCity());
            preparedStatement.setBoolean(7, donation.isClaimed());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDonations(List<Donation> donations) {
    }
}
