package books;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the {@link Publisher} class.
 *
 * @author Zachary Cook
 */
public class PublisherTest {
    /**
     * Tests that the construct works without failing.
     */
    @Test
    public void test_constructor() {
        new Publisher("Test Publisher");
    }

    /**
     * Tests the {@link Publisher#getName()} method.
     */
    @Test
    public void test_getName() {
        // Create the component under testing.
        Publisher CuT = new Publisher("Test Publisher");

        // Run the assertions.
        assertEquals(CuT.getName(),"Test Publisher","Name is incorrect.");
    }

    /**
     * Tests the {@link Publisher#toString()} method.
     */
    @Test
    public void test_toString() {
        // Create the component under testing.
        Publisher CuT = new Publisher("Test Publisher");

        // Run the assertions.
        assertEquals(CuT.toString(),"Test Publisher","Name is incorrect.");
    }

    /**
     * Tests the {@link Publisher#equals(Object)} method.
     */
    @Test
    public void test_equals() {
        // Create the components under testing.
        Publisher CuT1 = new Publisher("Test Publisher 1");
        Publisher CuT2 = new Publisher("Test Publisher 1");
        Publisher CuT3 = new Publisher("Test Publisher 2");

        // Run the assertions.
        assertEquals(CuT1,CuT2,"Publisher aren't equal.");
        assertNotEquals(CuT1,CuT3,"Publisher are equal.");
    }
}
