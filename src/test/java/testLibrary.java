import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class testLibrary {

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

    @Test
    public void testRegister_Mockito(){
        DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);
        Library library = new Library(mockDatabaseManager);

        Member newMember = new Member("Test", "Testsson", 123321, 123, 3);

        assertEquals(3,library.registerMember(newMember));
    }


}
