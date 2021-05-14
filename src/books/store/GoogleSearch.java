package books.store;

import books.Author;
import books.Book;
import books.Books;
import books.Publisher;
import com.google.gson.Gson;
import time.Date;
import user.Name;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Performs searching on the Google book store.
 *
 * @author Zachary Cook
 */
public class GoogleSearch implements StoreSearchService,Serializable {
    private Books localStoreBooks;
    private Books sharedBooks;

    /**
     * Creates the book store.
     *
     * @param books the main list of books.
     */
    public GoogleSearch(Books books) {
        this.localStoreBooks = new Books();
        this.sharedBooks = books;
    }

    /**
     * Adds a book to the book store.
     *
     * @param isbn the id of the book.
     * @param name the name of the book.
     * @param authors the authors of the book.
     * @param publisher the publisher of the book.
     * @param date the date the book was published.
     * @param pageCount the page count of the book.
     * @return the id of the book.
     */
    public int addBook(long isbn,String name,ArrayList<Author> authors,Publisher publisher,Date date,int pageCount) {
        // Return if the book exists.
        for (Book book : this.localStoreBooks) {
            if (book.getISBN() == isbn) {
                return book.getId();
            }
        }

        // Add the book.
        Book book = new Book(authors,publisher,isbn,date,pageCount,0,0,name,sharedBooks.size() + 1);
        this.localStoreBooks.add(book);
        this.sharedBooks.add(book);

        // Return the id.
        return book.getId();
    }

    /**
     * Returns the books for the given search.
     *
     * @param title the title of the book. To ignore this, leave it empty or use "*".
     * @param authors the authors of the book. To ignore this, leave it empty or use "*".
     * @param isbn the authors of the book. To ignore this, leave it empty or use "*".
     * @param publisher the publisher of the book. To ignore this, leave it empty or use "*".
     * @return the filtered books.
     */
    public Books getBooks(String title,String authors,String isbn,String publisher) {
        // Assemble the URL.
        String googleAPIURL = "https://www.googleapis.com/books/v1/volumes?q=";
        if (!title.equals("*")) {
            googleAPIURL += "intitle:" + URLEncoder.encode(title);
        }
        if (!authors.equals("*")) {
            googleAPIURL += "+inauthor:" + URLEncoder.encode(authors);
        }
        if (!publisher.equals("*")) {
            googleAPIURL += "+inpublisher:" + URLEncoder.encode(publisher);
        }

        // Run the Google Query.
        String response = "{}";
        try {
            // Create the URL object and open the connection.
            URL googleBooksConnection = new URL(googleAPIURL);
            HttpURLConnection urlConnection = (HttpURLConnection) googleBooksConnection.openConnection();

            // Set the parameters.
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Accept", "application/json");

            // Create an input stream for the connection.
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder responseBuilder = new StringBuilder();

            // Read the data from the connection until there is no new line.
            while ((inputLine = in.readLine()) != null) {
                responseBuilder.append(inputLine);
            }

            // Store the request.response and close the connection.
            response = responseBuilder.toString();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Register the books and store the ids.
        Books foundBooks = new Books();
        Map responseRootMap = new Gson().fromJson(response,Map.class);
        if (responseRootMap.keySet().contains("items")) {
            List queryBooks = (List) responseRootMap.get("items");
            for (Object bookDataRaw : queryBooks) {
                Map bookData = (Map) bookDataRaw;

                // Check if the book is in the United States.
                Map bookSaleability = (Map) bookData.get("saleInfo");
                Map volumeInfo = (Map) bookData.get("volumeInfo");
                if (bookSaleability.get("country").equals("US") && bookSaleability.get("saleability").equals("FOR_SALE")) {
                    // Get the book data.
                    long bookISBN = 0;
                    String bookName = (String) volumeInfo.get("title");
                    ArrayList<Author> bookAuthors = new ArrayList<>();
                    Publisher bookPublisher = new Publisher((String) volumeInfo.get("publisher"));
                    Date bookPublishDate;
                    int pageCount = 0;

                    // Get the ISBN.
                    List isbnData = (List) volumeInfo.get("industryIdentifiers");
                    if (isbnData != null) {
                        for (Object isbnInfoRaw : isbnData) {
                            Map isbnInfo = (Map) isbnInfoRaw;

                            // Set the ISBN if it is ISBN_13
                            if (isbnInfo.get("type").equals("ISBN_13")) {
                                bookISBN = Long.parseLong((String) isbnInfo.get("identifier"));
                                break;
                            }
                        }
                    }

                    // Get the authors.
                    List authorData = (List) volumeInfo.get("authors");
                    if (authorData != null) {
                        for (Object authorName : authorData) {
                            bookAuthors.add(new Author(new Name((String) authorName)));
                        }
                    }

                    // Get the publish date.
                    String publishDateString = (String) volumeInfo.get("publishedDate");
                    if (publishDateString.length() == 4) {
                        bookPublishDate = new Date(1,1,Integer.parseInt(publishDateString.substring(0,4)),0,0,0);
                    } else if (publishDateString.length() == 7) {
                        bookPublishDate = new Date(Integer.parseInt(publishDateString.substring(5,7)),0,Integer.parseInt(publishDateString.substring(0,4)),0,0,0);
                    } else {
                        bookPublishDate = new Date(Integer.parseInt(publishDateString.substring(5,7)),Integer.parseInt(publishDateString.substring(8,10)),Integer.parseInt(publishDateString.substring(0,4)),0,0,0);
                    }

                    // Get the page count.
                    if (volumeInfo.containsKey("pageCount")) {
                        pageCount = (int) (double) volumeInfo.get("pageCount");
                    }

                    // Add the book if the ISBN number is defined.
                    if (bookISBN != 0 && (isbn.equals("*") || Long.toString(bookISBN).contains(isbn))) {
                        int bookId = this.addBook(bookISBN,bookName,bookAuthors,bookPublisher,bookPublishDate,pageCount);
                        Book book = this.localStoreBooks.getBookById(bookId);
                        foundBooks.add(book);
                    }
                }
            }
        }

        // Return the found books.
        return foundBooks;
    }
}
