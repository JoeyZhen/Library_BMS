package Client;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import system.SerializedLibraryBookManagementSystem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StorePage extends Page {


    private final VBox searchResults;

    /**
     * constructor for all pages
     * creates a page
     *
     * @param clientApplication the client that this page is in
     * @param LBMS              the LBMS the client is connected to
     */
    public StorePage(ClientApplication clientApplication, SerializedLibraryBookManagementSystem LBMS) {
        super(clientApplication, LBMS);
        this.searchResults = new VBox();
        searchResults.setPrefHeight(500);
    }

    @Override
    public Node getRoot() {

        /*
         * Create book search section
         */
        BorderPane bookSearch = new BorderPane();
        VBox parameters = new VBox();

        HBox title = new HBox();
        TextField titleField = new TextField("*");
        Label titleLabel = new Label("Title");
        title.getChildren().addAll(titleLabel, titleField);

        HBox authors = new HBox();
        TextField authorsField = new TextField("*");
        Label authorsLabel = new Label("Authors (Separated by commas)");
        authors.getChildren().addAll(authorsLabel, authorsField);

        HBox isbn = new HBox();
        TextField isbnField = new TextField("*");
        Label isbnLabel = new Label("ISBN");
        isbn.getChildren().addAll(isbnLabel, isbnField);

        HBox publisher = new HBox();
        TextField publisherField = new TextField("*");
        Label publisherLabel = new Label("Publisher");
        publisher.getChildren().addAll(publisherLabel, publisherField);

        HBox sorting = new HBox();
        ToggleGroup sortType = new ToggleGroup();
        RadioButton titleSort = new RadioButton("Title");
        titleSort.setToggleGroup(sortType);
        titleSort.setSelected(true);
        RadioButton publishDate = new RadioButton("Publish-date");
        publishDate.setToggleGroup(sortType);
        RadioButton  bookStatus = new RadioButton(" Book-status");
        bookStatus.setToggleGroup(sortType);
        sorting.getChildren().addAll(titleSort,publishDate,bookStatus);

        Button search = new Button("search");
        search.setOnMouseClicked(event -> searchForBook(titleField.getCharacters(),
                authorsField.getCharacters(),
                isbnField.getCharacters(),
                publisherField.getCharacters(),
                titleSort,publishDate,bookStatus));

        parameters.getChildren().addAll(title,authors,isbn,publisher, sorting, search);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(searchResults);

        bookSearch.setTop(parameters);
        bookSearch.setBottom(scrollPane);


        /*
         * Create Buy book section
         */

        HBox buyBook = new HBox();
        Label buyBookLabel = new Label("Enter book ID or select a book with the book search");
        TextField buyBookFiled = new TextField();
        Label quantityLabel = new Label("how many");
        TextField quantityField = new TextField();

        Button buyBookButton = new Button("Buy Book");
        buyBookButton.setOnMouseClicked(Event -> Buy(buyBookFiled.getCharacters(), quantityField.getCharacters()));
        buyBook.getChildren().addAll(buyBookLabel, buyBookFiled,quantityLabel, quantityField, buyBookButton);


        //create a return to home button
        HBox returnBox = new HBox();
        Button returnButton = new Button("Return to menu");
        returnButton.setOnMouseClicked(mouseEvent -> clientApplication.changePage(new MenuPage(clientApplication, LBMS)));
        returnBox.getChildren().addAll(returnButton);
        returnBox.setSpacing(10);
        returnBox.setPrefWidth(250);

        BorderPane root = new BorderPane();
        root.setTop(buyBook);
        root.setBottom(returnBox);
        root.setCenter(bookSearch);
        return (root);
    }




    private void searchForBook(CharSequence title, CharSequence authors, CharSequence isbn, CharSequence publisher, RadioButton titleSort, RadioButton publishDate, RadioButton bookStatus) {
        String sortType;
        if (titleSort.isSelected()){
            sortType = "title";
        }else if (publishDate.isSelected()){
            sortType = "publish-date";
        }else {
            sortType = " book-status";
        }

        String request = clientApplication.currentClientID() + ",info," + title +",{" + authors + "}," + isbn + "," + publisher + "," + sortType + ";";
        System.out.println(request);
        String response = LBMS.performRequest(request);
        System.out.println(response);

        searchResults.getChildren().clear();

        if (response.contains("\n")){
            String[] books = response.split("\n");
            String regex = "([0-9]+),([0-9]+),([0-9]+),\"(.*)\",{(.*)},([0-9]{4}-[0-9]{2}-[0-9]{2}),";
            Pattern pattern = Pattern.compile(regex);

            ToggleGroup toggleGroup = new ToggleGroup();
            for(String book: books){
                Matcher matcher = pattern.matcher(book);

                HBox bookBox = new HBox();

                CheckBox radioButton = new CheckBox();

                Text copiesAvailable = new Text(matcher.group(1));
                Text bookID = new Text(matcher.group(2));
                Text ISBN = new Text(matcher.group(3));
                Text Name = new Text(matcher.group(4));
                Text Authors = new Text(matcher.group(5));
                Text date = new Text(matcher.group(6));

                bookBox.getChildren().addAll(radioButton,copiesAvailable,bookID,ISBN,Name,Authors,date);
                searchResults.getChildren().add(bookBox);

            }
        }else{
            HBox noResults = new HBox();
            noResults.getChildren().add(new Text("No results"));
            searchResults.getChildren().add(noResults);
        }
        clientApplication.refresh();

    }


    private void Buy(CharSequence characters, CharSequence quantityFieldCharacters) {
        String bookID = characters.toString();
        int quality = 0;
        try{
            quality = Integer.parseInt(quantityFieldCharacters.toString());
        }catch (Exception e){
            clientApplication.addError("not a number");
        }

        //get if the book search system has been used;
        for (Node node: searchResults.getChildren()){
            HBox book = (HBox) node;
            CheckBox  button = (CheckBox ) book.getChildren().get(0);
            if (button.isSelected()){
                Text bookIDText = (Text) book.getChildren().get(1);
                bookID += "," + bookIDText.getText();
            }
        }

        String request = clientApplication.getCurrentClient().getClientID() + ",buy," + quality + "," + bookID + ";";
    }
}
