package user.connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.Name;
import user.Visitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link User} class.
 *
 * @author Joey Zhen
 */
public class UserTest {
    private User user1;
    private User user2;

    /**
     * Sets up the unit test.
     */
    @BeforeEach
    public void setup() {
        // Create the users.
        this.user1 = new User("John Doe","p@ssword",User.PermissionLevel.EMPLOYEE,new Visitor("0000000001",new Name("John Doe"),"Test address","1234567890"));
        this.user2 = new User("Jane Doe","passw0rd",User.PermissionLevel.VISITOR,new Visitor("0000000002",new Name("Jane Doe"),"Test address","1234567890"));
    }

    /**
     * Tests the {@link User#getUsername()} method.
     */
    @Test
    public void test_getName() {
        assertEquals(this.user1.getUsername(),"John Doe","Name is incorrect.");
        assertEquals(this.user2.getUsername(),"Jane Doe","Name is incorrect.");
    }

    /**
     * Tests the {@link User#getPermissionLevel()} method.
     */
    @Test
    public void test_getPermissionLevel() {
        assertEquals(this.user1.getPermissionLevel(),User.PermissionLevel.EMPLOYEE,"Permission level is incorrect.");
        assertEquals(this.user2.getPermissionLevel(),User.PermissionLevel.VISITOR,"Permission level is incorrect.");
    }

    /**
     * Tests the {@link User#passwordMatches(String)} method.
     */
    @Test
    public void test_passwordMatches() {
        assertTrue(this.user1.passwordMatches("p@ssword"),"Password doesn't match.");
        assertFalse(this.user1.passwordMatches("passw0rd"),"Password matches.");

        assertFalse(this.user2.passwordMatches("p@ssword"),"Password matches");
        assertTrue(this.user2.passwordMatches("passw0rd"),"Password doesn't match.");
    }

    /**
     * Tests the {@link User#hasAccess(User.PermissionLevel)} method.
     */
    @Test
    public void test_hasAccess() {
        assertTrue(this.user1.hasAccess(User.PermissionLevel.VISITOR),"Access is incorrect.");
        assertTrue(this.user1.hasAccess(User.PermissionLevel.EMPLOYEE),"Access is incorrect.");

        assertTrue(this.user2.hasAccess(User.PermissionLevel.VISITOR),"Access is incorrect.");
        assertFalse(this.user2.hasAccess(User.PermissionLevel.EMPLOYEE),"Access is incorrect.");
    }
}
