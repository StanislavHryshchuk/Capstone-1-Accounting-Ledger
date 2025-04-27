import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String stringDate;
    private String stringTime;
    private String description;
    private String idOfTransaction;
    private double transactionAmount;
    private String paymentDescription;
    private String paymentId;



    //Deposit Constructor
    public Transaction (LocalDate date,LocalTime time,String description, String idOfTransaction, double transactionAmount){
        this.date = date;
        this.time = time;
        this.description = description;
        this.transactionAmount = transactionAmount;
        this.idOfTransaction = idOfTransaction;
    }
    public Transaction (LocalTime time,String description, String idOfTransaction, double transactionAmount){
        this.time = time;
        this.description = description;
        this.transactionAmount = transactionAmount;
        this.idOfTransaction = idOfTransaction;
    }

    public String toFileString() {
    return date.format(Main.dateFormatter) + "|" + time.format(Main.timeFormatter) + "|" + description + "|" + idOfTransaction + "|" + transactionAmount;
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
}
