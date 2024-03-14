import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseManager {

    private static Logger logger = LogManager.getLogger("DatabaseManager");

    private Connection connection;

    public DatabaseManager() {
        try {
            //this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library",  "root", "1IK173Lib");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "hejhej123");

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void printBookTitles() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Register member
     */
    public boolean isMember(long personal_number) {
        boolean hasRows = false;
        try {
            String query = "SELECT first_name, last_name FROM members WHERE personal_number = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, personal_number);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                /* System.out.println(resultSet.getString("first_name")+ " " + resultSet.getString("last_name"));*/
                hasRows = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasRows;
    }

    public boolean isSuspended(long personal_number) {
        boolean hasRows = false;
        try {
            String query = "SELECT suspension, suspension_start_date, suspension_end_date, first_name, last_name FROM members WHERE personal_number = ?";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, personal_number);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                /* System.out.println(resultSet.getString("first_name")+ " " + resultSet.getString("last_name") + " is Suspended from " + resultSet.getDate("suspension_start_date") + " - " + resultSet.getDate("suspension_end_date")); */
                hasRows = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasRows;
    }

    public void registerMember(Member member) {
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

            logger.info(member.getFirstName() + " was registered correctly! ");

        } catch (Exception e) {
            e.printStackTrace();
            String logMsg = String.format("Member was not added properly!. Member id=%d, ExMsg=%s",
                    member.getMemberID(), e.getStackTrace());
            logger.error(logMsg);
        }
    }

    /**
     * Delete member
     */

    public void deleteMember(int memberId) {
        try {
            String queryLoans = "DELETE FROM loans WHERE member_id = ?";
            String queryMembers = "DELETE FROM members where member_id = ?";

            try (PreparedStatement deleteLoansStatement = this.connection.prepareStatement(queryLoans);
                 PreparedStatement deleteMemberStatement = this.connection.prepareStatement(queryMembers)) {

                deleteLoansStatement.setInt(1, memberId);
                deleteLoansStatement.executeUpdate();

                deleteMemberStatement.setInt(1, memberId);
                deleteMemberStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSuspensionCount(int memberId) {
        int suspensionCount = 0;
        try {
            String query = "SELECT suspension FROM members WHERE member_id = ?";

            try (PreparedStatement suspensionStatment = this.connection.prepareStatement(query)) {
                suspensionStatment.setInt(1, memberId);

                try (ResultSet resultSet = suspensionStatment.executeQuery()) {
                    if (resultSet.next()) {
                        suspensionCount = resultSet.getInt("suspension");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suspensionCount;
    }


    /** Suspend member */


    public int getNumOfViolations2(int memberId){
        int violationCount = 0;
        try {
            String query = "SELECT Num_of_violations FROM members WHERE member_id = ?";

            try (PreparedStatement numOfViolationsStatement = this.connection.prepareStatement(query)){
                numOfViolationsStatement.setInt(1, memberId);

                try (ResultSet resultSet = numOfViolationsStatement.executeQuery()){
                    if (resultSet.next()){
                        violationCount = resultSet.getInt("Num_of_violations");
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return violationCount;
    }


    public void suspendMember(int memberId){
        try{
            String query = "UPDATE members SET suspension_start_date = CURRENT_DATE, suspension_end_date = CURRENT_DATE + INTERVAL 15 DAY, suspension = suspension + 1 WHERE member_id = ?";

            try (PreparedStatement suspendStatement = this.connection.prepareStatement(query)){
                suspendStatement.setInt(1, memberId);
                suspendStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetViolations(int memberId){
        try {
            String query = "UPDATE members SET Num_of_violations = 0 WHERE member_id = ?";

            try (PreparedStatement resetStatement = this.connection.prepareStatement(query)){
                resetStatement.setInt(1, memberId);
                resetStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /** Loan book */

    public int getNumOfLoans(int memberId) {
        int numOfLoans = 0;

        try {
            String query = "SELECT currentNumOfBooks FROM members WHERE member_id = ?";

            try (PreparedStatement loansStatment = this.connection.prepareStatement(query)) {
                loansStatment.setInt(1, memberId);
                try (ResultSet resultSet = loansStatment.executeQuery()) {
                    if (resultSet.next()) {
                        numOfLoans = resultSet.getInt("currentNumOfBooks");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numOfLoans;
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