package Client;

import javafx.scene.Node;
import system.SerializedLibraryBookManagementSystem;

public abstract class Page {
    protected final SerializedLibraryBookManagementSystem LBMS;
    protected final ClientApplication clientApplication;

    /**
     * constructor for all pages
     * creates a page
     * @param clientApplication the client that this page is in
     * @param LBMS the LBMS the client is connected to
     */
    public Page(ClientApplication clientApplication, SerializedLibraryBookManagementSystem LBMS) {
        this.clientApplication = clientApplication;
        this.LBMS = LBMS;
    }

    /**
     * gets the root pane of the page
     * @return the pane that contains the page
     */
    public abstract Node getRoot();

}
