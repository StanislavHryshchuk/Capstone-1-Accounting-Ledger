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
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static String transactionFileName = "src/transaction.csv";


    public static void main(String[] args) {

        System.out.println("\n----- Welcome to Accounting Ledger Application -----");
        userMenu();
    }

    public static void userMenu(){
        boolean running = true;
        while (running){
            System.out.println("\n          *** Home Screen *** ");
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
        double depositAmount = 0;
        try {
            depositAmount = Double.parseDouble(scanner.nextLine()) * -1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Defaulting to 0.");
        }

        System.out.println("Please provide a description of deposit: ");
        String depositDescription = scanner.nextLine();

        System.out.println("Please enter Vendor name: ");
        String vendor = scanner.nextLine();

        String depositId = "D";

        return new Transaction(ld,lt,depositDescription,vendor,depositId,depositAmount);
    }

    public static Transaction makePayment(){

        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        System.out.println("What is the payment amount?");
        double paymentAmount = 0;
        try {
            paymentAmount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Defaulting to 0.");
        }

        System.out.println("Please provide a description of payment.");
        String paymentDescription = scanner.nextLine();

        System.out.println("Please enter the Vendor name: ");
        String vendor = scanner.nextLine();
        String paymentId = "P";

        return new Transaction(ld,lt, paymentDescription,vendor,paymentId,paymentAmount);
    }

    public static void ledgerMenu(){
        boolean runningLedger = true;
        while(runningLedger){
            System.out.println("\n          *** Ledger Menu ***");
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
                    List<Transaction> allTransactions = getTransactionsFromFile(transactionFileName);
                    allTransactions.sort(Comparator.comparing(Transaction::getDateTime).reversed());
                    displayTransaction(allTransactions);
                    break;
                case 2 :
                    List<Transaction> depositTransactions = findTransactionsById("D");
                    depositTransactions.sort(Comparator.comparing(Transaction::getDateTime).reversed());
                    displayTransaction(depositTransactions);
                    break;
                case 3:
                    List<Transaction> paymentTransactions = findTransactionsById("P");
                    paymentTransactions.sort(Comparator.comparing(Transaction::getDateTime).reversed());
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

    public static List<Transaction> findTransactionsById(String idOfTransaction){
        List<Transaction> transactions = getTransactionsFromFile(transactionFileName);
        List<Transaction> matchingTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {

            if (transaction.getIdOfTransaction().equalsIgnoreCase(idOfTransaction)) {
                matchingTransactions.add(transaction);
            }
        }
        return matchingTransactions;
    }

    public static List<Transaction> getTransactionsFromFile(String fileName){
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;

            while ((line = br.readLine()) != null){

                String[] arrTransaction = line.split("\\|");

                Transaction transaction = new Transaction(LocalDate.parse(arrTransaction[0],dateFormatter),LocalTime.parse(arrTransaction[1],timeFormatter),arrTransaction[2],arrTransaction[3],arrTransaction[4],Double.parseDouble(arrTransaction[5]));

                transactions.add(transaction);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    public static void displayTransaction(List<Transaction> transactions){

        for (Transaction transaction : transactions) {

            System.out.printf("%s | %s | %s | %s | %s | %.2f%n", transaction.getDate().format(dateFormatter), transaction.getTime().format(timeFormatter), transaction.getDescription(),transaction.getVendor(), transaction.getIdOfTransaction(), transaction.getTransactionAmount());
        }
    }

    public static void reportsMenu (){
        boolean runningReport = true;
        while (runningReport){
            System.out.println("\n          *** Report Menu ***");
            System.out.println("Please select option from 1 - 7.");
            System.out.println("""
                    1. Month to Date.
                    2. Previous Month.
                    3. Year to Date.
                    4. Previous Year
                    5. Search by Vendor
                    6. Custom Search
                    7. Back""");
            int reportUserChoice = Integer.parseInt(scanner.nextLine());
            switch (reportUserChoice){
                case 1:
                    List<Transaction> sortMonthToDate = monthToDate(transactionFileName);
                    sortMonthToDate.sort(Comparator.comparing(Transaction::getDateTime).reversed());
                    displayTransaction(sortMonthToDate);

                    break;
                case 2:
                    List<Transaction> sortPreviousMonth = previousMonth(transactionFileName);
                    sortPreviousMonth.sort(Comparator.comparing(Transaction::getDateTime).reversed());
                    displayTransaction(sortPreviousMonth);
                    break;
                case 3:
                    List<Transaction> sortYearToDate = yearToDate(transactionFileName);
                    sortYearToDate.sort(Comparator.comparing(Transaction::getDateTime).reversed());
                    displayTransaction(sortYearToDate);
                    break;
                case 4:
                    List<Transaction> sortPreviousYear = previousYear(transactionFileName);
                    sortPreviousYear.sort(Comparator.comparing(Transaction::getDateTime).reversed());
                    displayTransaction(sortPreviousYear);
                    break;
                case 5:
                    List<Transaction> sortSearchByVendor = searchByVendor(transactionFileName);
                    sortSearchByVendor.sort(Comparator.comparing(Transaction::getDateTime).reversed());
                    displayTransaction(sortSearchByVendor);
                    break;
                case 6:
                    List<Transaction> customSearch = customSearch(transactionFileName);
                    customSearch.sort(Comparator.comparing(Transaction::getDateTime).reversed());
                    displayTransaction(customSearch);
                    break;
                case 7:
                    runningReport = false;
                    break;
                default:
                    System.out.println("Invalid input, please select from 1 - 6 option.");
            }
        }
    }

    public static List<Transaction> monthToDate(String fileName){
        List<Transaction> transactions = getTransactionsFromFile(fileName);
        List<Transaction> monthToDateTransactions = new ArrayList<>();

        LocalDateTime dateToday = LocalDateTime.now();

        LocalDateTime startOfMonth = dateToday.withDayOfMonth(1);

        for (Transaction transaction: transactions){

            LocalDateTime dt = transaction.getDateTime();

            if((dt.isEqual(startOfMonth) || dt.isAfter(startOfMonth)) && ((dt.isEqual(dateToday)) || (dt.isBefore(dateToday)))){
                monthToDateTransactions.add(transaction);
            }
        }
        return monthToDateTransactions;
    }

    public static List<Transaction> previousMonth(String fileName){

        List<Transaction> transactions = getTransactionsFromFile(fileName);
        List<Transaction> previousMonth = new ArrayList<>();

        LocalDateTime dateToday = LocalDateTime.now();

        LocalDateTime startOfPreviousMonth = dateToday.minusMonths(1).withDayOfMonth(1);

        LocalDateTime endOfPreviousMonth = startOfPreviousMonth.withDayOfMonth(startOfPreviousMonth.getMonth().length(LocalDate.of(startOfPreviousMonth.getYear(),1,1).isLeapYear()));


        for (Transaction transaction: transactions){
            LocalDateTime dt = transaction.getDateTime();

            if((dt.isEqual(startOfPreviousMonth) || dt.isAfter(startOfPreviousMonth)) && (dt.isEqual(endOfPreviousMonth) || dt.isBefore(endOfPreviousMonth))){

                previousMonth.add(transaction);
            }
        }
        return  previousMonth;
    }

    public static List<Transaction> yearToDate(String fileName){

        List<Transaction> transactions = getTransactionsFromFile(fileName);
        List<Transaction> yearToDate = new ArrayList<>();

        LocalDateTime todayDate = LocalDateTime.now();
        LocalDateTime startOfTheYear = todayDate.withDayOfYear(1);

        for (Transaction transaction: transactions){
            LocalDateTime dt = transaction.getDateTime();

            if((dt.isEqual(startOfTheYear) || dt.isAfter(startOfTheYear)) && (dt.isEqual(todayDate) || dt.isBefore(todayDate))){

                yearToDate.add(transaction);
            }
        }
        return yearToDate;
    }

    public static List<Transaction> previousYear(String fileName){
        List<Transaction> transactions = getTransactionsFromFile(fileName);

        List<Transaction> previousYear = new ArrayList<>();

        LocalDateTime todayDate = LocalDateTime.now();
        LocalDateTime startOfPreviousYear = todayDate.minusYears(1).withDayOfYear(1);

        LocalDateTime endOfPreviousYear = LocalDateTime.of(startOfPreviousYear.getYear(),12,31,23,59,59);

        for (Transaction transaction: transactions){

            LocalDateTime dt = transaction.getDateTime();

            if ((dt.isEqual(startOfPreviousYear) || dt.isAfter(startOfPreviousYear)) && (dt.isEqual(endOfPreviousYear) || dt.isBefore(endOfPreviousYear))){

                previousYear.add(transaction);
            }
        }
        return previousYear;
    }

    public static List<Transaction> searchByVendor (String fileName){

        List<Transaction> transactions = getTransactionsFromFile(fileName);
        List<Transaction> searchByVendor = new ArrayList<>();

        System.out.println("Please enter the Vendor: ");
        String userEnterVendor = scanner.nextLine();

        for (Transaction transaction: transactions){
            if(transaction.getVendor().equalsIgnoreCase(userEnterVendor)){

                searchByVendor.add(transaction);
            }
        }
        return searchByVendor;
    }

    public static List<Transaction> customSearch (String fileName) {

        List<Transaction> transactions = getTransactionsFromFile(fileName);

        System.out.println("Please enter the information for the following fields: Start Date,End Date, Description,Vendor,Id of Transaction,Amount\nPress 'Enter' button to skip");

        System.out.println("Please enter the Start Date in following format: yyyy-MM-dd");
        String userDate = scanner.nextLine().trim();
        if (!userDate.isBlank()) {
            LocalDate userStartDate = LocalDate.parse(userDate);
            transactions = filterByStartDate(transactions,userStartDate);
        }

        System.out.println("Please enter the End Date in following format: yyyy-MM-dd");
        String userDate2 = scanner.nextLine().trim();
        //reformatingDateFormat(userDate2);
        if (!userDate2.isBlank()){
            LocalDate userEndDate = LocalDate.parse(userDate2);
            transactions = filterByEndDate(transactions,userEndDate);
        }

        System.out.println("Please enter the Description:");
        String userDescription = scanner.nextLine().trim();
        if(!userDescription.isBlank()){
            transactions = filterByDescription(transactions,userDescription);
        }

        System.out.println("Please enter the Vendors name:");
        String userVendor = scanner.nextLine().trim();
        if (!userVendor.isBlank()){
            transactions = filterByVendor(transactions,userVendor);
        }

        System.out.println("Please enter the Id of Transaction: (D | P)");
        String userIdOfTransaction = scanner.nextLine().trim();
        if (!userIdOfTransaction.isBlank()){
            transactions = filterByID(transactions,userIdOfTransaction);
        }

        System.out.println("Please enter the Amount of Transaction:");
        String userAmountEntry = scanner.nextLine();
        if(!userAmountEntry.isBlank()) {
            double userAmount = Double.parseDouble(userAmountEntry);
            transactions = filterByAmount(transactions, userAmount);
        }

        return transactions;
    }

    public static List<Transaction> filterByStartDate(List<Transaction> transactions, LocalDate userStartDate){
        List<Transaction> matchingTransactions = new ArrayList<>();

        for(Transaction transaction:transactions){
            if (transaction.getDate().isAfter(userStartDate) || transaction.getDate().isEqual(userStartDate)){
                matchingTransactions.add(transaction);
            }
        }
        return matchingTransactions;
    }

    public static List<Transaction> filterByEndDate(List<Transaction> transactions, LocalDate userEndDate){
        List<Transaction> matchingTransactions = new ArrayList<>();

        for(Transaction transaction:transactions){
            if(transaction.getDate().isBefore(userEndDate) || transaction.getDate().isEqual(userEndDate)){
                matchingTransactions.add(transaction);
            }
        }
        return matchingTransactions;
    }

    public static List<Transaction> filterByDescription(List<Transaction> transactions, String userDescription){
        List<Transaction> matchingTransactions = new ArrayList<>();

        for(Transaction transaction:transactions){
            if(transaction.getDescription().equalsIgnoreCase(userDescription)){
                matchingTransactions.add(transaction);
            }
        }
        return matchingTransactions;
    }

    public static List<Transaction> filterByVendor(List<Transaction> transactions, String userVendor){
        List<Transaction> matchingTransactions = new ArrayList<>();

        for(Transaction transaction:transactions){
            if(transaction.getVendor().equalsIgnoreCase(userVendor)){
                matchingTransactions.add(transaction);
            }
        }
        return matchingTransactions;
    }

    public static List<Transaction> filterByID(List<Transaction> transactions, String userIdOfTransaction){
        List<Transaction> matchingTransactions = new ArrayList<>();

        for(Transaction transaction:transactions){
            if(transaction.getIdOfTransaction().equalsIgnoreCase(userIdOfTransaction)){
                matchingTransactions.add(transaction);
            }
        }
        return matchingTransactions;
    }

    public static List<Transaction> filterByAmount(List<Transaction> transactions, double userAmount){
        List<Transaction> matchingTransactions = new ArrayList<>();

        for(Transaction transaction:transactions){
            if(transaction.getTransactionAmount() == userAmount ){
                matchingTransactions.add(transaction);
            }
        }
        return matchingTransactions;
    }

}