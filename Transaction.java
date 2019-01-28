public class Transaction{

    private String sender;
    private String receiver;
    private int amount;


    /** 
     * Initializes a transaction
     * @param String sender  
     * @param String receiver
     * @param int amount
     */
    public Transaction(String sender, String receiver, int amount){
        this.sender= sender;
        this.receiver = receiver;
        this.amount = amount;
    }


    /** 
     * Returns the string representation of the transaction
     * @return String representation of a transaction
     */
    public String toString() {
        return sender + ":" + receiver + "=" + amount;
    }


    /** 
     * returns the sender
     * @return String representation of the sender
     */
    public String getSender(){
        return sender;
    }


    /** 
     * returns the receiver
     * @return String representation of the receiver
     */
    public String getReceiver(){
        return receiver;
    }

    /** 
     * returns the amount
     * @return int  amount
     */
    public int getAmount(){
        return amount;
    }
}