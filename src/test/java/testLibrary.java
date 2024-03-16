import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class testLibrary {


    /** Junit testing */
    


    /** setMemberMaxBooks method tests */
    @Test
    public void setMemberMaxBooks(){
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        int education_level = 1;
        int result = library.setMemberMaxBooks(education_level);

        int expected = 3;
        assertEquals(expected, result);
    }

    @Test
    public void setMemberMaxBooks_boundary(){
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        int education_level = 5;
        int result = library.setMemberMaxBooks(education_level);

        int expected = 0;
        assertEquals(expected, result);
    }


    @Test
    public void testRegister(){
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        Member newMember = new Member("Test", "Testsson", 123321, 123, 3);
        int result = library.registerMember(newMember);

        assertEquals(3, result);
    }

    @Test
    public void testRegister_existingMember(){
        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);
        Member newMember = new Member("Test", "Testsson", 123321, 123, 3);
        int result = library.registerMember(newMember);

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









}
