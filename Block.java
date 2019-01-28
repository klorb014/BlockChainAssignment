import java.util.Random;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

public class Block{
	private int index; // the index of the block in the list
	private Timestamp timestamp; // time at which transaction
 										  // has been processed
	private Transaction transaction; // the transaction object
	private String nonce; // random string (for proof of work)
	private String previousHash; // previous hash (set to "00000" in first block)
	private String hash; // hash of the block (hash of string obtained from previous variables via toString() method)…

	

	/** 
     * Initializes the block object
     * @param int index  
     * @param Timestamp timestamp
     * @param Transaction transaction
     * @param String prevHash
     * @param String nonce
     */
	public Block(int index, Timestamp timestamp, Transaction transaction, String prevHash, String nonce){
		this.index = index;
		this.timestamp = timestamp;
		this.transaction = transaction;
		previousHash = prevHash;
		this.nonce = nonce;
		try{hash = Sha1.hash(toString());}
 		catch(UnsupportedEncodingException e){}
		
	}


	/** 
     * Return the previous hash
     * @return the previous hash of the block
     */
	public String getPreviousHash(){
		return previousHash;
	}

	/** 
     * Return the index of the block
     * @return the index of the block
     */
	public int getIndex(){
		return index;
	}

	/** 
     * Return the previous hash
     * @return the previous hash of the block
     */
	public String getHash(){
		return hash;
	}

	/** 
     * Return the sender
     * @return the sender of the block
     */
	public String getSender(){
		return transaction.getSender();
	}

	/** 
     * Return the receiver
     * @return the receiver of the block
     */
    public String getReceiver(){
        return transaction.getReceiver();
    }

    /** 
     * Return the transaction amount
     * @return the transaction amount of the block
     */
    public int getAmount(){
        return transaction.getAmount();
    }

    /** 
     * Return the nonce
     * @return the nonce of the block
     */
    public String getNonce(){
    	return nonce;
    }

    /** 
     * Return the timestamp
     * @return the timestamp of the block
     */
    public long getTimestamp(){
    	return timestamp.getTime();
    }


    /** 
     * Calulate the proof of work of the block
     */
	public void calculateNonce(){
		
			boolean flag = true;
			Random rand = new Random();

			int length = 0;
			String hash = this.hash;
			String nonce = this.nonce;
			int attempts = 0;

			while (flag){
				nonce = "";
				hash = "";

				for (int i =1; i<length ; i++ ) {
					int num = rand.nextInt(94) + 33;
					nonce+= Character.toString((char)num);
				}

				this.nonce = nonce;
				

				
				try{hash = Sha1.hash(this.toString());}
				catch(UnsupportedEncodingException e){}

				for (int j=0;j<5 ;j++ ) {
					if(hash.charAt(j) != '0') break;
					else if(j==4) flag = false;
				}


				length = (length +1)%18;
				
				attempts++	;	
				
			}
			System.out.println(attempts);
			this.nonce = nonce;
			this.hash = hash;
	}


	/** 
     * Return the string representation of a block
     * @return the string representation
     */
 	public String toString() {
 		return timestamp.toString() + ":" + transaction.toString() + "." + nonce+ previousHash;
	}
}