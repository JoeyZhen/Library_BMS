package time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Date} class.
 *
 * @author Joey Zhen
 */
public class DateTest {
    /**
     * Tests that the construct works without failing.
     */
    @Test
    public void test_constructor() {
        new Date(3,14,2019,5,2,30);
    }

    /**
     * Tests the {@link Date#after(Date)} method.
     */
    @Test
    public void test_after() {
        // Create the components under testing.
        Date CuT = new Date(3,14,2019,5,2,30);

        // Run the assertions for dates being after.
        assertFalse(CuT.after(new Date(3,14,2020,5,2,30)),"Date is not after.");
        assertFalse(CuT.after(new Date(4,14,2019,5,2,30)),"Date is not after.");
        assertFalse(CuT.after(new Date(3,15,2019,5,2,30)),"Date is not after.");
        assertFalse(CuT.after(new Date(3,14,2019,6,2,30)),"Date is not after.");
        assertFalse(CuT.after(new Date(3,14,2019,5,3,30)),"Date is not after.");

        // Run the assertions for dates being the same.
        assertFalse(CuT.after(CuT),"Date is not after.");

        // Run the assertions for dates being before.
        assertTrue(CuT.after(new Date(3,14,2019,5,2,29)),"Date is after.");
        assertTrue(CuT.after(new Date(3,14,2019,5,1,30)),"Date is after.");
        assertTrue(CuT.after(new Date(3,14,2019,4,2,30)),"Date is after.");
        assertTrue(CuT.after(new Date(3,13,2019,5,2,30)),"Date is after.");
        assertTrue(CuT.after(new Date(2,14,2019,5,2,30)),"Date is after.");
        assertTrue(CuT.after(new Date(3,14,2018,5,2,30)),"Date is after.");
    }

    /**
     * Tests the {@link Date#differenceInDays(Date)} method.
     */
    @Test
    public void test_differenceInDays() {
        // Create the components under testing.
        Date CuT = new Date(3,14,2018,5,2,30);

        // Run the assertions.
        assertEquals(CuT.differenceInDays(new Date(3,14,2018,5,2,30)),0,"Difference is incorrect.");
        assertEquals(CuT.differenceInDays(new Date(3,15,2018,5,2,30)),1,"Difference is incorrect.");
        assertEquals(CuT.differenceInDays(new Date(4,14,2018,5,2,30)),31,"Difference is incorrect.");
        assertEquals(CuT.differenceInDays(new Date(4,14,2019,5,2,30)),31 + 365,"Difference is incorrect.");
        assertEquals(CuT.differenceInDays(new Date(4,14,2020,5,2,30)),31 + 365 + 366,"Difference with leap year is incorrect.");
    }

    /**
     * Test the {@link Time#advance(int, int, int)} method.
     */
    @Test
    public void test_advance() {
        // Create the component under testing.
        Date CuT = new Date(3, 14, 2018, 5, 2, 30);

        // Run the assertions.
        assertEquals(CuT.advance(6, 3, 8, 2, 3, 4), new Date(6, 22, 2024, 7, 5, 34), "Date advanced incorrectly.");
        assertEquals(CuT.advance(6, 3, 8, 2, 3, 34), new Date(6, 22, 2024, 7, 6, 4), "Date advanced incorrectly.");
        assertEquals(CuT.advance(6, 3, 8, 2, 73, 34), new Date(6, 22, 2024, 8, 16, 4), "Date advanced incorrectly.");
        assertEquals(CuT.advance(6, 3, 8, 25, 73, 34), new Date(6, 23, 2024, 7, 16, 4), "Date advanced incorrectly.");
        assertEquals(CuT.advance(6, 3, 17, 25, 73, 34), new Date(7, 2, 2024, 7, 16, 4), "Date advanced incorrectly.");
        assertEquals(CuT.advance(6, 13, 17, 25, 73, 34), new Date(5, 2, 2025, 7, 16, 4), "Date advanced incorrectly.");

    }

    /**
     * Tests the {@link Date#toString()} method.
     */
    @Test
    public void test_toString() {
        // Create the components under testing.
        Date CuT = new Date(3,14,2019,5,2,30);

        // Run the assertions.
        assertEquals(CuT.toString(),"3/14/2019 5:02:30","Date is incorrect.");
    }

    /**
     * Tests the {@link Date#equals(Object)} method.
     */
    @Test
    public void test_equals() {
        // Create the components under testing.
        Date CuT1 = new Date(3,14,2019,5,2,30);
        Date CuT2 = new Date(3,14,2019,5,2,30);

        // Run the assertions.
        assertEquals(CuT1,CuT2,"Dates aren't equal.");
    }
}
