package time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link Time} class.
 *
 * @author Joey Zhen
 */
public class TimeTest {
    /**
     * Tests that the construct works without failing.
     */
    @Test
    public void test_constructor() {
        new Time(5,2,30);
        new Time((5 * 3600) + (2 * 60) + 30);
    }

    /**
     * Tests the {@link Time#getSeconds()} method.
     */
    @Test
    public void test_getSeconds(){
        Time secondTime = new Time(560);
        Time regularTime = new Time(2, 3, 15);

        assertEquals(560, secondTime.getSeconds(), "Seconds incorrect");
        assertEquals(7395, regularTime.getSeconds(), "Seconds incorrect");
    }

    /**
     * Test the {@link Time#advance(int, int, int)} method.
     */
    @Test
    public void test_advance() {
        // Create the component under testing.
        Time CuT = new Time(5,2,30);

        // Run the assertions.
        assertEquals(CuT.advance(2,3,4),new Time(7,5,34), "Time advanced incorrectly.");
        assertEquals(CuT.advance(2,3,34),new Time(7,6,4), "Time advanced incorrectly.");
        assertEquals(CuT.advance(2,73,4),new Time(8,15,34), "Time advanced incorrectly.");
        assertEquals(CuT.advance(28,3,4),new Time(9,5,34), "Time advanced incorrectly.");
    }

    /**
     * Tests the {@link Time#toString()} method.
     */
    @Test
    public void test_toString() {
        // Create the components under testing.
        Time CuT1 = new Time(5,2,30);
        Time CuT2 = new Time((5 * 3600) + (2 * 60) + 30);

        // Run the assertions.
        assertEquals(CuT1.toString(),"5:02:30","Time incorrect.");
        assertEquals(CuT2.toString(),"5:02:30","Time incorrect.");
    }

    /**
     * Tests the {@link Time#equals(Object)} method.
     */
    @Test
    public void test_equals() {
        // Create the components under testing.
        Time CuT1 = new Time(5,2,30);
        Time CuT2 = new Time((5 * 3600) + (2 * 60) + 30);

        // Run the assertions.
        assertEquals(CuT1,CuT2,"Times aren't equal.");
    }
}

