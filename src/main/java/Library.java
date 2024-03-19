import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


public class Library {
    private DatabaseManager databaseManager;

    public Library(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public int registerMember(Member newMember) {
        if (databaseManager.isMember(newMember.getPersonalNumber())) {
            return 1;
        }
        if (databaseManager.isCurrentlySuspended(newMember.getMemberID())) { //databaseManager.isSuspended(newMember.getPersonalNumber()
            return 2;
        } else {
            databaseManager.registerMember(newMember);
            return 3;
        }
    }


    public int deleteMember(int memberId) {
        if (!databaseManager.isMember(memberId)) {
            return 1;
        }
        if (databaseManager.getNumOfLoans(memberId) > 0) {
            return 2;
        } else {
            databaseManager.deleteMember(memberId);
            return 3;
        }
    }


    public int deleteSuspendedMember(int memberId) {
        if (!databaseManager.isMember(memberId)) {
            return 1;
        }
        if (databaseManager.getNumOfLoans(memberId) > 0) {
            return 2;
        }
        if (databaseManager.getSuspensionCount(memberId) < 3) {
            return 3;
        } else {
            databaseManager.deleteMember(memberId);
            return 4;
        }
    }

    public int suspendMember(int memberId) {
        if (!databaseManager.isMember(memberId)) {
            return 1;
        }
        if (databaseManager.getNumOfViolations(memberId) > 2) {
            databaseManager.suspendMember(memberId);
            databaseManager.setCurrentlySuspended(memberId);
            databaseManager.resetViolations(memberId);
            if (databaseManager.getSuspensionCount(memberId) >= 3){
                return -1;
            }
            return 2;
        } else
            return 3;
    }

    public int loanBook(int memberId, String titel) {
        int isbn = databaseManager.getISBN(titel);
        if (!databaseManager.isMember(memberId)) {
            return 1;
        }
        if (!databaseManager.doesBookExist(isbn)) {
            return 2;
        }
        if (!databaseManager.isBookAvailable(isbn)) {
            return 3;
        }
        if (databaseManager.isCurrentlySuspended(memberId)) {
            return 4;
        }
        if (databaseManager.getLoansOfCopy(memberId, isbn) > 0) {
            return 5;
        }
        int booksLoaned = databaseManager.getNumOfLoans(memberId);
        int maxBooks = databaseManager.getMaxNumOfLoans(memberId);
        if (booksLoaned >= maxBooks) {
            return 6;
        } else {
            databaseManager.loanBook(titel, memberId);
            databaseManager.decrementAvailableCopies(isbn);
            databaseManager.incrementCurrentNumBooks(memberId);
            return 7;
        }
    }

    public int returnBook(int memberId, int isbn) {
        LocalDate loanDate = databaseManager.getLoanDate(memberId, isbn);

        if (databaseManager.getNumOfLoans(memberId) == 0){
            return -1; //finns inga lån att returnera
        }
        if (!databaseManager.isMember(memberId)) { //inte medlem
            return 1;
        }
        if (!databaseManager.doesBookExist(isbn)) {
            return 2; //bok finns ej

        }
        if (databaseManager.isDate15DaysAgo(loanDate)) {
            databaseManager.incrementViolations(memberId);
            databaseManager.returnBook(memberId, isbn);
            databaseManager.decrementCurrentNumBooks(memberId);
            databaseManager.incrementAvailableCopies(isbn);
            if (databaseManager.getNumOfViolations(memberId) >= 3) {
                return 3;
            }
            return 4; //medlem finns, bok finns, men överbelånat datum
        } else {
            databaseManager.returnBook(memberId, isbn);
            databaseManager.decrementCurrentNumBooks(memberId);
            databaseManager.incrementAvailableCopies(isbn);
            return 5; //vanlig return
        }

    }

    public int generateMemberID(int educationLevel) {

        boolean uniqueID = false;
        int result = 0;
        while (!uniqueID) {
            int randomPart = new Random().nextInt(900) + 100;
            result = Integer.parseInt(educationLevel + "" + randomPart);
            if (!databaseManager.isMember(result)) {
                uniqueID = true;
            }
        }

        return result;
    }

    public int setMemberMaxBooks(int educationLevel) {
        int result = 0;
        switch (educationLevel) {
            case 1:
                result = 3;
                break;
            case 2:
                result = 5;
                break;
            case 3:
                result = 7;
                break;
            case 4:
                result = 10;
                break;
        }
        return result;
    }



    public int unsuspendMember(){
        ArrayList arrayList = databaseManager.getMembersWithSuspension();
        LocalDate currentDate = LocalDate.now();

        if (arrayList.isEmpty()){
            return 1;
        }
        else{
            for (int i = 0; i < arrayList.size(); i++){
                int memberId = (int) arrayList.get(i);
                LocalDate endDate = databaseManager.getSuspensionEndDate(memberId);
                if (currentDate.isEqual(endDate) || currentDate.isAfter(endDate)){
                    databaseManager.resetCurrentSuspension(memberId);
                    return 2;
                }
            }
            return 3;
        }
    }




}
