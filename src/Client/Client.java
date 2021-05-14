package Client;

public class Client {
    private int clientID;
    private String firstName;
    private String lastName;
    private Page currentPage;
    private String username;
    private int VisitorID;

    public Client(int ID) {
        this.clientID = ID;
    }

    public String getName() {
        if (firstName == null || lastName == null){
            return Integer.toString(clientID);
        }else{
            return firstName + " " + lastName;
        }
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setName(String username) {
        this.username = username;
    }

    public Page getCurrentPage(){
        return currentPage;
    }

    public void changePage(Page page) {
        this.currentPage = page;
    }

    public int getVisitorID() {
        return VisitorID;
    }

    public void setVisitorID(int visitorID) {
        VisitorID = visitorID;
    }
}
