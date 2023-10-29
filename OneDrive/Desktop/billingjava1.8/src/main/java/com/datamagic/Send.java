package com.datamagic;/*
 * 1. Import DataMagic library.
 */

/*
 * 2. Import native Java libraries to do the following:
 *   i. java.io.IOException to handle input/output Exceptions.
 *  ii. java.net.URISyntaxException to ensure that the URI string can be parsed as a URI reference.
 */
import java.io.IOException;
import java.net.URISyntaxException;


public class Send {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        // The list of the addresses where the message is going to be sent.
        String[] recipient = {"billing%grp1@cyberthentic.com"};

        // The message to be sent.
        String message = "This is a message to billing";

        // Instantiate the DataMagic class.
        DataMagic dataMagic = new DataMagic();

        // 3. Call the DataMagic send function.
        try {
            dataMagic.send(recipient, message);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

    }


}