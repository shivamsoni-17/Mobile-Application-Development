package com.dk7aditya.firebaseimagerecognitionthroughml.models;

public class FirebaseUserListItem {

    private String text1;

    private boolean isSelected = false;
    public FirebaseUserListItem(){

    }
    public FirebaseUserListItem(String text1) {

        this.text1 = text1;
    }

    public String getText1() {
        return text1;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public void setText1(String text1) {
        this.text1 = text1;
    }
    public boolean isSelected() {
        return isSelected;
    }
}

