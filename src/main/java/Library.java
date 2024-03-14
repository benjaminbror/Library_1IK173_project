import java.util.Random;
import java.util.Scanner;

public class Library {
    private DatabaseManager databaseManager;

    public Library(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void registerMember(Member newMember) {
        if (databaseManager.isMember(newMember.getPersonalNumber())) {
            System.out.println("\u001B[31mThis member is already registered!");
            System.out.println("------------------------------");
        }
        if (databaseManager.isSuspended(newMember.getPersonalNumber())) {
            System.out.println("\u001B[31mThis member has a suspension/is suspended!");
        } else {
            databaseManager.registerMember(newMember);
            System.out.println("Added member: " + newMember.getFirstName() + " " + newMember.getLastName() + " with member ID: " + newMember.getMemberID());
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
