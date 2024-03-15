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
        if (databaseManager.isSuspended(newMember.getPersonalNumber())) {
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
            databaseManager.resetViolations(memberId);
            return 2;
        } else
        return 3;
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
    public  int setMemberMaxBooks(int educationLevel) {
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

    //registerMember

    //deleteMember GLÖM INTE BORT ATT KALLA PÅ RETURN BOOK

    //suspendMember

    //loanBook

    //returnBook
}
