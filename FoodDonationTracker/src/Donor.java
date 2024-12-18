public class Donor {
    private String name;
    private String contact;
    private String city;

    public Donor(String name, String contact, String city) {
        this.name = name;
        this.contact = contact;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getCity() {
        return city;
    }
}
