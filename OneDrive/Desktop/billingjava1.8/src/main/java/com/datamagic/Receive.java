/*
 * This program receives a message(s) sent by another application
 * using the DataMagic receive function.
 * To Receive a message, it does the following:
 * 1. Imports the DataMagic SDK.
 * 2. Imports native Java libraries to handle any anticipated exceptions.
 * 3. Calls DataMagic's receive function.
 */
package com.datamagic;

/*
 * 1. Import DataMagic SDK in order to Receive messages from other applications.
 */
import com.datamagic.DataMagic;

/*
 * 2. Import native Java libraries to do the following:
 *   i. java.io.IOException to handle input/output Exceptions.
 *  ii. java.net.URISyntaxException to ensure that the URI string can be parsed as a URI reference.
 */


import java.io.IOException;
import java.net.URISyntaxException;

public class Receive {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        // Instantiate the DataMagic class.
        DataMagic dataMagic = new DataMagic();

        // 3. Call the DataMagic receive function to download messages sent to this application.
        try {
            String[] messages = dataMagic.receive();
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
