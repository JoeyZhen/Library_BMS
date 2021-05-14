package user.connection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link ClientConnections} class.
 *
 * @author Joey Zhen
 */
public class ClientConnectionsTest {
    /**
     * Tests that the constructor works without failing.
     */
    @Test
    public void test_constructor() {
        new ClientConnections();
    }

    /**
     * Tests the {@link ClientConnections#createConnection()} method.
     */
    @Test
    public void test_createConnect() {
        // Create the component under testing.
        ClientConnections CuT = new ClientConnections();

        // Test creating new connections.
        assertEquals(CuT.createConnection().getId(),1,"Id is incorrect.");
        assertEquals(CuT.createConnection().getId(),2,"Id is incorrect.");
        assertEquals(CuT.createConnection().getId(),3,"Id is incorrect.");
        assertEquals(CuT.createConnection().getId(),4,"Id is incorrect.");
        assertEquals(CuT.createConnection().getId(),5,"Id is incorrect.");
    }

    /**
     * Tests the {@link ClientConnections#closeConnection(int)} method.
     */
    @Test
    public void test_closeConnection() {
        // Create the component under testing.
        ClientConnections CuT = new ClientConnections();

        // Create 5 connections.
        assertEquals(CuT.createConnection().getId(),1,"Id is incorrect.");
        assertEquals(CuT.createConnection().getId(),2,"Id is incorrect.");
        assertEquals(CuT.createConnection().getId(),3,"Id is incorrect.");
        assertEquals(CuT.createConnection().getId(),4,"Id is incorrect.");
        assertEquals(CuT.createConnection().getId(),5,"Id is incorrect.");

        // Close 2 connections.
        CuT.closeConnection(3);
        CuT.closeConnection(4);

        // Assert the correct connections exist.
        assertNotNull(CuT.getConnection(1),"Connection doesn't exist.");
        assertNotNull(CuT.getConnection(2),"Connection doesn't exist.");
        assertNull(CuT.getConnection(3),"Connection exists.");
        assertNull(CuT.getConnection(4),"Connection exists.");
        assertNotNull(CuT.getConnection(5),"Connection doesn't exist.");

        // Create 3 more connections.
        assertEquals(CuT.createConnection().getId(),3,"Id is incorrect.");
        assertEquals(CuT.createConnection().getId(),4,"Id is incorrect.");
        assertEquals(CuT.createConnection().getId(),6,"Id is incorrect.");
    }
}
