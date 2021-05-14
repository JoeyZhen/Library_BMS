package Client;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import system.SerializedLibraryBookManagementSystem;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientBar extends Page{

    private ArrayList<Client> clients = new ArrayList <Client>();

    /**
     * constructor for all pages
     * creates a page
     *
     * @param clientApplication the client that this page is in
     * @param LBMS              the LBMS the client is connected to
     */
    public ClientBar(ClientApplication clientApplication, SerializedLibraryBookManagementSystem LBMS) {
        super(clientApplication, LBMS);
    }

    @Override
    public Node getRoot() {
        HBox root = new HBox();
        ArrayList<Button> clientButtons = new ArrayList <>();
        for (Client client: clients){
            Button clientButton = new Button(client.getName());
            clientButton.setOnMouseClicked(event -> clientApplication.changeClient(client.getClientID()));
            clientButtons.add(clientButton);

        }
        root.getChildren().addAll(clientButtons);

        Button addClientButton = new Button("+");
        addClientButton.setOnMouseClicked(event -> addClient());

        root.getChildren().add(addClientButton);
        return root;
    }

    /**
     * adds a client to the the client bar
     */
    private void addClient() {
        String response = LBMS.performRequest("connect;");
        String regex = "connect,([0-9]+);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response);
        if(matcher.matches()){
            int ID = Integer.parseInt(matcher.group(1));
            Client current = new Client(ID);
            clients.add(current);
            clientApplication.changeClient(ID);
            clientApplication.changePage(new LoginPage(clientApplication,LBMS, ID));
            clientApplication.refresh();
        }else{
            clientApplication.addError(response);
        }
    }

    /**
     * logs the current client to the client bar
     */
    public void logOut() {
        int id = clientApplication.getCurrentClient().getClientID();
        String response = LBMS.performRequest(id+",logout;");
        clientApplication.changePage(new LoginPage(clientApplication,LBMS,id));

    }

    /**
     * gets a specific client by ID
     * @param clientID the id of the client you want to get
     * @return the client that has that ID
     */
    public Client getClient(int clientID) {
        for (int i = 0; i < clients.size(); i++){
            if (clients.get(i).getClientID() == clientID){
                return clients.get(i);
            }
        }
        return null;
    }

    /**
     * disconnects a client from the server
     * @param id the ID of the client you want to disconnect
     */
    public void disconnect(int id) {

        LBMS.performRequest(id + ",disconnect;");

        for (int i = 0; i < clients.size(); i++){
            if (clients.get(i).getClientID() == id){
                clients.remove(clients.get(i));
            }
        }
        if (clients.isEmpty()){
            clientApplication.changePage(new StartPage(clientApplication, LBMS));
        }else{
            clientApplication.changeClient(clients.get(0).getClientID());
        }
        clientApplication.refresh();
    }
}
