package com.keridano.abooks.model;

import java.util.List;

/**
 * @author enrico.sallusti
 */
public class SaleInfo {

    private String      country;
    private String      saleability;
    private boolean     isEbook;
    private Price       listPrice;
    private Price       retailPrice;
    private String      buyLink;
    private List<Offer> offers;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSaleability() {
        return saleability;
    }

    public void setSaleability(String saleability) {
        this.saleability = saleability;
    }

    public boolean isEbook() {
        return isEbook;
    }

    public void setEbook(boolean ebook) {
        isEbook = ebook;
    }

    public Price getListPrice() {
        return listPrice;
    }

    public void setListPrice(Price listPrice) {
        this.listPrice = listPrice;
    }

    public Price getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Price retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

}
