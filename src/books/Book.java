package books;

import time.Date;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Class representing a book.
 *
 * @author Michael Dolan
 * @author Zachary Cook
 */
public class Book implements Serializable {
    private ArrayList<Author> authors;
    private Publisher publisher;
    private long ISBN;
    private Date publishedDate;
    private int pageCount;
    private int numCopies;
    private int numCopiesCheckedOut;
    private String name;
    private int id;

    /**
     * Creates a book.
     *
     * @param authors the authors of the book.
     * @param publisher the publisher of the book.
     * @param ISBN the ISBN of the book.
     * @param publishedDate the date that book was published.
     * @param pageCount the page count of the book.
     * @param numCopies the number of copies of the book.
     * @param numCopiesCheckedOut the number of copies checked out.
     * @param name the name of the book.
     */
    public Book(ArrayList<Author> authors,Publisher publisher,long ISBN,Date publishedDate,int pageCount,int numCopies,int numCopiesCheckedOut,String name,int id) {
        this.authors = authors;
        this.publisher = publisher;
        this.ISBN = ISBN;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
        this.numCopies = numCopies;
        this.numCopiesCheckedOut = numCopiesCheckedOut;
        this.name = name;
        this.id = id;
    }

    /**
     * Creates a book.
     *
     * @param author the author of the book.
     * @param publisher the publisher of the book.
     * @param ISBN the ISBN of the book.
     * @param publishedDate the date that book was published.
     * @param pageCount the page count of the book.
     * @param numCopies the number of copies of the book.
     * @param numCopiesCheckedOut the number of copies checked out.
     * @param name the name of the book.
     */
    public Book(Author author,Publisher publisher,int ISBN,Date publishedDate,int pageCount,int numCopies,int numCopiesCheckedOut,String name) {
        this(new ArrayList<>(Collections.singletonList(author)),publisher,ISBN,publishedDate,pageCount,numCopies,numCopiesCheckedOut,name,0);
    }

    /**
     * Gets the book's id.
     *
     * @return the book's id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the book's ISBN.
     *
     * @return the  book's ISBN.
     */
    public long getISBN() {
        return ISBN;
    }

    /**
     * Gets the date the book was published.
     *
     * @return when the book was published.
     */
    public Date getPublishedDate() {
        return publishedDate;
    }

    /**
     * Gets how many pages are in the book.
     *
     * @return how many pages are in the book.
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Gets how many copies of this book the library owns.
     *
     * @return how many copies of this book the library owns.
     */
    public int getNumCopies() {
        return numCopies;
    }

    /**
     * Gets how many of this book are checked out.
     *
     * @return how many of this book are checked out.
     */
    public int getNumCopiesCheckedOut() {
        return numCopiesCheckedOut;
    }

    /**
     * Gets the authors of the book.
     *
     * @return the authors of the book.
     */
    public ArrayList<Author> getAuthors() {
        return new ArrayList<>(authors);
    }

    /**
     * Gets the publisher of this book.
     *
     * @return the publisher of this book.
     */
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * Gets the name of this book.
     *
     * @return the name of this book.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a copy of the book.
     */
    public void addCopy() {
        this.numCopies += 1;
    }

    /**
     * Removes a copy of the book.
     */
    public void removeCopy() {
        this.numCopies += -1;
    }

    /**
     * Takes out a copy of the book.
     */
    public void borrowCopy() {
        this.numCopiesCheckedOut += 1;
    }

    /**
     * Returns a copy of the book.
     */
    public void returnCopy() {
        this.numCopiesCheckedOut += -1;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Book{" +
                "authors=" + authors +
                ", publisher=" + publisher +
                ", ISBN=" + ISBN +
                ", publishedDate=" + publishedDate +
                ", pageCount=" + pageCount +
                ", numCopies=" + numCopies +
                ", numCopiesCheckedOut=" + numCopiesCheckedOut +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     *
     * @return true if this object is the same as the obj argument;
     * false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        // Return false if the class isn't the same.
        if (!(obj instanceof Book)) {
            return false;
        }

        // Cast the object and return if the hashcodes are the same.
        Book book = (Book) obj;
        return this.hashCode() == book.hashCode();
    }
}
