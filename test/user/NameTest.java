package user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Name} class.
 *
 * @author Joey Zhen
 */
public class NameTest {
    /**
     * Tests that the construct works without failing.
     */
    @Test
    public void test_constructor() {
        new Name("John","Doe");
        new Name("John Doe");
    }

    /**
     * Tests the {@link Name#toString()} method.
     */
    @Test
    public void test_toString() {
        // Create the component under testing.
        Name CuT = new Name("John","Doe");

        // Run the assertions.
        assertEquals(CuT.toString(),"John Doe","Name is incorrect.");
    }

    /**
     * Tests the {@link Name#equals(Object)} method.
     */
    @Test
    public void test_equals() {
        // Create the components under testing.
        Name CuT1 = new Name("John","Doe");
        Name CuT2 = new Name("John","Doe");

        // Run the assertions.
        assertEquals(CuT1,CuT2,"Names aren't equal.");
    }
}
