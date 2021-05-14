package user.connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.Name;
import user.Visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the {@link Connection} class.
 *
 * @author Joey Zhen
 */
public class ConnectionTest {
    private User user;
    private Connection connection;

    /**
     * Sets up the unit test.
     */
    @BeforeEach
    public void setup() {
        // Create a user.
        this.user = new User("John Doe","p@ssword",User.PermissionLevel.EMPLOYEE,new Visitor("0000000001",new Name("John Doe"),"Test address","1234567890"));

        // Create a connection.
        this.connection = new Connection(1);
    }

    /**
     * Tests the {@link Connection#getId()} method.
     */
    @Test
    public void test_getId() {
        assertEquals(connection.getId(),1,"Id not equal.");
    }

    /**
     * Tests the {@link Connection#setUser(User)} method.
     */
    @Test
    public void test_setUser() {
        // Assert no user ia associated.
        assertNull(connection.getUser(),"User initially not null.");

        // Assert a user is set.
        connection.setUser(this.user);
        assertEquals(connection.getUser(),this.user,"User not set.");
    }
}
