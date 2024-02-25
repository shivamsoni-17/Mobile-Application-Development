package com.dk7aditya.firebaseimagerecognitionthroughml.models;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private String groupDesc;
    private ArrayList<String> finalUsers;
    public Group() {
    }
    public Group(String groupName, String groupDesc, ArrayList<String> finalUsers) {
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.finalUsers = finalUsers;
    }

    public ArrayList<String> getFinalUsers() {
        return finalUsers;
    }

    public void setFinalUsers(ArrayList<String> finalUsers) {
        this.finalUsers = finalUsers;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }
}
