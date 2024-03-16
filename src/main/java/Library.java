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
        if (databaseManager.getSuspensionCount(memberId) <= 2) {
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
            return 2;
        } else
            return 3;
    }

    public int loanBook(int memberId, String titel) {
        int isbn = databaseManager.getISBN(titel);
        if (!databaseManager.isMember(memberId)) {
            return 1;
        }
        if (!databaseManager.doesBookExist(isbn)) { //La till denna för att se om boken finns
            return 2;
        }
        if (!databaseManager.isBookAvailable(isbn)) {
            return 3;
        }
        if (!databaseManager.isCurrentlySuspended(memberId)) { //tog bort !databaseManager.isSuspended(memberId) då den använder personnummer
            return 4;
        }
        if (databaseManager.getLoansOfCopy(memberId, isbn) > 0) { //La till denna så man ej kan låna fler än 1 copy av varje bok
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
        if (!databaseManager.isMember(memberId)) { //inte medlem
            return 1;
        }
        if (!databaseManager.doesBookExist(isbn)) { //inte rätt metod, behöver en som kollar om boken finns i systemet
            return 2; //bok finns ej

            //Denna är löst nu med ny metod
        }
        if (databaseManager.isDate15DaysAgo(loanDate)) {
            databaseManager.incrementViolations(memberId);
            databaseManager.returnBook(memberId, isbn);
            databaseManager.decrementCurrentNumBooks(memberId);
            databaseManager.incrementAvailableCopies(isbn);
            if (databaseManager.getNumOfViolations(memberId) > 3) { //La till denna så vi kan printa att member ska suspendas när han har 3 violations
                return 3;
            }
            return 4; //medlem finns, bok finns, men över belånad datum
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
            if (!databaseManager.isMember(result)) { //OBS isMemberID Måste fixa query och metod.
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

    public int unsuspendMember(int memberID) {
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = databaseManager.getSuspensionEndDate(memberID);
        if (currentDate.isEqual(endDate)) {
            databaseManager.resetCurrentSuspension(memberID);
            return 1;
        } else
            return 2;
    }


}
