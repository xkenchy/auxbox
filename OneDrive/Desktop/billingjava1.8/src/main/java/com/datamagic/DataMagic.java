package com.datamagic;

/*
 * Import Java libraries
 */
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.PatternSyntaxException;

/**
 * The DataMagic class implements the functionality for sending and receiving messages using the DataMagic platform.
 * It includes methods to send messages to recipients and receive messages from the platform.
 * The class also configures and sets up logging with a unique log file for each message sent or received
 * with the timestamp and the document name.
 * The class provides the following key methods:
 * - {@link #send(String[], String)}: Sends a message to one or more recipients using the DataMagic platform.
 * - {@link #receive()}: Downloads messages from the DataMagic platform and processes the API response.
 * - {@link #apiStringToJson(String)}: Converts the API response string into an array of JSON messages.
 * - {@link #getListOfIds(String)}: Parses a JSON list to extract the ids of each message and stores them in an array.
 * - {@link #getListOfMessageNames(String)}: Parses a JSON list to extract the file names of each message and stores them in an array.
 * * This class also provides an exception class:
 *  - {@link LoggingSetupException}: An exception specific to handling errors related to logging setup within the DataMagic class.
 *  Logging setup is performed through:
 * - {@link #setUpLogger()}: Configures logging, including creating a unique log file for each message sent or received with the timestamp and the document name.
 */
public class DataMagic {
    /**
     *  logger instance for logging events and calling the setUpLogger method to set up the logger.
     */
    static final Logger logger = Logger.getLogger(DataMagic.class.getName());
    static {
        try {
            setUpLogger();
        } catch (LoggingSetupException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configures and sets up the logger for logging events. It creates a unique log file
     * for each message sent or received with the timestamp and the document name.
     * The logger is configured with the following settings:
     * - Log file name format: "yyyy-MM-dd-HH-mm-DataMagic.log"
     * - Log level: INFO*
     * @throws LoggingSetupException Thrown if there is an error during logging setup, such as
     *                               IOException, SecurityException, or IllegalArgumentException.
     */
    private static void setUpLogger() throws LoggingSetupException {
        try {
            // Get the current date in the desired format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            String formattedDate = dateFormat.format(new Date());

            // Generate the log file name based on the timestamp and a document name.
            String logFileName = formattedDate + "-DataMagic.log";

            // Create a FileHandler to write log messages to a file.
            FileHandler fileHandler = new FileHandler(logFileName);

            // Set the log level (INFO, WARNING, SEVERE, etc.).
            logger.setLevel(Level.INFO);

            // Set a custom formatter to format log records.
            fileHandler.setFormatter(new SimpleFormatter());

            // Add the FileHandler to the logger.
            logger.addHandler(fileHandler);
        } catch (IOException|SecurityException|IllegalArgumentException e) {
            throw new LoggingSetupException("Error setting up logger: " + e.getMessage(), e);
        }
    }



    /**
     * The send method sends a message to one or more recipients.
     * It takes the following two parameters:
     *   1. recipient - an array that contains the recipient addresses.
     *   2. message - the message being sent.
     * To send a message, the send method does the following:
     *   1. Loads constants from the constants properties file.
     *   2. Creates the body of the API request.
     *   3. Consumes the DataMagic send API.
     * @param recipient the recipient address.
     * @param message the message to be sent.
     * @throws NullPointerException Thrown when an application attempts to use null in a case where an object is required.
     * @throws URISyntaxException Checked exception thrown to indicate that a string could not be parsed as a URI reference.
     */
    protected void send(String[] recipient, String message) throws NullPointerException, URISyntaxException, IOException {

        /*
         * 1. Load constants from the constants properties file.
         */
        Properties dataMagicMap = new Properties();
        try (InputStream is = DataMagic.class.getResourceAsStream("/constant.properties")) {
            if (is != null) {
                dataMagicMap.load(is);
                logger.info("Properties file 'constant.properties' loaded successfully.");
            }
        } catch (IOException e) {
            logger.info("Error loading constant.properties file: " + e.getMessage());
            e.printStackTrace();
        }

        String sender = dataMagicMap.getProperty("address"); // Set the sender address.
        for (String address : recipient) {

            /*
             * 2. Creates the body of the API request using the following:
             *   i. the constants loaded from the constants properties file.
             *  ii. the domain name which is retrieved from the recipient address.
             * iii. the business message to be sent to the recipient.
             *  iv. the timestamp.
             */

            ApiBody data = new ApiBody(
                    dataMagicMap.getProperty("aPId"),
                    dataMagicMap.getProperty("location"),
                    dataMagicMap.getProperty("softwareVersion"),
                    dataMagicMap.getProperty("deviceType"),
                    dataMagicMap.getProperty("deviceId"),
                    dataMagicMap.getProperty("domainId"),
                    dataMagicMap.getProperty("domain"),
                    dataMagicMap.getProperty("timeZone"),
                    dataMagicMap.getProperty("domainAgentId"),
                    // Extract recipient Domain Name from recipient address.
                    (address.split("@")[1]),
                    new BusinessMsg(address, sender, message),
                    //timeOfRequest
                    Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
            );

            // Setting the API key.
            String APIKEY = dataMagicMap.getProperty("ApiKey");
            //Setting the Url
            String urlSend = dataMagicMap.getProperty("urlSend");
            // Setting the contentType
            String contentType = dataMagicMap.getProperty("contentType");

            /*
             * Call the DataMagic Send API to send the message.
             * If the API call is successful, print the success response from the API, else print the exception thrown by the API.
             */
            HttpURLConnection connection = null;
            int responseCode = 0;
            StringBuilder response = null;
            try {
                URI uri = new URI(urlSend);
                connection = (HttpURLConnection) uri.toURL().openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", contentType);
                connection.setRequestProperty("x-api-key", APIKEY);
                connection.setDoOutput(true);

                // Write the JSON payload to the request
                String jsonPayload = data.ApiBodyToJson();
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(input, 0, input.length);
                }
                response = new StringBuilder();
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                     BufferedReader bufferedReader = new BufferedReader(reader)) {
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                }
                // Get the response code.
                responseCode = connection.getResponseCode();

                // Handle different status codes and log the information.
                handleApiResponse(responseCode, response);

            } catch (IOException e) {
                // Log the exception
                logger.info("Exception during API call :" + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                    // Get the response code.
                    responseCode = connection.getResponseCode();
                }
                if (responseCode > 300) {
                    // Handle different status codes above 300 and log the information.
                    handleApiResponse(responseCode, response);
                }
            }

        }
    }

    /**
     * This method downloads messages from the DataMagic platform by doing the following:
     * 1. Load the query parameters from the constants file.
     * 2. Call the receive API.
     * 3. Convert the receive API response from string format to a JSON format.
     * 4. Return an array of messages in JSON format .
     * @return messages.
     */

    protected String[] receive() throws IOException {
        String[] messages = null;
        String first = "";
        String last = "";
        // StringBuilder to store the response from the API
        StringBuilder response = new StringBuilder();
        // Declare an HttpURLConnection object to establish a connection
        HttpURLConnection connection = null;
        // Initialize responseCode to track the HTTP response code
        int responseCode = 0;
        try {
            /*
             * 1. Load constants from the constants properties file and log and handle any exception.
             */
            Properties dataMagicMapReceive = new Properties();
            try (InputStream isR = DataMagic.class.getResourceAsStream("/constant.properties")) {
                if (isR != null) {
                    dataMagicMapReceive.load(isR);
                    logger.info("Properties file 'constant.properties' loaded successfully.");
                }
            } catch (IOException e) {
                logger.info("Error loading constant.properties file: " + e.getMessage());
            }

            //Assign the loaded parameters to their corresponding fields.
            String aPId = dataMagicMapReceive.getProperty("aPId");
            String location = dataMagicMapReceive.getProperty("location").replace(" ", "");
            String deviceId = dataMagicMapReceive.getProperty("deviceId");
            String deviceType = dataMagicMapReceive.getProperty("deviceType");
            String lastVersion = dataMagicMapReceive.getProperty("softwareVersion");
            String timeOfRequest = LocalTime.now().toString();
            String baseUrl = dataMagicMapReceive.getProperty("baseUrl");


            //Construct the URL for the receive API by formatting the query parameters.
            String url = String.format("%s?aPId=%s&location=%s&deviceId=%s&deviceType=%s&lastVersion=%s&timeOfRequest=%s&first=%s&last=%s",
                    baseUrl, aPId, location, deviceId, deviceType, lastVersion, timeOfRequest, first, last);


            /*
             * Call the DataMagic receive API to receive the message.
             * If the message is successfully received, store the API response in the `savedResponse` variable.
             * Otherwise, print the Exception thrown by the API.
             */
            URL apiUrl = new URI(url).toURL();
            connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                String savedResponse = response.toString();

                // get and save response code in variable.
                responseCode = connection.getResponseCode();

                // Handle different status codes and log the information.
                handleApiResponse(responseCode, response);

                /*
                 * Convert the API response message (savedResponse) to a list of messages in JSON format
                 * by calling the `apiStringToJson` method.
                 */
                messages = apiStringToJson(savedResponse);


                // Retrieve all the names of the downloaded messages.

                String[] messageNames = getListOfMessageNames(Arrays.toString(messages));

                if (messageNames.length > 0) {
                    first = messageNames[0];
                    last = messageNames[messageNames.length - 1];

                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = messages[i].replaceAll("fileName[^}]+", "").replace(",\"}", "}");
                    }


                    //Construct a new URL for the second API call with updated values of `first` and `last`.
                    String secondUrl = String.format("%s?aPId=%s&location=%s&deviceId=%s&deviceType=%s&lastVersion=%s&timeOfRequest=%s&first=%s&last=%s",
                            baseUrl, aPId, location, deviceId, deviceType, lastVersion, timeOfRequest, first, last);


                    // Call the receive API to delete the messages.
                    URL secondApiUrl = new URI(secondUrl).toURL();
                    HttpURLConnection secondConnection = (HttpURLConnection) secondApiUrl.openConnection();
                    secondConnection.setRequestMethod("GET");

                    try (BufferedReader readerWithFileName = new BufferedReader(new InputStreamReader(secondConnection.getInputStream()))) {
                        StringBuilder responseWithFileName = new StringBuilder();
                        String lineWithFileName;
                        while ((lineWithFileName = readerWithFileName.readLine()) != null) {
                            responseWithFileName.append(lineWithFileName);
                        }
                        // get and save response code in variable.
                        responseCode = connection.getResponseCode();

                        // Handle different status codes and log the information.
                        handleApiResponse(responseCode, responseWithFileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // returns the list of messages.
            return messages;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
                // Get the response code.
                responseCode = connection.getResponseCode();
            }
            if (responseCode > 300) {
                // Handle different status codes above 300 and log the information.
                handleApiResponse(responseCode, response);
            }
        }
    }

    /**
     * This method prints appropriate messages based on the response code to indicate
     * the success or failure of the API call, along with the response code and body.
     * Handles the API response based on the provided response code.
     * @param responseCode The HTTP response code received from the API call.
     * @param response     The response body received from the API call.
     */
    private void handleApiResponse(int responseCode, StringBuilder response) {
        if (responseCode == 200) {
            // Handle success response
            logger.info("API call was successful.");
        } else if (responseCode == 400) {
            // Handle bad request
            logger.warning("Bad request.");
        } else if (responseCode == 401) {
            // Handle unauthorized
            logger.warning("Unauthorized.");
        }else if (responseCode == 403) {
            // Handle forbidden
            logger.warning("Forbidden - The server understood the request, but the client lacks the required permissions.");
        }else {
            // Handle other status codes
            logger.warning("Received unexpected response.");
        }

        // Log common response information
        logger.info("Response Code: " + responseCode);
        logger.info("Response Body: " + response);
    }


    /**
     * This method takes a string of messages from the receive api response and converts it to an array of JSON messages.
     * @param apiString The API response string to be processed.
     * @return An array of messages in JSON format.
     * @throws URISyntaxException If the API string contains an illegal URI syntax.
     * @throws IOException If an I/O error occurs during string processing.
     * @throws InterruptedException If the thread is interrupted during string processing.
     */
    String[] apiStringToJson(String apiString) throws URISyntaxException, IOException, InterruptedException {
        try {
            if (apiString != null && !apiString.isEmpty()) {

                //remove the unwanted part of the apiString to form a JSON using replace function.
                apiString = apiString.replace("\\\\", "").replace("\\\"", "\"")
                        .replace("\"{", "{").replace("}\"", "}}")
                        .replace("[", "").replace("]", "")
                        .replace("{\"statusCode\":200,\"body\":\"", "")
                        .replace(" \"statusCode\": 200,", "").replace("    \"body\": \"", "")
                        .replace("}\"}", "").replace("\n", "")
                        .replace("+", "").replace("}\"", "");

                // Splits the  modified apiString into an Array of messages and assigns it to apiStringToArray.
                String[] apiStringToAnArray = apiString.split("},");
                // return the apiStringToArray
                return apiStringToAnArray;
            }
        } catch (PatternSyntaxException e) {
            logger.info("You have a syntax error: ");
            throw new RuntimeException(e);
        }
        // Return an empty array
        return new String[0];
    }

    /***
     * This method parses a JSON list to get the ids of each message and stores them in a List.
     * @param json JSON string.
     * @return idList.
     */
    String[] getListOfIds(String json) {
        // Initializes a List called ids.
        List<String> ids = new ArrayList<>();

        if (json != null && !json.isEmpty()) {
            // Assuming the JSON is an array of objects represented as strings in the input.
            // We will split the input string into individual object strings and process them.
            String[] keyValuePairs = json.replace("[","").replace("]","").split("},");
            for (String pair : keyValuePairs) {
                // Since the JSON array might contain empty elements, we need to handle them.
                if (pair.trim().isEmpty()) {
                    continue;
                }

                // The current pair may contain "id" and other key-value pairs.
                // Split the pair into individual key-value entries.
                String[] entry = pair.split(",");

                // Iterate through the key-value entries to find the "id" key and its value.
                for (String keyValue : entry) {
                    String[] keyValueArray = keyValue.split(":");
                    String key = keyValueArray[0].trim().replace("\"", "").replace("{","");
                    if (key.equals("id")) {
                        String value = keyValueArray[1].trim().replace("\"", "").replace("}","");
                        ids.add(value);
                        // No need to continue searching once we find the "id" key.
                        break;
                    }
                }
            }
        }
        // Return the ids
        return ids.toArray(new String[0]);
    }

    /***
     * This method parses a JSON list to get the fileNames of each message and stores them in a List.
     * @param json input.
     * @return fileNameList
     */
    String[] getListOfMessageNames(String json) {

        // Initializes a List called fileNames.
        List<String> fileNames = new ArrayList<>();

        if (json != null && !json.isEmpty()) {
            // Assuming the JSON is an array of objects represented as strings in the input.
            // We will split the input string into individual object strings and process them.
            String[] keyValuePairs = json.replace("[","").replace("]","").split("},");
            for (String pair : keyValuePairs) {
                // Since the JSON array might contain empty elements, we need to handle them.
                if (pair.trim().isEmpty()) {
                    continue;
                }

                // The current pair may contain "fileNames" and other key-value pairs.
                // Split the pair into individual key-value entries.
                String[] entry = pair.split(",");

                // Iterate through the key-value entries to find the "fileName" key and its value.
                for (String keyValue : entry) {
                    String[] keyValueArray = keyValue.split(":");
                    String key = keyValueArray[0].trim().replace("\"", "").replace("{","");
                    if (key.equals("fileName")) {
                        String value = keyValueArray[1].trim().replace("\"", "").replace("}","");
                        fileNames.add(value);
                        // No need to continue searching once we find the "fileName" key.
                        break;
                    }
                }
            }
        }
        // return the filenames.
        return fileNames.toArray(new String[0]);
    }


    /**
     * An exception class specific to handling errors related to logging setup within the DataMagic class.
     * This exception is thrown when there is an issue setting up logging for messages sent or received.
     */
    private static class LoggingSetupException extends Exception {

        /**
         * Constructs a LoggingSetupException with the specified detail message and the cause of the exception.
         * @param exceptionMessage The detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
         * @param cause The cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).
         */
        public LoggingSetupException(String exceptionMessage, Exception cause) {
            super(exceptionMessage, cause);
        }
    }

    private static String decryptValue(String encryptedPropertyValue, SecretKeySpec secretKey) throws GeneralSecurityException, IOException {
        String iv = encryptedPropertyValue.split(":")[0];
        String property = encryptedPropertyValue.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    /**
     * @param property
     * @return byte[]
     * @throws IOException
     */
    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }

    private static SecretKeySpec createSecretKey(char[] secretKeyString, byte[] salt, int iterationCount, int keyLength) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec keySpec = new PBEKeySpec(secretKeyString, salt, iterationCount, keyLength);
            SecretKey keyTmp = keyFactory.generateSecret(keySpec);
            return new SecretKeySpec(keyTmp.getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error during key generation: " + e.getMessage(), e);
        }
    }



}
