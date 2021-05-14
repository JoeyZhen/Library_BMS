package Client;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import system.SerializedLibraryBookManagementSystem;

public class MenuPage extends Page {

    /**
     * constructor for all pages
     * creates a page
     *
     * @param clientApplication the client that this page is in
     * @param LBMS              the LBMS the client is connected to
     */
    public MenuPage(ClientApplication clientApplication, SerializedLibraryBookManagementSystem LBMS) {
        super(clientApplication, LBMS);
    }

    /**
     * This method creates and sets the layout for all the buttons for the main menu.
     *
     * @return returns the borderpane layout so the client can display it.
     */
    @Override
    public Node getRoot() {

        //create outer boarder pane
        BorderPane outerPane = new BorderPane();

        //create menu text
        Text menuText = new Text("Welcome\r\nSelect a menu");
        menuText.setFont(Font.font("Verdana", 20));
        HBox textPane = new HBox();
        textPane.getChildren().add(menuText);
        outerPane.setTop(textPane);
        textPane.setAlignment(Pos.CENTER);

        //create center grid pane of options
        GridPane centerPane = new GridPane();
        outerPane.setCenter(centerPane);
        centerPane.setAlignment(Pos.CENTER);

        //create button for time page
        HBox timeBox = new HBox();
        Button timeButton = new Button("Time");
        timeButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new TimePage(clientApplication, LBMS)));
        timeBox.getChildren().addAll(timeButton);
        timeBox.setAlignment(Pos.CENTER);


        //create button for visit page
        HBox visitBox = new HBox();
        Button visitButton = new Button("Visit");
        visitButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new VisitPage(clientApplication, LBMS)));
        visitBox.getChildren().addAll(visitButton);
        visitBox.setAlignment(Pos.CENTER);

        //create button for store page
        HBox storeBox = new HBox();
        Button storeButton = new Button("Store");
        storeButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new StorePage(clientApplication, LBMS)));
        storeBox.getChildren().addAll(storeButton);
        storeBox.setAlignment(Pos.CENTER);

        //create button for borrow page
        HBox borrowBox = new HBox();
        Button borrowButton = new Button("Borrow");
        borrowButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new BorrowPage(clientApplication, LBMS)));
        borrowBox.getChildren().addAll(borrowButton);
        borrowBox.setAlignment(Pos.CENTER);

        //create button for Status page
        HBox statusBox = new HBox();
        Button statusButton = new Button("Status");
        statusButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new StatusPage(clientApplication, LBMS)));
        statusBox.getChildren().addAll(statusButton);
        statusBox.setAlignment(Pos.CENTER);

        //create logout button
        HBox logoutBox = new HBox();
        Button logoutButton = new Button("Logout");
        logoutButton.setOnMouseClicked(mouseEvent -> clientApplication.logout());
        logoutBox.getChildren().addAll(logoutButton);
        logoutBox.setAlignment(Pos.CENTER);

        //create button for Register page
        HBox registerBox = new HBox();
        Button registerButton = new Button("Register new user");
        registerButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new RegisterVisitorPage(clientApplication, LBMS)));
        registerBox.getChildren().addAll(registerButton);
        registerBox.setAlignment(Pos.CENTER);

        //add buttons to gridpane
        centerPane.add(timeBox,0,1);
        centerPane.add(visitBox,0,2);
        centerPane.add(storeBox,0,3);
        centerPane.add(borrowBox,0,4);
        centerPane.add(statusBox,0,5);
        centerPane.add(logoutBox,0,6);
        centerPane.add(registerBox,0,0);
        centerPane.setVgap(10);
        centerPane.setHgap(10);
        centerPane.setAlignment(Pos.CENTER);

        return (outerPane);
    }

}
