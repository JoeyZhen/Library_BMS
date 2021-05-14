package system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.Name;
import user.Visitor;
import user.visit.Visit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Behavior tests for the {@link LibraryBookManagementSystemTest} class.
 *
 * @author Joey Zhen
 */
public class LibraryBookManagementSystemTest {
    private LibraryBookManagementSystem CuT;

    /**
     * Sets up the unit tests.
     */
    @BeforeEach
    public void setup() {
        // Create the component under testing.
        this.CuT = new LibraryBookManagementSystem();
    }

    /**
     * Asserts that a request returns the intended request.response.
     *
     * @param request the request to make.
     * @param result the intended result.
     * @param errorMessage the error message if the result doesn't match.
     */
    private void assertRequest(String request,String result,String errorMessage) {
        // Run the request and get the result.
        String actualResult = CuT.performRequest(request);

        // Assert the request.
        assertEquals(actualResult,result,errorMessage);
    }

    /**
     * Asserts that a request returns the intended request.response.
     *
     * @param request the request to make.
     * @param result the intended result.
     */
    private void assertRequest(String request,String result) {
        this.assertRequest(request,result,"Results don't match.");
    }

    /**
     * Creates a connection and logs in root.
     */
    public void logInRoot() {
        // Connect 1 client.
        this.assertRequest("connect;","connect,1;");

        // Log in as root.
        this.assertRequest("1,login,root,password;","1,login,success;");
    }

    /**
     * Sets up the preset accounts and connections.
     */
    public void createTestConnections() {
        // Log in as root.
        this.logInRoot();

        // Create 2 visitors and accounts.
        this.assertRequest("1,register,John,Doe,Test Address,1234567890;","1,register,0000000001,2019/01/01 08:00:00;");
        this.assertRequest("1,register,Jane,Doe,Test Address,1234567890;","1,register,0000000002,2019/01/01 08:00:00;");
        this.assertRequest("1,create,JohnDoe,password123,employee,0000000001;","1,create,success;");
        this.assertRequest("1,create,JaneDoe,password456,visitor,0000000002;","1,create,success;");

        // Disconnect root.
        this.assertRequest("1,disconnect;","1,disconnect;");

        // Create 2 connections and log them in.
        this.assertRequest("connect;","connect,1;");
        this.assertRequest("connect;","connect,2;");
        this.assertRequest("1,login,JohnDoe,password123;","1,login,success;");
        this.assertRequest("2,login,JaneDoe,password456;","2,login,success;");
    }

    /**
     * Tests that partial requests will fail.
     */
    @Test
    public void test_partialRequest() {
        this.assertRequest("register,John,Doe,Test Address,1234567890","partial-request;");
    }

    /**
     * Tests starting a connection.
     */
    @Test
    public void test_connect() {
        this.assertRequest("connect;","connect,1;");
        this.assertRequest("connect;","connect,2;");
        this.assertRequest("connect;","connect,3;");
    }

    /**
     * Tests ending a connection.
     */
    @Test
    public void test_disconnect() {
        // Connect three clients.
        this.assertRequest("connect;","connect,1;");
        this.assertRequest("connect;","connect,2;");
        this.assertRequest("connect;","connect,3;");

        // Assert that disconnecting without a connection errors.
        this.assertRequest("disconnect;","invalid-client-id;");

        // Disconnect 2 clients.
        this.assertRequest("1,disconnect;","1,disconnect;");
        this.assertRequest("2,disconnect;","2,disconnect;");

        // Connect 3 clients.
        this.assertRequest("connect;","connect,1;");
        this.assertRequest("connect;","connect,2;");
        this.assertRequest("connect;","connect,4;");
    }

    /**
     * Tests creating a new account.
     */
    @Test
    public void test_create() {
        // Connect a client, log in root, and register 2 visitors.
        this.assertRequest("connect;","connect,1;");
        this.assertRequest("1,login,root,password;","1,login,success;");
        this.assertRequest("1,register,John,Doe,Test Address,1234567890;","1,register,0000000001,2019/01/01 08:00:00;");
        this.assertRequest("1,register,Jane,Doe,Test Address,1234567890;","1,register,0000000002,2019/01/01 08:00:00;");

        // Assert creating users with incorrect requests.
        this.assertRequest("create;","invalid-client-id;");
        this.assertRequest("1,create;","1,create,missing-parameters,{username,password,role,visitor ID};");
        this.assertRequest("1,create,JohnDoe;","1,create,missing-parameters,{password,role,visitor ID};");
        this.assertRequest("1,create,JohnDoe,password123;","1,create,missing-parameters,{role,visitor ID};");
        this.assertRequest("1,create,JohnDoe,password123,visitor;","1,create,missing-parameters,{visitor ID};");

        // Assert creating a user.
        this.assertRequest("1,create,JohnDoe,password123,visitor,0000000001;","1,create,success;");

        // Asset creating invalid users fails.
        this.assertRequest("1,create,JohnDoe2,password123,visitor,0000000001;","1,create,duplicate-visitor;");
        this.assertRequest("1,create,JohnDoe,password123,visitor,0000000002;","1,create,duplicate-username;");
        this.assertRequest("1,create,JohnDoe2,password123,visitor,0000000003;","1,create,invalid-visitor;");
    }

    /**
     * Tests logging in a user.
     */
    @Test
    public void test_login() {
        // Connect a client.
        this.assertRequest("connect;","connect,1;");

        // Assert logging in with incorrect requests.
        this.assertRequest("login;","invalid-client-id;");
        this.assertRequest("1,login;","1,login,missing-parameters,{username,password};");
        this.assertRequest("1,login,root;","1,login,missing-parameters,{password};");
        this.assertRequest("1,login,root,password123;","1,login,bad-username-or-password;");
        this.assertRequest("1,login,foo,bar;","1,login,bad-username-or-password;");

        // Assert logging in as root.
        this.assertRequest("1,login,root,password;","1,login,success;");
        this.assertRequest("1,login,root,password;","1,login,already-logged-in;");
    }

    /**
     * Tests logging out a user.
     */
    @Test
    public void test_logout() {
        // Connect a client.
        this.assertRequest("connect;","connect,1;");
        this.assertRequest("1,login,root,password;","1,login,success;");

        // Assert logging out.
        this.assertRequest("1,logout;","1,logout,success;");

        // Assert logging out with incorrect requests.
        this.assertRequest("logout;","invalid-client-id;");
        this.assertRequest("1,logout;","1,logout,not-authorized;");

        // Assert logging back in.
        this.assertRequest("1,login,root,password;","1,login,success;");
    }

    /**
     * Tests undoing a request.
     */
    @Test
    public void test_undo() {
        // Create the test connections.
        this.createTestConnections();

        // Assert missing parameters.
        this.assertRequest("undo;","invalid-client-id;");
        this.assertRequest("1,undo;","1,undo,cannot-undo;");

        // Begin a visit.
        this.assertRequest("1,arrive,0000000001;","1,arrive,0000000001,2019/01/01,08:00:00;");

        // Undo the request.
        this.assertRequest("1,undo;","1,undo,success;");

        // Begin the visit again.
        this.assertRequest("1,arrive,0000000001;","1,arrive,0000000001,2019/01/01,08:00:00;");
    }

    /**
     * Tests redoing a request.
     */
    @Test
    public void test_redo() {
        // Create the test connections.
        this.createTestConnections();

        // Assert missing parameters.
        this.assertRequest("redo;","invalid-client-id;");
        this.assertRequest("1,redo;","1,redo,cannot-redo;");

        // Begin and end a visit.
        this.assertRequest("1,arrive,0000000001;","1,arrive,0000000001,2019/01/01,08:00:00;");
        this.assertRequest("1,depart,0000000001;","1,depart,0000000001,08:00:00,00:00:00;");

        // Undo the request twice.
        this.assertRequest("1,undo;","1,undo,success;");
        this.assertRequest("1,undo;","1,undo,success;");

        // Begin the visit again.
        this.assertRequest("1,arrive,0000000001;","1,arrive,0000000001,2019/01/01,08:00:00;");

        // Undo and redo the request.
        this.assertRequest("1,undo;","1,undo,success;");
        this.assertRequest("1,redo;","1,redo,success;");

        // End the visit.
        this.assertRequest("1,depart,0000000001;","1,depart,0000000001,08:00:00,00:00:00;");

        // Undo the request twice and begin a visit.
        this.assertRequest("1,undo;","1,undo,success;");
        this.assertRequest("1,undo;","1,undo,success;");
        this.assertRequest("1,arrive,0000000001;","1,arrive,0000000001,2019/01/01,08:00:00;");

        // Assert nothing can be redone.
        this.assertRequest("1,redo;","1,redo,cannot-redo;");
    }

    /**
     * Tests setting the book service.
     */
    @Test
    public void test_service() {
        // Log in root.
        this.logInRoot();

        // Assert missing parameters.
        this.assertRequest("service;","invalid-client-id;");
        this.assertRequest("1,service;","1,service,missing-parameters,{info-service};");
        this.assertRequest("1,service,fake-service;","1,service,invalid-service;");

        // Set the service to Google and search twice to make sure ids are constant.
        this.assertRequest("1,service,google;","1,service,success;");
        this.assertRequest("1,search,C,Dennis Ritchie,*,*,title;","1,search,2\n" +
                "516,9780133086218,C Programming Language,{Brian W. Kernighan,Dennis Ritchie},1988/03/22,\n" +
                "517,9788025142028,Programovací jazyk C,{Brian W. Kernighan,Dennis M. Ritchie},2017/02/21,;");
        this.assertRequest("1,search,C,Dennis Ritchie,*,*,title;","1,search,2\n" +
                "516,9780133086218,C Programming Language,{Brian W. Kernighan,Dennis Ritchie},1988/03/22,\n" +
                "517,9788025142028,Programovací jazyk C,{Brian W. Kernighan,Dennis M. Ritchie},2017/02/21,;");

        // Set the service to local.
        this.assertRequest("1,service,local;","1,service,success;");
        this.assertRequest("1,search,Harry Potter,*,*,*,title;","1,search,8\n" +
                "13,9781783296033,Harry Potter,{Jody Revenson},2015/09/25,\n" +
                "16,9781781107041,Harry Potter and the Cursed Child – Parts One and Two (Special Rehearsal Edition),{J.K. Rowling, John Tiffany, Jack Thorne},2016/07/31,\n" +
                "9,9781408855713,Harry Potter and the Deathly Hallows,{J. K. Rowling},2014/01/01,\n" +
                "15,9780545582971,Harry Potter and the Order of the Phoenix,{J. K. Rowling},2013/08/27,\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,\n" +
                "12,9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,\n" +
                "14,9780062101891,Harry Potter Page to Screen,{Bob McCabe},2011/10/25,;");
    }

    /**
     * Tests registering visitors.
     */
    @Test
    public void test_register() {
        // Log in root.
        this.logInRoot();

        // Assert missing parameters.
        this.assertRequest("register;","invalid-client-id;");
        this.assertRequest("1,register;","1,register,missing-parameters,{first-name,last-name,address,phone-number};");
        this.assertRequest("1,register,John;","1,register,missing-parameters,{last-name,address,phone-number};");
        this.assertRequest("1,register,John,Doe;","1,register,missing-parameters,{address,phone-number};");
        this.assertRequest("1,register,John,Doe,Test Address;","1,register,missing-parameters,{phone-number};");

        // Assert registering visitors.
        this.assertRequest("1,register,John,Doe,Test Address,1234567890;","1,register,0000000001,2019/01/01 08:00:00;");
        this.assertRequest("1,register,Jane,Doe,Test Address,1234567890;","1,register,0000000002,2019/01/01 08:00:00;");
        this.assertRequest("1,register,Jane,Doe,Test Address 2,1234567890;","1,register,0000000003,2019/01/01 08:00:00;");
        assertEquals(CuT.getServices().getVisitorsRegistry().getVisitor("0000000001").getName(),"John Doe","Wrong visitor stored.");
        assertEquals(CuT.getServices().getVisitorsRegistry().getVisitor("0000000002").getName(),"Jane Doe","Wrong visitor stored.");
        assertEquals(CuT.getServices().getVisitorsRegistry().getVisitor("0000000003").getName(),"Jane Doe","Wrong visitor stored.");

        // Assert an error for a duplicate visitor.
        this.assertRequest("1,register,John,Doe,Test Address,1234567890;","1,register,duplicate;");
        assertNull(CuT.getServices().getVisitorsRegistry().getVisitor("0000000004"),"Duplicate user stored.");
    }

    /**
     * Tests beginning visits.
     */
    @Test
    public void test_beginVisit() {
        // Log in root.
        this.logInRoot();

        // Assert missing parameters.
        this.assertRequest("arrive;","invalid-client-id;");

        // Assert an unregistered visitor.
        this.assertRequest("1,arrive,0000000001;","1,arrive,invalid-id;");

        // Assert someone arriving when closed.
        this.assertRequest("1,register,John,Doe,Test Address,1234567890;","1,register,0000000001,2019/01/01 08:00:00;");
        this.assertRequest("1,advance,0,12;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/01,20:00:00;","Time not advanced.");
        this.assertRequest("1,arrive,0000000001;","1,arrive,closed;");
        this.assertRequest("1,advance,0,10;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/02,06:00:00;","Time not advanced.");
        this.assertRequest("1,arrive,0000000001;","1,arrive,closed;");

        // Assert someone arriving when open.
        this.assertRequest("1,advance,0,2;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/02,08:00:00;","Time not advanced.");
        this.assertRequest("1,arrive;","1,arrive,0000000000,2019/01/02,08:00:00;");
        this.assertRequest("1,arrive,0000000001;","1,arrive,0000000001,2019/01/02,08:00:00;");

        // Assert someone arriving when already arrived.
        this.assertRequest("1,arrive,0000000001;","1,arrive,duplicate;");
    }

    /**
     * Tests ending visits.
     */
    @Test
    public void test_endVisit() {
        // Log in root.
        this.logInRoot();

        // Assert missing parameters.
        this.assertRequest("depart;","invalid-client-id;");

        // Assert an unregistered visitor.
        this.assertRequest("1,depart,0000000001;","1,depart,invalid-id;");

        // Assert someone arriving.
        this.assertRequest("1,register,John,Doe,Test Address,1234567890;","1,register,0000000001,2019/01/01 08:00:00;");
        this.assertRequest("1,arrive,0000000001;","1,arrive,0000000001,2019/01/01,08:00:00;");

        // Assert someone departing when already arrived.
        this.assertRequest("1,advance,0,2;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/01,10:00:00;","Time not advanced.");
        this.assertRequest("1,depart,0000000001;","1,depart,0000000001,10:00:00,02:00:00;");

        // Assert someone leaving when not arrived.
        this.assertRequest("1,depart,0000000001;","1,depart,invalid-id;");
    }

    /**
     * Tests book searching.
     */
    @Test
    public void test_bookSearch() {
        // Log in root.
        this.logInRoot();

        // Assert missing parameters.
        this.assertRequest("info;","invalid-client-id;");
        this.assertRequest("1,info;","1,info,missing-parameters,{title,{authors}};");
        this.assertRequest("1,info,title;","1,info,missing-parameters,{{authors}};");

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Search using title.
        this.assertRequest("1,info,Harry Potter,*;","1,info,3\n" +
                "3,10,9781781100516,\"Harry Potter and the Prisoner of Azkaban\",{J.K. Rowling},1999/07/08,\n" +
                "3,11,9781781100486,\"Harry Potter and the Sorcerer's Stone\",{J.K. Rowling},2015/12/08,\n" +
                "6,12,9781338029994,\"Harry Potter Coloring Book\",{Inc. Scholastic},2015/11/10,;");

        // Search using author.
        this.assertRequest("1,info,*,{J.K. Rowling};","1,info,2\n" +
                "3,10,9781781100516,\"Harry Potter and the Prisoner of Azkaban\",{J.K. Rowling},1999/07/08,\n" +
                "3,11,9781781100486,\"Harry Potter and the Sorcerer's Stone\",{J.K. Rowling},2015/12/08,;");

        // Search using title and author.
        this.assertRequest("1,info,Sorcerer's Stone,{J.K. Rowling};","1,info,1\n" +
                "3,11,9781781100486,\"Harry Potter and the Sorcerer's Stone\",{J.K. Rowling},2015/12/08,;");

        // Search using ISBN.
        this.assertRequest("1,info,*,*,9780545387200;","1,info,1\n" +
                "3,17,9780545387200,\"The Hunger Games Trilogy\",{Suzanne Collins},2011/05/01,;");

        // Search using publisher.
        this.assertRequest("1,info,*,*,*,Pottermore;","1,info,2\n" +
                "3,10,9781781100516,\"Harry Potter and the Prisoner of Azkaban\",{J.K. Rowling},1999/07/08,\n" +
                "3,11,9781781100486,\"Harry Potter and the Sorcerer's Stone\",{J.K. Rowling},2015/12/08,;");

        // Searching with sorts.
        this.assertRequest("1,info,*,*,*,*,title;","1,info,4\n" +
                "3,10,9781781100516,\"Harry Potter and the Prisoner of Azkaban\",{J.K. Rowling},1999/07/08,\n" +
                "3,11,9781781100486,\"Harry Potter and the Sorcerer's Stone\",{J.K. Rowling},2015/12/08,\n" +
                "6,12,9781338029994,\"Harry Potter Coloring Book\",{Inc. Scholastic},2015/11/10,\n" +
                "3,17,9780545387200,\"The Hunger Games Trilogy\",{Suzanne Collins},2011/05/01,;");
        this.assertRequest("1,info,*,*,*,*,publish-date;","1,info,4\n" +
                "3,11,9781781100486,\"Harry Potter and the Sorcerer's Stone\",{J.K. Rowling},2015/12/08,\n" +
                "6,12,9781338029994,\"Harry Potter Coloring Book\",{Inc. Scholastic},2015/11/10,\n" +
                "3,17,9780545387200,\"The Hunger Games Trilogy\",{Suzanne Collins},2011/05/01,\n" +
                "3,10,9781781100516,\"Harry Potter and the Prisoner of Azkaban\",{J.K. Rowling},1999/07/08,;");
        this.assertRequest("1,info,*,*,*,*,book-status;","1,info,4\n" +
                "6,12,9781338029994,\"Harry Potter Coloring Book\",{Inc. Scholastic},2015/11/10,\n" +
                "3,17,9780545387200,\"The Hunger Games Trilogy\",{Suzanne Collins},2011/05/01,\n" +
                "3,10,9781781100516,\"Harry Potter and the Prisoner of Azkaban\",{J.K. Rowling},1999/07/08,\n" +
                "3,11,9781781100486,\"Harry Potter and the Sorcerer's Stone\",{J.K. Rowling},2015/12/08,;");
    }

    /**
     * Tests borrowing books.
     */
    @Test
    public void test_bookBorrow() {
        // Create the test connections.
        this.createTestConnections();

        // Assert missing parameters.
        this.assertRequest("borrow;","invalid-client-id;");
        this.assertRequest("1,borrow;","1,borrow,missing-parameters,{{id}};");

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Assert various errors.
        this.assertRequest("1,borrow,{1},0000000003;","1,borrow,invalid-visitor-id;");
        this.assertRequest("1,borrow,{1},0000000001;","1,borrow,invalid-book-id;");

        // Borrow 5 books.
        this.assertRequest("1,borrow,{10,10};","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{10};","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{11},0000000001;","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{11},0000000001;","1,borrow,2019/01/08;");

        // Assert borrowing a 6th book fails.
        this.assertRequest("1,borrow,{11},0000000001;","1,borrow,book-limit-exceeded;");

        // Test a book that is not available.
        this.assertRequest("1,borrow,{10},0000000002;","1,borrow,book-limit-exceeded;");

        // Test an outstanding balance.
        this.assertRequest("1,borrow,{11,17},0000000002;","1,borrow,2019/01/08;");
        this.assertRequest("1,advance,6,0;","1,advance,success;");
        this.assertRequest("1,advance,6,0;","1,advance,success;");
        this.assertRequest("1,borrow,{12},0000000002;","1,borrow,outstanding-fine,20;");
    }

    /**
     * Tests borrowing books with undoing.
     */
    @Test
    public void test_bookBorrowWithUndo() {
        // Create the test connections.
        this.createTestConnections();

        // Assert missing parameters.
        this.assertRequest("borrow;","invalid-client-id;");
        this.assertRequest("1,borrow;","1,borrow,missing-parameters,{{id}};");

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Borrow 5 books.
        this.assertRequest("1,borrow,{10,10};","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{10};","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{11},0000000001;","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{11},0000000001;","1,borrow,2019/01/08;");

        // Undo the last borrow.
        this.assertRequest("1,undo;","1,undo,success;");

        // Assert borrowing the 6th book as the 5th book.
        this.assertRequest("1,borrow,{11},0000000001;","1,borrow,2019/01/08;");
    }

    /**
     * Tests finding borrowed books.
     */
    @Test
    public void test_findBorrowedBooks() {
        // Create the test connections.
        this.createTestConnections();

        // Assert missing parameters.
        this.assertRequest("borrowed;","invalid-client-id;");

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Borrow 5 books.
        this.assertRequest("1,borrow,{10,10,11};","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{11,10};","1,borrow,2019/01/08;");

        // Assert the search.
        this.assertRequest("1,borrowed,0000000001;","1,borrowed,5\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,2019/01/01\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,2019/01/01\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,2019/01/01\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,2019/01/01\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,2019/01/01;");
        this.assertRequest("1,borrowed;","1,borrowed,5\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,2019/01/01\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,2019/01/01\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,2019/01/01\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,2019/01/01\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,2019/01/01;");
    }

    /**
     * Tests returning books.
     */
    @Test
    public void test_returnBooks() {
        // Create the test connections.
        this.createTestConnections();

        // Assert missing parameters.
        this.assertRequest("return;","invalid-client-id;");
        this.assertRequest("1,return;","1,return,missing-parameters,{visitor-id,id};");
        this.assertRequest("1,return,0000000001;","1,return,missing-parameters,{id};");

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Borrow 5 books.
        this.assertRequest("1,borrow,{10,10,11};","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{11,10};","1,borrow,2019/01/08;");

        // Assert the search.
        this.assertRequest("1,borrowed;","1,borrowed,5\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,2019/01/01\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,2019/01/01\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,2019/01/01\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,2019/01/01\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,2019/01/01;");

        // Return 3 books.
        this.assertRequest("1,return,0000000001,9,9,10;","1,return,success;");

        // Assert the search.
        this.assertRequest("1,borrowed,0000000001;","1,borrowed,2\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,2019/01/01\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,2019/01/01;");

        // Return a non-existent book.
        this.assertRequest("1,return,0000000001,9,3,10;","1,return,invalid-book-id,3;");

        // Assert a book can be taken out.
        this.assertRequest("1,borrow,{10};","1,borrow,2019/01/08;");

        // Assert a late fee.
        this.assertRequest("1,advance,6,0;","1,advance,success;");
        this.assertRequest("1,advance,6,0;","1,advance,success;");
        this.assertRequest("1,return,0000000001,9,10;","1,return,overdue,20,9,10;");
    }

    /**
     * Tests returning books with undoing.
     */
    @Test
    public void test_returnBooksWithUndo() {
        // Create the test connections.
        this.createTestConnections();

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Borrow 5 books.
        this.assertRequest("1,borrow,{10,10,11};","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{11,10};","1,borrow,2019/01/08;");

        // Return 3 books.
        this.assertRequest("1,return,0000000001,9,9,10;","1,return,success;");

        // Undo the request.
        this.assertRequest("1,undo;","1,undo,success;");

        // Return 3 books.
        this.assertRequest("1,return,0000000001,9,9,10;","1,return,success;");
    }

    /**
     * Tests paying fines.
     */
    @Test
    public void test_payFines() {
        // Create the test connections.
        this.createTestConnections();

        // Assert missing parameters.
        this.assertRequest("pay;","invalid-client-id;");
        this.assertRequest("1,pay;","1,pay,missing-parameters,{amount};");

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Assert various errors.
        this.assertRequest("1,pay,1,0000000003;","1,pay,invalid-visitor-id;");

        // Borrow 5 books.
        this.assertRequest("1,borrow,{10,10,11};","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{11,10};","1,borrow,2019/01/08;");

        // Assert a late fee.
        this.assertRequest("1,advance,6,0;","1,advance,success;");
        this.assertRequest("1,advance,6,0;","1,advance,success;");
        this.assertRequest("1,return,0000000001,9,9,9,10,10;","1,return,overdue,50,9,9,9,10,10;");

        // Assert failing to pay fees.
        this.assertRequest("1,pay,-5;","1,pay,invalid-amount,-5,50;");
        this.assertRequest("1,pay,60;","1,pay,invalid-amount,60,50;");

        // Assert paying partial fees.
        this.assertRequest("1,pay,10,0000000001;","1,pay,success,40;");
        this.assertRequest("1,pay,15;","1,pay,success,25;");
        this.assertRequest("1,pay,25;","1,pay,success,0;");

        // Borrow 5 books.
        this.assertRequest("1,borrow,{10,10,11};","1,borrow,2019/01/20;");
        this.assertRequest("1,borrow,{11,10};","1,borrow,2019/01/20;");
    }

    /**
     * Tests paying fines with undoing.
     */
    @Test
    public void test_payFinesWithUndo() {
        // Create the test connections.
        this.createTestConnections();

        // Assert missing parameters.
        this.assertRequest("pay;","invalid-client-id;");
        this.assertRequest("1,pay;","1,pay,missing-parameters,{amount};");

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Borrow 5 books.
        this.assertRequest("1,borrow,{10,10,11};","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{11,10};","1,borrow,2019/01/08;");

        // Assert a late fee.
        this.assertRequest("1,advance,6,0;","1,advance,success;");
        this.assertRequest("1,advance,6,0;","1,advance,success;");
        this.assertRequest("1,return,0000000001,9,9,9,10,10;","1,return,overdue,50,9,9,9,10,10;");

        // Assert paying partial fees.
        this.assertRequest("1,pay,10,0000000001;","1,pay,success,40;");
        this.assertRequest("1,pay,15;","1,pay,success,25;");
        this.assertRequest("1,pay,25;","1,pay,success,0;");

        // Undo the fees.
        this.assertRequest("1,undo;","1,undo,success;");
        this.assertRequest("1,undo;","1,undo,success;");

        // Assert paying partial fees.
        this.assertRequest("1,pay,15;","1,pay,success,25;");
        this.assertRequest("1,pay,25;","1,pay,success,0;");

        // Borrow 5 books.
        this.assertRequest("1,borrow,{10,10,11};","1,borrow,2019/01/20;");
        this.assertRequest("1,borrow,{11,10};","1,borrow,2019/01/20;");
    }

    /**
     * Tests searching the book store.
     */
    @Test
    public void test_searchBookStore() {
        // Log in root.
        this.logInRoot();

        // Assert missing parameters.
        this.assertRequest("search;","invalid-client-id;");
        this.assertRequest("1,search;","1,search,missing-parameters,{title};");

        // Perform a search with only the title.
        this.assertRequest("1,search,Harry Potter;","1,search,8\n" +
                "13,9781783296033,Harry Potter,{Jody Revenson},2015/09/25,\n" +
                "16,9781781107041,Harry Potter and the Cursed Child – Parts One and Two (Special Rehearsal Edition),{J.K. Rowling, John Tiffany, Jack Thorne},2016/07/31,\n" +
                "9,9781408855713,Harry Potter and the Deathly Hallows,{J. K. Rowling},2014/01/01,\n" +
                "15,9780545582971,Harry Potter and the Order of the Phoenix,{J. K. Rowling},2013/08/27,\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,\n" +
                "12,9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,\n" +
                "14,9780062101891,Harry Potter Page to Screen,{Bob McCabe},2011/10/25,;");

        // Perform a search with the title and authors.
        this.assertRequest("1,search,Harry Potter,{J.K. Rowling};","1,search,3\n" +
                "16,9781781107041,Harry Potter and the Cursed Child – Parts One and Two (Special Rehearsal Edition),{J.K. Rowling, John Tiffany, Jack Thorne},2016/07/31,\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,;");

        // Perform a search with the title, authors, and ISBN.
        this.assertRequest("1,search,Harry Potter,{J.K. Rowling},9781781107041;","1,search,1\n" +
                "16,9781781107041,Harry Potter and the Cursed Child – Parts One and Two (Special Rehearsal Edition),{J.K. Rowling, John Tiffany, Jack Thorne},2016/07/31,;");

        // Perform a search with the title, authors, and publisher.
        this.assertRequest("1,search,Harry Potter,{J.K. Rowling},*,Pottermore;","1,search,3\n" +
                "16,9781781107041,Harry Potter and the Cursed Child – Parts One and Two (Special Rehearsal Edition),{J.K. Rowling, John Tiffany, Jack Thorne},2016/07/31,\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,;");

        // Perform a search with only the title and search order.
        this.assertRequest("1,search,Harry Potter,*,*,*,title;","1,search,8\n" +
                "13,9781783296033,Harry Potter,{Jody Revenson},2015/09/25,\n" +
                "16,9781781107041,Harry Potter and the Cursed Child – Parts One and Two (Special Rehearsal Edition),{J.K. Rowling, John Tiffany, Jack Thorne},2016/07/31,\n" +
                "9,9781408855713,Harry Potter and the Deathly Hallows,{J. K. Rowling},2014/01/01,\n" +
                "15,9780545582971,Harry Potter and the Order of the Phoenix,{J. K. Rowling},2013/08/27,\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,\n" +
                "12,9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,\n" +
                "14,9780062101891,Harry Potter Page to Screen,{Bob McCabe},2011/10/25,;");
        this.assertRequest("1,search,Harry Potter,*,*,*,publish-date;","1,search,8\n" +
                "16,9781781107041,Harry Potter and the Cursed Child – Parts One and Two (Special Rehearsal Edition),{J.K. Rowling, John Tiffany, Jack Thorne},2016/07/31,\n" +
                "11,9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,\n" +
                "12,9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,\n" +
                "13,9781783296033,Harry Potter,{Jody Revenson},2015/09/25,\n" +
                "9,9781408855713,Harry Potter and the Deathly Hallows,{J. K. Rowling},2014/01/01,\n" +
                "15,9780545582971,Harry Potter and the Order of the Phoenix,{J. K. Rowling},2013/08/27,\n" +
                "14,9780062101891,Harry Potter Page to Screen,{Bob McCabe},2011/10/25,\n" +
                "10,9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,;");
    }

    /**
     * Tests purchasing books.
     */
    @Test
    public void test_purchaseBook() {
        // Log in root.
        this.logInRoot();

        // Assert missing parameters.
        this.assertRequest("buy;","invalid-client-id;");
        this.assertRequest("1,buy;","1,buy,missing-parameters,{quantity,id};");
        this.assertRequest("1,buy,3;","1,buy,missing-parameters,{id};");

        // Test a purchase.
        this.assertRequest("1,buy,3,2,2,3,4,5;","1,buy,4\n" +
                "9780936070278,Galloway's Book on Running,{Jeff Galloway},2002/01/01,6,\n" +
                "9781840894622,Running Shoes,{Frederick Lipp},2007/09/01,3,\n" +
                "9780736045100,Fitness Running,{Richard L. Brown, Joe Henderson},2003/01/01,3,\n" +
                "9780375896798,The Running Dream,{Wendelin Van Draanen},2011/01/11,3,;");
    }

    /**
     * Tests purchasing books with undoing.
     */
    @Test
    public void test_purchaseBookWithUndo() {
        // Log in root.
        this.logInRoot();

        // Assert the base stats.
        this.assertRequest("1,report;","1,report,2019/01/01,"
                + "\n Number of Books: 0"
                + "\n Number of Visitors: 0"
                + "\n Average Length of Visit: 00:00:00"
                + "\n Number of Books Purchased: 0"
                + "\n Fines Collected: 0"
                + "\n Fines Outstanding: 0\n;");

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Undoes the first set of purchases.
        this.assertRequest("1,undo;","1,undo,success;");

        // Assert the stats.
        this.assertRequest("1,report;","1,report,2019/01/01,"
                + "\n Number of Books: 15"
                + "\n Number of Visitors: 0"
                + "\n Average Length of Visit: 00:00:00"
                + "\n Number of Books Purchased: 15"
                + "\n Fines Collected: 0"
                + "\n Fines Outstanding: 0\n;");
    }

    /**
     * Tests advancing time.
     */
    @Test
    public void test_advanceTime() {
        // Log in root.
        this.logInRoot();

        // Assert missing parameters.
        this.assertRequest("advance;","invalid-client-id;");
        this.assertRequest("1,advance;","1,advance,missing-parameters,{number-of-days};");

        // Assert advancing an incorrect amount days.
        this.assertRequest("1,advance,-2;","1,advance,invalid-number-of-days,-2;");
        this.assertRequest("1,advance,28;","1,advance,invalid-number-of-days,28;");
        this.assertRequest("1,advance,test;","1,advance,days-not-a-number;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/01,08:00:00;","Time mutated.");

        // Assert advancing an incorrect amount of hours.
        this.assertRequest("1,advance,0,-2;","1,advance,invalid-number-of-hours,-2;");
        this.assertRequest("1,advance,0,28;","1,advance,invalid-number-of-hours,28;");
        this.assertRequest("1,advance,0,test;","1,advance,hours-not-a-number;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/01,08:00:00;","Time mutated.");

        // Assert advancing into the next day.
        this.assertRequest("1,advance,1;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/02,08:00:00;","Time not advanced.");

        // Assert advancing only days.
        this.assertRequest("1,advance,2,0;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/04,08:00:00;","Time not advanced.");

        // Assert advancing only hours.
        this.assertRequest("1,advance,0,2;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/04,10:00:00;","Time not advanced.");

        // Assert advancing both.
        this.assertRequest("1,advance,2,2;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/06,12:00:00;","Time not advanced.");

        // Assert advancing into the next day only using hours.
        this.assertRequest("1,advance,0,20;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/07,08:00:00;","Time not advanced.");

        // Test a visitor having his visit ended by the time being past 19:00.
        Visitor visitor = CuT.getServices().getVisitorsRegistry().registerVisitor(new Name("John","Doe"),"Test Address","1234567890");
        Visit visit = CuT.getServices().getVisitHistory().addVisit(visitor,CuT.getServices().getClock().getDate());

        // Assert the visit has not ended.
        this.assertRequest("1,advance,0,2;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/07,10:00:00;","Time not advanced.");
        assertFalse(visit.hasEnded(),"Visit ended.");

        // Finish the visit
        this.assertRequest("1,advance,0,10;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/07,20:00:00;","Time not advanced.");
        assertTrue(visit.hasEnded(),"Visit not ended.");

        // Reset the time to the next day.
        this.assertRequest("1,advance,0,12;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/08,08:00:00;","Time not advanced.");

        // Test a visitor having his visit ended by the time being set to the next day.
        visit = CuT.getServices().getVisitHistory().addVisit(visitor,CuT.getServices().getClock().getDate());
        this.assertRequest("1,advance,1,0;","1,advance,success;");
        this.assertRequest("1,datetime;","1,datetime,2019/01/09,08:00:00;","Time not advanced.");
        assertTrue(visit.hasEnded(),"Visit not ended.");
    }

    /**
     * Tests getting the current date and time.
     */
    @Test
    public void test_currentDateAndTime() {
        // Log in root.
        this.logInRoot();

        // Assert missing parameters.
        this.assertRequest("datetime;","invalid-client-id;");

        // Assert the initial time.
        this.assertRequest("1,datetime;","1,datetime,2019/01/01,08:00:00;","Initial time is incorrect.");
    }

    /**
     * Tests getting library statistics.
     */
    @Test
    public void test_libraryStatistics() {
        // Log in root.
        this.logInRoot();

        // Assert missing parameters.
        this.assertRequest("report;","invalid-client-id;");

        // Assert the base stats.
        this.assertRequest("1,report;","1,report,2019/01/01,"
            + "\n Number of Books: 0"
            + "\n Number of Visitors: 0"
            + "\n Average Length of Visit: 00:00:00"
            + "\n Number of Books Purchased: 0"
            + "\n Fines Collected: 0"
            + "\n Fines Outstanding: 0\n;");
        this.assertRequest("1,report,1;","1,report,2019/01/01,"
            + "\n Number of Books: 0"
            + "\n Number of Visitors: 0"
            + "\n Average Length of Visit: 00:00:00"
            + "\n Number of Books Purchased: 0"
            + "\n Fines Collected: 0"
            + "\n Fines Outstanding: 0\n;");

        // Register 2 visitors.
        this.assertRequest("1,register,John,Doe,Test Address,1234567890;","1,register,0000000001,2019/01/01 08:00:00;");
        this.assertRequest("1,register,Jane,Doe,Test Address,1234567890;","1,register,0000000002,2019/01/01 08:00:00;");

        // Assert two people arriving.
        this.assertRequest("1,advance,0,2;","1,advance,success;");
        this.assertRequest("1,arrive,0000000001;","1,arrive,0000000001,2019/01/01,10:00:00;");
        this.assertRequest("1,arrive,0000000002;","1,arrive,0000000002,2019/01/01,10:00:00;");
        this.assertRequest("1,advance,0,2;","1,advance,success;");
        this.assertRequest("1,depart,0000000001;","1,depart,0000000001,12:00:00,02:00:00;");
        this.assertRequest("1,advance,0,2;","1,advance,success;");
        this.assertRequest("1,depart,0000000002;","1,depart,0000000002,14:00:00,04:00:00;");

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Borrow some books.
        this.assertRequest("1,borrow,{11,10},0000000001;","1,borrow,2019/01/08;");
        this.assertRequest("1,borrow,{10},0000000002;","1,borrow,2019/01/08;");

        // Assert the stats.
        this.assertRequest("1,report;","1,report,2019/01/01,"
                + "\n Number of Books: 15"
                + "\n Number of Visitors: 2"
                + "\n Average Length of Visit: 03:00:00"
                + "\n Number of Books Purchased: 15"
                + "\n Fines Collected: 0"
                + "\n Fines Outstanding: 0\n;");

        // Assert advance to create late fees.
        this.assertRequest("1,advance,6,0;","1,advance,success;");
        this.assertRequest("1,advance,6,0;","1,advance,success;");

        // Purchase some books.
        this.assertRequest("1,buy,3,17,10,11,12,12;","1,buy,4\n" +
                "9780545387200,The Hunger Games Trilogy,{Suzanne Collins},2011/05/01,3,\n" +
                "9781781100516,Harry Potter and the Prisoner of Azkaban,{J.K. Rowling},1999/07/08,3,\n" +
                "9781781100486,Harry Potter and the Sorcerer's Stone,{J.K. Rowling},2015/12/08,3,\n" +
                "9781338029994,Harry Potter Coloring Book,{Inc. Scholastic},2015/11/10,6,;");

        // Assert two people arriving.
        this.assertRequest("1,arrive,0000000001;","1,arrive,0000000001,2019/01/13,14:00:00;");
        this.assertRequest("1,arrive,0000000002;","1,arrive,0000000002,2019/01/13,14:00:00;");
        this.assertRequest("1,advance,0,1;","1,advance,success;");
        this.assertRequest("1,depart,0000000001;","1,depart,0000000001,15:00:00,01:00:00;");
        this.assertRequest("1,advance,0,1;","1,advance,success;");
        this.assertRequest("1,depart,0000000002;","1,depart,0000000002,16:00:00,02:00:00;");

        // Pay the part of the late fees.
        this.assertRequest("1,pay,10,0000000001;","1,pay,success,10;");
        this.assertRequest("1,pay,5,0000000002;","1,pay,success,5;");

        // Assert the stats.
        this.assertRequest("1,report;","1,report,2019/01/13,"
                + "\n Number of Books: 30"
                + "\n Number of Visitors: 2"
                + "\n Average Length of Visit: 02:15:00"
                + "\n Number of Books Purchased: 30"
                + "\n Fines Collected: 15"
                + "\n Fines Outstanding: 15\n;");
        this.assertRequest("1,report,8;","1,report,2019/01/13,"
                + "\n Number of Books: 30"
                + "\n Number of Visitors: 2"
                + "\n Average Length of Visit: 01:30:00"
                + "\n Number of Books Purchased: 15"
                + "\n Fines Collected: 15"
                + "\n Fines Outstanding: 15\n;");
    }
}
