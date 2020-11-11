package com.UEMEnrollment.projectx;

import java.io.Serializable;

public class UserInfo implements Serializable {
    String serialNum;
    String deviceId;
    String deviceAltEmail;
    String miMEI;
    String comment;
    String tradePartners;
    String state;
    String status;
    String username;

    public UserInfo() {}

    public UserInfo(String pSerialNum, String pDeviceId, String pDeviceAltEmail, String pMiMEI, String pComment,
                    String pTradePartners, String pState, String pStatus,   String pUsername) {
        serialNum = pSerialNum;
        deviceId = pDeviceId;
        deviceAltEmail = pDeviceAltEmail;
        miMEI = pMiMEI;
        comment = pComment;
        tradePartners = pTradePartners;
        state = pState;
        status = pStatus;
        username = pUsername;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceAltEmail() {
        return deviceAltEmail;
    }

    public String getMiMEI() {
        return miMEI;
    }

    public String getComment() {
        return comment;
    }

    public String getTradePartners() {
        return tradePartners;
    }

    public String getState() {
        return state;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public void setSerialNum(String pSerialNum) {
        serialNum = pSerialNum;
    }

    public void setDeviceId(String pDeviceId) {
        deviceId = pDeviceId;
    }

    public void setDeviceAltEmail(String pDeviceAltEmail) {
        deviceAltEmail = pDeviceAltEmail;
    }

    public void setMiMEI(String pMiMEI) {
        miMEI = pMiMEI;
    }

    public void setComment(String pComment) {
        comment = pComment;
    }

    public void setTradePartners(String pTradePartners) {
        tradePartners = pTradePartners;
    }

    public void setState(String pState) {
        state = pState;
    }

    public void setStatus(String pStatus) {
        status = pStatus;
    }

    public void setUsername(String pUsername) {
        username = pUsername;
    }
}
