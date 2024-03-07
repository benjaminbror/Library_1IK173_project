import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

public class DatabaseManager {
    private Connection connection;
    public DatabaseManager(){
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library",  "root", "1IK173Lib");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void printBookTitles() {
        try{
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void isMember(int personal_number){
        try{
            String query = "SELECT first_name, last_name FROM members WHERE personal_number = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, personal_number);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("first_name")+ " " + resultSet.getString("last_name"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        } 
    }
    public void isSuspended(int personal_number){
        try{
            String query = "SELECT suspension, suspension_start_date, suspension_end_date, first_name, last_name FROM members WHERE personal_number = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, personal_number);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("first_name")+ " " + resultSet.getString("last_name") + " is Suspended from " + resultSet.getDate("suspension_start_date") + " - " + resultSet.getDate("suspension_end_date"));
            }
        }catch (Exception e){
                e.printStackTrace();
        }
}
/*public static void main(String[] args) {
    DatabaseManager dbManager = new DatabaseManager();
    dbManager.isSuspended(1234);  // replace 1234567890 with the actual personal number
}*/
}

/*+isMember(person nummer)
+isSuspended(person nummer)
+registerMember(Member)

+deleteMember()

+getNumOfViolations()
+suspendMember()

+getTypeOfMember()
+getNumOfLoans()

+returnBook()




+getDateOfLoan()
+getNumOfAvailableBooks()
 */