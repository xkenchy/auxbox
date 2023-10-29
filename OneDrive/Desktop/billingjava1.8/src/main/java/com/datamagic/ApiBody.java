package com.datamagic;

/*
 * Import libraries needed for the class.
 */

import java.time.LocalTime;


/*
 * This class takes the constants, the business message, time of request as parameters
 * and uses them to build a JSON string that will be passed as the
 * body of the Send API.
 * The constants are: the SDK ID (aPId), the location (location), the latest SDK version (lastVersion),
 * the device type (deviceType), the device ID (deviceId), the name of the sender domain (senderDomainName),
 * the ID of the sender domain  (senderDomainId), the time zone (timeZone), the ID of the sender domain agent (senderDomainAgentId),
 * the ID of the recipient domain agent (recipientDomainAgentId).
 * The business message is the message sent by the SDK (businessMsg).
 * The time that the request to send the business message was received (timeOfRequest).
 *
 */

/**
 * Returns a string representation of the ApiBody object.
 * The string contains the values of its attributes.
 *
 */
public class ApiBody {


    /**
     * Instance variables or Properties of the class.
     */
    private final String aPId;
    private final String location;
    private final String lastVersion;
    private final String deviceType;
    private final String deviceId;
    private final String senderDomainName;
    private final String senderDomainId;
    private final String timeZone;
    private final String senderDomainAgentId;
    private final String recipientDomainName;
    private final BusinessMsg businessMsg;
    private final String timeOfRequest;

    /**
     * Construct a ApiBody object with the provided parameters.
     *
     * @param aPId                  Unique identifier for the ApiBody object
     * @param location              Location of the device
     * @param lastVersion           Last version of the data
     * @param deviceType            Type of the device
     * @param deviceId              ID of the device
     * @param senderDomainId        ID of the sending domain
     * @param senderDomainName      Name of the sending domain
     * @param timeZone              Time zone information
     * @param senderDomainAgentId   ID of the sending domain's agent
     * @param recipientDomainName   Name of the recipient domain
     * @param businessMsg           Business message associated with the data
     * @param timeOfRequest         Time when the request was made
     */

    public ApiBody(String aPId,
                   String location,
                   String lastVersion,
                   String deviceType,
                   String deviceId,
                   String senderDomainId,
                   String senderDomainName,
                   String timeZone,
                   String senderDomainAgentId,
                   String recipientDomainName,
                   BusinessMsg businessMsg,
                   String timeOfRequest) {
        this.aPId = aPId;
        this.location = location;
        this.lastVersion = lastVersion;
        this.deviceType = deviceType;
        this.timeOfRequest = timeOfRequest;
        this.senderDomainName = senderDomainName;
        this.timeZone = timeZone;
        this.senderDomainAgentId = senderDomainAgentId;
        this.recipientDomainName = recipientDomainName;
        this.businessMsg = businessMsg;
        this.deviceId = deviceId;
        this.senderDomainId = senderDomainId;
    }
    /**
     *  Generate the JSON string.
     * @return The JSON string representing this ApiBody.
     */
    public String ApiBodyToJson() {
        String sb = "{\"sendParams\":{" +
                "\"aPId\":\"" + this.getaPId() + "\"," +
                "\"location\":\"" + this.getLocation() + "\"," +
                "\"lastVersion\":\"" + this.getLastVersion() + "\"," +
                "\"deviceType\":\"" + this.getDeviceType() + "\"," +
                "\"deviceId\":\"" + this.getDeviceId() + "\"," +
                "\"timeOfRequest\":\"" + this.getTimeOfRequest() + "\"," +
                "\"senderDomainName\":\"" + this.getSenderDomainName() + "\"," +
                "\"senderDomainId\":\"" + this.getSenderDomainId() + "\"," +
                "\"timeZone\":\"" + this.getTimeZone() + "\"," +
                "\"senderDomainAgentId\":\"" + this.getSenderDomainAgentId() + "\"," +
                "\"recipientDomainName\":\"" + this.getRecipientDomainName() + "\"," +
                "\"businessMsg\":" + this.getBusinessMsg().businessMsgToJson() + "}" +
                "}";
        return sb;
    }
    /**
     * The getters and setters of the ApiBody Object
     */
    public String getaPId() {
        return aPId;
    }

    public String getLocation() {
        return location;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getTimeOfRequest() {
        return timeOfRequest;
    }

    public String getSenderDomainName() {
        return senderDomainName;
    }

    public String getSenderDomainId() {
        return senderDomainId;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getSenderDomainAgentId() {
        return senderDomainAgentId;
    }

    public String getRecipientDomainName() {
        return recipientDomainName;
    }

    public BusinessMsg getBusinessMsg() {
        return businessMsg;
    }

    /**
     * Returns a string representation of the ApiBody object.
     * The string contains the values of its attributes.
     * @return A string representation of the ApiBody object.
     */
    @Override
    public String toString() {
        return "sendParams :{" +
                "aPId='" + aPId + '\'' +
                ", location='" + location + '\'' +
                ", lastVersion='" + lastVersion + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", timeOfRequest=" + timeOfRequest +
                ", senderDomainName='" + senderDomainName + '\'' +
                ", senderDomainId='" + senderDomainId + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", senderDomainAgentId='" + senderDomainAgentId + '\'' +
                ", recipientDomainName='" + recipientDomainName + '\'' +
                ", businessMsg=" + businessMsg +
                '}';
    }
}
