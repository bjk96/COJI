package com.cojigae.coji.item;

public class WorldSituationItem {

    String nationNm;
    String natDefCnt;
    String natDeathCnt;
    String areaNm;
    String stdDay;

    public WorldSituationItem(String nationNm, String natDefCnt, String natDeathCnt, String areaNm, String stdDay) {

        this.nationNm = nationNm;
        this.natDefCnt = natDefCnt;
        this.natDeathCnt = natDeathCnt;
        this.areaNm = areaNm;
        this.stdDay = stdDay;
    }

    public String getNationNm() { return nationNm; }

    public void setNationNm(String nationNm) { this.nationNm = nationNm; }

    public String getNatDefCnt() {
        return natDefCnt;
    }

    public void setNatDefCnt(String natDefCnt) {
        this.natDefCnt = natDefCnt;
    }

    public String getNatDeathCnt() {
        return natDeathCnt;
    }

    public void setNatDeathCnt(String natDeathCnt) {
        this.natDeathCnt = natDeathCnt;
    }

    public String getAreaNm() {
        return areaNm;
    }

    public void setAreaNm(String areaNm) {
        this.areaNm = areaNm;
    }

    public String getStdDay() {
        return stdDay;
    }

    public void setStdDay(String stdDay) {
        this.stdDay = stdDay;
    }
}