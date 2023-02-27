package com.webplanet.convid4dos.model;

public class Guest {
    private String guestID;
    private String guestName;

    private String guestStatus;

    public Guest () {
    }

    public Guest(String guestID, String guestName, String guestStatus) {
        this.guestID = guestID;
        this.guestName = guestName;
        this.guestStatus = guestStatus;
    }

    public String getGuestID() {
        return guestID;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getGuestStatus() { return guestStatus; }

}
