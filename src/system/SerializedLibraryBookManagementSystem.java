package system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Extension of the LibraryBookManagementSystem to support saving the
 * services and loading them laster.
 *
 * @author Zachary Cook
 */
public class SerializedLibraryBookManagementSystem extends LibraryBookManagementSystem {
    /**
     * Creates the serialized library book management system.
     *
     * @param services the services to use.
     */
    public SerializedLibraryBookManagementSystem(Services services) {
        super(services);
    }

    /**
     * Creates the library book management system.
     */
    public SerializedLibraryBookManagementSystem() {
        this(new Services());
    }

    /**
     * Performs a request and returns a request.response as a string.
     *
     * @param request the request to make.
     * @return the request.response.
     */
    @Override
    public String performRequest(String request) {
        // Perform the request.
        String response = super.performRequest(request);

        // Save the services.
        this.save();

        // Return the request.response.
        return response;
    }

    /**
     * Saves the current services.
     */
    public void save() {
        try {
            // Write the services.
            FileOutputStream fileOut = new FileOutputStream(SERVICES_SAVE_LOCATION);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this.services);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads the library management system from file.
     */
    public static SerializedLibraryBookManagementSystem loadFromFile() {
        // Try to load the services.
        try {
            FileInputStream fileIn = new FileInputStream(SERVICES_SAVE_LOCATION);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object loadedServices = objectIn.readObject();

            objectIn.close();
            return new SerializedLibraryBookManagementSystem((Services) loadedServices);
        } catch (Exception ex) {
            System.out.println("Save of system not found; loading blank state.");
        }

        // Create a new system (fallback if unable to load).
        return new SerializedLibraryBookManagementSystem();
    }
}
