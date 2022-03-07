package eth.project.iss.bottled.models;

public class User {
    private String publicAddress;

    public User() {
    }

    public User(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    /**
     * @return the publicAddress
     */
    public String getPublicAddress() {
        return publicAddress;
    }

}
