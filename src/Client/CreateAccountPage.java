package Client;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import system.SerializedLibraryBookManagementSystem;


public class CreateAccountPage extends Page {
    private final String visitorID;

    /**
     * constructor for all pages
     * creates a page
     * @param clientApplication the client that this page is in
     * @param LBMS              the LBMS the client is connected to
     * @param id
     */
    public CreateAccountPage(ClientApplication clientApplication, SerializedLibraryBookManagementSystem LBMS, String id) {
        super(clientApplication, LBMS);
        this.visitorID = id;
    }

    @Override
    public Node getRoot() {
        //client ID,create,username,password,role,visitor ID;

        int clientID = clientApplication.getCurrentClient().getClientID();

        BorderPane root = new BorderPane();

        HBox username = new HBox();
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        username.getChildren().addAll(usernameLabel, usernameField);

        HBox password = new HBox();
        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        password.getChildren().addAll(passwordLabel,passwordField);

        HBox role = new HBox();

        HBox visitor = new HBox();
        Button submitVisitorButton = new Button("Visitor");
        submitVisitorButton.setOnMouseClicked(mouseEvent -> createAccount(clientID, usernameField.getCharacters(), passwordField.getCharacters(), "visitor"));
        visitor.getChildren().add(submitVisitorButton);

        HBox employee = new HBox();
        Button submitEmployeeButton = new Button("Employee");
        submitEmployeeButton.setOnMouseClicked(mouseEvent -> createAccount(clientID, usernameField.getCharacters(), passwordField.getCharacters(), "employee"));
        employee.getChildren().add(submitEmployeeButton);

        role.getChildren().addAll(visitor,employee);

        VBox accountCreation = new VBox();
        accountCreation.getChildren().addAll(username, password, role);
        root.setCenter(accountCreation);

        //create a return to home button
        HBox returnBox = new HBox();
        Button returnButton = new Button("Return to menu");
        returnButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new MenuPage(clientApplication, LBMS)));
        returnBox.getChildren().addAll(returnButton);
        returnBox.setSpacing(10);
        returnBox.setPrefWidth(250);

        root.setBottom(returnBox);

        return root;
    }

    private void createAccount(int clientID, CharSequence usernameChars, CharSequence passwordChars, String role) {
        String username = usernameChars.toString();
        String password = passwordChars.toString();
        String response = LBMS.performRequest(clientID + ",create," + username + "," + password + "," + role + "," + visitorID + ";");
        String expected = clientID + ",create,success;";
        if (response.equals(expected)){
            clientApplication.changePage(new MenuPage(clientApplication, LBMS));
        }else {
            clientApplication.addError(response);
        }
    }
}
