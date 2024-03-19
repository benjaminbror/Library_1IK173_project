import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);

        while (true){

            System.out.println("1. Register a loan");
            System.out.println("2. Register a return");
            System.out.println("3. Register a new member");
            System.out.println("4. Suspend member");
            System.out.println("5. Delete member");
            System.out.println("6. Unsuspend member");
            System.out.println("7. Exit application");
            System.out.println("========================");
            System.out.println("Choose an option:");

            int option = input.nextInt();

            switch (option){
                case 1:
                    //Loan
                    Scanner inputLoan = new Scanner(System.in);
                    System.out.println("Enter the title of the book you want to borrow: ");
                    String title = inputLoan.nextLine();
                    System.out.println("Enter memberID: ");
                    int memberID = inputLoan.nextInt();

                    int loanResult = library.loanBook(memberID,title);

                    if (loanResult == 1){
                        System.out.println("\033[0;31mMember not found! \033[0m");
                        System.out.println("------------------------------");
                    } else if (loanResult == 2) {
                        System.out.println("\033[0;33mBook does not exist! \033[0m");
                        System.out.println("------------------------------");
                    } else if (loanResult == 3) {
                        System.out.println("\033[0;33mNot enough copies of this book! \033[0m");
                        System.out.println("------------------------------");
                    } else if (loanResult == 4){
                        System.out.println("\033[0;33mMember is suspended! \033[0m");
                        System.out.println("------------------------------");
                    } else if (loanResult == 5){
                        System.out.println("\033[0;33mMember already has a loan of this copy! \033[0m");
                        System.out.println("------------------------------");
                    } else if (loanResult == 6){
                        System.out.println("\033[0;33mMember has reached the maximum number of loans! \033[0m");
                        System.out.println("------------------------------");
                    } else{
                        LocalDate dateToReturn = LocalDate.now().plusDays(15);
                        System.out.println("\033[0;32mBook with title: " + title + " has been loaned to memberID: " + memberID + " \033[0m");
                        System.out.println("\033[0;32mLatest return date: " + dateToReturn + " \033[0m");
                        System.out.println("------------------------------");
                    }

                    break;
                case 2:
                    //Return
                    Scanner inputReturn = new Scanner(System.in);
                    System.out.println("Enter your memberID: ");
                    int memberId = inputReturn.nextInt();
                    System.out.println("Enter ISBN of the book you want to return: ");
                    int isbn = inputReturn.nextInt();

                    int returnResult = library.returnBook(memberId, isbn);
                    if (returnResult == 1){
                        System.out.println("\033[0;31mMember not found! \033[0m");
                        System.out.println("------------------------------");
                    } else if (returnResult == 2){
                        System.out.println("\033[0;33mBook does not exist \033[0m");
                        System.out.println("------------------------------");
                    } else if (returnResult == 3) {
                        System.out.println("\033[0;33mReturn is delayed! Book with ISBN: " + isbn + " has been returned and violation has been added for memberID: " + memberId + " \033[0m");
                        System.out.println("\033[0;31mViolationcount is 3 for memberID: " + memberId + " please suspend member! \033[0m");
                        System.out.println("------------------------------");
                    } else if (returnResult == 4){
                        System.out.println("\033[0;33mReturn is delayed! Book with ISBN: " + isbn + " has been returned and violation has been added for memberID: " + memberId + " \033[0m");
                        System.out.println("------------------------------");
                    } else if (returnResult == -1){
                        System.out.println("\033[0;33mCannot return book with ISBN: " +isbn + " because no loans were found for member with memberID: " + memberId + " \033[0m");
                        System.out.println("------------------------------");
                    } else {
                        System.out.println("\033[0;32mReturn is on time. Book with ISBN: " + isbn + " has been returned  by member with memberID:  " + memberId + " \033[0m");
                        System.out.println("------------------------------");
                    }


                    break;
                case 3:
                    //Register
                    Scanner registrationInput = new Scanner(System.in);
                    System.out.println("Enter your first name:");
                    String fname = registrationInput.nextLine();
                    System.out.println("Enter your last name: ");
                    String lname = registrationInput.nextLine();

                    long pnr = 0;
                    boolean validPnr = false;

                    while (!validPnr){
                        System.out.println("Enter your personal number (YYMMDDXXXX): ");
                        String pnrInput = registrationInput.nextLine();
                        if (pnrInput.matches("\\d{6}\\d{4}")) {
                            pnr = Long.parseLong(pnrInput);
                            validPnr = true;
                        }else{
                            System.out.println("\033[0;33mEnter a valid personal number in format (YYMMDDXXXX) \033[0m");
                            System.out.println("------------------------------");
                        }
                    }

                    int edulvl = 0;
                    boolean validEducationLevel = false;

                    while(!validEducationLevel){
                        System.out.println("Enter your education level 1-4: (Undergrad / Postgrad / PHD / Teacher) ");
                        int educationLevel = registrationInput.nextInt();
                        if (educationLevel >= 1 && educationLevel <= 4){
                            edulvl = educationLevel;
                            validEducationLevel = true;
                        }else{
                            System.out.println("\033[0;33mEnter a valid education level, between 1 - 4! \033[0m");
                            System.out.println("------------------------------");
                        }
                    }

                    int generated_id = library.generateMemberID(edulvl);
                    int maxBooks = library.setMemberMaxBooks(edulvl);

                    Member newMember = new Member(fname,lname,pnr, generated_id, maxBooks);

                    int resultRegister = library.registerMember(newMember);
                    if (resultRegister == 1){
                        System.out.println("\033[0;31mThis member is already registered! \033[0m");
                        System.out.println("------------------------------");
                    } else if(resultRegister == 2){
                        System.out.println("\033[0;33mThis member has a suspension/is suspended! \033[0m");
                        System.out.println("------------------------------");
                    }else{
                        System.out.println("\033[0;32mRegistered member: " + newMember.getFirstName() + " " + newMember.getLastName()
                                        + " with memberID: " + newMember.getMemberID() + "\033[0m");
                        System.out.println("------------------------------");
                    }

                    break;

                case 4:
                    //Suspend
                    Scanner inputSuspension = new Scanner(System.in);

                    System.out.println("Enter memberID you want to suspend: ");
                    int memberid = inputSuspension.nextInt();

                    int resultSuspend = library.suspendMember(memberid);
                    if (resultSuspend == 1){
                        System.out.println("\033[0;31mMember with memberID: " + memberid + " not found!\033[0m");
                        System.out.println("------------------------------");
                    }else if (resultSuspend == 2){
                        LocalDate dateIn15Days = LocalDate.now().plusDays(15);
                        System.out.println("\033[0;32mMember with memberID: " + memberid + " has been suspended until " + dateIn15Days + "  \033[0m");
                        System.out.println("------------------------------");
                    }else if (resultSuspend == -1){
                        LocalDate dateIn15Days = LocalDate.now().plusDays(15);
                        System.out.println("\033[0;32mMember with memberID: " + memberid + " has been suspended until " + dateIn15Days + "  \033[0m");
                        System.out.println("\033[0;31mMember with: " + memberid + " has reached 3 suspensions, please delete member! \033[0m");
                        System.out.println("------------------------------");
                    } else{
                        System.out.println("\033[0;33mMember with: " + memberid + " does not have more than 2 violations! \033[0m");
                        System.out.println("------------------------------");
                    }


                    break;
                case 5:
                    //Delete
                    Scanner inputDeletion = new Scanner(System.in);
                    System.out.println("1. Member wants to delete account");
                    System.out.println("2. Member has too many suspensions");
                    int option2 = inputDeletion.nextInt();

                    if (option2 == 1){
                        //Member application to delete
                        System.out.println("Enter the memberID you wish to delete: ");
                        System.out.println("MemberID:");
                        int memberIdToDelete = inputDeletion.nextInt();

                        int resultDeleteApplication = library.deleteMember(memberIdToDelete);
                        if (resultDeleteApplication == 1){
                            System.out.println("\033[0;31mMember not found!\033[0m");
                            System.out.println("------------------------------");
                        }else if (resultDeleteApplication == 2){
                            System.out.println("\033[0;33mCannot delete member with memberID: " + memberIdToDelete + " due to an existing loan! \033[0m");
                            System.out.println("------------------------------");
                        }else {
                            System.out.println("\033[0;32mMember with memberID: " + memberIdToDelete + " found and deleted! \033[0m");
                            System.out.println("------------------------------");
                        }

                    }else if (option2 == 2){
                        //Member has too many suspensions
                        System.out.println("Enter a memberID with more than 2 suspensions you wish to delete: ");
                        int memberIdToDelete2 = inputDeletion.nextInt();
                        int resultDelete = library.deleteSuspendedMember(memberIdToDelete2);

                        if (resultDelete == 1){
                            System.out.println("\033[0;31mMember with memberID: " + memberIdToDelete2 + " not found! \033[0m");
                            System.out.println("------------------------------");
                        }else if (resultDelete == 2){
                            System.out.println("\033[0;33mMember with memberID: " + memberIdToDelete2 + " has an existing loan! \033[0m");
                            System.out.println("------------------------------");
                        } else if (resultDelete == 3) {
                            System.out.println("\033[0;33mMember with memberID: " + memberIdToDelete2 + " does not have more than 2 suspensions! \033[0m");
                            System.out.println("------------------------------");
                        } else {
                            System.out.println("\033[0;32mMember with memberID: " + memberIdToDelete2 + " has been deleted \033[0m");
                            System.out.println("------------------------------");
                        }
                    }else{
                        System.out.println("\033[0;31mInvalid option. \033[0m");
                        System.out.println("------------------------------");
                    }
                    break;


                case 6:
                    //Unsuspend member

                    int unsuspendMemberResult = library.unsuspendMember();
                    if (unsuspendMemberResult == 1){
                        System.out.println("\033[0;33mNo members with current suspension found! \033[0m");
                        System.out.println("------------------------------");
                    } else if (unsuspendMemberResult == 2) {
                        System.out.println("\033[0;32mExpired suspensions found and deleted! \033[0m");
                        System.out.println("------------------------------");
                    } else{
                        System.out.println("\033[0;33mNo expired suspensions found! \033[0m");
                        System.out.println("------------------------------");
                    }

                    break;

                case 7:
                    //Exit
                    System.out.println("\033[0;35mExiting the program.. \033[0m");
                    System.exit(0);

                    break;
                default:
                    System.out.println("\033[0;34mPlease choose a valid option. \033[0m");
                    System.out.println("------------------------------");

            }
        }
    }
}
