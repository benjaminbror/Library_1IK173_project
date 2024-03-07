import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        logger.info("Starting");


        while (true){

            System.out.println("1. Register a loan");
            System.out.println("2. Register a return");
            System.out.println("3. Register a new member");
            System.out.println("4. Suspend member");
            System.out.println("5. Delete member");
            System.out.println("6. Exit application");
            System.out.println("========================");
            System.out.println("Choose an option:");

            int option = input.nextInt();

            switch (option){
                case 1:
                
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
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
