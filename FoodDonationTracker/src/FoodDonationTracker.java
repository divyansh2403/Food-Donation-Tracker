import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class FoodDonationTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DonationStorage storage = new DonationStorage();

        while (true) {
            System.out.println("1. Donate Food");
            System.out.println("2. Check Available Donations by City");
            System.out.println("3. Claim Donation");
            System.out.println("4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            switch (choice) {
                case 1:
                    donateFood(scanner, storage);
                    break;
                case 2:
                    checkAvailableDonations(scanner, storage);
                    break;
                case 3:
                    claimDonation(scanner, storage);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void donateFood(Scanner scanner, DonationStorage storage) {
        System.out.println("Enter donor name:");
        String name = scanner.nextLine();
        System.out.println("Enter donor city:");
        String city = scanner.nextLine();
        System.out.println("Enter food description:");
        String foodDescription = scanner.nextLine();
        System.out.println("Enter quantity:");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Donor donor = new Donor(name, "Unknown", city);
        Donation donation = new Donation(foodDescription, quantity, new Date(), donor);

        storage.saveDonation(donation);
        System.out.println("Donation saved successfully!");
    }

    private static void checkAvailableDonations(Scanner scanner, DonationStorage storage) {
        System.out.println("Enter city to search for donations:");
        String city = scanner.nextLine();

        List<Donation> donations = storage.getDonations().stream()
                .filter(d -> d.getDonor().getCity().equalsIgnoreCase(city) && !d.isClaimed())
                .collect(Collectors.toList());

        if (donations.isEmpty()) {
            System.out.println("No available donations in " + city);
        } else {
            donations.forEach(System.out::println);
        }
    }

    private static void claimDonation(Scanner scanner, DonationStorage storage) {
        List<Donation> donations = storage.getDonations().stream()
                .filter(d -> !d.isClaimed())
                .collect(Collectors.toList());

        if (donations.isEmpty()) {
            System.out.println("No donations available to claim.");
            return;
        }

        System.out.println("Available Donations:");
        for (int i = 0; i < donations.size(); i++) {
            System.out.println(i + 1 + ". " + donations.get(i));
        }

        System.out.println("Enter the number of the donation you want to claim:");
        int index = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (index > 0 && index <= donations.size()) {
            donations.get(index - 1).setClaimed(true);
            storage.updateDonations(donations);
            System.out.println("Donation claimed successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }
}