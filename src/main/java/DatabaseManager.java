import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseManager {

    private static Logger logger = LogManager.getLogger("DatabaseManager");

    private Connection connection;
    public DatabaseManager(){
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library",  "root", "1IK173Lib");

        }catch (Exception e) {
            logger.error(e.getMessage());
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
    public boolean isMember(long personal_number){
        boolean hasRows = false;
        try{
            String query = "SELECT first_name, last_name FROM members WHERE personal_number = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, personal_number);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
               /* System.out.println(resultSet.getString("first_name")+ " " + resultSet.getString("last_name"));*/ 
                hasRows = true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } 
        return hasRows;
    }
    public boolean isSuspended(long personal_number){
        boolean hasRows = false;
        try{
            String query = "SELECT suspension, suspension_start_date, suspension_end_date, first_name, last_name FROM members WHERE personal_number = ?";
            
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, personal_number);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                /* System.out.println(resultSet.getString("first_name")+ " " + resultSet.getString("last_name") + " is Suspended from " + resultSet.getDate("suspension_start_date") + " - " + resultSet.getDate("suspension_end_date")); */
                hasRows = true;
            }
        }catch (Exception e){
                e.printStackTrace();
        }
        return hasRows;
}

    public void registerMember (Member member){
    try {
        String query = "INSERT INTO members(first_name, last_name, personal_number, member_id, maxNumOfBooks, currentNumOfBooks) VALUES(?,?,?,?,?,?)";

        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setString(1, member.getFirstName());
        preparedStatement.setString(2, member.getLastName());
        preparedStatement.setLong(3, member.getPersonalNumber());
        preparedStatement.setInt(4, member.getMemberID());
        preparedStatement.setInt(5, member.getMaxNumOfBooks());
        preparedStatement.setInt(6, member.getCurrentNumOfBooks());
        logger.info(preparedStatement.toString());
        preparedStatement.executeUpdate();
        logger.info("Registered member correctly");

    } catch (Exception e) {
        e.printStackTrace();
        String logMsg = String.format("Member was not added properly!. Member id=%d, ExMsg=%s",
                member.getMemberID(), e.getStackTrace());
        logger.error(logMsg);
    }
} 

}
/* 
+getPersonalNumber() int - Denna lär också behövas!

+deleteMember(memberID) void
+getSuspensionCount(memberID) int 

+getNumOfViolations(memberID) int 
+suspendMember(memberID) void
+resetViolations(memberID) void

+getISBN(title)
+getNumOfLoans(memberID) int
+getMaxNumOfLoans(memberID) int
+isBookAvailable(isbn) boolean
+loanBook(memberID, title alternativt isbn??) void
+decrementAvailableCopies(isbn) void
+incrementCurrentNumBooks(memberID) void

+returnBook(memberID, title alternativt isbn??) void 
+incrementAvailableCopies(isbn) void
+decrementCurrentNumBooks(memberID) void
+incrementViolations(memberID) void
+getDateOfLoan(memberID, isbn)

+is15DaysAgo(date/localdate?) boolean 

 */