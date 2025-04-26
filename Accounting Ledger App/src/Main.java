import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("KK:mm:ss a");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {

        System.out.println("\n-------------------- Welcome to Accounting Ledger Application --------------\n");
        userMenu();


    }

    public static void userMenu(){
        boolean running = true;
        while (running){
            System.out.println("\n--------------------------- Home Screen --------------------------------");
            System.out.println("Please select a menu option from 1 - 4.");
            System.out.println("""
                    1. Add Deposit.
                    2. Make Payment(Debit).
                    3. Ledger.
                    4. Exit.""");

            int userChoice = Integer.parseInt(scanner.nextLine());
            switch (userChoice){
                case 1:
                    Transaction newDeposit = addDeposit();
                    addNewTransaction("src/transaction.csv", newDeposit);
                    break;
                case 2:
                    Transaction newPayment = makePayment();
                    addNewTransaction("src/transaction.csv",newPayment);
                    break;
                case 3:
                    ledgerMenu();
                    break;
                case 4:
                    running = false;
                    break;
                default: System.out.println("Invalid input, please select from 1 - 4 option.");
            }
            if(userChoice == 3){
                running = false;
            }
        }
    }

    public static void addNewTransaction (String filename, Transaction t){
        try (FileWriter fw = new FileWriter(filename,true)){
            fw.write(t.toFileString() + "\n");
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static Transaction addDeposit(){

        LocalDate ld = LocalDate.now();
        ld.format(dateFormatter);

        LocalTime lt = LocalTime.now();
        String formatedLt =  lt.format(timeFormatter);
        LocalTime newTimeFormat = LocalTime.parse(formatedLt,timeFormatter);


        System.out.println("What amount you would like to deposit?");
        double depositAmount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Please provide a description of deposit.");
        String depositDescription = scanner.nextLine();
        String depositId = "D";

        return new Transaction(ld,newTimeFormat,depositDescription,depositId,depositAmount);
    }

    public static Transaction makePayment(){

        LocalDate ld = LocalDate.now();
        ld.format(dateFormatter);

        LocalTime lt = LocalTime.now();
        String formatedLt =  lt.format(timeFormatter);
        LocalTime newTimeFormat = LocalTime.parse(formatedLt,timeFormatter);

        System.out.println("What is the payment amount?");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Please provide a description of payment.");
        String paymentDescription = scanner.nextLine();
        String paymentId = "P";

        return new Transaction(ld,newTimeFormat, paymentDescription,paymentId,paymentAmount);
    }

    public static void ledgerMenu(){
        boolean runningLedger = true;
        while(runningLedger){
            System.out.println("\n------------------------ Ledger Menu -----------------------------\n");
            System.out.println("Please select a menu option from 1 - 5.");
            System.out.println("""
                  1. Display all entries.
                  2. Display Deposits.
                  3. Display Payments.
                  4. Show reports.
                  5. Go back to Home menu.""");

            int userLedgerChoice = Integer.parseInt(scanner.nextLine());
            switch (userLedgerChoice){
                case 1 :
                    displayAllEntries();
                    break;
                case 2 :
                    List<Transaction> depositTransactions = findTransactionsById("D");
                    displayTransaction(depositTransactions);
                    break;
                case 3:
                    List<Transaction> paymentTransactions = findTransactionsById("P");
                    displayTransaction(paymentTransactions);
                    break;
                case 5:
                    userMenu();
                    break;
                default: System.out.println("Invalid input, please select from 1 - 5 option.");
            }
            if (userLedgerChoice == 5){
                runningLedger = false;
            }
        }
    }

    public static void displayAllEntries(){
        try(BufferedReader bf = new BufferedReader(new FileReader("src/transaction.csv"))){
            String line;
            while ((line = bf.readLine())!= null){

                System.out.println(line);
            }
        }catch (IOException | InputMismatchException e){
            System.out.println(e.getMessage());
        }
    }

    public static List<Transaction> findTransactionsById(String idOfTransaction){
        List<Transaction> transactions = getTransactionsFromFile("src/transaction.csv");
        List<Transaction> matchingTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {

            if (transaction.getIdOfTransaction().equals(idOfTransaction)) {
                matchingTransactions.add(transaction);
            }
        }
        return matchingTransactions;
    }

    public static List<Transaction> getTransactionsFromFile(String filename){
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = br.readLine()) != null){
                String[] arrTransaction = line.split("\\|");
                Transaction transaction = new Transaction( LocalDate.parse(arrTransaction[0],dateFormatter),LocalTime.parse(arrTransaction[1]),arrTransaction[2],arrTransaction[3],Double.parseDouble(arrTransaction[4]));
                transactions.add(transaction);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    public static void displayTransaction(List<Transaction> transactions){
        for (Transaction t : transactions) {
            System.out.printf("%s | %s | %s | %s | %.1f%n", t.getDate(), t.getTime(), t.getDescription(), t.getIdOfTransaction(), t.getTransactionAmount());
        }
    }

    public static void reportsMenu (){
        boolean runningReport = true;
        while (runningReport){
            System.out.println("\n---------------------------------- Report Menu ----------------------------------");
            System.out.println("\nPlease select option from 1 - 6\n");
            System.out.println("""
                    1. Month to Date.
                    2. Previous Month.
                    3. Year to Date.
                    4. Previous Year
                    5. Search by Vendor
                    6. Back""");
            int reportUserChoice = Integer.parseInt(scanner.nextLine());
            switch (reportUserChoice){
                case 1:
                    monthToDate("src/transaction.csv");
                    ;

            }
        }
    }

    public static Map<LocalDate,List<Transaction> monthToDate(String filename){
        List<Transaction> transactions = getTransactionsFromFile(filename);
        Map<LocalDate,List<Transaction>> monthToDateTransactions = new HashMap<>();

        for (Transaction transaction: transactions){
            LocalDate date = transaction.getDate();
            monthToDateTransactions.put(date,new ArrayList<>());
            monthToDateTransactions.get(date).add(transaction);
        }
        return monthToDateTransactions;
    }
}