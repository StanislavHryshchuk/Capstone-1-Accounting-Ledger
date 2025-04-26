import java.time.LocalDate;

public class Transaction {
    private String dateTime;
    private String description;
    private String idOfTransaction;
    private double transactionAmount;

    private String paymentDescription;
    private String paymentId;

    //Payment Constructor

    //Deposit Constructor
    public Transaction (String dateTime, String description, String idOfTransaction, double transactionAmount){
        this.dateTime = dateTime;
        this.description = description;
        this.transactionAmount = transactionAmount;
        this.idOfTransaction = idOfTransaction;
    }
//    public Transaction (String description, double amountOfTransaction){
//        this.description = description;
//        this.amountOfTransaction = amountOfTransaction;
//    }
    public String toFileString() {
    return dateTime + " | " + description + " | " + idOfTransaction + " | " + transactionAmount + "$";
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
