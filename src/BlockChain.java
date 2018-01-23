import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class BlockChain {
	
	
	JSONArray transactions = new JSONArray();
	JSONArray chain = new JSONArray();
	JSONObject block;
		
	public BlockChain() throws NoSuchAlgorithmException {
		
		newBlock(1, "100");
	
	}
	
		
	public int newTransaction(String sender, String recipient, String amount) {
		JSONObject currTransaction = new JSONObject();
		currTransaction.put("sender", sender);
		currTransaction.put("recipient", recipient);
		currTransaction.put("amount", amount);
		transactions.add(currTransaction);
		
		return (int) getLastBlock().get("index")+1;
		
	}
	
	public JSONObject newBlock(int proof, String previousHash) {
		block = new JSONObject();
		block.put("index", chain.size());
		block.put("timestamp", new Timestamp(System.currentTimeMillis()));
		block.put("transactions", transactions);
		block.put("proof", proof);
		block.put("previous_hash", previousHash);
		
		//Reset current transactions
		transactions = new JSONArray();
		//Add block to chain
		chain.add(block);
		return block;
		
	}
	public int proofOfWork(int lastProof) throws NoSuchAlgorithmException {
		int proof = 0;
		
		while(validProof(lastProof, proof) == false){		
			proof++;
		}
		
		return proof;
	}
	
	public boolean validProof(int lastProof, int proof) throws NoSuchAlgorithmException {
		String hash = hash(Integer.toString(lastProof*proof)).substring(60, 64);
		System.out.println(hash);
		return hash.equals("0000");
		
	}
	
	
	//SHA-256 hash the block
	public String hash(String block) throws NoSuchAlgorithmException {		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(block.getBytes(StandardCharsets.UTF_8));
		return bytesToHex(hash);
	}
	
	private static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	
	public JSONObject getLastBlock() {
		return (JSONObject) chain.get(chain.size()-1);
	}
}	
