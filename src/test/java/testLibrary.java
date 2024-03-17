import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testLibrary {


    /** Junit testing */

    /**
     * setMemberMaxBooks method tests
     */
    @Test
    public void setMemberMaxBooks() {
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        int education_level = 1;
        int result = library.setMemberMaxBooks(education_level);

        int expected = 3;
        assertEquals(expected, result);
    }

    @Test
    public void setMemberMaxBooks_boundary() {
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        int education_level = 5;
        int result = library.setMemberMaxBooks(education_level);

        int expected = 0;
        assertEquals(expected, result);
    }


    /**
     * RegisterMember method
     */


    @Test
    @Order(1)
    public void testRegister() {
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        Member newMember = new Member("Test", "Testsson", 123321, 123, 3);
        int result = library.registerMember(newMember);

        assertEquals(3, result);
    }

    @Test
    @Order(2)
    public void testLoanBook() {
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        int result = library.loanBook(123, "Book 10");

        assertEquals(7, result);
    }

    @Test
    @Order(3)
    public void testReturnBook() {
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        int result = library.returnBook(123, 112345);

        assertEquals(5, result);

    }

    @Test
    @Order(4)
    public void testDeleteSuspendendMember() {
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        int result = library.deleteSuspendedMember(123);

        assertEquals(3, result);
    }


    @Test
    @Order(4)
    public void testSuspendMember() {
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        int result = library.suspendMember(123);

        assertEquals(3, result);
    }

    @Test
    @Order(5)
    public void testDeleteMember() {
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        int result = library.deleteMember(123);

        assertEquals(3, result);
    }


    @Test
    @Order(6)
    public void testUnsuspendMember() {
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        int result = library.unsuspendMember(123);

        assertEquals(1, result);
    }


    /** RegisterMember method tests MOCKITO */

    @Test
    public void testRegister_Mockito(){
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        Member newMember = new Member("Test", "Testsson", 123321, 123, 3);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.isCurrentlySuspended(123)).thenReturn(false);

        int result = library.registerMember(newMember);
        assertEquals(3,result);
    }

    @Test
    public void testRegister_suspendedMember_mockito(){
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        Member newMember = new Member("Test", "Testsson", 123321, 123, 3);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.isCurrentlySuspended(123)).thenReturn(true);

        int result = library.registerMember(newMember);
        assertEquals(2,result);
    }

    /** suspendMember method tests MOCKITO */


    @Test
    public void suspendMember_memberNotFound_mockito(){
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        when(mockDatabaseManager.isMember(123)).thenReturn(false);
        int result = library.suspendMember(123);

        assertEquals(1, result);
    }

    @Test
    public void suspendMember_withViolations_mockito(){
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.getNumOfViolations(123)).thenReturn(3);

        int result = library.suspendMember(123);
        assertEquals(2, result);
    }

    /** DeleteSuspended method tests MOCKITO */

    @Test
    public void deleteSuspendedMember_tooLowSuspensionCount_mockito(){
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.getSuspensionCount(123)).thenReturn(2);

        int result = library.deleteSuspendedMember(123);
        assertEquals(3,result);
    }

    @Test
    public void deleteSuspendedMember_Valid(){
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.getNumOfLoans(123)).thenReturn(0);
        when(mockDatabaseManager.getSuspensionCount(123)).thenReturn(3);

        int result = library.deleteSuspendedMember(123);
        assertEquals(4,result);
    }

    /** DeleteMember method tests MOCKITO */


    @Test
    public void testDeleteMember_existingLoan_mockito(){
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.getNumOfLoans(123)).thenReturn(2);

        int result = library.deleteMember(123);

        assertEquals(2, result);
    }


    /** LoanBook method tests MOCKITO */


    @Test
    public void testLoanBook_Valid() {
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.getISBN("Ex Title")).thenReturn(123456);
        when(mockDatabaseManager.doesBookExist(123456)).thenReturn(true);
        when(mockDatabaseManager.isBookAvailable(123456)).thenReturn(true);
        when(mockDatabaseManager.isCurrentlySuspended(123)).thenReturn(false);
        when(mockDatabaseManager.getLoansOfCopy(123, 123456)).thenReturn(0);
        when(mockDatabaseManager.getNumOfLoans(123)).thenReturn(0);
        when(mockDatabaseManager.getMaxNumOfLoans(123)).thenReturn(3);

        int result = library.loanBook(123, "Ex Title");
        assertEquals(7, result);
    }
    @Test
    public void testLoanBook_bookNotAvailable () {
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.getISBN("Ex Title")).thenReturn(123456);
        when(mockDatabaseManager.doesBookExist(123456)).thenReturn(false);
        when(mockDatabaseManager.isBookAvailable(123456)).thenReturn(false);
        when(mockDatabaseManager.isCurrentlySuspended(123)).thenReturn(false);
        when(mockDatabaseManager.getLoansOfCopy(123, 123456)).thenReturn(0);
        when(mockDatabaseManager.getNumOfLoans(123)).thenReturn(0);
        when(mockDatabaseManager.getMaxNumOfLoans(123)).thenReturn(3);

        int result = library.loanBook(123, "Ex Title");
        assertEquals(2, result);
    }

    @Test
    public void testLoanBook_MemberSuspended () {
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.getISBN("Ex Title")).thenReturn(123456);
        when(mockDatabaseManager.doesBookExist(123456)).thenReturn(true);
        when(mockDatabaseManager.isBookAvailable(123456)).thenReturn(true);
        when(mockDatabaseManager.isCurrentlySuspended(123)).thenReturn(true);
        when(mockDatabaseManager.getLoansOfCopy(123, 123456)).thenReturn(0);
        when(mockDatabaseManager.getNumOfLoans(123)).thenReturn(0);
        when(mockDatabaseManager.getMaxNumOfLoans(123)).thenReturn(3);

        int result = library.loanBook(123, "Ex Title");
        assertEquals(4, result);
    }
    @Test
    public void testLoanBook_MaxNumOfBooksReached () {
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.getISBN("Ex Title")).thenReturn(123456);
        when(mockDatabaseManager.doesBookExist(123456)).thenReturn(true);
        when(mockDatabaseManager.isBookAvailable(123456)).thenReturn(true);
        when(mockDatabaseManager.isCurrentlySuspended(123)).thenReturn(false);
        when(mockDatabaseManager.getLoansOfCopy(123, 123456)).thenReturn(0);
        when(mockDatabaseManager.getNumOfLoans(123)).thenReturn(3);
        when(mockDatabaseManager.getMaxNumOfLoans(123)).thenReturn(3);

        int result = library.loanBook(123, "Ex Title");
        assertEquals(6, result);
    }







    /** ReturnBook method tests MOCKITO */


    @Test
    public void testReturnBook_valid() {
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);
        LocalDate loanDate = LocalDate.of(2024, 03, 14);
        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.doesBookExist(123456)).thenReturn(true);
        when(mockDatabaseManager.getLoanDate(123, 123456)).thenReturn(loanDate);
        when(mockDatabaseManager.isDate15DaysAgo(loanDate)).thenReturn(false);
        when(mockDatabaseManager.getNumOfViolations(123)).thenReturn(0);
        when(mockDatabaseManager.getNumOfLoans(123)).thenReturn(1);

        int result = library.returnBook(123, 123456);
        assertEquals(5, result);

    }

    @Test
    public void testReturnBook_validButLateReturn() {
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);
        LocalDate loanDate = LocalDate.of(2024, 03, 14);
        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.doesBookExist(123456)).thenReturn(true);
        when(mockDatabaseManager.getLoanDate(123, 123456)).thenReturn(loanDate);
        when(mockDatabaseManager.isDate15DaysAgo(loanDate)).thenReturn(true);
        when(mockDatabaseManager.getNumOfViolations(123)).thenReturn(1);
        when(mockDatabaseManager.getNumOfLoans(123)).thenReturn(1);


        int result = library.returnBook(123, 123456);
        assertEquals(4, result);

    }
    @Test
    public void testReturnBook_validButLateReturnAndSuspension() {
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        LocalDate loanDate = LocalDate.of(2024, 03, 14);
        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.doesBookExist(123456)).thenReturn(true);
        when(mockDatabaseManager.getLoanDate(123, 123456)).thenReturn(loanDate);
        when(mockDatabaseManager.isDate15DaysAgo(loanDate)).thenReturn(true);
        when(mockDatabaseManager.getNumOfViolations(123)).thenReturn(3);
        when(mockDatabaseManager.getNumOfLoans(123)).thenReturn(1);


        int result = library.returnBook(123, 123456);
        assertEquals(3, result);

    }

    /** UnsuspendMember method tests MOCKITO */


    @Test
    public void testUnsuspendMember_valid() {
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        LocalDate endDate = LocalDate.of(2024, 3, 16);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.isCurrentlySuspended(123)).thenReturn(true);
        when(mockDatabaseManager.getSuspensionEndDate(123)).thenReturn(endDate);
        int result = library.unsuspendMember(123);
        assertEquals(3, result);
    }
    @Test
    public void testUnsuspendMember_notValidBcsDateOfSuspension() {
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        LocalDate endDate = LocalDate.of(2024, 3, 25);

        when(mockDatabaseManager.isMember(123)).thenReturn(true);
        when(mockDatabaseManager.isCurrentlySuspended(123)).thenReturn(true);
        when(mockDatabaseManager.getSuspensionEndDate(123)).thenReturn(endDate);
        int result = library.unsuspendMember(123);
        assertEquals(4, result);
    }



}



