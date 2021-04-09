package com.cojigae.coji.item;

public class SettingItem {
    private String title;
    private String subtitle;
    private Boolean isSwitchExist;

    public SettingItem(String title, String subtitle, Boolean isSwitchExist) {
        this.title = title;
        this.subtitle = subtitle;
        this.isSwitchExist = isSwitchExist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Boolean getSwitchExist() {
        return isSwitchExist;
    }

    public void setSwitchExist(Boolean switchExist) {
        isSwitchExist = switchExist;
    }
}
