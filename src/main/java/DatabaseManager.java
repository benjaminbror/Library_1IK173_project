import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.transform.Result;

public class DatabaseManager {

    private static Logger logger = LogManager.getLogger("DatabaseManager");

    private Connection connection;

    public DatabaseManager() {
        try {
            //this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library",  "root", "1IK173Lib");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "hejhej123");

        } catch (Exception e) {
            logger.error(e.getMessage() + " Could not connect to database.");
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
            logger.error(e.getMessage() + "could not get member data with personal number: " + personal_number);
        }
        return hasRows;
    }

    public boolean isMember(int memberId) {
        boolean hasRows = false;
        try {
            String query = "SELECT member_id FROM members WHERE member_id = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setLong(1, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                /* System.out.println(resultSet.getString("first_name")+ " " + resultSet.getString("last_name"));*/
                hasRows = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage() + "could not get data about member with memberID: " + memberId);
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
            logger.error("Could not get suspension-data for personal number: " + personal_number);
        }
        return hasRows;
    }

    public void registerMember(Member member) {
        try {
            String query = "INSERT INTO members(first_name, last_name, personal_number, member_id, max_num_books, current_num_books) VALUES(?,?,?,?,?,?)";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, member.getFirstName());
            preparedStatement.setString(2, member.getLastName());
            preparedStatement.setLong(3, member.getPersonalNumber());
            preparedStatement.setInt(4, member.getMemberID());
            preparedStatement.setInt(5, member.getMaxNumOfBooks());
            preparedStatement.setInt(6, member.getCurrentNumOfBooks());

            preparedStatement.executeUpdate();

            logger.info(preparedStatement.toString());
            logger.info(member.getFirstName() + " was registered correctly! ");

        } catch (Exception e) {
            //e.printStackTrace();
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
            logger.error(e.getMessage() + " could not delete member with MemberID: " + memberId);
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
            logger.error(e.getMessage() + " Could not get suspensionCount for member with MemberID: " + memberId);
        }
        return suspensionCount;
    }


    /** Suspend member */
    public int getNumOfViolations(int memberId){
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
            logger.error(e.getMessage() + "could not get number of violations for MemberID: " + memberId);
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
            logger.error(e.getMessage() + "member with memberID: " + memberId + " was not suspended properly.");
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
            logger.error(e.getMessage() + " memberID: " + memberId + " violations not reset properly.");
        }
    }










    /** Loan book */

    public int getISBN(String title){
        int isbn = 0;
        try {
            String query = "SELECT isbn FROM books WHERE title = ?";

            try (PreparedStatement isbnStatement = this.connection.prepareStatement(query)) {
                isbnStatement.setString(1, title);
                try (ResultSet resultSet = isbnStatement.executeQuery()){
                    if (resultSet.next()){
                        isbn = resultSet.getInt("isbn");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage() + " could not get ISBN with title: " + title);
        }
        return isbn;
    }

    public void loanBook(String title, int memberId){
        try {
            String queryIsbn = "SELECT isbn FROM books WHERE title = ?";
            String queryInsertLoan = "INSERT INTO loans (member_id, isbn, loan_date) VALUES (?, ?, ?)";
            try (PreparedStatement isbnStatement = this.connection.prepareStatement(queryIsbn);
                 PreparedStatement loanStatment = this.connection.prepareStatement(queryInsertLoan)){

                isbnStatement.setString(1, title);
                ResultSet resultSet = isbnStatement.executeQuery();

                if (resultSet.next()){
                    int isbn = resultSet.getInt("isbn");
                    loanStatment.setInt(1,memberId);
                    loanStatment.setInt(2, isbn);
                    loanStatment.setDate(3, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));

                    loanStatment.executeUpdate();

                    logger.info(loanStatment.toString());
                    logger.info("Book with title: " + title + " was borrowed successfully by member with memberID: " + memberId);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage() + " could not loan " + title + " for MemberID : " + memberId);
        }
    }

    public int getNumOfLoans(int memberId) {
        int numOfLoans = 0;

        try {
            String query = "SELECT current_num_books FROM members WHERE member_id = ?";

            try (PreparedStatement loansStatment = this.connection.prepareStatement(query)) {
                loansStatment.setInt(1, memberId);
                try (ResultSet resultSet = loansStatment.executeQuery()) {
                    if (resultSet.next()) {
                        numOfLoans = resultSet.getInt("current_num_books");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage() + " could not get number of loans for MemberID : " + memberId);
        }
        return numOfLoans;
    }

    /** Loan book */

    public void returnBook(int memberID, int isbn){
        try {
            String query = "DELETE FROM loans WHERE member_id = ? AND isbn = ?";

            try (PreparedStatement returnStatment = this.connection.prepareStatement(query)){

                returnStatment.setInt(1,memberID);
                returnStatment.setInt(2,isbn);
                returnStatment.executeUpdate();

                logger.info(returnStatment.toString());
                logger.info("Book with " + isbn + " and memberID: " + memberID + " was returned sucessfully");
            }
        } catch (Exception e) {
            logger.error(e.getMessage() + " could not return book for MemberID : " + memberID + " and ISBN: " + isbn);
        }
    }

    public LocalDate getLoanDate(int memberId, int isbn){
        Date firstLoanDate = null;
        LocalDate loanDate = null;


        try {
            String query = "SELECT loan_date FROM loans WHERE member_id = ? AND isbn = ?";

            try (PreparedStatement loanDateStatement = this.connection.prepareStatement(query)){

                loanDateStatement.setInt(1,memberId);
                loanDateStatement.setInt(2,isbn);

                try (ResultSet resultSet = loanDateStatement.executeQuery()) {
                    if (resultSet.next()){
                        firstLoanDate = resultSet.getDate("loan_date");
                        loanDate = firstLoanDate.toLocalDate();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage() + " could not get loanDate for MemberID : " + memberId + " and ISBN: " + isbn);
        }
        return loanDate;
    }

    /** Extra method */

    public boolean isDate15DaysAgo(LocalDate date){
       if (date == null){
           return false;
       }else{
           return LocalDate.now().minusDays(15).isAfter(date);
       }
    }

}





/* 
+getPersonalNumber() int - Denna lär också behövas!


+getMaxNumOfLoans(memberID) int
+isBookAvailable(isbn) boolean
+decrementAvailableCopies(isbn) void
+incrementCurrentNumBooks(memberID) void

+incrementAvailableCopies(isbn) void
+decrementCurrentNumBooks(memberID) void
+incrementViolations(memberID) void
+getDateOfLoan(memberID, isbn)

+is15DaysAgo(date/localdate?) boolean 

 */