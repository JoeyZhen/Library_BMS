package Client;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import system.SerializedLibraryBookManagementSystem;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class allows users to register a new visitor. Uses javafx and lambda functions to give commands to the lbms.
 * Creates page layout necessary for interacting with lbms system
 *
 * @author michael dolan
 * @author bendrix bailey
 */
public class RegisterVisitorPage extends Page {

    /**
     * constructor for all pages
     * creates a page
     *
     * @param clientApplication the client that this page is in
     * @param LBMS              the LBMS the client is connected to
     */
    public RegisterVisitorPage(ClientApplication clientApplication, SerializedLibraryBookManagementSystem LBMS) {
        super(clientApplication, LBMS);
    }

    /**
     * This method does the actual construction of the page. Gives buttons functionality to call method below
     * that sends information to the lbms
     *
     * @return returns the borderpane that will be displayed on the page
     */
    @Override
    public Node getRoot() {
        BorderPane rootPane = new BorderPane();
        GridPane root = new GridPane();

        HBox firstName = new HBox();
        TextField nameField = new TextField();
        Label nameLabel = new Label("First name: ");
        firstName.getChildren().addAll(nameLabel,nameField);

        HBox lastName = new HBox();
        TextField lastNameField = new TextField();
        Label lastNameLabel = new Label("Last name: ");
        lastName.getChildren().addAll(lastNameLabel, lastNameField);

        HBox address = new HBox();
        TextField addressField = new TextField();
        Label addressLabel = new Label("Address: ");
        address.getChildren().addAll(addressLabel, addressField);

        HBox phoneNumber = new HBox();
        TextField phoneField = new TextField();
        Label phoneLabel = new Label("Phone number: ");
        phoneNumber.getChildren().addAll(phoneLabel, phoneField);



        HBox buttonBox = new HBox();
        Button submitButton = new Button("Register");
        submitButton.setOnMouseClicked(event -> submitRegister(nameField.getCharacters(),
                                                                lastNameField.getCharacters(),
                                                                addressField.getCharacters(),
                                                                phoneField.getCharacters(),
                                                                clientApplication.currentClientID()));
        buttonBox.getChildren().add(submitButton);

        //create a return to home button
        Button returnButton = new Button("Return to menu");
        returnButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new MenuPage(clientApplication, LBMS)));
        buttonBox.getChildren().add(returnButton);
        buttonBox.setSpacing(10);
        buttonBox.setPrefWidth(250);

        root.addColumn(0, firstName,lastName,address,phoneNumber,buttonBox);
        rootPane.setCenter(root);
        root.setAlignment(Pos.CENTER);

        return rootPane;
    }

    /**
     * This method controls the submission of the registry data to the lbms. Because the lbms works using string input,
     * this method creates a string to send to the lbms with the data inputted by the user. This method also
     * handles errors if the user inputted information incorrectly, or with incorrect format. Called by the lambda
     * methods within this class.
     *
     * @param firstNameCharacters the string characters for the first name of the visitor
     * @param lastNameFieldCharacters characters inputted for the last name of the visitor
     * @param addressFieldCharacters characters inputted for the address of the visitor
     * @param phoneFieldCharacters characters inputted for the phone number of the bisitor
     * @param clientID the id of the current application running this program.
     */
    private void submitRegister(CharSequence firstNameCharacters, CharSequence lastNameFieldCharacters, CharSequence addressFieldCharacters, CharSequence phoneFieldCharacters, int clientID) {
        String request = clientID + ",register," + firstNameCharacters.toString() + "," + lastNameFieldCharacters.toString() + "," + addressFieldCharacters.toString() + "," + phoneFieldCharacters.toString() + ";";
        String response = LBMS.performRequest(request);
        // should be in the format "register,visitor ID,registration date;"
        String regex = "(.+),([0-9]{10}),(.+);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response);
        if (matcher.matches()){
            String id = matcher.group(2);
            clientApplication.changePage(new CreateAccountPage(clientApplication, LBMS, id));
        }else{
            clientApplication.addError(response);
        }
    }
}
