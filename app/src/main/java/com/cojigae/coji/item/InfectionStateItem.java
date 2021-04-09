package com.cojigae.coji.item;

public class InfectionStateItem {
    private String today;
    private String decide;
    private String exam;
    private String clear;
    private String death;

    public InfectionStateItem(String today, String decide, String exam, String clear, String death){
        this.today = today;
        this.decide = decide;
        this.exam = exam;
        this.clear = clear;
        this.death = death;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getDecide() {
        return decide;
    }

    public void setDecide(String decide) {
        this.decide = decide;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public String getClear() {
        return clear;
    }

    public void setClear(String clear) {
        this.clear = clear;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }
}
