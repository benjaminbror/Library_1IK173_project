import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;



public class DatabaseManager {
    public void printBookTitles() {

    try {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library", 
        "root", 
        "1IK173Lib");

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("Select * FROM books");

        while (resultSet.next()) {
            System.out.println(resultSet.getString("title"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}