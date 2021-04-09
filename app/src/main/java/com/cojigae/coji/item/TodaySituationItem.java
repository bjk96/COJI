package com.cojigae.coji.item;

public class TodaySituationItem {
    private String type;
    private int value;
    private int difference;

    public TodaySituationItem(String type, int value, int difference){
        this.type = type;
        this.value = value;
        this.difference = difference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }
}
