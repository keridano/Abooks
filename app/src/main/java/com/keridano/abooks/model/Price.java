package com.keridano.abooks.model;

/**
 * @author enrico.sallusti
 */
class Price {

    private double  amount;
    private long    amountInMicros;
    private String  currencyCode;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getAmountInMicros() {
        return amountInMicros;
    }

    public void setAmountInMicros(long amountInMicros) {
        this.amountInMicros = amountInMicros;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
