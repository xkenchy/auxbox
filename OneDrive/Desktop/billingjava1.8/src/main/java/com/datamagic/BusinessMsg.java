package com.datamagic;

/**
 * import java libraries.
 */

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


/**
 * This class creates a business Object
 */
public class BusinessMsg {

    /**
     * generate a random number and append BSMG to it.
     * @return BSMG+randomId.
     */
    public static String generateId() {
        String randomId = UUID.randomUUID().toString();
        return "BSMG" + randomId;
    }
    /**
     * Instance variables or Properties of the class.
     */
    // String time = LocalTime.now().toString();

    String time = Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    private String id = generateId();
    private String to ;
    private String from ;
    private String body;

    /**
     * getters and setters for the businessMsg class.
     */
    public void setId(String id) {
        this.id = id;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public void setFrom() {
        this.from = from;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getId() {
        return id;
    }
    public String getTo() {
        return to;
    }
    public String getFrom() {
        return from;
    }
    public String getBody() {
        return body;
    }
    public String getTime() {
        return time;
    }
    public BusinessMsg(String to, String from, String body) {
        this.to = to;
        this.from = from;
        this.body = body;

    }

    /**
     * Convert business message object into a JSON string.
     * @return A string representation of the BusinessMsg object.
     */
    public String businessMsgToJson() {
        String sb = "{" +
                "\"id\":\"" + this.getId() + "\"," +
                "\"to\":\"" + this.getTo() + "\"," +
                "\"from\":\"" + this.getFrom() + "\"," +
                "\"body\":\"" + this.getBody() + "\"," +
                "\"time\":\"" + this.getTime() + "\"" +
                "}";
        return sb;
    }
    @Override
    public String toString() {
        return "businessMsg{" +
                "id='" + id + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", body='" + body + '\'' +
                ", time=" + time +
                '}';
    }
}

