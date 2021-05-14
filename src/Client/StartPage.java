package Client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import system.SerializedLibraryBookManagementSystem;

/**
 * This page represents the first page users are presented with, and gives them the option to
 * make a new account or login. Extends page so that the clientApplication can show this on startup
 *
 *
 * @author bendrixbailey
 */
public class StartPage extends Page {

    /**
     * This method creates the page, and supers all relevant information
     * @param clientApplication client application
     * @param lbms the library book management system
     */
    public StartPage(ClientApplication clientApplication, SerializedLibraryBookManagementSystem lbms) {
        super(clientApplication, lbms);
    }

    /**
     * This method creates the layout of the page. Uses one large borderpane with a V box
     * and two text labels to create title and login info.
     *
     * @return returns the main border pane for display.
     */
    @Override
    public Node getRoot() {

        //=======Middle text=========//
        VBox labelVbox = new VBox();
        Label lbmstitle = new Label("Library Book Management System");
        lbmstitle.setFont(new Font("Helvetica", 50));
        lbmstitle.setPadding(new Insets(50,50,50,50));

        Label loginInstructions = new Label("Click the '+' button to login or create a new account!" );
        loginInstructions.setFont(new Font("Helvetica", 20));

        labelVbox.getChildren().add(lbmstitle);
        labelVbox.getChildren().add(loginInstructions);
        loginInstructions.setAlignment(Pos.TOP_CENTER);
        lbmstitle.setAlignment(Pos.BOTTOM_CENTER);

        //=====Main creation=======//
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(labelVbox);
        labelVbox.setAlignment(Pos.CENTER);

        return mainBorderPane;
    }
}
