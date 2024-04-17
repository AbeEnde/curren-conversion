package com.itembase.test.currencyconversion.dto;

public class ConversionResponse {
    private String from;
    private String to;
    private Double amount;

    private Double converted;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getConverted() {
        return converted;
    }

    public void setConverted(Double converted) {
        this.converted = converted;
    }
}
