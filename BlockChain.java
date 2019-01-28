import java.io.*; 
import java.util.ArrayList ;
import java.util.Scanner;
import java.sql.Timestamp;
import java.util.HashMap;

public class BlockChain{
	private ArrayList<Block> chain;
	private HashMap<String,Integer> balances;
	
	/** 
     * Initializes the the blockchain and the balances hash map
     */
	public BlockChain(){
		chain  = new ArrayList<Block>();
		balances = new HashMap<String,Integer>();
	}


	/** 
     * Reads a blockchain from a text file and return a blockchain object
     */
	public Block getLast(){
		return chain.get(chain.size() -1);
	}

	public static BlockChain fromFile(String fileName){

		BlockChain blockChain = new BlockChain();
		
		File file = new File(fileName); 

		int block_index = 0;
		

		try{


			
			java.sql.Timestamp timestamp =null;
			String sender = "";
			Transaction transaction;
			Block block;
			int lineNum = 0;

			BufferedReader reader = new BufferedReader(new FileReader(file));
			try{
				String line;
				String receiver = "";
				int amount = 0;
				String nonce="";
				String previousHash = "";
				while((line = reader.readLine()) != null){

					if (lineNum %7 == 0){
						block_index = Integer.parseInt(line);
						if (block_index ==0) previousHash = "00000";
					} 
					else if (lineNum %7 == 1) timestamp = new Timestamp(Long.parseLong(line));
					else if (lineNum %7 == 2) sender = line;
					else if (lineNum %7 == 3) receiver = line;
					else if (lineNum %7 == 4) amount = Integer.parseInt(line);
					else if (lineNum %7 == 5){
						nonce = line;
						transaction = new Transaction(sender,receiver,amount);
						block = new Block(block_index, timestamp, transaction, previousHash, nonce);
					
						blockChain.add(block );

						

					} 
					else if (lineNum %7 == 6) {
						
						previousHash = line;
						
						
					}
					
					lineNum++;

				}
			}catch(IOException e){System.out.println("file not found11");}

		}
		catch (FileNotFoundException ex){
			System.out.println("file not found2");
		}

		return blockChain;	
	}
 	


	/** 
     * writes the blockchain object to a file
     * @param String fileName
     */
 	public void toFile(String fileName){

 		Scanner scan = new Scanner(System.in);
 		boolean access = false;
 		File file =new File(fileName);
 		try{
 			if(file.createNewFile()){
 				access = true;
 				System.out.println("file created");
 			} 
 			else{
 				System.out.println("file already exists\nWould you like to replace it? [Y/N]");
 				String option = scan.nextLine();
 				while(!option.equals("Y") && !option.equals("N")){
 					System.out.println("Would you like to replace it? [Y/N]");
 					option = scan.nextLine();	
 				}
 				if (option.equals("Y")) {
 					file.delete();
 					file.createNewFile();
 					System.out.println("file created");
 					access = true;
 				}

 			}
 		}catch(IOException e){}
 		

 		if(access){
 			try{
 				FileWriter writer = new FileWriter(file, false);

  				PrintWriter print_line = new PrintWriter(writer);

  				for (int i=0; i<chain.size() ; i++ ) {
  					Block block = chain.get(i);
  				
  					print_line.printf("%s",Integer.toString(block.getIndex()));
  					print_line.printf("\n");
  					print_line.printf("%s",Long.toString(block.getTimestamp()));
  					print_line.printf("\n");
  					print_line.printf("%s",block.getSender());
  					print_line.printf("\n");
  					print_line.printf("%s",block.getReceiver());
  					print_line.printf("\n");
  					print_line.printf(Integer.toString(block.getAmount()));
  					print_line.printf("\n");
  					print_line.printf("%s",block.getNonce());
  					print_line.printf("\n");
  					print_line.printf("%s",block.getHash());
  					print_line.printf("\n");
  				
  				}
  				print_line.close();

 			} 
 			catch(IOException ej){System.out.println("file not found4");}
 		}	
 	}

 	public int getBalance(String username){

 		Block block;
 		int balance = 0;
 		String receiver = "";
 		String sender = "";

 		for (int i=0; i<chain.size() ; i++ ) {
 			block = chain.get(i);
 			receiver = block.getReceiver();
 			sender = block.getSender();

 			if (receiver.equals(username)) {
 				balance+= block.getAmount();
 			}
 			if (sender.equals(username)) {
 				balance-= block.getAmount();
 			}
 		}
 		return balance;
 	}



 	/** 
     * Validates the block chain
     * @return boolean result of validation
     */
 	public boolean validateBlockchain(){
 		boolean flag = true;
 		Block block;
 		String prevHash = "00000";
 		String hash = "";
 		String user = "";
 		int amount = 0;
 		Integer newBalance;
 		for (int i= 0; i<chain.size() ; i++ ) {

 			block = chain.get(i);

 			try{hash = Sha1.hash(block.toString());}
 			catch(UnsupportedEncodingException e){}
 			

 			if (!block.getHash().equals(hash))flag = false;
 			if (block.getIndex() != i)flag = false;
 			if (!block.getPreviousHash().equals(prevHash))flag = false; 
 			if(i!=0){ 
 				if (block.getAmount() > balances.get(block.getSender())) {
 					flag = false;
 				}else{
 					newBalance = balances.get(block.getSender()) - block.getAmount();
 					balances.replace(block.getSender(),newBalance);
 					if (!balances.containsKey(block.getReceiver())) balances.put(block.getReceiver(),block.getAmount());
 					else{
 						newBalance = balances.get(block.getReceiver()) + block.getAmount();
 						balances.replace(block.getReceiver(), newBalance);
 					}
 				}  
 			}else{
 				balances.put(block.getSender(),0);
 				user = block.getReceiver();
 				amount = block.getAmount();
 				balances.put(user,amount);
 			}

 			if(!flag) break;

 			prevHash = block.getHash();
 		}
 		
 		balances.clear();
 		return flag;
 	}

 	public void add(Block block){
 		chain.add(block);
 	}


 	public static void main(String[] args) {

 		Scanner scan = new Scanner(System.in);
 		System.out.print("Enter a Blockchain file to read from: ");
 		String file = scan.nextLine();
 		BlockChain chain = BlockChain.fromFile(file);

 		System.out.print("Would you like to verify a blockchain? ( Y for yes, any other key for no) ");
 		String key = scan.nextLine();
 		if(key.equals("Y")){
			if(chain.validateBlockchain()) System.out.println("Valid Blockchain");
			else{System.out.println("Invalid Blockchain");}
		}


 		System.out.print("Would you like to add a transaction ( Y for yes, any other key for no): ");
 		 key = scan.nextLine();

 		while(key.equals("Y")){

 			

 			if(chain.validateBlockchain()){
 				System.out.print("Sender: ");
 				String sender = scan.nextLine();
 				System.out.print("Receiver: ");
 				String receiver = scan.nextLine();
 				System.out.print("Amount: ");
 				String value = scan.nextLine();
 				int amount = Integer.parseInt(value);

 				if (chain.getBalance(sender) >= amount) {
 					Transaction transaction = new Transaction(sender,receiver,amount);

 					//create block
 					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
 					Block block = chain.getLast();
 					String previousHash = block.getHash();
 					int index = block.getIndex() +1;
 					String nonce = "";
 					Block newBlock = new Block(index,timestamp,transaction,previousHash,nonce);
 					newBlock.calculateNonce();
 					chain.add(newBlock);
 				}else {System.out.println("Invalid Transaction");}
 			
 				
 			}else{System.out.println("Invalid BlockChain");}

 			System.out.print("Would you like to add a transaction ( Y for yes, any other key for no): ");
 			key = scan.nextLine();
 			
 		}

 		System.out.print("Would you like to save blockchain? ( Y for yes, any other key for no): ");
 		key = scan.nextLine();
 		if(key.equals("Y")){
 			System.out.print("Input your minerID (UOttawa email): ");
 			String email = scan.nextLine();
 			while(email.length() <8){
 				System.out.print("Input your minerID (UOttawa email): ");
 				email = scan.nextLine();
 			}
 			String minerID="";
 			for (int i=0; i<8 ;i++ ) {
 				minerID+= email.charAt(i);
 			}
 			file = file.substring(0,file.length()-4);
 			file+= "_" + minerID + ".txt";
 			chain.toFile(file);
 		}

 		


 		
 	}
}