package books.transactions;

import books.Book;
import time.Date;
import user.Visitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Class for storing transactions.
 *
 * @author Michael Dolan
 * @author Zachary Cook
 */
public class TransactionHistory implements Serializable {
    private final HashMap<Integer,Transaction> transactions;

    /**
     * Creates the inventory of book.
     */
    public TransactionHistory() {
        this.transactions = new HashMap<>();
    }

    /**
     * Registers a transaction in the transaction history.
     *
     * @param transaction the transaction to register.
     */
    public void registerTransaction(Transaction transaction) {
        this.transactions.put(transaction.getId(),transaction);
    }

    /**
     * Registers a transaction in the transaction history.
     *
     * @param book the book part of the transaction.
     * @param visitor the visitor part of the transaction.
     * @param checkoutDate the checkout date.
     * @param dueDate the due date.
     */
    public void registerTransaction(Book book, Visitor visitor, Date checkoutDate, Date dueDate) {
        // Determine the next id.
        int nextId = 0;
        for (int i : this.transactions.keySet()) {
            if (i > nextId) {
                nextId = i;
            }
        }
        nextId = nextId + 1;

        // Create and store the transaction.
        Transaction transaction = new Transaction(nextId,visitor,book,checkoutDate,dueDate);
        this.registerTransaction(transaction);
    }

    /**
     * Registers a transaction in the transaction history.
     *
     * @param book the book part of the transaction.
     * @param visitor the visitor part of the transaction.
     * @param checkoutDate the checkout date.
     * @param dueDate the due date.
     */
    public void unregisterTransaction(Book book,Visitor visitor,Date checkoutDate,Date dueDate) {
        // Get the id to remove.
        int indexToRemove = -1;
        for (Integer id : this.transactions.keySet()) {
            Transaction transaction = this.getTransaction(id);
            if (transaction.getBook().equals(book) && transaction.getVisitor().equals(visitor) && transaction.getCheckedOut().equals(checkoutDate) && transaction.getDueDate().equals(dueDate)) {
                indexToRemove = id;
                break;
            }
        }

        // Remove the id.
        if (indexToRemove != -1) {
            this.transactions.remove(indexToRemove);
        }
    }

    /**
     * Returns the transaction for the given id.
     *
     * @param id the id of the transaction.
     * @return the transaction for the given id.
     */
    public Transaction getTransaction(int id){
        return transactions.get(id);
    }

    /**
     * Gets an ArrayList of transactions by a specific visitor.
     *
     * @param visitor the visitor whose transactions you want to find.
     * @return an ArrayList of transactions by the given visitor.
     */
    public ArrayList<Transaction> getTransactionsByVisitor(Visitor visitor){
        // Search for the transactions by the visitor.
        ArrayList<Transaction> output = new ArrayList<>();
        Collection<Transaction> transactions = this.transactions.values();
        for(Transaction transaction: transactions){
            if (transaction.getVisitor().equals(visitor)){
                output.add(transaction);
            }
        }

        // Return the transactions.
        return output;
    }

    /**
     * Gets an ArrayList of transactions by a the date the book was checked out.
     *
     * @param checkedOut the date on which the transaction took place.
     * @return an ArrayList of transactions where the book was checked out on the given date.
     */
    public ArrayList<Transaction> getTransactionsCheckedOutOn(Date checkedOut){
        // Search for the transactions by the date.
        ArrayList<Transaction> output = new ArrayList<>();
        Collection<Transaction> transactions = this.transactions.values();
        for(Transaction transaction: transactions){
            if (transaction.getCheckedOut().equals(checkedOut)){
                output.add(transaction);
            }
        }

        // Return the transactions.
        return output;
    }

    /**
     * Gets an ArrayList of transactions by a the date the book is due back.
     *
     * @param dueDate the date on which the book is due back.
     * @return an ArrayList of transactions where the book is due on the given date.
     */
    public ArrayList<Transaction> getTransactionsDueOn(Date dueDate){
        // Search for the transactions by the date.
        ArrayList<Transaction> output = new ArrayList<>();
        Collection<Transaction> transactions = this.transactions.values();
        for(Transaction transaction: transactions){
            if (transaction.getDueDate().equals(dueDate)){
                output.add(transaction);
            }
        }

        // Return the transactions.
        return output;
    }
}
