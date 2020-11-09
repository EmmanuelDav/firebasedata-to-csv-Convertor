package com.example.projectx;

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

    public UserInfo(String pSerialNum, String pDeviceId, String pDeviceAltEmail, String pMiMEI, String pComment, String pTradePartners, String pState, String pStatus,   String pUsername) {
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
}
