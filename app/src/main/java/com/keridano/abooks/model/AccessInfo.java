package com.keridano.abooks.model;

/**
 * @author enrico.sallusti
 */
class AccessInfo {

    private String      country;
    private String      viewability;
    private boolean     embeddable;
    private boolean     publicDomain;
    private String      textToSpeechPermission;
    private String      webReaderLink;
    private String      accessViewStatus;
    private boolean     quoteSharingAllowed;
    private AcsmFile    epub;
    private AcsmFile    pdf;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getViewability() {
        return viewability;
    }

    public void setViewability(String viewability) {
        this.viewability = viewability;
    }

    public boolean isEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(boolean embeddable) {
        this.embeddable = embeddable;
    }

    public boolean isPublicDomain() {
        return publicDomain;
    }

    public void setPublicDomain(boolean publicDomain) {
        this.publicDomain = publicDomain;
    }

    public String getTextToSpeechPermission() {
        return textToSpeechPermission;
    }

    public void setTextToSpeechPermission(String textToSpeechPermission) {
        this.textToSpeechPermission = textToSpeechPermission;
    }

    public String getWebReaderLink() {
        return webReaderLink;
    }

    public void setWebReaderLink(String webReaderLink) {
        this.webReaderLink = webReaderLink;
    }

    public String getAccessViewStatus() {
        return accessViewStatus;
    }

    public void setAccessViewStatus(String accessViewStatus) {
        this.accessViewStatus = accessViewStatus;
    }

    public boolean isQuoteSharingAllowed() {
        return quoteSharingAllowed;
    }

    public void setQuoteSharingAllowed(boolean quoteSharingAllowed) {
        this.quoteSharingAllowed = quoteSharingAllowed;
    }

    public AcsmFile getEpub() {
        return epub;
    }

    public void setEpub(AcsmFile epub) {
        this.epub = epub;
    }

    public AcsmFile getPdf() {
        return pdf;
    }

    public void setPdf(AcsmFile pdf) {
        this.pdf = pdf;
    }

}
