package com.keridano.abooks.model;

/**
 * @author enrico.sallusti
 */
class AcsmFile {

    private boolean isAvailable;
    private String acsTokenLink;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getAcsTokenLink() {
        return acsTokenLink;
    }

    public void setAcsTokenLink(String acsTokenLink) {
        this.acsTokenLink = acsTokenLink;
    }

}
