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
}
