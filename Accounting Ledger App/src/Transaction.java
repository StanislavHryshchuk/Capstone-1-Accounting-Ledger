import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Transaction {
    // Combine
    private LocalDateTime dateTime;
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String idOfTransaction;
    private double transactionAmount;
    private String paymentId;
    private String vendor;



    //Deposit Constructor
    public Transaction (LocalDate date, LocalTime time, String description, String vendor, String idOfTransaction, double transactionAmount){
        this.dateTime = LocalDateTime.of(date,time);
        this.date = date;
        this.time = time;
        this.description = description;
        this.transactionAmount = transactionAmount;
        this.idOfTransaction = idOfTransaction;
        this.vendor = vendor;
    }
    public Transaction (LocalTime time,String description,String vendor ,String idOfTransaction,double transactionAmount){
        this.time = time;
        this.description = description;
        this.transactionAmount = transactionAmount;
        this.idOfTransaction = idOfTransaction;
        this.vendor = vendor;
    }

    public String toFileString() {
    return date.format(Main.dateFormatter) + "|" + time.format(Main.timeFormatter) + "|" + description + "|" + vendor + "|" + idOfTransaction + "|" + transactionAmount;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdOfTransaction() {
        return idOfTransaction;
    }

    public void setIdOfTransaction(String idOfTransaction) {
        this.idOfTransaction = idOfTransaction;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
