package Client;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import system.SerializedLibraryBookManagementSystem;


/**
 * This class handles the changing of time of the lbms for testing purposes. It is an extension of page, so the
 * client application can grab the page layout and display it for the user. Also holds a method to relay user inputted
 * information back to the lbms.
 *
 * @author michael dolan
 * @author bendrix bailey
 */
public class TimePage extends Page {

    /**
     * constructor for all pages
     * creates a page
     *
     * @param clientApplication the client that this page is in
     * @param LBMS              the LBMS the client is connected to
     */
    public TimePage(ClientApplication clientApplication, SerializedLibraryBookManagementSystem LBMS) {
        super(clientApplication, LBMS);
    }

    /**
     * This method creates the page for changing time. Uses javafx to display buttons and text. Parent layout is a
     * borderpane
     *
     * @return returns the graphic display to show on the ui page.
     */
    @Override
    public Node getRoot() {
        //create an element to forward time
        HBox forwardTimeBox = new HBox();
        Button forwardTimeButton = new Button("Advance Time");
        TextField forwardAmountDays = new TextField("Days");
        TextField forwardAmountHours = new TextField("Hours");
        forwardTimeButton.setOnMouseClicked(mouseEvent -> this.forwardTime(forwardAmountDays.getCharacters(), forwardAmountHours.getCharacters()));
        forwardTimeBox.getChildren().addAll(forwardTimeButton, forwardAmountDays, forwardAmountHours);
        forwardTimeBox.setSpacing(10);
        //forwardTimeBox.setPrefWidth(250);

        //create an element to get the current time
        HBox currentTimeBox = new HBox();
        Button getTimeButton = new Button("Get Current Time");
        Label currentTimeLabel = new Label("Current Time:");
        String currentTimeText = "------";
        getTimeButton.setOnMouseClicked(mouseEvent -> this.getCurrentTime(currentTimeText));
        Text currentTime = new Text(currentTimeText);
        currentTimeBox.getChildren().addAll(currentTimeLabel, currentTime, getTimeButton);
        currentTimeBox.setSpacing(10);
        currentTimeBox.setPrefWidth(250);

        //create a return to home button
        HBox returnBox = new HBox();
        Button returnButton = new Button("Return to menu");
        returnButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new MenuPage(clientApplication, LBMS)));
        returnBox.getChildren().addAll(returnButton);
        returnBox.setSpacing(10);
        returnBox.setPrefWidth(250);


        GridPane center = new GridPane();
        center.add(forwardTimeBox,0,0);
        center.add(currentTimeBox,0,1);
        center.add(returnBox, 0, 2);
        center.setVgap(10);

        BorderPane root = new BorderPane();
        root.setCenter(center);
        center.setAlignment(Pos.CENTER);
        return (root);
    }

    /**
     * This method allows for the user to grab the current time from the lbms and display it.
     *
     * @param currentTimeText the text field for the current time.
     * @return returns the response from the lbms.
     */
    private String getCurrentTime(String currentTimeText) {
        String response = LBMS.performRequest("datetime;");
        System.out.println(response);
        currentTimeText = response;
        return response;
    }

    /**
     * This method sends the information to the server to progress time.
     *
     * @param daysSequence days to progress
     * @param hourSequence hours to progress
     */
    private void forwardTime(CharSequence daysSequence, CharSequence hourSequence) {
        String daysString = daysSequence.toString();
        String hoursString = hourSequence.toString();
        int days = 0;
        int hours = 0;
        boolean invalid = false;
        try{
            days = Integer.parseInt(daysString);
        }catch (Exception e){
            invalid = true;
            System.out.println("could not parse day");
        }
        try {
            hours = Integer.parseInt(hoursString);
        }catch (Exception e){
            invalid = true;
            System.out.println("could not parse hour");
        }

        if (!invalid) { //if the strings are correctly formatted
            System.out.println("advance," + days + "," + hours + ";");
            String request = "advance," + daysString + "[," + hoursString + "];";
            String response = LBMS.performRequest(request);
            if (response.contains("success")) { //if the lbms accepted the command
                clientApplication.addError("Successfully advanced time!");
            } else if (response.contains("invalid-number-of-days")) { //if number of days was wrong format
                clientApplication.addError("Invalid number of days entered!");
            } else { //if number of hours was invalid
                clientApplication.addError("Invalid number of hours entered!");
            }
        }
    }
}
