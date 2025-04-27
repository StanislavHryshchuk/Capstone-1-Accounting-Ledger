import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static String transactionFileName = "src/transaction.csv";
    static String testFileName = "src/test.csv";


    public static void main(String[] args) {

        System.out.println("\n-------------------- Welcome to Accounting Ledger Application --------------");
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
                    addNewTransaction(transactionFileName, newDeposit);
                    break;
                case 2:
                    Transaction newPayment = makePayment();
                    addNewTransaction(transactionFileName,newPayment);
                    break;
                case 3:
                    ledgerMenu();
                    break;
                case 4:
                    running = false;
                    break;
                default: System.out.println("Invalid input, please select from 1 - 4 option.");
            }
        }
    }

    public static void addNewTransaction (String filename, Transaction transaction){
        try (FileWriter fw = new FileWriter(filename,true)){
            fw.write(transaction.toFileString() + "\n");
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static Transaction addDeposit(){

        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        System.out.println("What amount you would like to deposit?");
        double depositAmount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Please provide a description of deposit.");
        String depositDescription = scanner.nextLine();
        String depositId = "D";

        return new Transaction(ld,lt,depositDescription,depositId,depositAmount);
    }

    public static Transaction makePayment(){

        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        System.out.println("What is the payment amount?");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Please provide a description of payment.");
        String paymentDescription = scanner.nextLine();
        String paymentId = "P";

        return new Transaction(ld,lt, paymentDescription,paymentId,paymentAmount);
    }

    public static void ledgerMenu(){
        boolean runningLedger = true;
        while(runningLedger){
            System.out.println("\n------------------------------ Ledger Menu ---------------------------------\n");
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
                case 4:
                    reportsMenu();
                    break;
                case 5:
                    runningLedger = false;
                    break;
                default: System.out.println("Invalid input, please select from 1 - 5 option.");
            }
        }
    }

    public static void displayAllEntries(){
        try(BufferedReader bf = new BufferedReader(new FileReader(transactionFileName))){
            String line;
            while ((line = bf.readLine())!= null){
                System.out.println(line);
            }
        }catch (IOException | InputMismatchException e){
            System.out.println(e.getMessage());
        }
    }

    public static List<Transaction> findTransactionsById(String idOfTransaction){
        List<Transaction> transactions = getTransactionsFromFile(transactionFileName);
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
                Transaction transaction = new Transaction( LocalDate.parse(arrTransaction[0],dateFormatter),LocalTime.parse(arrTransaction[1],timeFormatter),arrTransaction[2],arrTransaction[3],Double.parseDouble(arrTransaction[4]));
                transactions.add(transaction);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    public static void displayTransaction(List<Transaction> transactions){
        for (Transaction transaction : transactions) {
            System.out.printf("%s | %s | %s | %s | %.1f%n", transaction.getDate().format(dateFormatter), transaction.getTime().format(timeFormatter), transaction.getDescription(), transaction.getIdOfTransaction(), transaction.getTransactionAmount());
        }
    }

    public static void reportsMenu (){
        boolean runningReport = true;
        while (runningReport){
            System.out.println("\n---------------------------------- Report Menu ----------------------------------");
            System.out.println("\nPlease select option from 1 - 6.");
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
                    List<Transaction> monthToDate = monthToDate(transactionFileName);
                    addList(testFileName,monthToDate);
                    readTestFile(testFileName);
                    break;
                case 2:
                    List<Transaction> previousMonth = previousMonth(transactionFileName);
                    addList(testFileName,previousMonth);
                    readTestFile(testFileName);
                    break;
                case 3:
                    List<Transaction> yearToDate = yearToDate(transactionFileName);
                    addList(testFileName,yearToDate);
                    readTestFile(testFileName);
                    break;
                case 4:
                    List<Transaction> previousYear = previousYear(transactionFileName);
                    addList(testFileName,previousYear);
                    readTestFile(testFileName);
                    break;
                case 6:
                    runningReport = false;
                    break;
                default:
                    System.out.println("Invalid input, please select from 1- 6 option.");
            }
        }
    }

    public static void readTestFile (String fileName){
        try(BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bf.readLine()) != null){
                System.out.println(line);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void addList (String fileName, List<Transaction> nameOfList){
        try (FileWriter fw = new FileWriter(fileName)){
            for(Transaction transaction: nameOfList){
                fw.write(transaction.toFileString() + "\n");
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static List<Transaction> previousYear(String fileName){
        List<Transaction> previousYear = new ArrayList<>();
        LocalDate todayDate = LocalDate.now();
        LocalDate startOfPreviousYear = todayDate.minusYears(1).withDayOfYear(1);
        LocalDate endOfPreviousYear = startOfPreviousYear.withDayOfYear(startOfPreviousYear.lengthOfYear());

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null){
                String [] arrTransaction = line.split("\\|");

                LocalDate date = LocalDate.parse(arrTransaction[0],dateFormatter);
                LocalTime time = LocalTime.parse(arrTransaction[1],timeFormatter);
                String description = arrTransaction[2];
                String id = arrTransaction[3];
                double amount = Double.parseDouble(arrTransaction[4]);

                if ((date.isEqual(startOfPreviousYear) || date.isAfter(startOfPreviousYear)) && (date.isEqual(endOfPreviousYear) || date.isBefore(endOfPreviousYear))){

                    Transaction transaction = new Transaction(date,time,description,id,amount);

                    previousYear.add(transaction);
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return previousYear;
    }

    public static List<Transaction> yearToDate(String fileName){
        List<Transaction> yearToDate = new ArrayList<>();
        LocalDate todayDate = LocalDate.now();
        LocalDate startOfTheYear = todayDate.withDayOfYear(1);


        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null){
                String [] arrTransaction = line.split("\\|");

                LocalDate date = LocalDate.parse(arrTransaction[0],dateFormatter);
                LocalTime time = LocalTime.parse(arrTransaction[1],timeFormatter);
                String description = arrTransaction[2];
                String id = arrTransaction[3];
                double amount = Double.parseDouble(arrTransaction[4]);

                if((date.isEqual(startOfTheYear) || date.isAfter(startOfTheYear)) && (date.isEqual(todayDate) || date.isBefore(todayDate))){
                    Transaction transaction = new Transaction(date,time,description,id,amount);

                    yearToDate.add(transaction);
                }

            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return yearToDate;
    }

    public static List<Transaction> previousMonth(String fileName){
        List<Transaction> previousMonth = new ArrayList<>();

        LocalDate dateToday = LocalDate.now();

        LocalDate startOfPreviousMonth = dateToday.minusMonths(1).withDayOfMonth(1);

        LocalDate endOfPreviousMonth = startOfPreviousMonth.withDayOfMonth(startOfPreviousMonth.lengthOfMonth());

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null){
                String [] arrTransaction = line.split("\\|");

                LocalDate date = LocalDate.parse(arrTransaction[0],dateFormatter);
                LocalTime time = LocalTime.parse(arrTransaction[1],timeFormatter);
                String description = arrTransaction[2];
                String id = arrTransaction[3];
                double amount = Double.parseDouble(arrTransaction[4]);

                if((date.isEqual(startOfPreviousMonth) || date.isAfter(startOfPreviousMonth)) && (date.isEqual(endOfPreviousMonth) || date.isBefore(endOfPreviousMonth))){

                    Transaction transaction = new Transaction(date,time,description,id,amount);

                    previousMonth.add(transaction);
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return  previousMonth;
    }

    public static List<Transaction> monthToDate(String fileName){
        List<Transaction> monthToDateTransactions = new ArrayList<>();

        LocalDate dateToday = LocalDate.now();
        LocalDate startOfMonth = dateToday.withDayOfMonth(1);

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = br.readLine()) != null){

                String[] arrTransaction = line.split("\\|");

                LocalDate date = LocalDate.parse(arrTransaction[0],dateFormatter);
                LocalTime time = LocalTime.parse(arrTransaction[1],timeFormatter);
                String description = arrTransaction[2];
                String id = arrTransaction[3];
                double amount = Double.parseDouble(arrTransaction[4]);

                if((date.isEqual(startOfMonth) || date.isAfter(startOfMonth)) && ((date.isEqual(dateToday)) || (date.isBefore(dateToday)))){

                    Transaction transaction = new Transaction(date,time,description,id,amount);

                    monthToDateTransactions.add(transaction);
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return monthToDateTransactions;
    }
}