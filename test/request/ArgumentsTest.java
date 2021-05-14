package request;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Arguments} class.
 */
public class ArgumentsTest {
    /**
     * Tests that the constructor works without failing.
     */
    @Test
    public void test_constructor() {
        new Arguments("Test1,Test2,Test3;");
    }

    /**
     * Tests that the constructor rejects the last character
     * not being a semicolon.
     */
    @Test
    public void test_constructorInvalidIds() {
        // Test with an id that contains letters.
        assertThrows(IllegalArgumentException.class,() -> new Arguments("Test1,Test2,Test3"),
                "Arguments was created.");
    }

    /**
     * Tests the {@link Arguments#cloneFromCurrentPointer()} method.
     */
    @Test
    public void test_cloneFromCurrentPointer() {
        // Create the component under testing.
        Arguments CuT = new Arguments("Test1,Test2,Test3;");

        // Create a clone.
        CuT.offsetPointer(1);
        Arguments clone = CuT.cloneFromCurrentPointer();

        // Run the assertions.
        assertEquals(clone.getNextString(),"Test2","String is incorrect.");
        assertTrue(clone.hasNext(),"Next doesn't exist.");
        assertEquals(clone.getNextString(),"Test3","String is incorrect.");
        assertFalse(clone.hasNext(),"Next does exist.");
    }

    /**
     * Tests the {@link Arguments#getNextString()} and {@link Arguments#hasNext()} methods.
     */
    @Test
    public void test_getNextString() {
        // Create the component under testing.
        Arguments CuT = new Arguments("Test1,Test2,Test3;");

        // Run the assertions.
        assertEquals(CuT.getNextString(),"Test1","String is incorrect.");
        assertTrue(CuT.hasNext(),"Next doesn't exist.");
        assertEquals(CuT.getNextString(),"Test2","String is incorrect.");
        assertTrue(CuT.hasNext(),"Next doesn't exist.");
        assertEquals(CuT.getNextString(),"Test3","String is incorrect.");
        assertFalse(CuT.hasNext(),"Next does exist.");
    }

    /**
     * Tests the {@link Arguments#offsetPointer(int)} method.
     */
    @Test
    public void test_offsetPointer() {
        // Create the component under testing.
        Arguments CuT = new Arguments("Test1,Test2,Test3;");

        // Run the assertions.
        assertEquals(CuT.getNextString(),"Test1","String is incorrect.");
        CuT.offsetPointer(1);
        assertEquals(CuT.getNextString(),"Test3","String is incorrect.");
        CuT.offsetPointer(-2);
        assertEquals(CuT.getNextString(),"Test2","String is incorrect.");
        CuT.offsetPointer(-5);
        assertEquals(CuT.getNextString(),"Test1","String is incorrect.");
    }

    /**
     * Tests the {@link Arguments#resetPointer()} method.
     */
    @Test
    public void test_resetPointer() {
        // Create the component under testing.
        Arguments CuT = new Arguments("Test1,Test2,Test3;");

        // Run the assertions.
        assertEquals(CuT.getNextString(),"Test1","String is incorrect.");
        assertEquals(CuT.getNextString(),"Test2","String is incorrect.");
        assertEquals(CuT.getNextString(),"Test3","String is incorrect.");
        CuT.resetPointer();
        assertEquals(CuT.getNextString(),"Test1","String is incorrect.");
    }

    /**
     * Tests the {@link Arguments#getNextListAsStrings()} method.
     */
    @Test
    public void test_getNextListAsStrings() {
        // Create the component under testing.
        Arguments CuT = new Arguments("{Test1,Test2,Test3};");

        // Create the expected result.
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Test1");
        expected.add("Test2");
        expected.add("Test3");

        // Run the assertions.
        assertEquals(CuT.getNextListAsStrings(),expected,"List is incorrect.");
    }

    /**
     * Tests the {@link Arguments#getRemainingStrings()} method.
     */
    @Test
    public void test_getRemainingStrings() {
        // Create the component under testing.
        Arguments CuT = new Arguments("Test1,Test2,Test3;");

        // Create the expected result.
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Test2");
        expected.add("Test3");

        // Run the assertions.
        assertEquals(CuT.getNextString(),"Test1","String is incorrect.");
        assertEquals(CuT.getRemainingStrings(),expected,"List is incorrect.");
    }

    /**
     * Tests the {@link Arguments#getNextInteger()} method.
     */
    @Test
    public void test_getNextInteger() {
        // Create the component under testing.
        Arguments CuT = new Arguments("1,Test2,3;");

        // Run the assertions.
        assertEquals(CuT.getNextInteger(),1,"Integer is incorrect.");
        assertNull(CuT.getNextInteger(),"Integer was parsed.");
        assertEquals(CuT.getNextInteger(),3,"Integer is incorrect.");
    }

    /**
     * Tests the {@link Arguments#getNextListAsIntegers()}  method.
     */
    @Test
    public void test_getNextListAsIntegers() {
        // Create the component under testing.
        Arguments CuT = new Arguments("{1,Test2,3};");

        // Create the expected result.
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(null);
        expected.add(3);

        // Run the assertions.
        assertEquals(CuT.getNextListAsIntegers(),expected,"List is incorrect.");
    }

    /**
     * Tests the {@link Arguments#getRemainingIntegers()}  method.
     */
    @Test
    public void test_getRemainingIntegers() {
        // Create the component under testing.
        Arguments CuT = new Arguments("1,Test2,3,4;");

        // Create the expected result.
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(null);
        expected.add(3);
        expected.add(4);

        // Run the assertions.
        assertEquals(CuT.getNextInteger(),1,"Integer is incorrect.");
        assertEquals(CuT.getRemainingIntegers(),expected,"List is incorrect.");
    }
}
