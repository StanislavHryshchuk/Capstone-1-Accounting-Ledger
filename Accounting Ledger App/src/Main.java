import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static DateTimeFormatter formatter;

    public static void main(String[] args) {

        System.out.println("\n-------------------- Welcome to Accounting Ledger Application --------------\n");
        userMenu();


    }
    public static void userMenu(){
        boolean running = true;
        while (running){

            System.out.println("Please select a menu option from 1 - 4.");
            System.out.println("""
                    1. Add Deposit.
                    2. Make Payment(Debit).
                    3. Ledger.
                    4. Exit.""");

            int userChoice = Integer.parseInt(scanner.nextLine());
            switch (userChoice){
                case 1 -> addDeposit();
                case 2 -> makePayment();
                case 3 -> ledgerMenu();
                case 4 -> running = false;
                default -> System.out.println("Invalid input, please select from 1 - 4 option.");
            }
            if(userChoice == 3){
                running = false;
            }
        }
    }

    public static void addDeposit(){
        try (FileWriter fw = new FileWriter("src/transaction.csv",true)){

            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | KK:mm:ss a");
            LocalDate ld = LocalDate.now();
            LocalTime lt = LocalTime.now();
            LocalDateTime ldt = ld.atTime(lt);
            String ldtString = ldt.format(formatter);

            System.out.println("What amount you would like to deposit?");
            double depositAmount = Double.parseDouble(scanner.nextLine());

            System.out.println("Please provide a description of deposit.");
            String depositDescription = scanner.nextLine();
            String dpositId = "D";

            fw.write(ldtString + " | " + depositDescription + " | " + dpositId + " | " + "$" + depositAmount + "\n");

        }catch (IOException | InputMismatchException e){
            System.out.println(e.getMessage());
        }

    }

    public static void makePayment(){
        try (FileWriter fw = new FileWriter("src/transaction.csv",true)){

            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | KK:mm:ss a");
            LocalDate ld = LocalDate.now();
            LocalTime lt = LocalTime.now();
            LocalDateTime ldt = ld.atTime(lt);
            String ldtString = ldt.format(formatter);

            System.out.println("What is the payment amount?");
            double paymentAmount = Double.parseDouble(scanner.nextLine());

            System.out.println("Please provide a description of payment.");
            String paymentDescription = scanner.nextLine();
            String paymentId = "P";

            fw.write(ldtString + " | " + paymentDescription + " | " + paymentId + " | " + "$" + paymentAmount + "(P)\n");

        }catch (IOException | InputMismatchException e){
            System.out.println(e.getMessage());
        }
    }

    public static void ledgerMenu(){
        boolean runningLedger = true;
        while(runningLedger){

            System.out.println("Please select a menu option from 1 - 5.");
            System.out.println("""
                  1. Display all entries.
                  2. Display Deposits.
                  3. Display Payments.
                  4. Show reports.
                  5. Go back to Home menu.""");

            int userLedgerChoice = Integer.parseInt(scanner.nextLine());
            switch (userLedgerChoice){
                case 1 -> ledgerReader();
                case 5 -> userMenu() ;
                default -> System.out.println("Invalid input, please select from 1 - 5 option.");
            }
            if (userLedgerChoice == 5){
                runningLedger = false;
            }
        }
    }

    public static void ledgerReader(){
        try(BufferedReader bf = new BufferedReader(new FileReader("src/transaction.csv"))){
            String line;
            while ((line = bf.readLine())!= null){

                System.out.println(line);
            }
        }catch (IOException | InputMismatchException e){
            System.out.println(e.getMessage());
        }
    }
}