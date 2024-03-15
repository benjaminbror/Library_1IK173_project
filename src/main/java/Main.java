
import java.time.LocalDate;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {
    private static Logger logger = LogManager.getLogger("Main");
    private Library library;

        public Main(Library library) {
            this.library = library;
        }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        logger.info("Starting");

        DatabaseManager databaseManager = new DatabaseManager();
        Library library = new Library(databaseManager);


        while (true){

            //

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
                    System.out.println("Enter the title of the book you want to borrow: ");
                    System.out.println("Enter memberID: ");

                    //Loan
                    break;
                case 2:
                    //Return
                    

                    break;
                case 3:
                    Scanner registrationInput = new Scanner(System.in);

                    String fname = "";
                    boolean validFName = false;

                    while (!validFName) {
                        System.out.println("Enter your first name:");
                            fname = registrationInput.nextLine();
                            if (fname.matches("[a-zA-Z]+") || fname.length() <= 2) {
                                validFName = true;
                            } else {
                                System.out.println("\u001B[31mEnter a valid firstname! \u001B[0m");
                                System.out.println("------------------------------");
                            }
                    }

                    String lname = "";
                    boolean validLName = false;

                    while (!validLName){
                        System.out.println("Enter your last name: ");
                            lname = registrationInput.nextLine();
                            if (lname.matches("[a-zA-Z]+") || lname.length() <= 2) {
                                validLName = true;
                            } else{
                                System.out.println("\u001B[31mEnter a valid lastname! \u001B[0m");
                                System.out.println("------------------------------");
                            }
                    }

                    long pnr = 0;
                    boolean validPnr = false;

                    while (!validPnr){
                        System.out.println("Enter your personal number (YYMMDDXXXX): ");
                        String pnrInput = registrationInput.nextLine();
                        if (pnrInput.matches("\\d{6}\\d{4}")) {
                            pnr = Long.parseLong(pnrInput);
                            validPnr = true;
                        }else{
                            System.out.println("\u001B[31mEnter a valid personal number in format (YYMMDDXXXX) \u001B[0m");
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
                            System.out.println("\u001B[31mEnter a valid education level, between 1 - 4! \u001B[0m");
                            System.out.println("------------------------------");
                        }
                    }

                    int generated_id = library.generateMemberID(edulvl);
                    int maxBooks = library.setMemberMaxBooks(edulvl);

                    Member newMember = new Member(fname,lname,pnr, generated_id, maxBooks);
                    library.registerMember(newMember);

                    /*if (library.registerMember(newMember) == 1){
                        System.out.println("\u001B[31mThis member has a suspension/is suspended! \u001B[0m");
                        System.out.println("------------------------------");
                    } else if(library.registerMember(newMember) == 2){
                        System.out.println("\u001B[31mThis member is already registered! \u001B[0m");
                        System.out.println("------------------------------");
                    }else{
                        System.out.println("Added member: " + newMember.getFirstName() + " " + newMember.getLastName() + " with member ID: " + newMember.getMemberID());
                        System.out.println("------------------------------");
                    }*/

                    break;

                case 4:
                    //Suspend
                    Scanner inputSuspension = new Scanner(System.in);

                    System.out.println("Enter memberID you want to suspend: ");
                    int memberId = inputSuspension.nextInt();


                    break;
                case 5:
                    //Delete
                    Scanner inputDeletion = new Scanner(System.in);

                    System.out.println("Enter member id you want to delete: ");
                    int memberID = inputDeletion.nextInt();

                    break;

                case 6:
                    System.out.println("Exiting the program..");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please choose a valid option.");

            }
        }
    }
}
