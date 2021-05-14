package request.connected.revertable;

import books.transactions.Transaction;
import request.Arguments;
import request.Parameter;
import request.Waypoint;
import request.connected.AccountRequest;
import request.response.Response;
import system.Services;
import time.Date;
import user.Visitor;
import user.connection.Connection;
import user.connection.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Request for setting the fines paid by a user as paid.
 *
 * @author Joey Zhen
 * @author Zachary Cook
 */
public class PayFine extends AccountRequest implements Waypoint {
    private boolean wasCompleted;
    private HashMap<Transaction,Integer> transactionsToUndo;

    /**
     * Creates a request.
     *
     * @param services the services to use for the request.
     * @param connection the connection to use.
     * @param arguments the arguments to use.
     */
    public PayFine(Services services,Connection connection,Arguments arguments) {
        super(services,connection,arguments,User.PermissionLevel.EMPLOYEE);
        this.wasCompleted = false;
        this.transactionsToUndo = new HashMap<>();
    }

    /**
     * Returns the name of the request.
     *
     * @return the name of the request.
     */
    @Override
    public String getName() {
        return "pay";
    }

    /**
     * Returns a list of the required parameters.
     *
     * @return a list of the required parameters.
     */
    @Override
    public ArrayList<Parameter> getRequiredParameters() {
        // Create the required parameters.
        ArrayList<Parameter> requiredParameters = new ArrayList<>();
        requiredParameters.add(new Parameter("amount",Parameter.ParameterType.INTEGER));

        // Return the required parameters.
        return requiredParameters;
    }

    /**
     * Returns a response for the request.
     *
     * @return the response of the request.
     */
    @Override
    public Response handleRequest() {
        this.wasCompleted = false;
        Arguments arguments = this.getArguments();
        Services services = this.getServices();
        Connection connection = this.getConnection();

        // Get the visitor id and amount to pay.
        Integer amountToPay = arguments.getNextInteger();
        if (amountToPay == null) {
            return this.sendResponse("amount-not-a-number");
        }

        // Get the visitor.
        Visitor visitor = connection.getUser().getVisitor();
        if (arguments.hasNext()) {
            String visitorId = arguments.getNextString();
            visitor = services.getVisitorsRegistry().getVisitor(visitorId);

            if (visitor == null) {
                return this.sendResponse("invalid-visitor-id");
            }
        }

        // Get the current date and due date.
        Date currentDate = services.getClock().getDate();

        // Determine the unpaid transactions.
        int unpaidBalance = 0;
        ArrayList<Transaction> unpaidUnreturnedTransactions = new ArrayList<>();
        ArrayList<Transaction> unpaidReturnedTransactions = new ArrayList<>();
        for (Transaction transaction : services.getTransactionHistory().getTransactionsByVisitor(visitor)) {
            if (!transaction.getLateFeedPaid()) {
                unpaidBalance += transaction.calculateFee(currentDate) - transaction.getPartialLateFeePaid();

                if (transaction.getReturned()) {
                    unpaidReturnedTransactions.add(transaction);
                } else {
                    unpaidUnreturnedTransactions.add(transaction);
                }
            }
        }

        // Return errors for invalid payments.
        if (amountToPay > unpaidBalance || amountToPay < 0) {
            return this.sendResponse("invalid-amount," + amountToPay + "," + unpaidBalance);
        }

        // Pay for the books.
        int balanceAfterward = unpaidBalance;
        unpaidReturnedTransactions.addAll(unpaidUnreturnedTransactions);
        for (Transaction transaction : unpaidReturnedTransactions) {
            // Calculate the remaining late fee.
            int lateFeeRemainder = ((int) transaction.calculateFee(currentDate)) - transaction.getPartialLateFeePaid();

            // Either pay the partial fee or pay the transaction.
            if (lateFeeRemainder <= amountToPay) {
                transaction.incrementPartialLateFeePaid(lateFeeRemainder);
                balanceAfterward += -lateFeeRemainder;
                amountToPay += -lateFeeRemainder;
                this.transactionsToUndo.put(transaction,amountToPay);
                if (transaction.getReturned()) {
                    transaction.setLateFeeAsPaid(currentDate);
                }
            } else {
                transaction.incrementPartialLateFeePaid(amountToPay);
                balanceAfterward += -amountToPay;
                this.transactionsToUndo.put(transaction,amountToPay);
                amountToPay = 0;
            }
        }

        // Return the request.response.
        this.wasCompleted = true;
        return this.sendResponse("success," + balanceAfterward);
    }

    /**
     * Undos the waypoint.
     *
     * @return if it was successful.
     */
    @Override
    public boolean undo() {
        // Return if the request failed.
        if (!this.wasCompleted) {
            return false;
        }

        // Remove the books and purchase history.
        for (Transaction transaction : this.transactionsToUndo.keySet()) {
            transaction.incrementPartialLateFeePaid(-this.transactionsToUndo.get(transaction));
            transaction.setLateFeeAsUnpaid();
        }

        // Return true (success).
        this.wasCompleted = false;
        this.transactionsToUndo.clear();
        return true;
    }

    /**
     * Redos the waypoint. It should not be called without
     * performing the original request.
     *
     * @return if it was successful.
     */
    @Override
    public boolean redo() {
        this.handleRequest();
        return true;
    }
}