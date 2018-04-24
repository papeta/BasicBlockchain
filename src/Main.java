import static spark.Spark.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.json.simple.JSONObject;
public class HelloWorld {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		BlockChain blockChain = new BlockChain();
		String nodeIdentifier = UUID.randomUUID().toString().replace("-", "");
		System.out.print(nodeIdentifier);

		/*
		 * Post request for new transactions. 
		 * Example:
		 * http://localhost:4567/transactions/new?sender=Satoshi&recipient=RayKurzweil&amount=10'
		 */
		post("/transactions/new", (request, response) -> {
			//Get the request paramenters
			String sender = request.queryParams("sender");
			String recipient = request.queryParams("recipient");
			String amount = request.queryParams("amount");

			//Create a new transaction
			int index = blockChain.newTransaction(sender, recipient, amount);

			//Return message
			return "Added transaction to Block "+index+"\n"+
			blockChain.transactions.toJSONString()
			;
		});
		
		/*
		 * Get request for block mine
		 * http://localhost:4567/mine
		 */
		get("/mine", (request, response) -> {		
			JSONObject lastBlock = blockChain.getLastBlock();
			int lastProof = (int) lastBlock.get("proof");
			int proof = blockChain.proofOfWork(lastProof);

			blockChain.newTransaction("0",nodeIdentifier, "1");
			
			String previousHash = blockChain.hash(lastBlock.toJSONString());
			JSONObject block = blockChain.newBlock(proof, previousHash);

			return "New block mined"+ "\n"+
			block.toString();
		});
		
		




		get("/chain", (request, response) -> blockChain.chain.toString());


	}
}
