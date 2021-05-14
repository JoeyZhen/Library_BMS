package Client;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import system.SerializedLibraryBookManagementSystem;

public class StatusPage extends Page {

    /**
     * constructor for all pages
     * creates a page
     *
     * @param clientApplication the client that this page is in
     * @param LBMS              the LBMS the client is connected to
     */
    public StatusPage(ClientApplication clientApplication, SerializedLibraryBookManagementSystem LBMS) {
        super(clientApplication, LBMS);
    }

    @Override
    public Node getRoot() {
        //create a return to home button
        HBox returnBox = new HBox();
        Button returnButton = new Button("Return to menu");
        returnButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new MenuPage(clientApplication, LBMS)));
        returnBox.getChildren().addAll(returnButton);
        returnBox.setSpacing(10);
        returnBox.setPrefWidth(250);

        BorderPane root = new BorderPane();
        root.setBottom(returnBox);
        return (root);
    }
}
