package books;

import org.junit.jupiter.api.Test;
import user.Name;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Author} class.
 *
 * @author Zachary Cook
 */
public class AuthorTest {
    /**
     * Tests that the construct works without failing.
     */
    @Test
    public void test_constructor() {
        new Author("John","Doe");
        new Author(new Name("John","Doe"));
    }

    /**
     * Tests the {@link Author#getName()} method.
     */
    @Test
    public void test_getName() {
        // Create the component under testing.
        Author CuT = new Author("John","Doe");

        // Run the assertions.
        assertEquals(CuT.getName(),"John Doe","Name is incorrect.");
    }

    /**
     * Tests the {@link Author#toString()} method.
     */
    @Test
    public void test_toString() {
        // Create the component under testing.
        Author CuT = new Author("John","Doe");

        // Run the assertions.
        assertEquals(CuT.toString(),"John Doe","Name is incorrect.");
    }

    /**
     * Tests the {@link Author#equals(Object)} method.
     */
    @Test
    public void test_equals() {
        // Create the components under testing.
        Author CuT1 = new Author("John","Doe");
        Author CuT2 = new Author("John","Doe");
        Author CuT3 = new Author("Jane","Doe");

        // Run the assertions.
        assertEquals(CuT1,CuT2,"Authors aren't equal.");
        assertNotEquals(CuT1,CuT3,"Authors are equal.");
    }
}
