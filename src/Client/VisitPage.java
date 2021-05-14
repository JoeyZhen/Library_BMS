package Client;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import system.SerializedLibraryBookManagementSystem;

/**
 * This class represents the gui functionality for logging a visit to the library. Allows
 * beginning of the visit and ending of a current visit. Also shows a list of all visitors who are mid-visit
 *
 * @author michael dolan
 * @author bendrix bailey
 */
public class VisitPage extends Page {

    /**
     * constructor for all pages
     * creates a page
     *
     * @param clientApplication the client that this page is in
     * @param LBMS              the LBMS the client is connected to
     */
    public VisitPage(ClientApplication clientApplication, SerializedLibraryBookManagementSystem LBMS) {
        super(clientApplication, LBMS);
    }

    /**
     * This method creates the display for the visit page. uses lambda methods to send method calls to other functions
     * within this class
     *
     * @return root pane to be displayed
     */
    @Override
    public Node getRoot() {

        //begin visit
        BorderPane beginRoot = new BorderPane();
        Label visitLabel = new Label("Starting a visit:");
        Label visitLabel2 = new Label("Enter visitor ID");
        TextField idInput = new TextField();
        VBox beginVisitLabels = new VBox();
        visitLabel.setFont(new Font("Helvetica", 25));
        visitLabel2.setFont(new Font("Helvetica", 25));
        beginVisitLabels.getChildren().add(visitLabel);
        beginVisitLabels.getChildren().add(visitLabel2);
        visitLabel.setAlignment(Pos.CENTER);
        visitLabel2.setAlignment(Pos.CENTER);


        Button startVisitButton = new Button("Start Visit");
        startVisitButton.setOnMouseClicked(mouseEvent -> this.startVisit(idInput.getCharacters()));

        VBox visitVbox = new VBox();
        visitVbox.getChildren().add(beginVisitLabels);
        visitVbox.getChildren().add(idInput);
        visitVbox.getChildren().add(startVisitButton);
        visitVbox.setMaxWidth(250);

        beginRoot.setCenter(visitVbox);
        visitVbox.setAlignment(Pos.CENTER);


        //end visit
        BorderPane endRoot = new BorderPane();
        VBox endVisitLables = new VBox();
        Label endVisitLabel = new Label("Ending a visit:");
        Label endVisitLabel2 = new Label("Enter visitor ID");
        endVisitLabel.setFont(new Font("Helvetica", 25));
        endVisitLabel2.setFont(new Font("Helvetica", 25));
        endVisitLables.getChildren().add(endVisitLabel);
        endVisitLables.getChildren().add(endVisitLabel2);
        endVisitLabel.setAlignment(Pos.CENTER);
        endVisitLabel2.setAlignment(Pos.CENTER);

        TextField endIdInput = new TextField();
        visitLabel.setFont(new Font("Helvetica", 25));
        Button endVisitButton = new Button("End Visit");
        endVisitButton.setOnMouseClicked(mouseEvent -> this.endVisit(endIdInput.getCharacters()));

        VBox endVisitVbox = new VBox();
        endVisitVbox.getChildren().add(endVisitLables);
        endVisitVbox.getChildren().add(endIdInput);
        endVisitVbox.getChildren().add(endVisitButton);
        endVisitVbox.setMaxWidth(250);

        endRoot.setCenter(endVisitVbox);
        endVisitVbox.setAlignment(Pos.CENTER);


        //create a return to home button
        HBox returnBox = new HBox();
        Button returnButton = new Button("Return to menu");
        returnButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new MenuPage(clientApplication, LBMS)));
        returnBox.getChildren().addAll(returnButton);
        returnBox.setSpacing(10);
        returnBox.setPrefWidth(250);
        returnButton.setAlignment(Pos.CENTER);

        //putting it all together
        Label titleLabel = new Label("Visit Logger");
        titleLabel.setFont(new Font("Helvetica", 30));
        titleLabel.setAlignment(Pos.CENTER);

        VBox rootVbox = new VBox();
        BorderPane root = new BorderPane();
        root.setCenter(rootVbox);

        rootVbox.setAlignment(Pos.CENTER);
        rootVbox.getChildren().add(titleLabel);
        rootVbox.getChildren().add(beginRoot);
        rootVbox.getChildren().add(endRoot);
        rootVbox.getChildren().add(returnBox);
        returnBox.setAlignment(Pos.CENTER);

        root.setCenter(rootVbox);

        return (root);

    }

    /**
     * This method ends a visit with a given user id inputted by the text box.
     * Does error checking with the inputs.
     * Relays user id to lbms and if it succeeds, ends a visit.
     *
     * @param userId user id to end visit for
     */
    private void endVisit(CharSequence userId){
        if(userId.length() < 10){
            clientApplication.addError("User ID must be 10 characters long");
        }

        String request = "depart," + userId + ";";
        String response = LBMS.performRequest(request);

        if(response.contains("invalid-id")){
            clientApplication.addError("User id is not valid/has not been registered");
        }else{
            clientApplication.addError("Vist for user :"+ userId + " has successfully ended.");
        }
    }

    /**
     * This method begins a visit with a given user id inputted by the text box.
     * Does coherent error checking with the inputs.
     * Relays user id to lbms and if it succeeds, begins a visit.
     *
     * @param userId user id string from the text field.
     */
    private void startVisit(CharSequence userId){
        if(userId.length() < 10){
            clientApplication.addError("User ID must be 10 characters long");
        }


        String request = "arrive," + userId + ";";
        String response = LBMS.performRequest(request);

        if(response.contains("duplicate")){
            clientApplication.addError("User already is visiting library!");
        }else if(response.contains("invalid-id")){
            clientApplication.addError("User id is not valid/has not been registered");
        }else{
            clientApplication.addError("Vist for user :"+ userId + " has successfully begun.");
        }
    }
}
