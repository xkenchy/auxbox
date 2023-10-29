

import com.cyberthentic.*;


import java.io.IOException;
import java.net.URISyntaxException;


public class helloworld1{
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		
		 if (args.length < 2) {
            System.out.println("Usage: java Helloworld1 <recipients> <message>");
            return;
        }

        // Extract recipient and message from command-line arguments
        String[] recipient = args[0].split(" "); // Split the space-separated string
        String message = args[1];

        // The list of the addresses where the message is going to be sent.
       // String[] recipient = {"helloworld2%group2@cyberthentic.com"};

        // The message to be sent.
       // String message = "Helloworld!!!!";

        // Instantiate the Cyberthentic class.
        Cyberthentic cyberthentic = Cyberthentic.getInstance();

        // 3. Call the Cyberthentic send function.
        try {
            cyberthentic.send(recipient, message);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }


    }


}