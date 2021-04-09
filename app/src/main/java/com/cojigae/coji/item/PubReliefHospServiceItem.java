package com.cojigae.coji.item;

import android.graphics.drawable.Drawable;

public class PubReliefHospServiceItem {

    private String yadmNm;
    private String sidoNm;
    private String sgguNm;
    private String telno;
    private Drawable mapIcon;

    public PubReliefHospServiceItem(String yadmNm, String sidoNm, String sgguNm, String telno){
        this.yadmNm = yadmNm;
        this.sidoNm = sidoNm;
        this.sgguNm = sgguNm;
        this.telno = telno;
    }

    public String getYadmNm() {
        return yadmNm;
    }

    public void setYadmNm(String yadmNm) {
        this.yadmNm = yadmNm;
    }

    public String getSidoNm() {
        return sidoNm;
    }

    public void setSidoNm(String sidoNm) {
        this.sidoNm = sidoNm;
    }

    public String getSgguNm() {
        return sgguNm;
    }

    public void setSgguNm(String sgguNm) {
        this.sgguNm = sgguNm;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public Drawable getMapIcon() {
        return mapIcon;
    }

    public void setMapIcon(Drawable mapIcon) {
        this.mapIcon = mapIcon;
    }
}
