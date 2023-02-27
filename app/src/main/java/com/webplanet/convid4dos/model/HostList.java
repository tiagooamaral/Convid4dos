package com.webplanet.convid4dos.model;

public class HostList {
    private String hostListId;
    private String hostListName;

    public HostList() {
    }
    public HostList(String hostListId, String hostListName) {
        this.hostListId = hostListId;
        this.hostListName = hostListName;
    }

    // também precisamos de getters, exigência do Firebase.

    public String getHostListId() {
        return hostListId;
    }

    public String getHostListName() {
        return hostListName;
    }
}
