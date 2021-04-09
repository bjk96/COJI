package com.cojigae.coji.item;

public class CoronaSituationKR {

    String guBun;
    int defCnt;
    int deathCnt;
    int incDec;
    String stdDay;

    public CoronaSituationKR(String guBun, int defCnt, int deathCnt, int incDec, String stdDay) {
        this.guBun = guBun;
        this.defCnt = defCnt;
        this.deathCnt = deathCnt;
        this.incDec = incDec;
        this.stdDay = stdDay;
    }

    public String getGuBun() {
        return guBun;
    }

    public void setGuBun(String guBun) {
        this.guBun = guBun;
    }

    public int getDefCnt() {
        return defCnt;
    }

    public void setDefCnt(int defCnt) {
        this.defCnt = defCnt;
    }

    public int getDeathCnt() {
        return deathCnt;
    }

    public void setDeathCnt(int deathCnt) {
        this.deathCnt = deathCnt;
    }

    public int getIncDec() {
        return incDec;
    }

    public void setIncDec(int incDec) {
        this.incDec = incDec;
    }

    public String getStdDay() {
        return stdDay;
    }

    public void setStdDay(String stdDay) {
        this.stdDay = stdDay;
    }
}
