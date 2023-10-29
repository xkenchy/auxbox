

import com.cyberthentic.*;



import java.io.IOException;
import java.net.URISyntaxException;

public class helloworld2 {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        // Instantiate the Cyberthentic class.
     Cyberthentic cyberthentic = Cyberthentic.getInstance();

        // 3. Call the Cyberthentic receive function to download messages sent to this application.
        try {
            String[] messages = cyberthentic.receive();
            // Display each message.
            for (String message : messages) {
                System.out.println(message);
            }
        } catch (Exception e) {
            // If any exceptions occur during the receiving process, they will be caught and displayed.
            throw new RuntimeException(e);
        }

    }
}
