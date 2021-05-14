package Client;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import system.SerializedLibraryBookManagementSystem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents the login page ui. Creates and stores functionality and method
 * calls for buttons within this page.
 * Extends page so client application can grab ui layout from this.
 *
 * @author bendrix bailey
 * @author michael dolan
 */
public class LoginPage extends Page {
    private final int clientID;

    /**
     * Constructor for the page, supers the information necessary, and keeps track of the clientID
     * @param clientApplication client application running the program
     * @param lbms lbms structure for the system
     * @param clientID id of the client viewing page.
     */
    public LoginPage(ClientApplication clientApplication, SerializedLibraryBookManagementSystem lbms, int clientID) {
        super(clientApplication, lbms);
        this.clientID = clientID;
    }

    /**
     * This method creates the page ui and stores functionality for the various buttons.
     * Uses lambda methods to source method calls out to the client application for logging
     * in and switching ui states
     *
     * @return returns the root ui structure so clientApplication can display it
     */
    @Override
    public Node getRoot() {

        BorderPane mainRoot = new BorderPane();

        VBox root = new VBox();

        //======= Text entry creation =======//
        HBox username = new HBox();
        TextField usernameField = new TextField();
        Label usernameLabel = new Label("Username: ");
        username.getChildren().addAll(usernameLabel,usernameField);

        HBox password = new HBox();
        TextField passwordField = new TextField();
        Label passwordLabel = new Label("Password: ");
        password.getChildren().addAll(passwordLabel, passwordField);


        //======= Button Creation/Functionality =======//
        HBox buttons = new HBox();
        Button submitButton = new Button("login");
        submitButton.setOnMouseClicked(event -> submitLogin(usernameField.getCharacters(),
                passwordField.getCharacters(),
                clientID));
        buttons.getChildren().add(submitButton);
        Button disconnectButton = new Button("disconnect");
        disconnectButton.setOnMouseClicked(event -> disconnect());
        buttons.getChildren().add(disconnectButton);


        //======= Creation of page =========//
        root.getChildren().addAll(username,password, buttons);
        username.setAlignment(Pos.CENTER_RIGHT);
        password.setAlignment(Pos.CENTER_RIGHT);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        HBox centerHbox = new HBox();
        centerHbox.getChildren().add(root);
        centerHbox.setAlignment(Pos.CENTER);

        mainRoot.setCenter(centerHbox);
        centerHbox.setAlignment(Pos.CENTER);

        return mainRoot;
    }

    /**
     * This method throws out the call for disconnection of this client ID
     */
    private void disconnect() {
        clientApplication.disconnect(clientID);
    }

    /**
     * This method submits inputted information to log the user into this system
     * @param username username entered in text field
     * @param password password entered in text field
     * @param clientID client id of this logged in client
     */
    private void submitLogin(CharSequence username, CharSequence password, int clientID) {
        String request = clientID + ",login," + username + "," + password + ";";
        String response = LBMS.performRequest(request);
        String regex = clientID + ",login,success;";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response);
        if (matcher.matches()){
            clientApplication.getCurrentClient().setName(username.toString());
            clientApplication.changePage(new MenuPage(clientApplication,LBMS));
        }else{
            clientApplication.addError(response);
        }
    }
}
